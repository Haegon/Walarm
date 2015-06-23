package com.gohn.walarm.Model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gohn.walarm.Manager.AlarmDBMgr;

import java.security.PrivilegedAction;

/**
 * Created by Gohn on 2015. 6. 20..
 */
public class Alarm {

    public int Afternoon;
    public int Hour;
    public int Minute;
    public int No;
    public int IsOn;

    private static final String INTENT_ACTION = "aa";

    public Alarm() {
        Afternoon = 0;
        Hour = 0;
        Minute = 0;
        No = 0;
        IsOn = 0;
    }

    public Alarm(Context context, int afternoon, int hour, int minute, int ison) {
        //int lastSeq = AlarmDBMgr.getInstance().getLastNo();
        No = AlarmDBMgr.getInstance().getLastNo() + 1;
        Afternoon = afternoon;
        Hour = hour;
        Minute = minute;
        IsOn = ison;

        setAlarm(context,3000);
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

    // 알람 등록
    private void setAlarm(Context context, long second){
        Log.i("gohn", "setAlarm()");
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent Intent = new Intent(INTENT_ACTION);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, Intent, 0);

        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + second, pIntent);
    }

    // 알람 해제
    private void releaseAlarm(Context context){
        Log.i("gohn", "releaseAlarm()");
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent Intent = new Intent(INTENT_ACTION);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, Intent, 0);
        alarmManager.cancel(pIntent);

        // 주석을 풀면 먼저 실행되는 알람이 있을 경우, 제거하고
        // 새로 알람을 실행하게 된다. 상황에 따라 유용하게 사용 할 수 있다.
//      alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 3000, pIntent);
    }
}
