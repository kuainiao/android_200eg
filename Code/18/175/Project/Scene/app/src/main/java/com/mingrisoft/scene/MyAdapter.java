package com.mingrisoft.scene;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Author LYJ
 * Created on 2017/1/11.
 * Time 11:16
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder>{

    private  List<Bitmap> bitmaps;
    private List<Integer> heights;
    private LayoutInflater inflater;
    interface OnItemClickListener{
        void onItemClick(ImageView view,int position);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    public MyAdapter(Context context, List<Bitmap> bitmaps, List<Integer> height) {
        this.bitmaps = bitmaps;
        this.heights = height;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        holder.imageView.setImageBitmap(bitmaps.get(position));
        ViewGroup.LayoutParams p = holder.imageView.getLayoutParams();
        p.height = heights.get(position);
        holder.imageView.setLayoutParams(p);
        if (onItemClickListener != null){
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.imageView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }
    /**
     * ViewHolder
     */
   static class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public MyHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.picture);
        }
    }
}
