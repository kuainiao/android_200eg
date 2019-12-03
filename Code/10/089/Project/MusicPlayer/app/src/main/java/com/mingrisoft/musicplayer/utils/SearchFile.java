package com.mingrisoft.musicplayer.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.mingrisoft.musicplayer.R;
import com.mingrisoft.musicplayer.bean.Music;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Author LYJ
 * Created on 2017/1/16.
 * Time 12:46
 */

public class SearchFile {

    /**
     * 扫描本地歌曲
     * @param context
     * @return
     */
    public static ArrayList<Music> searchLocaltion(Context context){
        ArrayList<Music> musicList = null;
        //扫描本地的歌曲
        Cursor cursor = context .getApplicationContext()
                                .getContentResolver()
                                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                        null, null, null,null);
        if(cursor!=null){
            musicList=new ArrayList<>();//该集合用来存放歌曲的信息
            while(cursor.moveToNext() ){//游标下移
                Music music;
                music=new Music();
                //获取歌曲名称
                String music_name=cursor.getString(
                        cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                //获取歌手名称
                String music_singer=cursor.getString(
                        cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                //获取歌曲路径
                String music_path=cursor.getString(
                        cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                //获取音乐ID
                long music_id = cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Audio.Media._ID));
                //获取专辑
                String music_album = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.ALBUM));
                //获取专辑ID
                int music_albumId = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                //是否为音乐
                int isMusic = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                if (isMusic != 0){//如果是音乐保存到集合中
                    music.setMusic_name(music_name);
                    music.setMusic_singer(music_singer);
                    music.setMusic_path(music_path);
                    music.setMusic_id(music_id);
                    music.setMusic_album(music_album);
                    music.setMusic_albumId(music_albumId);
                    musicList.add(music);
                }
            }
        }
        cursor.close();//关闭游标
        return musicList;
    }

    /**
     * 功能 通过album_id查找 album_art 如果找不到返回null
     * @param album_id
     * @return album_art
     */
    private static String getAlbumArt(Context context,int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cursor = context.getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
                projection, null, null, null);
        String album_art = null;
        if (cursor.getCount() > 0 && cursor.getColumnCount() > 0) {
            cursor.moveToNext();
            //该方法与
            // album_art = cursor.getString(cursor.getColumnIndex("album_art"));
            //是一样的
            album_art = cursor.getString(0);

        }
        cursor.close();
        return album_art;
    }

    /**
     * 通过专辑ID获取专辑图片
     * @param context
     * @param album_id
     * @return
     */
    public static Bitmap getAuthor(Context context, int album_id){
        Context mContext = context.getApplicationContext();
        String albumArt = getAlbumArt(mContext,album_id);
        if (albumArt == null) {
            return BitmapFactory.decodeResource(mContext
                    .getResources(), R.mipmap.music);
        } else {
            return BitmapFactory.decodeFile(albumArt);
        }
    }

    /**
     * 获取歌词文件
     * @param fileName
     * @return
     */
    public static String getLrcText(String fileName) {
        String lrcText = null;
        try {
            InputStream is = new FileInputStream(new File(fileName));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            lrcText = new String(buffer,"gbk");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lrcText;
    }
}
