package com.gohn.walarm.Model;

/**
 * Created by HaegonKoh on 2015. 6. 25..
 */
public interface Flags {
    int ADD = 1;
    int MODIFY = ADD<<1;

    String ALARMSETINTENT = "AlarmSetIntent";

    String ALARMNAME = "AlarmName";
    String ALARMHOUR = "AlarmHour";
    String ALARMMINUTE = "AlarmMinute";
    String ALARMNUMBER = "AlarmNumber";
    String ALARMDAYS = "AlarmDays";
}
