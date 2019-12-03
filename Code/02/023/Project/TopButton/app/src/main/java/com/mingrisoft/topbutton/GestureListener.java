package com.mingrisoft.topbutton;

/**
 * Created by dllo on 16/1/23.
 */

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * 实现监听左右滑动的事件，哪个view需要的时候直接setOnTouchListener就可以用了
 *
 */
public class GestureListener extends SimpleOnGestureListener implements OnTouchListener {
    /**
     * 滑动最短距离
     */
    private MotionEvent downEvent = null;
    private int distance = 10;
    private ListView listView;
    private ImageButton button;
    /**
     * 滑动的最大速度
     */
    private int velocity = 0;

    private GestureDetector gestureDetector;

    public GestureListener(ListView listView, ImageButton button, Context context) {
        gestureDetector = new GestureDetector(context, this);
        this.listView = listView;
        this.button = button;
    }



    /**
     * 向上滑的时候调用的方法，子类应该重写
     *
     * @return
     */
    public boolean up(ImageButton imageBtn) {
        listView.bringToFront();
        imageBtn.setVisibility(View.INVISIBLE);
        return false;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        downEvent =e;
        return false;
    }

    /**
     * 向下滑的时候调用的方法，子类应该重写
     *
     * @return
     */
    public boolean down(ImageButton button) {
        button.bringToFront();
        button.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // e1：第1个ACTION_DOWN MotionEvent
        // e2：最后一个ACTION_MOVE MotionEvent
        // velocityX：X轴上的移动速度（像素/秒）
        // velocityY：Y轴上的移动速度（像素/秒）
       if(null==e1){e1=downEvent;}
        if(e1==null||e2==null){return false;}
        float beginY = e1.getY();  //开始位置的y轴坐标
        float endY = e2.getY();     //结束位置的y轴坐标
        if (beginY - endY > distance && Math.abs(velocityY) > velocity) {   //上滑
            up(button);  //使指定按钮隐藏
        } else if (endY - beginY > distance && Math.abs(velocityY) > velocity) {   //下滑
            down(button);   //是制定按钮显示
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return false;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public GestureDetector getGestureDetector() {
        return gestureDetector;
    }

    public void setGestureDetector(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }
}
