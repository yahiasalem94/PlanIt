package com.ysalem.android.planit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ysalem.android.planit.models.MyCalendar;

import java.util.List;

@Dao
public interface MyCalendarDao {

    @Query("SELECT * FROM my_calendar")
    LiveData<List<MyCalendar>> loadAllCalendarEntries();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertCalendarEntry(MyCalendar calendarEntry);

    @Update
    void updateaCalendarEntry(MyCalendar calendarEntry);

    @Query("SELECT * FROM my_calendar WHERE date = :date")
    LiveData<MyCalendar> loadEntry(String date);

    @Delete
    void deleteCalendarEntry(MyCalendar calendarEntry);
}
