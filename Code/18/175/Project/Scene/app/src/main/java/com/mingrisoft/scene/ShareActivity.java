package com.mingrisoft.scene;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.TransitionSet;
import android.widget.ImageView;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            TransitionSet transitionSet = new TransitionSet();
            transitionSet.addTransition(new ChangeBounds());
            transitionSet.addTarget("pic");
            transitionSet.setDuration(1000);
            //设置将要跳转的界面的进入动画
            getWindow().setSharedElementEnterTransition(transitionSet);
            //设置将要当前界面的退出动画
            getWindow().setSharedElementExitTransition(transitionSet);
            //设置将要跳转界面的退出动画
            getWindow().setSharedElementReturnTransition(transitionSet);
            //设置重返当前界面的进入动画
            getWindow().setSharedElementReenterTransition(transitionSet);
        }
        setContentView(R.layout.activity_share);
        Bitmap bitmap =getRes("jpg"+getIntent().getIntExtra("position",0));
        //展示图片
        ImageView iv = (ImageView) findViewById(R.id.pic);
        iv.setImageBitmap(bitmap);
    }
    /**
     * 获取图片资源
     *
     * @param name
     * @return
     */
    public Bitmap getRes(String name) {
        int resID = getResources().getIdentifier(name, "mipmap", getPackageName());
        return BitmapFactory.decodeResource(getResources(), resID);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAfterTransition();
    }

}
