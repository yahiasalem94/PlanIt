package com.example.android.planit.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "my_calendar")
public class MyCalendar {
    @PrimaryKey
    @NonNull
    private String date;
//    private PointsOfInterests poi;

    public MyCalendar(String date/*, PointsOfInterests poi*/) {
        this.date = date;
//        this.poi = poi;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

//    public PointsOfInterests getPoi() {
//        return poi;
//    }
//
//    public void setPoi(PointsOfInterests poi) {
//        this.poi = poi;
//    }
}
