package com.example.android.planit.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceDetailsResponse {

    @SerializedName("results")
    private PlaceDetails results;

    @SerializedName("status")
    private String status;

    public PlaceDetailsResponse(PlaceDetails results, String status) {
        this.results = results;
        this.status = status;
    }

    public PlaceDetails getResults() {
        return results;
    }

    public void setResults(PlaceDetails results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
