package com.ysalem.android.planit.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ysalem.android.planit.R;

public class NearbyAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public final ImageView mPoiPhoto;
    public final TextView mNameOfPoi;
    private final NearbyAdapter.NearbyAdapterOnClickHandler mClickHandler;

    public NearbyAdapterViewHolder(View view, NearbyAdapter.NearbyAdapterOnClickHandler clickHandler) {
        super(view);
        mPoiPhoto = view.findViewById(R.id.poiPoster);
        mNameOfPoi = view.findViewById(R.id.nameOfPoi);
        this.mClickHandler = clickHandler;
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mClickHandler.onClick(mPoiPhoto, getAdapterPosition());
    }
}
