package com.example.android.planit.ui;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.planit.Constants;
import com.example.android.planit.R;
import com.example.android.planit.adapters.NearbyAdapter;
import com.example.android.planit.models.PointsOfInterests;
import com.example.android.planit.models.PointsOfInterestsResponse;
import com.example.android.planit.utils.ApiInterface;
import com.example.android.planit.utils.LocationTrack;
import com.example.android.planit.utils.NetworkUtils;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class NearByFragment extends Fragment implements NearbyAdapter.NearbyAdapterOnClickHandler {

    private final static String TAG = NearByFragment.class.getSimpleName();


    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    private LocationTrack locationTrack;


    /* View */
    private View mRootview;
    private ProgressBar mProgressBar;
    private TextView errorTextView;
    private RecyclerView mRecyclerView;
    private ImageView header;

    /* Retrofit */
    private ApiInterface apiService;

    /* data */
    private LinearLayoutManager layoutManager;
    private ArrayList<PointsOfInterests> pois;
    private NearbyAdapter nearbyAdapter;

    /* Navigation */
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.

        apiService = NetworkUtils.getRetrofitInstance(getActivity()).create(ApiInterface.class);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        nearbyAdapter = new NearbyAdapter(this, getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootview = inflater.inflate(R.layout.fragment_nearby, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        mRecyclerView = mRootview.findViewById(R.id.nearby_recycler_view);
        errorTextView = mRootview.findViewById(R.id.tv_error_message_display);
        mProgressBar = mRootview.findViewById(R.id.pb_loading_indicator);

        setupRecyclerView();

        return mRootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            } else {
                locationTrack = new LocationTrack(getActivity());
                if (locationTrack.canGetLocation()) {
                    loadData(locationTrack.getLatitude()+"", locationTrack.getLongitude()+"");
                    Log.d(TAG, locationTrack.getLatitude()+"" + " " + locationTrack.getLongitude()+"");
                } else {
                    locationTrack.showSettingsAlert();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        header = ((MainActivity) getActivity()).imageView;
        header.setImageDrawable(null);
        ((MainActivity) getContext()).lockAppBarClosed();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(
                true) {
            @Override
            public void handleOnBackPressed() {
//                appBarLayout = ((MainActivity) getActivity()).appBarLayout;
//                appBarLayout.setExpanded(false);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigateUp();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setupRecyclerView() {

        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(nearbyAdapter);
    }

    private void showErrorMessage() {

        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        errorTextView.setText(getString(R.string.gps_error_message));
        errorTextView.setVisibility(View.VISIBLE);
    }



    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (ActivityCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    });
                            return;
                        }
                    }
                } else {
                    locationTrack = new LocationTrack(getActivity());
                    if (locationTrack.canGetLocation()) {
                        loadData(locationTrack.getLatitude()+"", locationTrack.getLongitude()+"");
                        Log.d(TAG, locationTrack.getLatitude()+"" + " " + locationTrack.getLongitude()+"");
                    } else {
                        locationTrack.showSettingsAlert();
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }

    @Override
    public void onClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putString(bestThingsTodoFragment.PLACE_ID, pois.get(position).getPlaceId());
        bundle.putString(bestThingsTodoFragment.POI_NAME, pois.get(position).getName());
        bundle.putString(bestThingsTodoFragment.PHOTO_REF, pois.get(position).getPhoto().get(0).getPhotoReference());
        bundle.putInt(bestThingsTodoFragment.PHOTO_WIDTH, pois.get(position).getPhoto().get(0).getWidth());


        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(view, String.valueOf(R.string.transition_image)).build();
        navController.navigate(R.id.detailsFragment, bundle,null, extras);
    }

    /* Pulling data from server */
    private void loadData(String latitude, String Longitude) {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<PointsOfInterestsResponse> call = apiService.getNearbyPlaces(latitude+","+Longitude, Constants.GOOGLE_TYPE,
                Constants.GOOGLE_RANKBY, Constants.GOOGLE_API_KEY);
        call.enqueue(new Callback<PointsOfInterestsResponse>() {
            @Override
            public void onResponse(Call<PointsOfInterestsResponse> call, Response<PointsOfInterestsResponse> response) {

                Log.d(TAG, response.toString());
//                Log.d(TAG, response.body().toString());
                pois = (ArrayList) response.body().getResults();
                mProgressBar.setVisibility(View.INVISIBLE);
                nearbyAdapter.setPoiData(pois);
            }
            @Override
            public void onFailure(Call<PointsOfInterestsResponse> call, Throwable t) {

                mProgressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, t.getMessage());
                showErrorMessage();
            }
        });
    }



}
