package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.mingrisoft.elasticballcollision.R;

import utils.Ball;
import utils.Coordinate;
import utils.Space;

/**
 *主要实现显示小球
 */
public class BallView extends SurfaceView implements SurfaceHolder.Callback,Runnable ,SensorEventListener{
	private Context lContext;			//上下文传递
	private SurfaceHolder surfaceHolder;		//控制视图接口
	private float accelerationX,accelerationY;//加速度值
	private Paint paint;//画笔
	private Canvas canvas;//画布
	private int screenW,screenH;	//屏幕的宽高
	private SensorManager sensorManager;	//传感器管理
	private Thread thread;		//显示小球的线程
	private boolean flag;		//初始化标记
	private Ball lBall;		//自定义小球类
	private Bitmap ballBitmap;	//获取显示小球的图片
	private Bitmap bgBitmap;	//背景图片


	private boolean isBall;
	public BallView(Context context) {
		super(context);
		this.lContext = context;
	
		this.setKeepScreenOn(true);					//设置屏幕保持开启状态
		this.setFocusable(true);					//设置此视图可以接收焦点
		this.setFocusableInTouchMode(true);			//触摸模式下可以接收焦点
		
		paint = new Paint();						//创建画笔
		paint.setStyle(Style.FILL_AND_STROKE);	//画笔样式
		paint.setColor(Color.RED);					//画笔颜色
		paint.setAntiAlias(true);					//开启抗锯齿

		
		this.surfaceHolder = this.getHolder();//获取控制器
		this.surfaceHolder.addCallback(this);//添加监听
		 //获取传感器服务与声音服务
		 sensorManager = (SensorManager) lContext.getSystemService(Context.SENSOR_SERVICE);
		
	}



	@Override
	public void run() {
		while(flag){
			 draw();	//调用绘制方法
			parameter(); //设置参数
		}
	}

	//绘图方法
	private void draw() {
		try {
			canvas = this.surfaceHolder.lockCanvas();				//锁定当前画布
			canvas.drawColor(Color.WHITE);							//画布颜色
			Rect rect = new Rect(0,0,getWidth(),getHeight());		//屏幕宽高
			canvas.drawBitmap(bgBitmap,null,rect,null);		    //绘制背景图片
			lBall.draw(canvas, paint);								//绘制小球

		} catch (Exception e) {

		} finally {
			try {
				if (canvas != null)							    //如果画布不为空
					surfaceHolder.unlockCanvasAndPost(canvas);	//解锁画布
			} catch (Exception e2) {

			}
		}
	}

	//设置参数
	private void parameter() {
		//边界碰撞检测
		if(lBall.isCollision(0.0f, 0.0f, screenW, screenH)){
			isBall=true;
		}
		//设置小球的向量
		lBall.setDirectionVector(new Coordinate(accelerationX,
				accelerationY));
		//小球的运动坐标
		lBall.go(0.0f, 0.0f, screenW, screenH);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	/**
	 * 首次创建视图后立即调用，创建并初始化小球默认显示的位置
	 * @param holder
     */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenW = getWidth();		//获取屏幕宽度
		screenH = getHeight();		//获取屏幕高度
		registerListener();			//注册监听器
		if (isBall==false){

		}
		//获取背景图片
		bgBitmap=BitmapFactory.decodeResource(lContext.getResources(), R.drawable.bg);
        //获取小球图片
		ballBitmap = BitmapFactory.decodeResource(lContext.getResources(), R.drawable.ball);

		int ballHeight=ballBitmap.getHeight();		//获取小球高度
		int ballWidth=ballBitmap.getWidth();		//获取小球宽度
		//设置小球默认显示的位置
		lBall = new Ball((screenW-ballWidth) / 2, (screenH-ballHeight) / 2,ballBitmap);
		//设置小球活动范围
		lBall.setActivityArea(new Space(0.0f, 0.0f, screenW, screenH));
		flag = true;				//初始化标记
		thread = new Thread(this);//创建显示小球的线程
		thread.start();				//启动线程
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		 unRegisterListener();			//注销传感器监听
		 flag = false;					//修改标记
	}


	/**
	 *传感器监听器
     */
    public boolean registerListener(){
    	if(sensorManager!=null){
			//加速度传感器
    		Sensor aSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			//判断手机是否有加速度传感器
			if(aSensor == null){
    			Toast.makeText(lContext, "设备不支持", Toast.LENGTH_SHORT).show();
    			return false;
    		}
    		sensorManager.registerListener(this, aSensor,SensorManager.SENSOR_DELAY_NORMAL);
    		return true;
    	}
    	return false;
    }
    
    /**
	 * 注销传感器监听
	 */
	public void unRegisterListener() {
		if (sensorManager != null)
			sensorManager.unregisterListener(this);
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}


	private long preTime;			//之前的时间
	private long lastTime;	    //最后的时间
	private float lastX,lastY,lastZ;
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		//主要是为了清除爆发模式时我们人为因素增大的加速度
		accelerationX = 0;
		accelerationY = 0;
		//获取现在的时间
		long curTime = System.currentTimeMillis();
		if ((curTime - lastTime) > 10) {
			long diffTime = (curTime - lastTime);
			lastTime = curTime;
			float x = event.values[0];
			float	y = event.values[1];
			float z = event.values[2];
			//计算晃动速度
			float speed = Math.abs(x + y + z - lastX - lastY - lastZ)
					/ diffTime * 10000;
			//加快小球运动速度
			if (speed > 400d) {
				//剧烈晃动，进入疯狂模式～～～
				accelerationX = x*10;
				accelerationY = y*10;
				preTime = curTime;		//初始化时间
			}
			//重新计算三个轴的值
			lastX = x;
			lastY = y;
			lastZ = z;
		}
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		return super.onTouchEvent(event);
	}
}
