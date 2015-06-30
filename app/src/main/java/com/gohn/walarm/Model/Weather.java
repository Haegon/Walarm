package com.gohn.walarm.Model;

/**
 * Created by HaegonKoh on 2015. 6. 30..
 */
public class Weather {

    public static int CLEAR = 0;
    public static int RAIN = 1;
    public static int SNOW = 2;
    public static int GRAY = 3;
    public static int EXTREAM = 4;

    // http://openweathermap.org/weather-conditions
    // 여기에서 코드 가져와서 날씨를 정해줌.
    public static int getWeather(int code) {
        if ( code < 600 )
            return RAIN;
        if ( code < 700 )
            return SNOW;
        if ( code < 800 )
            return GRAY;
        if ( code < 900 )
            return CLEAR;
        if ( code < 950 )
            return EXTREAM;
        if ( code < 958 )
            return CLEAR;
        return EXTREAM;
    }
}
