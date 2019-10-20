package com.example.android.planit.models;

import androidx.room.TypeConverters;

import com.example.android.planit.utils.PointsOfInterestConverter;

import java.util.List;

public class BucketListItem {

    @TypeConverters(PointsOfInterestConverter.class)
    private List<PointsOfInterests> activities;

    public BucketListItem(List<PointsOfInterests> activities) {
        this.activities = activities;
    }

    public List<PointsOfInterests> getActivities() {
        return activities;
    }

    public void setActivities(List<PointsOfInterests> activities) {
        this.activities = activities;
    }
}
