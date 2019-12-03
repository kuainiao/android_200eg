package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.mingrisoft.weimain.R;


/**
 * Created by Administrator on 2016/11/23.
 */

public class ChangeIcon extends View{

    private int lColor = 0xFF45C01A;    //定义默认颜色
    private Bitmap lIconBitmap;         //定义图标获取参数的Bitmap
    private String lText = "微信";      //定义默认文字
    //定义文字大小
    private int lTextSize = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());

    private Canvas lCanvas;         //定义画布
    private Bitmap lBitmap;         //定义绘制渐变色的Bitmap
    private Paint lPaint;           //定义绘制渐变的画笔

    private float lAlpha;           //定义透明度

    private Rect mIconRect;         //定义图标绘制的位置
    private Rect lTextBound;        //定义文字绘制的位置
    private Paint lTextPaint;       //定义文字画笔

    public ChangeIcon(Context context)
    {
        this(context, null);
    }

    public ChangeIcon(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    /**
     * 获取自定义属性的值
    */
    public ChangeIcon(Context context, AttributeSet attrs,
                                   int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        //获取自定义控件所有属性值
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ChangeIcon);

        int n = a.getIndexCount();   //获取值下标
        //便利属性值
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {   //获取图标属性值
                case R.styleable.ChangeIcon_icon:
                    BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(attr);
                    lIconBitmap = drawable.getBitmap();
                    break;
                //获取颜色属性值
                case R.styleable.ChangeIcon_color:
                    lColor = a.getColor(attr, 0xFF45C01A);
                    break;
                //获取文字属性值
                case R.styleable.ChangeIcon_text:
                    lText = a.getString(attr);
                    break;
                //获取字体大小属性值
                case R.styleable.ChangeIcon_text_size:
                    lTextSize = (int) a.getDimension(attr, TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                                    getResources().getDisplayMetrics()));
                    break;
            }

        }

        a.recycle();    //回收

        lTextBound = new Rect();            //创建Rect对象用于绘制图标下文字位置
        lTextPaint = new Paint();           //创建Paint对象用于绘制图标下文字
        lTextPaint.setTextSize(lTextSize); //设置绘制文字画笔的大小
        lTextPaint.setColor(0Xff555555);    //设置绘制文字画笔的颜色为灰色
        //测量文字的宽高
        lTextPaint.getTextBounds(lText, 0, lText.length(), lTextBound);

    }

    /**
     * 设置图标绘制位置
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取图标宽度
        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
                - getPaddingRight(), getMeasuredHeight() - getPaddingTop()
                - getPaddingBottom() - lTextBound.height());
        //设置图标横向居中
        int left = getMeasuredWidth() / 2 - iconWidth / 2;
        //设置图标纵向居中
        int top = getMeasuredHeight() / 2 - (lTextBound.height() + iconWidth)
                / 2;
        //设置图标绘制范围
        mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
    }

    /**
     * 实现绘制
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        //绘制图标
        canvas.drawBitmap(lIconBitmap, null, mIconRect, null);
        //设置渐变范围
        int alpha = (int) Math.ceil(255 * lAlpha);
        //调用绘制渐变色图标
        setupGradientIcon(alpha);
        drawOrdinaryText(canvas, alpha);  //调用绘制普通文字
        drawColorText(canvas, alpha);  //调用绘制变色文字
        //绘制变色图标
        canvas.drawBitmap(lBitmap, 0, 0, null);

    }

    /**
     * 绘制变色的文字
     *
     * @param canvas
     * @param alpha
     */
    private void drawColorText(Canvas canvas, int alpha)
    {
        lTextPaint.setColor(lColor);    //设置文字画笔颜色
        lTextPaint.setAlpha(alpha);     //设置文字画笔的透明度
        //绘制起始的X坐标
        int x = getMeasuredWidth() / 2 - lTextBound.width() / 2;
        //绘制起始的Y坐标
        int y = mIconRect.bottom + lTextBound.height();
        //绘制变色文字
        canvas.drawText(lText, x, y, lTextPaint);
    }

    /**
     * 绘制普通文字
     *
     * @param canvas
     * @param alpha
     */
    private void drawOrdinaryText(Canvas canvas, int alpha)
    {
        lTextPaint.setColor(0xff333333);    //设置普通文字画笔颜色
        lTextPaint.setAlpha(255 - alpha);   //设置透明度
        //绘制起始的X坐标
        int x = getMeasuredWidth() / 2 - lTextBound.width() / 2;
        //绘制起始的Y坐标
        int y = mIconRect.bottom + lTextBound.height();
        //绘制普通文字
        canvas.drawText(lText, x, y, lTextPaint);

    }

    /**
     * 绘制可变色的图标
     */
    private void setupGradientIcon(int alpha)
    {
        //设置渐变位图范围
        lBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        lCanvas = new Canvas(lBitmap);              //设置渐变画布
        lPaint = new Paint();                       //设置画笔
        lPaint.setColor(lColor);                    //设置画笔颜色
        lPaint.setAntiAlias(true);                  //开启抗锯齿
        lPaint.setDither(true);                     //开启仿抖动
        lPaint.setAlpha(alpha);                     //设置透明度
        lCanvas.drawRect(mIconRect, lPaint);      //图标绘制纯色
        //设置两层交集显示上层底色
        lPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        lPaint.setAlpha(255);                       //画笔设置为不透明
        //根据图标尺寸绘制纯色
        lCanvas.drawBitmap(lIconBitmap, null, mIconRect, lPaint);
    }


    /**
     * 设置图标渐变
     * @param alpha
     */
    public void setIconAlpha(float alpha)
    {
        this.lAlpha = alpha;
        invalidateView();       //调用重绘方法
    }

    /**
     * 重绘
     */
    private void invalidateView()
    {   //如果当前线程是UI线程的话
        if (Looper.getMainLooper() == Looper.myLooper())
        {
            invalidate();       //刷新
        } else
        {
            postInvalidate();   //非UI线程刷新
        }
    }

    private static final String INDATA = "indata";      //保存原有数据的常量
    private static final String ALPHA = "alpha";        //保存现有数据的常量

    /**
     * 系统销毁界面时，调用该方法保存当前界面显示的数据
     * @return
     */
    @Override
    protected Parcelable onSaveInstanceState()
    {
        Bundle bundle = new Bundle();    //创建传递数据的Bundle对象
        bundle.putParcelable(INDATA, super.onSaveInstanceState());    //保存原数据
        bundle.putFloat(ALPHA, lAlpha);                       //保存现有数据
        return bundle;
    }

    /**
     * 认为造成的系统销毁，调用该方法获取之前保存的显示数据
     * @param state
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if (state instanceof Bundle)        //如果是我们自己退出造成销毁的
        {
            Bundle bundle = (Bundle) state;
            lAlpha = bundle.getFloat(ALPHA);     //获取保存的现有数据
            //获取系统原有的数据
            super.onRestoreInstanceState(bundle.getParcelable(INDATA));
            return;
        }
        super.onRestoreInstanceState(state);
    }


}
