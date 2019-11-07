package com.example.android.planit.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PointOfInterestPhoto {


    private int height;
    @SerializedName("photo_reference")
    private String photoReference;
    private int width;
    @SerializedName("html_attributions")
    private ArrayList<String> attributions;

    public PointOfInterestPhoto(int height, String photoReference, int width, ArrayList<String> attributions) {
        this.height = height;
        this.photoReference = photoReference;
        this.width = width;
        this.attributions = attributions;
    }

    public PointOfInterestPhoto(String photoReference, int width) {
        this.photoReference = photoReference;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public ArrayList <String> getAttributions() {
        return attributions;
    }

    public void setAttributions(ArrayList <String> attributions) {
        this.attributions = attributions;
    }
}
