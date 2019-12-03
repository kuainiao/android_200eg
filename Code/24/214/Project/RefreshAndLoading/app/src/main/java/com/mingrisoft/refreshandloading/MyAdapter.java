package com.mingrisoft.refreshandloading;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.mingrisoft.refreshandloading.cache.AllLruCacheLoad;

public class MyAdapter extends BaseAdapter {
    Context context;
    private List<NewsEntity> list;
    private RequestQueue queue;
    int count = 0;
    AllLruCacheLoad cache;
    ImageLoader imageloader;

    public MyAdapter(Context context,List<NewsEntity> items) {
        this.context = context;
        list = new ArrayList<>();
        this.list = items;
        queue =
                Volley
                        .newRequestQueue(
                                context);
        cache = new AllLruCacheLoad();
        imageloader = new ImageLoader(queue, cache);
    }

    /**
     * 添加数据
     */
    public void addData(int count) {
       this.count = count;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {

        return count;
    }


    @Override
    public Object getItem(int position) {
        return count;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**
         * 绑定item的布局
         * */
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_layout, null);
        TextView nameTv = (TextView) view.findViewById(R.id.name_tv);
        TextView contentTV = (TextView) view.findViewById(R.id.content_tv);
        NetworkImageView imageViewOne = (NetworkImageView) view.findViewById(R.id.thumbnail_pic_s);
        NetworkImageView imageViewTwo = (NetworkImageView) view.findViewById(R.id.thumbnail_pic_s02);
        NetworkImageView imageViewThree = (NetworkImageView) view.findViewById(R.id.thumbnail_pic_s03);
        nameTv.setText(list.get(0).getResult().getData().get(position).getAuthor_name());
        contentTV.setText(list.get(0).getResult().getData().get(position).getTitle());
        imageViewOne.setImageUrl(list.get(0).getResult().getData().get(position).getThumbnail_pic_s(),imageloader );
        imageViewTwo.setImageUrl(list.get(0).getResult().getData().get(position).getThumbnail_pic_s02(), imageloader);
        imageViewThree.setImageUrl(list.get(0).getResult().getData().get(position).getThumbnail_pic_s03(), imageloader);
        return view;
    }

}
