package com.gohn.walarm.Extention;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewEx extends TextView {

    public TextViewEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewEx(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "man.mp3");
        setTypeface(tf);

        setTextColor(Color.WHITE);
    }
}
