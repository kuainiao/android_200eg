
package com.mingrisoft.lucky;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import java.util.ArrayList;

public class PrizeView extends View {

    private int defaultW = 200;//默认宽度
    private int defaultH = 200;//默认高度
    private Paint paint;//画笔
    public ValueAnimator animator;//动画
    public Bitmap background;//背景图
    public Bitmap centerBitmap;//指针图片
    public Bitmap present1;//代表礼品1
    public Bitmap present2;//代表礼品2
    public Bitmap present3;//代表礼品3
    public Bitmap present4;//代表礼品4
    public Bitmap present5;//代表礼品5
    public Bitmap present6;//代表礼品6
    public PrizeView(Context context) {
        this(context,null);
    }
    public PrizeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public PrizeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    @SuppressLint("NewApi")
    public void init() {
        paint = new Paint();//创建画笔
        paint.setAntiAlias(true);//设置抗锯齿
        paint.setStyle(Style.FILL);//设置画笔的风格
        paint.setColor(Color.YELLOW);//设置画笔颜色
        paint.setStrokeWidth(3);
        addData();//添加数据
        //设置动画
        animator = ValueAnimator.ofInt(0,360);//创建动画
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //设置旋转角度
                startAngle = (Integer) animation.getAnimatedValue();
                invalidate();//重绘
            }
        });
        animator.setInterpolator(new LinearInterpolator());//设置为线性插值器
        animator.setDuration(500);//设置动画事件500ms
        animator.setRepeatMode(ValueAnimator.RESTART);//设置动画循环模式
        animator.setRepeatCount(ValueAnimator.INFINITE);//设置动画循环播放
    }
    /**
     * 测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }
    /**
     * 绘制
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        screenW = getMeasuredWidth();
        screenH = getMeasuredHeight();
        int centerX = screenH / 2;//获取中心X坐标
        int centerY = screenH / 2;//获取中心Y坐标
        float dis = screenH / 3f;
        paint.setStyle(Style.FILL);//设置画笔的样式
        RectF dst = new RectF(0, 0, screenW, screenH);//设置区域
        RectF arcRect = new RectF(distanceR, distanceR, screenW - distanceR, screenH - distanceR);
        canvas.drawBitmap(background, null, dst, paint);//绘制背景
        for (int i = 0; i < list.size();i++){
            //绘制礼物
            drawPrize(canvas, centerX, centerY, dis, arcRect,i);
        }
        //绘制指针图片
        canvas.drawBitmap(centerBitmap, centerX - centerBitmap.getWidth() / 2,
                centerY - centerBitmap.getHeight() / 2, paint);
    }

    /**
     * 绘制礼物的位置
     * @param canvas
     * @param centerX
     * @param centerY
     * @param dis
     * @param arcRect
     * @param index
     */
    private void drawPrize(Canvas canvas, int centerX, int centerY,
                           float dis, RectF arcRect,int index) {
        int color = (index + 1)% 2 == 0 ? 0xFF88C7EF:0xFF56DB72;
        paint.setColor(color);//设置画笔的颜色
        //绘制扇形
        canvas.drawArc(arcRect, startAngle + index *
                sweepAngle, sweepAngle, true, paint);
        //获取旋转角度
        double radians = Math.toRadians(startAngle +
                sweepAngle / 2 + index * sweepAngle);
        //获取图片绘制的X起始坐标
        int x = (int) (centerX + dis * Math.cos(radians)) -
                prizeBitmap.get(index).getWidth() / 2;
        //获取图片绘制的Y起始坐标
        int y = (int) (centerY + dis * Math.sin(radians)) -
                prizeBitmap.get(index).getHeight() / 2;
        //绘制礼品图片
        canvas.drawBitmap(list.get(index).bmp, x, y, paint);
        //更新位置记录
        list.get(index).startAngle = startAngle + index * sweepAngle;
        list.get(index).endAngle =  list.get(index).startAngle + sweepAngle;
    }


    /**
     * 添加数据
     */
    private void addData() {
        background = BitmapFactory.decodeResource(getResources(), R.drawable.circle);
        centerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.start);

        present1 = BitmapFactory.decodeResource(getResources(), mImgs[0]);
        present2 = BitmapFactory.decodeResource(getResources(), mImgs[1]);
        present3 = BitmapFactory.decodeResource(getResources(), mImgs[2]);
        present4 = BitmapFactory.decodeResource(getResources(), mImgs[3]);
        present5 = BitmapFactory.decodeResource(getResources(), mImgs[4]);
        present6 = BitmapFactory.decodeResource(getResources(), mImgs[5]);
        prize1.bmp = present1;
        prize2.bmp = present2;
        prize3.bmp = present3;
        prize4.bmp = present4;
        prize5.bmp = present5;
        prize6.bmp = present6;
        list.add(prize1);
        list.add(prize2);
        list.add(prize3);
        list.add(prize4);
        list.add(prize5);
        list.add(prize6);
        prizeName.add("Android入门");
        prizeName.add("JavaWeb入门");
        prizeName.add("谢谢参与");
        prizeName.add("系列光盘");
        prizeName.add("Android入门");
        prizeName.add("谢谢参与");
        prizeBitmap.add(present1);
        prizeBitmap.add(present2);
        prizeBitmap.add(present3);
        prizeBitmap.add(present4);
        prizeBitmap.add(present5);
        prizeBitmap.add(present6);
    }

    /**
     * 设置控件的宽度
     * @param measureSpec
     * @return
     */
    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) (defaultW + getPaddingLeft()
                    + getPaddingRight());
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 设置控件高度
     * @param measureSpec
     * @return
     */
    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultH + getPaddingTop()
                    + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    public int screenW = 0;
    public int screenH = 0;
    public int startAngle = 50;
    public int distanceR = 50;
    public boolean bRunning = false;
    @SuppressLint("NewApi")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (bRunning) {
                    bRunning = false;
                    animator.cancel();
                    for(int i=0;i<list.size();i++){
                        if((list.get(i).endAngle%360) >=270 && (list.get(i).endAngle%360) <330){
                            if(listener != null){
                                listener.onSelect(prizeName.get(i));
                            }else{
                                Toast.makeText(getContext(), prizeName.get(i), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                    }
                }else{
                    bRunning = true;
                    animator.start();
                }
                break;
        }
        return true;
    }

    /**
     * 回调接口
     */
    public interface SelectListener{
        void onSelect(String str);
    }
    SelectListener listener = null;
    public void setOnSelectListener(SelectListener listener){
        this.listener = listener;
    }
    private int sweepAngle = 60;//设置每块代表的角度
    //图片资源数组
    private int[] mImgs = new int[] {
            R.drawable.android, R.drawable.javaweb,
            R.drawable.emoji, R.drawable.cd, R.drawable.android,
            R.drawable.emoji
    };

    /**
     * 礼物
     */
    static class Prize{
        public Bitmap bmp;
        public int startAngle;
        public int endAngle;
    }
    Prize prize1 = new Prize();
    Prize prize2 = new Prize();
    Prize prize3 = new Prize();
    Prize prize4 = new Prize();
    Prize prize5 = new Prize();
    Prize prize6 = new Prize();
    ArrayList<Prize> list = new ArrayList();
    ArrayList<String> prizeName = new ArrayList();
    ArrayList<Bitmap> prizeBitmap = new ArrayList<>();
}
