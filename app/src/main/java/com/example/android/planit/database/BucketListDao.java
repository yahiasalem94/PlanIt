package com.example.android.planit.database;




import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.planit.models.BucketList;
import com.example.android.planit.models.BucketListItem;

import java.util.List;

@Dao
public interface BucketListDao {

    @Query("SELECT * FROM bucket_list")
    LiveData<List<BucketList>> loadAllBucketLists();

    @Query("SELECT * FROM bucket_list")
    List<BucketList> loadWidgetBucketLists();

    @Query("SELECT * FROM bucket_list WHERE name = :name")
    LiveData<BucketList> loadBucket(String name);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertBucket(BucketList bucketList);

    @Update
    void updateBucket(BucketList bucketList);

    @Delete
    void deleteBucket(BucketList bucketList);


}
