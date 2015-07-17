package com.gohn.walarm.Model;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import java.util.Random;

/**
 * Created by HaegonKoh on 2015. 7. 17..
 */
public class Colors {

    static int[] array = {
            Color.parseColor("#ff644581"),
            Color.parseColor("#ffd54e9b"),
            Color.parseColor("#ffed8699"),
            Color.parseColor("#ff53c2bb"),
            Color.parseColor("#ff96d5c4")};

    public static ColorDrawable getRandomColor(int index) {
        return new ColorDrawable(array[index%5]);
    }
}
