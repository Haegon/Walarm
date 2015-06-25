package com.gohn.walarm.Model;

import android.content.Context;

import com.gohn.walarm.Manager.AlarmDBMgr;

/**
 * Created by Gohn on 2015. 6. 20..
 */
public class Alarm {

    public static String FLAGNUMBER = "no";
    public static String FLAGHOUR = "hour";
    public static String FLAGMINUTE = "minute";

    public int Hour;
    public int Minute;
    public int No;
    public int IsOn;

    private static final String INTENT_ACTION = "aa";

    public Alarm() {
        Hour = 0;
        Minute = 0;
        No = 0;
        IsOn = 0;
    }

    public Alarm(Context context, int hour, int minute, int ison) {
        No = AlarmDBMgr.getInstance().getLastNo() + 1;
        Hour = hour;
        Minute = minute;
        IsOn = ison;
    }

    public void On() {
        IsOn = 1;
        AlarmDBMgr.getInstance().onoffAlarm(this);
    }

    public void Off() {
        IsOn = 0;
        AlarmDBMgr.getInstance().onoffAlarm(this);
    }
}
