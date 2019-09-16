package com.example.android.planit.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

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
