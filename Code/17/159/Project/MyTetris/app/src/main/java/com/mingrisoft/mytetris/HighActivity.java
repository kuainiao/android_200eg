package com.mingrisoft.mytetris;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
/**
 * 
 * 文件名:HighActivity.java
 * 文件功能描述:高分排行榜
 * 开发时间:2016年7月29日
 * 公司网址:www.mingribook.com
 * 开发单位:吉林省明日科技有限公司
 */
public class HighActivity extends Activity {

	TextView nameView[] = new TextView[10];//排行前10人姓名集合
	TextView scoreView[] = new TextView[10];//排行分数集合

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_high);//加载高分布局

		nameView[0] = (TextView) findViewById(R.id.name1);//初始化排行第1姓名控件
		nameView[1] = (TextView) findViewById(R.id.name2);//初始化排行第2姓名控件
		nameView[2] = (TextView) findViewById(R.id.name3);//初始化排行第3姓名控件
		nameView[3] = (TextView) findViewById(R.id.name4);//初始化排行第4姓名控件
		nameView[4] = (TextView) findViewById(R.id.name5);//初始化排行第5姓名控件
		nameView[5] = (TextView) findViewById(R.id.name6);//初始化排行第6姓名控件
		nameView[6] = (TextView) findViewById(R.id.name7);//初始化排行第7姓名控件
		nameView[7] = (TextView) findViewById(R.id.name8);//初始化排行第8姓名控件
		nameView[8] = (TextView) findViewById(R.id.name9);//初始化排行第9姓名控件
		nameView[9] = (TextView) findViewById(R.id.name10);//初始化排行第10姓名控件

		scoreView[0] = (TextView) findViewById(R.id.score1);//初始化排行第1分数控件
		scoreView[1] = (TextView) findViewById(R.id.score2);//初始化排行第2分数控件
		scoreView[2] = (TextView) findViewById(R.id.score3);//初始化排行第3分数控件
		scoreView[3] = (TextView) findViewById(R.id.score4);//初始化排行第4分数控件
		scoreView[4] = (TextView) findViewById(R.id.score5);//初始化排行第5分数控件
		scoreView[5] = (TextView) findViewById(R.id.score6);//初始化排行第6分数控件
		scoreView[6] = (TextView) findViewById(R.id.score7);//初始化排行第7分数控件
		scoreView[7] = (TextView) findViewById(R.id.score8);//初始化排行第8分数控件
		scoreView[8] = (TextView) findViewById(R.id.score9);//初始化排行第9分数控件
		scoreView[9] = (TextView) findViewById(R.id.score10);//初始化排行第10分数控件

		BestScore bestScore = new BestScore(this);//声明初始化 高分类
		// 加载高分榜信息
		bestScore.loadConfig();

		// 显示到界面上
		for (int i = 0; i < 10; i++) {
			nameView[i].setText(bestScore.getName(i));//给名称负值
			scoreView[i].setText(bestScore.getScore(i) + "");//给分数负值
		}
	}
}
