package com.example.android.planit.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OpeningHours {

    @SerializedName("weekday_text")
    private ArrayList<String> weekdayHours;
    @SerializedName("open_now")
    private boolean openNow;


    public OpeningHours(ArrayList<String> weekdayHours, boolean openNow) {
        this.weekdayHours = weekdayHours;
        this.openNow = openNow;
    }

    public List<String> getWeekdayHours() {
        return weekdayHours;
    }

    public void setWeekdayHours(ArrayList<String> weekdayHours) {
        this.weekdayHours = weekdayHours;
    }

    public boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }
}
