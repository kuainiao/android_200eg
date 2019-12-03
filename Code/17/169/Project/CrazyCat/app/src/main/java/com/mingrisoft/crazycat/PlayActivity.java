package com.mingrisoft.crazycat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

public class PlayActivity extends AppCompatActivity {

	PlayGround playground;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		playground = new PlayGround(this);
		//设置不显示顶部栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(playground);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// playground.stopTimer();
	}

}
