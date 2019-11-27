package com.ysalem.android.planit.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ysalem.android.planit.R;

public class PopularDestinationsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public final ImageView mCityPhoto;
    public final TextView mCityName;
    private final PopularDestinationsAdapter.PopularDestinationsAdapterOnClickHandler mClickHandler;

    public PopularDestinationsAdapterViewHolder(View view, PopularDestinationsAdapter.PopularDestinationsAdapterOnClickHandler clickHandler) {
        super(view);
        mCityPhoto = view.findViewById(R.id.cityPoster);
        mCityName = view.findViewById(R.id.cityName);
        this.mClickHandler = clickHandler;
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mClickHandler.onClick(getAdapterPosition());
    }

}
