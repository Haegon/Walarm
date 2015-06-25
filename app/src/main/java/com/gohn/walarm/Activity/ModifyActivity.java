package com.gohn.walarm.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;

import com.gohn.walarm.Manager.AlarmDBMgr;
import com.gohn.walarm.Model.Alarm;
import com.gohn.walarm.R;
import com.gohn.walarm.Scheduler.AlarmReceiver;

public class ModifyActivity extends Activity {

    TimePicker tpModify = null;
    AlarmReceiver alarmReceiver = new AlarmReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        tpModify = (TimePicker) findViewById(R.id.timePicker_modify);

        int hour = getIntent().getExtras().getInt(Alarm.FLAGHOUR);
        int min = getIntent().getExtras().getInt(Alarm.FLAGMINUTE);

        tpModify.setCurrentHour(hour);
        tpModify.setCurrentMinute(min);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modify, menu);
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

        int no = getIntent().getExtras().getInt(Alarm.FLAGNUMBER);
        int hour = tpModify.getCurrentHour();
        int min = tpModify.getCurrentMinute();

        switch (id) {
            case R.id.action_modify:
               // 기존 알람 삭제
                AlarmDBMgr.getInstance(this).delAlarm(no);

                // 새 알람 추가
                Alarm a = new Alarm(this,hour,min,1);
                AlarmDBMgr.getInstance(this).addAlarm(a);
                alarmReceiver.setAlarm(this, a.No, hour, min);

                GoHome();
                return true;
            case R.id.action_delete:
                // 기존 알람 삭제
                AlarmDBMgr.getInstance(this).delAlarm(no);

                GoHome();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void GoHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
