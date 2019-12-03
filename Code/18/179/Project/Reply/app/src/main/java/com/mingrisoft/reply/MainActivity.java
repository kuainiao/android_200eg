package com.mingrisoft.reply;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout email,code;
    /**
     * 初始化操作
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (TextInputLayout) findViewById(R.id.email);
        code = (TextInputLayout) findViewById(R.id.code);
    }

    /**
     * 点击事件模拟提交操作
     * @param view
     */
    public void login(View view){
        if ("".equals(email.getEditText().getText().toString())){
            email.setError("邮箱不能为空");
        }else if ("".equals(code.getEditText().getText().toString())){
            code.setError("验证码不能为空");
        }else {
            code.setErrorEnabled(false);
            email.setErrorEnabled(false);
            Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
        }
    }
}
