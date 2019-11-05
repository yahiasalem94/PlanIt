package com.example.android.planit.Widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.planit.R;
import com.example.android.planit.database.AppDatabase;
import com.example.android.planit.models.BucketList;
import com.example.android.planit.ui.BucketListFragment;

import java.util.ArrayList;


public class ListWidgetService extends RemoteViewsService {



    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }


}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String ADD_RECIPE = "addedIngredient";
    private static final String FRAGMENT_KEY = "fragmentKey";
    private static final String FRAGMENT_VALUE = "RecipeStep";

    Context mContext;
    ArrayList<BucketList> mBuckets;
    AppDatabase mDb;
    public ListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
        mDb = AppDatabase.getBucketListDbInstance(mContext);
    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {

        mBuckets = (ArrayList) mDb.bucketListDao().loadWidgetBucketLists();

    }

    @Override
    public void onDestroy() {
        mBuckets = null;
    }

    @Override
    public int getCount() {
        if (mBuckets == null) return 0;
        return mBuckets.size();
    }


    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (mBuckets == null || mBuckets.size() == 0) return null;

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.bucket_list_widget);

        views.setTextViewText(R.id.bucket_name, mBuckets.get(position).getName());

        // Fill in the onClick PendingIntent Template using the specific info for each item individually
        Bundle extras = new Bundle();
        extras.putString(BucketListFragment.BUCKET_LIST_NAME, mBuckets.get(position).getName());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_linear_layout, fillInIntent);

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

