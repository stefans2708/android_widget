package com.android.htec.widgettest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.MonthDisplayHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class WeekCalendarActivity extends AppCompatActivity {

    private LinearLayout linearCalendarWeek;
    private TextView txtMonthName;
    private MonthDisplayHelper monthDisplayHelper;
    private int week;
    private DateTime today;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_calendar_activity);
        initViews();
        today = DateTime.now();
        monthDisplayHelper = new MonthDisplayHelper(today.getYear(), today.getMonthOfYear(), Calendar.MONDAY);
        week = monthDisplayHelper.getRowOf(today.getDayOfMonth());
        initDayArray();
        setClickListeners();
        setMonthName();
    }

    private void initViews() {
        linearCalendarWeek = findViewById(R.id.week_1);
        txtMonthName = findViewById(R.id.text_selected_week);
    }

    private void initDayArray() {
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        buttonParams.weight = 1;

        int[] digitsForWeek = monthDisplayHelper.getDigitsForRow(week);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = ((Button) v);
                DateTime dateTag = ((DateTime) button.getTag());
                if (dateTag.getMonthOfYear() < monthDisplayHelper.getMonth()) {
                    monthDisplayHelper.previousMonth();
                } else if (dateTag.getMonthOfYear() > monthDisplayHelper.getMonth()) {
                    monthDisplayHelper.nextMonth();
                }
                setMonthName();
            }
        };
        for (int day = 0; day < 7; day++) {
            Button btnDay = new Button(this);
            btnDay.setLayoutParams(buttonParams);
            btnDay.setSingleLine();
            btnDay.setTextColor(Color.BLUE);
            btnDay.setText(String.valueOf(digitsForWeek[day]));
            btnDay.setTag(new DateTime(monthDisplayHelper.getYear(), monthDisplayHelper.getMonth(), digitsForWeek[day], 0 ,0));
            btnDay.setOnClickListener(onClickListener);

            linearCalendarWeek.addView(btnDay);
        }
    }

    private void setClickListeners() {
        Button nextWeek = findViewById(R.id.button_next_week);
        Button prevWeek = findViewById(R.id.button_previous_week);

        nextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearCalendarWeek.removeAllViews();
                getNextWeek();
                initDayArray();
                setMonthName();
                Toast.makeText(WeekCalendarActivity.this, "month: " + monthDisplayHelper.getMonth() + "\nweek: " + week, Toast.LENGTH_SHORT).show();
            }
        });

        prevWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearCalendarWeek.removeAllViews();
                getPrevWeek();
                initDayArray();
                setMonthName();
                Toast.makeText(WeekCalendarActivity.this, "month: " + monthDisplayHelper.getMonth() + "\nweek: " + week, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMonthName() {
        DateFormatSymbols dfs = new DateFormatSymbols();
        txtMonthName.setText(dfs.getMonths()[monthDisplayHelper.getMonth()]);
    }

    private void getPrevWeek() {
        if (week == 0) {
            week = 5;
            monthDisplayHelper.previousMonth();
            int lastDate = monthDisplayHelper.getDigitsForRow(week)[6];
            while (lastDate < 15) {
                week--;
                lastDate = monthDisplayHelper.getDigitsForRow(week)[6];
            }
        } else {
            week -= 1;
        }

    }


    private void getNextWeek() {
        if (week == 5) {
            week = 0;
            monthDisplayHelper.nextMonth();
        } else {
            week += 1;
        }

        int firstDate = monthDisplayHelper.getDigitsForRow(week)[0];

        if (firstDate == 1) {
            week = 0;
            monthDisplayHelper.nextMonth();
        } else if (week == 5 && firstDate <= 8) {
            week = 1;
            monthDisplayHelper.nextMonth();
        } else if (week == 0 && firstDate >= 26) {
            week = 1;
        }

    }
}
