package com.mingrisoft.musicplayer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author LYJ
 * Created on 2017/1/16.
 * Time 11:40
 */

public class Music implements Parcelable{
    private String music_name;
    private String music_singer;
    private String music_path;
    private long music_id;
    private int music_albumId;
    private String music_album;

    public Music (){}
    protected Music(Parcel in) {
        music_name = in.readString();
        music_singer = in.readString();
        music_path = in.readString();
        music_id = in.readLong();
        music_albumId = in.readInt();
        music_album = in.readString();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    public String getMusic_name() {
        return music_name;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }

    public String getMusic_singer() {
        return music_singer;
    }

    public void setMusic_singer(String music_singer) {
        this.music_singer = music_singer;
    }

    public String getMusic_path() {
        return music_path;
    }

    public void setMusic_path(String music_path) {
        this.music_path = music_path;
    }

    public long getMusic_id() {
        return music_id;
    }

    public void setMusic_id(long music_id) {
        this.music_id = music_id;
    }

    public int getMusic_albumId() {
        return music_albumId;
    }

    public void setMusic_albumId(int music_albumId) {
        this.music_albumId = music_albumId;
    }

    public String getMusic_album() {
        return music_album;
    }

    public void setMusic_album(String music_album) {
        this.music_album = music_album;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(music_name);
        dest.writeString(music_singer);
        dest.writeString(music_path);
        dest.writeLong(music_id);
        dest.writeInt(music_albumId);
        dest.writeString(music_album);
    }

}
