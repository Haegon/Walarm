package com.gohn.walarm.Activity;

import android.app.Activity;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.gohn.walarm.Manager.AlarmDBMgr;
import com.gohn.walarm.Manager.LocateMgr;
import com.gohn.walarm.Model.Flags;
import com.gohn.walarm.Model.Weather;
import com.gohn.walarm.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class FireActivity extends Activity implements View.OnClickListener{

    Vibrator vibe;
    Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire);

        // 화면이 꺼져있을때도 잠금 화면 위로 액티비티가 보이도록 하는 설정
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        // 버튼 이벤트 여기서 함
        ((Button)findViewById(R.id.btn_off)).setOnClickListener(this);

        // 진동, 벨 옵션을 가져온다.
        int options = getIntent().getExtras().getInt(Flags.ALARMOPTIONS);

        // 진동을 울려주자
        if ((options & Flags.VIBRATION) == Flags.VIBRATION) {
            if (vibe == null) {
                vibe = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(1000);
            }
        }

        // 알람을 울려주자
        if ((options & Flags.RING) == Flags.RING) {

            // 위치 정보를 가져온다.
            LocateMgr locate = LocateMgr.getInstance(getApplicationContext());
            final double latitude = locate.getInstance(getApplicationContext()).getLatitude();
            final double longitude = locate.getInstance(getApplicationContext()).getLongitude();

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
                            // 날씨 코드로부터 날씨를 가져오고, 날씨로 날씨의 벨소리를 가져와서 울려준다.
                            ringtone = RingtoneManager.getRingtone(getApplicationContext(), AlarmDBMgr.getInstance().getRing(Weather.get(wcode)));
                            ringtone.play();
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
            case R.id.btn_off:
                ringtone.stop();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
