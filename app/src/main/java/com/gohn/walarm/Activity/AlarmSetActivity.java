package com.gohn.walarm.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;

import com.gohn.walarm.Manager.AlarmDBMgr;
import com.gohn.walarm.Model.Alarm;
import com.gohn.walarm.Model.Flags;
import com.gohn.walarm.R;
import com.gohn.walarm.Scheduler.AlarmReceiver;

public class AlarmSetActivity extends Activity {

    AlarmReceiver alarmReceiver = new AlarmReceiver();
    TimePicker timePicker = null;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        flag = getIntent().getExtras().getInt(Flags.ALARMSETINTENT);

        timePicker = (TimePicker) findViewById(R.id.timePicker_add);

        // 수정 모드인 경우 알람 시간을 picker에 표시
        if ( flag == Flags.MODIFY ) {
            int hour = getIntent().getExtras().getInt(Alarm.FLAGHOUR);
            int min = getIntent().getExtras().getInt(Alarm.FLAGMINUTE);

            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(min);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set, menu);

        // 메뉴에서 저장 옆에 버튼 이름을 상황에 맞게 바꿔준다.
        MenuItem item = menu.findItem(R.id.action_cancel_delete);

        if ( flag == Flags.ADD) {
            item.setTitle("취소");
        } else if ( flag == Flags.MODIFY ) {
            item.setTitle("삭제");
        }
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

        switch (id) {
            case R.id.action_save:
                if ( flag == Flags.ADD ) {
                    int hour = timePicker.getCurrentHour();
                    int min = timePicker.getCurrentMinute();

                    Alarm a = new Alarm(this, hour, min, 1);
                    AlarmDBMgr.getInstance(this).addAlarm(a);
                    alarmReceiver.setAlarm(this, a.No, hour, min);
                } else if ( flag == Flags.MODIFY ) {
                    int no = getIntent().getExtras().getInt(Alarm.FLAGNUMBER);
                    int hour = timePicker.getCurrentHour();
                    int min = timePicker.getCurrentMinute();

                    // 기존 알람 삭제
                    AlarmDBMgr.getInstance(this).delAlarm(no);

                    // 새 알람 추가
                    Alarm a = new Alarm(this,hour,min,1);
                    AlarmDBMgr.getInstance(this).addAlarm(a);
                    alarmReceiver.setAlarm(this, a.No, hour, min);
                }
                GoHome();
                return true;
            case R.id.action_cancel_delete:
                if ( flag == Flags.ADD ) {

                } else if ( flag == Flags.MODIFY ) {
                    // 기존 알람 삭제
                    int no = getIntent().getExtras().getInt(Alarm.FLAGNUMBER);
                    AlarmDBMgr.getInstance(this).delAlarm(no);
                }
                GoHome();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        GoHome();
    }

    public void GoHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
