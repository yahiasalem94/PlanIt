package com.example.android.planit.ui;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.planit.R;
import com.example.android.planit.adapters.BucketListAdapter;
import com.example.android.planit.database.AppDatabase;
import com.example.android.planit.databinding.FragmentMyBucketListsBinding;
import com.example.android.planit.models.BucketList;
import com.example.android.planit.utils.AppExecutors;
import com.example.android.planit.utils.ItemTouchHelperCallback;
import com.example.android.planit.utils.SpacesItemDecoration;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class BucketListFragment extends Fragment implements View.OnClickListener, BucketListAdapter.BucketListAdapterOnClickHandler {

    private static final String TAG = BucketListFragment.class.getSimpleName();
    public static final String BUCKET_LIST_NAME = "bucket_name";

    private AppDatabase mDb;

    private BucketListAdapter bucketListAdapter;
    private ArrayList<BucketList> mBucketLists;
    private SpacesItemDecoration decorator;

    private NavController navController;

    private  static int itemSpace = 16;
    /* Views */
    private FragmentMyBucketListsBinding binding;
    private LinearLayoutManager linearLayoutManager;

    public BucketListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDb = AppDatabase.getBucketListDbInstance((getActivity()).getApplicationContext());
        //  call the constructor of MoviesAdapter to send the reference and data to Adapter
        bucketListAdapter = new BucketListAdapter(this, getActivity());
        decorator = new SpacesItemDecoration(itemSpace);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        NavigationView navigation = getActivity().findViewById(R.id.nav_view);
        Menu drawer_menu = navigation.getMenu();
        MenuItem menuItem;
        menuItem = drawer_menu.findItem(R.id.my_bucket_lists);
        if(!menuItem.isChecked()) {
            menuItem.setChecked(true);
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {

            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigateUp();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_bucket_lists, container, false);
        binding.fab.setOnClickListener(this);

        setupRecyclerView();
        enableSwipeToDeleteAndUndo();

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        Navigation.setViewNavController(binding.fab, navController);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        retrieveTasks();
    }


    @Override
    public void onClick(View v) {
        showDialog();
    }


    @Override
    public void onClick(int position) {
        /* Adapter onClick */
        Bundle bundle = new Bundle();
        bundle.putString(BUCKET_LIST_NAME, mBucketLists.get(position).getName());
        navController.navigate(R.id.bucketListItemsFragment, bundle);
    }


    private void setupRecyclerView() {

        binding.bucketlistsRecyclerView.addItemDecoration(decorator);
        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        linearLayoutManager = new LinearLayoutManager(getActivity());
        // set LayoutManager to RecyclerView
        binding.bucketlistsRecyclerView.setLayoutManager(linearLayoutManager);
        binding.bucketlistsRecyclerView.setAdapter(bucketListAdapter); // set the Adapter to RecyclerView

    }

    private void enableSwipeToDeleteAndUndo() {
        ItemTouchHelperCallback swipeToDeleteCallback = new ItemTouchHelperCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final BucketList item = bucketListAdapter.getData().get(position);

                bucketListAdapter.removeItem(position);

                Snackbar snackbar = Snackbar.make(binding.constraintLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        bucketListAdapter.restoreItem(item, position);
                        binding.bucketlistsRecyclerView.scrollToPosition(position);
                    }
                });
                snackbar.addCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        //see Snackbar.Callback docs for event details
                        deleteBucketList(item);
                    }

                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(binding.bucketlistsRecyclerView);
    }

    private void retrieveTasks() {
        Log.d(TAG, "Actively retrieving the bucketLists from the DataBase");
        LiveData<List<BucketList>> bucketLists = mDb.bucketListDao().loadAllBucketLists();

        bucketLists.observe(this, new Observer<List<BucketList>>() {
            @Override
            public void onChanged(@Nullable List<BucketList> bucketLists) {
                Log.d(TAG, "Receiving database update from LiveData");

                Log.d(TAG, "bucketlist size " + bucketLists.size()+"");
                if (bucketLists.size() > 0) {
                    mBucketLists = (ArrayList) bucketLists;
                    binding.emptyBucket.setVisibility(View.INVISIBLE);
//                    binding.fab.hide();
                    binding.bucketlistsRecyclerView.setVisibility(View.VISIBLE);
                    bucketListAdapter.setBucketListsData(mBucketLists);
                } else {
                    binding.bucketlistsRecyclerView.setVisibility(View.INVISIBLE);
                    binding.emptyBucket.setVisibility(View.VISIBLE);
//                    binding.fab.show();
                }
            }
        });
    }

    private void showDialog(){

//        CreateBucketList dialogFragment = new CreateBucketList();
//        Bundle bundle = new Bundle();
//
//        bundle = new Bundle();
//        bundle.putBoolean("fullScreen", true);
//        bundle.putBoolean("notAlertDialog", false);
//
//        dialogFragment.setArguments(bundle);
//
//        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//
//        ft = getActivity().getSupportFragmentManager().beginTransaction();
//
//        ft.addToBackStack(null);
//
//        dialogFragment.show(ft, "dialog");
        navController.navigate(R.id.createBucketListDialog);

//        final Dialog dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.create_bucket_dialog);
//
////        final EditText et = dialog.findViewById(R.id.et);
//
//        final TextView txtVw = dialog.findViewById(R.id.placeName);
//
//        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
//                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//
//        AutocompleteFilter filter = new AutocompleteFilter.Builder()
//                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
//                .build();
//        autocompleteFragment.setFilter(filter);
//
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                txtVw.setText(place.getName());
//                Log.d(TAG, place.getName().toString());
//                dialog.dismiss();
//            }
//            @Override
//            public void onError(Status status) {
//                txtVw.setText(status.toString());
//            }
//        });

//        Button btnok = dialog.findViewById(R.id.btnok);
//        btnok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BucketList bucketList = new BucketList(et.getText().toString());
////                tv.setText(et.getText().toString());
//                Log.d(TAG, et.getText().toString());
//                addBucketList(bucketList);
//                dialog.dismiss();
//            }
//        });
//
//        Button btncn = dialog.findViewById(R.id.btncn);
//        btncn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

//        dialog.show();
    }

    private void addBucketList(final BucketList bucketList) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                    mDb.bucketListDao().insertBucket(bucketList);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "BucketList Created", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
        });
    }

    private void deleteBucketList(final BucketList bucketList) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.bucketListDao().deleteBucket(bucketList);

            }
        });
    }

}
