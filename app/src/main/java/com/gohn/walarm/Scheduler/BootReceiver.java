package com.gohn.walarm.Scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gohn.walarm.Manager.AlarmDBMgr;
import com.gohn.walarm.Model.Alarm;

import java.util.ArrayList;

// 부팅후 시스템에서 실행시켜주는 리시버
public class BootReceiver extends BroadcastReceiver {
    AlarmReceiver alarm = new AlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            // DB에서 알람을 싹다 가져온다.
            ArrayList<Alarm> alarmList = AlarmDBMgr.getInstance(context).getAlarms();

            for ( int i = 0 ; i < alarmList.size() ; i ++ ) {

                // 알람 하나하나에 대해서 알람이 켜져 있는 경우만 알람 등록을 해준다.
                Alarm a = alarmList.get(i);

                if ( a.IsOn == 1 ) {
                    Log.e("gohn", "@@@@@ " + a.No);
                    alarm.setAlarm(context, a.No, a.Hour,a.Minute);
                }
            }
        }
    }
}