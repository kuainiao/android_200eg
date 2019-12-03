package view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/11/22.
 */

public class QQMenu extends HorizontalScrollView {

    public QQMenu(Context context, AttributeSet attrs) {   //构造方法
        super(context, attrs);
        //获取窗口管理器服务
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        //创建显示尺寸对象
        DisplayMetrics outMetrics = new DisplayMetrics();
        //获取当前屏幕的宽高尺寸
        wm.getDefaultDisplay().getMetrics(outMetrics);
        //为屏幕宽度赋值
        mScreenWidth = outMetrics.widthPixels;
        //将50dp边距转为像素值px
        mMenuRightPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 50,
                context.getResources().getDisplayMetrics());

    }

    private LinearLayout mScrollView;            //定义横向滚动条布局
    private ViewGroup mMenu;                 //定义菜单区域
    private ViewGroup mContent;             //定义主显示区域
    private int mScreenWidth;              //定义屏幕宽度
    private int mMenuRightPadding = 50;   //定义菜单右边距为50dp
    private boolean call;                   //定义只设置一次自己和子视图的宽和高
    private int mMenuWidth;                 //定义菜单宽度

    //设置滚动视图与子视图的宽和高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!call) {                                          //用于判断只设置一次尺寸
            mScrollView = (LinearLayout) getChildAt(0);          //获取滚动视图中的子布局
            mMenu = (ViewGroup) mScrollView.getChildAt(0);      //获取菜单区域
            mContent = (ViewGroup) mScrollView.getChildAt(1);   //获取主显示区域
            //设置菜单宽度
            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
                    - mMenuRightPadding;
            //设置主显示区域宽度
            mContent.getLayoutParams().width = mScreenWidth;
            call = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }



    //设置偏移量让菜单隐藏
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(mMenuWidth, 0);    //滚动条向右移动，主显示区域向左移动
        }
    }



    //判断手指抬起时隐藏菜单还是显示菜单
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();                 //隐藏左边的宽度
                if (scrollX >= mMenuWidth / 2) {             //如果隐藏左边的宽度大于菜单宽度的1/2时
                    this.smoothScrollTo(mMenuWidth, 0);        //隐藏菜单
                } else {
                    this.smoothScrollTo(0, 0);              //显示菜单
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

}
