package com.mingrisoft.azsort;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{

	private Context context;
	private List<String> data;
	private LayoutInflater mLayoutInflater;
	private List<Integer> letterCharList;
	private String[] title;
	
	public MyAdapter(Context context, List<String> data, 
			List<Integer> letterCharList, String[] title) {
		super();
		this.context = context;
		this.data = data;
		this.letterCharList = letterCharList;
		this.title = title;
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(mLayoutInflater == null){
			mLayoutInflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		if(convertView == null){
			convertView = mLayoutInflater.inflate(R.layout.maillist_item, null, false);
		}
		TextView tv02 = (TextView) convertView.findViewById(R.id.mainlist_item_tv01);
		//判断是否显示分类提示
		if(letterCharList.get(position) >= 0){
			tv02.setVisibility(View.VISIBLE);
			tv02.setText(title[letterCharList.get(position)]);
		}else{
			tv02.setVisibility(View.GONE);
		}
		tv02.setTextColor(Color.BLACK);
		TextView tv01 = (TextView) convertView.findViewById(R.id.mainlist_item_tv02);
		//设置显示城市内容
		tv01.setText(data.get(position));
		tv01.setTextColor(Color.BLACK);
		//绑定列表点击方法
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                //设置当前定位城市
				MainActivity.cityTV.setText(data.get(position));
			}
		});
		return convertView;
	}

}
