package com.ysalem.android.planit.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ysalem.android.planit.R;

public class BestThingsTodoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mPoiPhoto;
        public final TextView mNameOfPoi;
        public final RelativeLayout mNameLayout;
        private final BestThingsTodoAdapter.BestThingsTodoAdapterOnClickHandler mClickHandler;

        public BestThingsTodoAdapterViewHolder(View view, BestThingsTodoAdapter.BestThingsTodoAdapterOnClickHandler clickHandler) {
            super(view);
            mPoiPhoto = view.findViewById(R.id.poiPoster);
            mNameOfPoi = view.findViewById(R.id.nameOfPoi);
            mNameLayout = view.findViewById(R.id.poiNameLayout);
            this.mClickHandler = clickHandler;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(mPoiPhoto, getAdapterPosition());
        }
}
