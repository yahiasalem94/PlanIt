package com.ysalem.android.planit.models;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.ysalem.android.planit.utils.BucketListItemConverter;

import java.util.ArrayList;

@Entity(tableName = "bucket_list")
public class BucketList {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name="name")
    private String name;
    @TypeConverters(BucketListItemConverter.class)
    private ArrayList<BucketListItem> items;

    @Ignore
    public BucketList(String name) {
        this.name = name;
    }
//
    public BucketList(String name, ArrayList<BucketListItem> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<BucketListItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<BucketListItem> items) {
        this.items = items;
    }
}
