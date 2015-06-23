package com.gohn.walarm.Scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// BEGIN_INCLUDE(autostart)
public class BootReceiver extends BroadcastReceiver {
    AlarmReceiver alarm = new AlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarm.setAlarm(context,0,0,0);
        }
    }
}
//END_INCLUDE(autostart)
