package com.mingrisoft.crazycat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置不显示顶部栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
	}

    //开始按钮单击事件
	public void onClick(View view) {
		//跳转到游戏页
		Intent intent = new Intent(MainActivity.this, PlayActivity.class);
		startActivity(intent);
	}
}
