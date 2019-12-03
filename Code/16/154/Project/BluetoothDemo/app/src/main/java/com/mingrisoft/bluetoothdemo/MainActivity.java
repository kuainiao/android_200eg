package com.mingrisoft.bluetoothdemo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class  MainActivity extends MPermissionsActivity implements AdapterView.OnItemClickListener {

    private DeviceAdapter adapter;
    private BluetoothReceiver receiver;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //动态判断权限
        requestPermission(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0x0001);

    }
    /**
     * 权限成功回调函数
     *
     * @param requestCode
     */
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        switch (requestCode) {
            case 0x0001:
                Init();
                break;
        }
    }

    public void Init() {
        //获取默认蓝牙适配器
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            Toast.makeText(this, "本设备没有蓝牙模块", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            ListView listView = (ListView) findViewById(R.id.list);
            //获取所有配对过的设备
            List<BluetoothDevice> list = new ArrayList<>(adapter.getBondedDevices());
            this.adapter = new DeviceAdapter(this, list);
            listView.setAdapter(this.adapter);
            if (!adapter.isEnabled()){
                //开启蓝牙
//                adapter.enable();
                //请求开启蓝牙
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 0);
            } else {
                startBluetooth();
            }
            listView.setOnItemClickListener(this);
        }
    }
    //开启蓝牙
    private void startBluetooth() {
        //创建蓝牙是配置
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        adapter.startDiscovery();
        //创建广播接收蓝牙消息
        receiver = new BluetoothReceiver(this.adapter);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //注册蓝牙广播
        registerReceiver(receiver, filter);
        //开启建立连接线程类
        new SocketThread(handler).start();
    }
    //销毁后调用
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            //销毁广播
            unregisterReceiver(receiver);
        }
    }
    //接收回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case RESULT_OK:
                startBluetooth();
                Toast.makeText(this, "开启成功", Toast.LENGTH_SHORT).show();
                break;
            case RESULT_CANCELED:
                Toast.makeText(this, "开启失败", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    //绑定单击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //获取蓝牙名称
        BluetoothDevice device = (BluetoothDevice) parent.getAdapter().getItem(position);
        //进入聊天页面
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("device", device);
        startActivity(intent);
    }
}
