package com.mingrisoft.qrcode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

/**
 * 使用Zxing精简包，这里使用的网上精简好的资源包
 * 如果想自己精简，可去https://github.com/zxing/zxing
 * 下载Zxing资源，这里有Google官方Demo
 */
public class MainActivity extends MPermissionsActivity {

    private static final int SCANCODE = 1;//用于请求扫描界面
    private TextView scanMessage;//展示扫描的二维码信息
    private ImageView enCodeImage1,enCodeImage2;//展示生成的二维码
    private EditText editText;//输入要生成二维码的内容
    private LinearLayout linearLayout;//用来拦截edittext的焦点

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        scanMessage = (TextView) findViewById(R.id.scan_txt);
        enCodeImage1 = (ImageView) findViewById(R.id.code_image1);
        enCodeImage2 = (ImageView) findViewById(R.id.code_image2);
        editText = (EditText) findViewById(R.id.input_txt);
        linearLayout = (LinearLayout) findViewById(R.id.ll);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    View view = getCurrentFocus();
                    if (view != null) {
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                                hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });
    }

    /**
     * 扫描二维码的点击事件
     * @param view
     */
    public void scanCode (View view){
        requestPermission(new String[]{Manifest.permission.CAMERA},1000);
    }

    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        if (requestCode == 1000){
            //开启扫描二维码界面
            startActivityForResult(new Intent(this, CaptureActivity.class),SCANCODE);
        }
    }

    /**
     * 生成不带Logo的二维码
     * @param view
     */
    public void enCode1(View view){
        if ("".equals(editText.getText().toString())){
            Toast.makeText(this, "请在输入框中输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        //生成二维码
        Bitmap codeBitmap = EncodingUtils.createQRCode(editText.getText().toString(),500,500,null);
        enCodeImage1.setImageBitmap(codeBitmap);//显示二维码
    }
    /**
     * 生成带Logo的二维码
     * @param view
     */
    public void enCode2 (View view){
        //获取logo资源
        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.mrkj);
        //生成二维码
        Bitmap codeBitmap = EncodingUtils.createQRCode("http://www.mingrisoft.com/",500,500,logoBitmap);
        enCodeImage2.setImageBitmap(codeBitmap);//显示二维码
    }

    /**
     * 返回信息
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            return;
        }
        switch (requestCode) {
            case SCANCODE://接收返回的二维码信息
                scanMessage.setText(data.getStringExtra("result"));
                break;
            default:
                break;
        }
    }

}
