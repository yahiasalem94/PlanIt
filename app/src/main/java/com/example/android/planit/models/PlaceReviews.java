package com.example.android.planit.models;

import com.google.gson.annotations.SerializedName;

public class PlaceReviews {

    @SerializedName("author_name")
    private String authorName;
    @SerializedName("author_url")
    private String authorUrl;
    private float rating;
    @SerializedName("relative_time_description")
    private String time;
    @SerializedName("text")
    private String review;
    @SerializedName("profile_photo_url")
    private String profilePhoto;

    public PlaceReviews(String authorName, float rating, String time, String review) {
        this.authorName = authorName;
        this.rating = rating;
        this.time = time;
        this.review = review;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
