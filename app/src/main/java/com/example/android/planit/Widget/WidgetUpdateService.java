package com.example.android.planit.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.android.planit.R;
import com.example.android.planit.database.AppDatabase;
import com.example.android.planit.models.BucketList;

import java.util.ArrayList;


public class WidgetUpdateService extends IntentService {

    private static final String TAG = WidgetUpdateService.class.getSimpleName();
    public static final String ACTION_UPDATE_RECIPE_WIDGETS = "com.example.android.planit.action.update_widget";
    private static final String ADD_RECIPE = "addedIngredient";

    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }

    public static void startActionUpdate(Context context) {
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPE_WIDGETS.equals(action)) {
                handleActionUpdateWidgets();
            }
        }
    }

    private void handleActionUpdateWidgets() {

        AppDatabase mDb = AppDatabase.getBucketListDbInstance(getApplicationContext());
        ArrayList<BucketList> mBuckets = (ArrayList) mDb.bucketListDao().loadWidgetBucketLists();
        Log.d(TAG, mBuckets.size()+"");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, PlanItWidget.class));

        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);

        if (mBuckets.size() > 0) {
            PlanItWidget.updateWidget(this, appWidgetManager, mBuckets.get(0).getName(), appWidgetIds);
        } else {
            PlanItWidget.updateWidget(this, appWidgetManager, null, appWidgetIds);
        }
    }
}
