package com.android.htec.widgettest;

import org.joda.time.DateTime;

public class WeekData {

    private int year;
    private int monthNumber;
    private String monthName;
    private int weekInMonth;
    private static DateTime selectedDay;

    public WeekData(int year, int monthNumber, String monthName, int weekInMonth) {
        this.year = year;
        this.monthNumber = monthNumber;
        this.monthName = monthName;
        this.weekInMonth = weekInMonth;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(int monthNumber) {
        this.monthNumber = monthNumber;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public int getWeekInMonth() {
        return weekInMonth;
    }

    public void setWeekInMonth(int weekInMonth) {
        this.weekInMonth = weekInMonth;
    }

    public static DateTime getSelectedDay() {
        return selectedDay;
    }

    public static void setSelectedDay(DateTime selectedDay) {
        WeekData.selectedDay = selectedDay;
    }
}
