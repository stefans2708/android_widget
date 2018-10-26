package com.android.htec.widgettest;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

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

        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, RC_WIDGET_CLICK_ACTION, intent, 0);

            SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
            String buttonText = prefs.getString(KEY_BUTTON_TEXT + appWidgetId, "Press me");

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.example_widget);
            remoteViews.setOnClickPendingIntent(R.id.button_widget, pendingIntent);
            remoteViews.setCharSequence(R.id.button_widget, "setText", buttonText);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

    }
}
