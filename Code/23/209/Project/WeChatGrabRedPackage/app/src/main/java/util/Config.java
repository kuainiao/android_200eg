package util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 配置类
 */
public class Config {
    //设置intent动作抢红包服务的连接与断开
    public static final String ACTION_GRAB_RED_PACKET_SERVICE_DISCONNECT = "ACCESSBILITY_DISCONNECT";
    public static final String ACTION_GRAB_RED_PACKETSERVICE_CONNECT = "ACCESSBILITY_CONNECT";
    //设置通知服务的连接与断开
    public static final String ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT = "NOTIFY_LISTENER_DISCONNECT";
    public static final String ACTION_NOTIFY_LISTENER_SERVICE_CONNECT = "NOTIFY_LISTENER_CONNECT";
    //共享名称
    public static final String SHARE_NAME = "config";
    //是否启动微信抢红包
    public static final String IS_START_WECHAT = "IS_START_WECHAT";
    //打开红包后的事件
    public static final String OPEN_GRAB_RED_PACKET_EVENT = "OPEN_GRAB_RED_PACKET_EVENT";
    //是否启动通知栏服务
    public static final String IS_NOTIFICATION_SERVICE = "IS_NOTIFICATION_SERVICE";
    //是否开启通知声音
    public static final String IS_OPEN_SOUND = "IS_OPEN_SOUND";
    //是否开启震动
    public static final String IS_OPEN_VIBRATE = "IS_OPEN_VIBRATE";
    //是否开启夜间模式
    public static final String IS_OPEN_NIGHT = "IS_OPEN_NIGHT";

    public static final int DISMANTLE_RED_PACKET = 0;//拆红包


    private static Config current;      //现在的配置

    public static synchronized Config getConfig(Context context) {
        if(current == null) {
            current = new Config(context.getApplicationContext());
        }
        return current;
    }

    private SharedPreferences preferences;
    private Context lContext;

    private Config(Context context) {
        lContext = context;
        preferences = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
    }

    /** 是否启动微信抢红包*/
    public boolean isEnableWechat() {
        return preferences.getBoolean(IS_START_WECHAT, true) && Config.isEnableWechat(lContext);
    }

    /** 微信打开红包后的事件*/
    public int getWechatAfterOpenHongBaoEvent() {
        int defaultValue = 0;
        String result =  preferences.getString(OPEN_GRAB_RED_PACKET_EVENT, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(result);
        } catch (Exception e) {}
        return defaultValue;
    }


    /** 是否启动通知栏模式*/
    public boolean isEnableNotificationService() {
        return preferences.getBoolean(IS_NOTIFICATION_SERVICE, false);
    }

    public void setNotificationServiceEnable(boolean enable) {
        preferences.edit().putBoolean(IS_NOTIFICATION_SERVICE, enable).apply();
    }

    /** 是否开启声音*/
    public boolean isNotifySound() {
        return preferences.getBoolean(IS_OPEN_SOUND, true);
    }

    /** 是否开启震动*/
    public boolean isNotifyVibrate() {
        return preferences.getBoolean(IS_OPEN_VIBRATE, true);
    }

    /** 是否开启夜间免打扰模式*/
    public boolean isNotifyNight() {
        return preferences.getBoolean(IS_OPEN_NIGHT, false);
    }

    /**
     *是否开启
     */
    public static boolean isEnableWechat(Context context) {
        return true;
    }


}
