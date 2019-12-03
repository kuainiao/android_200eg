package com.mingrisoft.selectimagecode.code;

import android.graphics.Bitmap;

/**
 * Author LYJ
 * Created on 2017/1/22.
 * Time 17:48
 */

public class Code {
    private Bitmap bitmap;//图片
    private String tag;//标记
    private int id;//id

    public Code(Bitmap bitmap, String tag, int id) {
        this.bitmap = bitmap;
        this.tag = tag;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
