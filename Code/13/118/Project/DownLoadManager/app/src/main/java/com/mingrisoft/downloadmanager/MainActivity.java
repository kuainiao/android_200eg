package com.mingrisoft.downloadmanager;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends MPermissionsActivity {

    private DownloadManager manager;
    private DownloadManager.Request requestApk;
    private long id;
    private boolean isFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void download(View view) {
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
    }

    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        if (requestCode == 1000) {
            downLoadApk("http://221.8.78.11/imtt.dd.qq.com/16891/A24AB287B4AB14D51C7DC64A91C21FD1.apk?mkey=584a21a7ceb0375a&f=4e1d&c=0&fsname=com.tencent.news_5.2.7_5207.apk&csr=4d5s&p=.apk");
        }
    }

    /**
     * 该方法是调用了系统的下载管理器
     */
    public void downLoadApk(String url) {
        if (isFinish) {
            Toast.makeText(this, "文件正在下载中", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri = Uri.parse(url);        //下载连接
        manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);  //得到系统的下载管理
        requestApk = new DownloadManager.Request(uri);  //得到连接请求对象
        requestApk.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);   //指定在什么网络下进行下载
        requestApk.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        requestApk.setDestinationInExternalPublicDir(this.getPackageName() +
                "/DownLoad", "sample.apk");  //指定下载文件的保存路径
        requestApk.setVisibleInDownloadsUi(true);  //设置显示下载界面
        requestApk.allowScanningByMediaScanner();  //表示允许MediaScanner扫描到这个文件，默认不允许。
        requestApk.setTitle("调用系统下载文件");    //设置下载中通知栏的提示消息
        requestApk.setDescription("下载腾讯新闻中");//设置设置下载中通知栏提示的介绍
        id = manager.enqueue(requestApk);
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        isFinish = true;
    }

    /**
     * 广播接收器
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                boolean finish = id == intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                String result = finish == true ? "下载完成" : "下载未完成";
                isFinish = false;
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
