package com.mingrisoft.alarmclock.scroll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mingrisoft.alarmclock.R;

public class ScrollBaseAdapter extends BaseAdapter {

    private int num;
    private Context context;
    private LayoutInflater mLayoutInflater;

    public ScrollBaseAdapter(Context context) {
        this.context = context;
    }
    public void addData(int n){
       this.num = n;
        notifyDataSetChanged();
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return num;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view==null) {
            view = mLayoutInflater.inflate(R.layout.scroll_item, null);
        }
        return view;
    }
}

