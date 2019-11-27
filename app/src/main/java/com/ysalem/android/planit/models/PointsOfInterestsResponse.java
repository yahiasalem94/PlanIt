package com.ysalem.android.planit.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PointsOfInterestsResponse {

    @SerializedName("results")
    private List<PointsOfInterests> results;

    @SerializedName("status")
    private String status;


    public PointsOfInterestsResponse(List<PointsOfInterests> results, String status) {
        this.results = results;
        this.status = status;
    }

    public List<PointsOfInterests> getResults() {
        return results;
    }

    public void setResults(List<PointsOfInterests> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
