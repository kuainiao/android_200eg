package com.mingrisoft.myschedule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class AseDialog extends Dialog implements android.view.View.OnClickListener{
	//声明控件
	private Button sendButton;
	private Button cancelButton;
	private Button saveButton;
	private EditText lessonName;
	private EditText teacher;
	private EditText location;
	private EditText email;
	private String coursename;
	private AseInterface interfaces;
	private SQLiteDatabase db;
	private String teacher_string="";
	private String location_string="";
	private String email_string="";
	Context c;
	public AseDialog(Context context,AseInterface e) {
		super(context);
		c=context;
		interfaces=e;
		// TODO Auto-generated constructor stub
	}
	public void setCouseName(String name){
		//设置课程名来查询详细信息;
		coursename=name;
		//下面完成查询信息
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialognice);
		cancelButton=(Button)findViewById(R.id.cancelButton);
		sendButton=(Button)findViewById(R.id.sendButton);
		saveButton=(Button)findViewById(R.id.saveButton);
		cancelButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);
		sendButton.setOnClickListener(this);
		lessonName=(EditText)findViewById(R.id.name);
		teacher=(EditText)findViewById(R.id.teacher);
		location=(EditText)findViewById(R.id.location_edittext);
		email=(EditText)findViewById(R.id.email_edittext);
		openDatabase();
		initDialog();//通过coursename来完成Dialog的初始化
		teacher.setText(teacher_string);
		location.setText(location_string);
		email.setText(email_string);
		lessonName.setText(coursename);
	}

	private void initDialog() {
		// TODO Auto-generated method stub
		String sql="select * from course where coursename='"+coursename+"';";
		db.beginTransaction();
		try{
			Cursor cur=db.rawQuery(sql, null);
			if(cur.moveToFirst()){
				int i1=cur.getColumnIndex("teacher");
				int i2=cur.getColumnIndex("location");
				int i3=cur.getColumnIndex("email");
				teacher_string=cur.getString(i1);
				location_string=cur.getString(i2);
				email_string=cur.getString(i3);
			}
			db.setTransactionSuccessful();
		}catch(SQLiteException x){
			Log.i("ASEDIALOG", x.getMessage());
		}finally{
			db.endTransaction();
		}
	}
	private void openDatabase() {
		// TODO Auto-generated method stub
		// 创建或打开数据库
		db = SQLiteDatabase.openOrCreateDatabase(c.getFilesDir()
				.toString() + "/homework.db", null);
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String s1=email.getText().toString().trim();
		switch(v.getId()){
			case R.id.sendButton:
				//发邮件要做的
				db.close();
//				sendEmail();
				break;
			case R.id.cancelButton:
				cancel();
				db.close();
				break;
			case R.id.saveButton:
				if(interfaces!=null){
					if(isEmail(s1)||s1.equals("")){
						interfaces.saveClick(lessonName.getText().toString().trim());//这里应该是新课程名
					}
				}
				saveIntoDatabase();
				if(s1.equals("")||isEmail(s1)){
					cancel();
				}
				break;
		}
	}

	public static boolean isEmail(String strEmail) {
		String strPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}
	public void saveIntoDatabase() {
		// TODO Auto-generated method stub
		//此方法实现数据库保存操作;
		String te=teacher.getText().toString().trim();
		String lo=location.getText().toString().trim();
		String le=lessonName.getText().toString().trim();
		String ema=email.getText().toString().trim();
		String sql="select * from course where coursename ='"+le+"';";
		Log.i("ASEDIALOG-save",sql);
		if(ema.equals("")||isEmail(ema)){//合法输入
			Cursor cursor=db.rawQuery(sql, null);//如果无记录cursor也是有的，不是null
			db.beginTransaction();
			try{
				if(!cursor.moveToFirst()){
					//说明数据库中无该课程信息，插入操作
					String sql2 = "insert into course (coursename,teacher,location,email) values ('"
							+ le
							+ "','"
							+ te
							+ "','"
							+ lo + "','"+ema+"');";
					db.execSQL(sql2);
					Log.i("AseDialog", "insert successful");//测试
				}else{
					//有的话执行更新操作
					if(lo.equals("")&&te.equals("")&&ema.equals("")){
						return;//自动补足信息功能
					}
					else{
						String updatesql = "update course set teacher='"
								+ te + "',location='" +lo
								+ "',email='"+ema+"' where coursename='" + le + "';";
						Log.i("ASEDIALOG-update",updatesql);
						db.execSQL(updatesql);
						Log.i("AseDialog", "update successful");//测试
					}
				}
				db.setTransactionSuccessful();
				//下面更新schedule表,只需改列中的课程名字.在CourseActivity中改.

			}catch(SQLiteException e){
				Log.i("ASEDIALOG", e.getMessage());
			}finally{
				db.endTransaction();
				db.close();
			}
		}else{
			//不合法
			email.setText("不合法输入");
		}
	}
}