package com.example.android.planit.ui;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.android.planit.R;
import com.example.android.planit.databinding.FragmentHomeBinding;
import com.google.android.material.appbar.AppBarLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private NavController navController;

    String formattedDate;


    /* Views */
    private FragmentHomeBinding binding;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        appBarLayout = ((MainActivity) getActivity()).appBarLayout;
        appBarLayout.setExpanded(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        binding.searchButton.setOnClickListener(this);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        Navigation.setViewNavController(binding.searchButton, navController);

        return binding.getRoot();
    }


    @Override
    public void onClick(View v) {
        appBarLayout.setExpanded(true);
//        toolbar.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));

        navController.navigate(R.id.bestThingsTodoFragment);
    }
}
