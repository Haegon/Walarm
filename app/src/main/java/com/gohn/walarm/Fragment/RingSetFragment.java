package com.gohn.walarm.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gohn.walarm.Activity.AlarmSetActivity;
import com.gohn.walarm.Extention.TextViewEx;
import com.gohn.walarm.Model.Flags;
import com.gohn.walarm.R;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class RingSetFragment extends Fragment {
	Context mContext;
	ArrayList<Button> buttons = new ArrayList<Button>();
	ArrayList<TextViewEx> titles = new ArrayList<TextViewEx>();
	ArrayList<TextViewEx> rings = new ArrayList<TextViewEx>();


	public RingSetFragment(Context context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_ringset, null);

		buttons.add((Button)view.findViewById(R.id.btn_weather_1));
		buttons.add((Button)view.findViewById(R.id.btn_weather_2));
		buttons.add((Button)view.findViewById(R.id.btn_weather_3));
		buttons.add((Button)view.findViewById(R.id.btn_weather_4));
		buttons.add((Button)view.findViewById(R.id.btn_weather_5));

		titles.add((TextViewEx)view.findViewById(R.id.text_weather_1));
		titles.add((TextViewEx)view.findViewById(R.id.text_weather_2));
		titles.add((TextViewEx)view.findViewById(R.id.text_weather_3));
		titles.add((TextViewEx)view.findViewById(R.id.text_weather_4));
		titles.add((TextViewEx)view.findViewById(R.id.text_weather_5));

		rings.add((TextViewEx)view.findViewById(R.id.text_ring_1));
		rings.add((TextViewEx)view.findViewById(R.id.text_ring_2));
		rings.add((TextViewEx)view.findViewById(R.id.text_ring_3));
		rings.add((TextViewEx)view.findViewById(R.id.text_ring_4));
		rings.add((TextViewEx)view.findViewById(R.id.text_ring_5));

		return view;
	}

	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.btn_add_alarm:
				Intent intent = new Intent(mContext, AlarmSetActivity.class);
				intent.putExtra(Flags.ALARMSETINTENT, Flags.ADD);

				startActivityForResult(intent, 0);
				break;
		}
	}
}