package com.mingrisoft.mytetris;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 文件名:MainActivity.java
 * 文件功能描述:程序主页面，程序入口
 * 开发时间:2016年7月29日
 * 公司网址:www.mingribook.com
 * 开发单位:吉林省明日科技有限公司
 */
public class MainActivity extends Activity {

	Button startButton;// 开始按钮
	Button setupButton;// 设置按钮
	Button highButton;// 高分按钮
	Button continueButton;// 继续游戏按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);// 加载首页布局
		startButton = (Button) findViewById(R.id.button_start);// 初始化开始按钮
		setupButton = (Button) findViewById(R.id.button_setup);// 初始化设置按钮
		highButton = (Button) findViewById(R.id.button_high);// 初始化高分按钮
		continueButton = (Button) findViewById(R.id.button_continue);// 初始化继续游戏按钮
		SharedPreferences sp = getSharedPreferences("save_game", MODE_PRIVATE);// 获取游戏储存信息，没有信息返回0
		continueButton
				.setVisibility(sp.getBoolean("Runing", false) ? View.VISIBLE
						: View.GONE);// 判断是否有游戏信息 false隐藏继续游戏按钮
		/**
		 * 继续游戏进入游戏页面
		 */
		continueButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent();
				intent.setClass(MainActivity.this, PlayActivity.class);// 进入游戏页面
				intent.putExtra("ContinueGame", true);
				startActivityForResult(intent, 1);// 回调跳转方法
			}
		});

		/**
		 * 开始游戏按钮点击事件 进入游戏页面
		 */
		startButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent();
				intent.setClass(MainActivity.this, PlayActivity.class);// 进入游戏页面
				startActivityForResult(intent, 1);// 回调跳转方法
			}
		});

		/**
		 * 设置按钮点击事件 点击后进入设置页面
		 */
		setupButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SetActivity.class);// 进入设置页面
				startActivity(intent);
			}
		});

		/**
		 * 排行榜按钮点击事件 点击进入高分排行
		 */
		highButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent();
				intent.setClass(MainActivity.this, HighActivity.class);// 进入高分页面
				startActivity(intent);

			}
		});
	}

	@Override
	protected void onDestroy() {// 页面销毁后执行方法
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());// 关闭进程
	}

	/**
	 * 回调 执行方法
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		SharedPreferences sp = getSharedPreferences("save_game", MODE_PRIVATE);// 获取游戏储存信息，没有信息返回0
		continueButton
				.setVisibility(sp.getBoolean("Runing", false) ? View.VISIBLE
						: View.GONE);// 判断是否有游戏信息 false隐藏继续游戏按钮
		super.onActivityResult(requestCode, resultCode, data);
	}
}
