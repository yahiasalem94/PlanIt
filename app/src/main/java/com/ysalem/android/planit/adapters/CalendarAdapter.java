package com.ysalem.android.planit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.ysalem.android.planit.R;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapterViewHolder> {

    private ArrayList<String> mData;
    private final CalendarAdapterOnClickHandler mClickHandler;
    private Context context;

    public interface CalendarAdapterOnClickHandler {
        void onClick(int position);
    }


    public CalendarAdapter(CalendarAdapterOnClickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
        this.context = context;
    }

    @Override
    public CalendarAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.calendar_events_row, viewGroup, false);
        return new CalendarAdapterViewHolder(view, mClickHandler);
    }

    @Override
    public void onBindViewHolder(CalendarAdapterViewHolder adapterViewHolder, int position) {
        adapterViewHolder.mEventName.setText(mData.get(position));

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(ArrayList<String> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }
}

