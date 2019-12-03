package com.mingrisoft.codedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class
MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = MainActivity.class.getName();
    private ImageView iv_showCode;
    private EditText et_phoneCode;
    private EditText et_phoneNum;
    //产生的验证码
    private String realCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //验证码输入框
        et_phoneCode = (EditText) findViewById(R.id.et_phoneCodes);
        //登录按钮
        Button but_toSetCode = (Button) findViewById(R.id.but_forgetpass_toSetCodes);
        //绑定点击事件
        but_toSetCode.setOnClickListener(this);
        //初始化显示验证码控件
        iv_showCode = (ImageView) findViewById(R.id.iv_showCode);
        //将验证码用图片的形式显示出来
        iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
        //获取验证码值
        realCode = Code.getInstance().getCode().toLowerCase();
        //验证码添加点击事件
        iv_showCode.setOnClickListener(this);
    }
    //点击事件
    @Override
    public void onClick(View v) {
        //判断view
        switch (v.getId()) {
            //验证码点击事件
            case R.id.iv_showCode:
                //设置新的验证码
                iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
                //获取验证码值
                realCode = Code.getInstance().getCode().toLowerCase();
                Log.v(TAG,"realCode"+realCode);
                break;
            //登录按钮点击事件
            case R.id.but_forgetpass_toSetCodes:
                //获取验证码值
                String phoneCode = et_phoneCode.getText().toString().toLowerCase();
                //验证码信息
                String msg = "生成的验证码："+realCode+"\n"+"输入的验证码:"+phoneCode;
                //判断验证码是否输入正确
                if (phoneCode.equals(realCode)) {
                    Toast.makeText(MainActivity.this, phoneCode + "验证码正确", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,"验证码错误:"+"\n"+msg,Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
