package com.mingrisoft.palette;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Window window;
    private List<Bitmap> bitmaps;
    private ListView showList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//添加ToolBar替代ActionBar
        bitmaps = new ArrayList<>();//图片集合
        showList = (ListView) findViewById(R.id.list);
        for (int i = 1; i <= 12; i++) {
            bitmaps.add(getRes("image" + i));//获取资源中的图片
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = getWindow();//获取窗体对象
        }
        showList.setAdapter(new MyAdapter());//设置适配器
        //监听列表的滑动事件
        showList.setOnScrollListener(new MyScrollListener());
        //初始化主题效果
        changeToolBarWithStatusBarColor(0);
    }

    /**
     * 列表滑动事件
     */
    private class MyScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //根据最先显示出的item来更改颜色
            changeToolBarWithStatusBarColor(view.getFirstVisiblePosition());
        }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    }

    /**
     * 更改状态栏颜色与标题栏颜色
     *
     * @param position
     */
    private void changeToolBarWithStatusBarColor(int position) {
        //异步检测图片的颜色
                    Palette.from(bitmaps.get(position)).generate(new Palette.PaletteAsyncListener() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onGenerated(Palette palette) {
                            //获取到充满活力的这种色调
                            Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                            if (vibrantSwatch != null){
                                //设置ToolBar的背景颜色
                                toolbar.setBackgroundColor(vibrantSwatch.getRgb());
                                if (window != null){
                                    //设置状态栏的颜色
                                    window.setStatusBarColor(colorBurn(vibrantSwatch.getRgb()));
                                    //设置底部功能栏的颜色
                                    window.setNavigationBarColor(colorBurn(vibrantSwatch.getRgb()));
                                }
                }else {
                    //设置ToolBar的背景颜色
                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary));
                    if (window != null){
                        //设置状态栏的颜色
                        window.setStatusBarColor(colorBurn(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary)));
                        //设置底部功能栏的颜色
                        window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary));
                    }
                }
            }
        });
    }

    /**
     * 颜色加深处理
     */
    private int colorBurn(int RGBValues) {
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }

    /**
     * 获取图片资源
     *
     * @param name
     * @return
     */
    public Bitmap getRes(String name) {
        int resID = getResources().getIdentifier(name, "mipmap", getPackageName());
        return BitmapFactory.decodeResource(getResources(), resID);
    }

    /**
     * 适配器
     */
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return bitmaps.size();
        }

        @Override
        public Object getItem(int position) {
            return bitmaps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.list_item, null);
                holder.image = (ImageView) convertView.findViewById(R.id.picture);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.image.setImageBitmap(bitmaps.get(position));
            return convertView;
        }
    }

    /**
     * 复用
     */
    private class ViewHolder {
        ImageView image;
    }

}
