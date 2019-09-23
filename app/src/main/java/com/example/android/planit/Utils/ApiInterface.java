package com.example.android.planit.Utils;

import com.example.android.planit.models.PlaceDetailsResponse;
import com.example.android.planit.models.PointsOfInterests;
import com.example.android.planit.models.PointsOfInterestsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterface {

    @GET("api/place/textsearch/json")
    Call<PointsOfInterestsResponse> getThingsTodoInCity(@Query("query") String query, @Query("key") String apiKey);

    @GET("api/place/details/json")
    Call<PlaceDetailsResponse> getPlaceDetails(@Query("placeid") String placeId, @Query("key") String apiKey);

    @GET("api/place/nearbysearc/json")
    Call<PointsOfInterestsResponse> getNfearbyPlaces(@Query("location") String location, @Query("rankby") String rankBy, @Query("key") String apiKey);

//    @GET("api/place/photo")
//    Call<PlaceResponse> getThingsTodoInCity(@Query("photoreference") String photoReference, @Query("key") String apiKey);
}
