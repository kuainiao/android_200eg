package com.mingrisoft.wechatgrabredpackage;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import service.GrabRedPacketService;
import util.Config;


/**
 * 抢红包主界面
 */
public class MainActivity extends BaseSettingsActivity {

    private Dialog tipsDialog;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationManage.activityStartMain(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_GRAB_RED_PACKETSERVICE_CONNECT);
        filter.addAction(Config.ACTION_GRAB_RED_PACKET_SERVICE_DISCONNECT);
        filter.addAction(Config.ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT);
        filter.addAction(Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT);
        registerReceiver(qhbConnectReceiver, filter);
    }

    @Override
    protected boolean isShowBack() {
        return false;
    }

    @Override
    public Fragment getSettingsFragment() {
        mainFragment = new MainFragment();
        return mainFragment;
    }

    private BroadcastReceiver qhbConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(isFinishing()) {
                return;
            }
            String action = intent.getAction();
            Log.d("MainActivity", "receive-->" + action);
            if(Config.ACTION_GRAB_RED_PACKETSERVICE_CONNECT.equals(action)) {
                if (tipsDialog != null) {
                    tipsDialog.dismiss();
                }
            } else if(Config.ACTION_GRAB_RED_PACKET_SERVICE_DISCONNECT.equals(action)) {
                showOpenAccessibilityServiceDialog();
            } else if(Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT.equals(action)) {
                if(mainFragment != null) {
                    mainFragment.updateNotifyPreference();
                }
            } else if(Config.ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT.equals(action)) {
                if(mainFragment != null) {
                    mainFragment.updateNotifyPreference();
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(GrabRedPacketService.isRunning()) {
            if(tipsDialog != null) {
                tipsDialog.dismiss();
            }
        } else {
            showOpenAccessibilityServiceDialog();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(qhbConnectReceiver);
        } catch (Exception e) {}
        tipsDialog = null;
    }

    /** 显示未开启辅助服务的对话框*/
    private void showOpenAccessibilityServiceDialog() {
        if(tipsDialog != null && tipsDialog.isShowing()) {
            return;
        }
        View view = getLayoutInflater().inflate(R.layout.tips_dialog_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccessibilityServiceSettings();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setPositiveButton(R.string.open_service_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openAccessibilityServiceSettings();
            }
        });
        tipsDialog = builder.show();
    }

    /** 打开辅助服务的设置*/
    private void openAccessibilityServiceSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, R.string.tips, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 打开通知栏设置*/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private void openNotificationServiceSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, R.string.open_notify_service, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class MainFragment extends BaseSettingsFragment {
        //通知开关
        private SwitchPreference notificationSwitch;
        //通知更改
        private boolean notificationChange = true;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.main);

            //微信红包开关
            Preference wechatPref = findPreference(Config.IS_START_WECHAT);
            wechatPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if((Boolean) newValue && !GrabRedPacketService.isRunning()) {
                        ((MainActivity)getActivity()).showOpenAccessibilityServiceDialog();
                    }
                    return true;
                }
            });
            //获取快速监听通知栏控件
            notificationSwitch = (SwitchPreference) findPreference("NOTIFICATION_SERVICE_SWITCH");
            notificationSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        Toast.makeText(getActivity(), "该功能只支持安卓4.3以上的系统", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    if(!notificationChange) {
                        notificationChange = true;
                        return true;
                    }

                    boolean enalbe = (boolean) newValue;

                    Config.getConfig(getActivity()).setNotificationServiceEnable(enalbe);

                    if(enalbe && !GrabRedPacketService.isNotificationServiceRunning()) {
                        ((MainActivity)getActivity()).openNotificationServiceSettings();
                        return false;
                    }
                    ApplicationManage.eventStatistics(getActivity(), "notify_service", String.valueOf(newValue));
                    return true;
                }
            });



        }

        /** 更新快速读取通知的设置*/
        public void updateNotifyPreference() {
            if(notificationSwitch == null) {
                return;
            }
            boolean running = GrabRedPacketService.isNotificationServiceRunning();
            boolean enable = Config.getConfig(getActivity()).isEnableNotificationService();
            if( enable && running && !notificationSwitch.isChecked()) {
                ApplicationManage.eventStatistics(getActivity(), "notify_service", String.valueOf(true));
                notificationChange = false;
                notificationSwitch.setChecked(true);
            } else if((!enable || !running) && notificationSwitch.isChecked()) {
                notificationChange = false;
                notificationSwitch.setChecked(false);
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            updateNotifyPreference();
        }
    }
}
