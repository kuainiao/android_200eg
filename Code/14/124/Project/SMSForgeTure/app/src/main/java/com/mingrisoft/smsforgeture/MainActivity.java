package com.mingrisoft.smsforgeture;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.et_phone)
    TextInputEditText editPhone;
    @BindView(R.id.et_time)
    TextInputEditText editTime;
    @BindView(R.id.et_context)
    TextInputEditText editContext;
    @BindView(R.id.insert_sms)
    Button sendSmsBtn;
    @BindView(R.id.til_time)
    TextInputLayout editTimeLayout;
    @BindView(R.id.til_content)
    TextInputLayout editContentLayout;
    @BindView(R.id.btn)
    Button setBtn;
    @BindView(R.id.tv_hint)
    TextView editHint;

    private String systemSms;
    private SharedPreferences mIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        editTimeLayout.setCounterEnabled(true);   //获取字符的个数
        editTimeLayout.setError("请输入正确的时间格式");
        editContentLayout.setCounterEnabled(true);//获取字符的个数
        editContentLayout.setError("请输入确保短信内容在140字符之内");

        initListener();


        if (Build.VERSION.SDK_INT >= 20) {

            systemSms = getSystemDefaultSms();
            mIndex = getSharedPreferences("index", MODE_PRIVATE);
            int count = mIndex.getInt("count", 0);

            if (count == 0) {
                onclickSetSms(null);
                mIndex.edit().putString("sms", systemSms);
            } else {
                if (getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(this))) {
                    setBtn.setBackgroundResource(R.drawable.btn_bg);
                    setBtn.setText("还原默认短信程序");
                    System.out.println(Telephony.Sms.getDefaultSmsPackage(this));
                }
            }

            mIndex.edit().putInt("count", ++count).commit();
            System.out.println(mIndex.getInt("count", 0));
        } else {
            setBtn.setVisibility(View.GONE);
            editHint.setText("请点击插入短信");
        }

    }


    /**
     * 监听事件是否正确
     */
    private void initListener() {
        editTime.addTextChangedListener(new SmsWatcher(new SmsWatcher.TextChangingListener() {
            @Override
            public void onChange(Editable s) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = s.toString();
                try {
                    long timeStart = sdf.parse(time).getTime();
                    editTimeLayout.setErrorEnabled(false);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }));

        editContext.addTextChangedListener(new SmsWatcher(new SmsWatcher.TextChangingListener() {
            @Override
            public void onChange(Editable s) {
                if (s.toString().length() < 140) {
                    editContentLayout.setErrorEnabled(false);
                }
            }
        }));


    }

    public void insertSms(View view) {

        if (Build.VERSION.SDK_INT >= 20) {
            if (!getPackageName().equals(getSystemDefaultSms())) {
                setDefaultSms(getPackageName());
            }
        }


        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = editTime.getText().toString();

            ContentValues values = new ContentValues();
            long timeStart = sdf.parse(time).getTime();
            values.put("date", new Long(timeStart));

            values.put("address", editPhone.getText().toString());
            values.put("body", editContext.getText().toString());
            values.put("type", "2");
            values.put("read", "1");//"1"means has read ,1表示已读

            System.out.println(editPhone.getText().toString() + "----------------");

            if (editPhone.getText().toString().isEmpty()) {
                Toast.makeText(MainActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                throw new Exception("error 内容是空");
            }
            if (editPhone.getText().toString().isEmpty()) {
                Toast.makeText(MainActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                throw new Exception("error 内容是空");
            }
            getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
            Toast.makeText(MainActivity.this, "短信插入成功，请在收信箱中查看", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            Toast.makeText(MainActivity.this, "时间输入异常，请重新尝试", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getSystemDefaultSms() {
        return Telephony.Sms.getDefaultSmsPackage(this);
    }

    /**
     * 设置默认的短信
     *
     * @param packageName
     */
    public void setDefaultSms(String packageName) {

        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName);
        startActivity(intent);

    }


    public void onclickSetSms(View view) {
        if (!getSystemDefaultSms().equals(getPackageName())) {
            setDefaultSms(getPackageName());
            setBtn.setBackgroundResource(R.drawable.btn_bg);
            setBtn.setText("还原默认短信程序");
        } else {
            setDefaultSms(mIndex.getString("sms", "com.android.messaging"));
            setBtn.setBackgroundResource(R.drawable.btn_error);
            setBtn.setText("设置成默认短信程序");
        }
    }






}
