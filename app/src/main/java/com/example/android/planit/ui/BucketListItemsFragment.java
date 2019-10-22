package com.example.android.planit.ui;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.planit.R;
import com.example.android.planit.adapters.BucketListItemsAdapter;
import com.example.android.planit.database.AppDatabase;
import com.example.android.planit.models.BucketList;
import com.example.android.planit.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;


public class BucketListItemsFragment extends Fragment implements BucketListItemsAdapter.BucketListItemsAdapterOnClickHandler {

    private static final String TAG = BucketListItemsFragment.class.getSimpleName();

    private String bucketName;
    private BucketList mBucketList;
    private BucketListItemsAdapter mBucketListItemAdapter;

    private AppDatabase mDb;
    /* Views */
    private View mRootView;
    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;

    private NavController navController;

    public BucketListItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(BucketListFragment.BUCKET_LIST_NAME)) {
            bucketName = getArguments().getString(BucketListFragment.BUCKET_LIST_NAME);
        }

        mDb = AppDatabase.getBucketListDbInstance((getActivity()).getApplicationContext());
        mBucketListItemAdapter = new BucketListItemsAdapter(this, getActivity());
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        loadBucketListItem();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_bucket_list_items, container, false);
        recyclerView = mRootView.findViewById(R.id.recycler_view);

        setupRecyclerView();

        return mRootView;
    }

    private void setupRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mBucketListItemAdapter);
    }

    private void loadBucketListItem() {
        Log.d(TAG, "Actively retrieving the bucketList from the DataBase");
        LiveData<BucketList> bucketList = mDb.bucketListDao().loadBucket(bucketName);

        bucketList.observe(this, new Observer<BucketList>() {
            @Override
            public void onChanged(@Nullable BucketList bucketList) {
                Log.d(TAG, "Receiving database update from LiveData");
                if (bucketList != null) {
                    Log.d(TAG, "bucketlist is not null");
                    Log.d(TAG, bucketList.getName());
                    Log.d(TAG, bucketList.getItems().size() + "");
                    mBucketList = bucketList;
                    mBucketListItemAdapter.setData(bucketList.getItems());
                }
            }
        });
    }

    @Override
    public void onClick(int position) {
        /* Adapter OnClick */
        Bundle bundle = new Bundle();
        bundle.putString(bestThingsTodoFragment.PLACE_ID, mBucketList.getItems().get(position).getActivities().get(0).getPlaceId());
        bundle.putString(bestThingsTodoFragment.POI_NAME, mBucketList.getItems().get(position).getActivities().get(0).getName());
        bundle.putString(bestThingsTodoFragment.PHOTO_REF,
                mBucketList.getItems().get(position).getActivities().get(0).getPhoto().get(0).getPhotoReference());
        bundle.putInt(bestThingsTodoFragment.PHOTO_WIDTH,
                mBucketList.getItems().get(position).getActivities().get(0).getPhoto().get(0).getWidth());


        navController.navigate(R.id.detailsFragment, bundle);
    }
}
