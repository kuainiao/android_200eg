package com.mingrisoft.musicplayer.avtivity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mingrisoft.musicplayer.R;
import com.mingrisoft.musicplayer.adapter.MusicListAdapter;
import com.mingrisoft.musicplayer.base.MPermissionsActivity;
import com.mingrisoft.musicplayer.bean.Music;
import com.mingrisoft.musicplayer.service.PlayMusicService;
import com.mingrisoft.musicplayer.utils.History;
import com.mingrisoft.musicplayer.utils.SearchFile;
import com.mingrisoft.musicplayer.utils.ToastUtils;
import com.mingrisoft.musicplayer.view.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.wcy.lrcview.LrcView;


/**
 * Author LYJ
 * Created on 2017/1/16.
 * Time 11:30
 */
public class MusicActivity extends MPermissionsActivity implements ServiceConnection {

    //显示图像
    @BindView(R.id.music_author)
    ImageView musicAuthor;
    //显示音乐名称
    @BindView(R.id.music_name)
    TextView musicName;
    //显示歌手的名称
    @BindView(R.id.music_singer)
    TextView musicSinger;
    //播放下一首
    @BindView(R.id.play_next)
    ImageButton playNext;
    //播放开关
    @BindView(R.id.play_switch)
    CheckBox playSwitch;
    //显示歌词开关
    @BindView(R.id.play_lrc)
    CheckBox playLrc;
    //歌曲列表
    @BindView(R.id.music_list)
    RecyclerView musicList;
    @BindView(R.id.music_lrc)
    LrcView musicLrc;
    private MusicListAdapter adapter;//数据适配器
    private ArrayList<Music> musics;//数据结合
    private boolean isBind;//是否绑定
    private History history;//记录
    private PlayMusicService playMusicService;//播放服务
    private long exitTime;//第一次单机退出键的时间
    private int index;//索引值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        //获取记录
        history = History.getInstance(this);
        //请求读写权限
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
    }

    /**
     * 初始化操作
     */
    private void initialization() {
        //扫描本地音乐
        musics = SearchFile.searchLocaltion(this);
        if (musicList != null) {
            //绑定服务
            Intent intent = new Intent(this, PlayMusicService.class);
            intent.putParcelableArrayListExtra("MUSIC", musics);
            bindService(intent, this, BIND_AUTO_CREATE);
            initBottomBar();//初始化底部功能栏
            setMusicList(); //设置音乐列表
            setBottomBar();//设置底部功能栏
        } else {
            ToastUtils.Short(this, "本地没有歌曲！");
        }
    }

    /**
     * 初始化底部功能栏
     */
    private void initBottomBar() {
        int record = history.getInt("HISTORY", -1);
        if (record != -1) {
            setBottomBarMusicMessage(musics.get(record));
        }
    }

    /**
     * 设置底部功能栏音乐显示信息
     *
     * @param music
     */
    private void setBottomBarMusicMessage(Music music) {
        //设置底部栏显示的歌名
        musicName.setText(music.getMusic_name());
        //设置底部栏显示的歌手
        musicSinger.setText(music.getMusic_singer());
        //设置底部栏显示的专辑封面
        musicAuthor.setImageBitmap(
                SearchFile.getAuthor(this, music.getMusic_albumId()));
    }

    /**
     * 初始化音乐列表
     */
    private void setMusicList() {
        //创建适配器
        adapter = new MusicListAdapter(this, musics);
        //设置列表样式
        musicList.setLayoutManager(new LinearLayoutManager(this));
        //绑定适配器
        musicList.setAdapter(adapter);
        //设置分割线
        musicList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        //设置item的点击事件
        adapter.setOnItemClickListener((itemView, position) ->
                itemOnClick(musics.get(position), position));

    }

    /**
     * 处理Item点击事件
     *
     * @param music
     */
    private void itemOnClick(Music music, int position) {
        setBottomBarMusicMessage(music);
        //开启播放
        if (isBind) {
            playSwitch.setChecked(false);
            //设置播放的曲目
            playMusicService.nowPlayPosition = position;
            playSwitch.setChecked(true);
        }
    }

    /**
     * 设置底部功能栏
     */
    private void setBottomBar() {
        //播放或者暂停音乐
        playSwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> playOrStop());
        //播放下一首歌曲
        playNext.setOnClickListener(
                v -> {
                    if (isBind) {
                        playSwitch.setChecked(false);
                        //播放下一首歌曲
                        playMusicService.playNextMusic();
                        playSwitch.setChecked(true);
                    }
                });
        //开启歌词显示
        playLrc.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if (isChecked){
                        musicLrc.setVisibility(View.VISIBLE);
                    }else {
                        musicLrc.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 播放或停止
     */
    private void playOrStop() {
        if (!isBind) {
            ToastUtils.Short(this, "音乐服务开启失败！");
            return;
        }
        //如果未播放音乐则播放音乐，否则则停止播放
        if (playMusicService.isPlay) {
            playMusicService.stopPlayMusic();
        } else {
            index = playMusicService.nowPlayPosition;
            if (index == -1) {
                playSwitch.setChecked(false);
                ToastUtils.Short(this, "请选择播放的音乐！");
            } else {
                playMusicService.startPlayMusic(index);
            }
        }
    }

    /**
     * 更改底部功能栏
     *
     * @param music
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeBottomStyle(Music music) {
        setBottomBarMusicMessage(music);
    }

    /**
     * 权限获取成功
     *
     * @param requestCode
     */
    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        requestPermissionResult(requestCode, true);
    }

    /**
     * 权限获取失败
     *
     * @param requestCode
     */
    @Override
    public void permissionFail(int requestCode) {
        super.permissionFail(requestCode);
        requestPermissionResult(requestCode, false);
    }

    /**
     * 申请结果
     *
     * @param code
     * @param flag
     */
    private void requestPermissionResult(int code, boolean flag) {
        switch (code) {
            case 1000:
                if (flag) {//获取权限成功
                    initialization();
                } else {//获取权限失败
                    ToastUtils.Short(this, "获取权限失败，无法扫描本地歌曲！");
                }
                break;
        }
    }

    /**
     * 退出按钮
     */
    /**
     * 按两次退出按钮退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            // System.currentTimeMillis()无论何时调用，肯定大于2000
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().register(this);
        unbindService(this);
    }

    /**
     * 当与service的连接建立后被调用
     *
     * @param name
     * @param service
     */
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        isBind = true;
        PlayMusicService.MusicBinder musicBinder = (PlayMusicService.MusicBinder) service;
        playMusicService = musicBinder.getService();
        playMusicService.setLrcVew(musicLrc);
    }

    /**
     * 当与service的连接意外断开时被调用
     *
     * @param name
     */
    @Override
    public void onServiceDisconnected(ComponentName name) {
        isBind = false;
    }
}
