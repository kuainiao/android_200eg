package com.mingrisoft.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mingrisoft.musicplayer.bean.Music;
import com.mingrisoft.musicplayer.utils.History;
import com.mingrisoft.musicplayer.utils.SearchFile;
import com.mingrisoft.musicplayer.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;

import me.wcy.lrcview.LrcView;


/**
 * Author LYJ
 * Created on 2017/1/16.
 * Time 22:54
 */

public class PlayMusicService extends Service implements MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener{
    private static final String TAG = PlayMusicService.class.getSimpleName();
    private ArrayList<Music> musicList;//音乐集合
    public boolean isPlay;//播放判断
    public int nowPlayPosition = -1;//当前播放的音乐标记
    private History history;//用于获取播放记录
    private MediaPlayer mediaPlayer;//音乐播放对象
    private LrcView lrcView;//
    private Thread thread;//
    private boolean isPlaying;//
    private Handler handler = new Handler(msg -> {
        if (msg.what == 1){
            long time = mediaPlayer.getCurrentPosition();
            if(lrcView.hasLrc()){
                lrcView.updateTime(time);
            }
        }
        return false;
    });

    /**
     * 同步资源后播放音乐
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    /**
     * 音乐播放结束后调用
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        lrcView.onDrag(0);
    }

    /**
     * 返回服务对象
     */
    public class MusicBinder extends Binder{
        public PlayMusicService getService(){
            return PlayMusicService.this;
        }
    }

    /**
     * 初始化
     */
    @Override
    public void onCreate() {
        super.onCreate();
        history = History.getInstance(this);
        nowPlayPosition = history.getInt("HISTORY",-1);
        //实例化音乐播放对象
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    /**
     * 绑定
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //获取音乐数据集合
        musicList = intent.getParcelableArrayListExtra("MUSIC");
        Log.w(TAG, "获取音乐列表->" + musicList.size() );
        return new MusicBinder();
    }

    /**
     * 播放测试
     */
    public void startPlayMusic(int index){
        nowPlayPosition = index;
        history.putInt("HISTORY",nowPlayPosition);//保存记录
        isPlay = true;//设置正在播放
        playMusic();//播放音乐
    }

    /**
     * 下一曲
     */
    public void playNextMusic(){
        if (nowPlayPosition == -1){//表示当前没有播放音乐
            return;
        }
        //如果当前播放的索引值小于数据总值
        if (nowPlayPosition < musicList.size() - 1){
            nowPlayPosition+=1;
            EventBus.getDefault().post(musicList.get(nowPlayPosition));
        }else {//提示播放到结尾
            ToastUtils.Short(this,"已经播到最后一首了！");
        }

    }
    /**
     * 停止播放
     */
    public void stopPlayMusic(){
        isPlay = false;
        mediaPlayer.pause();
        isPlaying = false;
        handler.removeCallbacks(thread);
        thread = null;
        Log.e(TAG, "停止播放: ");
    }

    /**
     * 播放音乐
     */
    private void playMusic(){
        try {
            mediaPlayer.reset();//重置播放器
            //获取歌曲对象
            Music music = musicList.get(nowPlayPosition);
            //获取歌曲的路径
            String musicPath = music.getMusic_path();
            //设置播放歌曲
            mediaPlayer.setDataSource(musicPath);
            //字符串替换
            String lrcPath = musicPath.replace(".mp3",".lrc");
            //查找歌词
            lrcView.loadLrc(SearchFile.getLrcText(lrcPath));
            isPlaying = true;
            //用于更新歌词
            thread = new Thread(runnable);
            thread.start();
            //设置播放模式为播放音乐
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //同步资源
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用线程控制歌词显示
     */
    private Runnable runnable = () -> {
        while (isPlaying) {
            try {
                Thread.sleep(200);
                handler.sendEmptyMessage(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 获取歌词控件
     * @param view
     */
    public void setLrcVew(LrcView view){
        lrcView = view;
    }
    /**
     * 销毁
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isPlay){
            stopPlayMusic();
        }
        isPlaying = false;
        handler.removeCallbacks(thread);
        thread = null;
        mediaPlayer.release();
        mediaPlayer = null;
        Log.i(TAG, "onDestroy: 服务被销毁！");
    }
}
