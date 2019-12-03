package com.mingrisoft.alarmclock.scroll;

import android.graphics.Color;


public class Argb {
    private int a;
    private int r;
    private int g;
    private int b;

    public static boolean check(String color) {
        boolean result;
        if (color==null || color.isEmpty()) {
            result = false;
        } else if (color.length()!=8) {
            result = false;
        } else if (!isAllHexStr(color)) {
            result = false;
        } else if (!isColorValue(Integer.parseInt(color.substring(0,2), 16))
                || !isColorValue(Integer.parseInt(color.substring(2,4), 16))
                || !isColorValue(Integer.parseInt(color.substring(4,6), 16))
                || !isColorValue(Integer.parseInt(color.substring(6,8), 16))) {
            result = false;
        } else {
            result = true;
        }

        return result;
    }

    private static boolean isColorValue(int val) {
        if (val <= 255 && val >=0) {
            return true;
        } else {
            return false;
        }
    }
    private static boolean isAllHexStr(String numStr) {
        boolean result = true;
        for (int i=0; i<numStr.length(); i++) {
            char tmp = numStr.charAt(i);
            if (!((tmp>='0' && tmp<='9') || (tmp>='a' && tmp<='f') || (tmp>='A' && tmp<='F'))) {
                result = false;
            }
        }

        return result;
    }

    public Argb(String color) {
        if (check(color)) {
            a = Integer.parseInt(color.substring(0, 2), 16);
            r = Integer.parseInt(color.substring(2, 4), 16);
            g = Integer.parseInt(color.substring(4, 6), 16);
            b = Integer.parseInt(color.substring(6, 8), 16);
        }
    }
    public Argb(int a, int r, int g, int b) {
        if (isColorValue(a) && isColorValue(r) && isColorValue(g) && isColorValue(b)) {
            this.a = a;
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    public int colorValue() {
        return Color.argb(a, r, g, b);
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }
}
