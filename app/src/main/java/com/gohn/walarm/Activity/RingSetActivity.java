package com.gohn.walarm.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gohn.walarm.Extention.ButtonEx;
import com.gohn.walarm.Extention.TextViewEx;
import com.gohn.walarm.Manager.AlarmDBMgr;
import com.gohn.walarm.Model.Colors;
import com.gohn.walarm.R;

import java.util.ArrayList;

public class RingSetActivity extends Activity implements View.OnClickListener {

    Context mContext;
    AlarmDBMgr dbMgr;
    ArrayList<ButtonEx> buttons = new ArrayList<ButtonEx>();
    ArrayList<TextViewEx> rings = new ArrayList<TextViewEx>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringset);

        mContext = getApplicationContext();
        dbMgr = AlarmDBMgr.getInstance(mContext);

        buttons.add((ButtonEx) findViewById(R.id.btn_weather_1));
        buttons.add((ButtonEx) findViewById(R.id.btn_weather_2));
        buttons.add((ButtonEx) findViewById(R.id.btn_weather_3));
        buttons.add((ButtonEx) findViewById(R.id.btn_weather_4));
        buttons.add((ButtonEx) findViewById(R.id.btn_weather_5));

        rings.add((TextViewEx) findViewById(R.id.text_ring_1));
        rings.add((TextViewEx) findViewById(R.id.text_ring_2));
        rings.add((TextViewEx) findViewById(R.id.text_ring_3));
        rings.add((TextViewEx) findViewById(R.id.text_ring_4));
        rings.add((TextViewEx) findViewById(R.id.text_ring_5));

        // 이 뷰에서 버튼 핸들링을 하겠다는 코드.
        for (int i = 0; i < buttons.size(); i++) {
            GradientDrawable drawable = (GradientDrawable)getResources().getDrawable(R.drawable.button_normal);
            drawable.setColor(Colors.getRingColor(i));

            buttons.get(i).setBackground(getResources().getDrawable(R.drawable.button_stroke));
            buttons.get(i).setOnClickListener(this);
        }

        updateRingName();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void updateRingName() {
        for (int i = 0; i < rings.size(); i++) {
            Ringtone ringtone = RingtoneManager.getRingtone(mContext, dbMgr.getRing(i));
            String title = ringtone.getTitle(mContext);
            rings.get(i).setText(title);
        }
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM|RingtoneManager.TYPE_RINGTONE);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));

        switch (v.getId()) {
            case R.id.btn_weather_1:
                startActivityForResult(intent, 0);
                break;
            case R.id.btn_weather_2:
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_weather_3:
                startActivityForResult(intent, 2);
                break;
            case R.id.btn_weather_4:
                startActivityForResult(intent, 3);
                break;
            case R.id.btn_weather_5:
                startActivityForResult(intent, 4);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 유저가 선택을 취소 했을때.
        if ( data == null ) return;

        // 벨소리 선택 팝업에서 선택된 알람이 인텐트에 담겨온다.
        Uri uri = (Uri)data.getExtras().get(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

        if ( uri == null ) return;

        Ringtone ringtone = RingtoneManager.getRingtone(mContext, uri);

        dbMgr.updateRing(requestCode, uri.toString());

        updateRingName();

        Log.e("gohn", "req code : " + requestCode);
        Log.e("gohn", "data : " + uri);
    }
}
