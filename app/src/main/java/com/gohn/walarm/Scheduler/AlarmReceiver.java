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
import com.gohn.walarm.Manager.LocateMgr;
import com.gohn.walarm.Model.Alarm;
import com.gohn.walarm.Model.Days;
import com.gohn.walarm.Model.Flags;
import com.gohn.walarm.Model.Weather;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;  // 알람에 하나에 대한 인텐트

    Vibrator vibe;

    @Override
    public void onReceive(final Context context, Intent intent) {

        // 알람의 셋팅 값을 가져온다.
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

        // 진동을 울려주자
        if ((options & Flags.VIBRATION) == Flags.VIBRATION) {
            if (vibe == null) {
                vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(1000);
            }
        }

        // 토스트 메세지
        Toast.makeText(context, String.format("Hello! Alarm Time =>: %d:%d", a.Hour, a.Minute), Toast.LENGTH_SHORT).show();

        // 알람음
        if ((options & Flags.RING) == Flags.RING) {

            // 위치 정보를 가져온다.
            LocateMgr locate = LocateMgr.getInstance(context);
            final double latitude = locate.getInstance(context).getLatitude();
            final double longitude = locate.getInstance(context).getLongitude();

            // 벨소리를 울린다.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {

                        // 위도 경도를 기준으로 현재 날씨 코드를 가져온다.
                        HttpClient client = new DefaultHttpClient();
                        String getURL = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f", latitude, longitude);
                        HttpGet get = new HttpGet(getURL);
                        HttpResponse responseGet = client.execute(get);
                        HttpEntity resEntityGet = responseGet.getEntity();
                        if (resEntityGet != null) {
                            // 결과를 처리합니다.
                            String res = EntityUtils.toString(resEntityGet);

                            JSONObject jObject = new JSONObject(res);
                            JSONArray weather = jObject.getJSONArray("weather");
                            JSONObject item = (JSONObject) weather.get(0);
                            int wcode = item.getInt("id");
                            String icon = item.getString("icon");

                            Log.e("ID", "weather code : " + wcode + " , weather : " + Weather.get(wcode));

                            // 여기서 실제로 알람 울림
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone r = RingtoneManager.getRingtone(context, notification);
                            r.play();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        // 앱이 내려가 있어도 알람이 울릴 수 있게 해주는 부분
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

        // Set the alarm
        calendar.set(Calendar.HOUR_OF_DAY, a.Hour);
        calendar.set(Calendar.MINUTE, a.Minute);

        // API 버전이 19보다 낮은 경우와 높은 경우에 부르는 함수가 다르다.
        // 그 이유는 setInexactRepeating만 호출하면 19보다 낮은 기기는 알람이 지연됨.
        if (Build.VERSION.SDK_INT < 19) {
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
