package com.example.android.planit.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.planit.R;

public class BestThingsTodoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mPoiPhoto;
        public final TextView mNameOfPoi;
        private final BestThingsTodoAdapter.BestThingsTodoAdapterOnClickHandler mClickHandler;

        public BestThingsTodoAdapterViewHolder(View view, BestThingsTodoAdapter.BestThingsTodoAdapterOnClickHandler clickHandler) {
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