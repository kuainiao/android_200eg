package com.mingrisoft.myfkdj;

//疯狂手指

import java.util.Timer;
import java.util.TimerTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView text_dwon, text_time, text_ci, text_play, moemy_ci;
	int recLen = 9, i = 0, j = 0;
	String k = null;
	Timer timer = new Timer();
	SharedPreferences sp;
	Context ctx;
	ImageButton text_topBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置没有顶部栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置全屏显示
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		ctx = MainActivity.this;
		//开启保存文件方法类型为应用私有，没有者创建用于保存文件
		sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
		//初始化控件
		text_dwon = (TextView) findViewById(R.id.text_dwon);
		text_time = (TextView) findViewById(R.id.text_time);
		text_ci = (TextView) findViewById(R.id.text_ci);
		text_play = (TextView) findViewById(R.id.text_play);
		moemy_ci = (TextView) findViewById(R.id.moemy_ci);
		text_topBack  = (ImageButton) findViewById(R.id.text_topBack);
		if (!sp.getString("user","").equals("")) {
			//获取最高分
			k = sp.getString("user", "");
			//转换为int类型
			j = Integer.valueOf(k).intValue();
			moemy_ci.setText("历史最高得分：" + k + "次");
		}
		//设置控件隐藏
		text_time.setVisibility(View.GONE);
		text_dwon.setVisibility(View.GONE);
		text_ci.setVisibility(View.GONE);
		moemy_ci.setVisibility(View.GONE);
         //绑定点击事件
		text_topBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				i += 1;
				if(i<5){
				text_dwon.setBackgroundResource(R.drawable.t1);
				}else if(i<10){
					text_dwon.setBackgroundResource(R.drawable.t2);
				}else if(i<15){
					text_dwon.setBackgroundResource(R.drawable.t3);
				}else if(i<20){
					text_dwon.setBackgroundResource(R.drawable.t4);
				}else if(i<25){
					text_dwon.setBackgroundResource(R.drawable.t5);
				}else if(i<30){
					text_dwon.setBackgroundResource(R.drawable.t6);
				}else if(i<35){
					text_dwon.setBackgroundResource(R.drawable.t7);
				}else {
					text_dwon.setBackgroundResource(R.drawable.t8);
				}
				text_ci.setText(i + "次");
			}
		});

		text_play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//线程每个1秒执行一次
				timer.schedule(task, 1000, 1000); // timeTask
				//设置控件隐藏
				text_play.setVisibility(View.GONE);
				//设置控件显示
				text_time.setVisibility(View.VISIBLE);
				text_dwon.setVisibility(View.VISIBLE);
				text_ci.setVisibility(View.VISIBLE);
				moemy_ci.setVisibility(View.VISIBLE);
			}
		});
	}

	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			runOnUiThread(new Runnable() { // UI thread
				@Override
				public void run() {
					recLen--;
					text_time.setText(recLen + 1 + " 秒");
					if (recLen < 0) {// 倒计时结束后的事件
						timer.cancel();
						text_dwon.setClickable(false);// 设置为不可点
						dialog();// 游戏结束提示
						// 记住最高分
						if (i > j) {
							//保存最高分
							SharedPreferences.Editor editor = sp.edit();
							editor.putString("user", String.valueOf(i));
							editor.commit();
						}
					}
				}
			});
		}
	};

	// 游戏结束提示
	protected void dialog() {
        //初始化弹出
		AlertDialog.Builder builder = new Builder(MainActivity.this);
        //设置弹出消息
		builder.setMessage("本轮得分:" + i + "次");
        //设置标题
		builder.setTitle("游戏结束");
        //设置退出按钮
		builder.setPositiveButton("退出",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
        //设置从玩按钮
		builder.setNegativeButton("重玩",

				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//重新加载当前页面
						Intent i = new Intent(MainActivity.this, MainActivity.class);
						startActivity(i);
						finish();
					}

				});
		builder.create().show();
	}
}
