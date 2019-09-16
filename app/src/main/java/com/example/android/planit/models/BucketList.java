package com.example.android.planit.models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bucket_list")
public class BucketList {

    @PrimaryKey
    private int id;
    private String name;
//    private ArrayList<PointsOfInterests> activities;

    public BucketList(int id, String name/*, ArrayList<PointsOfInterests> activities*/) {
        this.id = id;
        this.name = name;
//        this.activities = activities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
