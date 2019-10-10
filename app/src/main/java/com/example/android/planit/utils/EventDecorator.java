package com.example.android.planit.utils;

import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class EventDecorator implements DayViewDecorator {

//    private final int color;
    private final HashSet<CalendarDay> dates;
    private final Drawable drawable;

    public EventDecorator(Drawable drawable, Collection<CalendarDay> dates) {
//        this.color = color;
        this.dates = new HashSet<>(dates);
        this.drawable = drawable;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
//        view.addSpan(new Span(color));
        view.setBackgroundDrawable(drawable);
    }
}