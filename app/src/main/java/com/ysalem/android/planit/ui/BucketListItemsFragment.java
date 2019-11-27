package com.ysalem.android.planit.ui;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysalem.android.planit.R;
import com.ysalem.android.planit.adapters.BucketListItemsAdapter;
import com.ysalem.android.planit.database.AppDatabase;
import com.ysalem.android.planit.models.BucketList;
import com.ysalem.android.planit.models.BucketListItem;
import com.ysalem.android.planit.utils.AppExecutors;
import com.ysalem.android.planit.utils.ItemTouchHelperCallback;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;


public class BucketListItemsFragment extends Fragment implements BucketListItemsAdapter.BucketListItemsAdapterOnClickHandler {

    private static final String TAG = BucketListItemsFragment.class.getSimpleName();

    private String bucketName;
    private BucketList mBucketList;
    private BucketListItemsAdapter mBucketListItemAdapter;

    private AppDatabase mDb;
    /* Views */
    private View mRootView;
    private RecyclerView recyclerView;
    private TextView mErrorTv;
    private AppBarLayout appBarLayout;
    private ImageView header;
    private ConstraintLayout constraintLayout;

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
        constraintLayout = mRootView.findViewById(R.id.constraintLayout);
        mErrorTv = mRootView.findViewById(R.id.tv_error_message_display);

        setupRecyclerView();
        enableSwipeToDeleteAndUndo();
        return mRootView;
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
    public void onResume() {
        super.onResume();
        header = ((MainActivity) getActivity()).imageView;
        header.setImageDrawable(null);
        ((MainActivity) getContext()).lockAppBarClosed();
    }

    private void setupRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mBucketListItemAdapter);
    }

    private void enableSwipeToDeleteAndUndo() {
        ItemTouchHelperCallback swipeToDeleteCallback = new ItemTouchHelperCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final BucketListItem item = mBucketListItemAdapter.getData().get(position);

                mBucketListItemAdapter.removeItem(position);

                Snackbar snackbar = Snackbar.make(constraintLayout, getActivity().getString(R.string.item_removed), Snackbar.LENGTH_SHORT);
                snackbar.setAction(getActivity().getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mBucketListItemAdapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }
                });
                snackbar.addCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        //see Snackbar.Callback docs for event details
                        deleteBucketListItem(item);
                    }

                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void deleteBucketListItem(final BucketListItem bucketListItem) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mBucketList.getItems().remove(bucketListItem);
                mDb.bucketListDao().updateBucket(mBucketList);

            }
        });
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
                    if (bucketList.getItems().size() > 0) {
                        Log.d(TAG, bucketList.getItems().size() + "");
                        mBucketList = bucketList;
                        mBucketListItemAdapter.setData(bucketList.getItems());
                    } else {
                        mErrorTv.setVisibility(View.VISIBLE);
                    }
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
