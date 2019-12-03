package com.mingrisoft.mytetris;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * 文件名:PlayActivity.java
 * 文件功能描述:游戏页面
 * 开发时间:2016年7月29日
 * 公司网址:www.mingribook.com
 * 开发单位:吉林省明日科技有限公司
 */
public class PlayActivity extends Activity {

	MyGameView gameView;// 游戏页面

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gameView = new MyGameView(this);// 初始化游戏view
		setContentView(gameView);// 加载自定义游戏布局
		Intent intent = getIntent();// 接受传递过来的信息
		boolean bRuning = false;// 用于判断是否是继续游戏 是否加载完成 默认没有加载

		if (intent != null) {// intent不为空有数据传递过来
			if (intent.getBooleanExtra("ContinueGame", false)) {// 判断是否为true 如果为true代表继续游戏进入的当前页面
				if (gameView.loadGame()) {// 判断游戏数据是否加载完成
					bRuning = true;// 设置已经开始游戏
				}
			}
		}
		if (!bRuning) {// 判断游戏是否已经加载完成
			gameView.startGame(1000);// 速度最慢的方式开始游戏
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {// 自定义底部按键方法
		if (keyCode == KeyEvent.KEYCODE_BACK) {//当按下底部返回键的时候执行该方法
			gameView.saveGame();// 保存游戏
			setResult(RESULT_OK, getIntent());// 回传数据RESULT_OK
			finish();// 结束当前页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {// 页面销毁后执行方法
		gameView.finish();// 关闭自定义游戏view
		super.onDestroy();
	}

	@Override
	protected void onStop() {// 页面停止活动的时候执行该方法
		super.onStop();
		gameView.pauseGame();// 暂停游戏
	}

}
