package com.mingrisoft.brokenscreen.tool;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Region;
import android.view.MotionEvent;
import android.view.View;

public class BrokenTouchListener implements View.OnTouchListener  {

    private BrokenAnimator brokenAnim;
    private BrokenView brokenView;
    private BrokenConfig config;
    private BrokenTouchListener(Builder builder) {
        brokenView = builder.brokenView;
        config = builder.config;
    }
    public static class Builder {
        private BrokenConfig config;
        private BrokenView brokenView;
        public Builder(BrokenView brokenView) {
            this.brokenView = brokenView;
            config = new BrokenConfig();
        }

        public Builder setPaint(Paint paint) {
            config.paint = paint;
            return this;
        }
        public BrokenTouchListener build() {
            return new BrokenTouchListener(this);
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(brokenView.isEnable()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(config.childView != null){
                        config.region = new Region(config.childView.getLeft(),
                                config.childView.getTop(),
                                config.childView.getRight(),
                                config.childView.getBottom());
                    }
                    if(config.region == null || config.region.contains((int)event.getX(),(int)event.getY())) {
                        Point point = new Point((int) event.getRawX(), (int) event.getRawY());
                        brokenAnim = brokenView.getAnimator(v);
                        if (brokenAnim == null)
                            brokenAnim = brokenView.createAnimator(v, point, config);
                        if (brokenAnim == null)
                            return true;
                        if (!brokenAnim.isStarted()) {
                            brokenAnim.start();
                            brokenView.onBrokenStart(v);
                        } else if (brokenAnim.doReverse()) {
                            brokenView.onBrokenRestart(v);
                        }
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    else
                        return false;
                    break;
                case MotionEvent.ACTION_UP:
                    brokenAnim = brokenView.getAnimator(v);
                    if (brokenAnim != null && brokenAnim.isStarted()) {
                        if(brokenAnim.doReverse())
                            brokenView.onBrokenCancel(v);
                    }
                    break;
            }
            return true;
        }
        else
            return false;
    }
}
