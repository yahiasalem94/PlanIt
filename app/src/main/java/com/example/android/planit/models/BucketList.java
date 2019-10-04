package com.example.android.planit.models;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "bucket_list")
public class BucketList {

    @NonNull
    @PrimaryKey
    private String name;
//    private ArrayList<PointsOfInterests> activities;


    public BucketList(String name/*, ArrayList<PointsOfInterests> activities*/) {
        this.name = name;
//        this.activities = activities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public ArrayList<PointsOfInterests> getActivities() {
//        return activities;
//    }
//
//    public void setActivities(ArrayList<PointsOfInterests> activities) {
//        this.activities = activities;
//    }
}
