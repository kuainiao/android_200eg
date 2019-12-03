package com.mingrisoft.bluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private BluetoothDevice device;
    private ChatClient client;
    private EditText edit;
    private  ArrayAdapter<String> adapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                default:
                    adapter.add(msg.obj.toString());
                    Toast.makeText(getApplicationContext(), "有新消息", Toast.LENGTH_SHORT).show();
                    VibratorUtil.Vibrate(ChatActivity.this, 1000);   //震动1s
                    break;
            }
        }
    };
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        device = getIntent().getParcelableExtra("device");
        if (TextUtils.isEmpty(device.getName())) {
            setTitle("没有名字");
        } else {
            setTitle(device.getName());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        client = SocketThread.getClient(device);
        if (client == null){
            Toast.makeText(this, "此设备不支持聊天", Toast.LENGTH_SHORT).show();
            finish();
        }
        edit = ((EditText) findViewById(R.id.chat_edit));
        findViewById(R.id.chat_send).setOnClickListener(this);

        ListView listView = (ListView) findViewById(R.id.chat_list);
        listView.setAdapter(adapter);
        client.setListener(mListener);
        name = BluetoothAdapter.getDefaultAdapter().getName();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
    //回调接口
    public interface GetMase {
        void getMase(Message p);
    }
    //回调方法
    private GetMase mListener = new GetMase() {
        public void getMase(Message message) {
            handler.sendMessage(message);
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chat_send:
                String s = edit.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    client.send(s);
                    adapter.add(name + ":" + s);
                    edit.setText("");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.unregister(handler);
    }
}
