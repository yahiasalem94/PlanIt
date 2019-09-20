package com.example.android.planit.ui;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.planit.R;
import com.example.android.planit.databinding.FragmentMyBucketListsBinding;


public class BucketListFragment extends Fragment {

    private static final String TAG = BucketListFragment.class.getSimpleName();

    /* Views */
    FragmentMyBucketListsBinding binding;

    public BucketListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_bucket_lists, container, false);

        return binding.getRoot();
    }

}
