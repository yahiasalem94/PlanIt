package com.example.android.planit.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.planit.R;


public class BucketListFragment extends Fragment {

    private static final String TAG = BucketListFragment.class.getSimpleName();

    /* Views */
    private View mRootView;

    public BucketListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_my_bucket_lists, container, false);

        return mRootView;
    }

}
