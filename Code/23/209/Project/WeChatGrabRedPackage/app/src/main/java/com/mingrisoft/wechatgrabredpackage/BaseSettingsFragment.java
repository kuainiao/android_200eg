package com.mingrisoft.wechatgrabredpackage;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import util.Config;

/**
 *设置控件显示的共享名称
 */
public class BaseSettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(Config.SHARE_NAME);
    }
}
