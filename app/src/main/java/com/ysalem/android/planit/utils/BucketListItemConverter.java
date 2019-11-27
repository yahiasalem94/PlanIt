package com.ysalem.android.planit.utils;

import androidx.room.TypeConverter;

import com.ysalem.android.planit.models.BucketListItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BucketListItemConverter {
    @TypeConverter
    public static ArrayList<BucketListItem> stringToBucketListItems(String json) {
        if (json == null) {
            return (ArrayList) Collections.emptyList();
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<BucketListItem>>() {}.getType();

        return gson.fromJson(json, listType);
    }

    @TypeConverter
    public static String BucketListItemsToString(ArrayList<BucketListItem> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}