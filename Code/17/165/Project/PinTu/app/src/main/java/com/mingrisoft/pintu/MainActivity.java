package com.mingrisoft.pintu;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
   //初始化控件
	private ImageButton ib_00;
	private ImageButton ib_01;
	private ImageButton ib_02;
	private ImageButton ib_10;
	private ImageButton ib_11;
	private ImageButton ib_12;
	private ImageButton ib_20;
	private ImageButton ib_21;
	private ImageButton ib_22;
	private Button btn_reset;
	private boolean timeSwitch=true;
	private int imageX = 3;
	private int imageY = 3;
	private int imgCount = imageX * imageY;
	private int length = imgCount;
	private int blankSwap = length - 1;
	private int blankImgId = R.id.ib_02_02;

	// 声明一个图片数组的下标,随机排列这个数组
	private int[] imageIndex = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	//图片集合
	private int[] imageId = { R.drawable.img_xiaoxiong_00x00,
			R.drawable.img_xiaoxiong_00x01, R.drawable.img_xiaoxiong_00x02,
			R.drawable.img_xiaoxiong_01x00, R.drawable.img_xiaoxiong_01x01,
			R.drawable.img_xiaoxiong_01x02, R.drawable.img_xiaoxiong_02x00,
			R.drawable.img_xiaoxiong_02x01, R.drawable.img_xiaoxiong_02x02, };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//初始化控件
		initUI();
		//随机打乱顺序
		Random();
	}

	// 打乱顺序
	private void Random() {
		int ran1, ran2;
		for (int i = 0; i < 20; i++) {
			ran1 = new Random().nextInt(length - 1);
			do {
				ran2 = new Random().nextInt(length - 1);
				if (ran1 != ran2) {
					break;
				}
			} while (true);
			Swap(ran1, ran2);
		}
		// 随机排序
		ib_00.setImageDrawable(getResources().getDrawable(
				imageId[imageIndex[0]]));
		ib_01.setImageDrawable(getResources().getDrawable(
				imageId[imageIndex[1]]));
		ib_02.setImageDrawable(getResources().getDrawable(
				imageId[imageIndex[2]]));
		ib_10.setImageDrawable(getResources().getDrawable(
				imageId[imageIndex[3]]));
		ib_11.setImageDrawable(getResources().getDrawable(
				imageId[imageIndex[4]]));
		ib_12.setImageDrawable(getResources().getDrawable(
				imageId[imageIndex[5]]));
		ib_20.setImageDrawable(getResources().getDrawable(
				imageId[imageIndex[6]]));
		ib_21.setImageDrawable(getResources().getDrawable(
				imageId[imageIndex[7]]));
		ib_22.setImageDrawable(getResources().getDrawable(
				imageId[imageIndex[8]]));

		// ib_00.getDrawable().equals(R.id.ib_02_02);
	}

	private void Swap(int ran1, int ran2) {
		int temp = imageIndex[ran1];
		imageIndex[ran1] = imageIndex[ran2];
		imageIndex[ran2] = temp;
	}

	private void initUI() {
		btn_reset = (Button) findViewById(R.id.btn_reset);
		ib_00 = (ImageButton) findViewById(R.id.ib_00_00);
		ib_01 = (ImageButton) findViewById(R.id.ib_00_01);
		ib_02 = (ImageButton) findViewById(R.id.ib_00_02);
		ib_10 = (ImageButton) findViewById(R.id.ib_01_00);
		ib_11 = (ImageButton) findViewById(R.id.ib_01_01);
		ib_12 = (ImageButton) findViewById(R.id.ib_01_02);
		ib_20 = (ImageButton) findViewById(R.id.ib_02_00);
		ib_21 = (ImageButton) findViewById(R.id.ib_02_01);
		ib_22 = (ImageButton) findViewById(R.id.ib_02_02);
		//绑定点击事件
		ib_00.setOnClickListener(this);
		ib_01.setOnClickListener(this);
		ib_02.setOnClickListener(this);
		ib_10.setOnClickListener(this);
		ib_11.setOnClickListener(this);
		ib_12.setOnClickListener(this);
		ib_20.setOnClickListener(this);
		ib_21.setOnClickListener(this);
		ib_22.setOnClickListener(this);
		btn_reset.setOnClickListener(this);
	}
    //点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_00_00:
			//移动
			move(R.id.ib_00_00, 0);
			break;
		case R.id.ib_00_01:
			move(R.id.ib_00_01, 1);
			break;
		case R.id.ib_00_02:
			move(R.id.ib_00_02, 2);
			break;
		case R.id.ib_01_00:
			move(R.id.ib_01_00, 3);
			break;
		case R.id.ib_01_01:
			move(R.id.ib_01_01, 4);
			break;
		case R.id.ib_01_02:
			move(R.id.ib_01_02, 5);
			break;
		case R.id.ib_02_00:
			move(R.id.ib_02_00, 6);
			break;
		case R.id.ib_02_01:
			move(R.id.ib_02_01, 7);
			break;
		case R.id.ib_02_02:
			move(R.id.ib_02_02, 8);
			break;
		case R.id.btn_reset:
			Intent intent = new Intent(MainActivity.this,MainActivity.class);
			startActivity(intent);
			MainActivity.this.finish();
			break;
		}
	}

	/**
	 * 点击的图片与空白区域的图片交换位置
	 * 
	 * @param imageBtnId
	 * @param site
	 */
	public void move(int imageBtnId, int site) {
		// 相邻图片坐标
		int sizeX = site / imageX;
		int sizeY = site % imageY;

		// 空白图片坐标
		int blackX = blankSwap / imageX;
		int blackY = blankSwap % imageY;

		int x = Math.abs(sizeX - blackX);
		int y = Math.abs(sizeY - blackY);

		// 满足条件
		if ((x == 0 && y == 1) || (x == 1 && y == 0)) {
			// 点击图片的id
			ImageButton clickButton = (ImageButton) findViewById(imageBtnId);
			// 当前点击的图片设置为不可见状态
			clickButton.setVisibility(View.INVISIBLE);
			// 空白图片的id
			ImageButton blankButton = (ImageButton) findViewById(blankImgId);
			// 用点击图片覆盖空白图片
			blankButton.setImageDrawable(getResources().getDrawable(
					imageId[imageIndex[site]]));
			// 将之前空白图片设置为可见
			blankButton.setVisibility(View.VISIBLE);
			// 交换标记
			Swap(site, blankSwap);
			// 让上面能够重新运算空白的位置
			blankSwap = site;
			// 交换两者的ID
			blankImgId = imageBtnId;
			GameOver();
		}
	}
   //判断游戏是否结束
	private void GameOver() {
		boolean loop=true;
		for (int i = 0; i < 9; i++) {
			if(imageIndex[i]!=i){
				loop=false;
			}
		}
		if(loop){
			//停止记时线程
			timeSwitch=false;
			//设置不可点击
			ib_00.setClickable(false);
			ib_01.setClickable(false);
			ib_02.setClickable(false);
			ib_10.setClickable(false);
			ib_11.setClickable(false);
			ib_12.setClickable(false);
			ib_20.setClickable(false);
			ib_21.setClickable(false);
			ib_22.setClickable(false);
			ib_22.setImageDrawable(getResources().getDrawable(imageId[8]));
			ib_22.setVisibility(View.VISIBLE);
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setMessage("恭喜，拼图成功了！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create();
			builder.show();
		}
			
	}
}
