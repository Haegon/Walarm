package com.gohn.walarm.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.gohn.walarm.Extention.ButtonEx;
import com.gohn.walarm.Extention.EditTextEx;
import com.gohn.walarm.Extention.ToggleButtonEx;
import com.gohn.walarm.Manager.AlarmDBMgr;
import com.gohn.walarm.Model.Alarm;
import com.gohn.walarm.Model.Days;
import com.gohn.walarm.Model.Flags;
import com.gohn.walarm.R;
import com.gohn.walarm.Scheduler.AlarmReceiver;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmSetActivity extends Activity implements TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    AlarmReceiver alarmReceiver = new AlarmReceiver();
    ButtonEx btnTimePicker;
    ButtonEx btnCancel;
    ButtonEx btnDelete;
    ButtonEx btnSave;
    ArrayList<ToggleButtonEx> tbDays = new ArrayList<ToggleButtonEx>();
    EditTextEx editName;
    CheckBox cbVibe;
    CheckBox cbRing;
    Context context;

    int flag;
    int mHour;
    int mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        context = getApplicationContext();

        if (getIntent().getExtras() == null)
            flag = Flags.ADD;
        else
            flag = getIntent().getExtras().getInt(Flags.ALARMSETINTENT);

        tbDays.add((ToggleButtonEx) findViewById(R.id.tb_sunday));
        tbDays.add((ToggleButtonEx) findViewById(R.id.tb_monday));
        tbDays.add((ToggleButtonEx) findViewById(R.id.tb_tuesday));
        tbDays.add((ToggleButtonEx) findViewById(R.id.tb_wednesday));
        tbDays.add((ToggleButtonEx) findViewById(R.id.tb_thursday));
        tbDays.add((ToggleButtonEx) findViewById(R.id.tb_friday));
        tbDays.add((ToggleButtonEx) findViewById(R.id.tb_saturday));

        // 토글 버튼 색깔 리스너 달아준다.
        for (final ToggleButtonEx tb : tbDays) {
            tb.setTextColor(Color.GRAY);
            tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        tb.setTextColor(Color.WHITE);
                    } else {
                        tb.setTextColor(Color.GRAY);
                    }
                }
            });
        }

        btnCancel = (ButtonEx) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        btnDelete = (ButtonEx) findViewById(R.id.btn_del);
        btnDelete.setOnClickListener(this);
        btnSave = (ButtonEx) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
        btnTimePicker = (ButtonEx) findViewById(R.id.btn_timepicker);
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        AlarmSetActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.setThemeDark(true);
                tpd.vibrate(true);
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });
        editName = (EditTextEx) findViewById(R.id.text_name);

        cbVibe = (CheckBox)findViewById(R.id.cbVibe);
        cbRing = (CheckBox)findViewById(R.id.cbRing);

        // 수정 모드인 경우 현재 알람 정보를 뷰에 표시시
        if (flag == Flags.MODIFY) {
            String name = getIntent().getExtras().getString(Flags.ALARMNAME);
            mHour = getIntent().getExtras().getInt(Flags.ALARMHOUR);
            mMinute = getIntent().getExtras().getInt(Flags.ALARMMINUTE);
            int days = getIntent().getExtras().getInt(Flags.ALARMDAYS);
            int options = getIntent().getExtras().getInt(Flags.ALARMOPTIONS);

            editName.setText(name);
            btnTimePicker.setText(getTimeString(mHour, mMinute));

            if ((days & Days.SUNDAY) == Days.SUNDAY) tbDays.get(0).setChecked(true);
            if ((days & Days.MONDAY) == Days.MONDAY) tbDays.get(1).setChecked(true);
            if ((days & Days.TUESDAY) == Days.TUESDAY) tbDays.get(2).setChecked(true);
            if ((days & Days.WEDNESDAY) == Days.WEDNESDAY) tbDays.get(3).setChecked(true);
            if ((days & Days.THURSDAY) == Days.THURSDAY) tbDays.get(4).setChecked(true);
            if ((days & Days.FRIDAY) == Days.FRIDAY) tbDays.get(5).setChecked(true);
            if ((days & Days.SATURDAY) == Days.SATURDAY) tbDays.get(6).setChecked(true);

            if ((options & Flags.VIBRATION) == Flags.VIBRATION) cbVibe.setChecked(true);
            else cbVibe.setChecked(false);
            if ((options & Flags.RING) == Flags.RING) cbRing.setChecked(true);
            else cbRing.setChecked(false);

        } else {
            // 추가 모드인 경우 삭제 버튼을 제거.
            btnDelete.setVisibility(View.GONE);

            Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            btnTimePicker.setText(getTimeString(mHour, mMinute));

            cbVibe.setChecked(true);
            cbRing.setChecked(true);
        }
    }

    public String getTimeString(int hour, int minute) {

        String noon;
        if ( hour >= 0 && hour <12 ) noon = "AM";
        else noon = "PM";
        return String.format("%s %02d:%02d", noon, hour%12, minute);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        btnTimePicker.setText(getTimeString(mHour,mMinute));
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
        builder.setMessage(R.string.dlg_cancel).setPositiveButton(R.string.dlg_yes, dialogClickListener)
                .setNegativeButton(R.string.dlg_no, dialogClickListener).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                ExitPopup();
                break;
            case R.id.btn_del:
                DeletePopup();
                break;
            case R.id.btn_save:
                // 옵션은 스위치에서 가져온다.
                int options = 0;
                if ( cbVibe.isChecked() )
                    options += Flags.VIBRATION;
                if ( cbRing.isChecked() )
                    options += Flags.RING;

                // 알람 객체 하나 생성
                Alarm a = new Alarm(this, editName.getText().toString(), mHour, mMinute, getDays(), 1, options);

                if (flag == Flags.ADD) {
                    // 알람 추가.
                    Log.e("gohn", "Options Set : " + a.Options);

                    AlarmDBMgr.getInstance(this).addAlarm(a);
                    alarmReceiver.setAlarm(this, a);
                } else if (flag == Flags.MODIFY) {
                    int no = getIntent().getExtras().getInt(Flags.ALARMNUMBER);

                    // 기존 알람 삭제
                    AlarmDBMgr.getInstance(this).delAlarm(no);

                    // 새 알람 추가
                    AlarmDBMgr.getInstance(this).addAlarm(a);
                    alarmReceiver.setAlarm(this, a);
                }
                GoHome();
                break;
            default:
                Log.e("gohn", "Unknown Button");
                break;
        }
    }
}
