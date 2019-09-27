package com.example.android.planit.ui;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.planit.Constants;
import com.example.android.planit.R;
import com.example.android.planit.utils.ApiInterface;
import com.example.android.planit.utils.NetworkUtils;
import com.example.android.planit.databinding.FragmentDetailsBinding;
import com.example.android.planit.models.PlaceDetails;
import com.example.android.planit.models.PlaceDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private static final String TAG = DetailsFragment.class.getSimpleName();

    private String city;
    private String placeId;
    private PlaceDetails placeDetails;

    /* Retrofit */
    private ApiInterface apiService;


    /* Views */
    private FragmentDetailsBinding binding;


    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(HomeFragment.CITY_NAME)
                && getArguments().containsKey(bestThingsTodoFragment.PLACE_ID) ) {

            city = getArguments().getString(HomeFragment.CITY_NAME);
            placeId = getArguments().getString(bestThingsTodoFragment.PLACE_ID);
        }

        apiService = NetworkUtils.getRetrofitInstance().create(ApiInterface.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);

        return binding.getRoot();
    }

    private void loadDetails() {
        //        showDataView();
//        mProgressBar.setVisibility(View.VISIBLE);

        Call<PlaceDetailsResponse> call = apiService.getPlaceDetails(placeId, Constants.GOOGLE_API_KEY);
        call.enqueue(new Callback<PlaceDetailsResponse>() {
            @Override
            public void onResponse(Call<PlaceDetailsResponse> call, Response<PlaceDetailsResponse> response) {
                placeDetails = response.body().getResults();
                setupUI();
//                pois = (ArrayList) response.body().getResults();
//                mProgressBar.setVisibility(View.INVISIBLE);
//                bestThingsTodoAdapter.setPoiData(pois);
            }
            @Override
            public void onFailure(Call<PlaceDetailsResponse> call, Throwable t) {
//                mProgressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, t.getMessage());
//                showErrorMessage();
            }
        });
    }

    private void setupUI() {

    }

}
