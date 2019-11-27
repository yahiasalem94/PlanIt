package com.ysalem.android.planit.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.TypeConverters;

import com.ysalem.android.planit.utils.PointsOfInterestConverter;

import java.util.List;

public class BucketListItem implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    protected BucketListItem(Parcel in) {
    }

    public static final Creator<BucketListItem> CREATOR = new Creator<BucketListItem>() {
        @Override
        public BucketListItem createFromParcel(Parcel in) {
            return new BucketListItem(in);
        }

        @Override
        public BucketListItem[] newArray(int size) {
            return new BucketListItem[size];
        }
    };

}
