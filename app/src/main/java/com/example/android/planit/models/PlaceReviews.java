package com.example.android.planit.models;

import com.google.gson.annotations.SerializedName;

public class PlaceReviews {

    @SerializedName("author_name")
    private String authorName;
    private int rating;
    @SerializedName("relative_time_description")
    private String time;
    @SerializedName("text")
    private String review;

    public PlaceReviews(String authorName, int rating, String time, String review) {
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
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
}
