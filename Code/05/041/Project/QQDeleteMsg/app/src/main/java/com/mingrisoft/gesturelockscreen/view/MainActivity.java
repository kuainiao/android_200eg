package com.mingrisoft.gesturelockscreen.view;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;


import com.mingrisoft.gesturelockscreen.R;



import java.util.ArrayList;

/**
 * Created by Root on 2016/6/23.
 */
public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<String> mData;
    private MyListAdapt mAdapter;
    public static Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list);

        mData = new ArrayList<String>();
        for (int i = 1; i < 6; i++) {
            mData.add( "数据"+i);
        }
        mAdapter = new MyListAdapt(this, mData);
        mListView.setAdapter(mAdapter);

    }
}
