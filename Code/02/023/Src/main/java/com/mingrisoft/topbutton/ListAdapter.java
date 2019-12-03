package com.mingrisoft.topbutton;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Administrator on 2016/11/19.
 */

public class ListAdapter extends BaseAdapter {
    List<String> items;
    Context context;


    public ListAdapter(Context context) {
        this.context = context;
    }

    public void addData(List<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        if (items != null &&items.size()>0){
            return items.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (items != null &&items.size()>0){
            return items.get(position);
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        TextView tv = (TextView) view.findViewById(R.id.item_tv);
        tv.setText(items.get(position));
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, items.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
