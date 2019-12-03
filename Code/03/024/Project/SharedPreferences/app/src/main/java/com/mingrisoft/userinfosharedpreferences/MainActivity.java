package com.mingrisoft.userinfosharedpreferences;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText et_password;//声明输入密码控件
    private EditText et_username;//声明输入用户名控件
    private String username, password;//声明用户名密码
    SharedPreferences mySharedPreferences;//声明信息保存类
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);;
        setContentView(R.layout.activity_main);
        //初始化输入用户名，密码控件
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
    }
    public void onBack(View view){
        Toast.makeText(this, "返回", Toast.LENGTH_SHORT).show();
    }
    public void onRegister(View view){
        Toast.makeText(this, "注册", Toast.LENGTH_SHORT).show();
    }
    public void onFindPwd(View view){
        Toast.makeText(this, "忘记密码", Toast.LENGTH_SHORT).show();
    }
    public void onLogin(View view){
        //获取控件输入的内容
        username = et_username.getText().toString().trim();
        password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "用户名或密码不可为空", Toast.LENGTH_SHORT).show();
        }
        else {
            //实例化SharedPreferences对象
            mySharedPreferences= getSharedPreferences("myuserinfo",
                    MainActivity.MODE_PRIVATE);
            //实例化SharedPreferences.Editor对象（第二步）
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            //用putString的方法保存数据
            editor.putString("username", username);
            editor.putString("pwd", password);
            //提交当前数据
            editor.commit();
            Toast.makeText(this, "信息保存成功！", Toast.LENGTH_SHORT).show();
        }
    }
    public void onShow(View v) {
        //实例化SharedPreferences对象
        mySharedPreferences = getSharedPreferences("myuserinfo",
                MainActivity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        String username = mySharedPreferences.getString("username", "没有保存的数据");
        String pwd = mySharedPreferences.getString("pwd", "没有保存的数据");
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this);
        builder.setTitle("显示保存的用户信息");
        builder.setMessage("用户名："+username+"\n"+"密码："+pwd);
        builder.setCancelable(false);
        builder.setPositiveButton("知道了！", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
}
