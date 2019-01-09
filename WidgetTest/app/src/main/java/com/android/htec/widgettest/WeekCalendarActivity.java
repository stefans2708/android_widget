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
    private TextView txtMonthName, txtSelectedDay;
    private MonthDisplayHelper monthDisplayHelper;
    private int week;
    private DateTime selectedDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_calendar_activity);
        initViews();
        selectedDay = DateTime.now();
        monthDisplayHelper = new MonthDisplayHelper(selectedDay.getYear(), selectedDay.getMonthOfYear() - 1, Calendar.MONDAY);
        week = monthDisplayHelper.getRowOf(selectedDay.getDayOfMonth());
        initDayArray();
        setClickListeners();
        setMonthName();
    }

    private void initViews() {
        linearCalendarWeek = findViewById(R.id.week_1);
        txtMonthName = findViewById(R.id.text_selected_week);
        txtSelectedDay = findViewById(R.id.txt_selected_date);
    }

    private void initDayArray() {
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        buttonParams.weight = 1;

        final int[] digitsForWeek = monthDisplayHelper.getDigitsForRow(week);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDay((DateTime) v.getTag());
            }
        };

        for (int day = 0; day < 7; day++) {
            Button btnDay = new Button(this);
            btnDay.setLayoutParams(buttonParams);
            btnDay.setSingleLine();
            btnDay.setTextColor(Color.BLUE);
            btnDay.setText(String.valueOf(digitsForWeek[day]));
            DateTime thisMonth = new DateTime(monthDisplayHelper.getYear(), monthDisplayHelper.getMonth() + 1, 1, 0, 0);

            if (!monthDisplayHelper.isWithinCurrentMonth(week, day)) {
                if (hasFirstInMonthToRight(digitsForWeek, day)) {       //previous month
                    btnDay.setTag(thisMonth.minusMonths(1).withDayOfMonth(digitsForWeek[day]));
                } else { //next month
                    btnDay.setTag(thisMonth.plusMonths(1).withDayOfMonth(digitsForWeek[day]));
                }
            } else {
                btnDay.setTag(thisMonth.withDayOfMonth(digitsForWeek[day]));
            }

            btnDay.setOnClickListener(onClickListener);

            linearCalendarWeek.addView(btnDay);
        }
    }

    private boolean hasFirstInMonthToRight(int[] weekDays, int selectedDay) {
        int i = selectedDay + 1;
        while (i < weekDays.length) {
            if (weekDays[i] == 1) {
                return true;
            }
            i++;
        }
        return false;
    }

    private void setClickListeners() {
        Button nextWeek = findViewById(R.id.button_next_week);
        Button prevWeek = findViewById(R.id.button_previous_week);

        nextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearCalendarWeek.removeAllViews();
                selectDay(selectedDay.plusDays(7));
                initDayArray();
                Toast.makeText(WeekCalendarActivity.this, "month: " + monthDisplayHelper.getMonth() + "\nweek: " + week, Toast.LENGTH_SHORT).show();
            }
        });

        prevWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearCalendarWeek.removeAllViews();
                selectDay(selectedDay.minusDays(7));
                initDayArray();
                Toast.makeText(WeekCalendarActivity.this, "month: " + monthDisplayHelper.getMonth() + "\nweek: " + week, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMonthName() {
        DateFormatSymbols dfs = new DateFormatSymbols();
        txtMonthName.setText(dfs.getMonths()[monthDisplayHelper.getMonth()]);
    }

    private void selectDay(DateTime dateTag) {
        if (selectedDay.getMonthOfYear() != dateTag.getMonthOfYear()) {
            if (dateTag.isBefore(selectedDay)) {
                monthDisplayHelper.previousMonth();
            } else if (dateTag.isAfter(selectedDay)) {
                monthDisplayHelper.nextMonth();
            }
        }
        selectedDay = dateTag;
        week = monthDisplayHelper.getRowOf(selectedDay.getDayOfMonth());
        txtSelectedDay.setText(selectedDay.toString());
        setMonthName();
    }
}
