package com.mingrisoft.imagechoose;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/2/4.
 */

public class MyImageLoading extends FrameLayout {

    private int resids[];          //用于加载图片
    private ImageView image1, image2, image3, image4, image5;
    int i = 2;
    public MyImageLoading(Context context) {
        super(context);

    }

    public MyImageLoading(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ar = getResources().obtainTypedArray(R.array.imgArray); //获取图片数组
        image1 = new ImageView(context);
        image2 = new ImageView(context);
        image3 = new ImageView(context);
        image4 = new ImageView(context);
        image5 = new ImageView(context);
        /**
         * 设置需要添加视图的布局参数
         * */
        FrameLayout.LayoutParams lytp = new FrameLayout.LayoutParams(dip2px(context, 170), dip2px(context, 270), Gravity.CENTER);
        /**
         * 将图片添加到视图上
         * */
        addView(image1, lytp);
        addView(image2, lytp);
        addView(image3, lytp);
        addView(image4, lytp);
        addView(image5, lytp);
        int len = ar.length();  //获取数组的长度
        resids = new int[len];   //初始化加载图片的数组
        for (int i = 0; i < len; i++) {
            resids[i] = ar.getResourceId(i, 0);
        } //for循环吧图片读取出来
        image1.setImageResource(resids[0]);
        image2.setImageResource(resids[1]);
        image3.setImageResource(resids[2]);
        image4.setImageResource(resids[3]);
        image5.setImageResource(resids[4]);
        image5.setAnimation(AnimationUtils.loadAnimation(context, R.anim.image_in));
        image4.setAnimation(AnimationUtils.loadAnimation(context, R.anim.image_in));
        image3.setAnimation(AnimationUtils.loadAnimation(context, R.anim.image_in));
        image2.setAnimation(AnimationUtils.loadAnimation(context, R.anim.image_in));
        image1.setAnimation(AnimationUtils.loadAnimation(context, R.anim.image_in));
        image5.setRotation(28);
        image4.setRotation(21);
        image3.setRotation(14);
        image2.setRotation(7);
        image1.setRotation(0);
        image5.bringToFront();
        image4.bringToFront();
        image3.bringToFront();
        image2.bringToFront();
        image1.bringToFront();

    }

    public MyImageLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    /**
     * 点击左侧按钮时
     */
    public void leftBtn(Context cnt, int type) {
        setImageAnimation(cnt, type); //开启动画
    }

    /**
     * 点击右侧按钮时
     */
    public void rightBtn(Context cnt, int type) {
        setImageAnimation(cnt, type); //开启动画
    }

    private void setImageAnimation(Context cnt, int type) {

        switch (i) {

            case 1:
                setOutAnimation(cnt, image5, type); //调用图片移除的动画
                image4.bringToFront();
                image3.bringToFront();
                image2.bringToFront();
                image1.bringToFront();
                image1.setRotation(0);
                image2.setRotation(7);
                image3.setRotation(14);
                image4.setRotation(21);
                image5.setRotation(28);

                break;
            case 2:
                setOutAnimation(cnt, image1, type); //调用图片移除的动画
                image5.bringToFront();
                image4.bringToFront();
                image3.bringToFront();
                image2.bringToFront();
                image1.setRotation(28);
                image2.setRotation(0);
                image3.setRotation(7);
                image4.setRotation(14);
                image5.setRotation(21);
                break;
            case 3:
                setOutAnimation(cnt, image2, type); //调用图片移除的动画
                image1.bringToFront();
                image5.bringToFront();
                image4.bringToFront();
                image3.bringToFront();

                image1.setRotation(21);
                image2.setRotation(28);
                image3.setRotation(0);
                image4.setRotation(7);
                image5.setRotation(14);
                break;
            case 4:
                setOutAnimation(cnt, image3, type); //调用图片移除的动画
                image2.bringToFront();
                image1.bringToFront();
                image5.bringToFront();
                image4.bringToFront();
                image1.setRotation(14);
                image2.setRotation(21);
                image3.setRotation(28);
                image4.setRotation(0);
                image5.setRotation(7);
                break;

            case 5:
                setOutAnimation(cnt, image4, type); //调用图片移除的动画
                image3.bringToFront();
                image2.bringToFront();
                image1.bringToFront();
                image5.bringToFront();
                image1.setRotation(7);
                image2.setRotation(14);
                image3.setRotation(21);
                image4.setRotation(28);
                image5.setRotation(0);
                break;

        }
        /**
         * 判断当前图片的位置
         * */
        if (i < 5) {
            i++;
        } else {
            i = 1;
        }
    }
    /**
     * 设置图片移除的动画
     */
    private void setOutAnimation(Context cnt, ImageView iv, int i) {
        if (i == 1) {  //如果点击的是左侧的按钮
            //使图片从由侧飞出
            iv.setAnimation(AnimationUtils.loadAnimation(cnt, R.anim.right_out));
        } else {   //如果点击的是左侧的按钮
            //使图片从左侧飞出
            iv.setAnimation(AnimationUtils.loadAnimation(cnt, R.anim.left_out));
        }
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
