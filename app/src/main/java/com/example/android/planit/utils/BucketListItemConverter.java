package com.example.android.planit.utils;

import androidx.room.TypeConverter;

import com.example.android.planit.models.BucketListItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class BucketListItemConverter {
    @TypeConverter
    public static List<BucketListItem> stringToBucketListItems(String json) {
        if (json == null) {
            return Collections.emptyList();
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<BucketListItem>>() {}.getType();

        return gson.fromJson(json, listType);
    }

    @TypeConverter
    public static String BucketListItemsToString(List<BucketListItem> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}