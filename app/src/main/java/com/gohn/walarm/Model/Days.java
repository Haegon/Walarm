package com.gohn.walarm.Model;

/**
 * Created by HaegonKoh on 2015. 6. 25..
 */
public interface Days {
    int SUNDAY = 1;
    int MONDAY = SUNDAY<<1;
    int TUESDAY = SUNDAY<<2;
    int WEDNESDAY = SUNDAY<<3;
    int THURSDAY = SUNDAY<<4;
    int FRIDAY = SUNDAY<<5;
    int SATURDAY = SUNDAY<<6;

    int MINUTEOFWEEK = 7 * 24 * 60;
    int MINUTEOFDAY = 24 * 60;
    int MINUTEOFHOUR = 60;

    int[] DAYLIST = {SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY};
}
