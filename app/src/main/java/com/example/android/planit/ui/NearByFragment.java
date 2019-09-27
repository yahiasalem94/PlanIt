package com.example.android.planit.ui;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.planit.BuildConfig;
import com.example.android.planit.Constants;
import com.example.android.planit.R;
import com.example.android.planit.adapters.NearbyAdapter;
import com.example.android.planit.models.PointsOfInterests;
import com.example.android.planit.models.PointsOfInterestsResponse;
import com.example.android.planit.utils.ApiInterface;
import com.example.android.planit.utils.NetworkUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearByFragment extends Fragment implements NearbyAdapter.NearbyAdapterOnClickHandler {

    private final static String TAG = NearByFragment.class.getSimpleName();

    private LocationRequest mLocationRequest;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 14;

    private boolean isLocationFetched = false;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    /* View */
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
    private NearbyAdapter nearbyAdapter;
    private String lastLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkPermissions()) {
            requestPermissions();
        }

        apiService = NetworkUtils.getRetrofitInstance().create(ApiInterface.class);

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

    @Override
    public void onStart() {
        super.onStart();
        startLocationUpdates();
        getLastLocation();
    }

    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        String msg = "Updated Location: " + location.getLatitude() + "," + location.getLongitude();

                        Log.d(TAG, msg);
                        // GPS location can be null if GPS is switched off
                        if (location != null && !isLocationFetched) {
                            isLocationFetched = true;
                            lastLocation = location.getLatitude()+""+"%2C"+location.getLongitude()+"";
                            loadData(lastLocation, location.getLatitude()+"",
                                    location.getLongitude()+"");
                            //                            onLocationChanged(location);
                        } else {
                            showErrorMessage();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        try {
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        lastLocation = locationResult.getLastLocation().getLatitude()+""+","+locationResult.getLastLocation().getLongitude()+"";
                        if (locationResult.getLastLocation() != null && !isLocationFetched) {
                            isLocationFetched = true;
                            loadData(lastLocation, locationResult.getLastLocation().getLatitude() + "",
                                    locationResult.getLastLocation().getLongitude() + "");
//                        onLocationChanged(locationResult.getLastLocation());
                        }
                    }
                },
                Looper.myLooper());
    } catch (SecurityException ex) {
        ex.printStackTrace();
    }
}

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " + location.getLatitude() + "," + location.getLongitude();
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view, int position) {

    }

    /* Pulling data from server */
    private void loadData(String location, String latitude, String Longitude) {
        mProgressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "Load data with location:"+" "+location);
        Call<PointsOfInterestsResponse> call = apiService.getNearbyPlaces(latitude+","+Longitude, Constants.GOOGLE_RANKBY, Constants.GOOGLE_API_KEY);
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

    /* Permissions */
    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }


    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(getActivity().findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

}