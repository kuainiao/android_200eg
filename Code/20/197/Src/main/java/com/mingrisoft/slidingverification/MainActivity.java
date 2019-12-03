package com.mingrisoft.slidingverification;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends Activity {

    /**
     * 滑块
     */
    private SeekBar mSeekBar;
    /**
     * 自定义的控件
     */
    private SlidingVerificationView verificationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        verificationView = (SlidingVerificationView) findViewById(R.id.dy_v);
        mSeekBar = (SeekBar) findViewById(R.id.sb_dy);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                Log.e("main", "当前位置" + i);
                verificationView.setUnitMoveDistance(verificationView.getAverageDistance(seekBar.getMax()) * i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                verificationView.testPuzzle();
            }
        });
        /**
         * 滑块滑动时的监听
         * */
        verificationView.setPuzzleListener(new SlidingVerificationView.onPuzzleListener() {
            @Override
            public void onSuccess() {
                /**
                 * 滑动成功后弹出提示
                 * */
                Toast.makeText(MainActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                mSeekBar.setProgress(0);   //使进度条回归起点
                verificationView.reSet();               //重置滑动验证
            }

            @Override
            public void onFail() {
                /**
                 * 滑动失败时 弹出提示
                 * */
                Toast.makeText(MainActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                mSeekBar.setProgress(0);   //使进度条回归起点
            }
        });
    }
}
