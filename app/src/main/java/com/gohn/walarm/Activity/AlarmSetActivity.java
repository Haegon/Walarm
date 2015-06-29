package com.gohn.walarm.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.gohn.walarm.Manager.AlarmDBMgr;
import com.gohn.walarm.Model.Alarm;
import com.gohn.walarm.Model.Days;
import com.gohn.walarm.Model.Flags;
import com.gohn.walarm.R;
import com.gohn.walarm.Scheduler.AlarmReceiver;

import java.util.ArrayList;

public class AlarmSetActivity extends Activity {

    AlarmReceiver alarmReceiver = new AlarmReceiver();
    TimePicker timePicker = null;
    ArrayList<ToggleButton> tbDays = new ArrayList<ToggleButton>();
    EditText editName;
    Context context;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        context = getApplicationContext();
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        flag = getIntent().getExtras().getInt(Flags.ALARMSETINTENT);

        timePicker = (TimePicker) findViewById(R.id.timePicker_add);
        tbDays.add((ToggleButton) findViewById(R.id.tb_sunday));
        tbDays.add((ToggleButton) findViewById(R.id.tb_monday));
        tbDays.add((ToggleButton) findViewById(R.id.tb_tuesday));
        tbDays.add((ToggleButton) findViewById(R.id.tb_wednesday));
        tbDays.add((ToggleButton) findViewById(R.id.tb_thursday));
        tbDays.add((ToggleButton) findViewById(R.id.tb_friday));
        tbDays.add((ToggleButton) findViewById(R.id.tb_saturday));

        editName = (EditText) findViewById(R.id.edit_name);

        // 수정 모드인 경우 현재 알람 정보를 뷰에 표시시
        if (flag == Flags.MODIFY) {
            String name = getIntent().getExtras().getString(Flags.ALARMNAME);
            int hour = getIntent().getExtras().getInt(Flags.ALARMHOUR);
            int min = getIntent().getExtras().getInt(Flags.ALARMMINUTE);
            int days = getIntent().getExtras().getInt(Flags.ALARMDAYS);

            editName.setText(name);
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(min);

            if ((days & Days.SUNDAY) == Days.SUNDAY) tbDays.get(0).setChecked(true);
            if ((days & Days.MONDAY) == Days.MONDAY) tbDays.get(1).setChecked(true);
            if ((days & Days.TUESDAY) == Days.TUESDAY) tbDays.get(2).setChecked(true);
            if ((days & Days.WEDNESDAY) == Days.WEDNESDAY) tbDays.get(3).setChecked(true);
            if ((days & Days.THURSDAY) == Days.THURSDAY) tbDays.get(4).setChecked(true);
            if ((days & Days.FRIDAY) == Days.FRIDAY) tbDays.get(5).setChecked(true);
            if ((days & Days.SATURDAY) == Days.SATURDAY) tbDays.get(6).setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set, menu);

        // 메뉴에서 저장 옆에 버튼 이름을 상황에 맞게 바꿔준다.
        MenuItem item = menu.findItem(R.id.action_cancel_delete);

        if (flag == Flags.ADD) {
            item.setTitle("취소");
        } else if (flag == Flags.MODIFY) {
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

        switch (id) {
            case android.R.id.home:
                ExitPopup();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();

        // 알람 객체 하나 생성
        Alarm a = new Alarm(this, editName.getText().toString(), hour, min, getDays(), 1);

        switch (id) {
            case R.id.action_save:
                if (flag == Flags.ADD) {
                    // 알람 추가.
                    AlarmDBMgr.getInstance(this).addAlarm(a);
                    alarmReceiver.setAlarm(this, a.No, hour, min, a.Days);
                } else if (flag == Flags.MODIFY) {
                    int no = getIntent().getExtras().getInt(Flags.ALARMNUMBER);

                    // 기존 알람 삭제
                    AlarmDBMgr.getInstance(this).delAlarm(no);

                    // 새 알람 추가
                    AlarmDBMgr.getInstance(this).addAlarm(a);
                    alarmReceiver.setAlarm(this, a.No, hour, min, a.Days);
                }
                GoHome();
                return true;
            case R.id.action_cancel_delete:
                if (flag == Flags.ADD) {
                    ExitPopup();
                } else if (flag == Flags.MODIFY) {
                    // 기존 알람 삭제
                    DeletePopup();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // 저장 하지 않고 나가기
        ExitPopup();
    }

    public void GoHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public int getDays() {

        int size = tbDays.size();
        int days = 0;

        if (size > 0) {
            for (int i = 0; i < size; i++) {
                if (tbDays.get(i).isChecked())
                    days += Days.DAYLIST[i];
            }
        }
        return days;
    }

    private void DeletePopup() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        int no = getIntent().getExtras().getInt(Flags.ALARMNUMBER);
                        AlarmDBMgr.getInstance(context).delAlarm(no);
                        GoHome();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(AlarmSetActivity.this);
        builder.setMessage("삭제 하시겠습니까?").setPositiveButton("예", dialogClickListener)
                .setNegativeButton("아니오", dialogClickListener).show();
    }

    private void ExitPopup() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        GoHome();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(AlarmSetActivity.this);
        builder.setMessage("저장하지 않고 나가시겠습니까?").setPositiveButton("예", dialogClickListener)
                .setNegativeButton("아니오", dialogClickListener).show();
    }
}
