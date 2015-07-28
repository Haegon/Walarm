package com.gohn.walarm.Extention;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Gohn on 2015. 6. 21..
 */
public class ListViewEx extends ListView {

    private int bg = 0;

    public ListViewEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ListViewEx(Context context) {
        super(context);
    }

    public ListViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setOnClickListener(final OnClickListener l) {

        super.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                l.onClick(v);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int maskedAction = event.getActionMasked();
        if (maskedAction == MotionEvent.ACTION_DOWN) {
            Log.e("gohn", "@@@@@@@@@@@@@@@@@@@@@@ listviewex ");
            getBackground().setColorFilter(Color.argb(150, 155, 155, 155), PorterDuff.Mode.SRC_ATOP);
            bg++;

        } else if (maskedAction == MotionEvent.ACTION_UP) {
            getBackground().setColorFilter(null);
        }
        return super.onTouchEvent(event);
    }
}
