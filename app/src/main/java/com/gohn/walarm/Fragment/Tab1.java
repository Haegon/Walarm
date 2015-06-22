package com.gohn.walarm.Fragment;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gohn.walarm.Activity.AddActivity;
import com.gohn.walarm.Adapter.AlarmListAdapter;
import com.gohn.walarm.Manager.AlarmDBMgr;
import com.gohn.walarm.Model.Alarm;
import com.gohn.walarm.R;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class Tab1 extends ListFragment implements View.OnClickListener {
	Context mContext;

	AlarmDBMgr dbMgr = null;
	AlarmListAdapter mAdapter = null;

	public Tab1(Context context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_tabs1, null);

		dbMgr = AlarmDBMgr.getInstance(mContext);

		// 추가 버튼을 버튼 클래스를 확장한 클래스를 사용하다보니 사용하는 뷰에서 onClick 리스너를 호출하지 못하는 상황이 되었는데
		// 리스너를 현재 뷰로 지정해주면 이 뷰에서 클릭을 받아올 수 있다.
		Button btnAdd = (Button)view.findViewById(R.id.btn_add_alarm);
		btnAdd.setOnClickListener(this);


//		ArrayList<Alarm> items = new ArrayList<Alarm>();
//		for (int index = 0; index < 30; index++) {
//
//			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//			Date date = new Date();
//
//			Alarm a = new Alarm();
//			a.AlarmTime = dateFormat.format(date).toString();
//			a.No = index;
//			items.add(a);
//		}

		mAdapter = new AlarmListAdapter(mContext, dbMgr.getAlarms(), "번");

		setListAdapter(mAdapter);

		return view;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.btn_add_alarm:
				startActivityForResult(new Intent(mContext, AddActivity.class), 0);
				break;
		}
	}
}