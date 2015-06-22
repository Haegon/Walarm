package com.gohn.walarm.Model;

import com.gohn.walarm.Manager.AlarmDBMgr;

/**
 * Created by Gohn on 2015. 6. 20..
 */
public class Alarm {

    public int Afternoon;
    public int Hour;
    public int Minute;
    public int No;
    public int IsOn;

    public Alarm() {
        Afternoon = 0;
        Hour = 0;
        Minute = 0;
        No = 0;
        IsOn = 0;
    }

    public Alarm(int afternoon, int hour, int minute, int ison) {
        //int lastSeq = AlarmDBMgr.getInstance().getLastNo();
        No = AlarmDBMgr.getInstance().getLastNo() + 1;
        Afternoon = afternoon;
        Hour = hour;
        Minute = minute;
        IsOn = ison;
        //No = AlarmDBMgr.getInstance().getLastNo()+1;
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
