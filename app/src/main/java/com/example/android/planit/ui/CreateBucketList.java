package com.example.android.planit.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.android.planit.Constants;
import com.example.android.planit.R;
import com.example.android.planit.Widget.WidgetUpdateService;
import com.example.android.planit.database.AppDatabase;
import com.example.android.planit.models.BucketList;
import com.example.android.planit.utils.AppExecutors;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;


public class CreateBucketList extends Fragment {

    public static final String TAG = CreateBucketList.class.getSimpleName();
    public static final String CREATE_BUCKET_NAME = "bucketName";
    private View mRootview;

//    private TextView txtVw;
    private EditText queryText;
    private Button mSearchButton;
    private TextView mSearchResult;
    private StringBuilder mResult;

    private PlacesClient placesClient;

    private AutocompleteSupportFragment autocompleteFragment;
    private TextView txtView;

    private Bundle bundle;
    private NavController navController;
    private AppDatabase mDb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getBucketListDbInstance((getActivity()).getApplicationContext());
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootview =  inflater.inflate(R.layout.create_bucket_dialog, container, false);

        Places.initialize(getActivity().getApplicationContext(), Constants.GOOGLE_API_KEY);
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(getActivity());

        autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);


        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
//                txtView.setText(place.getName()+","+place.getId());
                bundle = new Bundle();
                bundle.putString(CREATE_BUCKET_NAME, place.getName());
                BucketList bucketList = new BucketList(place.getName());
                addBucketList(bucketList);
                navController.navigate(R.id.bucketListFragment);

                Log.i(TAG, "New Bucket List created");
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        return mRootview;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Places.


//        mSearchButton.setOnClickListener(v -> {
//            Toast.makeText(getActivity(), queryText.getText().toString(), Toast.LENGTH_SHORT).show();
//            // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
//            // and once again when the user makes a selection (for example when calling fetchPlace()).
//            AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
//            // Create a RectangularBounds object.
//           /* RectangularBounds bounds = RectangularBounds.newInstance(
//                    new LatLng(-33.880490, 151.184363), //dummy lat/lng
//                    new LatLng(-33.858754, 151.229596));*/
//            // Use the builder to create a FindAutocompletePredictionsRequest.
//            FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
//                    // Call either setLocationBias() OR setLocationRestriction().
//                    //.setLocationBias(bounds)
//                    //.setLocationRestriction(bounds)
//                    .setTypeFilter(TypeFilter.CITIES)
//                    .setSessionToken(token)
//                    .setQuery(queryText.getText().toString())
//                    .build();
//
//
//            placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
//                mResult = new StringBuilder();
//                for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
//                    mResult.append(" ").append(prediction.getFullText(null) + "\n");
//                    Log.i(TAG, prediction.getPlaceId());
//                    Log.i(TAG, prediction.getPrimaryText(null).toString());
//                    Toast.makeText(getActivity(), prediction.getPrimaryText(null) + "-" + prediction.getSecondaryText(null), Toast.LENGTH_SHORT).show();
//                }
//                mSearchResult.setText(String.valueOf(mResult));
//                bundle = new Bundle();
////                bundle.putString("BucketName", response.getAutocompletePredictions().get(0).);
////                navController.navigate(R.id.bucketListFragment, bundle);
//
//            }).addOnFailureListener((exception) -> {
//                if (exception instanceof ApiException) {
//                    ApiException apiException = (ApiException) exception;
//                    Log.e(TAG, "Place not found: " + apiException.getStatusCode());
//                }
//            });
//        });

    }

    private void addBucketList(final BucketList bucketList) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                long id = mDb.bucketListDao().insertBucket(bucketList);
                if (id == -1) {
                    Log.d(TAG, "BucketList will be updated");
                    mDb.bucketListDao().updateBucket(bucketList);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "BucketList Created", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        WidgetUpdateService.startActionUpdate(getActivity().getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface DialogListener {
        void onFinishEditDialog(String inputText);
    }


}