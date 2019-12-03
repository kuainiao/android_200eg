package com.mingrisoft.enterandregister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by dllo on 16/3/1.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextP, editSMS, editTextCT;
    private Button button,SMSBtn;
    private TextView enterText;
    private ImageView returnImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_activity);
       init();
    }

    private void init() {
        editTextP = (EditText) findViewById(R.id.et_phone_num);
        editSMS = (EditText) findViewById(R.id.et_sms_code);
        editTextCT = (EditText) findViewById(R.id.et_password);
        button = (Button) findViewById(R.id.bn_immediateRegistration);
        button.setOnClickListener(this);
        enterText = (TextView) findViewById(R.id.tv_enter);
        enterText.setOnClickListener(this);
        returnImage = (ImageView) findViewById(R.id.iv_return);
        returnImage.setOnClickListener(this);
        SMSBtn = (Button) findViewById(R.id.bn_sms_code);
        SMSBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bn_immediateRegistration:
                register();
                break;
            case R.id.tv_enter:
                returnEnter();
                break;
            case R.id.iv_return:
                returnEnter();
                break;
            case R.id.bn_sms_code:
                final String username = editTextP.getText().toString().trim();
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
                    editTextP.requestFocus();
                }else {
                    Toast.makeText(this, "验证码获取成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void returnEnter() {
        Intent intent = new Intent(this,EnterActivity.class);
        startActivity(intent);
        finish();
    }

    public void register() {
        final String username = editTextP.getText().toString().trim();
        final String password = editSMS.getText().toString().trim();
        String confirm_password = editTextCT.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {  //当手机号没有输入时
            Toast.makeText(this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            editTextP.requestFocus();//使输入框失去焦点
            return;
        } else if (TextUtils.isEmpty(password)) {//当验证码没有输入时
            Toast.makeText(this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
            editSMS.requestFocus();//使输入框失去焦点
            return;
        } else if (TextUtils.isEmpty(confirm_password)) {//当注册密码没有输入时
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            editTextCT.requestFocus();//使输入框失去焦点
            return;
        }
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("正在注册……");
            pd.show();

            new Thread(new Runnable() {
                public void run() {
                    //注册的操作放在此处
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {

                    }
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                }
            }).start();

        }
    }
}
