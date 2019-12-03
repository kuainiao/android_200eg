package com.mingrisoft.alarmclock.scroll;


public class TimeInDay {
    private int hour;
    private int min;

    public TimeInDay(int hour, int min) {
        this.hour = hour;
        this.min = min;
    }



    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public int getListPosition() {
        return hour * 60 + min;
    }

    @Override
    public String toString() {
        String hourStr = hour < 10 ? "0" + hour : "" + hour;
        String minStr = min < 10 ? "0" + min : "" + min;
        return hourStr + "   " + minStr;
    }
}
