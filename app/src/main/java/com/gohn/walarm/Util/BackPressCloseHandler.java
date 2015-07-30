package com.gohn.walarm.Util;

import android.app.Activity;
import android.widget.Toast;

import com.gohn.walarm.R;

/**
 * Created by HaegonKoh on 2015. 6. 29..
 */
public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;


    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    private void showGuide() {
        toast = Toast.makeText(activity, R.string.toast_back, Toast.LENGTH_SHORT);
        toast.show();
    }
}
