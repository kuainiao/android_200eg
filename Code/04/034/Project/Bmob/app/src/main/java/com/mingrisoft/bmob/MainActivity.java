package com.mingrisoft.bmob;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.internal.http.CacheStrategy;

public class MainActivity extends AppCompatActivity {
    private Contact obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "1c381e702f55261dd3fb12f61268e99a");
        obj = new Contact();
        findViewById(R.id.send_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhoneContacts();
            }
        });
        findViewById(R.id.get_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new  TaskThread().start();
            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                {
                    System.out.println("-->回到主线程刷新ui任务");
                    Toast.makeText(MainActivity.this, "联系人同步完成", Toast.LENGTH_SHORT).show();

                }
                break;

                default:
                    break;
            }
        };
    };

    class TaskThread extends Thread {
        public void run() {
            System.out.println("-->做一些耗时的任务");
            getData();
            handler.sendEmptyMessage(0);
        };
    };

    /**
     * 根据时间从后台获取数据
     * */
    private void getData() {
        BmobQuery<Contact> query = new BmobQuery<Contact>();
        List<BmobQuery<Contact>> and = new ArrayList<BmobQuery<Contact>>();
        //大于00：00：00
        BmobQuery<Contact> q1 = new BmobQuery<Contact>();
        String start = "2017-01-01 13:47:42";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q1.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(date));
        and.add(q1);
        query.and(and);
        query.findObjects(new FindListener<Contact>() {
            @Override
            public void done(List<Contact> list, BmobException e) {
                for (int i = 0; i < list.size(); i++) {
                    testAddContacts(list.get(i).getName(), list.get(i).getNum());
                }
            }

        });
    }

    /**
     * 向后台发送联系人数据
     */
    public void upData(String name, String number) {
        obj.setName(name);
        obj.setNum(number);
        obj.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                Log.e("------", s);
            }
        });
    }


    /**
     * 从手机中获取联系人
     */
    private void getPhoneContacts() {
        List<Contact> list = new ArrayList();
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
                    String contactName = phoneCursor
                            .getString(0);
                    //创建实体类解析联系人
                    Contact mContact = new Contact(contactName, phoneNumber);
                    list.add(mContact);  //添加到集合里
                }
                phoneCursor.close();  //关流
                setData(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData(List<Contact> list) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                upData(list.get(i).getName(), list.get(i).getNum());
            }
            Toast.makeText(this, "联系人同步完成", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "手机中暂无联系人", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 添加联系人
     * 数据一个表一个表的添加，每次都调用insert方法
     */
    public void testAddContacts(String name, String num) {
        /**
         * 往 raw_contacts 中添加数据，并获取添加的id号
         **/
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = this.getContentResolver();
        ContentValues values = new ContentValues();
        long contactId = ContentUris.parseId(resolver.insert(uri, values));
        /* 往 data 中添加数据（要根据前面获取的id号） */
        // 添加姓名
        uri = Uri.parse("content://com.android.contacts/data");
        values.put("raw_contact_id", contactId);
        values.put("mimetype", "vnd.android.cursor.item/name");
        values.put("data2", name);
        resolver.insert(uri, values);
        // 添加电话
        values.clear();
        values.put("raw_contact_id", contactId);
        values.put("mimetype", "vnd.android.cursor.item/phone_v2");
        values.put("data2", "2");
        values.put("data1", num);
        resolver.insert(uri, values);

    }

}

