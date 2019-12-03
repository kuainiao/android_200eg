package com.mingrisoft.searchapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;



import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import adapter.MyAdapter;

import util.AppInfo;
import util.IUninstall;
import util.Utils;


public class MainActivity extends Activity
        implements AdapterView.OnItemClickListener, IUninstall,
        SearchView.OnQueryTextListener{

    List<AppInfo> list;//数据集合
    List<AppInfo> allList = new ArrayList<AppInfo>();   //更新数据数据
    ListView listView;                                         // 列表视图
    MyAdapter adapter; // 适配器
    SearchView searchView;  //搜索控件
    public static final int NAME_SORT = 0;//按名称排序
    int currSort = NAME_SORT;//当前排序
    Comparator<AppInfo> comparator = null;// 当前所使用的比较器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.KEY = "";//初始化
        // 初始化控件
        listView = (ListView) findViewById(R.id.lv_main);
        adapter = new MyAdapter(this);
        adapter.setList(list);
        adapter.setUninstall(this);// 传入接口
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        searchView= (SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);
        updateData();// 子线程--拿数据
    }





    // 排序更新
    private void update_sort() {
        if (currSort == NAME_SORT) {
            comparator = nameComparator;//为了添加其它比较方式
        }
        Collections.sort(list, comparator);//按照排列顺序显示列表
        adapter.setList(list);
        adapter.notifyDataSetChanged();//刷新视图

    }

    // 应用名比较器
    Comparator<AppInfo> nameComparator = new Comparator<AppInfo>() {
        @Override
        public int compare(AppInfo lhs, AppInfo rhs) {
            // 为了适应汉字的比较
            Collator collator = Collator.getInstance(Locale.CHINA);
            //返回比较文字的顺序
            return collator.compare(lhs.appName, rhs.appName);

        }
    };


    // 1声明进度框对象
    ProgressDialog dialog;

    // 显示一个环形进度框
    public void showProgressDialog() {
        //创建对话框
        dialog = new ProgressDialog(this);
        //"旋转"样式
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("系统信息");
        dialog.setMessage("正在加载应用列表,请耐心等待...");
        dialog.show();//显示
    }

    Handler handler = new Handler() {// 内部类

        @Override
        public void handleMessage(Message msg) {
            // 重写方法
            if (msg.what == 1) {//UI线程的回调处理
                dialog.dismiss();
                //更新列表
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this
                        , "应用数:" + list.size(), Toast.LENGTH_LONG).show();
                update_sort();  //更新排序
            }
        }
    };

    //子线程
    private void updateData() {
        //启动新线程,处理耗时操作
        new Thread() {
            public void run() {
                //获得所有的应用
                list = Utils.getAppList(MainActivity.this);
                allList.clear();//清空
                allList.addAll(list);//复制集合
                adapter.setList(list);//设置集合
                //给主线程发消息
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);// msg.what=1
            }

            ;
        }.start();
        showProgressDialog();// 显示进度框
    }

    /**
     *打开列表中的应用
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //获取本行的应用信息对象
        AppInfo app = (AppInfo) parent.getItemAtPosition(position);
        //运行应用
        Utils.openPackage(this, app.packageName);
    }

    /**
     * 实现卸载应用
     * @param pos       行号
     * @param packageName 包名
     */
    @Override
    public void onBtnClick(int pos, String packageName) {
        Utils.uninstallApk(this, packageName, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //接收窗体返回值
        if (requestCode == 0) {
            //刷新列表
            updateData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //获取当前时间
    long time = System.currentTimeMillis();

    /**
     * 退出应用
     */
    @Override
    public void onBackPressed() {
        //计算两次单击间隔的时间
        long delta = System.currentTimeMillis() - time;
        if (delta < 1000) {// 小于1秒
            // 用户真的要退出
            finish();
        } else {//提示再次单击退出
            Toast.makeText(this, "再点击一次退出", Toast.LENGTH_SHORT).show();
            time = System.currentTimeMillis();//更新时间
        }
    }

    /**
     * 在用户提交查询时调用
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        //提交关键字
        Utils.toast(this, "您查询的关键字是 ：" + query.trim());
        Utils.KEY = query.trim();       //删除空白字符
        list = Utils.getSearchResult(allList, query);//根据关键字生成结果
        update_sort();//重新排序
        return true;
    }

    /**
     * 当用户更改查询文本时调用
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        Utils.KEY = newText.trim();     //删除空白字符
        list = Utils.getSearchResult(allList, newText.trim());//根就关键字生成结果
        update_sort();//重新排序更新
        return true;
    }





}


