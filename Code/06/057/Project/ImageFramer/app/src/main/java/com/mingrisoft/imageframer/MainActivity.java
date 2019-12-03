package com.mingrisoft.imageframer;

import java.io.FileNotFoundException;
import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends MPermissionsActivity {

	//控件
	private Button openImageBn;            //打开图片
	private Button roundImageBn;           //圆角图片
	private ImageView imageShow;         //显示图片
	//自定义变量
	private Bitmap bmp;                          //原始图片
	private final int IMAGE_OPEN = 0;   //打开图片

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//打开图片
		openImageBn = (Button)findViewById(R.id.button1);
		imageShow = (ImageView) findViewById(R.id.imageView1);

		openImageBn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//动态判断权限
				requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0x0001);
			}
		});
		//圆角合成图片按钮
		roundImageBn = (Button)findViewById(R.id.button5);
		roundImageBn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageShow.setImageBitmap(getRoundedCornerBitmap(bmp));
			}
		});
	}
	/**
	 * 权限成功回调函数
	 *
	 * @param requestCode
	 */
	public void permissionSuccess(int requestCode) {
		super.permissionSuccess(requestCode);
		switch (requestCode) {
			case 0x0001:
				//启动相册
				Intent intent = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, IMAGE_OPEN);
				break;
		}
	}
	//接收回调信息
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK && requestCode==IMAGE_OPEN) {
			//图片路径
			Uri imageFileUri = data.getData();
			//获得系统信息
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			int width = dm.widthPixels;    //手机屏幕水平分辨率
			int height = dm.heightPixels;  //手机屏幕垂直分辨率
			try {
				//载入图片尺寸大小没载入图片本身 true
				BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
				bmpFactoryOptions.inJustDecodeBounds = true;
				//获得Bitmap对象
				bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFileUri), null, bmpFactoryOptions);
				int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
				int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);
				//inSampleSize表示图片占原图比例 1表示原图
				if(heightRatio>1&&widthRatio>1) {
					if(heightRatio>widthRatio) {
						bmpFactoryOptions.inSampleSize = heightRatio;
					}
					else {
						bmpFactoryOptions.inSampleSize = widthRatio;
					}
				}
				//图像真正解码 false
				bmpFactoryOptions.inJustDecodeBounds = false;
				//获取Bitmap对象
				bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFileUri), null, bmpFactoryOptions);
				//选择图片
				imageShow.setImageBitmap(bmp);
			}
			catch(FileNotFoundException e) {
				e.printStackTrace();
			}
		}  //end if
	}

	//生成圆角图片
	private Bitmap getRoundedCornerBitmap(Bitmap bitmap)
	{
		Bitmap roundBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(roundBitmap);
		int color = 0xff424242;
		Paint paint = new Paint();
		//设置圆形半径
		int radius;
		if(bitmap.getWidth()>bitmap.getHeight()) {
			radius = bitmap.getHeight()/2;
		}
		else {
			radius = bitmap.getWidth()/2;
		}
		//绘制圆形
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle( bitmap.getWidth()/ 2, bitmap.getHeight() / 2, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, 0, 0, paint);
		return roundBitmap;
	}
}
