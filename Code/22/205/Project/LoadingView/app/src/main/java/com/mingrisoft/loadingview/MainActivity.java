package com.mingrisoft.loadingview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private MetaballView metaballView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        metaballView = (MetaballView) this.findViewById(R.id.meta_ball);
        metaballView.setPaintMode(1);
//        metaballView.setPaintMode(0);
    }

}
