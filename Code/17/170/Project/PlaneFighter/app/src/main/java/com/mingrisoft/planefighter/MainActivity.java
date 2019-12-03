package com.mingrisoft.planefighter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import com.mingrisoft.planefighter.view.AirSurfaceView;
import com.mingrisoft.planefighter.view.IGameEventListener;

public class MainActivity extends Activity implements IGameEventListener, View.OnClickListener {

	private AirSurfaceView mainView;
	private Button restart;
	private FrameLayout rootView;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler = new Handler();
		//设置全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//取消掉顶部栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		mainView.setListener(this);

	}

	private void initView() {
		//设置在屏幕中满屏显示
		FrameLayout.LayoutParams full = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		//设置在屏幕中中间位置显示
		FrameLayout.LayoutParams center = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		//新的游戏布局
		rootView = new FrameLayout(this);
		//布局背景
		rootView.setBackgroundColor(Color.WHITE);
		//初始化游戏页面
		mainView = new AirSurfaceView(this);
		//添加游戏页面到布局全屏显示
		rootView.addView(mainView, full);
		//中间位置显示
		center.gravity = Gravity.CENTER;
		//创建按钮
		restart = new Button(this);
		//设置按钮文字
		restart.setText(R.string.restart_game);
		//设置重新开始游戏按钮字号
		restart.setTextSize(getResources().getDimension(R.dimen.restart_btn_text_size));
		//隐藏游戏按钮
		restart.setVisibility(View.GONE);
		//绑定按钮点击事件
		restart.setOnClickListener(this);
		//添加按钮到布局中间位置
		rootView.addView(restart, center);
		//设置新建布局为当前页面布局
		setContentView(rootView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mainView.postInvalidate();
	}

	@Override
	public void onGameStart() {

	}
   //游戏结束
	@Override
	public void onGameFinish(int score) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				//显示重新开始按钮
				restart.setVisibility(View.VISIBLE);
			}
		});

	}
     //重新开始按钮单击事件
	@Override
	public void onClick(View v) {
		//判断是否是游戏开始如果是开始
		if (v.equals(restart)) {
			//隐藏重新开始按钮
			restart.setVisibility(View.GONE);
			//重置游戏状态
			mainView.reset();
		}
	}
}
