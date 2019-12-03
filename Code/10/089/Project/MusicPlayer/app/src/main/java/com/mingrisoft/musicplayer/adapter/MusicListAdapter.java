package com.mingrisoft.musicplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mingrisoft.musicplayer.R;
import com.mingrisoft.musicplayer.bean.Music;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author LYJ
 * Created on 2017/1/16.
 * Time 11:30
 */

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicListHolder> {

    private List<Music> musicList;
    private LayoutInflater inflater;
    private OnItemClickListener listener;
    /**
     * 回调接口
     */
    public interface OnItemClickListener{
        void onItemClick(View itemView,int position);
    }

    /**
     * 传入接口实现类对象
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    /**
     * 构造
     * @param context
     * @param musicList
     */
    public MusicListAdapter(Context context, List<Music> musicList) {
        this.inflater = LayoutInflater.from(context);
        this.musicList = musicList;
    }

    /**
     * 创建视图
     */
    @Override
    public MusicListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_item_layout, parent, false);
        MusicListHolder holder = new MusicListHolder(itemView);
        return holder;
    }

    /**
     * 绑定控件
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MusicListHolder holder, int position) {
        Music music = musicList.get(position);
        holder.song.setText(music.getMusic_name());//显示歌名
        String singerName = music.getMusic_singer();
        holder.singer.setText(singerName == null? "未知歌手" : singerName);//显示歌手
        holder.number.setText(String.valueOf(position + 1));//显示序号
        //设置item点击事件
        if (listener != null){
            holder.itemView.setOnClickListener(
                    v -> listener.onItemClick(v,position));
        }
    }

    /**
     * 数据的长度
     * @return
     */
    @Override
    public int getItemCount() {
        return musicList.size();
    }

    /**
     * 复用类
     */
    static class MusicListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_number)
        TextView number;
        @BindView(R.id.item_song)
        TextView song;
        @BindView(R.id.item_singer)
        TextView singer;
        public MusicListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
