package com.example.android.planit.ui;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.planit.R;
import com.example.android.planit.adapters.BucketListItemsAdapter;
import com.example.android.planit.database.AppDatabase;
import com.example.android.planit.models.BucketListItem;
import com.example.android.planit.models.MyCalendar;
import com.example.android.planit.utils.AppExecutors;
import com.example.android.planit.utils.ItemTouchHelperCallback;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;


public class CalendarItems extends Fragment implements BucketListItemsAdapter.BucketListItemsAdapterOnClickHandler {

    private static final String TAG = CalendarItems.class.getSimpleName();

    private String bucketName;
//    private BucketList mBucketList;
    private MyCalendar calendarEntry;
    private ArrayList<BucketListItem> items;
    private CalendarDay date;
    private BucketListItemsAdapter mBucketListItemAdapter;

    private AppDatabase mDb;
    /* Views */
    private View mRootView;
    private RecyclerView recyclerView;
    private RecyclerView mRecyclerView;
    private AppBarLayout appBarLayout;
    private NestedScrollView nestedScrollView;
    private ConstraintLayout constraintLayout;

    private LinearLayoutManager linearLayoutManager;

    private NavController navController;

    public CalendarItems() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(MyCalendarFragment.CALENDAR_ITEMS)
                && getArguments().containsKey(MyCalendarFragment.CALENDAR_ENTRY)) {
            items = getArguments().getParcelableArrayList(MyCalendarFragment.CALENDAR_ITEMS);
//            calendarEntry = getArguments().getParcelable(MyCalendarFragment.CALENDAR_ENTRY);
            date = getArguments().getParcelable(MyCalendarFragment.CALENDAR_ENTRY);
        }

        mDb = AppDatabase.getMyCalendarDbInstance((getActivity()).getApplicationContext());
        mBucketListItemAdapter = new BucketListItemsAdapter(this, getActivity());
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        loadCalendarEntry();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_calendar_items, container, false);
        recyclerView = mRootView.findViewById(R.id.recycler_view);
        constraintLayout = mRootView.findViewById(R.id.constraintLayout);

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
        ((MainActivity) getContext()).lockAppBarClosed();
    }

    private void setupRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mBucketListItemAdapter);
    }

    private void loadCalendarEntry() {
        Log.d(TAG, "Actively retrieving the entries from the DataBase");
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDay();
        String stringDate;
        StringBuffer sb = new StringBuffer();
        sb.append(year).append(month).append(day);
        stringDate = sb.toString();
        Log.d(TAG, stringDate);
        LiveData<MyCalendar> entry = mDb.myCalendarDao().loadEntry(stringDate);
        entry.observe(this, new Observer<MyCalendar>() {
            @Override
            public void onChanged(@Nullable MyCalendar entry) {
                Log.d(TAG, "Receiving database update from LiveData");
                if (entry != null) {
                    calendarEntry = entry;
                    mBucketListItemAdapter.setData(calendarEntry.getItems());
                }
            }
        });
    }


    private void enableSwipeToDeleteAndUndo() {
        ItemTouchHelperCallback swipeToDeleteCallback = new ItemTouchHelperCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final BucketListItem item = mBucketListItemAdapter.getData().get(position);

                mBucketListItemAdapter.removeItem(position);

                Snackbar snackbar = Snackbar.make(constraintLayout, getActivity().getString(R.string.item_removed), Snackbar.LENGTH_LONG);
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
                        deleteBucketListItem(item, position);
                    }

                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void deleteBucketListItem(final BucketListItem bucketListItem, final int position) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
//                items.get(position).getActivities().remove(bucketListItem);
                calendarEntry.getItems().remove(bucketListItem);

                if (calendarEntry.getItems().size() > 0) {
                    mDb.myCalendarDao().updateaCalendarEntry(calendarEntry);
                } else {
                    mDb.myCalendarDao().deleteCalendarEntry(calendarEntry);
                }

            }
        });
    }

    @Override
    public void onClick(int position) {
        /* Adapter OnClick */
        Bundle bundle = new Bundle();
        bundle.putString(bestThingsTodoFragment.PLACE_ID, items.get(position).getActivities().get(0).getPlaceId());
        bundle.putString(bestThingsTodoFragment.POI_NAME, items.get(position).getActivities().get(0).getName());
        bundle.putString(bestThingsTodoFragment.PHOTO_REF,
                items.get(position).getActivities().get(0).getPhoto().get(0).getPhotoReference());
        bundle.putInt(bestThingsTodoFragment.PHOTO_WIDTH,
                items.get(position).getActivities().get(0).getPhoto().get(0).getWidth());


        navController.navigate(R.id.detailsFragment, bundle);
    }
}

