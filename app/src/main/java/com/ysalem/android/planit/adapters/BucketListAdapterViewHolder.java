package com.ysalem.android.planit.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ysalem.android.planit.R;

public class BucketListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public final TextView mBucketListName;
    public final CardView mBucketListCardView;
    private final BucketListAdapter.BucketListAdapterOnClickHandler mClickHandler;

    public BucketListAdapterViewHolder(View view, BucketListAdapter.BucketListAdapterOnClickHandler clickHandler) {
        super(view);
        mBucketListName = view.findViewById(R.id.bucketlist_name);
        mBucketListCardView = view.findViewById(R.id.bucket_list_card_view);
        this.mClickHandler = clickHandler;
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mClickHandler.onClick(getAdapterPosition());
    }

}
