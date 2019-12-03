package com.mingrisoft.fileinfo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends MPermissionsActivity implements View.OnClickListener {
    //声明控件
    private Button bt1, bt2;
    private EditText et1;
    private File file;
    //创建文件的文件名
    private static final String FILENAME = "filedemo.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        bt1 = (Button) this.findViewById(R.id.bt1);
        bt2 = (Button) this.findViewById(R.id.bt2);
        et1 = (EditText) this.findViewById(R.id.et1);
        //绑定点击事件
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        //初始化文件 在根目录创建目录为filedemo的txt文件
        file = new File(Environment.getExternalStorageDirectory(),
                FILENAME);
    }

    //    写入SDcard信息
    private void writesd() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                //写入文件流
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(et1.getText().toString().getBytes());
                //关闭写入流
                fos.close();
                //弹出提示信息
                Toast.makeText(MainActivity.this, "写入文件成功",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                //弹出提示信息
                Toast.makeText(MainActivity.this, "写入文件失败",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // 此时SDcard不存在或者不能进行读写操作的
            Toast.makeText(MainActivity.this,
                    "此时SDcard不存在或者不能进行读写操作", Toast.LENGTH_SHORT).show();
        }
    }

    //    读取SDcard信息
    private void readsd() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                byte[] b = new byte[inputStream.available()];
                //读取文件流
                inputStream.read(b);
                String nr = new String(b);
                if (!TextUtils.isEmpty(nr)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            MainActivity.this);
                    builder.setTitle("读取文档信息");
                    builder.setMessage("文档内容： " + nr);
                    builder.setCancelable(false);
                    builder.setPositiveButton("知道了！", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.create().show();
                } else {
                    //弹出提示信息
                    Toast.makeText(MainActivity.this, "文档内容为空,请输入要保存信息！",
                            Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                //弹出提示信息
                Toast.makeText(MainActivity.this, "读取失败",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // 此时SDcard不存在或者不能进行读写操作的
            Toast.makeText(MainActivity.this,
                    "此时SDcard不存在或者不能进行读写操作", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt1:// 使用SDcard写操作
                //权限判断 没有弹出提示选择
                requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0001);
                break;
            case R.id.bt2:// 使用SDcard读操作
                //权限判断 没有弹出提示选择
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
                // 使用SDcard写操作
                writesd();
                break;
            case 0x0002:
                // 使用SDcard读操作
                readsd();
                break;
        }
    }
}
