package com.mingrisoft.myguesscard;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	//声明控件
	private TextView outPut;
	private ImageView imageFace;
	private ImageView image_View1;
	private ImageView image_View2;
	private ImageView image_View3;
	private ProgressBar progressBar;
	private Button button_start;
	private Button button_end;
	private MyHandler myHandler;	//进度条线程
	private int i;	//进度条参数
    //扑克牌图片数组
	private static int[] poker = {R.drawable.poker_a,R.drawable.poker_2,R.drawable.poker_3};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//建立循环消息队列
		HandlerThread handlerThread = new HandlerThread("thread");
		//很重要，必须要写
		handlerThread.start();
		myHandler = new MyHandler(handlerThread.getLooper());
		//取得控件实例
		outPut = (TextView)findViewById(R.id.outPut);
		imageFace = (ImageView)findViewById(R.id.imageface);
		image_View1 = (ImageView)findViewById(R.id.image1);
		image_View2 = (ImageView)findViewById(R.id.image2);
		image_View3 = (ImageView)findViewById(R.id.image3);
		progressBar = (ProgressBar)findViewById(R.id.progressbar);
		button_start = (Button)findViewById(R.id.start);
		button_end = (Button)findViewById(R.id.end);
		//洗牌
		random();
		//牌1监听器
		image_View1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                //设置图片
				image_View1.setImageDrawable(getResources().getDrawable(poker[0]));
				image_View2.setImageDrawable(getResources().getDrawable(poker[1]));
				image_View3.setImageDrawable(getResources().getDrawable(poker[2]));
				//设置透明度
				image_View2.setAlpha(100);	//设置没被选中的牌渐隐效果
				image_View3.setAlpha(100);
                //判断点击是否是黑桃
				if(poker[0] == R.drawable.poker_a){
					outPut.setText("WOW，选对了哦，真厉害！你是怎么做到的？");
					imageFace.setImageDrawable(getResources().getDrawable(R.drawable.qq_suprise));
				}else{
					outPut.setText("真遗憾~~，这次运气不好，再来一次吧？");
					imageFace.setImageDrawable(getResources().getDrawable(R.drawable.qq_despise));
				}
			}

		});
		//牌2监听器
		image_View2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//设置图片
				image_View1.setImageDrawable(getResources().getDrawable(poker[0]));
				image_View2.setImageDrawable(getResources().getDrawable(poker[1]));
				image_View3.setImageDrawable(getResources().getDrawable(poker[2]));
                //设置透明度
				image_View1.setAlpha(100);
				image_View3.setAlpha(100);
				//判断点击是否是黑桃
				if(poker[1] == R.drawable.poker_a){
					outPut.setText("WOW，选对了哦，真厉害！你是怎么做到的？");
					imageFace.setImageDrawable(getResources().getDrawable(R.drawable.qq_suprise));

				}else{
					outPut.setText("真遗憾~~，这次运气不好，再来一次吧？");
					imageFace.setImageDrawable(getResources().getDrawable(R.drawable.qq_despise));

				}
			}

		});
		//牌3监听器
		image_View3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                //设置控件图片
				image_View1.setImageDrawable(getResources().getDrawable(poker[0]));
				image_View2.setImageDrawable(getResources().getDrawable(poker[1]));
				image_View3.setImageDrawable(getResources().getDrawable(poker[2]));
               //设置透明度
				image_View1.setAlpha(100);
				image_View2.setAlpha(100);
				//判断点击是否是黑桃
				if(poker[2] == R.drawable.poker_a){
					outPut.setText("WOW，选对了哦，真厉害！你是怎么做到的？");
					imageFace.setImageDrawable(getResources().getDrawable(R.drawable.qq_suprise));
				}else{
					outPut.setText("真遗憾~~，这次运气不好，再来一次吧？");
					imageFace.setImageDrawable(getResources().getDrawable(R.drawable.qq_despise));
				}
			}

		});

		button_start.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				outPut.setText("猜猜看黑桃A是 哪一张？");
				//设置进度条可见，启动进度条线程
				progressBar.setVisibility(View.VISIBLE);
				myHandler.post(progressBarThread);
				//初始化控件
				imageFace.setImageDrawable(getResources().getDrawable(R.drawable.qq_laugh));
				image_View1.setImageDrawable(getResources().getDrawable(R.drawable.poker_back));
				image_View2.setImageDrawable(getResources().getDrawable(R.drawable.poker_back));
				image_View3.setImageDrawable(getResources().getDrawable(R.drawable.poker_back));
				image_View1.setAlpha(255);
				image_View2.setAlpha(255);
				image_View3.setAlpha(255);
                //洗牌
				random();
			}
		});
		//end键结束程序
		button_end.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	//随机洗牌函数
	private void random(){
		Random rand = new Random();
		int temp1 = 0;
		int temp2 = 0;
		for(int i=0; i<3; i++){
			temp1 = rand.nextInt(3);
			temp2 = poker[i];
			poker[i] = poker[temp1];
			poker[temp1] = temp2;
		}
	}
	//进度条线程
	Runnable progressBarThread = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			i+=10;
			try{
				Thread.sleep(20);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			Message msg = myHandler.obtainMessage();
			msg.arg1 = i;
			myHandler.sendMessage(msg);
		}
	};
	//设置进度条不可见，如果直接关闭会因为调用了其他线程的View而报错，
	// 因此采用了runOnUiThread(progressInvisible);
	Runnable progressInvisible = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			progressBar.setVisibility(View.INVISIBLE);
		}
	};

	class MyHandler extends Handler{
		MyHandler(Looper looper){
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			progressBar.setProgress(msg.arg1);
			if(i >= 100){
				//注意 ：这里要把i归0，否则下次进度条直接显示满状态。
				i = 0;
				//如果不先执行一次，下次启动进度条会先显示满条，再清空，看起来很怪
				progressBar.setProgress(i);
				myHandler.removeCallbacks(progressBarThread);
				MainActivity.this.runOnUiThread(progressInvisible);
				return;
			}
			myHandler.post(progressBarThread);
		}
	}
}