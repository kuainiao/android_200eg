package com.mingrisoft.gesturerecognition;

import java.io.File;
import java.util.ArrayList;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;


/**
 * 手势识别界面
 */
public class GesturePerformedActivity extends MPermissionsActivity {


    /**
     * 从手机中读取之前存入的文件
     */
    private final File mStoreFile = new File(
            Environment.getExternalStorageDirectory(), "gestures");


    GestureLibrary mGestureLib;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gesture_perform);
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE}, 1000);
        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures_overlay);

        gestures.setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);

        gestures.setFadeOffset(2000);
        gestures.setGestureColor(Color.CYAN);
        gestures.setGestureStrokeWidth(6);


        gestures.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView overlay,
                                           Gesture gesture) {

                ArrayList<Prediction> predictions = mGestureLib.recognize(gesture);
                if (predictions.size() > 0) {
                    Prediction prediction = (Prediction) predictions.get(0);
                    if (prediction.score > 1.0) {
                        /*弹出予以性对应的号码提示*/
                        Toast.makeText(GesturePerformedActivity.this, prediction.name, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_CALL);  //调用系统拨打电话
                        Uri data = Uri.parse("tel:" + prediction.name);  //获取电话号
                        intent.setData(data);                            //讲电话号传给intent
                        startActivity(intent);                           //启动intent
                    }
                }
            }
        });

        if (mGestureLib == null) {
            mGestureLib = GestureLibraries.fromFile(mStoreFile);       //将文件用GestureLibrary读取出来
            mGestureLib.load();
        }
    }

}
