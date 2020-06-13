package com.ysalem.android.planit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.ysalem.android.planit.R;
import com.ysalem.android.planit.models.PlaceReviews;
import com.squareup.picasso.Picasso;
import com.ysalem.android.planit.utils.NetworkUtils;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapterViewHolder> {

    private ArrayList<PlaceReviews> mData;
    private final ReviewsAdapterOnClickHandler mClickHandler;
    private Picasso picasso;

    public interface ReviewsAdapterOnClickHandler {
        void onClick(int position);
    }


    public ReviewsAdapter(ReviewsAdapterOnClickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
        picasso = NetworkUtils.picassoClient(context);
    }

    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.reviews_row, viewGroup, false);
        return new ReviewsAdapterViewHolder(view, mClickHandler);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapterViewHolder adapterViewHolder, int position) {
        picasso.get()
                .load(mData.get(position).getProfilePhoto())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(adapterViewHolder.mProfilePhoto);

        adapterViewHolder.mAuthorName.setText(mData.get(position).getAuthorName());
        adapterViewHolder.mAuthorRating.setRating(mData.get(position).getRating());
        adapterViewHolder.mReviewTime.setText(mData.get(position).getTime());
        adapterViewHolder.mReviewText.setText(mData.get(position).getReview());

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(ArrayList<PlaceReviews> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }
}

