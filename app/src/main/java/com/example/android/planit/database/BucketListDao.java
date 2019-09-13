package com.example.android.planit.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import com.example.android.planit.models.BucketList;

import java.util.ArrayList;

@Dao
public interface BucketListDao {

    @Query("SELECT * FROM bucket_list")
    LiveData<ArrayList<BucketList>> loadAllBucketLists();

    @Query("SELECT * FROM bucket_list WHERE id = :id")
    BucketList loadBucket(Integer id);

    @Insert
    void insertBucket(BucketList bucketList);

    @Delete
    void deleteBucket(BucketList bucketList);

}
