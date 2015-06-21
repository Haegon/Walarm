package com.gohn.walarm.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gohn.walarm.Adapter.AlarmListAdapter;
import com.gohn.walarm.Model.Alarm;
import com.gohn.walarm.R;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class Tab1 extends ListFragment {
	Context mContext;

	AlarmListAdapter mAdapter = null;

	public Tab1(Context context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_tabs1, null);

		ArrayList<Alarm> items = new ArrayList<Alarm>();
		for (int index = 0; index < 30; index++) {
			Alarm a = new Alarm();
			a.Name = "알람 : " + index;
			a.Number = index;
			items.add(a);
		}

		mAdapter = new AlarmListAdapter(mContext, items, "번");

		setListAdapter(mAdapter);

		return view;
	}
}