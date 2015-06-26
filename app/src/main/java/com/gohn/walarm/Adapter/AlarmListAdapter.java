package com.gohn.walarm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.gohn.walarm.Extention.TextViewEx;
import com.gohn.walarm.Model.Alarm;
import com.gohn.walarm.R;

import java.util.ArrayList;

/**
 * Created by Gohn on 2015. 6. 20..
 */
public class AlarmListAdapter extends BaseAdapter {

    Context mContext = null;
    public ArrayList<Alarm> mData = null;
    LayoutInflater mLayout = null;

    class ViewHolder {
        TextViewEx mAfternoon;
        TextViewEx mTime;
        CheckBox mCheckBox;
    }

    public AlarmListAdapter(Context context, ArrayList<Alarm> data) {

        mContext = context;
        mData = data;
        mLayout = LayoutInflater.from(mContext);
    }

    public void add(int index, Alarm addData) {
        mData.add(index, addData);
        notifyDataSetChanged();
    }

    public void delete(int index) {
        mData.remove(index);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View itemLayout = convertView;
        final ViewHolder viewHolder;// = null;

        if (itemLayout == null) {
            itemLayout = mLayout.inflate(R.layout.alarm_list, null);

            viewHolder = new ViewHolder();

            viewHolder.mAfternoon = (TextViewEx) itemLayout.findViewById(R.id.alarm_afternoon);
            viewHolder.mTime = (TextViewEx) itemLayout.findViewById(R.id.alarm_time);
            viewHolder.mCheckBox = (CheckBox) itemLayout.findViewById(R.id.cbAlarm);

            itemLayout.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemLayout.getTag();
        }


        int hour = mData.get(position).Hour;
        int min = mData.get(position).Minute;

        // 시간 그리기
        String nowTime = String.format("%02d:%02d",hour%12,min);

        // 오전오후 표시하기
        if ( mData.get(position).Hour/12 == 1 ) {
            viewHolder.mAfternoon.setText("오후");
            // 새벽 12시는 0시로 표현하고,
            // 낮 12시는 12시로 표현하기 위한 코드.
            if ( hour%12 == 0 )
                nowTime = String.format("%02d:%02d",hour,min);
        } else {
            viewHolder.mAfternoon.setText("오전");
        }


        viewHolder.mTime.setText(nowTime);

        // 체크 박스 표시
        if ( mData.get(position).IsOn == 1 ) {
            viewHolder.mCheckBox.setChecked(true);
        } else {
            viewHolder.mCheckBox.setChecked(false);
        }

        // 체크 박스 변경 될 때마다 리스너에서 db에 업데이트
        viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {

            //please note that objPlace, position and holder must be declared
            //as final inside the getView() function scope.
            @Override
            public void onClick(View arg0) {
                final boolean isChecked = viewHolder.mCheckBox.isChecked();
                if (isChecked) {
                    mData.get(position).On();
                } else {
                    mData.get(position).Off();
                }
            }
        });

        return itemLayout;
    }
}
