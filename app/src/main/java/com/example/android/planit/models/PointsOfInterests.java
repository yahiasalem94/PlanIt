package com.example.android.planit.models;

import com.google.gson.annotations.SerializedName;

public class PointsOfInterests {

    @SerializedName("name")
    private String name;
    @SerializedName("place_id")
    private String placeId;

    private String photoReference;

    public PointsOfInterests(String name, String placeId, String photoReference) {
        this.name = name;
        this.placeId = placeId;
        this.photoReference = photoReference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() { return placeId; }

    public void setPlaceId(String placeId) {this.placeId = placeId; }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }
}
