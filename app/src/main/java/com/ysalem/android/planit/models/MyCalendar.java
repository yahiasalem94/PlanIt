package com.ysalem.android.planit.models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.ysalem.android.planit.utils.BucketListItemConverter;
import com.ysalem.android.planit.utils.CalendarConverter;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "my_calendar")
public class MyCalendar implements Parcelable {
//    @PrimaryKey(autoGenerate = true)
    private int uniqueId;

//    private int year;
//    private int month;
//    private int day;
    @PrimaryKey
    @NonNull
    @TypeConverters(CalendarConverter.class)
    private Date date;
    @TypeConverters(BucketListItemConverter.class)
    private ArrayList<BucketListItem> items;

    public MyCalendar(/*int year, int month, int day, */Date date, ArrayList<BucketListItem> items) {
//        this.year = year;
//        this.month = month;
//        this.day = day;
        this.date = date;
        this.items = items;
    }


    protected MyCalendar(Parcel in) {
        uniqueId = in.readInt();
//        year = in.readInt();
//        month = in.readInt();
//        day = in.readInt();
        date = in.readParcelable(CalendarDay.class.getClassLoader());
        items = in.createTypedArrayList(BucketListItem.CREATOR);
    }

//    public int getYear() {
//        return year;
//    }
//
//    public void setYear(int  year) {
//        this.year = year;
//    }
//
//    public int getMonth() {
//        return month;
//    }
//
//    public void setMonth(int  month) {
//        this.month = month;
//    }
//
//    public int getDay() {
//        return day;
//    }
//
//    public void setDay(int  day) {
//        this.day = day;
//    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<BucketListItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<BucketListItem> items) {
        this.items = items;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uniqueId);
//        dest.writeInt(year);
//        dest.writeInt(month);
//        dest.writeInt(day);
        dest.writeParcelable((Parcelable) date, flags);
        dest.writeTypedList(items);
    }

    public static final Creator<MyCalendar> CREATOR = new Creator<MyCalendar>() {
        @Override
        public MyCalendar createFromParcel(Parcel in) {
            return new MyCalendar(in);
        }

        @Override
        public MyCalendar[] newArray(int size) {
            return new MyCalendar[size];
        }
    };
}
