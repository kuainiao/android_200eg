package com.mingrisoft.suspensionwindow;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends MPermissionsActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button start = (Button) findViewById(R.id.start_id);

        Button remove = (Button) findViewById(R.id.remove_id);

        start.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /**
                 * 开启服务
                 * */
                Intent intent = new Intent(MainActivity.this, MyService.class);
                startService(intent);
                finish();
            }
        });

        remove.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /**
                 * 关闭服务
                 * */
                Intent intent = new Intent(MainActivity.this, MyService.class);
                stopService(intent);

            }
        });

    }
}