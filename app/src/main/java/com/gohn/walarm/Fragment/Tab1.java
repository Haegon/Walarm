package com.gohn.walarm.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.gohn.walarm.Activity.AddActivity;
import com.gohn.walarm.Adapter.AlarmListAdapter;
import com.gohn.walarm.Manager.AlarmDBMgr;
import com.gohn.walarm.R;

@SuppressLint("ValidFragment")
public class Tab1 extends ListFragment implements View.OnClickListener {
    Context mContext;

    AlarmDBMgr dbMgr = null;
    AlarmListAdapter mAdapter = null;

    public Tab1(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tabs1, null);

        dbMgr = AlarmDBMgr.getInstance(mContext);

        // 추가 버튼을 버튼 클래스를 확장한 클래스를 사용하다보니 사용하는 뷰에서 onClick 리스너를 호출하지 못하는 상황이 되었는데
        // 리스너를 현재 뷰로 지정해주면 이 뷰에서 클릭을 받아올 수 있다.
        Button btnAdd = (Button) view.findViewById(R.id.btn_add_alarm);
        btnAdd.setOnClickListener(this);

        return view;
    }

    // onCreate에서 getListView를 가져오려면 뷰가 생성되지도 않았는데 가져오려는 에러가 남
    // onCreate -> onViewCreated 순서이므로 어댑터 설정을 뷰 생성 이후로 하였다.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new AlarmListAdapter(mContext, dbMgr.getAlarms());
        setListAdapter(mAdapter);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("gohn", "Long Click => " + mAdapter.mData.get(position).Hour + ":" + mAdapter.mData.get(position).Minute);
                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_add_alarm:
                startActivityForResult(new Intent(mContext, AddActivity.class), 0);
                break;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Log.e("gohn", "Just Click => " + mAdapter.mData.get(position).Hour + ":" + mAdapter.mData.get(position).Minute);
    }
}