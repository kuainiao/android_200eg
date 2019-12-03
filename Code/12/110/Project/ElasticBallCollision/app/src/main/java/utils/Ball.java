package utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * 自定义小球类
 */

public class Ball {
	private static final int COLLISION_X = 0;				//碰撞X坐标标记
	private static final int COLLISION_Y = COLLISION_X+1;	//碰撞Y坐标标记
	
	private float x, y, r;    //圆心坐标x、y，直径r；
	private Bitmap lBitmap;	   //图形
	private Coordinate speed; //图形速度
	private Space space;	    //活动空间

	private float stepTime = 30f;
	private float goFast = 2.0f;
	/**
	 * 1.手机站立时：x = 0，01；y = 9.8,实际方向下微微偏左（0.01，9.8）， 2.手机左高右低 x = -9.8; y = 0.01
	 * 因为我们要表现的其实就是每时每刻改变后图形的坐标（x,y）值，根据得出 x-(v0+ax*t)*t）, y+(v0+ay*t)*t）;
	 */
	private Coordinate directionVector;// 物体加速度

	/**
	 * 
	 * @param x 
	 * @param y
	 * @param bitmap
	 */
	public Ball(float x, float y, Bitmap bitmap) {
		this( x,  y, bitmap,new Coordinate(0.0f, 0.0f));
	}
	
	/**
	 * @param initialvelocity  初速度
	 */
	public Ball(float x, float y, Bitmap bitmap,Coordinate initialvelocity) {
		this.x = x;
		this.y = y;
		this.lBitmap = bitmap;
		this.r = this.lBitmap.getHeight();
		speed = initialvelocity;
		directionVector = new Coordinate(0.0f, 0.0f);

	}
	
	/**
	 * 活动空间
	 * @param space
	 */
	public void setActivityArea(Space space){
		this.space = space;
	}
	
	/**
	 * 绘图
	 */
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(lBitmap, x, y, paint);
	}

	/**
	 *开始运动
	 */
	public void go(float x, float y, int w, int h) {
		speed.x += directionVector.x * goFast;
		speed.y += directionVector.y * goFast;
		//小球运动时的坐标
		float willX = this.x - speed.x * stepTime / 1000;
		float willY = this.y + speed.y * stepTime / 1000;

		if (willY < y) {
			willY = y;
		}
		if (willY > h - r) {
			willY = h - r;
		}
		if (willX < x) {
			willX = x;
		}
		if (willX > w - r) {
			willX = w - r;
		}
		//最后的运动坐标
		this.x = willX;
		this.y = willY;

	}

	/**
	 * 设置坐标向量
	 * @param directionVector
     */
	public void setDirectionVector(Coordinate directionVector) {
		this.directionVector = directionVector;
	}

	/**
	 * 与边界检测碰撞
	 */
	public boolean isCollision(float x, float y, float w, float h) {
		//监测碰到X轴边框
		if (y >= this.y || this.y + r >= h) {
			rebound(COLLISION_X);	//小球碰到X轴的反弹
			return true;
			//监测碰到Y轴边框
		} else if (this.x <= x || this.x >= w - r) {
			rebound(COLLISION_Y);	//小球碰到Y轴的反弹
			return true;
		}
		return false;
	}

	/**
	 * 碰撞后反弹
	 */
	public void rebound(int tag) {

		if (tag == COLLISION_X) {			//如果小球碰到X轴的边框
			speed.x = (speed.x * 0.9f);	//计算小球反弹的X运动坐标
			speed.y = -(speed.y * 0.9f);	//计算小球反弹的Y运动坐标
		} else if (tag == COLLISION_Y) {	//如果小球碰到Y轴的边框
			speed.x = -(speed.x * 0.9f);
			speed.y = (speed.y * 0.9f);
		}
	}

	
}
