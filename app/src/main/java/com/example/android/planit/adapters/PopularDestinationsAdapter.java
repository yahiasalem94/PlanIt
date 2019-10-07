package com.example.android.planit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.planit.R;
import com.example.android.planit.models.PopularDestinations;
import com.example.android.planit.utils.NetworkUtils;
import com.example.android.planit.models.PointsOfInterests;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PopularDestinationsAdapter extends RecyclerView.Adapter<PopularDestinationsAdapterViewHolder> {

    private ArrayList<PopularDestinations> mData;
    private final PopularDestinationsAdapterOnClickHandler mClickHandler;
    private Context context;
    private NavController navController;

    public interface PopularDestinationsAdapterOnClickHandler {
        void onClick(int position);
    }


    public PopularDestinationsAdapter(PopularDestinationsAdapterOnClickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
        this.context = context;
    }

    @Override
    public PopularDestinationsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.pop_dest_row, viewGroup, false);
        return new PopularDestinationsAdapterViewHolder(view, mClickHandler);
    }

    @Override
    public void onBindViewHolder(PopularDestinationsAdapterViewHolder adapterViewHolder, int position) {
        adapterViewHolder.mCityPhoto.setImageResource(mData.get(position).getImage());
        adapterViewHolder.mCityName.setText(mData.get(position).getName());

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(ArrayList<PopularDestinations> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }
}

