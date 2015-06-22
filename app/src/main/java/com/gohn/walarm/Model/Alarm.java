package com.gohn.walarm.Model;

import java.sql.Time;

/**
 * Created by Gohn on 2015. 6. 20..
 */
public class Alarm {

    public String AlarmTime;
    public int No;
    public boolean IsOn;

    public Alarm() {

        AlarmTime = "2015-01-01";
        No = 0;
        IsOn = false;
    }

    public Alarm(String alarmTime, int no, boolean ison) {
        AlarmTime = alarmTime;
        No = no;
        IsOn = ison;
    }
}
