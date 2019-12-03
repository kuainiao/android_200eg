package service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import accessbility.Accessbility;
import accessbility.WechatAccessbility;
import util.Config;

/**
 * 抢红包外挂服务
 */
public class GrabRedPacketService extends AccessibilityService {


    private static final Class[] ACCESSBILITY_JOBS= {
            WechatAccessbility.class,
    };

    private static GrabRedPacketService service;
    //界面信息的list
    private List<Accessbility> accessbilities;
    private HashMap<String, Accessbility> accessbilityHashMap;

    @Override
    public void onCreate() {
        super.onCreate();

        accessbilities = new ArrayList<>();
        accessbilityHashMap = new HashMap<>();

        //初始化辅助插件工作
        for(Class clazz : ACCESSBILITY_JOBS) {
            try {
                Object object = clazz.newInstance();
                if(object instanceof Accessbility) {
                    Accessbility job = (Accessbility) object;
                    job.onCreateAccessbility(this);
                    accessbilities.add(job);
                    accessbilityHashMap.put(job.getTargetPackageName(), job);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 销毁
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(accessbilityHashMap != null) {
            accessbilityHashMap.clear();
        }
        if(accessbilities != null && !accessbilities.isEmpty()) {
            for (Accessbility job : accessbilities) {
                job.onStopAccessbility();
            }
            accessbilities.clear();
        }

        service = null;
        accessbilities = null;
        accessbilityHashMap = null;
        //发送广播，已经断开辅助服务
        Intent intent = new Intent(Config.ACTION_GRAB_RED_PACKET_SERVICE_DISCONNECT);
        sendBroadcast(intent);
    }

    @Override
    public void onInterrupt() {
        Toast.makeText(this, "中断抢红包服务", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        service = this;
        //发送广播，已经连接上了
        Intent intent = new Intent(Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT);
        sendBroadcast(intent);
        Toast.makeText(this, "已连接抢红包服务", Toast.LENGTH_SHORT).show();
    }

    /**
     *窗口变化事件
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //获取当前界面通知中应用的包名，也就是微信红包的通知
        String pkn = String.valueOf(event.getPackageName());
        //判断当前界面信息是不是空的
        if(accessbilities != null && !accessbilities.isEmpty()) {
            //便利当前界面中的信息
            for (Accessbility job : accessbilities) {
                //判断包名是否相同
                if(pkn.equals(job.getTargetPackageName()) && job.isEnable()) {
                    //传递获取到的信息
                    job.onReceiveAccessbility(event);
                }
            }
        }
    }

    public Config getConfig() {
        return Config.getConfig(this);
    }

    /** 接收通知栏事件*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void handeNotificationPosted(NotificationBar notificationService) {
        if(notificationService == null) {
            return;
        }
        if(service == null || service.accessbilityHashMap == null) {
            return;
        }
        String pack = notificationService.getPackageName();
        Accessbility job = service.accessbilityHashMap.get(pack);
        if(job == null) {
            return;
        }
        job.onReleaseNotification(notificationService);
    }

    /**
     * 判断当前服务是否正在运行
     * */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isRunning() {
        if(service == null) {
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo info = service.getServiceInfo();
        if(info == null) {
            return false;
        }
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Iterator<AccessibilityServiceInfo> iterator = list.iterator();

        boolean isConnect = false;
        while (iterator.hasNext()) {
            AccessibilityServiceInfo i = iterator.next();
            if(i.getId().equals(info.getId())) {
                isConnect = true;
                break;
            }
        }
        if(!isConnect) {
            return false;
        }
        return true;
    }

    /** 快速读取通知栏服务是否启动*/
    public static boolean isNotificationServiceRunning() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return false;
        }
        //部份手机没有NotificationService服务
        try {
            return NotificationService.isRunning();
        } catch (Throwable t) {}
        return false;
    }


}
