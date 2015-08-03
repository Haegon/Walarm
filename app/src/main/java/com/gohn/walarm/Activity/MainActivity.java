package com.gohn.walarm.Activity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.gohn.walarm.Adapter.AlarmListAdapter;
import com.gohn.walarm.Extention.TextViewEx;
import com.gohn.walarm.Manager.AlarmDBMgr;
import com.gohn.walarm.Manager.LocateMgr;
import com.gohn.walarm.Model.Flags;
import com.gohn.walarm.R;
import com.gohn.walarm.Util.BackPressCloseHandler;
import com.gohn.walarm.Util.licensesdialog.LicensesDialog;
import com.melnykov.fab.FloatingActionButton;

public class MainActivity extends ListActivity implements View.OnClickListener, AbsListView.OnScrollListener{

    Context mContext;
    BackPressCloseHandler backPressCloseHandler;
    LocateMgr gps;
    AlarmListAdapter mAdapter = null;
    FloatingActionsMenu fam;
    FloatingActionButton fab_menu;
    AlarmDBMgr dbMgr;
    boolean isToggle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        // gps 매니저 초기화
        gps = LocateMgr.getInstance(this);

        // GPS 기능이 꺼져 있으면 키도록 다이얼로그 띄움
        if (!gps.canGetLocation()) {
            LocateMgr.getInstance(this).showSettingsAlert();
        }

        dbMgr = AlarmDBMgr.getInstance(this);
        backPressCloseHandler = new BackPressCloseHandler(this);

        View view = getWindow().getDecorView().findViewById(android.R.id.content);
        view.setBackgroundColor(Color.WHITE);

        mAdapter = new AlarmListAdapter(mContext, dbMgr.getAlarms());
        setListAdapter(mAdapter);

        ListView listView = (ListView) view.findViewById(android.R.id.list);
        getListView().setOnScrollListener(this);
        // 리스트들의 구분선 제거 - xml에서 해도 되는데 여기서 해보고 싶었음.
        listView.setDivider(null);
        //listView.setDividerHeight(3);

        // 추가 버튼을 버튼 클래스를 확장한 클래스를 사용하다보니 사용하는 뷰에서 onClick 리스너를 호출하지 못하는 상황이 되었는데
        // 리스너를 현재 뷰로 지정해주면 이 뷰에서 클릭을 받아올 수 있다.
        fab_menu = (FloatingActionButton) view.findViewById(R.id.fab_menu);
        fab_menu.setOnClickListener(this);
        // 리스트 뷰에 매달아서 리스너를 자동 등록해주는 경우에만 사용.
        // 참 거지같당....
        //fab_add.attachToListView(listView);

        fam = (FloatingActionsMenu)view.findViewById(R.id.fam_menu);
        fam.setOnClickListener(this);

        // 버튼 이벤트를 가져온다.
        com.getbase.floatingactionbutton.FloatingActionButton fab_add = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.fab_add);
        fab_add.setOnClickListener(this);
//        com.getbase.floatingactionbutton.FloatingActionButton fab_option = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.fab_option);
//        fab_option.setOnClickListener(this);
        com.getbase.floatingactionbutton.FloatingActionButton fab_help = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.fab_help);
        fab_help.setOnClickListener(this);
        com.getbase.floatingactionbutton.FloatingActionButton fab_info = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.fab_info);
        fab_info.setOnClickListener(this);
        com.getbase.floatingactionbutton.FloatingActionButton fab_ring = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.fab_ring);
        fab_ring.setOnClickListener(this);


        // 알람이 없는 경우만 메뉴 라벨과 안내 문구를 보여준다.
        TextViewEx textEmpty = (TextViewEx)findViewById(R.id.text_empty);
        TextView textMenu = (TextView)findViewById(R.id.text_menu);
            if ( mAdapter.getCount() > 0 ) {
                textEmpty.setVisibility(View.GONE);
                textMenu.setVisibility(View.GONE);

                fam.setMinimumHeight(0);
                ViewGroup.LayoutParams lp = fam.getLayoutParams();
                lp.height = 0;
        } else {
            textEmpty.setTextColor(Color.GRAY);
        }

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


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab_menu:
                toggle();
                break;
            case R.id.fab_add:
                Intent intentAdd = new Intent(mContext, AlarmSetActivity.class);
                intentAdd.putExtra(Flags.ALARMSETINTENT, Flags.ADD);
                startActivityForResult(intentAdd, 0);
                break;
            case R.id.fab_ring:
                Intent intentRing = new Intent(mContext, RingSetActivity.class);
                startActivityForResult(intentRing, 0);
                break;
//            case R.id.fab_option:
//                Log.e("gohn", "fab_option");
//                break;
            case R.id.fab_info:
                Log.e("gohn", "fab_option");

                new LicensesDialog.Builder(MainActivity.this)
                        .setNotices(R.raw.notices)
                        .build()
                        .show();

                break;
            case R.id.fab_help:
                Intent intentHelp = new Intent(mContext, HelpActivity.class);
                startActivityForResult(intentHelp, 0);
                Log.e("gohn","fab_help");
                break;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String name = mAdapter.mData.get(position).Name;
        int no = mAdapter.mData.get(position).No;
        int hour = mAdapter.mData.get(position).Hour;
        int min = mAdapter.mData.get(position).Minute;
        int days = mAdapter.mData.get(position).Days;
        int options = mAdapter.mData.get(position).Options;

        Log.e("gohn", "Just Click => " + hour + ":" + min);

        Intent intent = new Intent(mContext, AlarmSetActivity.class);
        intent.putExtra(Flags.ALARMSETINTENT, Flags.MODIFY);
        intent.putExtra(Flags.ALARMNAME, name);
        intent.putExtra(Flags.ALARMNUMBER, no);
        intent.putExtra(Flags.ALARMHOUR, hour);
        intent.putExtra(Flags.ALARMMINUTE, min);
        intent.putExtra(Flags.ALARMDAYS, days);
        intent.putExtra(Flags.ALARMOPTIONS, options);

        startActivityForResult(intent, 0);
    }

    // 스크롤 할 때 fab가 자동으로 보였다 사라지는 기능이 있긴한데
    // toggle 기능때문에 자동을 막고 스크롤 onScroll에서 보이고 숨기고를 담당하도록 하였다.
    // fab의 온스크롤에다가 현재 리스트뷰를 등록하면 전에 등록했던 리스너는 씹히는 거지같은 경우 때문에..
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if ( scrollState == SCROLL_STATE_IDLE) {
            fab_menu.show();
        } else {
            if ( isToggle )
                toggle();
            fab_menu.hide();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    void toggle() {
        isToggle = !isToggle;
        fam.toggle();
        if ( isToggle )
            fam.setVisibility(View.VISIBLE);
        else
            fam.setVisibility(View.INVISIBLE);
    }
}
