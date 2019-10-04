package com.example.android.planit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.android.planit.models.PopularDestinations;

import java.util.List;

public interface PopularDestinationDao {

    @Query("SELECT * FROM popular_destinations")
    LiveData<List<PopularDestinations>> loadAllDestinations();

    @Insert
    void insertAll(PopularDestinations... destinations);
}
