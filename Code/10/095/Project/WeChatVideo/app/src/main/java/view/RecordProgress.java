package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
   进度条
 */
public class RecordProgress extends View {

    private Paint paint = new Paint();               //创建画笔
    private boolean isStart = false;               //进度条是否启动的标记
    private int recordTime = 10 * 1000;             //默认最长录制时间是10秒
    private int progressColor = Color.BLUE;       //默认进度条是黑色
    private long startTime;                         //启动进度条的时间
    private Context lcontext;

    public RecordProgress(Context context) {
        super(context, null);
    }

    public RecordProgress(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public RecordProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        lcontext = context;
        init();
    }

    /**
     * 初始化 进度条的画笔
     */
    private void init() {
        paint.setStyle(Paint.Style.FILL);      //设置画笔填充样式
        paint.setColor(progressColor);       //设置画笔颜色
        stop();                                  //停止进度条
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long currTime = System.currentTimeMillis();     //获取当前时间
        if (isStart) {                                //如果进度条启动
            int measureWidth = getMeasuredWidth();      //进度条长度
            float mSpeed = measureWidth / 2.0f / recordTime;//进度条速度
            float durTime = (currTime - startTime);//时间间隔
            float dist = mSpeed * durTime;//根据时间行走的距离

            if (dist < measureWidth / 2.0f) {//判断是否到达终点
                canvas.drawRect(dist, 0.0f, measureWidth - dist, getMeasuredHeight(), paint);//绘制进度条
                invalidate();                //刷新
                return;
            } else {
                stop();                     //停止进度条
            }
        }
        canvas.drawRect(0.0f, 0.0f,0.0f, getMeasuredHeight(), paint);
    }

    /**
     * 开启进度条
     */
    public void start() {
        isStart = true;                                //设置显示进度条标记
        startTime = System.currentTimeMillis();        //获取进度条启动时间
        invalidate();                                    //刷新
        setVisibility(VISIBLE);                         //显示进度条
    }

    /**
     * 停止进度条
     */
    public void stop() {
        isStart = false;                       //设置隐藏进度条标记
        setVisibility(INVISIBLE);               //隐藏进度条
    }


    /**
     * 设置进度条颜色
     */
    public void setProgressColor(int progressColor) {
        progressColor = progressColor;
        paint.setColor(progressColor);
    }


    /**
     *设置最长的录制时间
     */
    public void setRecordTime(int recordTime) {
        recordTime = recordTime * 1000;
    }
}
