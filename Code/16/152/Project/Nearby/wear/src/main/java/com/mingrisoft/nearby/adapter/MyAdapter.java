package com.mingrisoft.nearby.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mingrisoft.nearby.R;
import com.mingrisoft.nearby.entity.Result;

import java.util.ArrayList;

/**
 * 作者： LYJ
 * 功能： 适配器
 * 创建日期： 2017/3/17
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<Result> list;
    private LayoutInflater inflater;

    public MyAdapter(Context context, ArrayList<Result> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Result result = list.get(position);
        holder.title.setText("商家：" + result.getName() + "\n" +
            "地址：" + result.getAddress() + "\n"+"联系方式：" +
                (TextUtils.isEmpty(result.getPhoneNum())? "暂无" : result.getPhoneNum()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.message);
        }
    }
}
