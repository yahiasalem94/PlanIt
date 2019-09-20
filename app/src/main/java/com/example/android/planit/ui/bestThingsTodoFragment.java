package com.example.android.planit.ui;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.android.planit.R;
import com.google.android.material.appbar.AppBarLayout;

public class bestThingsTodoFragment extends Fragment  {

    private static final String TAG = bestThingsTodoFragment.class.getSimpleName();

    /* Views */
    private View mRootview;
    private AppBarLayout appBarLayout;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public bestThingsTodoFragment() {

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(
                true) {
            @Override
            public void handleOnBackPressed() {
                appBarLayout = ((MainActivity) getActivity()).appBarLayout;
                appBarLayout.setExpanded(false);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigateUp();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootview = inflater.inflate(R.layout.fragment_best_things_todo, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

//        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
//            @Override
//            public void handleOnBackPressed() {
//                showAreYouSureDialog();
//            }
//        };

        return mRootview;
    }

}
