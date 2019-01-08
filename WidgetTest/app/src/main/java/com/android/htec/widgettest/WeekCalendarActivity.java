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
    private int month;
    private int year;
    private DateTime today;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_calendar_activity);
        initViews();
        today = DateTime.now();
        month = today.getMonthOfYear();
        year = today.getYear();
        monthDisplayHelper = new MonthDisplayHelper(year, month, Calendar.MONDAY);
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
        monthDisplayHelper = new MonthDisplayHelper(year, month, Calendar.MONDAY);  //todo - use nextmonth and prevmonth methods

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        buttonParams.weight = 1;

        int[] digitsForWeek = monthDisplayHelper.getDigitsForRow(week);
        for (int day = 0; day < 7; day++) {
            Button btnDay = new Button(this);
            btnDay.setLayoutParams(buttonParams);
            btnDay.setSingleLine();
            btnDay.setTextColor(Color.BLUE);
            btnDay.setText(String.valueOf(digitsForWeek[day]));

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
                getNextWeek2();
                initDayArray();
                setMonthName();
                Toast.makeText(WeekCalendarActivity.this, "month: " + month + "\nweek: " + week, Toast.LENGTH_SHORT).show();
            }
        });

        prevWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearCalendarWeek.removeAllViews();
                getPrevWeek();
                initDayArray();
                setMonthName();
                Toast.makeText(WeekCalendarActivity.this, "month: " + month + "\nweek: " + week, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMonthName() {
        DateFormatSymbols dfs = new DateFormatSymbols();
        txtMonthName.setText(dfs.getMonths()[month]);
    }

    private void getNextWeek() {
        if (week == 5) {
            week = 0;
            month++;
        } else {
            week += 1;
        }

        int[] digitsForRow = monthDisplayHelper.getDigitsForRow(week);
        int first = digitsForRow[0];
        if (week == 5 && first < 20) {
            if (first == 1) {
                week = 0;
            } else {
                week = 1;
            }
            month++;
        }
    }

    private void getPrevWeek() {
        if (week == 0) {
            week = 5;
            month--;
        } else {
            week -= 1;
        }

        int[] digitsForRow = monthDisplayHelper.getDigitsForRow(week);
        if (week == 5 && digitsForRow[0] < 20) {
            week--;
        }
    }


    private void getNextWeek2() {
        if (week == 5) {
            week = 0;
            month++;
        } else {
            week += 1;
        }

        int firstDate = monthDisplayHelper.getDigitsForRow(week)[0];

        if (firstDate == 1) {
            week = 0;
            month++;
        } else if ((week == 5 || week == 0) && firstDate <= 8) {
            week = 1;
            month++;
        }

    }
}
