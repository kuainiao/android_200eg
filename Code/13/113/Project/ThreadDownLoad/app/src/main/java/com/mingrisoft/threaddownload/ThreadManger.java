package com.mingrisoft.threaddownload;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ThreadManger extends Thread {

    private int threadId;//线程ID
    private int startPosition;//下载开始位置
    private int endPosition;//下载结束位置
    private String fileName;//文件名称
    private String urlPath;//文件路径
    private DownLoadManger.Progress progress;
    private int all;

    public ThreadManger(int threadId, int startPosition, int endPosition, String fileName, String urlPath, DownLoadManger.Progress progress, int all) {
        super();
        this.threadId = threadId;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.fileName = fileName;
        this.urlPath = urlPath;
        this.progress = progress;
        this.all = all;

    }

    @Override
    public void run() {
        super.run();
        HttpURLConnection conn = null;
        InputStream is = null;
        RandomAccessFile accessFile = null;
        try {
            URL url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);//超时时间
            conn.setRequestMethod("GET");//get请求
            conn.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition);//断点下载的位置
            Log.w("Range", "s :" + startPosition + "-" + endPosition);
            conn.connect();//打开连接
            int code = conn.getResponseCode();//获取返回值，应该为206，注意这里不是200返回200是不能断点下载的
            System.out.println("线程ID" + threadId);
            System.out.println("返回码" + code);
            is = conn.getInputStream();//获取输入流
            byte[] buff = new byte[1024];//设置缓存
            int len;
            accessFile = new RandomAccessFile(fileName, "rwd");
            accessFile.seek(startPosition);//设置文件的写入位置
            while ((len = is.read(buff)) != -1) {
                accessFile.write(buff, 0, len);
                progress.get(len, all);//该回调接口用来获取进度
                Log.d("id", threadId+"");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }
                if (is != null) {
                    is.close();
                }
                if (accessFile != null) {
                    accessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
