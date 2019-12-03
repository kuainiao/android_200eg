package com.mingrisoft.snackBar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //实例化控件
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示Snackbar(类似Toast)
               Snackbar snackbar = Snackbar.make(view, "提示", Snackbar.LENGTH_LONG)
                        .setAction("知道了", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                snackbar.show();//显示SnackBar
            }
        });
    }
}
