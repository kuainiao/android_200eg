package com.mingrisoft.mbliv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final Context context;
    private static final int ITEM_COUNT = 8;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new HeaderHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_header, parent, false));
        }
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position==1) {
            holder.mTv1.setText("星期一  -10~-20°");
            holder.mTv2.setText("小雪 西北风微风");
        }else if(position==2){
            holder.mTv1.setText("星期二  -11°~-19°");
            holder.mTv2.setText("晴 西北风微风");
        }else if(position==3){
            holder.mTv1.setText("星期三  -11°~-19°");
            holder.mTv2.setText("晴 西北微风");
        }else if(position==4){
            holder.mTv1.setText("星期四  -10°~-18°");
            holder.mTv2.setText("晴 西北风转微风");
        }else if(position==5){
            holder.mTv1.setText("星期五  -7°~-12°");
            holder.mTv2.setText("晴  西南风3-4级");
        }else if(position==6){
            holder.mTv1.setText("星期六  -1°~-7°");
            holder.mTv2.setText("晴转多云  西南风3-4级");
        }else if(position==7){
            holder.mTv1.setText("星期七   1°~-4°");
            holder.mTv2.setText("多云  西南风3-4级");
        }
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    private class HeaderHolder extends MyViewHolder {
        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{
    public TextView mTv1,mTv2;
    public MyViewHolder(View itemView) {
        super(itemView);
        mTv1 = (TextView) itemView.findViewById(R.id.textV1);
        mTv2 = (TextView) itemView.findViewById(R.id.textV2);
    }
}