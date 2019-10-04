package com.example.android.planit.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "popular_destinations")
public class PopularDestinations {


    @NonNull
    @PrimaryKey
    String name;


    public PopularDestinations(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
