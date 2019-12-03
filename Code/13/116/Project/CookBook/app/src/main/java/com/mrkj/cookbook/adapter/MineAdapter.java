package com.mrkj.cookbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mrkj.cookbook.R;
import com.mrkj.cookbook.bean.MineList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author LYJ
 * Created on 2016/12/8.
 * Time 10:47
 */

public class MineAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private List<MineList.ResultEntity> mList;

    public MineAdapter(List<MineList.ResultEntity> mList, Context context) {
        this.mList = mList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MineList.ResultEntity resultEntity = mList.get(position);
        holder.name.setText(resultEntity.getName());
        holder.description.setText(resultEntity.getDescription());
//        holder.food.setText(resultEntity.getFood());
//        holder.keywords.setText(resultEntity.getKeywords());
        Glide.with(context).load(resultEntity.getImg()).into(holder.image1);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.image1)
        ImageView image1;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.description)
        TextView description;
      /*  @BindView(R.id.food)
        TextView food;
        @BindView(R.id.keywords)
        TextView keywords;*/

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
