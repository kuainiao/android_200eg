package com.mingrisoft.threaddownload;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class DownLoadManger {
	private String urlPath;
	public interface Progress{
		void get(int size, int all);
	}

	/**
	 * 接口回调用来获取下载进度
	 */
	private Progress progress;
	public void setProgessListener(Progress listener){
		this.progress = listener;
	}

	public DownLoadManger(String urlPath) {
		this.urlPath = urlPath;
	}

	public void downLoadFileTest() {
		HttpURLConnection conn = null;
		String filename = null;
		InputStream inputStream = null;
		RandomAccessFile accessFile = null;
		try {
			URL url = new URL(urlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);// 设置延迟时间5s
			conn.setRequestMethod("GET");// 网络请求方式为Get请求
			conn.connect();
			int code = conn.getResponseCode();//200
			System.out.println("返回码：" + code);
			int size = conn.getContentLength();
			System.out.println("filename"+size);
			Map<String, List<String>> map = conn.getHeaderFields();//获取响应头中的信息
			for (String str : map.keySet()) {
				if (str != null) {
					System.out.println("H3c : " + str + map.get(str));
				}
			}
			filename = conn.getHeaderField("Content-Disposition");
			if (filename == null || filename.length() < 1) {
				filename = url.getFile();
				filename = filename.substring(filename.lastIndexOf("/") + 1, filename.length());
			} else {
				filename = filename.substring(filename.indexOf("=") + 1, filename.length());
				if (filename.indexOf("\"") == 0) {
					filename = filename.replaceAll("\"", " ").trim();
				}
			}
			System.out.println("文件名字："+filename);
			File file = new File(Environment.getExternalStorageDirectory() , filename);
			int every = size%4==0?(size/4):(size/4+1);
			System.out.println("分段："+every);
			accessFile = new RandomAccessFile(file, "rwd");
			accessFile.setLength(size);
			accessFile.close();
			for(int i = 0;i < 4;i++){
				new ThreadManger(i, i*every,(i+1)*every-1,
						file.getAbsolutePath(),urlPath,
						progress == null?progress=null:progress,size).start();
			}
			Log.e("78475", "downLoadFileTest: " + "end" );
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (conn != null) {
					conn.disconnect();
				}
				if (inputStream != null) {
					inputStream.close();

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
