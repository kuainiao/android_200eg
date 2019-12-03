package com.mingrisoft.codecard.view;

/**
 * 作者： LYJ
 * 功能：
 * 创建日期： 2017/3/4
 */

public class KeyPoint {
    private int x;//坐标x
    private int y;//坐标y
    private int index;//索引值
    private float angle;//旋转角度

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
