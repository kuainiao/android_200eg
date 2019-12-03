package utils;

/**
 * Created by Administrator on 2017/1/13.
 */

import android.graphics.drawable.Drawable;

/**
 * 类说明：应用信息
 */
public class AppInfo {

    public String packageName;     // 包名
    public String appName;         // APK名称
    public String appSize;         // 大小
    public String appVersion;      // 版本名
    public int appVersion_code;    // 版本号
    public Drawable appIcon=null;    // 图标
    public boolean isSysFlag=false; // 系统标识

    @Override
    public String toString() {
        return "AppPackageInfo [packageName=" + packageName + ", appName="
                + appName + ", appSize=" + appSize + ", appVersion="
                + appVersion + ", appVersion_code=" + appVersion_code
                + ", appIcon=" + appIcon + ", isSysFlag=" + isSysFlag + "]";
    }
}

