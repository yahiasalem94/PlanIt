package com.example.android.planit.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.planit.R;

public class CalendarAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public final TextView mEventName;
    private final CalendarAdapter.CalendarAdapterOnClickHandler mClickHandler;

    public CalendarAdapterViewHolder(View view, CalendarAdapter.CalendarAdapterOnClickHandler clickHandler) {
        super(view);
        mEventName = view.findViewById(R.id.eventName);
        this.mClickHandler = clickHandler;
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mClickHandler.onClick(getAdapterPosition());
    }

}