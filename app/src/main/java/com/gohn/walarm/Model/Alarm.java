package com.gohn.walarm.Model;

import android.content.Context;

import com.gohn.walarm.Manager.AlarmDBMgr;

/**
 * Created by Gohn on 2015. 6. 20..
 */
public class Alarm {

    public String Name;        // 알람 이름
    public int Hour;        // 알람 시간
    public int Minute;      // 알람 분
    public int No;          // 알람 번호
    public int IsOn;        // 알람 온오프 상태
    public int Options;   // 진동-벨소리 선택
    public int Days;        // 날짜 선택
    public int Volume;      // 음량

    private static final String INTENT_ACTION = "aa";

    public Alarm() {
        Hour = 0;
        Minute = 0;
        No = 0;
        IsOn = 0;
    }

    public Alarm(Context context, String name, int hour, int minute, int days, int ison, int options) {
        Name = name;
        No = AlarmDBMgr.getInstance().getLastNo() + 1;
        Hour = hour;
        Minute = minute;
        Days = days;
        IsOn = ison;
        Options = options;
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
