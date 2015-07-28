package com.gohn.walarm.Extention;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ToggleButton;

public class ToggleButtonEx extends ToggleButton{

    public ToggleButtonEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ToggleButtonEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ToggleButtonEx(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "ca.mp3");
        setTypeface(tf);

        setTextColor(Color.WHITE);
    }
}
