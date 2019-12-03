package com.mingrisoft.mywhiteblock;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    //定义宽
	public static int screen_width;
	//定义高
	public static int screen_height;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置没有顶部栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置全屏显示
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//获取屏幕信息
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		//屏幕宽
		screen_width = wm.getDefaultDisplay().getWidth();
		//屏幕高
		screen_height = wm.getDefaultDisplay().getHeight();
		//装载布局
		setContentView(new GameView(this));
	}
}
