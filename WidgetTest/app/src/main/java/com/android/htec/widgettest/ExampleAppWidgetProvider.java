package com.android.htec.widgettest;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import static com.android.htec.widgettest.ExampleAppWidgetConfigActivity.KEY_BUTTON_TEXT;
import static com.android.htec.widgettest.MainActivity.RC_WIDGET_CLICK_ACTION;

public class ExampleAppWidgetProvider extends AppWidgetProvider {

    /**
     * Iako je konfiguracija widget-a prilikom prvog kreiranja prebacena u Config activity, ovde idalje treba da se izvrsava
     * setovanje onClick listener-a. To je potrebno za slucaj kad se telefon restartuje, a korisnik ima widget-e na home screen-u.
     * Tad nece da se pozove configure activity za svaki widget....
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Toast.makeText(context, "onUpdate", Toast.LENGTH_SHORT).show();

        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, RC_WIDGET_CLICK_ACTION, intent, 0);

            SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
            String buttonText = prefs.getString(KEY_BUTTON_TEXT + appWidgetId, "Press me");

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.example_widget);
            remoteViews.setOnClickPendingIntent(R.id.button_widget, pendingIntent);
            remoteViews.setCharSequence(R.id.button_widget, "setText", buttonText);

            Bundle appWidgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId);
            resizeWidget(appWidgetOptions, remoteViews);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

    }


    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Toast.makeText(context, "onAppWidgetOptionsChanged", Toast.LENGTH_SHORT).show();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_widget);
        resizeWidget(newOptions, views);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void resizeWidget(Bundle appWidgetOptions, RemoteViews views) {
        int maxWidth = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
        int minWidth = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        int minHeight = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        int maxHeight = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

        views.setViewVisibility(R.id.text_widget, maxHeight > 100 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Toast.makeText(context, "onDeleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context) {
        Toast.makeText(context, "onEnabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(Context context) {
        Toast.makeText(context, "onDisabled", Toast.LENGTH_SHORT).show();
    }

}
