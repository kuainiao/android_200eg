package com.mingrisoft.applicationmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.AppInstalledAdapter;
import interfaces.InstalledInterface;
import utils.AppInfo;
import utils.Utils;

public class MainActivity extends Activity implements InstalledInterface {

    private AppInfo appInfo;                                //app应用
    public List<AppInfo> appPackageInfos=null;             //数据集
    public List<AppInfo> appNoSysInfos=null;               //非系统应用信息
    public List<AppInfo> appSysInfos=null;                 //系统应用
    private AppInstalledAdapter adapter;                    //已安装应用数据适配器
    private ListView listView;                              //显示应用信息的列表控件
    private LinearLayout layout_loading;                   //显示加载中的布局
    private Button show_all,show_no_sys_apps,show_sys_apps;      //按钮
    private int flagNum=0;//当前显示项
    public static MainActivity AppAct=null;

    public static final int APPS_ALL=0;//所有应用
    public static final int APPS_THIRD=1;//非系统应用
    public static final int APPS_SYS=2;//系统应用
    private boolean isWhole=true;            //是否选中全部按钮，true为选中
    private boolean isNonSystem=true;       //是否选中非系统按钮，true为选中
    private boolean isSystem=true;       //是否选中系统按钮，true为选中

    public Handler handler=new Handler(){

        public  void handleMessage(Message msg) {
            switch (msg.what){
                //刷新
                case 0:
                    if(appPackageInfos!=null && appPackageInfos.size()>0){
                        adapter.setList(appPackageInfos);
                        adapter.notifyDataSetChanged();
                    }
                    layout_loading.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initUI();           //调用初始化UI控件
    }

    /**
     * 初始化UI控件
     */
    private void initUI(){
        AppAct=this;
        listView=(ListView)findViewById(R.id.listview_show);
        show_all=(Button)findViewById(R.id.show_all);
        show_no_sys_apps=(Button)findViewById(R.id.show_no_sys_apps);
        show_sys_apps=(Button)findViewById(R.id.show_sys_apps);
        layout_loading=(LinearLayout)findViewById(R.id.layout_loading);

        adapter = new AppInstalledAdapter(this);
        adapter.setAppInstalledInterface(this);
        listView.setAdapter(adapter);           //配置适配器，用于显示应用信息
        initData();                               //初始化数据

        show_all.setOnClickListener(listener);              //全部应用按钮监听
        show_no_sys_apps.setOnClickListener(listener);     //非系统应用按钮监听
        show_sys_apps.setOnClickListener(listener);        //系统应用按钮监听
    }

    /**
     * 按钮的单击事件
     */
    private View.OnClickListener listener=new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                //单击全部应用按钮，显示全部应用信息
                case R.id.show_all:                 //全部应用按钮
                        //单击更换按钮背景图片
                        show_all.setBackgroundResource(R.drawable.whole_btn_cion);
                        show_no_sys_apps.setBackgroundResource(R.drawable.non_btn_icon1);
                        show_sys_apps.setBackgroundResource(R.drawable.system_btn_cion1);
                    if(adapter!=null && flagNum!= APPS_ALL){
                        adapter.setList(appPackageInfos);   //设置数据集
                        adapter.notifyDataSetChanged();       //更改数据集
                        flagNum=APPS_ALL;                    //当前显示项
                    }
                    break;
                //单击非系统应用按钮，显示非系统应用信息
                case R.id.show_no_sys_apps:
                        //单击更换按钮背景图片
                        show_all.setBackgroundResource(R.drawable.whole_btn_cion1);
                        show_no_sys_apps.setBackgroundResource(R.drawable.non_btn_icon);
                        show_sys_apps.setBackgroundResource(R.drawable.system_btn_cion1);
                    if(adapter!=null && flagNum!=APPS_THIRD){
                        adapter.setList(appNoSysInfos);
                        adapter.notifyDataSetChanged();
                        flagNum=APPS_THIRD;
                    }
                    break;
                //单击系统应用按钮，显示系统应用信息
                case R.id.show_sys_apps:
                    //单击更换按钮背景图片
                    show_all.setBackgroundResource(R.drawable.whole_btn_cion1);
                    show_no_sys_apps.setBackgroundResource(R.drawable.non_btn_icon1);
                    show_sys_apps.setBackgroundResource(R.drawable.system_btn_cion);
                    if(adapter!=null && flagNum!=APPS_SYS){
                        adapter.setList(appSysInfos);
                        adapter.notifyDataSetChanged();
                        flagNum=APPS_SYS;
                    }
                    break;
            }
        }
    };
    /**
     * 方法说明：初始化数据
     */
    private void initData(){
        layout_loading.setVisibility(View.VISIBLE);
        new Thread(new Runnable(){
            @Override
            public void run() {
                appPackageInfos= Utils.getAppsList(getApplicationContext());
                setCustomInfos(appPackageInfos);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }
    /**
     * 方法说明：区分系统及非系统应用
     */
    private void setCustomInfos(List<AppInfo> list){
        appSysInfos=new ArrayList<AppInfo>();
        appNoSysInfos=new ArrayList<AppInfo>();
        for(AppInfo info:list){
            if(info.isSysFlag){
                appSysInfos.add(info);
            }else{
                appNoSysInfos.add(info);
            }
        }
    }
    /**
     * 方法说明：打开应用
     */
    @Override
    public void setOpenOnClick(int pos, View v) {
            if (flagNum==APPS_ALL){
                appInfo=appPackageInfos.get(pos);
                Utils.openPackage(AppAct, appInfo.packageName);
            }if (flagNum==APPS_THIRD){
                appInfo=appNoSysInfos.get(pos);
                Utils.openPackage(AppAct, appInfo.packageName);
            }if (flagNum==APPS_SYS){
                appInfo=appSysInfos.get(pos);
                Utils.openPackage(AppAct, appInfo.packageName);
            }
    }
    /**
     * 方法说明：卸载应用
     */
    @Override
    public void setUninstallOnClick(final int pos, View v) {
        appInfo=appPackageInfos.get(pos);
        new AlertDialog.Builder(AppAct)
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_apkUninstall)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.uninstallApk(AppAct,appInfo.packageName,0);
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }


    /**
     * 方法说明：卸载结果返回
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0){
            //卸载结束 无论是否成功都更新列表
            initData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
