package com.gohn.walarm.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gohn.walarm.Extention.ButtonEx;
import com.gohn.walarm.Extention.TextViewEx;
import com.gohn.walarm.Manager.AlarmDBMgr;
import com.gohn.walarm.R;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class RingSetFragment extends Fragment implements View.OnClickListener {
    Context mContext;
    AlarmDBMgr dbMgr;
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
        dbMgr = AlarmDBMgr.getInstance(mContext);

        buttons.add((ButtonEx) view.findViewById(R.id.btn_weather_1));
        buttons.add((ButtonEx) view.findViewById(R.id.btn_weather_2));
        buttons.add((ButtonEx) view.findViewById(R.id.btn_weather_3));
        buttons.add((ButtonEx) view.findViewById(R.id.btn_weather_4));
        buttons.add((ButtonEx) view.findViewById(R.id.btn_weather_5));

        titles.add((TextViewEx) view.findViewById(R.id.text_weather_1));
        titles.add((TextViewEx) view.findViewById(R.id.text_weather_2));
        titles.add((TextViewEx) view.findViewById(R.id.text_weather_3));
        titles.add((TextViewEx) view.findViewById(R.id.text_weather_4));
        titles.add((TextViewEx) view.findViewById(R.id.text_weather_5));

        rings.add((TextViewEx) view.findViewById(R.id.text_ring_1));
        rings.add((TextViewEx) view.findViewById(R.id.text_ring_2));
        rings.add((TextViewEx) view.findViewById(R.id.text_ring_3));
        rings.add((TextViewEx) view.findViewById(R.id.text_ring_4));
        rings.add((TextViewEx) view.findViewById(R.id.text_ring_5));

        // 이 뷰에서 버튼 핸들링을 하겠다는 코드.
        for (int i = 0; i < buttons.size(); i++)
            buttons.get(i).setOnClickListener(this);

        updateRingName();

        return view;
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

        Intent intent;

        switch (v.getId()) {
            case R.id.btn_weather_1:
                intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                startActivityForResult(intent, 0);
                break;
            case R.id.btn_weather_2:
                intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_weather_3:
                intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                startActivityForResult(intent, 2);
                break;
            case R.id.btn_weather_4:
                intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                startActivityForResult(intent, 3);
                break;
            case R.id.btn_weather_5:
                intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                startActivityForResult(intent, 4);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri uri = (Uri)data.getExtras().get(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        Ringtone ringtone = RingtoneManager.getRingtone(mContext, uri);
        String title = ringtone.getTitle(mContext);

        dbMgr.updateRing(requestCode, uri.toString());

//        rings.get(requestCode).setText(title);
        updateRingName();

        Log.e("gohn", "req code : " + requestCode);
        Log.e("gohn", "data : " + uri);


//        //Iterator<String> keyset= data.getExtras().keySet().iterator();
//        //while( keyset.hasNext())
//        //    Log.v("key" , keyset.next());
//
//        //Data i get as NULL, it should be valid intent
//        //super.onActivityResult(requestCode, resultCode, data);
////
//        switch (requestCode) {
//
//            case 100:
//
//
//        }
    }
}