package com.android.htec.widgettest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private Button[] days;
    private LinearLayout[] weeks;
    private Calendar currentCalendar;

    private enum MondayFirstDays {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY;

        public static int convertFromCalendar(int calendarDay) {
            return calendarDay == Calendar.SUNDAY ? SUNDAY.ordinal() : MondayFirstDays.values()[calendarDay - 1].ordinal();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        setCalendarDays();
    }

    private void setCalendarDays() {
        currentCalendar = Calendar.getInstance();
        initWeekArray();
        initDayArray();
        initButtons();
        setDates();
    }

    private void setCurrentMonthData() {
        TextView textCurrentMonth = findViewById(R.id.text_selected_date);
        DateFormatSymbols dfs = new DateFormatSymbols();
        textCurrentMonth.setText(dfs.getMonths()[currentCalendar.get(Calendar.MONTH)]);
    }

    private void initButtons() {
        Button nextMonth = findViewById(R.id.button_next_month);
        Button prevMonth = findViewById(R.id.button_previous_month);

        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendar.add(Calendar.MONTH, 1);
                setDates();
            }
        });

        prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendar.add(Calendar.MONTH, -1);
                setDates();
            }
        });
    }

    private void initWeekArray() {
        LinearLayout firstWeekLayout = findViewById(R.id.calendar_week_1);
        LinearLayout secondWeekLayout = findViewById(R.id.calendar_week_2);
        LinearLayout thirdWeekLayout = findViewById(R.id.calendar_week_3);
        LinearLayout fourthWeekLayout = findViewById(R.id.calendar_week_4);
        LinearLayout fifthWeekLayout = findViewById(R.id.calendar_week_5);
        LinearLayout sixthWeekLayout = findViewById(R.id.calendar_week_6);

        weeks = new LinearLayout[6];

        weeks[0] = firstWeekLayout;
        weeks[1] = secondWeekLayout;
        weeks[2] = thirdWeekLayout;
        weeks[3] = fourthWeekLayout;
        weeks[4] = fifthWeekLayout;
        weeks[5] = sixthWeekLayout;
    }

    private void initDayArray() {
        days = new Button[6 * 7];

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.weight = 1;

        for (int week = 0; week < 6; week++) {
            for (int day = 0; day < 7; day++) {
                Button btnDay = new Button(this);
                btnDay.setLayoutParams(buttonParams);
                btnDay.setSingleLine();
                btnDay.setTextColor(Color.BLUE);

                days[week * 7 + day] = btnDay;
                weeks[week].addView(btnDay);
            }
        }

    }

    /**
     * firstDayOfMonth - first day in first week, for example, Monday = 1
     * lastDateOfMonth - number of days in current month
     * daysFromPreviousMonth - number of days in first row to be displayed from previous month
     * lastDateOfPreviousMonth - number of days in previous month
     */
    private void setDates() {
        setCurrentMonthData();
        int today = currentCalendar.get(Calendar.DAY_OF_MONTH);
        currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = MondayFirstDays.convertFromCalendar(currentCalendar.get(Calendar.DAY_OF_WEEK));
        int lastDateOfMoth = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int daysFromPreviousMonth = firstDayOfMonth - 1;

        Calendar previousMonth = ((Calendar) currentCalendar.clone());
        previousMonth.add(Calendar.MONTH, -1);
        int lastDateOfPreviousMoth = previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

        int counter = 0;
        for (int i = daysFromPreviousMonth - 1; i >= 0; i--) {
            days[counter++].setText(String.valueOf(lastDateOfPreviousMoth - i));
        }

        for (int i = 1; i <= lastDateOfMoth; i++) {
            days[counter++].setText(String.valueOf(i));
        }

        counter = 1;
        for (int i = lastDateOfMoth + daysFromPreviousMonth; i < 42; i++) {
            days[i].setText(String.valueOf(counter++));
        }

        currentCalendar.set(Calendar.DAY_OF_MONTH, today);
    }


}
