package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.mingrisoft.pedometer.MainActivity;
import com.mingrisoft.pedometer.R;


public class CircularRingPercentageView extends View {
    private Paint paint;                    //定义画笔
    private int circleWidth;               //定义圆形进度条直径
    private int roundBackgroundColor;     //渐变背景色
    private int textColor;                 //刻度字体颜色
    private float textSize;                //刻度字体大小
    private float roundWidth;              //进度条的宽度
    private float progress = 0;            //进度
    private int[] colors = {0xffff4639, 0xffCDD513, 0xff3CDF5F};    //颜色
    private int radius;                     //圆环的半径
    private RectF oval;                      //进度条与中心的位置
    private Paint mPaintText;               //绘制刻度的画笔
    private int maxColorNumber = 100;      //分割数量
    private float singlPoint ;             //两个间隔块之间的距离
    private float lineWidth = 0.3f;        //间隔块的宽度
    private int circleCenter;              //圆环半径
    private SweepGradient sweepGradient;   //渐变色位置
    private boolean isLine;                //是否绘制间隔块
    private float tarGet = 6000;           //设置默认目标步数

    /**
     * 设置目标的步数
     */
    public void setTarGet(Float tarGet) {
        this.tarGet = tarGet;
        invalidate();
    }

    /**
     *返回默认与修改后的目标步数
     */
    public float getTarGet() {
        return tarGet;
    }




    /**
     * 是否是线条
     *
     * @param line true 是 false否
     */
    public void setLine(boolean line) {
        isLine = line;
        invalidate();
    }

    /**
     * 获取圆形进度条直径
     */
    public int getCircleWidth() {
        return circleWidth;
    }

    public CircularRingPercentageView(Context context) {
        this(context, null);
    }

    public CircularRingPercentageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CircularRingPercentageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularRing);
        maxColorNumber = mTypedArray.getInt(R.styleable.CircularRing_circleNumber, 100);
        circleWidth = mTypedArray.getDimensionPixelOffset(R.styleable.CircularRing_circleWidth, getDpValue(280));
        roundBackgroundColor = mTypedArray.getColor(R.styleable.CircularRing_roundColor, 0xffdddddd);
        textColor = mTypedArray.getColor(R.styleable.CircularRing_circleTextColor, 0xff999999);
        roundWidth = mTypedArray.getDimension(R.styleable.CircularRing_circleRoundWidth, 40);
        textSize = mTypedArray.getDimension(R.styleable.CircularRing_circleTextSize, getDpValue(8));
        colors[0] = mTypedArray.getColor(R.styleable.CircularRing_circleColor1, 0xffff4639);
        colors[1] = mTypedArray.getColor(R.styleable.CircularRing_circleColor2, 0xffcdd513);
        colors[2] = mTypedArray.getColor(R.styleable.CircularRing_circleColor3, 0xff3cdf5f);
        initView();
        mTypedArray.recycle();
    }


    /**
     * 空白出颜色背景
     *
     * @param roundBackgroundColor
     */
    public void setRoundBackgroundColor(int roundBackgroundColor) {
        this.roundBackgroundColor = roundBackgroundColor;
        paint.setColor(roundBackgroundColor);
        invalidate();
    }

    /**
     * 刻度字体颜色
     *
     * @param textColor
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
        mPaintText.setColor(textColor);
        invalidate();
    }

    /**
     * 刻度字体大小
     *
     * @param textSize
     */
    public void setTextSize(float textSize) {
        this.textSize = textSize;
        mPaintText.setTextSize(textSize);
        invalidate();
    }

    /**
     * 渐变颜色
     *
     * @param colors
     */
    public void setColors(int[] colors) {
        if (colors.length < 2) {
            throw new IllegalArgumentException("colors length < 2");
        }
        this.colors = colors;
        sweepGradientInit();
        invalidate();
    }


    /**
     * 间隔角度大小
     *
     * @param lineWidth
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        invalidate();
    }


    private int getDpValue(int w) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                w, getContext().getResources().getDisplayMetrics());
    }

    /**
     * 圆环宽度
     *
     * @param roundWidth 宽度
     */
    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
        if (roundWidth > circleCenter) {
            this.roundWidth = circleCenter;
        }
        radius = (int) (circleCenter - this.roundWidth / 2); // 圆环的半径
        oval.left = circleCenter - radius;                    //圆环左侧位置
        oval.right = circleCenter + radius;                   //圆环右侧
        oval.bottom = circleCenter + radius;                  //圆环底部
        oval.top = circleCenter - radius;                     //圆环上面
        paint.setStrokeWidth(this.roundWidth);
        invalidate();
    }

    /**
     * 圆环的直径
     *
     * @param circleWidth 直径
     */
    public void setCircleWidth(int circleWidth) {
        this.circleWidth = circleWidth;
        circleCenter = circleWidth / 2;

        if (roundWidth > circleCenter) {
            roundWidth = circleCenter;
        }
        setRoundWidth(roundWidth);
        sweepGradient = new SweepGradient(this.circleWidth / 2, this.circleWidth / 2, colors, null);
        //旋转 不然是从0度开始渐变
        Matrix matrix = new Matrix();
        matrix.setRotate(-90, this.circleWidth / 2, this.circleWidth / 2);
        sweepGradient.setLocalMatrix(matrix);
    }

    /**
     * 渐变初始化
     */
    public void sweepGradientInit() {
        //渐变颜色位置
        sweepGradient = new SweepGradient(this.circleWidth / 2, this.circleWidth / 2, colors, null);
        //旋转 不然是从0度开始渐变
        Matrix matrix = new Matrix();
        matrix.setRotate(-90, this.circleWidth / 2, this.circleWidth / 2);
        sweepGradient.setLocalMatrix(matrix);
    }

    public void initView() {

        circleCenter = circleWidth / 2;//半径
        singlPoint = (float) 360 / (float) maxColorNumber;   //两个间隔块之间的距离
        radius = (int) (circleCenter - roundWidth / 2);      //圆环的半径
        sweepGradientInit();                                    //渐变初始化
        mPaintText = new Paint();                              //刻度文字画笔
        mPaintText.setColor(textColor);                       //画笔颜色
        mPaintText.setTextAlign(Paint.Align.CENTER);          //排列位置
        mPaintText.setTextSize(textSize);                     //文字大小
        mPaintText.setAntiAlias(true);                        //开启抗锯齿

        paint = new Paint();                                  //渐变色画笔
        paint.setColor(roundBackgroundColor);               //背景色
        paint.setStyle(Paint.Style.STROKE);                   //画笔样式
        paint.setStrokeWidth(roundWidth);                     //宽度
        paint.setAntiAlias(true);                             //抗锯齿

        // 用于定义的圆弧的形状和大小的界限
        oval = new RectF(circleCenter - radius, circleCenter - radius, circleCenter + radius, circleCenter + radius);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float share = 360 / tarGet;             //根据步数计算进度
        int scale = (int) tarGet;               //根据步数显示刻度值
        paint.setShader(sweepGradient);        //背景渐变颜色
        //绘制渐变色
        canvas.drawArc(oval, -90, (progress * share), false, paint);
        paint.setShader(null);
        //是否是线条模式
        if (!isLine) {
            float start = -90f;                                 //起始角度
            float p = ((float) maxColorNumber / tarGet);     //计算一步的进度
            p = (int) (progress * p);                          //当前步数的进度
            for (int i = 0; i < p; i++) {
                paint.setColor(roundBackgroundColor);        //设置画笔颜色
                canvas.drawArc(oval, start + singlPoint - lineWidth,
                        lineWidth, false, paint);             // 绘制间隔块
                start = (start + singlPoint);                  //下一个起始位置
                postInvalidate();                               //刷新
            }
        }
        //绘制剩下的空白区域
        paint.setColor(roundBackgroundColor);
        canvas.drawArc(oval, -90, (-(tarGet - progress) * share), false, paint);
        //绘制文字刻度
        for (int i = 1; i <= 10; i++) {
            canvas.save();// 保存当前画布
            canvas.rotate(360 / 10 * i, circleCenter, circleCenter);
            canvas.drawText(i * (scale / 10) + "", circleCenter,
                    circleCenter - radius + roundWidth / 2 +
                            getDpValue(4) + textSize, mPaintText);
            canvas.restore();//恢复画布
        }
    }


    /**
     *设置进度的方法
     */
    public synchronized void setProgress(float p) {
        progress = p;
        postInvalidate();
    }


}
