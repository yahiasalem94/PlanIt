package com.example.android.planit.ui;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.android.planit.Constants;
import com.example.android.planit.R;
import com.example.android.planit.database.AppDatabase;
import com.example.android.planit.databinding.FragmentDetailsBinding;
import com.example.android.planit.models.BucketList;
import com.example.android.planit.models.BucketListItem;
import com.example.android.planit.models.MyCalendar;
import com.example.android.planit.models.PlaceDetails;
import com.example.android.planit.models.PlaceDetailsResponse;
import com.example.android.planit.models.PointsOfInterests;
import com.example.android.planit.utils.ApiInterface;
import com.example.android.planit.utils.AppExecutors;
import com.example.android.planit.utils.NetworkUtils;
import com.google.android.gms.maps.model.PointOfInterest;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = DetailsFragment.class.getSimpleName();

    private DatePickerDialog datePickerDialog;
    private final Calendar calendar;
    private int year;
    private int month;
    private int day;

    private String city;
    private String placeId;
    private String poi_name;
    private PlaceDetails placeDetails;

    private ArrayList<BucketList> mBucketLists;
    private ArrayList<String> mBucketListsName;

    /* Retrofit */
    private ApiInterface apiService;

    /*Database*/
    private AppDatabase mDb;

    /* Views */
    private FragmentDetailsBinding binding;


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

        if (getArguments() != null && getArguments().containsKey(HomeFragment.CITY_NAME)
                && getArguments().containsKey(bestThingsTodoFragment.PLACE_ID)
                && getArguments().containsKey(bestThingsTodoFragment.POI_NAME)) {

            city = getArguments().getString(HomeFragment.CITY_NAME);
            placeId = getArguments().getString(bestThingsTodoFragment.PLACE_ID);
            poi_name = getArguments().getString(bestThingsTodoFragment.POI_NAME);
        }

        apiService = NetworkUtils.getRetrofitInstance(getActivity()).create(ApiInterface.class);
        mDb = AppDatabase.getMyCalendarDbInstance((getActivity()).getApplicationContext());
        datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

//        retrieveBucketLists();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);

        binding.speedDial.addActionItem(
                new SpeedDialActionItem.Builder(R.id.AddToCalendar, R.drawable.calendar_icon_fab)
                        .setLabel(getString(R.string.add_to_calendar))
                        .setLabelColor(Color.WHITE)
                        .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccentDark, getActivity().getTheme()))
                        .setTheme(R.style.AppTheme)
                        .setLabelClickable(false)
                        .create()
        );

        binding.speedDial.addActionItem(
                new SpeedDialActionItem.Builder(R.id.AddToBucketList, R.drawable.add_bucket_icon)
                        .setLabel(getString(R.string.add_to_bucketList))
                        .setLabelColor(Color.WHITE)
                        .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccentDark, getActivity().getTheme()))
                        .setTheme(R.style.AppTheme)
                        .setLabelClickable(false)
                        .create()
        );

        binding.speedDial.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                switch (speedDialActionItem.getId()) {
                    case R.id.AddToCalendar:
                        Log.d(TAG, "Add to Calendar");
                        datePickerDialog.show();
                        return false; // true to keep the Speed Dial open
                    case R.id.AddToBucketList:
                        Log.d(TAG, "Add to bucket list");
//                        chooseBucketListDialog();
                        return false;
                    default:
                        return false;
                }
            }
        });

        return binding.getRoot();
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

    private void setupUI() {

    }

//    private void retrieveBucketLists() {
//        Log.d(TAG, "Actively retrieving the bucketLists from the DataBase");
//        LiveData<List<BucketList>> bucketLists = mDb.bucketListDao().loadAllBucketLists();
//
//        bucketLists.observe(this, new Observer<List<BucketList>>() {
//            @Override
//            public void onChanged(@Nullable List<BucketList> bucketLists) {
//                Log.d(TAG, "Receiving database update from LiveData");
//                if (bucketLists.size() > 0) {
//                    mBucketLists = (ArrayList) bucketLists;
//                    for (int i = 0; i<mBucketLists.size(); i++) {
//                        mBucketListsName.add(mBucketLists.get(i).getName());
//                    }
//                }
//            }
//        });
//    }

//    private void chooseBucketListDialog() {
//
//        if (mBucketLists.size() <= 0){
//            Toast.makeText(getActivity(), "No BucketLists Created", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        // setup the alert builder
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Choose a BucketList");
//
//        // add a list
//        String[] array = mBucketListsName.toArray(new String[mBucketListsName.size()]);
//        builder.setItems(array, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                addItemToBucketList(which);
//            }
//        });
//
//// create and show the alert dialog
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
    private void addToCalendar(int year, int month, int dayOfMonth) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                MyCalendar calendar = new MyCalendar(year, month, dayOfMonth, city);
                mDb.myCalendarDao().insertCalendarEntry(calendar);
            }
        });
    }

//    private void addItemToBucketList(int index) {
//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//
//                PointsOfInterests item = new PointsOfInterests(poi_name, placeId);
//                ArrayList<PointsOfInterests> pointsOfInterestsArrayList = new ArrayList<>();
//                pointsOfInterestsArrayList.add(item);
//                BucketListItem bucketListItem = new BucketListItem(pointsOfInterestsArrayList);
//                ArrayList<BucketListItem> items = new ArrayList<>();
//                items.add(bucketListItem);
//                BucketList bucketList = new BucketList(mBucketListsName.get(index), items);
//                mDb.bucketListDao().insertBucket(bucketList);
//            }
//        });
//    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        addToCalendar(year, month+1 , dayOfMonth);
    }
}
