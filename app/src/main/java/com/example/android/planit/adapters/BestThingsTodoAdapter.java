package com.example.android.planit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.planit.Constants;
import com.example.android.planit.R;
import com.example.android.planit.Utils.NetworkUtils;
import com.example.android.planit.models.PointsOfInterests;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BestThingsTodoAdapter extends RecyclerView.Adapter<BestThingsTodoAdapterViewHolder> {

    private ArrayList<PointsOfInterests> mPoiData;
    private final BestThingsTodoAdapterOnClickHandler mClickHandler;
    private Context context;
    private NavController navController;

    public interface BestThingsTodoAdapterOnClickHandler {
        void onClick(View view, int position);
    }


    public BestThingsTodoAdapter(BestThingsTodoAdapterOnClickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
        this.context = context;
    }

    @Override
    public BestThingsTodoAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.best_things_todo_row, viewGroup, false);
        return new BestThingsTodoAdapterViewHolder(view, mClickHandler);
    }

    @Override
    public void onBindViewHolder(BestThingsTodoAdapterViewHolder adapterViewHolder, int position) {
        Picasso.get()
                .load(NetworkUtils.buildGooglePhotoUrl(/*mPoiData.get(position).getPhoto().get(0).getWidth()*/200, mPoiData.get(position).getPhoto().get(0).getPhotoReference()))
               /* TODO Change place holder */
                .placeholder(R.drawable.home_icon)
                .into(adapterViewHolder.mPoiPhoto);
        adapterViewHolder.mNameOfPoi.setText(mPoiData.get(position).getName());
        adapterViewHolder.mPoiPhoto.setAdjustViewBounds(true);
    }

    @Override
    public int getItemCount() {
        if (null == mPoiData) return 0;
        return mPoiData.size();
    }

    public void setPoiData(ArrayList<PointsOfInterests> poiData) {
        mPoiData = poiData;
        notifyDataSetChanged();
    }
}
