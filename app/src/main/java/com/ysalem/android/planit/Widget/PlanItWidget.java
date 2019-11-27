package com.ysalem.android.planit.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.ysalem.android.planit.R;
import com.ysalem.android.planit.ui.MainActivity;

public class PlanItWidget extends AppWidgetProvider {

    private static final String FRAGMENT_KEY = "fragmentKey";
    private static final String FRAGMENT_VALUE = "RecipeStep";

    // setImageViewResource to update the widget’s image
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, String bucketName, int appWidgetId) {

        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int height = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        RemoteViews rv;
//        if (height < 100) {
//            rv = getSingleRemoteView(context, bucketName);
//        } else {
            rv = getListRemoteView(context);
//        }
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Start the intent service update widget action, the service takes care of updating the widgets UI
        WidgetUpdateService.startActionUpdate(context);
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, String bucketName, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, bucketName, appWidgetId);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {

        WidgetUpdateService.startActionUpdate(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Perform any action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled(Context context) {
        // Perform any action when the last AppWidget instance for this provider is deleted
    }

//    private static RemoteViews getSingleRemoteView(Context context, String bucketName) {
//
//        Intent intent = new Intent(context, MainActivity.class);
//        Bundle bundle = new Bundle();
////        bundle.putString(Constants.RECIPE_NAME, SharedPreferenceUtil.getRecipeNameFromSharedPrefsForKey(Constants.ADDED_RECIPE_NAME, context.getApplicationContext()));
////        bundle.putParcelableArrayList(Constants.STEPS_LIST, SharedPreferenceUtil.getRecipeStepsFromSharedPrefsForKey(Constants.ADDED_STEPS, context.getApplicationContext()));
////        bundle.putParcelableArrayList(Constants.INGREDIENTS_LIST, SharedPreferenceUtil.getIngredientsFromSharedPrefsForKey(Constants.ADDED_INGREDIENT, context.getApplicationContext()));
//        intent.putExtras(bundle);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bucket_list_widget);
//
//        if (bucketName != null) {
//            // Update image and text
//            views.setTextViewText(R.id.bucket_name, bucketName);
//
//            // Widgets allow click handlers to only launch pending intents
//            views.setOnClickPendingIntent(R.id.widget_linear_layout, pendingIntent);
//
//        } else {
//            views.setEmptyView(R.id.widget_linear_layout, R.id.empty_view);
//        }
//
//        return views;
//    }

    /**
     * Creates and returns the RemoteViews to be displayed in the GridView mode widget
     *
     * @param context The context
     * @return The RemoteViews for the GridView mode widget
     */
    private static RemoteViews getListRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_listview);
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, intent);

        // Set the MainActivity intent to launch when clicked
        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, pendingIntent);

        // Handle empty gardens
        views.setEmptyView(R.id.widget_list_view, R.id.empty_view);
        return views;
    }
}
