package view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.mingrisoft.panoramicview.R;

import listener.GyroSensor;


/**
 * Created by gjz on 19/12/2016.
 */

public class ImageScrollView extends ImageView {

    //图像滚动方向
    public final static byte NONE_ORIENTATION = -1;
    public final static byte HORIZONTAL_ORIENTATION = 0;
    public final static byte VERTICAL_ORIENTATION = 1;
    private byte mOrientation = NONE_ORIENTATION;

    //是否启动全景效果
    private boolean lEnablePanoramaMode;

    //如果是真的，图像滚动左（上）当装置顺时针旋转沿Y轴（X轴）
    private boolean lInvertScrollDirection;

    //图像的宽度和高度
    private int lDrawableWidth;
    private int lDrawableHeight;

    //视图的宽度和高度
    private int lWidth;
    private int lHeight;

    //图像从初始状态偏移（视图中的中心）
    private float lMaxOffset;

    //进度
    private float lProgress;



    public ImageScrollView(Context context) {
        this(context, null);
    }

    public ImageScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setScaleType(ScaleType.CENTER_CROP);
        //初始化自定义控件
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageScrollView);
        //设置是否显示全景效果
        lEnablePanoramaMode = typedArray.getBoolean(R.styleable.ImageScrollView_piv_enablePanoramaMode, true);
        //滚动视图
        lInvertScrollDirection = typedArray.getBoolean(R.styleable.ImageScrollView_piv_invertScrollDirection, false);
        typedArray.recycle();           //回收资源

    }

    /**
     *绑定陀螺仪监听器
     */
    public void setGyroscopeObserver(GyroSensor observer) {
        if (observer != null) {
            observer.addPanoramaImageView(this);
        }
    }

    /**
     *更新进度
     */
    public void updateProgress(float progress) {
        if (lEnablePanoramaMode) {          //如果开启全景效果
            lProgress = lInvertScrollDirection? -progress : progress;
            invalidate();                     //清屏起到刷新作用
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //计算控件的宽度与高度
        lWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        lHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        //如果图像不为空，获取图像的宽度与高度
        if (getDrawable() != null) {
            lDrawableWidth = getDrawable().getIntrinsicWidth();
            lDrawableHeight = getDrawable().getIntrinsicHeight();
            //计算滚动的方向是横向还是纵向
            if (lDrawableWidth * lHeight > lDrawableHeight * lWidth) {
                mOrientation = HORIZONTAL_ORIENTATION;
                float imgScale = (float) lHeight / (float) lDrawableHeight;
                lMaxOffset = Math.abs((lDrawableWidth * imgScale - lWidth) * 0.5f);
            } else if(lDrawableWidth * lHeight < lDrawableHeight * lWidth) {
                mOrientation = VERTICAL_ORIENTATION;
                float imgScale = (float) lWidth / (float) lDrawableWidth;
                lMaxOffset = Math.abs((lDrawableHeight * imgScale - lHeight) * 0.5f);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!lEnablePanoramaMode || getDrawable() == null || isInEditMode()) {
            super.onDraw(canvas);
            return;
        }

        //绘制图像
        if (mOrientation == HORIZONTAL_ORIENTATION) {
            //计算横向偏移的进度值
            float currentOffsetX = lMaxOffset * lProgress;
            canvas.save();                          //保存当前画布
            canvas.translate(currentOffsetX, 0);    //使用指定的转换预转换当前矩阵
            super.onDraw(canvas);
            canvas.restore();                       //恢复画布
        } else if (mOrientation == VERTICAL_ORIENTATION) {
            //计算纵向偏移的进度值
            float currentOffsetY = lMaxOffset * lProgress;
            canvas.save();                          //保存当前画布
            canvas.translate(0, currentOffsetY);    //使用指定的转换预转换当前矩阵
            super.onDraw(canvas);
            canvas.restore();                       //恢复画布
        }

    }

    //获取当前处于横向滚动还是纵向
    public byte getOrientation() {
        return mOrientation;
    }

    /**
     * 设置控件显示中心位置
     */
    @Override
    public void setScaleType(ScaleType scaleType) {
    }



}
