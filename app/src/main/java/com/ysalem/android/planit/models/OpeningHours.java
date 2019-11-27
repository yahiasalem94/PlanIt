package com.ysalem.android.planit.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OpeningHours {

    @SerializedName("weekday_text")
    private ArrayList<String> weekdayHours;
    @SerializedName("open_now")
    private Boolean openNow;


    public OpeningHours(ArrayList<String> weekdayHours, Boolean openNow) {
        this.weekdayHours = weekdayHours;
        this.openNow = openNow;
    }

    public List<String> getWeekdayHours() {
        return weekdayHours;
    }

    public void setWeekdayHours(ArrayList<String> weekdayHours) {
        this.weekdayHours = weekdayHours;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }
}
