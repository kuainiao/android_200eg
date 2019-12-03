package com.wedding.smsallsend;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MPermissionsActivity implements View.OnClickListener {

    private EditText smsEdit;
    private Button smsBtn;
    List<PersionEntity> listPersion;
    private LisAdapter adapter;
    private ListView listView;
    int len;
    List list;
    TypedArray ar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        requestPermission(new String[]{Manifest.permission.SEND_SMS}, 1000);

    }

    private void init() {
        listPersion = new ArrayList();
        smsEdit = (EditText) findViewById(R.id.input_sms);
        smsBtn = (Button) findViewById(R.id.send_sms);
        smsBtn.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.sms_list);
        adapter = new LisAdapter(this);
        listView.setAdapter(adapter);
        ar = getResources().obtainTypedArray(R.array.listTitle); //获取数据数组
        len = ar.length();              //获取数组的长度
        list = new ArrayList<>();     //初始化list
        for (int i = 0; i < len; i++)       //for循环添加数据
            list.add(i, ar.getString(i));     //向ListView中添加数据
        adapter.addData(list);              //将数据传给adapter
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_sms:  //点击发送按钮
                if (TextUtils.isEmpty(smsEdit.getText().toString())) { //如果内容为空，则弹出提示
                    Toast.makeText(this, "短信内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    getAllPeople();//如果不是空，获取所有联系人
                }
                break;
        }
    }

    /**
     * 获取全部联系人
     */
    private void getAllPeople() {
        /**
         * 利用游标获取手机通讯录里面的人
         * */
        final String[] PHONES_PROJECTION = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        ContentResolver resolver = getContentResolver();
        try {
            // 获取手机联系人
            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    PHONES_PROJECTION, null, null, null);
            if (phoneCursor != null) {

                while (phoneCursor.moveToNext()) {

                    // 得到手机号码
                    String phoneNumber = phoneCursor
                            .getString(1);
                    // 当手机号码为空的或者为空字段 跳过当前循环
                    if (TextUtils.isEmpty(phoneNumber))
                        continue;
                    // 得到联系人名称
                    String contactName = phoneCursor.getString(0);
                    //用PersionEntity实体类获取联系人姓名与电话
                    PersionEntity mContact = new PersionEntity(contactName, phoneNumber);
                    listPersion.add(mContact);  //添加到list中
                }
                phoneCursor.close(); //关流
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * for循环发送短信
         * */
        String strings[] = new String[listPersion.size()];
        if (listPersion != null && listPersion.size() > 0) {  //判断手机中是否有联系人
            for (int i = 0, j = listPersion.size(); i < j; i++) {    //for循环发送短信
                strings[i] = listPersion.get(i).getNum();   //获取手机号码
                sendSMS(listPersion.get(i).getNum(), smsEdit.getText().toString());  //给该号码发短信
            }
            Toast.makeText(this, "短信全部发送完毕", Toast.LENGTH_SHORT).show();  //弹出提示
        } else {
            Toast.makeText(this, "手机联系人为空", Toast.LENGTH_SHORT).show();     //弹出提示
        }

    }

    /**
     * 发送短信
     */
    private void sendSMS(String phone, String message) {
        PendingIntent pi = PendingIntent.getActivity(this, 0, null, 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone, null, message, pi, null);

    }

    /**
     * ListView内部的适配器
     * */
    class LisAdapter extends BaseAdapter{
        List<String> items;
        Context context;


        public LisAdapter(Context context) {
            this.context = context;
        }

        public void addData(List<String> items) {
            this.items = items;
            notifyDataSetChanged();
        }



        @Override
        public int getCount() {
            if (items != null &&items.size()>0){
                return items.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (items != null &&items.size()>0){
                return items.get(position);
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.item, null);
            TextView tv = (TextView) view.findViewById(R.id.iemt_tv);
            tv.setText(items.get(position));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    smsEdit.setText(items.get(position));
//                Toast.makeText(context, items.get(position), Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }
    }
}
