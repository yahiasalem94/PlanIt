package com.ysalem.android.planit.utils;

import android.util.Log;

import androidx.room.TypeConverter;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Date;

public class CalendarConverter {


    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

//    @TypeConverter
//    public CalendarDay fromTimestamp(String stringDate) {
//        if (stringDate == null) {
//            return null;
//        } else {
//            int year = Integer.parseInt(stringDate.substring(0, 4));
//            int month = Integer.parseInt(stringDate.substring(4, 6));
//            int day = Integer.parseInt(stringDate.substring(stringDate.length()-2));
//
//            CalendarDay date;
//            date = CalendarDay.from(year, month, day);
//            return date;
//        }
//
//
//    }
//
//    @TypeConverter
//    public String dateToTimestamp(CalendarDay date) {
//        if (date == null) {
//            return null;
//        } else {
//            int year = date.getYear();
//            int month = date.getMonth();
//            int day = date.getDay();
//            String stringDate;
//            StringBuffer sb = new StringBuffer();
//            sb.append(year).append(month).append(day);
//            stringDate = sb.toString();
//            Log.d("CONVERTER", stringDate);
//            return stringDate;
//        }
//    }
}
