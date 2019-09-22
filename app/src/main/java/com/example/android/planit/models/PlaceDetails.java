package com.example.android.planit.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlaceDetails {

    @SerializedName("formatted_address")
    private String address;
    @SerializedName("international_phone_number")
    private String phoneNumber;
    @SerializedName("geometry")
    private Location location;
    private String name;
    private int rating;
    @SerializedName("reviews")
    private ArrayList <PlaceReviews> placeReviews;
    private String website;

    public PlaceDetails(String address, String phoneNumber, Location location, String name, int rating, ArrayList<PlaceReviews> placeReviews, String website) {
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.name = name;
        this.rating = rating;
        this.placeReviews = placeReviews;
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public ArrayList<PlaceReviews> getPlaceReviews() {
        return placeReviews;
    }

    public void setPlaceReviews(ArrayList<PlaceReviews> placeReviews) {
        this.placeReviews = placeReviews;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
