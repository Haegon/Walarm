package com.gohn.walarm.Scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gohn.walarm.Manager.AlarmDBMgr;
import com.gohn.walarm.Model.Alarm;

import java.util.ArrayList;

// BEGIN_INCLUDE(autostart)
public class BootReceiver extends BroadcastReceiver {
    AlarmReceiver alarm = new AlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            ArrayList<Alarm> alarmList = AlarmDBMgr.getInstance(context).getAlarms();

            for ( int i = 0 ; i < alarmList.size() ; i ++ ) {
                Alarm a = alarmList.get(i);

                if ( a.IsOn == 1 ) {
                    Log.e("gohn", "@@@@@ " + a.No);
                    alarm.setAlarm(context, a.No, a.Hour,a.Minute);
                }
            }
        }
    }
}
//END_INCLUDE(autostart)
