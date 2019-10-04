package com.example.android.planit.database;




import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.android.planit.models.BucketList;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface BucketListDao {

    @Query("SELECT * FROM bucket_list")
    LiveData<List<BucketList>> loadAllBucketLists();

    @Query("SELECT * FROM bucket_list WHERE name = :name")
    BucketList loadBucket(String name);

    @Insert
    void insertBucket(BucketList bucketList);

    @Delete
    void deleteBucket(BucketList bucketList);

}
