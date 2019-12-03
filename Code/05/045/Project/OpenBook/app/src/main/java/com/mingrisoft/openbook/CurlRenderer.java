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

import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;


public class CurlRenderer implements GLSurfaceView.Renderer {

	// 请求离开页面矩形常数。
	public static final int PAGE_LEFT = 1;
	// 请求页矩形常数。
	public static final int PAGE_RIGHT = 2;
	// 改变视图模式的常数。
	public static final int SHOW_ONE_PAGE = 1;
	public static final int SHOW_TWO_PAGES = 2;
	// 设置为真正的检查快速透视投影看起来。
	private static final boolean USE_PERSPECTIVE_PROJECTION = false;
	// 背景填充颜色。
	private int mBackgroundColor;
	// 用于静态和动态渲染的卷曲网格。
	private Vector<CurlMesh> mCurlMeshes;
	private RectF mMargins = new RectF();
	private Observer mObserver;
	//页面的矩形。
	private RectF mPageRectLeft;
	private RectF mPageRectRight;
	// 视图模式。
	private int mViewMode = SHOW_ONE_PAGE;
	// 屏幕尺寸。
	private int mViewportWidth, mViewportHeight;
	// 矩形渲染区域。
	private RectF mViewRect = new RectF();

	/**
	 * 基本的构造函数。
	 */
	public CurlRenderer(Observer observer) {
		mObserver = observer;
		mCurlMeshes = new Vector<CurlMesh>();
		mPageRectLeft = new RectF();
		mPageRectRight = new RectF();
	}

	/**
	 * 增加了curlmesh这个渲染器。
	 */
	public synchronized void addCurlMesh(CurlMesh mesh) {
		removeCurlMesh(mesh);
		mCurlMeshes.add(mesh);
	}

	/**
	 * 返回Rect保留左或右页。价值页面应该是
	 * page_left或page_right。
	 */
	public RectF getPageRect(int page) {
		if (page == PAGE_LEFT) {
			return mPageRectLeft;
		} else if (page == PAGE_RIGHT) {
			return mPageRectRight;
		}
		return null;
	}

	@Override
	public synchronized void onDrawFrame(GL10 gl) {

		mObserver.onDrawFrame();

		gl.glClearColor(Color.red(mBackgroundColor) / 255f,
				Color.green(mBackgroundColor) / 255f,
				Color.blue(mBackgroundColor) / 255f,
				Color.alpha(mBackgroundColor) / 255f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glLoadIdentity();

		if (USE_PERSPECTIVE_PROJECTION) {
			gl.glTranslatef(0, 0, -6f);
		}

		for (int i = 0; i < mCurlMeshes.size(); ++i) {
			mCurlMeshes.get(i).onDrawFrame(gl);
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		mViewportWidth = width;
		mViewportHeight = height;

		float ratio = (float) width / height;
		mViewRect.top = 1.0f;
		mViewRect.bottom = -1.0f;
		mViewRect.left = -ratio;
		mViewRect.right = ratio;
		updatePageRects();

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		if (USE_PERSPECTIVE_PROJECTION) {
			GLU.gluPerspective(gl, 20f, (float) width / height, .1f, 100f);
		} else {
			GLU.gluOrtho2D(gl, mViewRect.left, mViewRect.right,
					mViewRect.bottom, mViewRect.top);
		}

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0f, 0f, 0f, 1f);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		gl.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);
		gl.glHint(GL10.GL_POLYGON_SMOOTH_HINT, GL10.GL_NICEST);
		gl.glEnable(GL10.GL_LINE_SMOOTH);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL10.GL_CULL_FACE);

		mObserver.onSurfaceCreated();
	}

	/**
	 * 删除curlmesh从这个渲染器。
	 */
	public synchronized void removeCurlMesh(CurlMesh mesh) {
		while (mCurlMeshes.remove(mesh))
			;
	}

	/**
	 * 更改背景/清除颜色。
	 */
	public void setBackgroundColor(int color) {
		mBackgroundColor = color;
	}

	/**
	 * 设置边缘或填充。注：是成比例的。
	 */
	public synchronized void setMargins(float left, float top, float right,
			float bottom) {
		mMargins.left = 0;
		mMargins.top = 0;
		mMargins.right = 0;
		mMargins.bottom = 0;
		updatePageRects();
	}

	/**
	 * 将可见页计数设置为一个或两个。应该是show_one_page或
	 * show_two_pages。
	 */
	public synchronized void setViewMode(int viewmode) {
		if (viewmode == SHOW_ONE_PAGE) {
			mViewMode = viewmode;
			updatePageRects();
		} else if (viewmode == SHOW_TWO_PAGES) {
			mViewMode = viewmode;
			updatePageRects();
		}
	}

	/**
	 *将屏幕坐标转换为视图坐标。
	 */
	public void translate(PointF pt) {
		pt.x = mViewRect.left + (mViewRect.width() * pt.x / mViewportWidth);
		pt.y = mViewRect.top - (-mViewRect.height() * pt.y / mViewportHeight);
	}

	/**
	 *重新计算页面的矩形。
	 */
	private void updatePageRects() {
		if (mViewRect.width() == 0 || mViewRect.height() == 0) {
			return;
		} else if (mViewMode == SHOW_ONE_PAGE) {
			mPageRectRight.set(mViewRect);
			mPageRectRight.left += mViewRect.width() * mMargins.left;
			mPageRectRight.right -= mViewRect.width() * mMargins.right;
			mPageRectRight.top += mViewRect.height() * mMargins.top;
			mPageRectRight.bottom -= mViewRect.height() * mMargins.bottom;

			mPageRectLeft.set(mPageRectRight);
			mPageRectLeft.offset(-mPageRectRight.width(), 0);

			int bitmapW = (int) ((mPageRectRight.width() * mViewportWidth) / mViewRect
					.width());
			int bitmapH = (int) ((mPageRectRight.height() * mViewportHeight) / mViewRect
					.height());
			mObserver.onPageSizeChanged(bitmapW+60, bitmapH+60);
		} else if (mViewMode == SHOW_TWO_PAGES) {
			mPageRectRight.set(mViewRect);
			mPageRectRight.left += mViewRect.width() * mMargins.left;
			mPageRectRight.right -= mViewRect.width() * mMargins.right;
			mPageRectRight.top += mViewRect.height() * mMargins.top;
			mPageRectRight.bottom -= mViewRect.height() * mMargins.bottom;

			mPageRectLeft.set(mPageRectRight);
			mPageRectLeft.right = (mPageRectLeft.right + mPageRectLeft.left) / 2;
			mPageRectRight.left = mPageRectLeft.right;

			int bitmapW = (int) ((mPageRectRight.width() * mViewportWidth) / mViewRect
					.width());
			int bitmapH = (int) ((mPageRectRight.height() * mViewportHeight) / mViewRect
					.height());
			mObserver.onPageSizeChanged(bitmapW, bitmapH);
		}
	}

	/**
	 * 等待渲染引擎/状态更新。
	 */
	public interface Observer {
		/**
		 * 所谓的ondrawframe称在渲染之前开始。这是
		 * 拟用于动画的目的。
		 */
		void onDrawFrame();

		/**
		 * 调用一次页面大小改变。宽度和高度告诉页面大小
		 * 在像素，使有可能更新纹理相应。
		 */
		void onPageSizeChanged(int width, int height);

		/**
		 * 所谓的onsurfacecreated使纹理重新初始化等
		 * 当这种情况发生时需要做什么。
		 */
		 void onSurfaceCreated();
	}
}
