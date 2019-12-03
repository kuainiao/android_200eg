package com.mingrisoft.screencapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.media.MediaActionSound;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.ImageView;


class GlobalScreenShot {
  private static final String TAG = "GlobalScreenShot";

  private static final int SCREENSHOT_FLASH_TO_PEAK_DURATION = 130;
  private static final int SCREENSHOT_DROP_IN_DURATION = 430;
  private static final int SCREENSHOT_DROP_OUT_DELAY = 500;
  private static final int SCREENSHOT_DROP_OUT_DURATION = 430;
  private static final int SCREENSHOT_DROP_OUT_SCALE_DURATION = 370;
  private static final int SCREENSHOT_FAST_DROP_OUT_DURATION = 320;
  private static final float BACKGROUND_ALPHA = 0.5f;
  private static final float SCREENSHOT_SCALE = 1f;
  private static final float SCREENSHOT_DROP_IN_MIN_SCALE = SCREENSHOT_SCALE * 0.725f;
  private static final float SCREENSHOT_DROP_OUT_MIN_SCALE = SCREENSHOT_SCALE * 0.45f;
  private static final float SCREENSHOT_FAST_DROP_OUT_MIN_SCALE = SCREENSHOT_SCALE * 0.6f;
  private static final float SCREENSHOT_DROP_OUT_MIN_SCALE_OFFSET = 0f;

  private Context mContext;
  private WindowManager mWindowManager;
  private WindowManager.LayoutParams mWindowLayoutParams;
  private Display mDisplay;
  private DisplayMetrics mDisplayMetrics;

  private Bitmap mScreenBitmap;
  private View mScreenshotLayout;
  private ImageView mBackgroundView;
  private ImageView mScreenshotView;
  private ImageView mScreenshotFlash;

  private AnimatorSet mScreenshotAnimation;

  private float mBgPadding;
  private float mBgPaddingScale;

  private MediaActionSound mCameraSound;


  private onScreenShotListener mOnScreenShotListener;

  /**
   * @param context everything needs a context :(
   */
  public GlobalScreenShot(Context context) {
    Resources r = context.getResources();
    mContext = context;
    LayoutInflater layoutInflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // 截图布局
    mScreenshotLayout = layoutInflater.inflate(R.layout.global_screenshot, null);
    mBackgroundView = (ImageView) mScreenshotLayout.findViewById(R.id.global_screenshot_background);
    mScreenshotView = (ImageView) mScreenshotLayout.findViewById(R.id.global_screenshot);
    mScreenshotFlash = (ImageView) mScreenshotLayout.findViewById(R.id.global_screenshot_flash);
    mScreenshotLayout.setFocusable(true);
    mScreenshotLayout.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        //拦截和忽略所有的触摸事件
        return true;
      }
    });

    // 设置要使用的窗口
    mWindowLayoutParams = new WindowManager.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0, 0,
        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
            | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
        PixelFormat.TRANSLUCENT);
    mWindowLayoutParams.setTitle("ScreenshotAnimation");
    mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

    mDisplay = mWindowManager.getDefaultDisplay();
    mDisplayMetrics = new DisplayMetrics();
    mDisplay.getRealMetrics(mDisplayMetrics);


    mBgPadding = (float) r.getDimensionPixelSize(R.dimen.global_screenshot_bg_padding);
    mBgPaddingScale = mBgPadding / mDisplayMetrics.widthPixels;

    // 设置相机快门声音
    mCameraSound = new MediaActionSound();
    mCameraSound.load(MediaActionSound.SHUTTER_CLICK);
  }


  /**
   * 获取当前显示的截图并显示动画。
   */
  void takeScreenshot(Bitmap bitmap, onScreenShotListener onScreenShotListener, boolean statusBarVisible, boolean navBarVisible) {
    //截图
    mScreenBitmap = bitmap;
    this.mOnScreenShotListener = onScreenShotListener;

    if (mOnScreenShotListener != null) {
      mOnScreenShotListener.onStartShot();
    }

    if (mScreenBitmap == null) {
      notifyScreenshotError(mContext);
      return;
    }

    // 优化
    mScreenBitmap.setHasAlpha(false);
    mScreenBitmap.prepareToDraw();

    // 启动后截图动画
    startAnimation(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels,
        statusBarVisible, navBarVisible);
  }


  /**
   * 开始动画后采取截图
   */
  private void startAnimation(int w, int h, boolean statusBarVisible,
                              boolean navBarVisible) {
    // 添加动画视图
    mScreenshotView.setImageBitmap(mScreenBitmap);
    mScreenshotLayout.requestFocus();

    // 设置动画与刚才拍摄的截图
    if (mScreenshotAnimation != null) {
      mScreenshotAnimation.end();
      mScreenshotAnimation.removeAllListeners();
    }

    mWindowManager.addView(mScreenshotLayout, mWindowLayoutParams);
    ValueAnimator screenshotDropInAnim = createScreenshotDropInAnimation();
    ValueAnimator screenshotFadeOutAnim = createScreenshotDropOutAnimation(w, h,
        statusBarVisible, navBarVisible);
    mScreenshotAnimation = new AnimatorSet();
    mScreenshotAnimation.playSequentially(screenshotDropInAnim, screenshotFadeOutAnim);
    mScreenshotAnimation.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        //保存截图
        saveScreenshotInWorkerThread();
        mWindowManager.removeView(mScreenshotLayout);

        //清除位图
        mScreenBitmap = null;
        mScreenshotView.setImageBitmap(null);

      }
    });
    mScreenshotLayout.post(new Runnable() {
      @Override
      public void run() {
        // 播放快门声音通知我们已经采取了截图
        mCameraSound.play(MediaActionSound.SHUTTER_CLICK);
        mScreenshotView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mScreenshotView.buildLayer();
        mScreenshotAnimation.start();
      }
    });
  }

  private ValueAnimator createScreenshotDropInAnimation() {
    final float flashPeakDurationPct = ((float) (SCREENSHOT_FLASH_TO_PEAK_DURATION)
        / SCREENSHOT_DROP_IN_DURATION);
    final float flashDurationPct = 2f * flashPeakDurationPct;
    final Interpolator flashAlphaInterpolator = new Interpolator() {
      @Override
      public float getInterpolation(float x) {
        // 快速查看和快速的闪光
        if (x <= flashDurationPct) {
          return (float) Math.sin(Math.PI * (x / flashDurationPct));
        }
        return 0;
      }
    };
    final Interpolator scaleInterpolator = new Interpolator() {
      @Override
      public float getInterpolation(float x) {
        //我们开始缩放时，闪光是在其高峰期
        if (x < flashPeakDurationPct) {
          return 0;
        }
        return (x - flashDurationPct) / (1f - flashDurationPct);
      }
    };
    ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
    anim.setDuration(SCREENSHOT_DROP_IN_DURATION);
    anim.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationStart(Animator animation) {
        mBackgroundView.setAlpha(0f);
        mBackgroundView.setVisibility(View.VISIBLE);
        mScreenshotView.setAlpha(0f);
        mScreenshotView.setTranslationX(0f);
        mScreenshotView.setTranslationY(0f);
        mScreenshotView.setScaleX(SCREENSHOT_SCALE + mBgPaddingScale);
        mScreenshotView.setScaleY(SCREENSHOT_SCALE + mBgPaddingScale);
        mScreenshotView.setVisibility(View.VISIBLE);
        mScreenshotFlash.setAlpha(0f);
        mScreenshotFlash.setVisibility(View.VISIBLE);
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        mScreenshotFlash.setVisibility(View.GONE);

      }
    });
    anim.addUpdateListener(new AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        float t = (Float) animation.getAnimatedValue();
        float scaleT = (SCREENSHOT_SCALE + mBgPaddingScale)
            - scaleInterpolator.getInterpolation(t)
            * (SCREENSHOT_SCALE - SCREENSHOT_DROP_IN_MIN_SCALE);
        mBackgroundView.setAlpha(scaleInterpolator.getInterpolation(t) * BACKGROUND_ALPHA);
        mScreenshotView.setAlpha(t);
        mScreenshotView.setScaleX(scaleT);
        mScreenshotView.setScaleY(scaleT);
        mScreenshotFlash.setAlpha(flashAlphaInterpolator.getInterpolation(t));
      }
    });
    return anim;
  }

  private ValueAnimator createScreenshotDropOutAnimation(int w, int h, boolean statusBarVisible,
                                                         boolean navBarVisible) {
    ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
    anim.setStartDelay(SCREENSHOT_DROP_OUT_DELAY);
    anim.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        mBackgroundView.setVisibility(View.GONE);
        mScreenshotView.setVisibility(View.GONE);
        mScreenshotView.setLayerType(View.LAYER_TYPE_NONE, null);
      }
    });

    if (!statusBarVisible || !navBarVisible) {
      // 没有状态栏或导航栏，所以只是截图以外的地方
      anim.setDuration(SCREENSHOT_FAST_DROP_OUT_DURATION);
      anim.addUpdateListener(new AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
          float t = (Float) animation.getAnimatedValue();
          float scaleT = (SCREENSHOT_DROP_IN_MIN_SCALE + mBgPaddingScale)
              - t * (SCREENSHOT_DROP_IN_MIN_SCALE - SCREENSHOT_FAST_DROP_OUT_MIN_SCALE);
          mBackgroundView.setAlpha((1f - t) * BACKGROUND_ALPHA);
          mScreenshotView.setAlpha(1f - t);
          mScreenshotView.setScaleX(scaleT);
          mScreenshotView.setScaleY(scaleT);
        }
      });
    } else {
      final float scaleDurationPct = (float) SCREENSHOT_DROP_OUT_SCALE_DURATION
          / SCREENSHOT_DROP_OUT_DURATION;
      final Interpolator scaleInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float x) {
          if (x < scaleDurationPct) {
            // 减速，并输入相应的规模
            return (float) (1f - Math.pow(1f - (x / scaleDurationPct), 2f));
          }
          return 1f;
        }
      };

      //确定如何规模的界限
      float halfScreenWidth = (w - 2f * mBgPadding) / 2f;
      float halfScreenHeight = (h - 2f * mBgPadding) / 2f;
      final float offsetPct = SCREENSHOT_DROP_OUT_MIN_SCALE_OFFSET;
      final PointF finalPos = new PointF(
          -halfScreenWidth + (SCREENSHOT_DROP_OUT_MIN_SCALE + offsetPct) * halfScreenWidth,
          -halfScreenHeight + (SCREENSHOT_DROP_OUT_MIN_SCALE + offsetPct) * halfScreenHeight);

      // 动画截图到状态栏
      anim.setDuration(SCREENSHOT_DROP_OUT_DURATION);
      anim.addUpdateListener(new AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
          float t = (Float) animation.getAnimatedValue();
          float scaleT = (SCREENSHOT_DROP_IN_MIN_SCALE + mBgPaddingScale)
              - scaleInterpolator.getInterpolation(t)
              * (SCREENSHOT_DROP_IN_MIN_SCALE - SCREENSHOT_DROP_OUT_MIN_SCALE);
          mBackgroundView.setAlpha((1f - t) * BACKGROUND_ALPHA);
          mScreenshotView.setAlpha(1f - scaleInterpolator.getInterpolation(t));
          mScreenshotView.setScaleX(scaleT);
          mScreenshotView.setScaleY(scaleT);
          mScreenshotView.setTranslationX(t * finalPos.x);
          mScreenshotView.setTranslationY(t * finalPos.y);
        }
      });
    }
    return anim;
  }

  private void notifyScreenshotError(Context context) {
    if (mOnScreenShotListener != null) {
      mOnScreenShotListener.onFinishShot(false);
    }
  }

  private void saveScreenshotInWorkerThread() {
    if (mOnScreenShotListener != null) {
      mOnScreenShotListener.onFinishShot(true);
    }
  }


  public interface onScreenShotListener {

    public void onStartShot();

    public void onFinishShot(boolean success);
  }
}
