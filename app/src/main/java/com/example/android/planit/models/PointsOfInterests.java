package com.example.android.planit.models;

import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PointsOfInterests {

    @SerializedName("name")
    private String name;
    @SerializedName("place_id")
    private String placeId;
    @SerializedName("photos")
    private ArrayList <PointOfInterestPhoto> photo;

    public PointsOfInterests(String name, String placeId, ArrayList <PointOfInterestPhoto> photo) {
        this.name = name;
        this.placeId = placeId;
        this.photo = photo;
    }

    @Ignore
    public PointsOfInterests(String name, String placeId) {
        this.name = name;
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() { return placeId; }

    public void setPlaceId(String placeId) {this.placeId = placeId; }

    public ArrayList <PointOfInterestPhoto> getPhoto() {
        return photo;
    }

    public void setPhoto(ArrayList <PointOfInterestPhoto> photo) {
        this.photo = photo;
    }

}
