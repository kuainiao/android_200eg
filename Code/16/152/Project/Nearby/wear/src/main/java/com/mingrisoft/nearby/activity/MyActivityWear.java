package com.mingrisoft.nearby.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;
import com.mingrisoft.nearby.R;
import com.mingrisoft.nearby.entity.Result;

import java.util.ArrayList;
import java.util.List;

public class MyActivityWear extends Activity implements DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks, View.OnClickListener {
    private GoogleApiClient mGoogleApiClient;//谷歌服务
    private BroadcastReceiver mResultReceiver;//广播接收器
    private Button one, two, three, four;//按钮
    private Dialog dialog;//等待弹窗

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wear);
        dialog = new Dialog(this,R.style.Theme_dialog);//创建弹窗
        //设置弹窗效果
        dialog.setContentView(View.inflate(this,R.layout.dialog_loding,null));
        //谷歌服务对象
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)//添加回调监听
                .addApi(Wearable.API)//添加Api
                .build();//创建对象
        mGoogleApiClient.connect();//开启连接
        Wearable.DataApi.addListener(mGoogleApiClient, this);//添加消息变化的监听
        //用来适配手表的屏幕
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        //控件需在这里进行设置
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                //初始化控件
                one = (Button) findViewById(R.id.one);
                two = (Button) findViewById(R.id.two);
                three = (Button) findViewById(R.id.three);
                four = (Button) findViewById(R.id.four);
                //事件按钮的点击事件监听
                one.setOnClickListener(MyActivityWear.this);
                two.setOnClickListener(MyActivityWear.this);
                three.setOnClickListener(MyActivityWear.this);
                four.setOnClickListener(MyActivityWear.this);
            }
        });
        //创建本地广播
        mResultReceiver = createBroadcastReceiver();
        //注册广播接收器
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mResultReceiver, new IntentFilter("wearable.localIntent"));
    }

    /**
     * 销毁资源
     */
    @Override
    protected void onDestroy() {
        //解除广播接收器的注册
        if (mResultReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mResultReceiver);
        }
        //移除消息改变的监听
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();//断开连接
        }
        super.onDestroy();
    }

    private BroadcastReceiver createBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ArrayList<Result> list = intent.getParcelableArrayListExtra("data");
                if (list != null) {
                    Result result;
                    for (int i = 0, length = list.size(); i < length; i++) {
                        result = list.get(i);
                        Log.w("数据", "" + i + "************************");
                        Log.w("地址", result.getAddress());
                        Log.w("城市", result.getCity());
                        Log.w("名称", result.getName());
                        Log.w("电话", result.getPhoneNum());
                    }
                    Intent start = new Intent(MyActivityWear.this,MainActivity.class);
                    start.putParcelableArrayListExtra("data",list);
                    startActivity(start);
                }else {
                    Toast.makeText(context.getApplicationContext(),"获取POI检索失败！",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        };
    }

    /**
     * 接收消息
     *
     * @param dataEventBuffer
     */
    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        Log.i("有数据", "onDataChanged: ");
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEventBuffer);
        for (DataEvent event : events) {
            final Uri uri = event.getDataItem().getUri();
            final String path = uri != null ? uri.getPath() : null;
            if ("/POI".equals(path)) {
                final DataMap map = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                ArrayList<DataMap> list = map.getDataMapArrayList("data");
                ArrayList<Result> data = new ArrayList<>();
                DataMap dataMap;
                Result result;
                for (int i = 0,length = list.size();i < length;i++){
                    dataMap = list.get(i);
                    result = new Result();
                    result.setAddress(dataMap.getString("address"));
                    result.setCity(dataMap.getString("city"));
                    result.setName(dataMap.getString("name"));
                    result.setPhoneNum(dataMap.getString("phoneNum"));
                    data.add(result);//添加数据
                }
                Intent localIntent = new Intent("wearable.localIntent");
                localIntent.putParcelableArrayListExtra("data", data);
                LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("手表", "正在连接");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("手表", "连接断开");
    }

    /**
     * 按钮的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        dialog.show();
        switch (v.getId()) {
            case R.id.one://美食
                putDataToPhone(getString(R.string.one));
                break;
            case R.id.two://饮品
                putDataToPhone(getString(R.string.two));
                break;
            case R.id.three://购物
                putDataToPhone(getString(R.string.three));
                break;
            case R.id.four://休闲
                putDataToPhone(getString(R.string.four));
                break;
        }
    }

    /**
     * 发送数据给手机
     *
     * @param key
     */
    private void putDataToPhone(String key) {
        //创建请求
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create("/KEY");
        DataMap dataMap = dataMapRequest.getDataMap();//获取消息的载体
        dataMap.putLong("time", System.currentTimeMillis());//用于区分数据
        dataMap.putString("keyword", key);//关键字
        Wearable.DataApi.//发送数据
                putDataItem(mGoogleApiClient, dataMapRequest.asPutDataRequest());
    }
}
