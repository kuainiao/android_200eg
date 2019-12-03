package com.mingrisoft.clock;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.*;
import android.os.Handler;


public class MyClock extends View {
    private Time mCalendar;

    private Drawable mHourHand;// 时针
    private Drawable mMinuteHand;// 分针
    private Drawable mSecondHand;// 秒针
    private Drawable mDial;// 表盘

    private int mDialWidth;// 表盘宽度
    private int mDialHeight;// 表盘高度

    private final Handler mHandler = new Handler();
    private float mHour;// 时针值
    private float mMinutes;// 分针之
    private float mSecond;// 秒针值
    private boolean mChanged;// 是否需要更新界面

    private Paint mPaint;// 画笔

    private Runnable mTicker;// 由于秒针的存在，因此我们需要每秒钟都刷新一次界面，用的就是此任务

    private boolean mTickerStopped = false;// 是否停止更新时间，当View从窗口中分离时，不需要更新时间了

    public MyClock(Context context) {
        this(context, null);
    }

    public MyClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyClock(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Resources r = getContext().getResources();
        // 下面是从layout文件中读取所使用的图片资源，如果没有则使用默认的
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.AnalogClock, defStyle, 0);
        mDial = a.getDrawable(R.styleable.AnalogClock_dial);
        mHourHand = a.getDrawable(R.styleable.AnalogClock_hand_hour);
        mMinuteHand = a.getDrawable(R.styleable.AnalogClock_hand_minute);
        mSecondHand = a.getDrawable(R.styleable.AnalogClock_hand_second);

        // 为了整体美观性，只要缺少一张图片，我们就用默认的那套图片
        if (mDial == null || mHourHand == null || mMinuteHand == null
                || mSecondHand == null) {
            mDial = r.getDrawable(R.drawable.clock_bj);
            mHourHand = r.getDrawable(R.drawable.clock_hour);
            mMinuteHand = r.getDrawable(R.drawable.clock_fenzhen);
            mSecondHand = r.getDrawable(R.drawable.clock_miao);
        }
        a.recycle();// 不调用这个函数，则上面的都是白费功夫

        // 获取表盘的宽度和高度
        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();

        // 初始化画笔
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#3399ff"));
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setFakeBoldText(true);
        mPaint.setAntiAlias(true);

        // 初始化Time对象
        if (mCalendar == null) {
            mCalendar = new Time();
        }
    }

    /**
     * 时间改变时调用此函数，来更新界面的绘制
     */
    private void onTimeChanged() {
        mCalendar.setToNow();// 时间设置为当前时间
        // 下面是获取时、分、秒、日期和星期
        int hour = mCalendar.hour;
        int minute = mCalendar.minute;
        int second = mCalendar.second;
        mHour = hour + mMinutes / 60.0f + mSecond / 3600.0f;// 小时值，加上分和秒，效果会更加逼真
        mMinutes = minute + second / 60.0f;// 分钟值，加上秒，也是为了使效果逼真
        mSecond = second;
        mChanged = true;// 此时需要更新界面了
    }

    @Override
    protected void onAttachedToWindow() {
        // 添加到窗口中就要更新时间了
        mTickerStopped = false;
        super.onAttachedToWindow();
        //更新线程
        mTicker = new Runnable() {
            public void run() {
                //判断是否退出线程
                if (mTickerStopped)
                    //退出线程
                    return;
                //事件改改变方法
                onTimeChanged();
                //刷新控件
                invalidate();
                //获取当前系统时间
                long now = SystemClock.uptimeMillis();
                // 计算下次需要更新的时间间隔
                long next = now + (1000 - now % 1000);
                // 递归执行，就达到秒针一直在动的效果
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 当view从当前窗口中移除时，停止更新
        mTickerStopped = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 模式： UNSPECIFIED(未指定),父元素不对子元素施加任何束缚，子元素可以得到任意想要的大小；
        // EXACTLY(完全)，父元素决定自元素的确切大小，子元素将被限定在给定的边界里而忽略它本身大小；
        // AT_MOST(至多)，子元素至多达到指定大小的值。
        // 根据提供的测量值(格式)提取模式(上述三个模式之一)
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // 根据提供的测量值(格式)提取大小值(这个大小也就是我们通常所说的大小)
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        // 高度与宽度类似
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        // 缩放值
        float hScale = 1.0f;
        float vScale = 1.0f;
        // 如果父元素提供的宽度比图片宽度小，就需要压缩一下子元素的宽度
        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }
        // 如果父元素提供的高度比图片高度小，就需要压缩一下子元素的高度
        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float) heightSize / (float) mDialHeight;// 同上
        }
        // 取最小的压缩值，值越小，压缩越厉害
        float scale = Math.min(hScale, vScale);
        // 最后保存一下，这个函数一定要调用
        setMeasuredDimension(
                resolveSizeAndState((int) (mDialWidth * scale),
                        widthMeasureSpec, 0),
                resolveSizeAndState((int) (mDialHeight * scale),
                        heightMeasureSpec, 0));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }
     //绘制控件
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }
        int availableWidth = getRight() - getLeft();// view可用宽度，通过右坐标减去左坐标
        int availableHeight = getBottom() - getTop();// view可用高度，通过下坐标减去上坐标

        int x = availableWidth / 2;// view宽度中心点坐标
        int y = availableHeight / 2;// view高度中心点坐标

        final Drawable dial = mDial;// 表盘图片
        int w = dial.getIntrinsicWidth();// 表盘宽度
        int h = dial.getIntrinsicHeight();

        boolean scaled = false;
        // 最先画表盘，最底层的要先画上画板
        if (availableWidth < w || availableHeight < h) {// 如果view的可用宽高小于表盘图片，就要缩小图片
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w,
                    (float) availableHeight / (float) h);// 计算缩小值
            canvas.save();
            canvas.scale(scale, scale, x, y);// 实际上是缩小的画板
        }

        if (changed) {
            // 设置表盘图片位置。组件在容器X轴上的起点； 组件在容器Y轴上的起点； 组件的宽度；组件的高度
            dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        dial.draw(canvas);// 这里才是真正把表盘图片画在画板上
        canvas.save();// 一定要保存一下
        // 再画时针
        // 旋转画板，第一个参数为旋转角度，第二、三个参数为旋转坐标点
        canvas.rotate(mHour / 12.0f * 360.0f, x, y);
        final Drawable hourHand = mHourHand;
        if (changed) {
            w = hourHand.getIntrinsicWidth();
            h = hourHand.getIntrinsicHeight();
            hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y
                    + (h / 2));
        }
        hourHand.draw(canvas);// 把时针画在画板上
        canvas.restore();// 恢复画板到最初状态
        canvas.save();
        // 然后画分针
        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);
        final Drawable minuteHand = mMinuteHand;
        if (changed) {
            w = minuteHand.getIntrinsicWidth();
            h = minuteHand.getIntrinsicHeight();
            minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y
                    + (h / 2));
        }
        minuteHand.draw(canvas);
        canvas.restore();
        canvas.save();
        // 最后画秒针
        canvas.rotate(mSecond / 60.0f * 360.0f, x, y);
        final Drawable secondHand = mSecondHand;
        if (changed) {
            w = secondHand.getIntrinsicWidth();
            h = secondHand.getIntrinsicHeight();
            secondHand.setBounds(x - (w / 2), y - (h /2), x + (w / 2), y+ (h / 2)-4);
        }
        secondHand.draw(canvas);
        canvas.restore();

        if (scaled) {
            canvas.restore();
        }
    }
}
