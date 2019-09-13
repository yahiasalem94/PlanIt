package com.example.android.planit.Utils;

import com.example.android.planit.models.pointsOfInterestsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterface {

    @GET("api/place/textsearch/json")
    Call<pointsOfInterestsResponse> getThingsTodoInCity(@Query("query") String query, @Query("key") String apiKey);

//    @GET("api/place/photo")
//    Call<PlaceResponse> getThingsTodoInCity(@Query("photoreference") String photoReference, @Query("key") String apiKey);
}
