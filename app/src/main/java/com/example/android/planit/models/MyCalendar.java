package com.example.android.planit.models;

import android.arch.persistence.room.Entity;

@Entity(tableName = "my_calendar")
public class MyCalendar {
    private String date;
    private PointsOfInterests poi;

    public MyCalendar(String date, PointsOfInterests poi) {
        this.date = date;
        this.poi = poi;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public PointsOfInterests getPoi() {
        return poi;
    }

    public void setPoi(PointsOfInterests poi) {
        this.poi = poi;
    }
}
