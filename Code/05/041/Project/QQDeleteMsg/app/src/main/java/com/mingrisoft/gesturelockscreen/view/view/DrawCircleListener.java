package com.mingrisoft.gesturelockscreen.view.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mingrisoft.gesturelockscreen.R;
import com.mingrisoft.gesturelockscreen.view.MainActivity;


/**
 * Created by Root on 2016/6/25.
 */
public class DrawCircleListener implements View.OnTouchListener, DrawCircle.onDragBallListener {

    private final DrawCircle mDrawCircle;
    private final WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private Context mContext;
    private View mPoint;


    public DrawCircleListener(Context context, TextView msgPoint){

        mContext = context;
        mPoint = msgPoint;

        mDrawCircle = new DrawCircle(mContext);
        mDrawCircle.setonDragBallListener(this);
        mDrawCircle.setDragRang(dp2px(context,60));  //60dp
        mDrawCircle.setNumber((Integer) msgPoint.getTag());

        mWindowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);

        mParams = new WindowManager.LayoutParams();
        mParams.format = PixelFormat.TRANSLUCENT;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {

            mParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;  //设置状态栏透明

        }

    }



    /**
     * 手势的判断
     * */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        /**
         * 当手指落下时的操作
         **/
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            mPoint.getParent().requestDisallowInterceptTouchEvent(true); //获取手势
            mPoint.setVisibility(View.INVISIBLE);   //使原来的图片隐藏
            //将不动圆中心点的坐标传过去
            mDrawCircle.setCenterPoint(event.getRawX()-5, event.getRawY()+55);
            mWindowManager.addView(mDrawCircle, mParams); //然后将滑动的小点加载到windowManager中
        }
        mDrawCircle.onTouchEvent(event);//讲手指坐标时间传给移动的圆
        return true;
    }




    @Override
    public void onDisappear(DrawCircle drawCircle, float x, float y) {


        if (mWindowManager != null && mDrawCircle.getParent() != null){
            mWindowManager.removeView(mDrawCircle);
        }

        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(R.drawable.frame_bao);
        AnimationDrawable mAnimDrawable = (AnimationDrawable) imageView
                .getDrawable();

        final MyLayout bubbleLayout = new MyLayout(mContext);


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {

            bubbleLayout.setCenter((int) x, (int) (y ));    //4.4以上把状态栏设置为透明后,顶上去了,不用减状态栏高度了
        }else {

            bubbleLayout.setCenter((int) x, (int) (y ));
        }


        bubbleLayout.addView(imageView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));

        mWindowManager.addView(bubbleLayout, mParams);

        mAnimDrawable.start();

        // 播放结束后，移除帧动画
        MainActivity.mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mWindowManager.removeView(bubbleLayout);
            }
        }, 600);
    }

    @Override
    public void onReset() {

        if (mWindowManager != null && mDrawCircle.getParent() != null){
            mWindowManager.removeView(mDrawCircle);
            mPoint.setVisibility(View.VISIBLE);
        }
    }


    public int dp2px(Context context,int dp){

        return (int) (context.getResources().getDisplayMetrics().density * dp + .5f);
    }

}
