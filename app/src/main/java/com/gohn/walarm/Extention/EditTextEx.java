package com.gohn.walarm.Extention;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by HaegonKoh on 2015. 7. 22..
 */
public class EditTextEx extends EditText {

    public EditTextEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditTextEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextEx(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "man.mp3");
        setTypeface(tf);
    }
}
