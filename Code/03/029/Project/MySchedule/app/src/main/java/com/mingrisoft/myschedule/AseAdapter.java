package com.mingrisoft.myschedule;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AseAdapter extends BaseAdapter{
	private Context viewContext;
	private ArrayList<TextView> list=new ArrayList<TextView>();
	private ArrayList<Schedule> schedule=new ArrayList<Schedule>();
	private SQLiteDatabase db;
	int width;
	int height;
	public AseAdapter(Context x,int w,int h){
		viewContext=x;
		this.width=w;
		this.height=h;
		openDatabase();
		initList();
	}
	public ArrayList<TextView> getList(){
		return list;
	}
	private void openDatabase() {
		try {
			// 创建或打开数据库
			db = SQLiteDatabase.openOrCreateDatabase(viewContext.getFilesDir()
					.toString() + "/homework.db", null);
			//Toast.makeText(this, "DB was opened!", 1).show();
		}
		catch (SQLiteException e) {
			Log.i("ASEADAPTER", e.getMessage())   ;
		}
	}//createDatabase
	private void initList() {
		// TODO Auto-generated method stub
		initSchedule();//从数据库读入schedule
		for(int k=0;k<54;k++){
			TextView text=new TextView(viewContext);
			text.setTextSize(12);
//			text.setBackgroundColor(viewContext.getResources().getColor(R.color.colorwhitetm));
			text.setTextColor(viewContext.getResources().getColor(R.color.colorbule));
			text.setHeight(height/12);
			text.setGravity(Gravity.CENTER);
			text.setPadding(20, 20, 20,20);
			list.add(text);
		}
		//设置中间小字体
		int k=6;
		for(int i=0;i<8;i++){
			for(int j=0;j<5;j++){
				int p=j+k+1;
				list.get(p).setTextColor(viewContext.getResources().getColor(R.color.colorbule));
				list.get(p).setTextSize(15);
			}
			k+=6;
		}
		list.get(0).setText("课程表");
		list.get(1).setText("周一");
		list.get(2).setText("周二");
		list.get(3).setText("周三");
		list.get(4).setText("周四");
		list.get(5).setText("周五");
		list.get(6).setText("第一节");
		list.get(12).setText("第二节");
		list.get(18).setText("第三节");
		list.get(24).setText("第四节");
		list.get(30).setText("第五节");
		list.get(36).setText("第六节");
		list.get(42).setText("第七节");
		list.get(48).setText("第八节");
		//显示schedule信息
		for(int i=0;i<schedule.size();i++){
			Schedule s=schedule.get(i);
			String date=s.getDate();
			if(date.equals("周一")){
				list.get(7).setText(s.getOne());
				list.get(13).setText(s.getTwo());
				list.get(19).setText(s.getThree());
				list.get(25).setText(s.getFour());
				list.get(31).setText(s.getFive());
				list.get(37).setText(s.getSix());
				list.get(43).setText(s.getSeven());
				list.get(49).setText(s.getEight());
			}
			if(date.equals("周二")){
				list.get(8).setText(s.getOne());
				list.get(14).setText(s.getTwo());
				list.get(20).setText(s.getThree());
				list.get(26).setText(s.getFour());
				list.get(32).setText(s.getFive());
				list.get(38).setText(s.getSix());
				list.get(44).setText(s.getSeven());
				list.get(50).setText(s.getEight());
			}
			if(date.equals("周三")){
				list.get(9).setText(s.getOne());
				list.get(15).setText(s.getTwo());
				list.get(21).setText(s.getThree());
				list.get(27).setText(s.getFour());
				list.get(33).setText(s.getFive());
				list.get(39).setText(s.getSix());
				list.get(45).setText(s.getSeven());
				list.get(51).setText(s.getEight());
			}
			if(date.equals("周四")){
				list.get(10).setText(s.getOne());
				list.get(16).setText(s.getTwo());
				list.get(22).setText(s.getThree());
				list.get(28).setText(s.getFour());
				list.get(34).setText(s.getFive());
				list.get(40).setText(s.getSix());
				list.get(46).setText(s.getSeven());
				list.get(52).setText(s.getEight());
			}
			if(date.equals("周五")){
				list.get(11).setText(s.getOne());
				list.get(17).setText(s.getTwo());
				list.get(23).setText(s.getThree());
				list.get(29).setText(s.getFour());
				list.get(35).setText(s.getFive());
				list.get(41).setText(s.getSix());
				list.get(47).setText(s.getSeven());
				list.get(53).setText(s.getEight());
			}
		}
	}
	private void initSchedule() {
		// TODO Auto-generated method stub
		String sql="select * from schedule;";
		Cursor cur=db.rawQuery(sql, null);
		if(cur.moveToFirst()){
			//有的话初始schedule
			int date_int=cur.getColumnIndex("date");
			int one_int=cur.getColumnIndex("one");
			int two_int=cur.getColumnIndex("two");
			int three_int=cur.getColumnIndex("three");
			int four_int=cur.getColumnIndex("four");
			int five_int=cur.getColumnIndex("five");
			int six_int=cur.getColumnIndex("six");
			int seven_int=cur.getColumnIndex("seven");
			int eight_int=cur.getColumnIndex("eight");
			String date_str="";
			String one_str="";
			String two_str="";
			String three_str="";
			String four_str="";
			String five_str="";
			String six_str="";
			String seven_str="";
			String eight_str="";

			do{
				date_str=cur.getString(date_int);
				one_str=cur.getString(one_int);
				two_str=cur.getString(two_int);
				three_str=cur.getString(three_int);
				four_str=cur.getString(four_int);
				five_str=cur.getString(five_int);
				six_str=cur.getString(six_int);
				seven_str=cur.getString(seven_int);
				eight_str=cur.getString(eight_int);
				Schedule s=new Schedule();
				s.setDate(date_str);
				s.setOne(one_str);
				s.setTwo(two_str);
				s.setThree(three_str);
				s.setFour(four_str);
				s.setFive(five_str);
				s.setSix(six_str);
				s.setSeven(seven_str);
				s.setEight(eight_str);
				schedule.add(s);
			}while(cur.moveToNext());
		}
		db.close();
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView textview;
		if(convertView==null){
			textview=list.get(position);

		}else{
			textview=(TextView) convertView;
		}
		return textview;
	}
}