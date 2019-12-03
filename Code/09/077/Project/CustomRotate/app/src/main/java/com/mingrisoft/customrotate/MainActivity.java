package com.mingrisoft.customrotate;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private MyAnimation myAnimation;//自定义动画
    private ImageView image;//用于展示动画效果
    private boolean isRunning;//判断动画是否在执行

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.animation);//实例化控件
        myAnimation = new MyAnimation();//实例化动画对象
        myAnimation.setAnimationListener(animationListener);//给动画设置监听
        myAnimation.setRepeatCount(0);//不重复播放
    }

    /**
     * 点击开始动画
     *
     * @param view
     */
    public void start(View view) {
        if (!isRunning){
            image.startAnimation(myAnimation);//播放动画
            isRunning = true;
        }
    }

    /**
     * 动画监听
     */
    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        /**
         * 动画开始
         * @param animation
         */
        @Override
        public void onAnimationStart(Animation animation) {

        }

        /**
         * 动画结束
         * @param animation
         */
        @Override
        public void onAnimationEnd(Animation animation) {
            animation.reset();//重置动画
            isRunning = false;
        }

        /**
         * 动画循环
         * @param animation
         */
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    /**
     * 自定义动画
     */
    class MyAnimation extends Animation {
        private int x;//图像的宽度
        private int y;//图形的高度
        private Camera camera;
       @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            this.x = width;
            this.y = height;
            this.setDuration(5000);//设置动画时间
            camera = new Camera();//获取Camera对象
        }
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            final Matrix matrix = t.getMatrix();//获取举证对象
            camera.save();//保存当前视角
            camera.rotateY(360 * interpolatedTime);//延Y轴旋转
            camera.getMatrix(matrix);//设置矩阵
            //图形变换中心
            matrix.preTranslate(-x / 2, -y);
            matrix.postTranslate(x / 2, y);
            camera.restore();//恢复视角
        }
    }
}
