package com.example.android.planit.models;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;
import java.util.Date;

@Entity(tableName = "my_calendar")
public class MyCalendar {
    @PrimaryKey
    @NonNull
    private int year;
    private int month;
    private int day;
    private String name;

    public MyCalendar(int year, int month, int day, String name) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.name = name;
    }


    public int getYear() {
        return year;
    }

    public void setYear(int  year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int  month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int  day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
