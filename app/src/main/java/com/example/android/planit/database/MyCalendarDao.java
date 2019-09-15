package com.example.android.planit.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.planit.models.BucketList;
import com.example.android.planit.models.MyCalendar;

import java.util.ArrayList;

@Dao
public interface MyCalendarDao {

//    @Query("SELECT * FROM my_calendar")
//    LiveData<ArrayList<MyCalendar>> loadAllCalendarEntries();

    @Insert
    void insertCalendarEntry(MyCalendar calendarEntry);

    @Delete
    void deleteCalendarEntry(MyCalendar calendarEntry);
}
