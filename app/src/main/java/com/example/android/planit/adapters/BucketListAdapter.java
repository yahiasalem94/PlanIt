package com.example.android.planit.adapters;

import android.app.usage.NetworkStats;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.MotionEventCompat;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.planit.R;
import com.example.android.planit.models.BucketList;

import java.util.ArrayList;
import java.util.Random;

public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapterViewHolder> {

    private ArrayList<BucketList> mBucketListData;
    private final BucketListAdapterOnClickHandler mClickHandler;
    private Context context;
    private NavController navController;
    private static ArrayList<Integer>cardColors;

    public interface BucketListAdapterOnClickHandler {
        void onClick(int position);
    }


    public BucketListAdapter(BucketListAdapterOnClickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
        this.context = context;
        cardColors = new ArrayList<>();
    }

    @Override
    public BucketListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.bucketlist_row, viewGroup, false);
        return new BucketListAdapterViewHolder(view, mClickHandler);
    }

    @Override
    public void onBindViewHolder(final BucketListAdapterViewHolder adapterViewHolder, int position) {
        if (cardColors.size()-1 >= position) {
            adapterViewHolder.mBucketListCardView.setCardBackgroundColor(cardColors.get(position));
        } else {
            getColor();
            adapterViewHolder.mBucketListCardView.setCardBackgroundColor(cardColors.get(position));
        }
        adapterViewHolder.mBucketListName.setText(mBucketListData.get(position).getName());

    }

    @Override
    public int getItemCount() {
        if (null == mBucketListData) return 0;
        return mBucketListData.size();
    }

    public void removeItem(int position) {
        mBucketListData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(BucketList item, int position) {
        mBucketListData.add(position, item);
        notifyItemInserted(position);
    }

    public ArrayList<BucketList> getData() {
        return mBucketListData;
    }

    public void setBucketListsData(ArrayList<BucketList> bucketListData) {
        mBucketListData = bucketListData;
        notifyDataSetChanged();
    }

    private void getColor() {
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        cardColors.add(randomAndroidColor);
    }
}
