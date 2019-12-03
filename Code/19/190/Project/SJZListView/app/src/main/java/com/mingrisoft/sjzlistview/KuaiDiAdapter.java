package com.mingrisoft.sjzlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class KuaiDiAdapter extends BaseAdapter {
    //印章数据
    private List<KuaiDi> list;
    //获取LayoutInflater实例用来获取布局
    private LayoutInflater mInflater;

    public KuaiDiAdapter(Context context, List<KuaiDi> list) {
        this.list = list;
        mInflater = LayoutInflater.from(context);
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
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.time_item, null);
            holder = new ViewHolder();
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            //绑定ViewHolder对象
            convertView.setTag(holder);
        } else {
            //取出ViewHolder对象
            holder = (ViewHolder) convertView.getTag();
        }
        KuaiDi kd = list.get(position);
        holder.tv_content.setText(kd.getContent());
        holder.tv_time.setText(kd.getTime());
        return convertView;
    }

    /**
     * 控件管理类
     */
    private class ViewHolder {
        private TextView tv_content, tv_time;
    }
}
