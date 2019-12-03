package com.mingrisoft.friendcircle.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.mingrisoft.friendcircle.MyGridView;
import com.mingrisoft.friendcircle.R;
import com.mingrisoft.friendcircle.ShowImageActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/1/24.
 */

public class MyListViewAdapter extends BaseAdapter {
    private Context context;
    /**
     * 手动添加图片数组
     * */
    int[] image = {R.mipmap.a, R.mipmap.b, R.mipmap.c,
            R.mipmap.d, R.mipmap.a, R.mipmap.b,
            R.mipmap.g, R.mipmap.h, R.mipmap.i};
    ArrayList<HashMap<String, Object>> imagelist;  //加载数组的hashmap

    public MyListViewAdapter(Context context) {
        this.context = context;
        imagelist = new ArrayList<HashMap<String, Object>>();  //初始化数组
        // 使用HashMap将图片添加到一个数组中，注意一定要是HashMap<String,Object>类型的，因为装到map中的图片要是资源ID，
        // 而不是图片本身,如果是用findViewById(R.drawable.image)这样把真正的图片取出来了，放到map中是无法正常显示的
        for (int i = 0; i < 9; i++) {  //for循环添加
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", image[i]);
            imagelist.add(map);
        }
    }


    @Override
    public int getCount() {
        return 5;  //listview的item数量
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 5;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder = new MyHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_layout, null);  //绑定item的布局
            /**
             * 绑定id
             * */
            holder.titleIV = (ImageView) convertView.findViewById(R.id.list_title_image);
            holder.titleTV = (TextView) convertView.findViewById(R.id.list_title_text);
            holder.gridView = (MyGridView) convertView.findViewById(R.id.list_item_grid_view);
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }
//        holder.titleIV.setImageDrawable();
        holder.titleTV.setText("这是朋友圈模拟的第" + (position + 1) + "条数据");
        /**
         * 使用simpleAdapter封装数据，将图片显示出来
         * 参数一是当前上下文Context对象
         * 参数二是图片数据列表，要显示数据都在其中
         * 参数三是界面的XML文件，注意，不是整体界面，而是要显示在GridView中的单个Item的界面XML
         * 参数四是动态数组中与map中图片对应的项，也就是map中存储进去的相对应于图片value的key
         * 参数五是单个Item界面XML中的图片ID
         * */
        SimpleAdapter simpleAdapter = new SimpleAdapter(context, imagelist, R.layout.grid_item_layout,
                new String[]{"image"}, new int[]{R.id.grid_item_image});
        holder.gridView.setAdapter(simpleAdapter);      //给GridView设置适配器
        holder.gridView.setOnItemClickListener(new GridViewItemOnClick());   //添加GridView的点击事件
        return convertView;
    }

    class MyHolder {
        ImageView titleIV;
        TextView titleTV;
        MyGridView gridView;
    }

    class GridViewItemOnClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /**
             * 启动展示图片的Activity
             * */
            Intent intent = new Intent(context, ShowImageActivity.class);
            intent.putExtra("id", position);   //将当前点击的位置传递过去
            intent.putExtra("image",image);
            context.startActivity(intent);     //启动Activity
        }
    }
}
