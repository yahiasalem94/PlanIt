package com.example.android.planit.models;

import com.google.gson.annotations.SerializedName;

public class PointOfInterestPhoto {


    private int height;
    @SerializedName("photo_reference")
    private String photoReference;
    private int width;

    public PointOfInterestPhoto(int height, String photoReference, int width) {
        this.height = height;
        this.photoReference = photoReference;
        this.width = width;
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
}
