package com.mingrisoft.smsforgeture;

import android.text.Editable;
import android.text.TextWatcher;


/**
 * 继承TextWatcher，用来监听文本框输入的监听
 * */
public class SmsWatcher implements TextWatcher {

    private TextChangingListener mChangingListener;

    public SmsWatcher(TextChangingListener changingListener) {
        mChangingListener = changingListener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mChangingListener.onChange(s);
    }

    /**
     * 定义个监听的接口
     * */
    public interface TextChangingListener {
        void onChange(Editable s);
    }
}
