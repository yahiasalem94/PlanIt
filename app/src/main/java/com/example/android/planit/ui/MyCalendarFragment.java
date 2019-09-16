package com.example.android.planit.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.planit.R;


public class MyCalendarFragment extends Fragment {

    private static final String TAG = MyCalendarFragment.class.getSimpleName();

    /* Views */
    private View mRootView;

    public MyCalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        return mRootView;
    }

}
