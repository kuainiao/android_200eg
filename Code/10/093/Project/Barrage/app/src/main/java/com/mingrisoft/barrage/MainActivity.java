package com.mingrisoft.barrage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

public class MainActivity extends MPermissionsActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LAND = 1;//竖屏
    private static final int PORT = 2;//横屏
    private int screenOrientation;//屏幕方向
    private VideoView videoView;//视屏播放器
    private MyController controller;//播放控制器
    private MyBarraggeSwitch barragge;//弹幕开关控制类
    private FrameLayout controllerHome;//添加控制器的布局
    private boolean barraggeState;//弹幕状态
    private ImageView hint;//图片用于提示
    private AudioManager manager;//音频管理器
    private int maxSoundValues;//最大音量
    private int nowSoundValues;//当前音量
    private float nowScreenBright;//当前界面亮度
    private DanmakuView danmakuView;//显示弹幕
    private DanmakuContext danmakuContext;//弹幕配置
    private ArrayList<String> messages = new ArrayList<>();//弹幕假数据
    private Random random = new Random();//获取随机数
    private SharedPreferences preferences;//数据存储
    private SharedPreferences.Editor editor;//事物提交者
    private boolean isFinish;//是否退出
    private boolean isvisible;//是否显示
    /**
     * 弹幕数据解析器
     */
    private BaseDanmakuParser parser = new BaseDanmakuParser() {

        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };

    /**
     * 初始化弹幕的数据
     */
    private void initMessages() {
        messages.add("讲的太好了。");
        messages.add("收藏！");
        messages.add("给力！！！");
        messages.add("收藏给硬币！");
        messages.add("真相！");
        messages.add("三分归元气，七分靠打拼。");
        messages.add("刷歌词挡字母的你不要停！");
        messages.add("我怎么看不到弹幕？？？");
        messages.add("前方准备?");
        messages.add("笑死我了！");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉Activity上面的状态栏
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("mine", MODE_PRIVATE);
        editor = preferences.edit();
        videoView = (VideoView) findViewById(R.id.video);
        controllerHome = (FrameLayout) findViewById(R.id.home);
        hint = (ImageView) findViewById(R.id.hint);
        Log.w("生命周期", "onCreate: ");
        if (!Vitamio.isInitialized(this)) {
            Log.e("Vitamio", "没有被初始化！");
            return;
        }
        //播放控制器
        controller = new MyController(this, true, controllerHome);
        barragge = new MyBarraggeSwitch();
        controller.setBarrageSwitchListener(barragge);
        videoView.setVideoURI(Uri.parse("http://video.mingrisoft.com/5571553ac28cb.mp4"));
        videoView.setMediaController(controller);//设置控制器
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });
        /**
         * 手势控制
         */
        MyGestureDetector myGestureDetector = new MyGestureDetector();
        final GestureDetector gestureDetector = new GestureDetector(this, myGestureDetector);
        controllerHome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    hint.setVisibility(View.GONE);
                    return true;
                }
                return gestureDetector.onTouchEvent(event);
            }
        });
        /**
         * 声音管理
         */
        manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxSoundValues = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//得到听筒模式的最大值
        /**
         * 弹幕
         */
        initMessages();
        danmakuView = (DanmakuView) findViewById(R.id.mdanmaku);
        danmakuView.enableDanmakuDrawingCache(true);
        danmakuView.setVisibility(View.GONE);
        danmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                barraggeState = true;
                danmakuView.start();
                generateSomeDanmaku();
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });
        danmakuContext = DanmakuContext.create();
        danmakuView.prepare(parser, danmakuContext);
    }

    /**
     * 向弹幕View中添加一条弹幕
     */
    private void addDanmaku(String content) {
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text = content;
        danmaku.padding = 5;//弹幕内容
        danmaku.textSize = sp2px(20);//字体大小
        danmaku.textColor = Color.BLACK;//字体颜色
        danmaku.setTime(danmakuView.getCurrentTime());//发布时间
        danmakuView.addDanmaku(danmaku);
    }

    /**
     * 随机生成一些弹幕内容以供测试
     */
    private void generateSomeDanmaku() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (barraggeState) {
                    int time = random.nextInt(300);
                    int position = random.nextInt(messages.size());
                    String content = messages.get(position);
                    addDanmaku(content);
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * sp转px的方法。
     */
    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (danmakuView != null && danmakuView.isPrepared()) {
            danmakuView.pause();
        }
        editor.putLong("DURATION", videoView.getCurrentPosition());
        editor.putBoolean("VISIBLE",isvisible);
        editor.commit();
        Log.w("生命周期", "onPause: " + preferences.getLong("DURATION",0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (danmakuView != null && danmakuView.isPrepared() && danmakuView.isPaused()) {
            danmakuView.resume();
        }
        long duration = preferences.getLong("DURATION", 0);
        Log.w("生命周期", "DURATION：" + duration);
        videoView.seekTo(duration);
        Log.w("生命周期", "onResume: ");
        isvisible = preferences.getBoolean("VISIBLE",false);
        if (isvisible){
            danmakuView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        barraggeState = false;
        if (danmakuView != null) {
            danmakuView.release();
            danmakuView = null;
        }
        if (isFinish){
            editor.putLong("DURATION", 0);
            editor.putBoolean("VISIBLE" , false);
            editor.commit();
        }
        Log.w("生命周期", "onDestroy: ");
    }

    /**
     * 弹幕开关管理类
     */
    private class MyBarraggeSwitch implements MyController.BarraggeSwitchListener {

        @Override
        public void barraggeSwitchState(boolean state) {
            isvisible = state;
            if (state) {
                danmakuView.setVisibility(View.VISIBLE);
                Log.w(TAG, "barraggeSwitchState: " + state);
            } else {
                danmakuView.setVisibility(View.GONE);
                Log.w(TAG, "barraggeSwitchState: " + state);
            }
        }
    }

    /**
     * 手势封装类
     */
    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private int windowWidth;//屏幕宽度
        private int windowHeight;//控制器的高度
        private static final int LEFT = 1;
        private static final int RIGHT = 2;
        private int down;

        @Override
        public boolean onDown(MotionEvent e) {
            windowSize();
            if (e.getRawX() < windowWidth / 2) {
                hint.setImageResource(R.mipmap.sound);
                down = LEFT;
            } else {
                hint.setImageResource(R.mipmap.light);
                down = RIGHT;
            }
            hint.setVisibility(View.VISIBLE);
            return true;
        }

        /**
         * Y轴上减 下增
         * x轴左键 右增
         *
         * @param e1
         * @param e2
         * @param distanceX
         * @param distanceY
         * @return
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float startY = e1.getY();
            float moveY = e2.getRawY();
            float changeValues = (startY - moveY) / windowHeight;
            switch (down) {
                case LEFT://调节声音
                    changeSound(changeValues);
                    break;
                case RIGHT://调节亮度
                    changeBright(changeValues);
                    break;
            }
            return true;
        }

        /**
         * 调节声音
         *
         * @param values
         */
        private void changeSound(float values) {
            nowSoundValues = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (nowSoundValues < 0) {
                nowSoundValues = 0;
            }
            int index = (int) (values * maxSoundValues) + nowSoundValues;
            if (index > maxSoundValues) {
                index = maxSoundValues;
            } else if (index < 0) {
                index = 0;
            }
            manager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        }

        /**
         * 调节亮度
         *
         * @param values
         */
        private void changeBright(float values) {
            nowScreenBright = getWindow().getAttributes().screenBrightness;
            if (nowScreenBright <= 0.00f || nowScreenBright < 0.01f) {
                nowScreenBright = 0.50f;
            }
            WindowManager.LayoutParams lpa = getWindow().getAttributes();
            lpa.screenBrightness = nowScreenBright + values;
            if (lpa.screenBrightness > 1.0f)
                lpa.screenBrightness = 1.0f;
            else if (lpa.screenBrightness < 0.01f)
                lpa.screenBrightness = 0.01f;
            getWindow().setAttributes(lpa);
        }

        /**
         * 获取屏幕的宽度
         */
        private void windowSize() {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            windowWidth = dm.widthPixels;
            windowHeight = dm.heightPixels;
            Log.w(TAG, "windowSizeW: " + windowWidth);
            Log.w(TAG, "windowSizeH: " + windowHeight);
        }
    }

    /**
     * 窗口的焦点发生改变时调用
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        checkScreenOrientation();
        if (screenOrientation == LAND) {
            if (hasFocus && Build.VERSION.SDK_INT >= 19) {
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
        Log.w("生命周期", "onWindowFocusChanged: ");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.w("生命周期", "onConfigurationChanged: ");
    }

    /**
     * 屏幕方向检查
     */
    private void checkScreenOrientation() {
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                screenOrientation = LAND;
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                screenOrientation = PORT;
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            isFinish = true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
