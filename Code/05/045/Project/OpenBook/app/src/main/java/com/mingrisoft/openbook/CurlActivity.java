/*
   Copyright 2012 Harri Smatt

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.mingrisoft.openbook;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.WindowManager;


public class CurlActivity extends Activity {

	private MyView mMyView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);     //设置布局

		/**
		 * 初始化控件
		 * */
		int index = 0;
		if (getLastNonConfigurationInstance() != null) {
			index = (Integer) getLastNonConfigurationInstance();
		}
		mMyView = (MyView) findViewById(R.id.curl);
		mMyView.setPageProvider(new PageProvider());
		mMyView.setSizeChangedObserver(new SizeChangedObserver());
		mMyView.setCurrentIndex(index);
		mMyView.setBackgroundColor(0xFF202830);
	}

	/**
	 * onPause生命周期
	 * */
	@Override
	public void onPause() {
		super.onPause();
		mMyView.onPause();
	}

	/**
	 * onResume命周期
	 * */
	@Override
	public void onResume() {
		super.onResume();
		mMyView.onResume();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return mMyView.getCurrentIndex();
	}

	/**
	 * 位图的提供者.
	 */
	private class PageProvider implements MyView.PageProvider {

		//图片资源.
		private int[] mBitmapIds = { R.mipmap.book1, R.mipmap.book2,
				R.mipmap.book3, R.mipmap.book4 };

		@Override
		public int getPageCount() {
			return 5;
		}

		private Bitmap loadBitmap(int width, int height, int index) {
			width = mMyView.get()[0];
			height = mMyView.get()[1];
			Bitmap b = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			b.eraseColor(0xFFFFFFFF);
			Canvas c = new Canvas(b);
			Drawable d = getResources().getDrawable(mBitmapIds[index]);
			Rect r = new Rect();
			Paint p = new Paint();
			p.setColor(0xFFC0C0C0);
			c.drawRect(r, p);
			r.left = 0;
			r.right = width;
			r.top = 0;
			r.bottom = height;

			d.setBounds(r);
			d.draw(c);

			return b;
		}

		@Override
		public void updatePage(CurlPage page, int width, int height, int index) {
			switch (index) {
			// 正面是是一张图片，背景是是自己的镜面像
			case 0: {
				Bitmap front = loadBitmap(width, height, 0);
				page.setTexture(front, CurlPage.SIDE_BOTH);
				page.setColor(Color.argb(127, 255, 255, 255),
						CurlPage.SIDE_BACK);
				break;
			}
			//是同一个图像被分配到前面和后面。在这
			//场景只有一个纹理被使用和共享的双方
			case 1: {
				Bitmap front = loadBitmap(width, height, 3);
				page.setTexture(front, CurlPage.SIDE_BOTH);
				page.setColor(Color.argb(127, 255, 255, 255),
						CurlPage.SIDE_BACK);
				break;
			}
			//是同一个图像被分配到前面和后面。在这
			//场景只有一个纹理被使用和共享的双方
			case 2: {
				Bitmap front = loadBitmap(width, height, 1);
				page.setTexture(front, CurlPage.SIDE_BOTH);
				page.setColor(Color.argb(127, 255, 255, 255),
						CurlPage.SIDE_BACK);

				break;
			}
			//是同一个图像被分配到前面和后面。在这
			//场景只有一个纹理被使用和共享的双方
			case 3: {

				Bitmap front = loadBitmap(width, height, 2);
				page.setTexture(front, CurlPage.SIDE_BOTH);
				page.setColor(Color.argb(127, 255, 255, 255),
						CurlPage.SIDE_BACK);
				break;
			}
			//是同一个图像被分配到前面和后面。在这
			//场景只有一个纹理被使用和共享的双方。
			case 4:
				Bitmap front = loadBitmap(width, height, 0);
				page.setTexture(front, CurlPage.SIDE_BOTH);
				page.setColor(Color.argb(127, 255, 255, 255),
						CurlPage.SIDE_BACK);

				break;
			}
		}

	}

	/**
	 * CurlView 大小变化观察。
	 */
	private class SizeChangedObserver implements MyView.SizeChangedObserver {
		@Override
		public void onSizeChanged(int w, int h) {
				mMyView.setMargins(0f, 0f, 0f, 0f);
		}
	}

}