package com.ysalem.android.planit.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.ysalem.android.planit.R;
import com.ysalem.android.planit.models.BucketListItem;
import com.ysalem.android.planit.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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

    public ArrayList<BucketListItem> getData() {
        return mData;
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(BucketListItem item, int position) {
        mData.add(position, item);
        notifyItemInserted(position);
    }
}

