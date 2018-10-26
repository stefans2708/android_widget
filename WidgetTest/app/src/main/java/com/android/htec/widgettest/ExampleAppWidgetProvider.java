package com.android.htec.widgettest;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class ExampleAppWidgetProvider extends AppWidgetProvider {

    private static final int RC_WIDGET_CLICK_ACTION = 1;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, RC_WIDGET_CLICK_ACTION, intent, 0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.example_widget);
            remoteViews.setOnClickPendingIntent(R.id.button_widget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

    }
}
