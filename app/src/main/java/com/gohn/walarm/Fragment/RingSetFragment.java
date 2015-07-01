package com.gohn.walarm.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gohn.walarm.Activity.AlarmSetActivity;
import com.gohn.walarm.Extention.ButtonEx;
import com.gohn.walarm.Extention.TextViewEx;
import com.gohn.walarm.Model.Flags;
import com.gohn.walarm.R;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class RingSetFragment extends Fragment implements View.OnClickListener {
	Context mContext;
	ArrayList<ButtonEx> buttons = new ArrayList<ButtonEx>();
	ArrayList<TextViewEx> titles = new ArrayList<TextViewEx>();
	ArrayList<TextViewEx> rings = new ArrayList<TextViewEx>();


	public RingSetFragment(Context context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_ringset, null);

		buttons.add((ButtonEx)view.findViewById(R.id.btn_weather_1));
		buttons.add((ButtonEx)view.findViewById(R.id.btn_weather_2));
		buttons.add((ButtonEx)view.findViewById(R.id.btn_weather_3));
		buttons.add((ButtonEx)view.findViewById(R.id.btn_weather_4));
		buttons.add((ButtonEx)view.findViewById(R.id.btn_weather_5));

		titles.add((TextViewEx)view.findViewById(R.id.text_weather_1));
		titles.add((TextViewEx)view.findViewById(R.id.text_weather_2));
		titles.add((TextViewEx)view.findViewById(R.id.text_weather_3));
		titles.add((TextViewEx)view.findViewById(R.id.text_weather_4));
		titles.add((TextViewEx)view.findViewById(R.id.text_weather_5));

		rings.add((TextViewEx) view.findViewById(R.id.text_ring_1));
		rings.add((TextViewEx)view.findViewById(R.id.text_ring_2));
		rings.add((TextViewEx)view.findViewById(R.id.text_ring_3));
		rings.add((TextViewEx) view.findViewById(R.id.text_ring_4));
		rings.add((TextViewEx) view.findViewById(R.id.text_ring_5));

		for ( int i = 0 ; i < buttons.size() ; i ++ ) {
			buttons.get(i).setOnClickListener(this);
		}

		return view;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.btn_weather_1:
				Intent intent = new Intent(mContext, AlarmSetActivity.class);
				intent.putExtra(Flags.ALARMSETINTENT, Flags.ADD);

				startActivityForResult(intent, 0);
				break;
		}
	}
}