package com.example.android.planit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.android.planit.models.MyCalendar;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MyCalendarDao {

    @Query("SELECT * FROM my_calendar")
    LiveData<List<MyCalendar>> loadAllCalendarEntries();

    @Insert
    void insertCalendarEntry(MyCalendar calendarEntry);

    @Delete
    void deleteCalendarEntry(MyCalendar calendarEntry);
}
