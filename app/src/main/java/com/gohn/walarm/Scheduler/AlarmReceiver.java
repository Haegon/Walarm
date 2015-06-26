package com.gohn.walarm.Scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.gohn.walarm.Manager.AlarmDBMgr;
import com.gohn.walarm.Model.Alarm;

import java.util.Calendar;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;  // 알람에 하나에 대한 인텐트

    Vibrator vibe;
  
    @Override
    public void onReceive(Context context, Intent intent) {
        int number = intent.getExtras().getInt("requestCode");
        Log.e("gohn", "request code : " + number);

        Alarm a = AlarmDBMgr.getInstance(context).getAlarm(number);

        // 알람이 꺼져있으면 아무것도 안함
        if ( a.IsOn == 0 ) return;

        // 진동을 울려주
        if ( vibe == null ) {
            vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
        vibe.vibrate(1000);

        // 토스트 메세지
        Toast.makeText(context, String.format("Hello! Alarm Time =>: %d:%d",a.Hour,a.Minute),Toast.LENGTH_SHORT).show();

        // 알람음
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();

        // 앱이 내려가 있어도 알람이 울릴 수 있게 해주는 부분
        Intent service = new Intent(context, SchedulingService.class);
        startWakefulService(context, service);
    }

    public void setAlarm(Context context,int no, int hour, int minute) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("requestCode", no);
        alarmIntent = PendingIntent.getBroadcast(context, no, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // Set the alarm
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
  
        // API 버전이 19보다 낮은 경우와 높은 경우에 부르는 함수가 다르다.
        // 그 이유는 setInexactRepeating만 호출하면 19보다 낮은 기기는 알람이 지연됨.
        if(Build.VERSION.SDK_INT < 19) {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                    // SystemClock.elapsedRealtime() 가 추가 된 이유는
                    // setRepeating이 현재 시간 이전을 등록하면
                    // 알람이 바로 울려버린다. 그래서 지금 시간 이후로 시작하도록 한것.
                    SystemClock.elapsedRealtime() + calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        } else {
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        }
        Log.e("gohn", "Alarm Number : " + no);

        // 부트 리시버에 등록해서 부팅되면 이 앱의 리시버를 시스템에 등록 한다.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        // 뒤지지 않도록 설정.
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);           
    }


    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
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
