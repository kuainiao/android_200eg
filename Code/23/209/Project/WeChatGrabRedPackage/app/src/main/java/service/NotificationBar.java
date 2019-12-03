package service;

import android.app.Notification;

/**
 * 通知栏接口
 */
public interface NotificationBar {
    //获取通知中的包名
    String getPackageName();
    //获取通知
    Notification getNotification();
}
