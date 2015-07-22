package com.gohn.walarm.Scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.gohn.walarm.Activity.FireActivity;
import com.gohn.walarm.Manager.AlarmDBMgr;
import com.gohn.walarm.Model.Alarm;
import com.gohn.walarm.Model.Days;
import com.gohn.walarm.Model.Flags;

import java.util.Calendar;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;  // 알람에 하나에 대한 인텐트


    @Override
    public void onReceive(final Context context, Intent intent) {

        int number = intent.getExtras().getInt(Flags.ALARMNUMBER);
        int days = intent.getExtras().getInt(Flags.ALARMDAYS);
        int options = intent.getExtras().getInt(Flags.ALARMOPTIONS);

        Log.e("gohn", "Number : " + number + ", Days : " + days);

        Alarm a = AlarmDBMgr.getInstance(context).getAlarm(number);

        // 알람을 울리는 날이 아니면 아무것도 안함.
        // 캘린더는 일월화수목금토 가 1,2,3,4,5,6,7 로 되어있음
        // 배열에 맞추기 위해 -1을 함
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        day = Days.DAYLIST[day];
        Log.e("gohn", "Receive DAY : " + day);
        Log.e("gohn", "Options : " + options);

        if ((days & day) != day) return;

        // 알람이 꺼져있으면 아무것도 안함
        if (a.IsOn == 0) return;

        // FireActivity에 모든 알람 액션을 위임한다.
        intent.setClass(context, FireActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // 로컬 푸시
        Intent service = new Intent(context, SchedulingService.class);
        startWakefulService(context, service);
    }

    public void setAlarm(Context context, Alarm a) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(Flags.ALARMNUMBER, a.No);
        intent.putExtra(Flags.ALARMDAYS, a.Days);
        intent.putExtra(Flags.ALARMOPTIONS, a.Options);
        alarmIntent = PendingIntent.getBroadcast(context, a.No, intent, 0);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        Log.e("gohn", "@@@@@ Hour : " + a.Hour + ", Minute : " + a.Minute + ", Days : " + a.Days + ", Options : " + a.Options);

        // Set the alarm
        calendar.set(Calendar.HOUR_OF_DAY, a.Hour);
        calendar.set(Calendar.MINUTE, a.Minute);

        // API 버전이 19보다 낮은 경우와 높은 경우에 부르는 함수가 다르다.
        // 그 이유는 setInexactRepeating만 호출하면 19보다 낮은 기기는 알람이 지연됨.
        if (Build.VERSION.SDK_INT <= 19) {
            // SystemClock.elapsedRealtime() 가 추가 된 이유는
            // setRepeating이 현재 시간 이전을 등록하면 알람이 바로 울려버린다.
            // 알람 시간이 현재 시간 보다 작으면 하루를 추가 해서 시작하도록 한것.
            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                calendar.add(Calendar.DATE, 1);
            }
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        } else {
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        }
        Log.e("gohn", String.format("SystemClock.elapsedRealtime() : %ds", SystemClock.elapsedRealtime() / 1000));
        Log.e("gohn", "Set DAY : " + a.Days);
        Log.e("gohn", "Alarm Number : " + a.No);

        // 부트 리시버에 등록해서 부팅되면 이 앱의 리시버를 시스템에 등록 한다.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        // 뒤지지 않도록 설정.
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }


    public void setSnooze(Context context, Intent intent) {

        Intent i = new Intent(context, AlarmReceiver.class);
        i.putExtra(Flags.ALARMNUMBER, intent.getExtras().getInt(Flags.ALARMNUMBER));
        i.putExtra(Flags.ALARMDAYS, intent.getExtras().getInt(Flags.ALARMDAYS));
        i.putExtra(Flags.ALARMOPTIONS, intent.getExtras().getInt(Flags.ALARMOPTIONS));
        alarmIntent = PendingIntent.getBroadcast(context, intent.getExtras().getInt(Flags.ALARMNUMBER), i, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000 * 60 * 5, alarmIntent);
    }


    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);
        }

        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the 
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
