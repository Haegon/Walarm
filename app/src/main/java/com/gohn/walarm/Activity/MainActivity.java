package com.gohn.walarm.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

import com.gohn.walarm.Fragment.AlarmSetFragment;
import com.gohn.walarm.Fragment.RingSetFragment;
import com.gohn.walarm.Manager.AlarmDBMgr;
import com.gohn.walarm.Manager.LocateMgr;
import com.gohn.walarm.Util.BackPressCloseHandler;
import com.gohn.walarm.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends FragmentActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    BackPressCloseHandler backPressCloseHandler;
    LocateMgr gps;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // gps 매니저 초기화
        gps = LocateMgr.getInstance(this);

        final double latitude = gps.getInstance(this).getLatitude();
        final double longitude = gps.getInstance(this).getLongitude();


        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    HttpClient client = new DefaultHttpClient();
                    String getURL = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f", latitude, longitude);
                    Log.i("REQUEST", getURL);
                    HttpGet get = new HttpGet(getURL);
                    HttpResponse responseGet = client.execute(get);
                    HttpEntity resEntityGet = responseGet.getEntity();
                    if (resEntityGet != null) {
                        // 결과를 처리합니다.
                        String res = EntityUtils.toString(resEntityGet);

                        JSONObject jObject = new JSONObject(res);
                        JSONArray weather = jObject.getJSONArray("weather");
                        JSONObject item = (JSONObject) weather.get(0);
                        int wcode = item.getInt("id");

                        Log.e("ID", "wcode : " + wcode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        // GPS 기능이 꺼져 있으면 키도록 다이얼로그 띄움
        if (!gps.canGetLocation()) {
            gps.getInstance(this).showSettingsAlert();
        }

        AlarmDBMgr dbMgr = AlarmDBMgr.getInstance(this);
        backPressCloseHandler = new BackPressCloseHandler(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getApplicationContext(), getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Context mContext;

        public SectionsPagerAdapter(Context mContext, FragmentManager fm) {
            super(fm);
            this.mContext = mContext;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            switch (position) {
                case 0:
                    return new AlarmSetFragment(mContext);
                case 1:
                    return new RingSetFragment(mContext);
            }
            return null;
        }

        @Override
        public int getCount() {
            // total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

}
