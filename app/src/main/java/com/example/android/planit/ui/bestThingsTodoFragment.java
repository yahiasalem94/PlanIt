package com.example.android.planit.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.planit.R;

public class bestThingsTodoFragment extends Fragment {

    private static final String TAG = bestThingsTodoFragment.class.getSimpleName();
    private View mRootView;

    /* Views */
    private View mRootview;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public bestThingsTodoFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_best_things_todo, container, false);
        
        return mRootView;
    }

}
