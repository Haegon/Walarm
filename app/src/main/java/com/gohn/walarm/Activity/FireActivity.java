package com.gohn.walarm.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.gohn.walarm.Extention.ButtonEx;
import com.gohn.walarm.Extention.TextViewEx;
import com.gohn.walarm.Manager.AlarmDBMgr;
import com.gohn.walarm.Manager.LocateMgr;
import com.gohn.walarm.Model.Flags;
import com.gohn.walarm.Model.Weather;
import com.gohn.walarm.R;
import com.gohn.walarm.Scheduler.AlarmReceiver;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class FireActivity extends Activity implements View.OnClickListener {

    AlarmReceiver alarmReceiver = new AlarmReceiver();
    Vibrator vibe;
    MediaPlayer ring;
    ArrayList<TextViewEx> digits;
    int number;
    int options;
    int days;

    int[] question;
    int cur_location;

    Intent intent;

    void InitView() {
        // 버튼 이벤트 여기서 함
        findViewById(R.id.btn_num_ok).setOnClickListener(this);
        findViewById(R.id.btn_num_clear).setOnClickListener(this);
        findViewById(R.id.btn_snooze_1).setOnClickListener(this);
        findViewById(R.id.btn_snooze_2).setOnClickListener(this);
        findViewById(R.id.btn_num_0).setOnClickListener(this);
        findViewById(R.id.btn_num_1).setOnClickListener(this);
        findViewById(R.id.btn_num_2).setOnClickListener(this);
        findViewById(R.id.btn_num_3).setOnClickListener(this);
        findViewById(R.id.btn_num_4).setOnClickListener(this);
        findViewById(R.id.btn_num_5).setOnClickListener(this);
        findViewById(R.id.btn_num_6).setOnClickListener(this);
        findViewById(R.id.btn_num_7).setOnClickListener(this);
        findViewById(R.id.btn_num_8).setOnClickListener(this);
        findViewById(R.id.btn_num_9).setOnClickListener(this);

        digits = new ArrayList<TextViewEx>();
        digits.add((TextViewEx) findViewById(R.id.text_digit_1));
        digits.add((TextViewEx) findViewById(R.id.text_digit_2));
        digits.add((TextViewEx) findViewById(R.id.text_digit_3));
        digits.add((TextViewEx) findViewById(R.id.text_digit_4));

        // 문제 날짜 초기화
        question = getQuestion();
        for (int i = 0; i < digits.size(); i++) {
            digits.get(i).setText(String.format("%d", question[i]));
            digits.get(i).setTextColor(Color.GRAY);
        }

        cur_location = 0;
    }

    int[] getQuestion() {
        int[] q = new int[4];

        // Month에 1을 더한 이유는 안드로이드 month는 1월이 0이고 12월이 11임.. ㅜㅜ
        Calendar c = Calendar.getInstance();
        int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DAY_OF_MONTH);

        q[0] = m / 10;
        q[1] = m % 10;
        q[2] = d / 10;
        q[3] = d % 10;

        return q;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire);

        // 인텐트를 가져온다.
        intent = getIntent();

        // 화면이 꺼져있을때도 잠금 화면 위로 액티비티가 보이도록 하는 설정
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        // 뷰 초기화
        InitView();

        // 진동, 벨 옵션을 가져온다.
        options = getIntent().getExtras().getInt(Flags.ALARMOPTIONS);
        number = getIntent().getExtras().getInt(Flags.ALARMNUMBER);
        days = getIntent().getExtras().getInt(Flags.ALARMDAYS);

        // 진동을 울려주자
        if ((options & Flags.VIBRATION) == Flags.VIBRATION) {
            if (vibe == null) {
                vibe = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {1000, 1000, 1000, 1000, 1000, 1000};
                vibe.vibrate(pattern, 2);
            }
        }

        // 알람을 울려주자
        if ((options & Flags.RING) == Flags.RING) {

            // 위치 정보를 가져온다.
            LocateMgr locate = LocateMgr.getInstance(getApplicationContext());
            final double latitude = LocateMgr.getInstance(getApplicationContext()).getLatitude();
            final double longitude = LocateMgr.getInstance(getApplicationContext()).getLongitude();

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

                            ring = new MediaPlayer();
                            ring.setDataSource(getApplicationContext(), AlarmDBMgr.getInstance().getRing(Weather.get(wcode)));
                            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                            ring.setAudioStreamType(AudioManager.STREAM_ALARM);
                            ring.setLooping(true);
                            ring.prepare();
                            ring.start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_num_ok:
                if (cur_location == 4) {
                    Log.e("gohn", "@@@@@ Finish");
                    stop();
                    finish();
                }
                break;
            case R.id.btn_num_clear:
                InitView();
                break;
            case R.id.btn_num_0:
            case R.id.btn_num_1:
            case R.id.btn_num_2:
            case R.id.btn_num_3:
            case R.id.btn_num_4:
            case R.id.btn_num_5:
            case R.id.btn_num_6:
            case R.id.btn_num_7:
            case R.id.btn_num_8:
            case R.id.btn_num_9:
                ButtonEx btn = (ButtonEx) findViewById(v.getId());
                if (btn.getText().equals(String.format("%d", question[cur_location]))) {
                    digits.get(cur_location).setTextColor(Color.WHITE);
                    cur_location++;
                }
                break;
            case R.id.btn_snooze_1:
                if (intent != null) {
                    stop();
                    alarmReceiver.setSnooze(this, intent, 5);
                    Toast.makeText(this, "5분 후에 알람이 다시 울립니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Log.e("gohn", "Intent is null");
                break;
            case R.id.btn_snooze_2:
                if (intent != null) {
                    stop();
                    alarmReceiver.setSnooze(this, intent, 10);
                    Toast.makeText(this, "10분 후에 알람이 다시 울립니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Log.e("gohn", "Intent is null");
                break;
            default:
                Log.e("gohn", "Unknown Button");
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }

    void stop() {
        if (vibe != null) vibe.cancel();
        if (ring != null) ring.stop();
    }
}
