package com.mingrisoft.hideorshowtitle;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Author LYJ
 * Created on 2017/2/3.
 * Time 09:45
 */
public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    private Toolbar toolbar;//标题栏
    private ListView listView;//列表
    private String[] mStr = new String[20];//测试数据
    private int touchSlop;//滑动判断值
    private float firstY;//起始Y坐标
    private float currentY;//滑动Y坐标
    private int direction;//显示或隐藏标题栏的判断值
    private ObjectAnimator animator;//动画

    /**
     * 初始化界面
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取滑动的判断值
        touchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//使用ToolBar替代ActionBar
        listView = (ListView) findViewById(R.id.listview);
        View header = new View(this);//获取一个View的对象
        //设置View的大小与ActionBar的大小相同
        header.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.abc_action_bar_default_height_material)));
        listView.addHeaderView(header);//像列表添加头部
        //添加测试数据
        for (int i = 0; i < mStr.length; i++) {
            mStr[i] = "Item " + i;
        }
        //绑定适配器
        listView.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, mStr));
        //设置触摸监听
        listView.setOnTouchListener(this);
    }
    /**
     * 标题栏的显示或隐藏的动画
     * @param flag
     */
    private void toolbarAnim(int flag) {
        if (animator != null && animator.isRunning()) {
            animator.cancel();//取消动画
        }
        //设置属性动画是标题栏显示或者隐藏
        animator = ObjectAnimator.ofFloat(toolbar, "translationY",
                toolbar.getTranslationY(),flag == 0 ? 0 : -toolbar.getHeight());
        animator.start();//开始动画
    }

    /**
     * 触摸监听
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstY = event.getY();//获取起始的Y坐标
                break;
            case MotionEvent.ACTION_MOVE:
                currentY = event.getY();//获取滑动的Y坐标
                //判断列表的滑动是向上还是向下
                direction = currentY - firstY > touchSlop ? 0:1;
                toolbarAnim(direction);//隐藏标题栏
                break;
        }
        return false;
    }
}
