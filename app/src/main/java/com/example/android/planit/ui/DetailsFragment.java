package com.example.android.planit.ui;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import com.example.android.planit.Constants;
import com.example.android.planit.R;
import com.example.android.planit.adapters.ReviewsAdapter;
import com.example.android.planit.database.AppDatabase;
import com.example.android.planit.databinding.FragmentDetailsBinding;
import com.example.android.planit.models.BucketList;
import com.example.android.planit.models.BucketListItem;
import com.example.android.planit.models.MyCalendar;
import com.example.android.planit.models.PlaceDetails;
import com.example.android.planit.models.PlaceDetailsResponse;
import com.example.android.planit.models.PointOfInterestPhoto;
import com.example.android.planit.models.PointsOfInterests;
import com.example.android.planit.utils.ApiInterface;
import com.example.android.planit.utils.AppExecutors;
import com.example.android.planit.utils.NetworkUtils;
import com.example.android.planit.utils.SpacesItemDecoration;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CALL_PHONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements DatePickerDialog.OnDateSetListener,
        ReviewsAdapter.ReviewsAdapterOnClickHandler {

    private static final String TAG = DetailsFragment.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100;
    private DatePickerDialog datePickerDialog;
    private final Calendar calendar;
    private int year;
    private int month;
    private int day;
    boolean isFound = false;
//    private String city;
    private String placeId;
    private String poi_name;
    private PlaceDetails placeDetails;
    private String photoRef;
    private int photoWidth;

    private ArrayList<MyCalendar> calendarEntries;
    private ArrayList<BucketList> mBucketLists;
    private ArrayList<String> mBucketListsName;

    private ReviewsAdapter mAdapter;

    /* Retrofit */
    private ApiInterface apiService;

    /*Database*/
    private AppDatabase mDb;

    /* Views */
    private FragmentDetailsBinding binding;
    private ImageView header;
    private RecyclerView recyclerView;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private SpeedDialView speedDialView;

    private boolean isShow = false;
    private int scrollRange = -1;
    private boolean isPause = false;

    public DetailsFragment() {
        // Required empty public constructor
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        //Add one to month {0 - 11}
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBucketLists = new ArrayList<>();
        mBucketListsName = new ArrayList<>();

        if (getArguments() != null
                && getArguments().containsKey(bestThingsTodoFragment.PLACE_ID)
                && getArguments().containsKey(bestThingsTodoFragment.POI_NAME)
                && getArguments().containsKey(bestThingsTodoFragment.PHOTO_REF)
                && getArguments().containsKey(bestThingsTodoFragment.PHOTO_WIDTH)) {

           // city = getArguments().getString(HomeFragment.CITY_NAME);
            placeId = getArguments().getString(bestThingsTodoFragment.PLACE_ID);
            poi_name = getArguments().getString(bestThingsTodoFragment.POI_NAME);
            photoRef = getArguments().getString(bestThingsTodoFragment.PHOTO_REF);
            photoWidth = getArguments().getInt(bestThingsTodoFragment.PHOTO_WIDTH);
        }

        apiService = NetworkUtils.getRetrofitInstance(getActivity()).create(ApiInterface.class);
        mDb = AppDatabase.getMyCalendarDbInstance((getActivity()).getApplicationContext());
        datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

        mAdapter = new ReviewsAdapter(this, getActivity());

//        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
//        postponeEnterTransition();
    }

    @Override
    public void onStart() {
        super.onStart();
        appBarLayout = ((MainActivity) getActivity()).appBarLayout;
        toolbar = ((MainActivity) getActivity()).toolbar;
        ((MainActivity) getContext()).unlockAppBarOpen();
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (scrollRange == -1) {
                scrollRange = appBarLayout.getTotalScrollRange();
            }
            if (!isPause) {
                if ((Math.abs(scrollRange + verticalOffset) < 20)) {
                    Log.d(TAG, "OnStart setting title");
                    toolbar.setTitle("PlanIt");
                    isShow = true;
                } else if (isShow) {
                    Log.d(TAG, "OnStart removing title");
                    toolbar.setTitle("");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause");
        isPause = true;
        speedDialView.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {

            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigateUp();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);

        header = ((MainActivity) getActivity()).imageView;

        binding.addressLayout.setClickable(false);
        binding.addressLayout.setOnClickListener(v -> {
            Uri uri = Uri.parse("geo:0,0?q="+placeDetails.getAddress());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        binding.phoneLayout.setClickable(false);
        binding.phoneLayout.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+placeDetails.getPhoneNumber()));

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);

                // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            } else {
                //You already have permission
                try {
                    startActivity(callIntent);
                } catch(SecurityException e) {
                    e.printStackTrace();
                }
            }
        });

        speedDialView = ((MainActivity) getActivity()).speedDialView;
        speedDialView.setExpansionMode(SpeedDialView.ExpansionMode.BOTTOM);
        speedDialView.setVisibility(View.VISIBLE);
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.AddToCalendar, R.drawable.calendar_icon_fab)
                        .setLabel(getString(R.string.add_to_calendar))
                        .setLabelColor(Color.WHITE)
                        .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccentDark, getActivity().getTheme()))
                        .setTheme(R.style.AppTheme)
                        .setLabelClickable(false)
                        .create()
        );

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.AddToBucketList, R.drawable.add_bucket_icon)
                        .setLabel(getString(R.string.add_to_bucketList))
                        .setLabelColor(Color.WHITE)
                        .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccentDark, getActivity().getTheme()))
                        .setTheme(R.style.AppTheme)
                        .setLabelClickable(false)
                        .create()
        );

        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                switch (speedDialActionItem.getId()) {
                    case R.id.AddToCalendar:
                        Log.d(TAG, "Add to Calendar");
                        datePickerDialog.show();
                        return false; // true to keep the Speed Dial open
                    case R.id.AddToBucketList:
                        Log.d(TAG, "Add to bucket list");
                        chooseBucketListDialog();
                        return false;
                    default:
                        return false;
                }
            }
        });

        setupRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Picasso.get()
                .load(NetworkUtils.buildGooglePhotoUrl(photoWidth, photoRef))
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(header, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
//                        scheduleStartPostponedTransition(header);
                    }

                    @Override
                    public void onError(Exception e) {
//                        startPostponedEnterTransition();
                    }
                });

        retrieveBucketLists();
        retrieveCalendarEntries();
        loadDetails();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the phone call
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+placeDetails.getPhoneNumber()));
                    startActivity(callIntent);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), getActivity().getString(R.string.call_permission_denied), Toast.LENGTH_SHORT);
                }
                return;
            }
        }
    }

    private void loadDetails() {
        //        showDataView();
//        mProgressBar.setVisibility(View.VISIBLE);

        Call<PlaceDetailsResponse> call = apiService.getPlaceDetails(placeId, Constants.GOOGLE_API_KEY);
        call.enqueue(new Callback<PlaceDetailsResponse>() {
            @Override
            public void onResponse(Call<PlaceDetailsResponse> call, Response<PlaceDetailsResponse> response) {
                placeDetails = response.body().getResults();
                setupUI();
//                pois = (ArrayList) response.body().getResults();
//                mProgressBar.setVisibility(View.INVISIBLE);
//                bestThingsTodoAdapter.setPoiData(pois);
            }
            @Override
            public void onFailure(Call<PlaceDetailsResponse> call, Throwable t) {
//                mProgressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, t.getMessage());
//                showErrorMessage();
            }
        });
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }

    private void setupRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        binding.reviewsRecyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        binding.reviewsRecyclerView.addItemDecoration(new SpacesItemDecoration(20));
        binding.reviewsRecyclerView.setAdapter(mAdapter);
    }

    private void setupUI() {

        if (placeDetails.getName() != null) {
            binding.poiName.setText(placeDetails.getName());
        }

        if (placeDetails.getOpeningHours() != null) {
            if (placeDetails.getOpeningHours().getOpenNow() != null) {
                if (placeDetails.getOpeningHours().getOpenNow()) {
                    binding.openNowValue.setText(getActivity().getString(R.string.open));
                }
            }

            if (placeDetails.getOpeningHours().getWeekdayHours() != null) {
                binding.mondayOpeningHoursTv.setText(placeDetails.getOpeningHours().getWeekdayHours().get(0));
                binding.tuesdayOpeningHoursTv.setText(placeDetails.getOpeningHours().getWeekdayHours().get(1));
                binding.wednesdayOpeningHoursTv.setText(placeDetails.getOpeningHours().getWeekdayHours().get(2));
                binding.thursdayOpeningHoursTv.setText(placeDetails.getOpeningHours().getWeekdayHours().get(3));
                binding.fridayOpeningHoursTv.setText(placeDetails.getOpeningHours().getWeekdayHours().get(4));
                binding.saturdayOpeningHoursTv.setText(placeDetails.getOpeningHours().getWeekdayHours().get(5));
                binding.sundayOpeningHoursTv.setText(placeDetails.getOpeningHours().getWeekdayHours().get(6));
            }
        }

        if (placeDetails.getPhoto().get(0).getAttributions() != null) {
            binding.attribution.setText(placeDetails.getPhoto().get(0).getAttributions().get(0));
        }

        String rating = String.format("%.1f", placeDetails.getRating());
        binding.ratingValue.setText(rating);
        binding.ratingBar.setRating(placeDetails.getRating());

        if (placeDetails.getAddress() != null) {
            binding.address.setText(placeDetails.getAddress());
            binding.addressLayout.setClickable(true);
        }

        if (placeDetails.getPhoneNumber() != null) {
            binding.phone.setText(placeDetails.getPhoneNumber());
            binding.phoneLayout.setClickable(true);
        }


        if (placeDetails.getPlaceReviews() != null) {
            mAdapter.setData(placeDetails.getPlaceReviews());
        }
    }

    private void retrieveBucketLists() {
        Log.d(TAG, "Actively retrieving the bucketLists from the DataBase");
        LiveData<List<BucketList>> bucketLists = mDb.bucketListDao().loadAllBucketLists();

        bucketLists.observe(this, new Observer<List<BucketList>>() {
            @Override
            public void onChanged(@Nullable List<BucketList> bucketLists) {
                Log.d(TAG, "Receiving database update from LiveData");
                if (bucketLists.size() > 0) {
                    mBucketLists = (ArrayList) bucketLists;
                    Log.d(TAG, mBucketLists.get(0).getName());
//                    Log.d(TAG, mBucketLists.get(0).getItems().size()+"");
                    for (int i = 0; i<mBucketLists.size(); i++) {
                        mBucketListsName.add(mBucketLists.get(i).getName());
                    }
                }
            }
        });
    }

    private void retrieveCalendarEntries() {
        Log.d(TAG, "Actively retrieving the Entries from the DataBase");
        LiveData<List<MyCalendar>> entries = mDb.myCalendarDao().loadAllCalendarEntries();

        entries.observe(this, new Observer<List<MyCalendar>>() {
            @Override
            public void onChanged(@Nullable List<MyCalendar> entries) {
                Log.d(TAG, "Receiving database update from LiveData");
                if (entries.size() > 0) {
                    calendarEntries = (ArrayList) entries;
                }
            }
        });
    }

    private void chooseBucketListDialog() {

        if (mBucketLists.size() <= 0){
            Toast.makeText(getActivity(), getString(R.string.no_buckets_created), Toast.LENGTH_SHORT).show();
            return;
        }
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getActivity().getString(R.string.choose_bucket));

        // add a list
        String[] array = mBucketListsName.toArray(new String[mBucketListsName.size()]);
        builder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addItemToBucketList(which);
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addToCalendar(int year, int month, int dayOfMonth) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                PointOfInterestPhoto photo = new PointOfInterestPhoto(photoRef, photoWidth);
                ArrayList<PointOfInterestPhoto> pointsOfInterestsPhotoArrayList = new ArrayList<>();
                pointsOfInterestsPhotoArrayList.add(photo);

                PointsOfInterests item = new PointsOfInterests(poi_name, placeId, pointsOfInterestsPhotoArrayList);
                ArrayList<PointsOfInterests> pointsOfInterestsArrayList = new ArrayList<>();
                pointsOfInterestsArrayList.add(item);

                BucketListItem bucketListItem = new BucketListItem(pointsOfInterestsArrayList);
                ArrayList<BucketListItem> items = new ArrayList<>();
                items.add(bucketListItem);
                CalendarDay date = CalendarDay.from(year, month, dayOfMonth);
                MyCalendar calendar = new MyCalendar(year, month, dayOfMonth,date, items);
                long result = mDb.myCalendarDao().insertCalendarEntry(calendar);

                if (result == -1) {
                    CalendarDay calendardate = CalendarDay.from(year, month, dayOfMonth);
                    CalendarDay eventDate;
                    for (int i = 0; i< calendarEntries.size(); i++) {
                        eventDate = CalendarDay.from(calendarEntries.get(i).getYear(),
                                calendarEntries.get(i).getMonth(),
                                calendarEntries.get(i).getDay());

                        if (eventDate.equals(calendardate)) {
                            items = calendarEntries.get(i).getItems();
                        }
                    }
                    for (int i =0; i < items.size(); i++) {
                        if (items.get(i).getActivities().get(0).getName().equals(bucketListItem.getActivities().get(0).getName())) {
                            isFound = true;
                        }
                    }
                    if (isFound) {
                        Toast.makeText(getActivity(), getActivity().getString(R.string.item_already_in_calendar), Toast.LENGTH_SHORT).show();
                        isFound = false;
                    } else {
                        items.add(bucketListItem);
                        calendar.setItems(items);
                        mDb.myCalendarDao().updateaCalendarEntry(calendar);
                        Toast.makeText(getActivity(), getActivity().getString(R.string.item_added_calendar), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void addItemToBucketList(int index) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Adding Item to bucket");
                PointOfInterestPhoto photo = new PointOfInterestPhoto(photoRef, photoWidth);
                ArrayList<PointOfInterestPhoto> pointsOfInterestsPhotoArrayList = new ArrayList<>();
                pointsOfInterestsPhotoArrayList.add(photo);

                PointsOfInterests item = new PointsOfInterests(poi_name, placeId, pointsOfInterestsPhotoArrayList);
                ArrayList<PointsOfInterests> pointsOfInterestsArrayList = new ArrayList<>();
                pointsOfInterestsArrayList.add(item);

                BucketListItem bucketListItem = new BucketListItem(pointsOfInterestsArrayList);
                ArrayList<BucketListItem> items = new ArrayList<>();

                for (int i =0; i < mBucketLists.size(); i++) {
                    if (mBucketLists.get(i).getName() == mBucketListsName.get(index)) {
                        if (mBucketLists.get(i).getItems() != null) {
                            items = mBucketLists.get(i).getItems();
                            break;
                        }
                    }
                }
                items.add(bucketListItem);

                BucketList bucketList = new BucketList(mBucketListsName.get(index), items);
                Log.d(TAG, bucketList.getItems().size()+"");
                Log.d(TAG, bucketList.getItems().get(0).getActivities().get(0).getName());
//                long id = mDb.bucketListDao().insertBucket(bucketList);
//                Log.d(TAG, "Value of insert is" + " " + id+"");
//                if (id == -1) {
                    Log.d(TAG, "BucketList will be updated");
                    mDb.bucketListDao().updateBucket(bucketList);
//                }
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        addToCalendar(year, month+1 , dayOfMonth);
    }

    @Override
    public void onClick(int position) {
        /*Reviews*/
    }

}
