package com.mingrisoft.myschedule;

import java.util.ArrayList;
import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Context context;
	LineGridView gridview;
	private SQLiteDatabase db;
	AseAdapter ase;
	ArrayList<TextView> courselist;
	int index=0;//选中的view的下标
	int width;
	int height;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		WindowManager wm = this.getWindowManager();

		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();

		setContentView(R.layout.main);
		context=this;
		gridview=(LineGridView) findViewById(R.id.gridview);
		registerForContextMenu(gridview);
		try {
			openDatabase();		//open (create if needed) database
			initTable();
			db.close();			//make sure to release the DB
		} catch (Exception e) {
		}
		ase=new AseAdapter(this,width,height);
		gridview.setAdapter(ase);
		gridview.setOnItemClickListener(new AseListener(this));
	}
	private void openDatabase() {
		try {
		// 创建或打开数据库homework.db3
			db = SQLiteDatabase.openOrCreateDatabase(context.getFilesDir()
					.toString() + "/homework.db", null);
			//Toast.makeText(this, "DB was opened!", 1).show();
		}
		catch (SQLiteException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}//createDatabase
	private void initTable() {
		//create table: tblAmigo
		db.beginTransaction();
		try {
			//创建表
			db.execSQL("create table schedule ("
					+ "sid integer PRIMARY KEY autoincrement, "
					+ " date  text, "
					+" one text, "
					+" two text, "
					+"three text,"
					+ "four text,"+"five text,"+"six text,"+"seven text,"+"eight text);");
			String sql="create table course(lid integer PRIMARY KEY autoincrement,coursename text,teacher text,location text,email text);";
			db.execSQL(sql);
			//commit your changes
			db.setTransactionSuccessful();

			//Toast.makeText(this, "Table was created",1).show();
		} catch (SQLException e1) {
			//Toast.makeText(this, e1.getMessage(),1).show();
		}
		finally {
			db.endTransaction();
		}

	}//初始化两个表
    //列表点击事件
	class AseListener implements OnItemClickListener{
		Context context;
		public AseListener(Context x){
			context=x;
		}
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
			//这里通过arg2来获得课程名字，传给AseDialog
			if (arg2 == 0 || arg2 == 1 || arg2 == 2 || arg2 == 3 || arg2 == 4
					|| arg2 == 5 || arg2 == 6 || arg2 == 12 || arg2 == 18
					|| arg2 == 24|| arg2 == 30|| arg2 == 36|| arg2 == 42|| arg2 == 48){
				return;
			}
			else{
				//显示修改的Dialog
				index=arg2;
				courselist=ase.getList();
				GetDialogReturnListener listener=new GetDialogReturnListener();
				AseDialog dia=new AseDialog(context,listener);
				dia.setCouseName(courselist.get(arg2).getText().toString().trim());//传入课程名字
				dia.show();
			}
		}

	}
//	@Override
//	protected void onResume() {
//		/**
//		 * 设置为横屏
//		 */
//		if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		}
//		super.onResume();
//	}
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		return super.onCreateDialog(id);
	}
	class GetDialogReturnListener implements AseInterface{

		public void saveClick(String name) {
			// TODO Auto-generated method stub
			openDatabase();
			//更新所选TextView
			courselist.get(index).setText(name);
			//下面数据库update schedule表
			if(index==7||index==13||index==19||index==25||index==31||index==37||index==43||index==49){
				//周一课程
				String sql="select * from schedule where date='周一';";
				Cursor cur=db.rawQuery(sql, null);
				db.beginTransaction();
				try{
					if(cur.moveToFirst()){
						//update
						String update="";
						switch(index){
							case 7:
								update="update schedule set one='"+name+"' where date='周一';";
								break;
							case 13:
								update="update schedule set two='"+name+"' where date='周一';";
								break;
							case 19:
								update="update schedule set three='"+name+"' where date='周一';";
								break;
							case 25:
								update="update schedule set four='"+name+"' where date='周一';";
								break;
							case 31:
								update="update schedule set five='"+name+"' where date='周一';";
								break;
							case 37:
								update="update schedule set six='"+name+"' where date='周一';";
								break;
							case 43:
								update="update schedule set seven='"+name+"' where date='周一';";
								break;
							case 49:
								update="update schedule set eight='"+name+"' where date='周一';";
								break;
						}
						db.execSQL(update);
						db.setTransactionSuccessful();
					}else{
						//insert
						String insert="";
						switch(index){
							case 7:
								insert="insert into schedule (one,date) values ('"+name+"','周一');";
								break;
							case 13:
								insert="insert into schedule (two,date) values ('"+name+"','周一');";
								break;
							case 19:
								insert="insert into schedule (three,date) values ('"+name+"','周一');";
								break;
							case 25:
								insert="insert into schedule (four,date) values ('"+name+"','周一');";
								break;
							case 31:
								insert="insert into schedule (five,date) values ('"+name+"','周一');";
								break;
							case 37:
								insert="insert into schedule (six,date) values ('"+name+"','周一');";
								break;
							case 43:
								insert="insert into schedule (seven,date) values ('"+name+"','周一');";
								break;
							case 49:
								insert="insert into schedule (eight,date) values ('"+name+"','周一');";
								break;
						}
						db.execSQL(insert);
						db.setTransactionSuccessful();
					}
				}catch(SQLiteException x){
					Log.i("COURSEACTIVITY",x.getMessage());
				}finally{
					db.endTransaction();
				}
			}
			if(index==8||index==14||index==20||index==26||index==32||index==38||index==44||index==50){
				//周二课程
				String sql="select * from schedule where date='周二';";
				Cursor cur=db.rawQuery(sql, null);
				db.beginTransaction();
				try{
					if(cur.moveToFirst()){
						//update
						String update="";
						switch(index){
							case 8:
								update="update schedule set one='"+name+"' where date='周二';";
								break;
							case 14:
								update="update schedule set two='"+name+"' where date='周二';";
								break;
							case 20:
								update="update schedule set three='"+name+"' where date='周二';";
								break;
							case 26:
								update="update schedule set four='"+name+"' where date='周二';";
								break;
							case 32:
								update="update schedule set five='"+name+"' where date='周二';";
								break;
							case 38:
								update="update schedule set six='"+name+"' where date='周二';";
								break;
							case 44:
								update="update schedule set seven='"+name+"' where date='周二';";
								break;
							case 50:
								update="update schedule set eight='"+name+"' where date='周二';";
								break;
						}
						db.execSQL(update);
						db.setTransactionSuccessful();
					}else{
						//insert
						String insert="";
						switch(index){
							case 8:
								insert="insert into schedule (one,date) values ('"+name+"','周二');";
								break;
							case 14:
								insert="insert into schedule (two,date) values ('"+name+"','周二');";
								break;
							case 20:
								insert="insert into schedule (three,date) values ('"+name+"','周二');";
								break;
							case 26:
								insert="insert into schedule (four,date) values ('"+name+"','周二');";
								break;
							case 32:
								insert="insert into schedule (five,date) values ('"+name+"','周二');";
								break;
							case 38:
								insert="insert into schedule (six,date) values ('"+name+"','周二');";
								break;
							case 44:
								insert="insert into schedule (seven,date) values ('"+name+"','周二');";
								break;
							case 50:
								insert="insert into schedule (eight,date) values ('"+name+"','周二');";
								break;
						}
						db.execSQL(insert);
						db.setTransactionSuccessful();
					}
				}catch(SQLiteException x){
					Log.i("COURSEACTIVITY",x.getMessage());
				}finally{
					db.endTransaction();
				}
			}
			if(index==9||index==15||index==21||index==27||index==33||index==39||index==45||index==51){
				//周三课程
				String sql="select * from schedule where date='周三';";
				Cursor cur=db.rawQuery(sql, null);
				db.beginTransaction();
				try{
					if(cur.moveToFirst()){
						//update
						String update="";
						switch(index){
							case 9:
								update="update schedule set one='"+name+"' where date='周三';";
								break;
							case 15:
								update="update schedule set two='"+name+"' where date='周三';";
								break;
							case 21:
								update="update schedule set three='"+name+"' where date='周三';";
								break;
							case 27:
								update="update schedule set four='"+name+"' where date='周三';";
								break;
							case 33:
								update="update schedule set five='"+name+"' where date='周三';";
								break;
							case 39:
								update="update schedule set six='"+name+"' where date='周三';";
								break;
							case 45:
								update="update schedule set seven='"+name+"' where date='周三';";
								break;
							case 51:
								update="update schedule set eight='"+name+"' where date='周三';";
								break;
						}
						db.execSQL(update);
						db.setTransactionSuccessful();
					}else{
						//insert
						String insert="";
						switch(index){
							case 9:
								insert="insert into schedule (one,date) values ('"+name+"','周三');";
								break;
							case 15:
								insert="insert into schedule (two,date) values ('"+name+"','周三');";
								break;
							case 21:
								insert="insert into schedule (three,date) values ('"+name+"','周三');";
								break;
							case 27:
								insert="insert into schedule (four,date) values ('"+name+"','周三');";
								break;
							case 33:
								insert="insert into schedule (five,date) values ('"+name+"','周三');";
								break;
							case 39:
								insert="insert into schedule (six,date) values ('"+name+"','周三');";
								break;
							case 45:
								insert="insert into schedule (seven,date) values ('"+name+"','周三');";
								break;
							case 51:
								insert="insert into schedule (eight,date) values ('"+name+"','周三');";
								break;
						}
						db.execSQL(insert);
						db.setTransactionSuccessful();
					}
				}catch(SQLiteException x){
					Log.i("COURSEACTIVITY",x.getMessage());
				}finally{
					db.endTransaction();
				}
			}
			if(index==10||index==16||index==22||index==28||index==34||index==40||index==46||index==52){
				//周四课程
				String sql="select * from schedule where date='周四';";
				Cursor cur=db.rawQuery(sql, null);
				db.beginTransaction();
				try{
					if(cur.moveToFirst()){
						//update
						String update="";
						switch(index){
							case 10:
								update="update schedule set one='"+name+"' where date='周四';";
								break;
							case 16:
								update="update schedule set two='"+name+"' where date='周四';";
								break;
							case 22:
								update="update schedule set three='"+name+"' where date='周四';";
								break;
							case 28:
								update="update schedule set four='"+name+"' where date='周四';";
								break;
							case 34:
								update="update schedule set five='"+name+"' where date='周四';";
								break;
							case 40:
								update="update schedule set six='"+name+"' where date='周四';";
								break;
							case 46:
								update="update schedule set seven='"+name+"' where date='周四';";
								break;
							case 52:
								update="update schedule set eight='"+name+"' where date='周四';";
								break;
						}
						db.execSQL(update);
						db.setTransactionSuccessful();
					}else{
						//insert
						String insert="";
						switch(index){
							case 10:
								insert="insert into schedule (one,date) values ('"+name+"','周四');";
								break;
							case 16:
								insert="insert into schedule (two,date) values ('"+name+"','周四');";
								break;
							case 22:
								insert="insert into schedule (three,date) values ('"+name+"','周四');";
								break;
							case 28:
								insert="insert into schedule (four,date) values ('"+name+"','周四');";
								break;
							case 34:
								insert="insert into schedule (five,date) values ('"+name+"','周四');";
								break;
							case 40:
								insert="insert into schedule (six,date) values ('"+name+"','周四');";
								break;
							case 46:
								insert="insert into schedule (seven,date) values ('"+name+"','周四');";
								break;
							case 52:
								insert="insert into schedule (eight,date) values ('"+name+"','周四');";
								break;
						}
						db.execSQL(insert);
						db.setTransactionSuccessful();
					}
				}catch(SQLiteException x){
					Log.i("COURSEACTIVITY",x.getMessage());
				}finally{
					db.endTransaction();
				}
			}
			if(index==11||index==17||index==23||index==29||index==35||index==41||index==47||index==53){
				//周五课程
				String sql="select * from schedule where date='周五';";
				Cursor cur=db.rawQuery(sql, null);
				db.beginTransaction();
				try{
					if(cur.moveToFirst()){
						//update
						String update="";
						switch(index){
							case 11:
								update="update schedule set one='"+name+"' where date='周五';";
								break;
							case 17:
								update="update schedule set two='"+name+"' where date='周五';";
								break;
							case 23:
								update="update schedule set three='"+name+"' where date='周五';";
								break;
							case 29:
								update="update schedule set four='"+name+"' where date='周五';";
								break;
							case 35:
								update="update schedule set five='"+name+"' where date='周五';";
								break;
							case 41:
								update="update schedule set six='"+name+"' where date='周五';";
								break;
							case 47:
								update="update schedule set seven='"+name+"' where date='周五';";
								break;
							case 53:
								update="update schedule set eight='"+name+"' where date='周五';";
								break;
						}
						db.execSQL(update);
						db.setTransactionSuccessful();
					}else{
						//insert
						String insert="";
						switch(index){
							case 11:
								insert="insert into schedule (one,date) values ('"+name+"','周五');";
								break;
							case 17:
								insert="insert into schedule (two,date) values ('"+name+"','周五');";
								break;
							case 23:
								insert="insert into schedule (three,date) values ('"+name+"','周五');";
								break;
							case 29:
								insert="insert into schedule (four,date) values ('"+name+"','周五');";
								break;
							case 35:
								insert="insert into schedule (five,date) values ('"+name+"','周五');";
								break;
							case 41:
								insert="insert into schedule (six,date) values ('"+name+"','周五');";
								break;
							case 47:
								insert="insert into schedule (seven,date) values ('"+name+"','周五');";
								break;
							case 53:
								insert="insert into schedule (eight,date) values ('"+name+"','周五');";
								break;
						}
						db.execSQL(insert);
						db.setTransactionSuccessful();
					}
				}catch(SQLiteException x){
					Log.i("COURSEACTIVITY",x.getMessage());
				}finally{
					db.endTransaction();
				}
			}
			db.close();
		}
	}
}