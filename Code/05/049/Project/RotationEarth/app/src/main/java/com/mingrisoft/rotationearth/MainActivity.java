package com.mingrisoft.rotationearth;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;


import com.mingrisoft.mylibrary.RotateGestureDetector;


public class MainActivity extends Activity implements OnTouchListener {

    private Matrix mMatrix = new Matrix();
    private float mScaleFactor = .4f;
    private float mRotationDegrees = 0.f;
    private float mFocusX = 0.f;
    private float mFocusY = 0.f;
    private int mImageHeight, mImageWidth;

    private ScaleGestureDetector mScaleDetector;  //定义图片的放大缩小
    private RotateGestureDetector mRotateDetector;  //设置图片旋转

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 获取屏幕的中心点
        Display display = getWindowManager().getDefaultDisplay();
        mFocusX = display.getWidth() / 2f;    //屏幕的宽度一半
        mFocusY = display.getHeight() / 2f;   //屏幕的高度一半

        // 将图片设置触碰监听
        ImageView earthView = (ImageView) findViewById(R.id.imageView);
        earthView.setOnTouchListener(this);

        // 确定维度的地球图像”
        Drawable d = this.getResources().getDrawable(R.mipmap.earth);
        mImageHeight = d.getIntrinsicHeight();
        mImageWidth = d.getIntrinsicWidth();

        // 视图缩放和转动的矩阵
        float scaledImageCenterX = (mImageWidth * mScaleFactor) / 2;
        float scaledImageCenterY = (mImageHeight * mScaleFactor) / 2;

        mMatrix.postScale(mScaleFactor, mScaleFactor);
        /**
         * 用Matrix类中的平移方法设置地球的位置
         * */
        mMatrix.postTranslate(mFocusX - scaledImageCenterX, mFocusY - scaledImageCenterY);
        earthView.setImageMatrix(mMatrix);     //将地球设置到屏幕中心

        // 设置手势检测器
        mScaleDetector = new ScaleGestureDetector(getApplicationContext(), new ScaleListener());  //图片缩放
        mRotateDetector = new RotateGestureDetector(getApplicationContext(), new RotateListener());  //图片的旋转
    }

    /**
     * 设置手势
     * */
    @SuppressWarnings("deprecation")
    public boolean onTouch(View v, MotionEvent event) {
        mScaleDetector.onTouchEvent(event);  //设置图片缩放的监听
        mRotateDetector.onTouchEvent(event);  //设置图片的旋转监听

        float scaledImageCenterX = (mImageWidth * mScaleFactor) / 2;
        float scaledImageCenterY = (mImageHeight * mScaleFactor) / 2;
        mMatrix.reset();
        mMatrix.postScale(mScaleFactor, mScaleFactor);
        mMatrix.postRotate(mRotationDegrees, scaledImageCenterX, scaledImageCenterY);
        mMatrix.postTranslate(mFocusX - scaledImageCenterX, mFocusY - scaledImageCenterY);

        ImageView view = (ImageView) v;
        view.setImageMatrix(mMatrix);
        return true; // 指示事件处理
    }

    /**
     * 图片的放大缩小的监听
     * */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            //不要让物体太小或太大。
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            return true;
        }
    }
    /**
     * 图片转动的监听
     * */
    private class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
        @Override
        public boolean onRotate(RotateGestureDetector detector) {
            mRotationDegrees -= detector.getRotationDegreesDelta();
            return true;
        }
    }
}