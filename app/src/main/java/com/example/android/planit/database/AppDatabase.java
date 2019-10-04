package com.example.android.planit.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.android.planit.models.BucketList;
import com.example.android.planit.models.MyCalendar;


@Database(entities = {BucketList.class, MyCalendar.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "PlanIt";
    private static AppDatabase myBucketListInstance;
    private static AppDatabase myCalendarInstance;
    private static AppDatabase popularDestinationsInstance;

    public static AppDatabase getBucketListDbInstance(Context context) {
        if (myBucketListInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                myBucketListInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return myBucketListInstance;
    }

    public static AppDatabase getMyCalendarDbInstance(Context context) {
        if (myCalendarInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                myCalendarInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return myCalendarInstance;
    }

    public static AppDatabase getPopularDestinationsDbInstance(Context context) {
        if (popularDestinationsInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                popularDestinationsInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return popularDestinationsInstance;
    }

    public abstract BucketListDao bucketListDao();
    public abstract MyCalendarDao myCalendarDao();
    public abstract PopularDestinationDao popularDestinationDao();
}
