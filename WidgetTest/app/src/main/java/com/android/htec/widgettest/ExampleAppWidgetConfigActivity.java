package com.android.htec.widgettest;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

import static com.android.htec.widgettest.MainActivity.RC_WIDGET_CLICK_ACTION;

public class ExampleAppWidgetConfigActivity extends AppCompatActivity {


    public static String KEY_BUTTON_TEXT = "KEY_BUTTON_TEXT";
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private EditText editTextConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_app_widget_config);

        Intent configIntent = getIntent(); //intent sa podesavanjima koji se dobija kad se doda widget na homescreen
        Bundle bundle = configIntent.getExtras();
        if (bundle != null) {
            appWidgetId = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        //default ponasanje je da ako se ne vrati RESULT_OK, smatra se da je rezultat RESULT_CANCELED, medjutim,
        //na nekim telefonima moze da crash-uje ako se ne vrati eksplicitno rezultat (tj. definise kao RESULT_CANCELED).
        //ako bude sve ok, ovaj result ce biti override-ovan u f-ji confirmConfiguration
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        editTextConfig = findViewById(R.id.edit_widget_text_config);
    }

    public void confirmConfiguration(View v) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, RC_WIDGET_CLICK_ACTION, intent, 0);

        String buttonText = editTextConfig.getText().toString();
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.example_widget);
        remoteViews.setOnClickPendingIntent(R.id.button_widget, pendingIntent);
        remoteViews.setCharSequence(R.id.button_widget, "setText", buttonText);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        prefs.edit().putString(KEY_BUTTON_TEXT + appWidgetId, buttonText).apply();

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

}
