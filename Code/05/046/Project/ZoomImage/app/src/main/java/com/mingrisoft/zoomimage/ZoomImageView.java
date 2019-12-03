package com.mingrisoft.zoomimage;

import android.view.View;

import java.util.Observer;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/11/21.
 */

public class ZoomImageView extends View implements Observer {

    /**
     * 绘制位图时使用的画图对象。
     */
    private final Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);

    /**
     *用于裁剪源图像的矩形（可重复使用）。
     */
    private final Rect mRectSrc = new Rect();

    /**
     * 用于在画布上指定绘图区域的矩形（可重新使用）。
     */
    private final Rect mRectDst = new Rect();

    /**
     * 算商值
     */
    private final AspectQuotient mAspectQuotient = new AspectQuotient();

    /**
     *我们正在缩放的位图，并在屏幕上绘制。
     */
    private Bitmap mBitmap;

    /**
     * 变焦状态
     */
    private ZoomState mState;

    private BasicZoomControl mZoomControl;
    private BasicZoomListener mZoomListener;

    /*
     *初始化
     * */
    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mZoomControl = new BasicZoomControl();  //初始化基本的缩放控制
        mZoomListener = new BasicZoomListener();  //初始化基本的缩放监听
        mZoomListener.setZoomControl(mZoomControl);   //给监听设置
        setZoomState(mZoomControl.getZoomState());  //设置缩放状态
        setOnTouchListener(mZoomListener);      //设置手势监听
        mZoomControl.setAspectQuotient(getAspectQuotient());   //求商值
    }

    public void zoomImage(float f, float x, float y) {
        mZoomControl.zoom(f, x, y);
    }

    public void setImage(Bitmap bitmap) {
        mBitmap = bitmap;

        mAspectQuotient.updateAspectQuotient(getWidth(), getHeight(),
                mBitmap.getWidth(), mBitmap.getHeight());
        mAspectQuotient.notifyObservers();

        invalidate();
    }

    private void setZoomState(ZoomState state) {
        if (mState != null) {
            mState.deleteObserver(this);
        }

        mState = state;
        mState.addObserver(this);

        invalidate();
    }

    private AspectQuotient getAspectQuotient() {
        return mAspectQuotient;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null && mState != null) {

            final float aspectQuotient = mAspectQuotient.get();

            final int viewWidth = getWidth();
            final int viewHeight = getHeight();
            final int bitmapWidth = mBitmap.getWidth();
            final int bitmapHeight = mBitmap.getHeight();

            final float panX = mState.getPanX();
            final float panY = mState.getPanY();
            final float zoomX = mState.getZoomX(aspectQuotient) * viewWidth
                    / bitmapWidth;
            final float zoomY = mState.getZoomY(aspectQuotient) * viewHeight
                    / bitmapHeight;

            // 设置目标矩形
            mRectSrc.left = (int) (panX * bitmapWidth - viewWidth / (zoomX * 2)); //矩形的左边
            mRectSrc.top = (int) (panY * bitmapHeight - viewHeight / (zoomY * 2));   //矩形的上边
            mRectSrc.right = (int) (mRectSrc.left + viewWidth / zoomX);  //矩形的右边
            mRectSrc.bottom = (int) (mRectSrc.top + viewHeight / zoomY);  //矩形的下边
            //设置画布
            mRectDst.left = 0;     //设置画布的左边
            mRectDst.top = 0;     //设置画布的右边
            mRectDst.right = getWidth();    //使画布的右边为屏幕宽度
            mRectDst.bottom = getHeight();  //设置画布的下边为屏幕的高度

            // 调整矩形，以便它适合于图像中。
            if (mRectSrc.left < 0) {
                mRectDst.left += -mRectSrc.left * zoomX;
                mRectSrc.left = 0;
            }
            if (mRectSrc.right > bitmapWidth) {
                mRectDst.right -= (mRectSrc.right - bitmapWidth) * zoomX;
                mRectSrc.right = bitmapWidth;
            }
            if (mRectSrc.top < 0) {
                mRectDst.top += -mRectSrc.top * zoomY;
                mRectSrc.top = 0;
            }
            if (mRectSrc.bottom > bitmapHeight) {
                mRectDst.bottom -= (mRectSrc.bottom - bitmapHeight) * zoomY;
                mRectSrc.bottom = bitmapHeight;
            }

            mRectDst.left = 0;
            mRectDst.top = 0;
            mRectDst.right = viewWidth;
            mRectDst.bottom = viewHeight;
            canvas.drawBitmap(mBitmap, mRectSrc, mRectDst, mPaint);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mAspectQuotient.updateAspectQuotient(right - left, bottom - top,
                mBitmap.getWidth(), mBitmap.getHeight());
        mAspectQuotient.notifyObservers();
    }

    @Override
    public void update(Observable observable, Object data) {
        invalidate();
    }

    private class BasicZoomListener implements View.OnTouchListener {

        /**
         * 缩放控制操作
         */
        private BasicZoomControl mZoomControl;

        private float mFirstX = -1;
        private float mFirstY = -1;
        private float mSecondX = -1;
        private float mSecondY = -1;

        private int mOldCounts = 0;

        /**
         * 设置缩放控件来操作
         *
         */
        public void setZoomControl(BasicZoomControl control) {
            mZoomControl = control;
        }

        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mOldCounts = 1;
                    mFirstX = event.getX();
                    mFirstY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE: {
                    float fFirstX = event.getX();
                    float fFirstY = event.getY();

                    int nCounts = event.getPointerCount();

                    if (1 == nCounts) {
                        mOldCounts = 1;
                        float dx = (fFirstX - mFirstX) / v.getWidth();
                        float dy = (fFirstY - mFirstY) / v.getHeight();
                        mZoomControl.pan(-dx, -dy);
                    } else if (1 == mOldCounts) {
                        mSecondX = event.getX(event.getPointerId(nCounts - 1));
                        mSecondY = event.getY(event.getPointerId(nCounts - 1));
                        mOldCounts = nCounts;
                    } else {
                        float fSecondX = event
                                .getX(event.getPointerId(nCounts - 1));
                        float fSecondY = event
                                .getY(event.getPointerId(nCounts - 1));

                        double nLengthOld = getLength(mFirstX, mFirstY, mSecondX,
                                mSecondY);
                        double nLengthNow = getLength(fFirstX, fFirstY, fSecondX,
                                fSecondY);

                        float d = (float) ((nLengthNow - nLengthOld) / v.getWidth());

                        mZoomControl.zoom((float) Math.pow(20, d),
                                ((fFirstX + fSecondX) / 2 / v.getWidth()),
                                ((fFirstY + fSecondY) / 2 / v.getHeight()));

                        mSecondX = fSecondX;
                        mSecondY = fSecondY;
                    }
                    mFirstX = fFirstX;
                    mFirstY = fFirstY;

                    break;
                }

            }

            return true;
        }

        private double getLength(float x1, float y1, float x2, float y2) {
            return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        }
    }

    private class BasicZoomControl implements Observer {

        /**
         * 最小缩放级别限制
         */
        private static final float MIN_ZOOM = 1;

        /**
         * 最大缩放级别限制
         */
        private static final float MAX_ZOOM = 16;

        /**
         * 在控制了变焦镜头
         */
        private final ZoomState mState = new ZoomState();

        /**
         * 现在视图和内容的对象求商值
         */
        private AspectQuotient mAspectQuotient;

        /**
         * 设置参考对象保持纵横商
         *
         */
        public void setAspectQuotient(AspectQuotient aspectQuotient) {
            if (mAspectQuotient != null) {
                mAspectQuotient.deleteObserver(this);
            }

            mAspectQuotient = aspectQuotient;
            mAspectQuotient.addObserver(this);
        }

        /**
         * 获取缩放状态被控制
         *
         */
        public ZoomState getZoomState() {
            return mState;
        }

        /**
         * 变焦
         *
         * @param f 缩放的因素
         * @param x X-不变的位置坐标
         * @param y Y-不变的位置坐标
         */
        public void zoom(float f, float x, float y) {

            // Log.d("Zoom", "zoom f = " + f);

            final float aspectQuotient = mAspectQuotient.get();

            final float prevZoomX = mState.getZoomX(aspectQuotient);
            final float prevZoomY = mState.getZoomY(aspectQuotient);

            mState.setZoom(mState.getZoom() * f);
            limitZoom();

            final float newZoomX = mState.getZoomX(aspectQuotient);
            final float newZoomY = mState.getZoomY(aspectQuotient);

            //保持X和Y坐标不变量
            mState.setPanX(mState.getPanX() + (x - .5f)
                    * (1f / prevZoomX - 1f / newZoomX));
            mState.setPanY(mState.getPanY() + (y - .5f)
                    * (1f / prevZoomY - 1f / newZoomY));

            limitPan();

            mState.notifyObservers();
        }

        /**
         * Pan
         *
         * @param dx Amount to pan in x-dimension
         * @param dy Amount to pan in y-dimension
         */
        public void pan(float dx, float dy) {
            final float aspectQuotient = mAspectQuotient.get();

            mState.setPanX(mState.getPanX() + dx
                    / mState.getZoomX(aspectQuotient));
            mState.setPanY(mState.getPanY() + dy
                    / mState.getZoomY(aspectQuotient));

            limitPan();

            mState.notifyObservers();
        }

        /**
         *帮助函数从中心位置计算出最大的居中位置。
         *
         * @param zoom Zoom value
         * @return Max delta of pan
         */
        private float getMaxPanDelta(float zoom) {
            return Math.max(0f, .5f * ((zoom - 1) / zoom));
        }

        /**
         *强制缩放保持在限制范围内
         */
        private void limitZoom() {
            if (mState.getZoom() < MIN_ZOOM) {
                mState.setZoom(MIN_ZOOM);
            } else if (mState.getZoom() > MAX_ZOOM) {
                mState.setZoom(MAX_ZOOM);
            }
        }

        /**
         * 迫使留在限度内
         */
        private void limitPan() {
            final float aspectQuotient = mAspectQuotient.get();

            final float zoomX = mState.getZoomX(aspectQuotient);
            final float zoomY = mState.getZoomY(aspectQuotient);

            final float panMinX = .5f - getMaxPanDelta(zoomX);
            final float panMaxX = .5f + getMaxPanDelta(zoomX);
            final float panMinY = .5f - getMaxPanDelta(zoomY);
            final float panMaxY = .5f + getMaxPanDelta(zoomY);

            if (mState.getPanX() < panMinX) {
                mState.setPanX(panMinX);
            }
            if (mState.getPanX() > panMaxX) {
                mState.setPanX(panMaxX);
            }
            if (mState.getPanY() < panMinY) {
                mState.setPanY(panMinY);
            }
            if (mState.getPanY() > panMaxY) {
                mState.setPanY(panMaxY);
            }
        }

        // 观察到的接口的实现

        public void update(Observable observable, Object data) {
            limitZoom();
            limitPan();
        }
    }

    private class AspectQuotient extends Observable {

        /**
         * Aspect quotient
         */
        private float mAspectQuotient;

        // Public methods

        /**
         * Gets aspect quotient
         *
         * @return The aspect quotient
         */
        public float get() {
            return mAspectQuotient;
        }

        /**
         * 更新和重新计算方面商基于提供的视图和内容尺寸。
         *
         * @param viewWidth     Width of view
         * @param viewHeight    Height of view
         * @param contentWidth  Width of content
         * @param contentHeight Height of content
         */
        public void updateAspectQuotient(float viewWidth, float viewHeight,
                                         float contentWidth, float contentHeight) {
            final float aspectQuotient = (contentWidth / contentHeight)
                    / (viewWidth / viewHeight);

            if (aspectQuotient != mAspectQuotient) {
                mAspectQuotient = aspectQuotient;
                setChanged();
            }
        }
    }

    private class ZoomState extends Observable {
        //缩放级别一个值为1意味着内容适合视图。
        private float mZoom;
        // 缩放窗口中心位置坐标x位置，相对于内容的宽度。
        private float mPanX;
        // 缩放窗口中心位置坐标Y位置，纵坐标平移位置，相对于内容的高度。
        private float mPanY;

        public float getPanX() {
            return mPanX;
        }
        public float getPanY() {
            return mPanY;
        }


        public float getZoom() {
            return mZoom;
        }
        /**
         * 帮助功能计算当前的缩放值x
         *
         * @param aspectQuotient (Aspect ratio content) / (Aspect ratio view)
         * @return 在当前的缩放值x
         */
        public float getZoomX(float aspectQuotient) {
            return Math.min(mZoom, mZoom * aspectQuotient);
        }
        /**
         * 帮助功能计算当前的缩放值y
         *
         * @param aspectQuotient (Aspect ratio content) / (Aspect ratio view)
         * @return 在当前的缩放值y
         */
        public float getZoomY(float aspectQuotient) {
            return Math.min(mZoom, mZoom / aspectQuotient);
        }

        /**
         * 设置X
         *
         * @param panX Pan-x value to set
         */
        public void setPanX(float panX) {
            if (panX != mPanX) {
                mPanX = panX;
                setChanged();
            }
        }

        /**
         * 设置Y
         *
         * @param panY Pan-y value to set
         */
        public void setPanY(float panY) {
            if (panY != mPanY) {
                mPanY = panY;
                setChanged();
            }
        }

        /**
         * 设置缩放
         *
         * @param zoom Zoom value to set
         */
        public void setZoom(float zoom) {
            if (zoom != mZoom) {
                mZoom = zoom;
                setChanged();
            }
        }
    }

}
