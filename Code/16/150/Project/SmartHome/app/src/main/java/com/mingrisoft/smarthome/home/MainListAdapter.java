package com.mingrisoft.smarthome.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mingrisoft.smarthome.R;
import com.mingrisoft.smarthome.detail.DetailActivity;

/**
 * Created by Administrator on 2017/2/17.
 */

public class MainListAdapter extends BaseAdapter {
    private Context context;

    private int resids[];          //用于加载图片
    private String name[] = {"主卧", "次卧", "客厅", "书房", "卫生间", "厨房"};

    public MainListAdapter(Context context) {
        this.context = context;
        /**
         * 将图片提取出来
         * */
        TypedArray ar = context.getResources().obtainTypedArray(R.array.imgArray);
        int len = ar.length();
        resids = new int[len];
        //通过for循环获取数据
        for (int i = 0; i < len; i++) {
            resids[i] = ar.getResourceId(i, 0);
        }

    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_main_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.main_item_image);
            holder.textView = (TextView) convertView.findViewById(R.id.main_item_name);
            holder.layout = (RelativeLayout) convertView.findViewById(R.id.main_item_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setBackgroundResource(resids[position]);
        holder.textView.setText(name[position]);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("name", name[position]);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        RelativeLayout layout;
        ImageView imageView;
        TextView textView;
    }
}
