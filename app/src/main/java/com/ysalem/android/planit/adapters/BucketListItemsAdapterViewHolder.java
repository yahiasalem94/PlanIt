package com.ysalem.android.planit.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ysalem.android.planit.R;

public class BucketListItemsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public final ImageView mPoiPhoto;
    public final TextView mPoiName;

    private final BucketListItemsAdapter.BucketListItemsAdapterOnClickHandler mClickHandler;

    public BucketListItemsAdapterViewHolder(View view, BucketListItemsAdapter.BucketListItemsAdapterOnClickHandler clickHandler) {
        super(view);
        mPoiPhoto = view.findViewById(R.id.poi_image);
        mPoiName = view.findViewById(R.id.poi_name);
        this.mClickHandler = clickHandler;
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mClickHandler.onClick(getAdapterPosition());
    }

}
