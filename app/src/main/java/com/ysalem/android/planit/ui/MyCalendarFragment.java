package com.ysalem.android.planit.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ysalem.android.planit.R;
import com.ysalem.android.planit.adapters.CalendarAdapter;
import com.ysalem.android.planit.database.AppDatabase;
import com.ysalem.android.planit.models.BucketListItem;
import com.ysalem.android.planit.models.MyCalendar;
import com.ysalem.android.planit.utils.EventDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.List;


public class MyCalendarFragment extends Fragment implements  CalendarAdapter.CalendarAdapterOnClickHandler {

    private static final String TAG = MyCalendarFragment.class.getSimpleName();
    public static final String CALENDAR_ITEMS = "calendar_items";
    public static final String CALENDAR_ENTRY = "calendar_entry";

    private AppDatabase mDb;

    private ArrayList<MyCalendar> calendarsEntries;
    private ArrayList<CalendarDay> calendarDays;
    private ArrayList<BucketListItem> items;
    private int position;
    private CalendarDay eventDate;
    private boolean foundItems = false;
    /* Views */
    private View mRootView;
    private MaterialCalendarView mCalendarView;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private NavController navController;

    private CalendarAdapter mAdapter;

    public MyCalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getMyCalendarDbInstance((getActivity()).getApplicationContext());
        mAdapter = new CalendarAdapter(this, getActivity());
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        items = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getContext()).lockAppBarClosed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        mCalendarView = mRootView.findViewById(R.id.calendarView);
        recyclerView = mRootView.findViewById(R.id.calendar_recycler_view);

        setupRecyclerView();

        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                for (int i = 0; i<calendarsEntries.size(); i++) {
                    eventDate = CalendarDay.from(calendarsEntries.get(i).getYear(),
                            calendarsEntries.get(i).getMonth(),
                            calendarsEntries.get(i).getDay());

                    if (eventDate.equals(date)) {
                        items = calendarsEntries.get(i).getItems();
                        position = i;
                        foundItems = true;
                    }
                }
//                mAdapter.setData(eventNames);
                if (foundItems) {
                    foundItems = false;
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(CALENDAR_ITEMS, items);
//                    bundle.putParcelable(CALENDAR_ENTRY, calendarsEntries.get(position));
                    bundle.putParcelable(CALENDAR_ENTRY, calendarsEntries.get(position).getDate());
                    navController.navigate(R.id.calendarItems, bundle);
                }
            }
        });

        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retrieveTasks();
    }

    private void setupRecyclerView() {

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        recyclerView.setAdapter(mAdapter);
    }

    private void retrieveTasks() {
        Log.d(TAG, "Actively retrieving the Data for calendar from the DataBase");
        LiveData<List<MyCalendar>> entries = mDb.myCalendarDao().loadAllCalendarEntries();

        entries.observe(this, new Observer<List<MyCalendar>>() {
            @Override
            public void onChanged(List<MyCalendar> entries) {
                calendarsEntries = (ArrayList) entries;
                highlightDates(calendarsEntries);
            }
        });
    }

    private void highlightDates(ArrayList<MyCalendar> entries) {
        calendarDays = new ArrayList<>();
        mCalendarView.invalidateDecorators();
        for (int i = 0; i<entries.size(); i++) {
            CalendarDay calendarDay = CalendarDay.from(entries.get(i).getYear(), entries.get(i).getMonth(), entries.get(i).getDay());

            calendarDays.add(calendarDay);
        }
        EventDecorator eventDecorator = new EventDecorator(ContextCompat.getDrawable(getActivity(), R.drawable.circle_border), calendarDays);
        mCalendarView.addDecorator(eventDecorator);
    }



    @Override
    public void onClick(int position) {
        /*Recyclerview click*/
    }
}
