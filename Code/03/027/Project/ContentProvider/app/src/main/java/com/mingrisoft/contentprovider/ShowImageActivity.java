package com.mingrisoft.contentprovider;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

public class ShowImageActivity extends Activity {
	private GridView mGridView;
	private List<String> list;
	private ChildAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_image_activity);
       //初始化控件
		mGridView = (GridView) findViewById(R.id.child_grid);
		//接收数据信息
		list = getIntent().getStringArrayListExtra("data");
		//初始化适配器
		adapter = new ChildAdapter(this, list, mGridView);
		//为控件绑定适配器
		mGridView.setAdapter(adapter);

	}
   //底部返回键 返回方法
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}

