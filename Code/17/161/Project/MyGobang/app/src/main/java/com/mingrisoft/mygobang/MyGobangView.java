package com.mingrisoft.mygobang;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MyGobangView extends View {
    private int mPanelWidth;
    private float mLineHight;
    private int MAX_LINE = 10;

    private Paint mPaint = new Paint();
    //文字画笔
    private Paint mTpaint =new Paint();
    private Bitmap mWhitePiece,mBoxWhite;
    private Bitmap mBlackPiece,mBoxBlack;
    private float ratioPieceOfLineHight = 3 * 1.0f / 4;

    private boolean mIsWith = true;
    private List<Point> mWitharry = new ArrayList<Point>();
    private List<Point> mBlackarry = new ArrayList<Point>();

    private boolean mIsGemOver;
    private boolean mIsWhiteWinner;

    private int i = 0;
   //初始化方法
    public MyGobangView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //设置画笔颜色
        mPaint.setColor(Color.GRAY);
        //去掉锯齿
        mPaint.setAntiAlias(true);
        //防抖动
        mPaint.setDither(true);
        //设置画笔样式
        mPaint.setStyle(Paint.Style.STROKE);
        mTpaint.setTextSize(30f);
        mTpaint.setColor(Color.BLACK);
        //去掉锯齿
        mTpaint.setAntiAlias(true);
        //防抖动
        mTpaint.setDither(true);
        //设置画笔样式
        mTpaint.setStyle(Paint.Style.STROKE);
        //初始化图片资源
        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
        mBoxBlack= BitmapFactory.decodeResource(getResources(), R.drawable.box_b1);
        mBoxWhite= BitmapFactory.decodeResource(getResources(), R.drawable.box_w2);
    }
    //重新开始方法
   public void reStart(){
       mWitharry=new ArrayList<>();
       mBlackarry=new ArrayList<>();
       mIsGemOver=false;
       //刷新
       invalidate();
   }
    //触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsGemOver) {
            return false;
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            //获取棋子坐标
            Point p = getVaLidPoint(x, y);
            if (mWitharry.contains(p) || mBlackarry.contains(p)) {
                return false;
            }
            //判断该添加的棋子
            if (mIsWith) {
                mWitharry.add(p);
            } else {
                mBlackarry.add(p);
            }
            invalidate();
            mIsWith = !mIsWith;
        }
        return true;
    }
    //返回左边点
    private Point getVaLidPoint(int x, int y) {
        return new Point((int) (x / mLineHight), (int) (y / mLineHight));
    }
    //测量控件大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heighSize = MeasureSpec.getSize(heightMeasureSpec);
        int heighMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = Math.min(widthSize, heighSize);
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heighSize;
        } else if (heighMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }
    //按比例缩小图片
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;
        //缩小比例
        mLineHight = mPanelWidth * 1.0f / MAX_LINE;
        int PiceWhite = (int) (mLineHight * ratioPieceOfLineHight);
        //按比例重新绘制图片
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, PiceWhite, PiceWhite, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, PiceWhite, PiceWhite, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        drawBoard(canvas);
        //绘制棋子
        draePicec(canvas);
        //判断是否结束
        checkGameOver(canvas);
        //绘制棋盘
        outPicec(canvas);
    }
    //绘制棋盘
    private void outPicec(Canvas canvas) {
            canvas.drawText("接下来轮到：",mLineHight/2,30,mTpaint);
        if (mIsWith){
            //图片绘制位置
            RectF rectF = new RectF(mLineHight*5/2-30, 5, mLineHight*5/2, 35);
            canvas.drawBitmap(mBoxWhite, null, rectF, null);

            RectF rectF1 = new RectF(mPanelWidth-mLineHight*5/2-30, mPanelWidth-40, mPanelWidth-mLineHight*5/2, mPanelWidth-10);
            canvas.drawBitmap(mBoxWhite, null, rectF1, null);
        }else {
            //图片绘制位置
            RectF rectF = new RectF(mLineHight*5/2-30, 5, mLineHight*5/2, 35);
            canvas.drawBitmap(mBoxBlack, null, rectF, null);

            RectF rectF1 = new RectF(mPanelWidth-mLineHight*5/2-30, mPanelWidth-40, mPanelWidth-mLineHight*5/2, mPanelWidth-10);
            canvas.drawBitmap(mBoxBlack, null, rectF1, null);
        }
        ondrawText(canvas,"接下来轮到：",mPanelWidth-mLineHight/2,mPanelWidth-30,mTpaint,(float) 180);
    }
    //选择文字
    public void ondrawText(Canvas canvas ,String text , float x ,float y,Paint paint ,float angle){
        if(angle != 0){
            canvas.rotate(angle, x, y);
        }
        canvas.drawText(text, x, y, paint);
        if(angle != 0){
            canvas.rotate(-angle, x, y);
        }
    }
    //判断是否结束
    private void checkGameOver(Canvas canvas) {
        //判断白棋是否胜利
        boolean whithWin = chechFiveInLine(mWitharry);
        //判断黑棋是否胜利
        boolean blickWin = chechFiveInLine(mBlackarry);
        if (whithWin || blickWin) {
            mIsGemOver = true;
            mIsWhiteWinner = whithWin;
            String text = mIsWhiteWinner ? "白棋胜利" : "黑棋胜利";
            canvas.drawText("本轮："+text,mLineHight*5,30,mTpaint);
            ondrawText(canvas,"本轮："+text,mPanelWidth/2,mPanelWidth-30,mTpaint,(float) 180);
        }
    }
     //检查是否5子连线
    private boolean chechFiveInLine(List<Point> mWitharry2) {
        for (Point p : mWitharry2) {
            int x = p.x;
            int y = p.y;
            boolean win = checkHorizontal(x, y, mWitharry2);
            if (win) return true;
            win = checkVertIcal(x, y, mWitharry2);
            if (win) return true;
            win = checkLeftDiagonal(x, y, mWitharry2);
            if (win) return true;
            win = checkRightDiagonl(x, y, mWitharry2);
            if (win) return true;
        }
        return false;
    }

    private boolean checkHorizontal(int x, int y, List<Point> mWitharry2) {
        int count = 1;
        for (int i = 1; i < 5; i++) {
            if (mWitharry2.contains(new Point(x - i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == 5) return true;
        for (int i = 1; i < 5; i++) {
            if (mWitharry2.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
            if (count == 5) return true;
        }
        return false;
    }

    private boolean checkRightDiagonl(int x, int y, List<Point> mWitharry2) {
        int count = 1;
        for (int i = 1; i < 5; i++) {
            if (mWitharry2.contains(new Point(x - i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == 5) return true;
        for (int i = 1; i < 5; i++) {
            if (mWitharry2.contains(new Point(x + i, y + i))) {
                count++;
            } else {
                break;
            }
            if (count == 5) return true;
        }
        return false;
    }

    private boolean checkLeftDiagonal(int x, int y, List<Point> mWitharry2) {
        int count = 1;
        for (int i = 1; i < 5; i++) {
            if (mWitharry2.contains(new Point(x - i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == 5) return true;
        for (int i = 1; i < 5; i++) {
            if (mWitharry2.contains(new Point(x + i, y - i))) {
                count++;
            } else {
                break;
            }
            if (count == 5) return true;
        }
        return false;
    }

    private boolean checkVertIcal(int x, int y, List<Point> mWitharry2) {
        int count = 1;
        for (int i = 1; i < 5; i++) {
            if (mWitharry2.contains(new Point(x, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == 5) return true;
        for (int i = 1; i < 5; i++) {
            if (mWitharry2.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
            if (count == 5) return true;
        }
        return false;
    }
    //绘制画过的棋子
    private void draePicec(Canvas canvas) {
        for (int i = 0, n = mWitharry.size(); i < n; i++) {
            Point whitePoint = mWitharry.get(i);
            canvas.drawBitmap(mWhitePiece, (whitePoint.x + (1 - ratioPieceOfLineHight) / 2) * mLineHight,
                    (whitePoint.y + (1 - ratioPieceOfLineHight) / 2) * mLineHight, null);
        }
        for (int i = 0, n = mBlackarry.size(); i < n; i++) {
            Point blackPoint = mBlackarry.get(i);
            canvas.drawBitmap(mBlackPiece, (blackPoint.x + (1 - ratioPieceOfLineHight) / 2) * mLineHight,
                    (blackPoint.y + (1 - ratioPieceOfLineHight) / 2) * mLineHight, null);
        }
    }
   //绘制棋盘
    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHight;
        for (int i = 0; i < MAX_LINE; i++) {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);
            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startX, y, endX, y, mPaint);
            canvas.drawLine(y, startX, y, endX, mPaint);
        }
    }
}
