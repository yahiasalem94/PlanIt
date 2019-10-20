package com.example.android.planit.models;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.android.planit.utils.BucketListItemConverter;

import java.util.List;

@Entity(tableName = "bucket_list")
public class BucketList {

    @NonNull
    @PrimaryKey
    private String name;
    @TypeConverters(BucketListItemConverter.class)
    private List<BucketListItem> items;

    @Ignore
    public BucketList(String name) {
        this.name = name;
    }
//
    public BucketList(String name, List<BucketListItem> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BucketListItem> getItems() {
        return items;
    }

    public void setItems(List<BucketListItem> items) {
        this.items = items;
    }
}
