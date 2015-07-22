package com.gohn.walarm.Model;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

/**
 * Created by HaegonKoh on 2015. 7. 17..
 */
public class Colors {

    static int[] arrayRing = {
            Color.parseColor("#ff644581"),
            Color.parseColor("#ffd54e9b"),
            Color.parseColor("#ffed8699"),
            Color.parseColor("#ff53c2bb"),
            Color.parseColor("#ff96d5c4")};

    static int[] arrayWeather = {
Color.parseColor("#ff2c696c"),
        Color.parseColor("#ffeabe5f"),
        Color.parseColor("#ffbf4b18"),
        Color.parseColor("#ff582c2b"),
        Color.parseColor("#ff20151b")};

public static int Background = Color.parseColor("#ffffaa43");

public static ColorDrawable getAlarmColor(int index) {
        return new ColorDrawable(arrayRing[index%5]);
        }

public static ColorDrawable getRingColor(int index) {
        return new ColorDrawable(arrayWeather[index%5]);
        }
}
