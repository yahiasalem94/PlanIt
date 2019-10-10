package com.example.android.planit.ui;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.android.planit.Constants;
import com.example.android.planit.R;
import com.example.android.planit.utils.ApiInterface;
import com.example.android.planit.utils.NetworkUtils;
import com.example.android.planit.adapters.BestThingsTodoAdapter;
import com.example.android.planit.models.PointsOfInterests;
import com.example.android.planit.models.PointsOfInterestsResponse;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class bestThingsTodoFragment extends Fragment implements BestThingsTodoAdapter.BestThingsTodoAdapterOnClickHandler  {

    private static final String TAG = bestThingsTodoFragment.class.getSimpleName();
    public static final String PLACE_ID = "place_id";

    private String city;

    private NavController navController;

    /* Views */
    private View mRootview;
    private ProgressBar mProgressBar;
    private TextView errorTextView;
    private RecyclerView mRecyclerView;
    private AppBarLayout appBarLayout;

    /* Retrofit */
    private ApiInterface apiService;

    /* data */
    private LinearLayoutManager layoutManager;
    private ArrayList<PointsOfInterests> pois;
    private BestThingsTodoAdapter bestThingsTodoAdapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public bestThingsTodoFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(HomeFragment.CITY_NAME)) {
            city = getArguments().getString(HomeFragment.CITY_NAME);
        }
        apiService = NetworkUtils.getRetrofitInstance(getActivity()).create(ApiInterface.class);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        bestThingsTodoAdapter = new BestThingsTodoAdapter(this, getActivity());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(
                true) {
            @Override
            public void handleOnBackPressed() {
//                appBarLayout = ((MainActivity) getActivity()).appBarLayout;
//                appBarLayout.setExpanded(false);       appBarLayout = ((MainActivity) getActivity()).appBarLayout;
//                appBarLayout.setExpanded(false);
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

        mRecyclerView = mRootview.findViewById(R.id.best_things_recycler_view);
        errorTextView = mRootview.findViewById(R.id.tv_error_message_display);
        mProgressBar = mRootview.findViewById(R.id.pb_loading_indicator);

        setupRecyclerView();

        return mRootview;
    }

    private void setupRecyclerView() {
        // set a StaggeredGridLayoutManager with 3 number of columns and vertical orientation
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(bestThingsTodoAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    private void showDataView() {
        /* First, make sure the error is invisible */
        errorTextView.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {

        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        errorTextView.setText(getString(R.string.no_connection));
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void loadData() {
        showDataView();
        mProgressBar.setVisibility(View.VISIBLE);

        Call<PointsOfInterestsResponse> call = apiService.getThingsTodoInCity(Constants.QUERY_THINGS_TODO+city, Constants.GOOGLE_API_KEY);
        call.enqueue(new Callback<PointsOfInterestsResponse>() {
            @Override
            public void onResponse(Call<PointsOfInterestsResponse> call, Response<PointsOfInterestsResponse> response) {

                if (response.raw().networkResponse() != null) {
                    Log.d(TAG, "Network Response");
                } else if (response.raw().cacheResponse() != null) {
                Log.d(TAG, "Cache response");
            }
                if (response.body().getStatus().equals(Constants.CODE_STATUS_OK)) {
                    Log.d(TAG, response.body().getResults().get(0).getName());
                    pois = (ArrayList) response.body().getResults();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    bestThingsTodoAdapter.setPoiData(pois);
                }
            }
            @Override
            public void onFailure(Call<PointsOfInterestsResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, t.getMessage());
                showErrorMessage();
            }
        });
    }

    @Override
    public void onClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putString(HomeFragment.CITY_NAME, city);
        bundle.putString(PLACE_ID, pois.get(position).getPlaceId());

        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(view, String.valueOf(R.string.transition_image)).build();
        navController.navigate(R.id.detailsFragment, bundle,null, extras);
    }
}
