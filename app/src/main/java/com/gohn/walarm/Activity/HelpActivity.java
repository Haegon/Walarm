package com.gohn.walarm.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.Button;

import com.gohn.walarm.Fragment.HelpFragment;
import com.gohn.walarm.R;

public class HelpActivity extends FragmentActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    Button bt1;
    Button bt2;
    Button bt3;
    Button bt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        bt1=(Button) findViewById(R.id.btn1);
        bt2=(Button) findViewById(R.id.btn2);
        bt3=(Button) findViewById(R.id.btn3);
        bt4=(Button) findViewById(R.id.btn4);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getApplicationContext(), getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager_help);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                btnAction(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            switch (position) {
                case 0:
                    return new HelpFragment(mContext,R.drawable.help_alarm);
                case 1:
                    return new HelpFragment(mContext,R.drawable.help_ring);
                case 2:
                    return new HelpFragment(mContext,R.drawable.help_set);
                case 3:
                    return new HelpFragment(mContext,R.drawable.help_fire);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    private void btnAction(int action){

        switch(action)
        {
            case 0:
                setButton(bt1,4,15,R.drawable.frag_circle2);
                setButton(bt2,4,15,R.drawable.frag_circle1);
                setButton(bt3,4,15,R.drawable.frag_circle1);
                setButton(bt4,4,15,R.drawable.frag_circle1);
                break;
            case 1:
                setButton(bt1,4,15,R.drawable.frag_circle1);
                setButton(bt2,4,15,R.drawable.frag_circle2);
                setButton(bt3,4,15,R.drawable.frag_circle1);
                setButton(bt4,4,15,R.drawable.frag_circle1);
                break;
            case 2:
                setButton(bt1,4,15,R.drawable.frag_circle1);
                setButton(bt2,4,15,R.drawable.frag_circle1);
                setButton(bt3,4,15,R.drawable.frag_circle2);
                setButton(bt4,4,15,R.drawable.frag_circle1);
                break;
            case 3:
                setButton(bt1, 4, 15, R.drawable.frag_circle1);
                setButton(bt2,4,15,R.drawable.frag_circle1);
                setButton(bt3,4,15,R.drawable.frag_circle1);
                setButton(bt4,4,15,R.drawable.frag_circle2);
                break;
        }
    }

    private void setButton(Button btn,int w,int h,int c)
    {
        btn.setWidth(w);
        btn.setHeight(h);
        btn.setBackgroundResource(c);
    }
}
