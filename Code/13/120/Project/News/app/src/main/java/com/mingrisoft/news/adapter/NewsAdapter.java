package com.mingrisoft.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mingrisoft.news.R;
import com.mingrisoft.news.bean.Reasult;

import java.util.List;

/**
 * Author LYJ
 * Created on 2016/12/6.
 * Time 16:00
 */

public class NewsAdapter extends BaseAdapter{

    private List<Reasult> reasults;
    private LayoutInflater inflater;
    private Context context;
    public NewsAdapter(Context context,List<Reasult> list) {
        this.reasults = list;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public int getCount() {
        return reasults.size();
    }

    @Override
    public Object getItem(int position) {
        return reasults.get(position);
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
            convertView = inflater.inflate(R.layout.news_item,null);
            holder.picUrl = (ImageView) convertView.findViewById(R.id.picture);
            holder.title = (TextView) convertView.findViewById(R.id.titles);
            holder.ctime = (TextView) convertView.findViewById(R.id.ctime);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Reasult reasult = reasults.get(position);
        holder.title.setText(reasult.getTitle());
        holder.ctime.setText(reasult.getCtime());
        holder.description.setText(reasult.getDescription());
        Glide.with(context)
                .load(reasult.getPicUrl())
                .placeholder(R.mipmap.load)
                .error(R.mipmap.error)
                .into(holder.picUrl);
        return convertView;
    }

    class ViewHolder{
        ImageView picUrl;
        TextView ctime,title,description;
    }
}
