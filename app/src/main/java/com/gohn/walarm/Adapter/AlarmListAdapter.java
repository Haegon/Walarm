package com.gohn.walarm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
    String word;

    class ViewHolder {
        TextViewEx mNameTv;
        TextViewEx mNumbersTv;
    }

    public AlarmListAdapter(Context context, ArrayList<Alarm> data, String w) {

        mContext = context;
        mData = data;
        word = w;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View itemLayout = convertView;
        ViewHolder viewHolder = null;

        if (itemLayout == null) {
            itemLayout = mLayout.inflate(R.layout.alarm_list, null);

            viewHolder = new ViewHolder();

            viewHolder.mNameTv = (TextViewEx) itemLayout.findViewById(R.id.alarm_name_text);
            viewHolder.mNumbersTv = (TextViewEx) itemLayout.findViewById(R.id.alarm_numbers_text);

            itemLayout.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemLayout.getTag();
        }

        viewHolder.mNameTv.setText(mData.get(position).Name);
        viewHolder.mNumbersTv.setText(mData.get(position).Number + " " + word);
        return itemLayout;
    }
}
