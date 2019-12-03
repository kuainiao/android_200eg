package com.mingrisoft.codecard.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.mingrisoft.codecard.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 作者： LYJ
 * 功能： 显示验证码
 * 创建日期： 2017/3/4
 */

public class CodeImageView extends View {
    private static final int DEFAULT_DEGREES = 15;//默认偏移角度
    private static final int OFFSET_VALUES = 1;//偏移量
    public static final int DEFAULT_COUNTS = 7;//默认数量
    private static final int DEFAULT_WIDTH = 300;//默认宽度
    private static final int DEFAULT_VALUES = 0;//0
    private int textSize = sp2px(DEFAULT_WIDTH / DEFAULT_COUNTS);//字体大小
    private int pointOffset = dp2px(OFFSET_VALUES);//设置默认的偏移量
    private int mWidth;//宽
    private int mHeight;//高
    private int startX = DEFAULT_VALUES;//起始X
    private int startY = DEFAULT_VALUES;//起始Y
    private Paint paint;//画笔
    private final Random random = new Random();//用来获取随机数
    private ArrayList<KeyPoint> points = new ArrayList<>();//记录绘制坐标中心点
    private boolean showKeyPoint;//显示标记点的开关
    private List<String> mCodeDatas;//验证码选择项的数据源
    private static int bitmapRes[] = new int[]{R.mipmap.mrkj_one,//图片资源ID
            R.mipmap.mrkj_two, R.mipmap.mrkj_three, R.mipmap.mrkj_four};
    private List<Rect> selectLayer = new ArrayList<>();//覆盖层
    private int mCodeSize;//验证码长度
    private int layerOffset = dp2px(5);//绘制覆盖物的偏移量
    private ArrayList<Rect> rects = new ArrayList<>();//点击区域
    private List<String> selectCodeStrs = new ArrayList<>();//选择的文字
    private Bitmap background;//背景
    public CodeImageView(Context context) {
        this(context, null);
    }

    public CodeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();//初始化操作
    }

    /**
     * 生成关键点
     */
    private void generateKeyPoint() {
        int counts = 0;
        while (counts < DEFAULT_COUNTS) {
            int x = random.nextInt(mWidth);
            int y = random.nextInt(mHeight);
            //边界判断
            if ((x + pointOffset + textSize / 2) < mWidth && (x - pointOffset - textSize / 2) > startX
                    && (y + pointOffset + textSize / 2) < mHeight && (y - pointOffset - textSize / 2) > startY) {
                //数据对比
                if (points.size() != DEFAULT_VALUES) {
                    if (!isLegal(x, y)) continue;//跳出循环
                }
                KeyPoint point = new KeyPoint();
                point.setIndex(counts);
                point.setX(x);
                point.setY(y);
                //生成随机偏移角度
                float rotateDegrees = ((random.nextInt(2) * 2) - 1)
                        * random.nextFloat() * DEFAULT_DEGREES;
                point.setAngle(rotateDegrees);//设置旋转角度
                points.add(point);
                int l = x - pointOffset - textSize / 2;
                int t = y - pointOffset - textSize / 2;
                int r = x + pointOffset + textSize / 2;
                int b = y + pointOffset + textSize / 2;
                rects.add(new Rect(l, t, r, b));//增加点击区域
                counts++;
            }
        }
    }

    /**
     * 判断数据是否合法
     *
     * @return
     */
    private boolean isLegal(int nowX, int nowY) {
        for (int i = 0; i < points.size(); i++) {
            KeyPoint point = points.get(i);
            int x = point.getX();
            int y = point.getY();
            if (Math.abs(nowX - x) < textSize + pointOffset * 2
                    && Math.abs(nowY - y) < textSize + pointOffset * 2) {
                return false;
            }
        }
        return true;
    }

    /**
     * 初始化
     */
    private void initialize() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);//设置油漆桶
        paint.setDither(true);//设置防抖动
        paint.setAntiAlias(true);//设置抗锯齿
        paint.setTextSize(textSize);//设置字体的大小
        paint.setTextAlign(Paint.Align.CENTER);//设置文字文字
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != background){
            canvas.drawBitmap(background,null,new Rect(startX,startY,mWidth,mHeight),null);
        }
        if (null == mCodeDatas) {
            paint.setColor(Color.BLACK);
            canvas.drawText("请设置数据源", mWidth / 2, getBaseLine(startY, mHeight), paint);
            return;
        }

        //标记点
        int count = 0;
        for (KeyPoint point : points) {
            canvas.save();//保存画布的当前状态
            //旋转画布
            canvas.rotate(point.getAngle(), point.getX(), point.getY());
            //绘制文字
            paint.setStrokeWidth(0);
            paint.setColor(Color.BLACK);
            Rect drawRect = rects.get(count);//获取绘制区域
            canvas.drawText(mCodeDatas.get(count), point.getX(), getBaseLine(drawRect.bottom, drawRect.top), paint);
            //绘制选择的验证文字
            if (showKeyPoint) {
                //绘制坐标点
                paint.setStrokeWidth(dp2px(5));
                paint.setColor(Color.YELLOW);
                canvas.drawPoint(point.getX(), point.getY(), paint);
                //绘制文字绘制区域
                paint.setColor(Color.YELLOW);
                paint.setStrokeWidth(dp2px(2));
                canvas.drawRect(drawRect.left, drawRect.top, drawRect.right, drawRect.bottom, paint);
            }
            count++;//资政
            canvas.restore();//恢复画布的状态
        }
        //绘制覆盖层
        if (null != selectLayer && selectLayer.size() > 0) {
            for (int i = 0; i < selectLayer.size(); i++) {
                Rect rect = selectLayer.get(i);//获取文字绘制的区域
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), bitmapRes[i]);//获取图片
                Rect drawRect = new Rect(rect.left + layerOffset, rect.top + layerOffset,
                        rect.right - layerOffset, rect.bottom - layerOffset);//设置惠子区域
                canvas.drawBitmap(bitmap, null, drawRect, null);//绘制覆盖物
            }
        }
    }

    /**
     * 设置背景图片
     * @param bitmap
     */
    public void setCodeBackground(Bitmap bitmap){
        this.background = bitmap;
    }
    /**
     * 设置是否显示绘制标记
     * @param showKeyPoint
     */
    public void setShowKeyPoint(boolean showKeyPoint) {
        this.showKeyPoint = showKeyPoint;
    }

    /**
     * 获取文字绘制基线
     *
     * @param bottom
     * @param top
     * @return
     */
    private int getBaseLine(int bottom, int top) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return (bottom + top - fontMetrics.bottom - fontMetrics.top) / 2;
    }

    /**
     * 测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = opinionSide(MeasureSpec.getMode(widthMeasureSpec), MeasureSpec.getSize(widthMeasureSpec));
        int height = width/2;//高度何为宽度的一半
        setMeasuredDimension(width, height);
    }

    /**
     * 测量
     *
     * @param mode
     * @param size
     * @return
     */

    private int opinionSide(int mode, int size) {
        int result = 0;
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            // 设置默认边长
            int defaultSize = dp2px(DEFAULT_WIDTH);
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(size, defaultSize);
            }
        }
        return result;
    }

    /**
     * 确定控件宽高
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;//获取宽度
        mHeight = h;//获取高度
        generateKeyPoint();//获取关键点坐标
    }


    /**
     * 重置
     */
    public void reset() {
        points.clear();//清空绘制坐标
        rects.clear();//清空区域数据
        selectLayer.clear();//清空选择覆盖层
        selectCodeStrs.clear();//清空选择的数据
        generateKeyPoint();//重新获取数据
        invalidate();
    }

    /**
     * 选额验证码
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            /**
             * 确定点击区域
             */
            for (int i = 0; i < DEFAULT_COUNTS; i++) {
                int l = rects.get(i).left;
                int t = rects.get(i).top;
                int r = rects.get(i).right;
                int b = rects.get(i).bottom;
                if (x < r && x > l && y > t && y < b) {
                    selectedEvent(i);//拼接
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 选中事件处理
     *
     * @param index
     */
    private void selectedEvent(int index) {
        //用于增加覆盖物
        Rect rect = rects.get(index);//获取文字点击区域
        boolean isAdded = selectLayer.contains(rect);//查找对象
        if (isAdded) {
            int position = selectLayer.indexOf(rect);//获取索引值
            selectLayer = selectLayer.subList(0, position);
            selectCodeStrs = selectCodeStrs.subList(0,position);
        } else {
            if (selectLayer.size() == mCodeSize) {//数据已经添满
                return;
            }
            selectLayer.add(rect);//增加覆盖物绘制数据
            selectCodeStrs.add(mCodeDatas.get(index));//增加选择的文字
        }
        //返回选择结果-->测试阶段
        if (null != selectedListener) {
            if (null != selectCodeStrs && selectCodeStrs.size() != 0){
                StringBuilder result = new StringBuilder();
                for (String str: selectCodeStrs){
                    result.append(str);
                }
                selectedListener.selectedCodeMessage(result.toString());
            }else {
                selectedListener. selectedCodeMessage("");
            }
        }
        invalidate();//重绘
    }

    /**
     * 设置选择验证码数据源
     *
     * @param datas
     */
    public void setSelectOptionsData(List<String> datas, int codeLength) {
        this.mCodeDatas = datas;
        this.mCodeSize = codeLength;
    }


    /**
     * 获取验证码选择结果
     */
    public interface OnCodeSelectedListener {
        void selectedCodeMessage(String codeStr);
    }

    private OnCodeSelectedListener selectedListener;

    public void setOnCodeSelectedListener(OnCodeSelectedListener onCodeSelectedListener) {
        this.selectedListener = onCodeSelectedListener;
    }

    /**
     * 密度转换
     *
     * @param values
     * @return
     */
    private int dp2px(int values) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                , values, getResources().getDisplayMetrics());
    }

    /**
     * 字体
     *
     * @param values
     * @return
     */
    private int sp2px(int values) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP
                , values, getResources().getDisplayMetrics());
    }
}
