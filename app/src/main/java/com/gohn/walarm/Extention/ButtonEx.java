package com.gohn.walarm.Extention;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by Gohn on 2015. 6. 21..
 */
public class ButtonEx extends Button {

    public ButtonEx(Context context) {
        super(context);
    }

    public ButtonEx(Context context, AttributeSet attributes) {
        super(context, attributes);
    };

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
        if (maskedAction == MotionEvent.ACTION_DOWN)
            getBackground().setColorFilter(Color.argb(150, 155, 155, 155), PorterDuff.Mode.DST_IN);
        else if (maskedAction == MotionEvent.ACTION_UP) {
            getBackground().setColorFilter(null);
        }
        return super.onTouchEvent(event);
    }
}
