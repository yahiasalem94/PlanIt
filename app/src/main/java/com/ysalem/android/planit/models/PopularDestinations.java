package com.ysalem.android.planit.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "popular_destinations")
public class PopularDestinations {


    @NonNull
    @PrimaryKey
    private String name;
    private int image;


    public PopularDestinations(@NonNull String name, int image) {
        this.name = name;
        this.image = image;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
