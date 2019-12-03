package accessbility;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.compat.BuildConfig;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;



import java.util.List;

import service.GrabRedPacketService;
import service.NotificationBar;
import util.AccessibilityUtil;
import util.Config;
import util.NotificationUtil;

/**
 * 微信Accessbility
 */
public class WechatAccessbility extends BaseAccessbility {


    /**
     * 微信的包名
     */
    public static final String WECHAT_PACKAGENAME = "com.tencent.mm";

    /**
     * 红包消息的关键字
     */
    private static final String HONGBAO_TEXT_KEY = "[微信红包]";

    private static final String BUTTON_CLASS_NAME = "android.widget.Button";


    /**
     * 不能再使用文字匹配的最小版本号
     */
    private static final int USE_ID_MIN_VERSION = 700;//设置比较版本7.0

    private static final int WINDOW_NONE = 0;
    private static final int WINDOW_LUCKYMONEY_RECEIVEUI = 1;
    private static final int WINDOW_LUCKYMONEY_DETAIL = 2;
    private static final int WINDOW_LAUNCHER = 3;
    private static final int WINDOW_OTHER = -1;

    private int mCurrentWindow = WINDOW_NONE;

    private boolean isReceivingHongbao;     //是否收到红包
    private PackageInfo mWechatPackageInfo = null;
    private Handler mHandler = null;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新安装包信息
            updatePackageInfo();
        }
    };

    /**
     *Accessbility初始化
     */
    @Override
    public void onCreateAccessbility(GrabRedPacketService service) {
        super.onCreateAccessbility(service);

        updatePackageInfo();        //更新微信包信息

        IntentFilter filter = new IntentFilter();
        filter.addDataScheme("package");
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REPLACED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");

        getContext().registerReceiver(broadcastReceiver, filter);
    }

    /**
     * 停止Accessbility
     */
    @Override
    public void onStopAccessbility() {
        try {
            getContext().unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
        }
    }

    /**
     *发布通知
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onReleaseNotification(NotificationBar sbn) {
        Notification nf = sbn.getNotification();
        String text = String.valueOf(sbn.getNotification().tickerText);
        notificationEvent(text, nf);
    }

    /**
     *是否启动了微信抢红包
     */
    @Override
    public boolean isEnable() {
        return getConfig().isEnableWechat();
    }

    @Override
    public String getTargetPackageName() {
        return WECHAT_PACKAGENAME;
    }

    /**
     *接收Accessbility事件信息
     */
    @Override
    public void onReceiveAccessbility(AccessibilityEvent event) {
        //获取用户界面中发生事件的类型
        final int eventType = event.getEventType();
        //通知栏事件
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            //获取微信包中的数据
            Parcelable data = event.getParcelableData();
            //判断数据是否为空
            if (data == null || !(data instanceof Notification)) {
                return;
            }
            //开启快速模式，不处理
            if (GrabRedPacketService.isNotificationServiceRunning() && getConfig().isEnableNotificationService()) {
                return;
            }
            //获取微信红包通知中的文字信息
            List<CharSequence> texts = event.getText();
            //如果信息不为空
            if (!texts.isEmpty()) {
                String text = String.valueOf(texts.get(0));
                //设置通知事件
                notificationEvent(text, (Notification) data);

            }
        } else if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            openHongBao(event);     //打开红包方法
        } else if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            if (mCurrentWindow != WINDOW_LAUNCHER) { //不在聊天界面或聊天列表，不处理
                return;
            }
        }
    }


    /**
     * 通知栏事件
     */
    private void notificationEvent(String ticker, Notification nf) {
        String text = ticker;
        int index = text.indexOf(":");
        if (index != -1) {
            text = text.substring(index + 1);
        }
        text = text.trim();
        if (text.contains(HONGBAO_TEXT_KEY)) { //红包消息
            openHongBaoNotification(nf);     //打开红包通知
        }
    }

    /**
     * 打开通知栏消息
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openHongBaoNotification(Notification notification) {
        System.out.println("打开消息通知！");
        isReceivingHongbao = true;
        //以下是精华，将微信的通知栏消息打开
        PendingIntent pendingIntent = notification.contentIntent;
        boolean lock = NotificationUtil.isLockScreen(getContext());

        if (!lock) {
            NotificationUtil.send(pendingIntent);
        } else {
            //显示通知
            NotificationUtil.showNotify(getContext(), String.valueOf(notification.tickerText), pendingIntent);
        }
        //播放声音与振动
        NotificationUtil.playEffect(getContext(), getConfig());
    }

    /**
     *打开红包
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openHongBao(AccessibilityEvent event) {
      System.out.println("openHongBao");
        if ("com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f".equals(event.getClassName())) {
            mCurrentWindow = WINDOW_LUCKYMONEY_RECEIVEUI;
            //点中了红包，下一步就是去拆红包
            handleLuckyMoneyReceive();
        } else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI".equals(event.getClassName())) {
            mCurrentWindow = WINDOW_LUCKYMONEY_DETAIL;
            //关闭拆完红包对话框，防止无法进入微信
            AccessibilityUtil.returnBack(getService());
            //拆完红包后返回主界面，以便收到下一次的红包通知
            AccessibilityUtil.returnHome(getService());
        } else if ("com.tencent.mm.ui.LauncherUI".equals(event.getClassName())) {
            mCurrentWindow = WINDOW_LAUNCHER;
            //在聊天界面,去领取红包
            handleChatListHongBao();
        } else {
            mCurrentWindow = WINDOW_OTHER;
        }
    }

    /**
     * 点击聊天里的红包后，显示的界面
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void handleLuckyMoneyReceive() {
        //获取当前窗口的信息
        AccessibilityNodeInfo nodeInfo = getService().getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }

        AccessibilityNodeInfo targetNode = null;
        //获取微信打开红包后的事件
        int event = getConfig().getWechatAfterOpenHongBaoEvent();
        int wechatVersion = getWechatVersion();            //获取微信版本
        if (event == Config.DISMANTLE_RED_PACKET) {    //拆红包
            if (wechatVersion < USE_ID_MIN_VERSION) {   //当前微信版本小于7.0
                //使用文字匹配
                targetNode = AccessibilityUtil.findNodeInfosByText(nodeInfo, "拆红包");
            } else {
                //设置拆红包按钮id
                String buttonId = "com.tencent.mm:id/b43";
                if (wechatVersion == 700) {
                    buttonId = "com.tencent.mm:id/b2c";
                }
                if (buttonId != null) {
                    //通过id查找界面信息与按钮信息
                    targetNode = AccessibilityUtil.findNodeInfosById(nodeInfo, buttonId);
                }

                if (targetNode == null) { //通过组件查找
                    targetNode = AccessibilityUtil.findNodeInfosByClassName(nodeInfo, BUTTON_CLASS_NAME);
                }
            }
        }
        //如果拆红包对话框存在 就单击拆开红包
        if (targetNode != null) {
            final AccessibilityNodeInfo n = targetNode;
            AccessibilityUtil.performClick(n);
            System.out.println("拆红包");
        }
    }

    /**
     * 领取聊天里的红包
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void handleChatListHongBao() {
        AccessibilityNodeInfo nodeInfo = getService().getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }

        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("领取红包");

        if (list != null && list.isEmpty()) {
            // 从消息列表查找红包
            AccessibilityNodeInfo node = AccessibilityUtil.findNodeInfosByText(nodeInfo, "[微信红包]");
            if (node != null) {
                if (BuildConfig.DEBUG) {
                    Log.i("-->微信红包:",""+ node);
                }
                isReceivingHongbao = true;
                //单击微信红包
                AccessibilityUtil.performClick(nodeInfo);
            }
        } else if (list != null) {
            if (isReceivingHongbao) {
                //最新的红包领起
                AccessibilityNodeInfo node = list.get(list.size() - 1);
                //单击最新的微信红包
                AccessibilityUtil.performClick(node);
                isReceivingHongbao = false;
                System.out.println("领取聊天里的红包");
            }
        }
    }


    /**
     * 获取微信的版本
     */
    private int getWechatVersion() {
        if (mWechatPackageInfo == null) {
            return 0;
        }
        return mWechatPackageInfo.versionCode;
    }

    /**
     * 更新微信包信息
     */
    private void updatePackageInfo() {
        try {
            mWechatPackageInfo = getContext().getPackageManager().getPackageInfo(WECHAT_PACKAGENAME, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
