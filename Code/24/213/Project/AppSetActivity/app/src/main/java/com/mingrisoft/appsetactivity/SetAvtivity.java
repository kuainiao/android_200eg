package com.mingrisoft.appsetactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;



/**
 * Created by dllo on 16/3/2.
 */
public class SetAvtivity extends AppCompatActivity implements View.OnClickListener {

    private SwitchButton switch_accept_news, switch_sound, switch_shock, switch_loundspeaker;
    private LinearLayout ll_sound, ll_shock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        switch_accept_news = (SwitchButton) findViewById(R.id.switch_accept_news);
        switch_sound = (SwitchButton) findViewById(R.id.switch_sound);
        switch_shock = (SwitchButton) findViewById(R.id.switch_shock);
        switch_loundspeaker = (SwitchButton) findViewById(R.id.switch_loundspeaker);
        ll_sound = (LinearLayout) findViewById(R.id.ll_sound);
        ll_shock = (LinearLayout) findViewById(R.id.ll_shock);
        switch_accept_news.setOnClickListener(this);
        switch_sound.setOnClickListener(this);
        switch_shock.setOnClickListener(this);
        switch_loundspeaker.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_accept_news:
                if (switch_accept_news.isSwitchOpen()) {
                    switch_accept_news.closeSwitch();
                    ll_sound.setVisibility(View.GONE);
                    ll_shock.setVisibility(View.GONE);
                } else {
                    switch_accept_news.openSwitch();
                    ll_sound.setVisibility(View.VISIBLE);
                    ll_shock.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.switch_sound:  //点击声音的开关按钮
                if (switch_sound.isSwitchOpen()) {  //判断开关是否已打开
                    switch_sound.closeSwitch();   // 如果打开执行关闭操作
                } else {
                    switch_sound.openSwitch();   //如果未打开执行打开操作
                }
                break;
            case R.id.switch_shock:
                if (switch_shock.isSwitchOpen()) {
                    switch_shock.closeSwitch();
                } else {
                    switch_shock.openSwitch();
                }
                break;
            case R.id.switch_loundspeaker:
                if (switch_loundspeaker.isSwitchOpen()) {
                    switch_loundspeaker.closeSwitch();
                } else {
                    switch_loundspeaker.openSwitch();
                }
                break;
        }
    }
}
