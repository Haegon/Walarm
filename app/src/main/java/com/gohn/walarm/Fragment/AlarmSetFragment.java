package com.gohn.walarm.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.gohn.walarm.Activity.AlarmSetActivity;
import com.gohn.walarm.Adapter.AlarmListAdapter;
import com.gohn.walarm.Manager.AlarmDBMgr;
import com.gohn.walarm.Model.Flags;
import com.gohn.walarm.R;
import com.melnykov.fab.FloatingActionButton;

@SuppressLint("ValidFragment")
public class AlarmSetFragment extends ListFragment implements View.OnClickListener, AbsListView.OnScrollListener {
    Context mContext;

    AlarmDBMgr dbMgr = null;
    AlarmListAdapter mAdapter = null;
    FloatingActionsMenu fam;
    FloatingActionButton fab_menu;

    boolean isToggle = false;

    public AlarmSetFragment(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarmset, null);
        view.setBackgroundColor(Color.WHITE);

        dbMgr = AlarmDBMgr.getInstance(mContext);

        return view;
    }

    // onCreate에서 getListView를 가져오려면 뷰가 생성되지도 않았는데 가져오려는 에러가 남
    // onCreate -> onViewCreated 순서이므로 어댑터 설정을 뷰 생성 이후로 하였다.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        fam.setVisibility(View.INVISIBLE);

        com.getbase.floatingactionbutton.FloatingActionButton fab_add = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.fab_add);
        fab_add.setOnClickListener(this);

        com.getbase.floatingactionbutton.FloatingActionButton fab_option = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.fab_option);
        fab_option.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab_menu:
                toggle();
                break;
            case R.id.fab_add:
                Intent intent = new Intent(mContext, AlarmSetActivity.class);
                intent.putExtra(Flags.ALARMSETINTENT, Flags.ADD);
                startActivityForResult(intent, 0);
                break;
            case R.id.fab_option:
                Log.e("gohn", "fab_option");
            case R.id.fab_help:
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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.e("gohn", "onScrollStateChanged");
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
        Log.e("gohn", "onScroll");
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