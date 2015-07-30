package com.gohn.walarm.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gohn.walarm.R;

@SuppressLint("ValidFragment")
public class HelpFragment extends Fragment implements View.OnClickListener {
    Context mContext;
    int mResource;


    public HelpFragment(Context context, int resource) {
        mContext = context;
        mResource = resource;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_help, null);
        view.setBackgroundColor(Color.WHITE);

        ImageView iv = (ImageView)view.findViewById(R.id.imageHelp);
        iv.setImageResource(mResource);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        }
    }
}