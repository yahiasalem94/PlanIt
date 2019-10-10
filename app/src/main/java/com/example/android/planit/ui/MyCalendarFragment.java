package com.example.android.planit.ui;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.planit.R;
import com.example.android.planit.database.AppDatabase;
import com.example.android.planit.models.MyCalendar;
import com.example.android.planit.models.PopularDestinations;
import com.example.android.planit.utils.EventDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MyCalendarFragment extends Fragment {

    private static final String TAG = MyCalendarFragment.class.getSimpleName();

    private AppDatabase mDb;

    private ArrayList<MyCalendar> calendarsEntries;
    private ArrayList<CalendarDay> calendarDays;
    /* Views */
    private View mRootView;
    private MaterialCalendarView mCalendarView;

    public MyCalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getMyCalendarDbInstance((getActivity()).getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        mCalendarView = mRootView.findViewById(R.id.calendarView);

        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retrieveTasks();
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
}
