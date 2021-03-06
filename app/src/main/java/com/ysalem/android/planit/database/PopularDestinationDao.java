package com.ysalem.android.planit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ysalem.android.planit.models.PopularDestinations;

import java.util.List;

@Dao
public interface PopularDestinationDao {

    @Query("SELECT * FROM popular_destinations")
    LiveData<List<PopularDestinations>> loadAllDestinations();

    @Insert
    void insertAll(PopularDestinations... destinations);

    @Query("SELECT count(*) FROM popular_destinations")
    int numberOfRows();
}
