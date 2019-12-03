package com.mingrisoft.alarmclock.scroll;

import android.graphics.Color;

public class ViewColorGenerator {
    private static final ColorRange[] colorRanges = {
            new ColorRange("ff000109", "ff112d43"), // 00:00
            new ColorRange("ff000109", "ff112d43"), // 01:00
            new ColorRange("ff070d32", "ff32526b"), // 02:00
            new ColorRange("ff070d32", "ff32526b"), // 03:00
            new ColorRange("ff070d32", "ff32526b"), // 04:00
            new ColorRange("ff0f325a", "ff9975bf"), // 05:00
            new ColorRange("ff0f325a", "ff9975bf"), // 06:00
            new ColorRange("ff0f325a", "ff9975bf"), // 07:00
            new ColorRange("ff0f325a", "ff9975bf"), // 08:00
            new ColorRange("ff0f325a", "ff9975bf"), // 09:00
            new ColorRange("ff3ccbe9", "fffde4c6"), // 10:00
            new ColorRange("ff3ccbe9", "fffde4c6"), // 11:00
            new ColorRange("ff3ccbe9", "fffde4c6"), // 12:00
            new ColorRange("ff3ccbe9", "fffde4c6"), // 13:00
            new ColorRange("ff3ccbe9", "fffde4c6"), // 14:00
            new ColorRange("ff3ccbe9", "fffde4c6"), // 15:00
            new ColorRange("ff784f84", "ffef9786"), // 16:00
            new ColorRange("ff784f84", "ffef9786"), // 17:00
            new ColorRange("ff784f84", "ffef9786"), // 18:00
            new ColorRange("ff784f84", "ffef9786"), // 19:00
            new ColorRange("ff070d32", "ff32526b"), // 20:00
            new ColorRange("ff070d32", "ff32526b"), // 21:00
            new ColorRange("ff000109", "ff112d43"), // 22:00
            new ColorRange("ff000109", "ff112d43"), // 23:00
    };

    public static ColorRange getColorRange(int hour) {
        return colorRanges[hour];
    }



    public static class ColorRange {
        private ColorRange(String start, String end) {
            this.start = start;
            this.end = end;
        }

        public String getStart() {
            return start;
        }

        public String getEnd() {
            return end;
        }

        private String start;
        private String end;
    }



    public static int[] getTopBtmColor(TimeInDay time) {

        ColorRange colorRange = getColorRange(time.getHour());
        ColorRange finalColorRange = getColorRange(time.getHour()+1 > 23 ? 0 : time.getHour()+1);
        Argb startTop = new Argb(colorRange.getStart());
        Argb startBottom = new Argb(colorRange.getEnd());

        Argb finalTop = new Argb(finalColorRange.getStart());
        Argb finalBottom = new Argb(finalColorRange.getEnd());



        int curTopA = ((finalTop.getA() - startTop.getA()) * time.getMin()) / 60 + startTop.getA();
        int curTopR = ((finalTop.getR() - startTop.getR()) * time.getMin()) / 60 + startTop.getR();
        int curTopG = ((finalTop.getG() - startTop.getG()) * time.getMin()) / 60 + startTop.getG();
        int curTopB = ((finalTop.getB() - startTop.getB()) * time.getMin()) / 60 + startTop.getB();

        int top = Color.argb(curTopA, curTopR, curTopG, curTopB);

        int curBtmA = ((finalBottom.getA() - startBottom.getA()) * time.getMin()) / 60 + startBottom.getA();
        int curBtmR = ((finalBottom.getR() - startBottom.getR()) * time.getMin()) / 60 + startBottom.getR();
        int curBtmG = ((finalBottom.getG() - startBottom.getG()) * time.getMin()) / 60 + startBottom.getG();
        int curBtmB = ((finalBottom.getB() - startBottom.getB()) * time.getMin()) / 60 + startBottom.getB();

        int btm = Color.argb(curBtmA, curBtmR, curBtmG, curBtmB);

        return new int[]{top, btm};
    }
}
