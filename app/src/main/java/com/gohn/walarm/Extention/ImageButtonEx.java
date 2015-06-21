package com.gohn.walarm.Extention;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

/**
 * Created by Gohn on 2015. 6. 21..
 */
public class ImageButtonEx extends ImageButton {

    public ImageButtonEx(Context context) {
        super(context);
    }

    public ImageButtonEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int maskedAction = event.getActionMasked();
        if (maskedAction == MotionEvent.ACTION_DOWN)
            setColorFilter(Color.argb(150, 155, 155, 155), PorterDuff.Mode.DST_IN);
        else if (maskedAction == MotionEvent.ACTION_UP)
            setColorFilter(null);
        return super.onTouchEvent(event);
    }
}