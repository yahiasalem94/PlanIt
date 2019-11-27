package com.ysalem.android.planit.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ysalem.android.planit.R;

public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mProfilePhoto;
        public final TextView mAuthorName;
        public final RatingBar mAuthorRating;
        public final TextView mReviewTime;
        public final TextView mReviewText;
        private final ReviewsAdapter.ReviewsAdapterOnClickHandler mClickHandler;

        public ReviewsAdapterViewHolder(View view, ReviewsAdapter.ReviewsAdapterOnClickHandler clickHandler) {
            super(view);
            mProfilePhoto = view.findViewById(R.id.profile_image);
            mAuthorName = view.findViewById(R.id.authorName);
            mAuthorRating = view.findViewById(R.id.ratingBar);
            mReviewTime = view.findViewById(R.id.time);
            mReviewText = view.findViewById(R.id.reviewText);

            this.mClickHandler = clickHandler;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition());
        }

    }
