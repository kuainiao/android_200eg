package com.mingrisoft.asexcel;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mingrisoft.asexcel.db.DBHelper;
import com.mingrisoft.asexcel.excel.ExcelUtils;

public class MainActivity extends MPermissionsActivity implements OnClickListener {
    //信息输入框
    private EditText mFoodEdt;
    private EditText mArticlesEdt;
    private EditText mTrafficEdt;
    private EditText mTravelEdt;
    //按钮
    private Button export_bill;
    private TextView import_bill;
    //文件类 创建excel文档
    private File file;
    //excel首行
    private String[] title = { "日期", "吃", "穿", "住", "行" };
    //日期数组
    private String[] saveData;
    private DBHelper mDbHelper;
    //信息集合
    private ArrayList<ArrayList<String>> bill2List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        findViewsById();
        //初始化数据库
        mDbHelper = new DBHelper(this);
        //打开数据库类
        mDbHelper.open();
        //初始化信息集合
        bill2List = new ArrayList<ArrayList<String>>();
    }
    //初始化控件方法
    private void findViewsById() {
        //初始化信息输入框
        mFoodEdt = (EditText) findViewById(R.id.family_bill_food_edt);
        mArticlesEdt = (EditText) findViewById(R.id.family_bill_articles_edt);
        mTrafficEdt = (EditText) findViewById(R.id.family_bill_traffic_edt);
        mTravelEdt = (EditText) findViewById(R.id.family_bill_travel_edt);
        //初始化按钮
        export_bill = (Button) findViewById(R.id.export_bill);
        import_bill = (TextView) findViewById(R.id.import_bill);
        //绑定按钮点击事件
        export_bill.setOnClickListener(this);
        import_bill.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        //点击事件
        switch (v.getId()) {
            case R.id.export_bill:// 使用SDcard写操作
                requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0001);
                break;

            case R.id.import_bill:// 使用SDcard读操作
                requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0x0002);
                break;
        }

    }
    /**
     * 权限成功回调函数
     *
     * @param requestCode
     */
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        switch (requestCode) {
            case 0x0001:
                into1();
                break;
            case 0x0002:
                //进入用户账单页
                Intent it =new Intent(MainActivity.this,UserInfoActivity.class);
                //跳转页面
                startActivity(it);
                break;
        }

    }
    public void into1() {
        //获取输入信息添加到信息数组
        saveData = new String[]{
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                mFoodEdt.getText().toString().trim(),
                mArticlesEdt.getText().toString().trim(),
                mTrafficEdt.getText().toString().trim(),
                mTravelEdt.getText().toString().trim()};
        if (canSave(saveData)) {
            // ContentValues 和HashTable类似都是一种存储的机制 但是两者最大的区别就在于,
            // contenvalues只能存储基本类型的数据,像string,int之类的
            //创建ContentValues储存数据
            ContentValues values = new ContentValues();
            values.put("time",
                    new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            values.put("food", mFoodEdt.getText().toString());
            values.put("use", mArticlesEdt.getText().toString());
            values.put("traffic", mTrafficEdt.getText().toString());
            values.put("travel", mTravelEdt.getText().toString());
            //添加数据到数据库中
            long insert = mDbHelper.insert("family_bill", values);
            //判断数据添加
            if (insert > 0) {
                //创建ecxcel文件，保存数据
                initData();
            }
        } else {
            Toast.makeText(this, "请填写任意一项内容", Toast.LENGTH_SHORT).show();
        }
    }

    //创建文件保存信息方法
    public void initData() {
        //创建文件夹
        file = new File(getSDPath() + "/Family");
        //创建
        makeDir(file);
        //利用Excelutils创建在文件夹中创建excelutils文件，以及添加标题
        ExcelUtils.initExcel(file.toString() + "/bill.xls", title);
        //获取数据库中信息添加到exce文档中
        ExcelUtils.writeObjListToExcel(getBillData(), getSDPath()
                + "/Family/bill.xls", this);
    }
   //获取数据库信息添加到list列表中
    private ArrayList<ArrayList<String>> getBillData() {
        //查询Cursor类
        Cursor mCrusor = mDbHelper.exeSql("select * from family_bill");
        //初始化列表
        bill2List =new ArrayList<ArrayList<String>>();
        //循环查询数据
        while (mCrusor.moveToNext()) {
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(mCrusor.getString(1));
            beanList.add(mCrusor.getString(2));
            beanList.add(mCrusor.getString(3));
            beanList.add(mCrusor.getString(4));
            beanList.add(mCrusor.getString(5));
            bill2List.add(beanList);
        }
        //关闭查询类
        mCrusor.close();
        //返回列表信息
        return bill2List;
    }
   //创建文件夹
    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }
    //获取sd卡更目录路径
    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;

    }
    //判断是否有数据
    private boolean canSave(String[] data) {
        boolean isOk = false;
        for (int i = 0; i < data.length; i++) {
            if (i >= 0 && i < data.length) {
                if (!TextUtils.isEmpty(data[i])) {
                    isOk = true;
                }
            }
        }
        return isOk;
    }
}

