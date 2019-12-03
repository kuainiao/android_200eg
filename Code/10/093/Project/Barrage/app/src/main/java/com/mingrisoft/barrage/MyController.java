package com.mingrisoft.barrage;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;

import io.vov.vitamio.widget.MediaController;

/**
 * Author LYJ
 * Created on 2016/12/2.
 * Time 10:56
 */

public class MyController extends MediaController implements View.OnClickListener {

    private static final String TAG = MyController.class.getSimpleName();
    private ImageButton myControllerBackBtn;//返回键
    private ImageButton myControllerChangeBtn;//切换按钮
    private TextView myControllerDataTime;//显示时间
    private Activity mActivity;//使用VideoView的界面
    private ViewGroup mParent;//父布局
    private View mRootTop;//控制器上部分
    private CheckBox mBarrageSwitch;//弹幕开关

    /**
     * 用于监听弹幕开关的状态
     */
    private BarraggeSwitchListener mBarraggeSwitchListener;
    interface BarraggeSwitchListener{
        void barraggeSwitchState(boolean state);
    }
    public void setBarrageSwitchListener(BarraggeSwitchListener barrageSwitchListener){
        mBarraggeSwitchListener = barrageSwitchListener;
    }
    /**
     * 构造方法
     * @param context
     * @param override
     * @param container
     */
    public MyController(Context context,boolean override,ViewGroup container) {
        super(context, override);
        initMineSetting(context);
        mParent = container;
        if (mParent == null){
            throw new NullPointerException("container is null,please add parent for controller");
        }
        addViewToControllerParent(makeControllerView(),Gravity.BOTTOM);
        addViewToControllerParent(makeControllerTop(),Gravity.TOP);
    }


    /**
     * 添加控件
     * @param view
     * @param gravity
     */
    private void addViewToControllerParent(View view ,int gravity) {
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = gravity;
        view.setLayoutParams(params);
        mParent.addView(view);
    }

    /**
     * 初始化我的设置
     */
    private void initMineSetting(Context context) {
        this.mActivity = (Activity) context;
    }

    /**
     * 设置控制器的布局
     *
     * @return
     */
    @Override
    protected View makeControllerView() {
        return mRoot = mActivity.getLayoutInflater()
                .inflate(R.layout.mine_mediacontroller_bottom,this);
    }

    /**
     * 控制器下部分
     * @return
     */
    private View makeControllerTop(){
        return mRootTop = mActivity.getLayoutInflater()
                .inflate(R.layout.mine_mediacontroller_top,null);
    }
    /**
     * 控制器底部控制播放进度、屏幕切换、视频播放与暂停
     *
     * @param bottom
     */
    @Override
    protected void initControllerView(View bottom) {
        super.initControllerView(bottom);
        //切换视频的按钮
        myControllerChangeBtn = (ImageButton) bottom.findViewById(R.id.mediacontroller_change);
        myControllerChangeBtn.setOnClickListener(this);
    }

    @Override
    protected void initAddView() {
        //返回键
        myControllerBackBtn = (ImageButton) mRootTop.findViewById(R.id.mediacontroller_top_back);
        myControllerBackBtn.setOnClickListener(this);
        //显示当前时间
        myControllerDataTime = (TextView) mRootTop.findViewById(R.id.mediacontroller_time);
        //弹幕开关
        mBarrageSwitch = (CheckBox) mRootTop.findViewById(R.id.mediacontroller_barrage_switch);
        mBarrageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    //如果接口对象为空会进行一个异常捕获
                    mBarraggeSwitchListener.barraggeSwitchState(isChecked);
                } catch (Exception e) {
                    Log.e(TAG, "onCheckedChanged: " + e.getMessage());
                }
            }
        });
    }

    /**
     * 为添加的控件设置点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mediacontroller_top_back://返回
                Log.e(TAG, "onClick: 该键可用于当作返回键使用");
                break;
            case R.id.mediacontroller_change://切换
                setChangeWindowSize();
                break;
        }
    }
    /**
     * 控制器显示后将会调用该方法
     */
    @Override
    protected void showAddView() {
        mRootTop.setVisibility(VISIBLE);
        myControllerDataTime.setText(getNowTime());
    }
    /**
     * 隐藏控制器
     */
    @Override
    protected void hideAddView() {
        mRootTop.setVisibility(GONE);
    }
    /**
     * 调节视屏大小
     */
    private void setChangeWindowSize() {
        int mCurrentOrientation = getResources().getConfiguration().orientation;
        WindowManager.LayoutParams attrs = mActivity.getWindow().getAttributes();
        if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            mActivity.getWindow().setAttributes(attrs);
            //设置全屏
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mActivity.getWindow().setAttributes(attrs);
            //取消全屏设置
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
    /**
     * 获取当前时间戳
     *
     * @return
     */
    private static String getNowTime() {
        String dateStr;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        dateStr = hour + ":" + (minute < 10? "0"+minute:minute);
        return dateStr;
    }
}
