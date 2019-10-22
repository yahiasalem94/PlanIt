package com.example.android.planit.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.planit.R;
import com.example.android.planit.models.BucketListItem;
import com.example.android.planit.models.PlaceReviews;
import com.example.android.planit.models.PopularDestinations;
import com.example.android.planit.utils.NetworkUtils;
import com.example.android.planit.models.PointsOfInterests;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class BucketListItemsAdapter extends RecyclerView.Adapter<BucketListItemsAdapterViewHolder> {

    private static final String TAG = BucketListItemsAdapter.class.getSimpleName();

    private ArrayList<BucketListItem> mData;
    private final BucketListItemsAdapterOnClickHandler mClickHandler;

    public interface BucketListItemsAdapterOnClickHandler {
        void onClick(int position);
    }


    public BucketListItemsAdapter(BucketListItemsAdapterOnClickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
    }

    @Override
    public BucketListItemsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.bucket_list_item_row, viewGroup, false);
        return new BucketListItemsAdapterViewHolder(view, mClickHandler);
    }

    @Override
    public void onBindViewHolder(BucketListItemsAdapterViewHolder adapterViewHolder, int position) {
//        Picasso.get()
//                .load(mData.get(position).getActivities().get(position).getPhoto().get(0).)
//                .placeholder(R.drawable.no_image)
//                .error(R.drawable.no_image)
//                .into(adapterViewHolder.mProfilePhoto);
        Picasso.get()
                .load(NetworkUtils.buildGooglePhotoUrl(mData.get(position).getActivities().get(0).getPhoto().get(0).getWidth(),
                        mData.get(position).getActivities().get(0).getPhoto().get(0).getPhotoReference()))
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(adapterViewHolder.mPoiPhoto);

        Log.d(TAG, mData.get(position).getActivities().get(0).getName());
        Log.d(TAG, mData.get(position).getActivities().get(0).getPlaceId());
        Log.d(TAG, mData.get(position).getActivities().get(0).getPhoto().size()+"");
        adapterViewHolder.mPoiName.setText(mData.get(position).getActivities().get(0).getName());

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(ArrayList<BucketListItem> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }
}

