package com.mingrisoft.loadtoast;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import net.steamcrafted.loadtoast.LoadToastView;

/**
 * Toast加载
 */
public class LoadToast {

    private LoadToastView loadToastView;           //Toast自定义控件，需要build.gradle文件中的依赖代码
    private ViewGroup parentView;        //父视图
    private int mTranslationY = 0;       //Toast显示的Y坐标位置

    public LoadToast(Context context) {
        loadToastView = new LoadToastView(context);
        //获取父视图的布局文件
        parentView = (ViewGroup) ((Activity) context).getWindow().
                getDecorView().findViewById(android.R.id.content);
        //添加自定义Toast控件
        parentView.addView(loadToastView, new ViewGroup.
                LayoutParams(ViewGroup.LayoutParams.
                WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //设置视图为透明
        ViewHelper.setAlpha(loadToastView, 0f);
    }

    /**
     *设置Toast显示位置的Y坐标
     */
    public LoadToast setTranslationY(int pixels) {
        mTranslationY = pixels;
        return this;
    }

    /**
     * 显示Toast
     */
    public LoadToast show() {
        loadToastView.show();                                           //显示加载进度的控件
        //设置Toast显示屏幕中间的位置
        ViewHelper.setTranslationX(loadToastView, (parentView.getWidth() - loadToastView.getWidth()) / 2);
        //设置Toast显示Y坐标
        ViewHelper.setTranslationY(loadToastView, -loadToastView.getHeight() + mTranslationY);
        //设置显示Toast进入的动画
        ViewPropertyAnimator.animate(loadToastView)
                .alpha(1f)                                          //设置透明度为显示
                .translationY(50 + mTranslationY)                 //设置Toast进入动画滑动的距离
                .setInterpolator(new DecelerateInterpolator())     //设置动画减速
                .setDuration(300)                                  //持续的时间
                .setStartDelay(0).start();                         //启动动画
        return this;
    }

    /**
     * 完成方法
     */
    public void success() {
        loadToastView.success();  //调用完成动画与图标
        vanishingAnimation();        //Toast消失与动画方法
    }

    /**
     * 错误方法
     */
    public void error() {
        loadToastView.error();  //调用错误动画与图标
        vanishingAnimation();      //Toast消失与动画方法
    }


    /**
     * Toast消失与动画
     */
    private void vanishingAnimation() {
        //设置图标消失动画的延迟时间与透明度
        ViewPropertyAnimator.animate(loadToastView).setStartDelay(1000).alpha(0f)
                .translationY(-loadToastView.getHeight() + mTranslationY)    //设置位置
                .setInterpolator(new AccelerateInterpolator())        //设置动画加速
                .setDuration(300).start();                            //设置动画的持续时间并启动
    }
}





