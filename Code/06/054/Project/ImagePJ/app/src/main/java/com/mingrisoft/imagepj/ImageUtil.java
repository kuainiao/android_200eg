package com.mingrisoft.imagepj;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Administrator on 2016/11/29 0029.
 */

public class ImageUtil {

    /**
     * 横向拼接
     * <功能详细描述>
     * @param first 第一张图片
     * @param second 第二张图片
     * @return
     */
    public static Bitmap add2Bitmap(Bitmap first, Bitmap second) {
        int width = first.getWidth() + second.getWidth();
        int height = Math.max(first.getHeight(), second.getHeight());
        //创建一个新图高度和图片高度一样 宽度为2张图片和起来的图片
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //创建图片画布
        Canvas canvas = new Canvas(result);
        //绘制第一张图片
        canvas.drawBitmap(first, 0, 0, null);
        //绘制第二章图片
        canvas.drawBitmap(second, first.getWidth(), 0, null);
        return result;
    }


    /**
     * 纵向拼接
     * <功能详细描述>
     * @param first
     * @param second
     * @return
     */
    public static Bitmap addBitmap(Bitmap first, Bitmap second) {
        int width = Math.max(first.getWidth(),second.getWidth());
        int height = first.getHeight() + second.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, 0, first.getHeight(), null);
        return result;
    }
    //两张图上左右合并一张
    public static  Bitmap toConformBitmap(Bitmap first, Bitmap second) {
        if( first == null ) {
            return null;
        }
        int bgWidth = first.getWidth();
        int bgHeight = first.getHeight();
        int fgWidth = second.getWidth();
        int fgHeight = second.getHeight();
        //创建一个新的和SRC长度宽度一样的位图
        Bitmap newbmp = Bitmap.createBitmap(bgWidth+fgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newbmp);
        //绘制第一张图片
        cv.drawBitmap(first, 0, 0, null);//在 0，0坐标开始画入bg
        //绘制第二章图片
        cv.drawBitmap(second, bgWidth, 0, null);//在 0，0坐标开始画入fg ，可以从任意位置画入
        //保存新图
        cv.save(Canvas.ALL_SAVE_FLAG);//保存
        cv.restore();//存储
        return newbmp;
    }
}
