package com.mingrisoft.mytetris;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * 文件名:SetActivity.java
 * 文件功能描述:设置页面
 * 开发时间:2016年7月29日
 * 公司网址:www.mingribook.com
 * 开发单位:吉林省明日科技有限公司
 */
public class SetActivity extends PreferenceActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setup);//加载设置布局
		
	}
}
