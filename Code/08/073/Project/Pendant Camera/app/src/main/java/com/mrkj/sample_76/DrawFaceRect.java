package com.mrkj.sample_76;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import java.util.List;

public class DrawFaceRect {


    /**
     * 绘制人脸检测框
     *
     * @param canvas      给定的画布
     * @param face        需要绘制的人脸信息
     * @param width       原图宽
     * @param frontCamera 是否为前置摄像头，如为前置摄像头需左右对称
     */
    static public void drawFaceRect(int type, List<Bitmap> bitmapList, Canvas canvas, FaceRect face, int width, boolean frontCamera) {
        if (canvas == null) {
            return;
        }

        Paint paint = new Paint(); //创建画笔对象
        paint.setColor(Color.GREEN);//设置画笔的颜色
        Paint paintBitmap = new Paint();
        paintBitmap.setAntiAlias(true);
        paintBitmap.setDither(true);
        int len = (face.bound.bottom - face.bound.top) / 8;
        if (len / 8 >= 2) {
            paint.setStrokeWidth(len / 8);//设置画笔的粗度
        } else {
            paint.setStrokeWidth(2);//设置画笔的粗度
        }
        Rect rect = face.bound;//获取人脸区域
        if (frontCamera) {
            int top = rect.top;
            rect.top = width - rect.bottom;
            rect.bottom = width - top;
        }
        int drawL = rect.left - len;
        int drawR = rect.right + len;
        int drawU = rect.top - len;
        int drawD = rect.bottom + len;
        //绘制人脸识别框，每两个一组
        /**
         * 左下尖角
         * */
//        canvas.drawLine(drawL, drawD, drawL, drawD - len, paint);
//        canvas.drawLine(drawL, drawD, drawL + len, drawD, paint);
        /**
         * 右下尖角
         * */
//        canvas.drawLine(drawR, drawD, drawR, drawD - len, paint);
//        canvas.drawLine(drawR, drawD, drawR - len, drawD, paint);
        /**
         * 左上尖角
         * */
//        canvas.drawLine(drawL, drawU, drawL, drawU + len, paint);
//        canvas.drawLine(drawL, drawU, drawL + len, drawU, paint);
        /**
         * 右上尖角
         * */
//        canvas.drawLine(drawR, drawU, drawR, drawU + len, paint);
//        canvas.drawLine(drawR, drawU, drawR - len, drawU, paint);

        /**
         * 绘制人脸监测点
         */
        int[] a = new int[21];
        int[] b = new int[21];
        int i = 0;
        Rect dst = new Rect();
        Rect dst1 = new Rect();
        Rect dst2 = new Rect();
        if (face.point != null) {
            //遍历检测点，并绘制
            for (Point p : face.point) {
                if (frontCamera) {
                    p.y = width - p.y;
                }
                a[i] = p.x;
                b[i] = p.y;
                i++;
            }
        }

        CleanCanvas(canvas);  //清空画布

        switch (type) {
            case 1:   //哭的表情
                            /**
                             * 设置矩形大小
                             * */
                            dst.left = a[17] - 50;
                            dst.top = b[17] - 100;
                            dst.right = a[17] + 150;
                            dst.bottom = b[17] + 100;
                            dst1.left = a[16] - 150;
                            dst1.top = b[16] - 100;
                            dst1.right = a[16] + 50;
                            dst1.bottom = b[16] + 100;
                            dst2.left = a[18] - 80;
                            dst2.top = b[18] - 120;
                            dst2.right = a[18] + 80;
                            dst2.bottom = b[18] + 80;

                            canvas.drawBitmap(bitmapList.get(0), null, dst2, paintBitmap);  //绘制鼻子
                            canvas.drawBitmap(bitmapList.get(1), null, dst, paintBitmap);  //绘制左眼
                            canvas.drawBitmap(bitmapList.get(2), null, dst1, paintBitmap);  //绘制右眼
                break;
            case 2:  //猫耳朵及皇冠
                /**
                 * 设置矩形大小
                 * */
                        dst.left = (drawL + drawR) / 2 - 150;
                        dst.top = drawU - 100;
                        dst.right = (drawL + drawR) / 2 + 150;
                        dst.bottom = drawU + 100;
                        dst1.left = drawL;
                        dst1.top = drawU - 100;
                        dst1.right = drawL + 250;
                        dst1.bottom = drawU + 100;
                        dst2.left = drawR - 250;
                        dst2.top = drawU - 100;
                        dst2.right = drawR;
                        dst2.bottom = drawU + 100;
                        canvas.drawBitmap(bitmapList.get(3), null, dst,  paintBitmap);  //绘制皇冠
                        canvas.drawBitmap(bitmapList.get(4), null, dst1, paintBitmap);  //绘制左耳
                        canvas.drawBitmap(bitmapList.get(5), null, dst2, paintBitmap);  //绘制右耳
                break;
            case 3:   //帽子
                /**
                 * 设置矩形大小
                 * */
                dst.left = drawR - 330;
                dst.top = drawU - 200;
                dst.right = drawR + 50;
                dst.bottom = drawU + 150;
                canvas.drawBitmap(bitmapList.get(6), null, dst, paintBitmap);  //绘制帽子
                break;
            case 4:  //眼镜
                /**
                 * 设置矩形大小
                 * */
                dst.left = drawL + 30;
                dst.top = b[4] - 80;
                dst.right = drawR - 50;
                dst.bottom = b[17] + 120;
                canvas.drawBitmap(bitmapList.get(8), null, dst, paintBitmap);  //绘制帽子
                break;
            case 5:  //胡子
                /**
                 * 设置矩形大小
                 * */
                dst.left = a[11] - 80;
                dst.top = b[11] - 50;
                dst.right = a[11] + 80;
                dst.bottom = b[11] + 80;
                canvas.drawBitmap(bitmapList.get(7), null, dst, paintBitmap);  //绘制胡子
                break;
        }
    }

    //                /------------------------------------\
    //              /                                       \
    //             |    5    4   3           2   1   0       \
    //             |     9-17-8              7-16-6           |
    //             |                                          |
    //             |                                          |
    //             |                  18  （鼻尖）             |
    //             |              12      10                  |
    //             |                  11                      |
    //             |                                          |
    //             |                 13 （上嘴唇）             |
    //             |        20-------14-------19 （嘴角）      |
    //             |                 15  （下嘴唇）            |
    //              \                                        /
    //               \  ---------------------------------- /

    /**
     * 旋转人脸识别框
     *
     * @param r
     * @param height
     * @return
     */
    static public Rect RotateDeg90(Rect r, int height) {
        int left = r.left;
        r.left = height - r.bottom;
        r.bottom = r.right;
        r.right = height - r.top;
        r.top = left;
        return r;
    }

    /**
     * 旋转人脸识别点
     *
     * @param p
     * @param height
     * @return
     */
    static public Point RotateDeg90(Point p, int height) {
        int x = p.x;
        p.x = height - p.y;
        p.y = x;
        return p;
    }

    /**
     * 清空画布，防止程序无响应
     */
    public static void CleanCanvas(Canvas canvas) {
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }
}
