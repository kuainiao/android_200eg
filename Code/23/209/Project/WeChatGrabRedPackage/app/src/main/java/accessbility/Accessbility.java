package accessbility;

import android.view.accessibility.AccessibilityEvent;

import service.GrabRedPacketService;
import service.NotificationBar;


/**
 * Accessbility接口
 */
public interface Accessbility {
    //获取目标包名称
    String getTargetPackageName();
    //初始化
    void onCreateAccessbility(GrabRedPacketService service);
    //接收Accessbility事件信息
    void onReceiveAccessbility(AccessibilityEvent event);
    //停止Accessbility
    void onStopAccessbility();
    //发布通知
    void onReleaseNotification(NotificationBar service);
    //是否启动
    boolean isEnable();
}
