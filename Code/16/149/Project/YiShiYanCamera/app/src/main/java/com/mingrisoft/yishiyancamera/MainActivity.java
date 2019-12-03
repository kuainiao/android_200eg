package com.mingrisoft.yishiyancamera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText nameEt,codeEt,UIDEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameEt = (EditText) findViewById(R.id.et_name);
        codeEt = (EditText) findViewById(R.id.et_code);
        UIDEt = (EditText) findViewById(R.id.et_UID);
        findViewById(R.id.star_line).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nameEt.getText().toString().trim().equals(null)){
                    Toast.makeText(MainActivity.this, "用户不能为空", Toast.LENGTH_SHORT).show();
                }else if (UIDEt.getText().toString().trim().equals(null)){
                    Toast.makeText(MainActivity.this, "UID错误", Toast.LENGTH_SHORT).show();
                }else if (codeEt.getText().toString().trim().equals(null)){
                    Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }else {

                    Intent intent = new Intent(MainActivity.this,CameraActivity.class);
                    intent.putExtra("name",nameEt.getText().toString().trim());
                    intent.putExtra("UID",UIDEt.getText().toString().trim());
                    intent.putExtra("code",codeEt.getText().toString().trim());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
