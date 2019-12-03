package com.mingrisoft.dzglthreeld;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class CityDBManager {
	private final int BUFFER_SIZE = 1024;
	//数据库名称
	public static final String DB_NAME = "city.s3db";
	//数据库位置
	public static final String PACKAGE_NAME = "com.mingrisoft.dzglthreeld";
	//数据库路径
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME;
	private SQLiteDatabase database;
	//上下文
	private Context context;
	//初始化文件类
	private File file = null;
   //声明方法
	public CityDBManager(Context context) {
		this.context = context;
	}

	public void openDatabase() {
		//打开数据库
		this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
	}
   //获取数据库
	public SQLiteDatabase getDatabase() {
		return this.database;
	}
    //开启数据库
	private SQLiteDatabase openDatabase(String dbfile) {
		try {
			file = new File(dbfile);
			if (!file.exists()) {
				//文件流
				InputStream is = context.getResources().openRawResource(
						R.raw.city);
				//创建输入流
				FileOutputStream fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					//写入信息
					fos.write(buffer, 0, count);
					fos.flush();
				}
				//关闭数据流
				fos.close();
				//关闭文件
				is.close();
			}
			//打开数据库文件
			database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
			return database;
		} catch (FileNotFoundException e) {
			Log.e("cc", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("cc", "IO exception");
			e.printStackTrace();
		} catch (Exception e) {
			Log.e("cc", "exception " + e.toString());
		}
		return null;
	}
	//关闭数据库
	public void closeDatabase() {
		if (this.database != null)
			this.database.close();
	}
}