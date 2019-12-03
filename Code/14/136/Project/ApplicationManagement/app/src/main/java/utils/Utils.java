package utils;

/**
 * Created by Administrator on 2017/1/13.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.StatFs;
import android.text.format.Formatter;


import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 类说明：工具类
 */
public class Utils {


    /**
     * 获取已安装应用信息列表
     */
        public static List<AppInfo> getAppsList(Context context) {
        //所有信息集合
        List<AppInfo> appPackageInfos = new ArrayList<AppInfo>();
        try{
            //软件包管理器
            PackageManager pManager = context.getPackageManager();
            //获取系统中所有的应用
            List<PackageInfo> appList = getAllApkList(context);
            AppInfo appPackageInfo;
            String dir;
            for (int i = 0; i < appList.size(); i++) {
                PackageInfo packageInfo = appList.get(i);
                appPackageInfo=new AppInfo();
                //获取包名
                appPackageInfo.packageName=packageInfo.packageName;
                //获取应用的版本名称
                appPackageInfo.appVersion=packageInfo.versionName;
                //获取版本号
                appPackageInfo.appVersion_code=packageInfo.versionCode;
                //系统标识
                appPackageInfo.isSysFlag=((packageInfo.applicationInfo.flags&
                        ApplicationInfo.FLAG_SYSTEM)<=0)?false:true;
                //应用的位置路径
                dir = packageInfo.applicationInfo.publicSourceDir;
                //应用的大小
                appPackageInfo.appSize=getSize(new File(dir).length());
                //包名与图标
                appPackageInfo.appName=(String) packageInfo.applicationInfo.loadLabel(pManager);
                appPackageInfo.appIcon=packageInfo.applicationInfo.loadIcon(pManager);
                //添加所有信息
                appPackageInfos.add(appPackageInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appPackageInfos;
    }

    /**
     * 格式转换应用大小 单位"M"
     */
    public static String getSize(long size) {
        return new DecimalFormat("0.##").format(size * 1.0 / (1024 * 1024)) + "M";
    }

    /**
     * 获取系统中已安装应用
     */
    public static List<PackageInfo> getAllApkList(Context context) {
        //保存所有的应用包信息
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        //查找所有的应用包
        PackageManager pm = context.getPackageManager();
        // 获取系统中已安装应用的包信息
        List<PackageInfo> packageInfoList = pm.getInstalledPackages(0);
        for (int i = 0; i < packageInfoList.size(); i++) {
            PackageInfo packageInfo = (PackageInfo) packageInfoList.get(i);
            //不添加本应用包信息
            if (!packageInfo.packageName.equals(context.getPackageName())) {
                apps.add(packageInfo);      //添加应用包信息
            }
        }
        return apps;
    }

    /**
     * 打开应用
     * @param context
     * @param packageName
     * @return
     */
    public static boolean openPackage(Context context, String packageName) {

        try {
            Intent intent = new Intent();  //创建intent对象
            //获取程序包的名称，实现开启功能
            intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                //设置以新任务打开.
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //启动要打开的应用程序
                context.startActivity(intent);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 卸载程序
     */
    public static void uninstallApk(Activity context, String packageName, int requestCode) {
        //卸载程序的包名
        Uri packageURI = Uri.parse("package:" + packageName);
        //创建卸载Intent对象
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        //启动卸载
        context.startActivityForResult(uninstallIntent, requestCode);
    }



}