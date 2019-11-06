package com.example.android.planit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.planit.R;
import com.example.android.planit.utils.NetworkUtils;
import com.example.android.planit.models.PointsOfInterests;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapterViewHolder> {

    private ArrayList<PointsOfInterests> mPoiData;
    private final NearbyAdapterOnClickHandler mClickHandler;
    private Context context;
    private NavController navController;

    public interface NearbyAdapterOnClickHandler {
        void onClick(View view, int position);
    }


    public NearbyAdapter(NearbyAdapterOnClickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
        this.context = context;
    }

    @Override
    public NearbyAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.best_things_todo_row, viewGroup, false);
        return new NearbyAdapterViewHolder(view, mClickHandler);
    }

    @Override
    public void onBindViewHolder(NearbyAdapterViewHolder adapterViewHolder, int position) {
        if (mPoiData.get(position).getPhoto() != null) {
            Picasso.get()
                    .load(NetworkUtils.buildGooglePhotoUrl(/*mPoiData.get(position).getPhoto().get(0).getWidth()*/200,
                            mPoiData.get(position).getPhoto().get(0).getPhotoReference()))
                    /* TODO Change place holder */
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(adapterViewHolder.mPoiPhoto);
        } else {
            adapterViewHolder.mPoiPhoto.setImageResource(R.drawable.no_image);
        }
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
