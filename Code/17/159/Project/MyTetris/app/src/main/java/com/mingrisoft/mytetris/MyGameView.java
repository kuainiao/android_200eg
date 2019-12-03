package com.mingrisoft.mytetris;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * 文件名:MyGameView.java 
 * 文件功能描述:自定义游戏view,用于游戏页面 
 * 开发时间:2016年7月29日
 * 公司网址:www.mingribook.com 
 * 开发单位:吉林省明日科技有限公司
 */
public class MyGameView extends View {
	ImageManager imageManager;// 图像加载显示类
	SoundManager soundManager;// 声音加载显示类
	Ground ground;// 定义背景墙方块的类
	Shape shape;// 下落方块的定义类

	long touchDownTime;// 记录触摸屏幕时按下时间
	int touchDownX;// 屏幕按下时的X值
	int touchDownY;// 屏幕按下时的Y值
	int touchActions;// 接下后执行了几个控制游戏的动作
	Timer touchTimer = null;// 点击在虚拟按钮上后用于连续动作的计时器.
	Timer mainTimer = null;// 主计时器，用于游戏时主方块的定时下移。
	boolean bGameRun = false;// 游戏是否正在运行的变量。
	boolean bGameOver = false;// 游戏是否结束的变量，主要用于显示游戏结束的字样
	boolean bGamePause = false;// 游戏是否暂停中，因为游戏过程中，可能会有电话等进入，这时需要暂停游戏

	boolean bEnableMusic;// 用于控制背景音乐播放的变量
	boolean bEnableEffect;// 控制音效播放的变量
	boolean bShadow;// 是否显示移动方块的影子
	boolean bContrarotate;// 是否逆时针转动
	String backImageString;// 配置中的背景图片文件名
	String shapeImageString;// 配置中的背景图片文件名

	public MyGameView(Context context) {// 构建方法
		super(context);
		Init(context);// 加载游戏数据
	}

	public MyGameView(Context context, AttributeSet attrs) {// 重构方法
		super(context, attrs);
		Init(context);// 加载游戏数据
	}

	protected void onDraw(Canvas canvas) {// 重新绘制画面
		super.onDraw(canvas);
		imageManager.drawBackGround(canvas);// 游戏绘制时,先绘制背景图等
		ground.drawMe(canvas);// 绘制 *20的方块背景墙
		shape.drawMe(canvas);// 绘制方块
		if (bGameOver)// 如果游戏结束了，则显示结束的画面
			imageManager.drawGameOver(canvas);// 绘制游戏结束画面
		else if (bGamePause)// 判断是否时暂停游戏
			imageManager.drawGamePause(canvas);// 绘制游戏暂停画面
	}

	public void Init(Context context) {// 加载游戏数据
		loadConfig(context);// 加载游戏设置中设定好的2个变量的值
		imageManager = new ImageManager(context);// 初始化图像
		soundManager = new SoundManager(context);// 初始化声音
		ground = new Ground();// 初始化背景墙方块
		shape = new Shape();// 初始化方块类
		shape.createShape(new Random().nextInt(7));// 获取第一个方块的类
		if (bEnableMusic)// 如果开启了背景音乐选项,则循环播放背景音乐
			soundManager.playBackMusic();// 播放音乐
		setFocusable(true);// 设置焦点,有些实物键盘需要有焦点才可以控制
	}

	/**
	 * 加载游戏设置
	 */
	public void loadConfig(Context context) {
		SharedPreferences sPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);// 加载配置后读取相应的值
		bEnableMusic = sPreferences.getBoolean("bEnableMusic", true);// 获取音乐是否开启
		bEnableEffect = sPreferences.getBoolean("bEnableEffect", true);// 获取音效是否开启
		bShadow = sPreferences.getBoolean("bShadow", true);// 获取是否显示阴影
		bContrarotate = sPreferences.getBoolean("bContrarotate", true);// 是否保存的游戏数据
		backImageString = sPreferences.getString("backImage", "back1.jpg");// 获游戏背景
		shapeImageString = sPreferences.getString("shapeImage", "class");// 获取方块样式
	}

	/**
	 * 保存整个游戏的状态，以便在下一次继续游戏。
	 */
	public void saveGame() {
		Editor editor = getContext().getSharedPreferences("save_game",
				Activity.MODE_PRIVATE).edit();
		editor.putBoolean("Runing", bGameRun);// 把游戏是否在运行状态存入editor
		if (bGameRun) {// 判断游戏是否运行
			ground.saveStatus(editor);
			shape.saveStatus(editor);
			imageManager.saveStatus(editor);
		}
		editor.commit();// 关闭editor
	}

	/**
	 * 加载游戏页面并开始游戏。
	 * 
	 * @return
	 */
	public boolean loadGame() {
		SharedPreferences sp = getContext().getSharedPreferences("save_game",
				Activity.MODE_PRIVATE);
		if (sp.getBoolean("Runing", false)) {
			ground.loadStatus(sp);// 加载背景设置
			shape.loadStatus(sp);// 加载方块设置
			imageManager.loadStatus(sp);// 加载游戏数据

			switch (imageManager.getLevel()) {// 游戏级别
			case 1:
				startGame(1000);// 级别为1的时候开始游戏 1000为下落速度
				break;
			case 2:
				startGame(850);// 级别为2的时候开始游戏 850为下落速度
				break;
			case 3:
				startGame(700);// 级别为3的时候开始游戏 700为下落速度
				break;
			case 4:
				startGame(600);// 级别为4的时候开始游戏 600为下落速度
				break;
			case 5:
				startGame(500);// 级别为5的时候开始游戏 500为下落速度
				break;
			case 6:
				startGame(400);// 级别为6的时候开始游戏 400为下落速度
				break;
			case 7:
				startGame(300);// 级别为7的时候开始游戏 300为下落速度
				break;
			case 8:
				startGame(200);// 级别为8的时候开始游戏 200为下落速度
				break;
			case 9:
				startGame(100);// 级别为9的时候开始游戏 100为下落速度
				break;
			default:
				break;
			}

			return true;
		}

		return false;

	}

	// 用于结束时清除音乐等
	public void finish() {
		soundManager.stopBackMusic();// 停止音乐
		if (mainTimer != null)
			mainTimer.cancel();// 清楚事件线程
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!bGameRun) {// 判断是否游戏进行中
			return true; // 退出方法返回true
		}
		// 处理触屏的事件,这儿是控制游戏的关键.
		if (event.getAction() == MotionEvent.ACTION_UP) {// 手在屏幕上抬起
			if (touchTimer != null) {
				touchTimer.cancel();// 取消计时器
				touchTimer = null;// 设置计时器为空
			}
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {// 手指在屏幕上移动
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {// 手指在屏幕按下
			// 记录接下的坐标
			touchDownX = (int) event.getX();
			touchDownY = (int) event.getY();
			touchActions = 0;
			touchDownTime = System.currentTimeMillis();// 系统当前事件
			if (touchDownY > imageManager.getArrowButtonTop()) {
				if (touchDownX > imageManager.getArrowButtonWidth() / 2
						&& touchDownX < imageManager.getArrowButtonWidth() * 3 / 2) {// 点击在下移的虚拟按钮上
					touchActions++;
					if (shape.moveDown(ground)) {// 方块向下移动
						// soundManager.play(1, 0);
						postInvalidate();// 刷新布局
						// 单机向下 直接到底
						shape.moveDownOver(ground);// 方块向下移动
						refresh();// 刷新状态。
						// 以下代码是处理一直按住不放时，定时下移，这样符合操作习惯。
						if (touchTimer != null)
							touchTimer.cancel();
						touchTimer = new Timer();
						touchTimer.schedule(new TimerTask() {
							@Override
							public void run() {

								if (shape.moveDown(ground)) {// 方块向下移动
									postInvalidate();// 刷新布局
								}
							}
						}, 300, 200);
					}
				} else if (touchDownX > imageManager.widthPixels
						- imageManager.getArrowButtonWidth() * 2
						&& touchDownX < imageManager.widthPixels
								- imageManager.getArrowButtonWidth()) {// 暂停游戏按钮
					if (bGamePause) {
						bGamePause = false;
						if (bEnableMusic)
							soundManager.playBackMusic();// 播放音乐
						touchActions = 1;// 这儿是点击继续游戏后，不加的话，会现出ACTIO_UP的消息。
						postInvalidate();// 刷新游戏界面
						return true;
					} else {
						pauseGame();// 是否暂停游戏
					}
				}
			}

			if (touchDownY > imageManager.getArrowButtonTop()
					- imageManager.getArrowButtonWidth()
					&& touchDownY < imageManager.getArrowButtonTop()) {// 点击在左移虚拟按钮上

				int w = imageManager.getArrowButtonWidth();

				if (touchDownX < w)// 点击在左移的虚拟按钮上。
				{
					touchActions++;
					if (shape.moveLeft(ground)) {// 方块向左移动
						postInvalidate();// 刷新布局
						soundManager.play(1, 0);// 播放声音
					}
					// 以下代码是处理一直按住不放时，定时左移，这样符合操作习惯。
					if (touchTimer != null)
						touchTimer.cancel();
					touchTimer = new Timer();
					touchTimer.schedule(new TimerTask() {
						@Override
						public void run() {
							if (shape.moveLeft(ground)) {// 向左移动方块
								postInvalidate();// 刷新布局
							}
						}
					}, 300, 200);

				} else if (touchDownX > imageManager.getArrowButtonWidth() * 3 / 2
						&& touchDownX < imageManager.getArrowButtonWidth() * 5 / 2) {// 点击在右移的虚拟按钮上.
					touchActions++;
					if (shape.moveRight(ground)) {// 方块向右移动
						postInvalidate();// 刷新布局
						soundManager.play(1, 0);
					}
					// 以下代码是处理一直按住不放时，定时右移，这样符合操作习惯。
					if (touchTimer != null)
						touchTimer.cancel();// 取消线程
					touchTimer = new Timer();// 初始化线程
					touchTimer.schedule(new TimerTask() {// 时间线程
								@Override
								public void run() {

									if (shape.moveRight(ground)) {// 方块向右移动
										postInvalidate();// 刷新布局
									}
								}
							}, 300, 200);
				} else if (touchDownX > imageManager.getWidthPixels()
						- imageManager.getArrowButtonWidth()) {// 点击改变按钮

					if (shape.rotate()) {// 转动方块
						postInvalidate();// 刷新布局
						soundManager.play(2, 0);// 播放声音
					}
				}

			}
		}
		// 这儿需要返回true，要不然会有问题
		return true;
	}

	// 开始启动定时器，游戏开始。
	// delay是方块停留的时间，时间越短，则速度越快
	public void startGame(int delay) {
		if (mainTimer != null) {
			mainTimer.cancel();// 取消线程
		}

		mainTimer = new Timer();// 计时器
		bGameRun = true;
		mainTimer.schedule(new TimerTask() { // 计划运行时间间隔
					public void run() {
						refresh(); // 过delay毫秒调用一下refresh()
					}
				}, delay, delay);
	}

	// 是否暂停游戏
	public void pauseGame() {
		if (!bGamePause) {
			bGamePause = true;// 暂停
			soundManager.stopBackMusic();// 停止音乐
			postInvalidate();// 刷新布局
		}
	}

	// 时间到了后，直接让方块下移一格，同时刷新所有的屏幕与状态。进入不同级别
	public void refresh() {

		if (bGamePause)
			return;// 游戏暂停时，不处理。

		if (!shape.moveDown(ground)) {// 方块已经不能下降，说明已经到底了。

			// 从移动的方块加入到方块背景墙中。
			ground.add(shape.left, shape.top, shape);

			if (ground.removeRow() > 0) // 如果有新消去的行数，则判断是不是要进入下一关卡
			{
				switch (imageManager.getLevel()) {
				case 1:// 消去10行进入第2关
					if (imageManager.getRemoveRows() >= 10) {// 判断消去行数
						imageManager.addLevel();// 增加级别
						startGame(850);// 开启游戏速度
					}
					break;
				case 2:// 消去30行进入第3关
					if (imageManager.getRemoveRows() >= 30) {
						imageManager.addLevel();
						startGame(700);
					}
					break;
				case 3:// 消去60行进入第4关
					if (imageManager.getRemoveRows() >= 60) {
						imageManager.addLevel();
						startGame(600);
					}
					break;
				case 4:// 消去100行进入第5关
					if (imageManager.getRemoveRows() >= 100) {
						imageManager.addLevel();
						startGame(500);
					}
					break;
				case 5:// 消去200行进入第6关
					if (imageManager.getRemoveRows() >= 200) {
						imageManager.addLevel();
						startGame(400);
					}
					break;
				case 6:// 消去300行进入第7关
					if (imageManager.getRemoveRows() >= 300) {
						imageManager.addLevel();
						startGame(300);
					}
					break;
				case 7:// 消去400行进入第8关
					if (imageManager.getRemoveRows() >= 400) {
						imageManager.addLevel();
						startGame(200);
					}
					break;
				case 8:// 消去500行进入第9关
					if (imageManager.getRemoveRows() >= 500) {
						imageManager.addLevel();
						startGame(100);
					}
					break;
				default:
					break;
				}
			}

			shape.createShape(imageManager.getNextShape());// 重新生成一个新的方块。
			// 测试新的方块出现的位置是否已经有内容存在，如果有说明游戏结束了。
			if (!shape.checkBound(Shape.NOW, ground)) {
				bGameRun = false;// 运行状态结束
				bGameOver = true;// 游戏确定结束
				mainTimer.cancel();// 取消计时器停止方块移动
				mainTimer = null;// 取消计时器停止活动

				// 线程中不能直接操作与界面相关的代码，所以需要加入到Handler中。
				Message msg = new Message();
				dialogHandler.sendMessageDelayed(msg, 500);
			}
		}
		this.postInvalidate(); // 使用postInvalidate();刷新
	}

	Handler dialogHandler = new Handler() {
		public void handleMessage(Message msg) {
			// 检测是否进入高分榜，如果进入了就显示对话框输入姓名
			showInputDialog();
		};

	};

	/**
	 * 方法名:showInputDialog 功能描述: 显示高分姓名输入项目输入框 Created by 明日科技 on 2016年7月29日
	 */
	public void showInputDialog() {

		final BestScore bestScore = new BestScore(getContext());
		// 加载高分榜的信息
		bestScore.loadConfig();
		final int score = imageManager.getScore();
		// 测试当前分数是否进入高分榜
		if (bestScore.testScore(score)) {
			// 准备显示对话框。
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			LayoutInflater inflater = LayoutInflater.from(getContext());
			final View view = inflater.inflate(R.layout.dialog_input, null);// 加载布局文件
			builder.setView(view);

			builder.setTitle("高分排行");
			builder.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 点击确定按钮后，处理高分榜的更新。
							EditText editText = (EditText) view
									.findViewById(R.id.editText1);
							String string = editText.getText().toString();
							if (string.length() == 0)
								string = "无名";
							bestScore.insertScore(string, score);
							bestScore.saveConfig();

							// 显示高分榜的界面
							Intent intent = new Intent();
							intent.setClass(getContext(), HighActivity.class);
							getContext().startActivity(intent);
						}
					});

			builder.show();
		}

	}

	// ====================================================

	/**
	 * 该类用于管理图像的加载与绘制.
	 */
	public class ImageManager {
		Bitmap backImage;// 背景图片
		Bitmap arrowLeft, arrowRight, arrowDown, arrowChange, arrowPause;// 保存3个方向的按钮图片
		Bitmap shapeImage[] = new Bitmap[8];// 保存8个不同色块的图片
		Bitmap nextImage[] = new Bitmap[7];// 保存7个不同类型方块的图片，为显示下一个方块准备。
		Bitmap gameOverBitmap;// 游戏结束的图片。
		Bitmap gamePauseBitmap;// 游戏暂停的图片。

		int widthPixels;// 屏幕宽度
		int heightPixels;// 屏幕高度
		float scaleWidht;// 宽度的缩放比率
		float scaleHeight;// 高度的缩放比率
		int shapeWidth = 1;// 一个方块的块
		int shapeHeight = 1;// 一个方块的高
		int boxLeft = 100;// 方块的左边的X坐标
		int boxTop = 50;// 方块的上边的Y坐标

		int removeRows = 0;// 消去的行数。
		int score = 0;// 得分
		int level = 1;// 关卡
		int nextShapeType = 0;// 下一个方块的类型，正确值为0-6

		Paint paint;// 声明画笔，画边框线用
		Paint textPaint;// 声明画笔，输出文字用。
		Paint alphaPaint;// 透明画笔，用于显示半透明的小方块
		Rect dst = new Rect();

		public ImageManager(Context context) {
			widthPixels = context.getResources().getDisplayMetrics().widthPixels;// 获取屏幕宽度
			heightPixels = context.getResources().getDisplayMetrics().heightPixels-80;// 获取屏幕高度
			// 因为图片等资源是按1280*720来准备的，如果是其它分辨率，则计算机缩放比率，适应屏幕做准备
			scaleWidht = ((float) widthPixels / 720 * 5 / 6);
			scaleHeight = ((float) heightPixels / 1280 * 5 / 6);
			// 计算机方块背景墙的左上角坐标，这是适应各种分辨率后的坐标
			boxLeft = (int) (boxLeft * scaleWidht);
			boxTop = (int) (boxTop * scaleHeight);
			paint = new Paint(Paint.DITHER_FLAG);// 创建一个画笔，用于画方块背景墙外的矩形
			paint.setStyle(Style.STROKE);// 设置非填充
			paint.setStrokeWidth(5);// 笔宽5像素
			paint.setColor(Color.GRAY);// 设置为灰色
			paint.setAntiAlias(true);// 锯齿不显示
			textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 创建一个画笔，用于显示文字
			textPaint.setTextSize(30 * scaleWidht);
			textPaint.setColor(0xffffffff);// 设置为白
			textPaint.setTextAlign(Align.CENTER);// 设置文字为居中对齐
			alphaPaint = new Paint();
			try {

				InputStream is = getResources().getAssets().open(
						"arrow_left.png");// 加载左按钮图片
				arrowLeft = BitmapFactory.decodeStream(is);
				is = getResources().getAssets().open("arrow_right.png");// 加载右按钮图片
				arrowRight = BitmapFactory.decodeStream(is);
				is = getResources().getAssets().open("arrow_down.png");// 加载向下按钮图片
				arrowDown = BitmapFactory.decodeStream(is);
				is = getResources().getAssets().open("arrow_change.png");// 加载转动按钮图片
				arrowChange = BitmapFactory.decodeStream(is);
				is = getResources().getAssets().open("btn_md_up.png");// 加载暂停按钮图片
				arrowPause = BitmapFactory.decodeStream(is);
				is = getResources().getAssets().open("gameover.png");// 加载游戏结束图片
				gameOverBitmap = BitmapFactory.decodeStream(is);
				is = getResources().getAssets().open("pause.png");// 加载游戏暂停图片
				gamePauseBitmap = BitmapFactory.decodeStream(is);
				if (!shapeImageString.equals("class")) {// 方块图片资源路径
					shapeImageString = "class";
				}
				for (int i = 0; i < 8; i++) {
					is = getResources().getAssets().open(
							shapeImageString + "/" + i + ".png");
					shapeImage[i] = BitmapFactory.decodeStream(is);
				}
				for (int i = 1; i < 8; i++) {
					is = getResources().getAssets().open(
							shapeImageString + "/next" + i + ".png");
					nextImage[i - 1] = BitmapFactory.decodeStream(is);
				}
				// // 计算出每一个小方块在不同屏幕上的大小
				shapeWidth = (int) (shapeImage[0].getWidth() * scaleWidht);
				shapeHeight = (int) (shapeImage[0].getHeight() * scaleHeight);

				if (!backImageString.equals("back1.jpg")
						&& !backImageString.equals("back2.jpg")
						&& !backImageString.equals("back3.jpg")) {// 判断背景图片
					backImageString = "back1.jpg";
				}
				is = getResources().getAssets().open(backImageString);
				backImage = BitmapFactory.decodeStream(is);

			} catch (IOException e) {

				e.printStackTrace();
			}

		}

		// 计算出方块的总宽度
		public int getShapeBoxsWidth() {
			return (int) (shapeWidth + 1) * 10 - 1;
		}

		// 计算机方块的总高度
		public int getShapeBoxsHeight() {
			return (int) (shapeHeight + 1) * 20 - 1;
		}

		// 将backImage与箭头按钮图像以及其它一些需要的内容绘制到屏幕上
		public void drawBackGround(Canvas canvas) {
			// 将背景绘制出来
			RectF rectF = new RectF(0, 0, widthPixels, heightPixels); // w和h分别是屏幕的宽和高，也就是你想让图片显示的宽和高
			canvas.drawBitmap(backImage, null, rectF, null);
			int top = getArrowButtonTop()
					- (int) (arrowDown.getWidth() * scaleWidht);// 计算4个箭头按钮的Y坐标位置
			drawBitmap(arrowLeft, 0, top, canvas, 0xff);// 第一个按钮在左边。
			int left = (int) (arrowDown.getWidth() * scaleWidht) * 3 / 4;// 计算出中间按钮的X坐标
			drawBitmap(arrowDown, left, getArrowButtonTop(), canvas, 0xff);// 第二个按钮在中间。
			left = (int) (arrowDown.getWidth() * scaleWidht) * 3 / 2;// 计算出右边按钮的X坐标
			drawBitmap(arrowRight, left, top, canvas, 0xff);// 第三个按钮在右边。
			top = getArrowButtonTop()
					- (int) (arrowRight.getWidth() * scaleWidht);// 计算出右边按钮的X坐标

			left = widthPixels - (int) (arrowRight.getWidth() * scaleWidht) - 5;
			drawBitmap(arrowChange, left, top, canvas, 0xff);// 第4个按钮
			left = widthPixels - (int) (arrowRight.getWidth() * scaleWidht) * 3
					/ 2;
			top = heightPixels - (int) (arrowRight.getWidth() * scaleWidht);
			drawBitmap(arrowPause, left, top, canvas, 0xff);// 第五个按钮
			// 在方块的四周画出一个外框，线粗为2像素
			canvas.drawLine(boxLeft - 5, boxTop - 5, boxLeft - 5, boxTop
					+ getShapeBoxsHeight() + 5, paint);// 画左边的线
			canvas.drawLine(boxLeft - 5, boxTop - 5, boxLeft
					+ getShapeBoxsWidth() + 5, boxTop - 5, paint);// 画上边的线
			canvas.drawLine(boxLeft + getShapeBoxsWidth() + 5, boxTop - 5,
					boxLeft + getShapeBoxsWidth() + 5, boxTop
							+ getShapeBoxsHeight() + 5, paint);// 画右边的线
			canvas.drawLine(boxLeft - 5, boxTop + getShapeBoxsHeight() + 5,
					boxLeft + getShapeBoxsWidth() + 5, boxTop
							+ getShapeBoxsHeight() + 5, paint);// 画出下边的线
			// 文字是居中对齐，所以计算机右边部分的中间坐标left值
			int textLeft = boxLeft + getShapeBoxsWidth();
			textLeft = (widthPixels - textLeft) / 2 + textLeft;

			// 文本默认是底部对齐，所在需要在Y坐标上稍做计算，让它顶部对齐
			FontMetrics fm = paint.getFontMetrics();
			int boxTopT = boxTop + 10;// 文字顶部高度
			canvas.drawText("下一类型", textLeft, boxTopT + fm.bottom - fm.top,
					textPaint);

			canvas.drawText("当前关卡", textLeft, boxTopT + shapeHeight * 4
					+ fm.bottom - fm.top, textPaint);

			canvas.drawText(level + "", textLeft, boxTopT + shapeHeight * 6
					+ fm.bottom - fm.top, textPaint);

			canvas.drawText("当前分数", textLeft, boxTopT + shapeHeight * 8
					+ fm.bottom - fm.top, textPaint);

			canvas.drawText(score + "", textLeft, boxTopT + shapeHeight * 10
					+ fm.bottom - fm.top, textPaint);

			canvas.drawText("消去行数", textLeft, boxTopT + shapeHeight * 12
					+ fm.bottom - fm.top, textPaint);

			canvas.drawText(removeRows + "", textLeft, boxTopT + shapeHeight
					* 14 + fm.bottom - fm.top, textPaint);

			// 画出下一个方块的小图
			drawNextShape(nextShapeType, canvas);

		}

		/**
		 * 画出游戏结束的图片
		 */
		public void drawGameOver(Canvas canvas) {

			int top = boxTop + imageManager.getShapeBoxsHeight() / 3;
			drawBitmap(gameOverBitmap, boxLeft, top, canvas, 0xff);
		}

		/**
		 * 画出游戏暂停的图片
		 */
		public void drawGamePause(Canvas canvas) {

			int top = boxTop + imageManager.getShapeBoxsHeight() / 3;
			drawBitmap(gamePauseBitmap, boxLeft, top, canvas, 0xff);
		}

		// 将下一个出现的方块类型在屏幕上绘制出来
		// int type为类型，其值为0－6的范围
		public void drawNextShape(int type, Canvas canvas) {
			if (type < 0 || type > 6)
				return;
			int left = boxLeft + getShapeBoxsWidth();
			left = (widthPixels - left) / 2 + left
					- (int) (nextImage[type].getWidth() * scaleWidht) / 2;
			int top = (int) (boxTop + 1.5 * (float) shapeHeight);
			drawBitmap(nextImage[type], left, top, canvas, 0xff);
		}

		// 将10*20的俄罗斯方块数组中，将x行y列的方块画出来。行列从0开始。
		public void drawShapeBox(int type, int x, int y, Canvas canvas,
				int alpha) {
			if (x < 0 || x >= 10)
				return;
			if (y < 0 || y >= 20)
				return;
			// 计算出这个方块在屏幕上的位置
			int left = (int) (boxLeft + (shapeWidth + 1) * x);
			int top = (int) (boxTop + (shapeHeight + 1) * y);
			// 进行绘制。
			drawBitmap(shapeImage[type], left, top, canvas, alpha);
		}

		// 增加分数
		public void addScore(int score) {
			this.score += score;
		}

		// 获取分数
		public int getScore() {
			return score;
		}

		// 增加消去的行数
		public void addRemoveRows(int n) {
			removeRows += n;
		}

		// 获取总消去的行数
		public int getRemoveRows() {
			return removeRows;
		}

		// 获取当前关卡值
		public int getLevel() {
			return level;
		}

		// 进入下一关卡
		public void addLevel() {
			level++;
		}

		// 设置下一个方块类型
		public void setNextShape(int type) {
			nextShapeType = type;
		}

		// 获取下一个方块类型
		public int getNextShape() {
			return nextShapeType;
		}

		// 获取屏幕宽度
		public int getWidthPixels() {
			return widthPixels;
		}

		// 获取屏幕高度
		public int getHeightPixels() {
			return heightPixels;
		}

		// 获取下方虚拟按钮的Y坐标。
		public int getArrowButtonTop() {
			return heightPixels - (int) (arrowLeft.getHeight() * scaleHeight);
		}

		// 获取虚拟按钮的宽度
		public int getArrowButtonWidth() {
			return (int) (arrowLeft.getWidth() * scaleWidht);
		}

		// 将bitmap图片在指定的坐标绘画
		public void drawBitmap(Bitmap bitmap, int left, int top, Canvas canvas,
				int alpha) {
			dst.left = left;
			dst.top = top;
			dst.right = dst.left + (int) (bitmap.getWidth() * scaleWidht);
			dst.bottom = dst.top + (int) (bitmap.getHeight() * scaleHeight);

			alphaPaint.setAlpha(alpha);
			canvas.drawBitmap(bitmap, null, dst, alphaPaint);
		}

		// 将游戏数据保存到XML文件中
		public void saveStatus(Editor editor) {
			editor.putInt("game_level", level);
			editor.putInt("game_score", score);
			editor.putInt("game_rows", removeRows);
			editor.putInt("game_next_type", nextShapeType);
		}

		// 将游戏数据从XML文件中加载
		public void loadStatus(SharedPreferences sp) {
			level = sp.getInt("game_level", 0);
			score = sp.getInt("game_score", 0);
			removeRows = sp.getInt("game_rows", 0);
			nextShapeType = sp.getInt("game_next_type", 0);
		}
	}

	// ====================================================
	// 该类用于管理声音的加载与播放.
	public class SoundManager {
		Context context;

		public SoundManager(Context context) {// 构造函数中加载声音文件
			this.context = context;
			initSounds();
			loadSfx(R.raw.move, 1);// 移动的声音
			loadSfx(R.raw.turn, 2);// 转动的声音
			loadSfx(R.raw.elimination, 3);// 消行的声音
			loadSfx(R.raw.down, 4);// 直接到底的声音
		}

		int streamVolume;// 音效的音量
		private MediaPlayer mediaPlayer;// 定义Mediaplay对象播放背景音乐
		private SoundPool soundPool;// 定义SoundPool 对象播放音效
		private HashMap<Integer, Integer> soundPoolMap; // 定义HASH表

		/**
		 * initSounds(); 初始化声音系统
		 */
		public void initSounds() {
			// 初始化soundPool 对象,第一个参数是允许有多少个声音流同时播放,第2个参数是声音类型,第三个参数是声音的品质
			soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 100);
			// 初始化HASH表
			soundPoolMap = new HashMap<Integer, Integer>();
			// 获得声音设备和设备音量
			AudioManager mgr = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		}

		/**
		 * loadSfx 加载音乐到播放制定id raw：指定音乐标识 ID：指定音乐id
		 */
		public void loadSfx(int raw, int ID) {
			// 把资源中的音效加载到指定的ID(播放的时候就对应到这个ID播放就行了)
			soundPoolMap.put(ID, soundPool.load(context, raw, ID));
		}

		/**
		 * play():播放声音方法; sound:要播放的音效的ID, loop:循环次数 Returns:
		 * 
		 */
		public void play(int sound, int uLoop) {
			if (bEnableEffect)
				soundPool.play(soundPoolMap.get(sound), streamVolume,
						streamVolume, 1, uLoop, 1f);
		}

		// 播放背景音乐
		public void playBackMusic() {
			mediaPlayer = MediaPlayer.create(context, R.raw.back);
			mediaPlayer.setLooping(true);
			mediaPlayer.start();
		}

		// 停止背景音乐的播放
		public void stopBackMusic() {
			if (mediaPlayer != null) {
				mediaPlayer.stop();// 停止声音播放
				mediaPlayer.release();// 释放声音资源
				mediaPlayer = null;// 设置mediaPlayer为空
			}
		}
	}

	// ====================================================
	// 下落方块的定义类
	public class Shape {
		private int left;// 与左边的距离
		private int top;// 与上边的距离
		private int type;// 当前方块类型

		private int status;// 方块状态，0－4表示不同的方向状态
		public static final int LEFT = 1, RIGHT = 2, DOWN = 3, ROTATE = 4,
				NOW = 5;// 移动标识

		// 开始生成一个新的方块
		public void createShape(int type) {
			this.type = type;
			imageManager.setNextShape(new Random().nextInt(7));
			shape.setStatus(0);

			switch (type) { // 以下的内容用于初始化不同方块的初始位置
			case 0:
				shape.setLeftTop(3, -1);
				break;
			case 1:
				shape.setLeftTop(4, -1);
				break;
			case 2:
				shape.setLeftTop(3, -1);
				break;
			case 3:
				shape.setLeftTop(3, -2);
				break;
			case 4:
				shape.setLeftTop(3, -2);
				break;
			case 5:
				shape.setLeftTop(3, -1);
				break;
			case 6:
				shape.setLeftTop(3, -1);
				break;
			default:
				break;
			}

		}

		// 方块的形状 第一组代表方块类型有S、Z、L、J、I、O、T 7种
		// 第二组为图型的型状态
		// 每三组图型数据，根据这些数来描绘在view中
		private final int shapes[][][] = new int[][][] {
				// i
				{ { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 },
						{ 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 } },
				// s
				{ { 0, 2, 2, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 2, 0, 0, 0, 2, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0 },
						{ 0, 2, 2, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 2, 0, 0, 0, 2, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0 } },
				// z
				{ { 3, 3, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 3, 0, 0, 3, 3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0 },
						{ 3, 3, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 3, 0, 0, 3, 3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0 } },
				// J
				{ { 0, 0, 4, 0, 0, 0, 4, 0, 0, 4, 4, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 4, 4, 4, 0, 0, 0, 4, 0, 0, 0, 0, 0 },
						{ 0, 4, 4, 0, 0, 4, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 4, 0, 0, 0, 4, 4, 4, 0, 0, 0, 0, 0 } },
				// l(竖右勾)
				{ { 0, 5, 0, 0, 0, 5, 0, 0, 0, 5, 5, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 5, 0, 5, 5, 5, 0, 0, 0, 0, 0 },
						{ 0, 5, 5, 0, 0, 0, 5, 0, 0, 0, 5, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 5, 5, 5, 0, 5, 0, 0, 0, 0, 0, 0, 0 } },

				// o(田字)
				{ { 0, 6, 6, 0, 0, 6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 6, 6, 0, 0, 6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 6, 6, 0, 0, 6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 6, 6, 0, 0, 6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
				// T
				{ { 0, 7, 0, 0, 7, 7, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 7, 0, 0, 7, 7, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 0, 0, 7, 7, 7, 0, 0, 7, 0, 0, 0, 0, 0, 0 },
						{ 0, 7, 0, 0, 0, 7, 7, 0, 0, 7, 0, 0, 0, 0, 0, 0 } } };

		// 将游戏数据保存到XML文件中
		public void saveStatus(Editor editor) {

			editor.putInt("shape_type", type);
			editor.putInt("shape_status", status);
			editor.putInt("shape_left", left);
			editor.putInt("shape_top", top);

		}

		// 将游戏数据从XML文件中加载
		public void loadStatus(SharedPreferences sp) {
			type = sp.getInt("shape_type", 0);
			status = sp.getInt("shape_status", 0);
			left = sp.getInt("shape_left", 4);
			top = sp.getInt("shape_top", 0);
		}

		// 向左移动方块,成功返回true,不能移动了返回false
		public synchronized boolean moveLeft(Ground ground) {
			if (checkBound(Shape.LEFT, ground)) {
				left--;
				soundManager.play(1, 0);
				return true;
			}
			return false;
		}

		// 向右移动方块,成功返回true,不能移动了返回false
		public synchronized boolean moveRight(Ground ground) {
			if (checkBound(Shape.RIGHT, ground)) {
				left++;
				soundManager.play(1, 0);
				return true;
			}
			return false;
		}

		// 向下移动方块,成功返回true,不能移动了返回false
		// 这个需要考虑多线程的问题(1-refresh schedule, 2-event)
		public synchronized boolean moveDown(Ground ground) {
			if (checkBound(Shape.DOWN, ground)) {
				top++;
				return true;
			}
			return false;
		}

		// 向下移动方块，一直到底部为止
		// 这个需要考虑多线程的问题(1-refresh schedule, 2-event)
		public synchronized void moveDownOver(Ground ground) {
			while (checkBound(Shape.DOWN, ground)) {
				top++;
			}
		}

		// 旋转其实就是转到下一个图形中，通过改变status的值即可
		public synchronized boolean rotate() {
			if (checkBound(Shape.ROTATE, ground)) {

				if (bContrarotate) {// 逆时针转动
					status++;
					if (status > 3)
						status = 0;
				} else {// 顺时针转动
					status--;
					if (status < 0)
						status = 3;
				}

				soundManager.play(2, 0);
				return true;
			}
			return false;
		}

		/** 绘制Shape需要传入画布Canvas **/
		// 根据body中的值，绘制出这个方块
		public synchronized void drawMe(Canvas canvas) {

			if (bShadow) { // 如果开启了影子功能，则先将方块直接下降到底部，画出半透明的影子
				int saveTop = top;
				moveDownOver(ground);
				for (int x = 0; x < 4; x++) {
					for (int y = 0; y < 4; y++) {
						if (getValue(x, y) > 0) {
							int tempx = (x + left);
							int tempy = (y + top);
							imageManager.drawShapeBox(getValue(x, y), tempx,
									tempy, canvas, 0x55);
						}
					}
				}
				// 恢复原来的位置
				top = saveTop;
			}

			// 画出正在移动的方块
			for (int x = 0; x < 4; x++) {
				for (int y = 0; y < 4; y++) {
					if (getValue(x, y) > 0) {
						int tempx = (x + left);
						int tempy = (y + top);
						imageManager.drawShapeBox(getValue(x, y), tempx, tempy,
								canvas, 0xff);
					}
				}
			}
		}

		// 检查Shape是否到达边界,这里需要考虑向左 向右和向下三种情况
		// 返回true，则是没有到边界，如果返回false,则已经到边界。
		public boolean checkBound(int action, Ground ground) {
			int temptop = top;
			int templeft = left;
			int saveStatus = status;
			switch (action) {
			case LEFT:
				templeft--;
				break;
			case RIGHT:
				templeft++;
				break;
			case DOWN:
				temptop++;
				break;
			case ROTATE:
				if (bContrarotate) {// 逆时针转动
					status++;
					if (status > 3)
						status = 0;
				} else {// 顺时针转动
					status--;
					if (status < 0)
						status = 3;
				}
				break;
			case NOW:
			}
			for (int x = 0; x < 4; x++) {
				for (int y = 0; y < 4; y++) {
					if (getValue(x, y) > 0) {// 如果单元格的值为1,需要判断它是否到达边界

						if (templeft + x < 0 || templeft + x >= 10 // X方向的边界判断
								|| temptop + y >= 20 // Y方向的边界判断
								|| ground.checkValue(templeft, temptop, x, y)) {
							status = saveStatus;
							return false;
						}// end if
					}
				}
			}// end for
			status = saveStatus;
			return true;
		}

		// 返回x,y点的值
		public int getValue(int x, int y) {
			return shapes[type][status][4 * y + x];
		}

		// 返回数组的值
		public int[][] getBody() {
			return shapes[type];
		}

		// 用于设置初始的位置
		public void setLeftTop(int left, int top) {
			this.left = left;
			this.top = top;
		}

		// 获取转动的状态,0为初始状态
		public int getStatus() {
			return status;
		}

		// 设置转动的状态
		public void setStatus(int status) {
			this.status = status;
		}

	}

	// ====================================================
	// 定义背景墙方块的类
	public class Ground {
		public int[][] body = new int[10][20];// 整个方块的数组，10x20格

		// 将游戏数据保存到XML文件中
		public void saveStatus(Editor editor) {
			for (int x = 0; x < 10; x++) {
				for (int y = 0; y < 20; y++) {
					editor.putInt("body_" + x + "_" + y, body[x][y]);
				}
			}
		}

		// 将游戏数据从XML文件中加载
		public void loadStatus(SharedPreferences sp) {
			for (int x = 0; x < 10; x++) {
				for (int y = 0; y < 20; y++) {
					body[x][y] = sp.getInt("body_" + x + "_" + y, 0);
				}
			}
		}

		// 检查指定的位置是否有方块，0为没有，其它为有方块
		public boolean checkValue(int left, int top, int x, int y) {
			if (top + y < 0)
				return false;
			if (body[left + x][top + y] == 0) {
				return false;
			}
			return true;
		}

		// 画出整个方块的情况。
		public void drawMe(Canvas canvas) {
			for (int x = 0; x < 10; x++) {// 列 --> X方向
				for (int y = 0; y < 20; y++) {// 行--> Y方向
					imageManager.drawShapeBox(body[x][y], x, y, canvas, 0xff);
				}
			}
		}

		/**
		 * 将一个shape方块加入到当前背景墙方块中
		 * 
		 * @param left
		 *            左边的距离
		 * @param top
		 *            上边的距离
		 */
		public void add(int left, int top, Shape shape) {
			for (int x = 0; x < 4; x++) {
				for (int y = 0; y < 4; y++) {
					if (shape.getValue(x, y) > 0) {
						try {
							body[left + x][top + y] = shape.getValue(x, y);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		/**
		 * removeRow 当所堆积的小框框满一行时，就要进行消行。
		 * 消行的逻辑是:以Y方向为基准，从最底下一行的开始检查，当某一行全部满格时，则进行消行
		 * 消行其实就是把上一行的数据填充到下一行的位置上，以此进行循环操作
		 */
		public int removeRow() {// 消掉多少行
			int c = 1;
			int removeRows = 0;// 统计消掉的行数
			for (int y = 20 - 1; y > 0; y--) {

				for (int x = 0; x < 10; x++) {// 计算这一行中的所有数的乘积，如果有一个或以上空的则积为0
					c = c * body[x][y];
				}

				if (c > 0) {// 这个大于0，则说明这一行是满的。
					removeRows++;
					for (int j = y; j > 0; j--) {

						for (int z = 0; z < 10; z++) {
							body[z][j] = body[z][j - 1];
						}
					}
					postInvalidate();// 刷新布局
					soundManager.play(3, 0);
					y++;
				}
				c = 1;
			}

			if (removeRows > 0) {// 如果有行被清除
				imageManager.addScore(removeRows * 10);// 增加行数*10分数
			} else {// 没有行数被清楚
				imageManager.addScore(5);// 增加5分
			}

			imageManager.addRemoveRows(removeRows);// 增加消去的行数
			return removeRows;
		}

	}

}
