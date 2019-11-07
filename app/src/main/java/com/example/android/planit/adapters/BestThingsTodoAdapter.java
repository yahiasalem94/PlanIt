package com.example.android.planit.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.planit.Constants;
import com.example.android.planit.R;
import com.example.android.planit.utils.NetworkUtils;
import com.example.android.planit.models.PointsOfInterests;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.Places;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BestThingsTodoAdapter extends RecyclerView.Adapter<BestThingsTodoAdapterViewHolder> {

    private static final String TAG = BestThingsTodoAdapter.class.getSimpleName();

    private ArrayList<PointsOfInterests> mPoiData;
    private final BestThingsTodoAdapterOnClickHandler mClickHandler;
    private Context context;
    private NavController navController;
    private Picasso picasso;


    public interface BestThingsTodoAdapterOnClickHandler {
        void onClick(View view, int position);
    }


    public BestThingsTodoAdapter(BestThingsTodoAdapterOnClickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
        this.context = context;
        picasso = NetworkUtils.picassoClient(context);
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

        if (mPoiData.get(position).getPhoto() != null) {
            String url = NetworkUtils.buildGooglePhotoUrl(mPoiData.get(position).getPhoto().get(0).getWidth(),
                    mPoiData.get(position).getPhoto().get(0).getPhotoReference());

            picasso.get()
                    .load(NetworkUtils.buildGooglePhotoUrl(mPoiData.get(position).getPhoto().get(0).getWidth(),
                            mPoiData.get(position).getPhoto().get(0).getPhotoReference()))
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(adapterViewHolder.mPoiPhoto, new Callback() {
                        @Override
                        public void onSuccess() {
                            adapterViewHolder.mNameOfPoi.setText(mPoiData.get(position).getName());
                            adapterViewHolder.mPoiPhoto.setAdjustViewBounds(true);
                        }

                        @Override
                        public void onError(Exception e) {
                            adapterViewHolder.mNameOfPoi.setVisibility(View.INVISIBLE);
                            adapterViewHolder.mNameLayout.setVisibility(View.INVISIBLE);
                        }
                    });
        } else {
            adapterViewHolder.mPoiPhoto.setImageResource(R.drawable.no_image);
            adapterViewHolder.mNameOfPoi.setText(mPoiData.get(position).getName());
            adapterViewHolder.mPoiPhoto.setAdjustViewBounds(true);
        }
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
