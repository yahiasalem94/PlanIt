package com.ysalem.android.planit.models;

import com.google.gson.annotations.SerializedName;

public class PlaceDetailsResponse {

    @SerializedName("result")
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
