package com.mingrisoft.scene;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.ChangeBounds;
import android.transition.TransitionSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener{
    private List<Bitmap> bitmaps = new ArrayList<>();;
    private List<Integer> height = new
            ArrayList<>();
    private RecyclerView showList;
    private Random random = new Random();
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showList = (RecyclerView) findViewById(R.id.list);
        //获取屏幕的宽度
        DisplayMetrics dm = getResources().getDisplayMetrics();
        width = dm.widthPixels;
        //获取图片
        for (int i = 1; i <= 15; i++) {
            bitmaps.add(getRes("jpg" + i));
        }
        //随机给图片设置高度
        for (int i = 0; i < 15; i++) {
            height.add(getHeight());
        }
        //创建适配器
        MyAdapter adapter = new MyAdapter(this,bitmaps,height);
        //设置item点击事件
        adapter.setOnItemClickListener(this);
        //设置列表的展示效果
        showList.setLayoutManager(new StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL));
        //绑定适配器
        showList.setAdapter(adapter);
    }

    /**
     * 获取随机高度
     * @return
     */
    public int getHeight(){
        //宽度为屏幕宽度一半
        return width /2*(random.nextInt(4)+3)/3;
    }
    /**
     * 获取图片资源
     *
     * @param name
     * @return
     */
    public Bitmap getRes(String name) {
        //获取资源ID值
        int resID = getResources().getIdentifier(name, "mipmap", getPackageName());
        Matrix matrix = new Matrix();//将图像左缩放处理
        matrix.setScale(0.1f, 0.1f);
        //获取图片
        Bitmap bitmap =  BitmapFactory.decodeResource(getResources(), resID);
        //生成缩放后的图片
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth()
                ,bitmap.getHeight(), matrix, true);
        return bitmap;
    }

    /**
     * item点击事件
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(ImageView view, int position) {
        //做一个版本的判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //创建用于实现过渡动画效果的对象
            TransitionSet transitionSet = new TransitionSet();
            //添加过渡效果
            transitionSet.addTransition(new ChangeBounds());
            //设置动画时长为1000毫秒
            transitionSet.setDuration(1000);
            //设置将要跳转的界面的进入动画
            getWindow().setSharedElementEnterTransition(transitionSet);
            //设置将要当前界面的退出动画
            getWindow().setSharedElementExitTransition(transitionSet);
            //设置将要跳转界面的退出动画
            getWindow().setSharedElementReturnTransition(transitionSet);
            //设置重返当前界面的进入动画
            getWindow().setSharedElementReenterTransition(transitionSet);
            //设置跳转界面的意图
            Intent intent = new Intent(this, ShareActivity.class);
            //传值
            intent.putExtra("position",position + 1);
            //设置共享的元素
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, view, "pic");
            //启动跳转界面
            startActivity(intent, options.toBundle());
        }else{//API小于21执行此处跳转界面
            startActivity(new Intent().putExtra("position",position + 1));
        }
    }

}
