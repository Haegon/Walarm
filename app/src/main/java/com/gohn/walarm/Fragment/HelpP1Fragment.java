package com.gohn.walarm.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import com.gohn.walarm.Model.Colors;
import com.gohn.walarm.R;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class HelpP1Fragment extends Fragment implements View.OnClickListener {
    Context mContext;


    public HelpP1Fragment(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_help1, null);

        view.setBackgroundColor(Color.WHITE);
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        }
    }
}