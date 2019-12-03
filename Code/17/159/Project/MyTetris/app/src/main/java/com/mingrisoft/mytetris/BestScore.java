package com.mingrisoft.mytetris;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 文件名:BestScore.java
 * 文件功能描述:高分排行类 处理高分排行相关信息
 * 开发时间:2016年7月29日
 * 公司网址:www.mingribook.com
 * 开发单位:吉林省明日科技有限公司
 */
public class BestScore {
	Context context;//上下文
	int scores[] = new int[10];//记录10个高分
	String names[] = new String[10];//10个人的高分姓名
	boolean bChanged = false;//高分榜是否有变动。
	public BestScore(Context context) {//初始化高分功能
		this.context = context;
	}
	
	/**
	 * 加载配置中的排行信息
	 */
	public void loadConfig()
	{
		SharedPreferences sp = context.getSharedPreferences("BestScore",Activity.MODE_PRIVATE);
		for(int i=0;i<10;i++)
		{
			scores[i] = sp.getInt("score"+i, 0);
			names[i] = sp.getString("name"+i, "无");
		}
	}
	
	/**
	 * 保存高分榜到配置文件中
	 */
	public void saveConfig()
	{
		if(!bChanged) return;
		Editor editor = context.getSharedPreferences("BestScore",Activity.MODE_PRIVATE).edit();
		
		for(int i=0;i<10;i++)
		{
			editor.putInt("score"+i, scores[i]);
			editor.putString("name"+i, names[i]);
		}
		
		editor.commit();
	}
	
	/**
	 * 获取指定的排行的姓名
	 */
	public String getName(int pos)
	{
		return names[pos];
	}
	
	/**
	 * 获取指定排行的分数
	 */
	public int getScore(int pos)
	{
		return scores[pos];
	}
	
	/**
	 * 测试分数是否进入高分榜
	 */
	public boolean testScore(int score)
	{
		for(int i=9;i>=0;i--)
		{
			if(score>scores[i]) 
				return true;
		}
		return false;
	}
	
	/**
	 * 添加姓名与分数到高分榜中。
	 */
	public void insertScore(String name,int score)
	{
		
		for(int i=8;i>=0;i--)
		{
			//把底部的信息向下移动
			if(score>scores[i]) 
			{
				scores[i+1] = scores[i];
				names[i+1] = names[i];
			}
			
			if(i==0 || (i>0 && score< scores[i-1]))
			{
				scores[i] = score;
				names[i] = name;
				
				bChanged = true;
				break;
			}
		}
	}
}
