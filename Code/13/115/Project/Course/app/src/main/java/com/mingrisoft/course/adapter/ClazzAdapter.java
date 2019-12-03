package com.mingrisoft.course.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mingrisoft.course.R;
import com.mingrisoft.course.bean.Clazz;

import java.util.List;

/**
 * Author LYJ
 * Created on 2016/12/28.
 * Time 16:24
 */

public class ClazzAdapter extends BaseAdapter{

    private List<Clazz.ResultEntity> list;
    private Context context;
    private LayoutInflater inflater;

    public ClazzAdapter(List<Clazz.ResultEntity> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item,null);
            holder.image = (ImageView) convertView.findViewById(R.id.show_pic);
            holder.title = (TextView) convertView.findViewById(R.id.show_title);
            holder.teacher = (TextView) convertView.findViewById(R.id.show_teacher);
            holder.time = (TextView) convertView.findViewById(R.id.show_time);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Clazz.ResultEntity entity = list.get(position);
        holder.title.setText(entity.getCourse_name());
        holder.teacher.setText("主讲："+entity.getMain_teacher());
        holder.time.setText("时间："+entity.getClass_hour());
        Glide.with(context).load(entity.getCover()).into(holder.image);
        return convertView;
    }


    class ViewHolder{
        ImageView image;
        TextView title,teacher,time;
    }
}
