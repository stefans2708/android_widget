package com.android.htec.widgettest;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.MonthDisplayHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.WeekViewHolder> {

    private List<WeekData> weeks;
    private int yearCount = 3;
    private DateTime selectedDay;
    private OnSelectedDateChangeListener listener;

    public WeekAdapter(int startYear, Context context, DateTime selectedDay, OnSelectedDateChangeListener listener) {
        generateWeeks(startYear, context);
        this.selectedDay = selectedDay;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeekViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeekViewHolder holder, int position) {
        initDays(weeks.get(position), holder);
    }

    @Override
    public int getItemCount() {
        return weeks == null ? 0 : weeks.size();
    }

    private void generateWeeks(int startYear, Context context) {
        MonthDisplayHelper monthDisplayHelper = new MonthDisplayHelper(startYear - 1, 11, Calendar.MONDAY);
        List<WeekData> weekDataList = new ArrayList<>();
        String[] monthNames = context.getResources().getStringArray(R.array.months);
        for (int year = startYear; year <= startYear + yearCount; year++) {
            for (int month = 0; month < 12; month++) {
                monthDisplayHelper.nextMonth();
                for (int week = 0; week < 6; week++) {
                    int[] digitsForRow = monthDisplayHelper.getDigitsForRow(week);
                    if ((week == 4 || week == 5) && (digitsForRow[0] < 10 || hasFirstInMonthToRight(digitsForRow, 0))) {
                        break;
                    } else {
                        weekDataList.add(new WeekData(year, month+1, monthNames[month], week));
                    }
                }
            }
        }
        this.weeks = weekDataList;
    }

    private void initDays(WeekData weekData, WeekViewHolder holder) {
        holder.linearLayoutWeek.removeAllViews();
        final MonthDisplayHelper monthDisplayHelper = new MonthDisplayHelper(weekData.getYear(), weekData.getMonthNumber() - 1, Calendar.MONDAY);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        buttonParams.weight = 1;

        final int[] digitsForWeek = monthDisplayHelper.getDigitsForRow(weekData.getWeekInMonth());
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDay(monthDisplayHelper, (DateTime) v.getTag());
            }
        };

        for (int day = 0; day < 7; day++) {
            Button btnDay = new Button(holder.itemView.getContext());
            btnDay.setLayoutParams(buttonParams);
            btnDay.setSingleLine();
            btnDay.setTextColor(Color.BLUE);
            btnDay.setText(String.valueOf(digitsForWeek[day]));
            DateTime thisMonth = new DateTime(monthDisplayHelper.getYear(), monthDisplayHelper.getMonth() + 1, 1, 0, 0);

            if (!monthDisplayHelper.isWithinCurrentMonth(weekData.getWeekInMonth(), day)) {
                if (hasFirstInMonthToRight(digitsForWeek, day)) {       //previous month
                    btnDay.setTag(thisMonth.minusMonths(1).withDayOfMonth(digitsForWeek[day]));
                } else { //next month
                    btnDay.setTag(thisMonth.plusMonths(1).withDayOfMonth(digitsForWeek[day]));
                }
            } else {
                btnDay.setTag(thisMonth.withDayOfMonth(digitsForWeek[day]));
            }

            btnDay.setOnClickListener(onClickListener);

            holder.linearLayoutWeek.addView(btnDay);
        }
    }

    private boolean hasFirstInMonthToRight(int[] weekDays, int selectedDayIndex) {
        int i = selectedDayIndex + 1;
        while (i < weekDays.length) {
            if (weekDays[i] == 1) {
                return true;
            }
            i++;
        }
        return false;
    }

    private void selectDay(MonthDisplayHelper monthDisplayHelper, DateTime dateTag) {
        if (selectedDay.getMonthOfYear() != dateTag.getMonthOfYear()) {
            if (dateTag.isBefore(selectedDay)) {
                monthDisplayHelper.previousMonth();
            } else if (dateTag.isAfter(selectedDay)) {
                monthDisplayHelper.nextMonth();
            }
        }
        selectedDay = dateTag;

        if (listener != null) {
            listener.onDaySelected(selectedDay);
        }
    }

    public WeekData getItem(int weekPosition) {
        return this.weeks.get(weekPosition);
    }

    class WeekViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout linearLayoutWeek;

        WeekViewHolder(View itemView) {
            super(itemView);
            linearLayoutWeek = itemView.findViewById(R.id.week);
        }
    }

    public interface OnSelectedDateChangeListener {
        void onDaySelected(DateTime selectedDay);
    }
}
