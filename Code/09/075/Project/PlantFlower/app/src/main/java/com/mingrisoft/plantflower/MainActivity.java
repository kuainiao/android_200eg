package com.mingrisoft.plantflower;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Plant plant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        plant = (Plant) findViewById(R.id.plant);
    }

    /**
     * 点击事件
     * @param view
     */
    public void start(View view){
        if (!plant.getIsRun()){
            plant.plantAnimatorStart();//开启动画
        }else {
            Toast.makeText(this,"动画没有停止",Toast.LENGTH_SHORT).show();
        }
    }
}
