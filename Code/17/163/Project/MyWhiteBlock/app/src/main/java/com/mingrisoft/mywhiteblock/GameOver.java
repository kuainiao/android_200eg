package com.mingrisoft.mywhiteblock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import com.mingrisoft.mywhiteblock.GameMenu.changeState;

public class GameOver extends BaseProperty {
	private changeState change;
	private DrawText text_score;//分数
	private DrawText text_Mkuai;//每秒块数
	private DrawText text_again;//重来
	private DrawText text_over;//结束
	private static int mScreenWidth;
	private static int mScreenHeight;
	Context context;
	Bitmap mBitmap;
	@Override
	public void onTouch(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(text_again.onTouch(event)){
				Log.d("66666", event.getX()+"x");
				change.execute(GameState.STATE_PLAY_INIT);
			}
			if(text_over.onTouch(event)){
				Log.d("111111111111111111", event.getX()+"x");
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
			break;

		default:
			break;
		}
	}

	public GameOver(){

		text_score=new DrawText();
		text_score.setXY(screen_width/2, screen_height/2);
		
		text_Mkuai=new DrawText();
		text_Mkuai.setText(GameScore.SCORE+"分", 60);
		text_Mkuai.setXY(screen_width/4, screen_height/4);
		
		text_again=new DrawText();
		text_again.setText("重来", 90);
		text_again.setXY(screen_width/4, screen_height*3/4);
		
		text_over=new DrawText();
		text_over.setText("结束", 90);
		text_over.setXY(screen_width*3/4, screen_height*3/4);
		
	}
	public void ondraw(Canvas canvas){
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		Bitmap bmp = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.gamebj)).getBitmap();
		mBitmap = Bitmap.createScaledBitmap(bmp, mScreenWidth, mScreenHeight, true);
		canvas.drawBitmap(mBitmap,0,0,paint);
		text_score.setText(GameScore.SCORE+"分", 60);
		text_score.ondraw(canvas);
//		text_Mkuai.ondraw(canvas);
		text_again.ondraw(canvas);
		text_over.ondraw(canvas);	
	}
	public void setchange(changeState change, Context context){
		this.change=change;
		this.context=context;
	}
}
