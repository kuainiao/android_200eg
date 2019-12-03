package com.mingrisoft.mywhiteblock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

//游戏菜单页面
public class GameMenu  extends BaseProperty{
	Rect targetRect;
	String str="Play";
	float x_left;
	float x_right;
	float y_top;
	float y_botton;
	changeState change;
	Context context;
	private static int mScreenWidth;
	private static int mScreenHeight;
	public GameMenu(){
		paint=new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setTextSize(70);
		targetRect = new Rect();
		//返回包围整个字符串的最小的一个Rect区域
		paint.getTextBounds(str, 0, str.length(), targetRect); 
	}
	public void setChange(changeState change, Context context){
		this.change=change;
		this.context=context;
	}
	public void ondraw(Canvas canvas){
		canvas.drawRGB(0, 0, 0);
		//获取屏幕信息
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		Bitmap bmp = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.gamebj)).getBitmap();
		Bitmap mBitmap = Bitmap.createScaledBitmap(bmp, mScreenWidth, mScreenHeight, true);
		//绘制背景图
		canvas.drawBitmap(mBitmap,0,0,paint);
		int textheight=targetRect.height();
		int textwidth=targetRect.width();
		x_left=(screen_width-textwidth)/2;
		x_right=(screen_width+textwidth)/2;
		y_top=(screen_height-textheight)/2-textheight;
		y_botton=(screen_height-textheight)/2;
		//绘制文字
		canvas.drawText(str,x_left ,(screen_height-textheight)/2 , paint);
	}
	@Override
	public void onTouch(MotionEvent event){
		float x=event.getX();
		float y=event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(isStart(x, y)){
				if(change!=null){
					//改变游戏状态
					change.execute(GameState.STATE_PLAY_INIT);
				}
			}
			break;
		default:
			break;
		}
	}
	public boolean isStart(float x,float y){
		if(x>x_left&&x<x_right){
			if(y>y_top&&y<y_botton){
				return true;
			}
		}
		return false;
	}
	public interface changeState{
		public void execute(int state);
	}
}
