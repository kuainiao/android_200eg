package com.mingrisoft.applicationlock;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Debug;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;


public class LockPatternView extends View {
	private static final String TAG = "-*-*-*-*-*-*";
	// 渲染视图时使用的方面
	private static final int ASPECT_SQUARE = 0;      //查看将是最小的
												     //宽度/高度
	private static final int ASPECT_LOCK_WIDTH = 1;  //固定宽度；高度
													 //是最小的（W，H）
	private static final int ASPECT_LOCK_HEIGHT = 2; //固定高度；宽度
													 //是最小的（W，H）

	private static final boolean PROFILE_DRAWING = false;
	private boolean mDrawingProfilingStarted = false;

	private Paint mPaint = new Paint();
	private Paint mPathPaint = new Paint();

	// TODO: make this common with PhoneWindow
	static final int STATUS_BAR_HEIGHT = 25;

	/**
	 *我们花在多少毫秒每圈锁模式如果
	 *动画模式设置。整个动画应该采取这个常数
	 **完成模式的长度。
	 */
	private static final int MILLIS_PER_CIRCLE_ANIMATING = 700;

	private OnPatternListener mOnPatternListener;
	private ArrayList<Cell> mPattern = new ArrayList<Cell>(9);

	/**
	 * 我们正在绘制的图案圆的查找表。
	 * 这将是完整的细胞，除非我们是动画模式，
	 * 在这种情况下，我们使用这个持有我们正在绘制的细胞
	 * 进步的动画。
	 */
	private boolean[][] mPatternDrawLookup = new boolean[3][3];

	/**
	 * 正在进行中的点：在交互过程中：用户的手指在哪里—
	 * 动画：动画中的线电流尖
	 */
	private float mInProgressX = -1;
	private float mInProgressY = -1;

	private long mAnimatingPeriodStart;

	private DisplayMode mPatternDisplayMode = DisplayMode.Correct;
	private boolean mInputEnabled = true;
	private boolean mInStealthMode = false;
	private boolean mEnableHapticFeedback = true;
	private boolean mPatternInProgress = false;

	private float mDiameterFactor = 0.10f; // TODO: move to attrs
	private final int mStrokeAlpha = 128;
	private float mHitFactor = 0.6f;

	private float mSquareWidth;
	private float mSquareHeight;

	private Bitmap mBitmapCircleDefault;
	private Bitmap mBitmapCircleGreen;
	private Bitmap mBitmapCircleRed;

	private final Path mCurrentPath = new Path();
	private final Rect mInvalidate = new Rect();

	private int mBitmapWidth;
	private int mBitmapHeight;

	private int mAspect;
	private final Matrix mCircleMatrix = new Matrix();

	/**
	 * 表示解锁模式视图的3 x 3矩阵中的单元格.。
	 */
	public static class Cell {
		int row;
		int column;
		// 保持#对象限制为9
		static Cell[][] sCells = new Cell[3][3];
		static {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					sCells[i][j] = new Cell(i, j);
				}
			}
		}

		/**
		 * @param row
		 *           单元格的行。
		 * @param column
		 *            单元格的列。
		 */
		private Cell(int row, int column) {
			checkRange(row, column);
			this.row = row;
			this.column = column;
		}

		public int getRow() {
			return row;
		}

		public int getColumn() {
			return column;
		}

		/**
		 * @param row
		 *           单元格的行.
		 * @param column
		 *            单元格的列。
		 */
		public static synchronized Cell of(int row, int column) {
			checkRange(row, column);
			return sCells[row][column];
		}

		private static void checkRange(int row, int column) {
			if (row < 0 || row > 2) {
				throw new IllegalArgumentException("row must be in range 0-2");
			}
			if (column < 0 || column > 2) {
				throw new IllegalArgumentException(
						"column must be in range 0-2");
			}
		}

		public String toString() {
			return "(row=" + row + ",clmn=" + column + ")";
		}
	}

	/**
	 * 如何显示当前模式。
	 */
	public enum DisplayMode {

		/**
		 *绘制的图案是正确的（即以友好的颜色绘制）
		 */
		Correct,

		/**
		 * 动画模式（演示和帮助）。
		 */
		Animate,

		/**
		 * 该模式是错误的（即画一个不祥的颜色）
		 */
		Wrong
	}

	/**
	 * 用于检测用户输入的模式的回叫接口。
	 */
	public interface OnPatternListener {

		/**
		 * 一种新的模式已经开始
		 */
		void onPatternStart();

		/**
		 * 该模式被清除。
		 */
		void onPatternCleared();

		/**
		 * 用户扩展当前由一个单元格绘制的模式.
		 * 
		 * @param pattern
		 *            新加入点的模式。
		 */
		void onPatternCellAdded(List<Cell> pattern);

		/**
		 * 从用户中检测到一个模式。
		 * 
		 * @param pattern
		 *            模式。
		 */
		void onPatternDetected(List<Cell> pattern);
	}

	public LockPatternView(Context context) {
		this(context, null);
	}

	public LockPatternView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.LockPatternView);

		final String aspect = a.getString(R.styleable.LockPatternView_aspect);

		if ("square".equals(aspect)) {
			mAspect = ASPECT_SQUARE;
		} else if ("lock_width".equals(aspect)) {
			mAspect = ASPECT_LOCK_WIDTH;
		} else if ("lock_height".equals(aspect)) {
			mAspect = ASPECT_LOCK_HEIGHT;
		} else {
			mAspect = ASPECT_SQUARE;
		}

		setClickable(true);

		mPathPaint.setAntiAlias(true);
		mPathPaint.setDither(true);
		mPathPaint.setColor(Color.YELLOW); // TODO this should be from the style
		mPathPaint.setAlpha(mStrokeAlpha);
		mPathPaint.setStyle(Paint.Style.STROKE);
		mPathPaint.setStrokeJoin(Paint.Join.ROUND);
		mPathPaint.setStrokeCap(Paint.Cap.ROUND);

		mBitmapCircleDefault = getBitmapFor(R.drawable.gesture_pattern_item_bg);
		mBitmapCircleGreen = getBitmapFor(R.drawable.gesture_pattern_selected);
		mBitmapCircleRed = getBitmapFor(R.drawable.gesture_pattern_selected_wrong);
		// 位图有本组中最大的位图的大小
		final Bitmap bitmaps[] = { mBitmapCircleDefault, mBitmapCircleGreen,
				mBitmapCircleRed };
		for (Bitmap bitmap : bitmaps) {
			mBitmapWidth = Math.max(mBitmapWidth, bitmap.getWidth());
			mBitmapHeight = Math.max(mBitmapHeight, bitmap.getHeight());
		}
		a.recycle();

	}

	private Bitmap getBitmapFor(int resId) {
		return BitmapFactory.decodeResource(getContext().getResources(), resId);
	}

	/**
	 * @return 是否该视图是在隐形模式。
	 */
	public boolean isInStealthMode() {
		return mInStealthMode;
	}

	/**
	 * @return 视图是否具有触觉反馈功能。
	 */
	public boolean isTactileFeedbackEnabled() {
		return mEnableHapticFeedback;
	}

	/**
	 * 设置视图是否处于隐身模式。如果是真的，就不会有
	 * 用户进入模式时的可见反馈。
	 * 
	 * @param inStealthMode
	 *            是否在隐身模式。
	 */
	public void setInStealthMode(boolean inStealthMode) {
		mInStealthMode = inStealthMode;
	}

	/**
	 * 设置视图是否会使用触觉反馈。如果是真的，会有
	 * 用户进入模式时的触觉反馈。
	 * 
	 * @param tactileFeedbackEnabled
	 *           是否启用了触觉反馈

	 */
	public void setTactileFeedbackEnabled(boolean tactileFeedbackEnabled) {
		mEnableHapticFeedback = tactileFeedbackEnabled;
	}

	/**
	 * 设置模式检测的回调。
	 * 
	 * @param onPatternListener
	 *            回调
	 */
	public void setOnPatternListener(OnPatternListener onPatternListener) {
		mOnPatternListener = onPatternListener;
	}

	/**
	 * 设置模式明显（而不是等待用户输入
	 * 模式）。
	 * 
	 * @param displayMode
	 *           如何显示模式。
	 * @param pattern
	 *           模式。
	 */
	public void setPattern(DisplayMode displayMode, List<Cell> pattern) {
		mPattern.clear();
		mPattern.addAll(pattern);
		clearPatternDrawLookup();
		for (Cell cell : pattern) {
			mPatternDrawLookup[cell.getRow()][cell.getColumn()] = true;
		}

		setDisplayMode(displayMode);
	}

	/**
	 * 设置当前模式的显示模式。这可能是有用的，因为
	 * 实例，在检测模式后告诉此视图是否更改
	 * 在进步的结果是正确的或错误的。
	 * 
	 * @param displayMode
	 *           显示模式。
	 */
	public void setDisplayMode(DisplayMode displayMode) {
		mPatternDisplayMode = displayMode;
		if (displayMode == DisplayMode.Animate) {
			if (mPattern.size() == 0) {
				throw new IllegalStateException(
						"you must have a pattern to "
								+ "animate if you want to set the display mode to animate");
			}
			mAnimatingPeriodStart = SystemClock.elapsedRealtime();
			final Cell first = mPattern.get(0);
			mInProgressX = getCenterXForColumn(first.getColumn());
			mInProgressY = getCenterYForRow(first.getRow());
			clearPatternDrawLookup();
		}
		invalidate();
	}

	private void notifyCellAdded() {
		if (mOnPatternListener != null) {
			mOnPatternListener.onPatternCellAdded(mPattern);
		}
		sendAccessEvent(R.string.lockscreen_access_pattern_cell_added);
	}

	private void notifyPatternStarted() {
		if (mOnPatternListener != null) {
			mOnPatternListener.onPatternStart();
		}
		sendAccessEvent(R.string.lockscreen_access_pattern_start);
	}

	private void notifyPatternDetected() {
		if (mOnPatternListener != null) {
			mOnPatternListener.onPatternDetected(mPattern);
		}
		sendAccessEvent(R.string.lockscreen_access_pattern_detected);
	}

	private void notifyPatternCleared() {
		if (mOnPatternListener != null) {
			mOnPatternListener.onPatternCleared();
		}
		sendAccessEvent(R.string.lockscreen_access_pattern_cleared);
	}

	/**
	 *清除模式。
	 */
	public void clearPattern() {
		resetPattern();
	}

	/**
	 * 重置所有模式状态。
	 */
	private void resetPattern() {
		mPattern.clear();
		clearPatternDrawLookup();
		mPatternDisplayMode = DisplayMode.Correct;
		invalidate();
	}

	/**
	 * 清除模式查找表。
	 */
	private void clearPatternDrawLookup() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				mPatternDrawLookup[i][j] = false;
			}
		}
	}

	/**
	 * 禁用输入（例如，当显示一个消息将超时
	 * 所以用户没有进入混乱状态的视图。
	 */
	public void disableInput() {
		mInputEnabled = false;
	}

	/**
	 *使能输入。
	 */
	public void enableInput() {
		mInputEnabled = true;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		final int width = w - getPaddingLeft() - getPaddingRight();
		mSquareWidth = width / 3.0f;

		final int height = h - getPaddingTop() - getPaddingBottom();
		mSquareHeight = height / 3.0f;
	}

	private int resolveMeasured(int measureSpec, int desired) {
		int result = 0;
		int specSize = MeasureSpec.getSize(measureSpec);
		switch (MeasureSpec.getMode(measureSpec)) {
		case MeasureSpec.UNSPECIFIED:
			result = desired;
			break;
		case MeasureSpec.AT_MOST:
			result = Math.max(specSize, desired);
			break;
		case MeasureSpec.EXACTLY:
		default:
			result = specSize;
		}
		return result;
	}

	@Override
	protected int getSuggestedMinimumWidth() {
		// 视图应该足够大，包含3个并排的目标位图
		return 3 * mBitmapWidth;
	}

	@Override
	protected int getSuggestedMinimumHeight() {
		//  视图应该足够大，包含3个并排的目标位图
		return 3 * mBitmapWidth;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int minimumWidth = getSuggestedMinimumWidth();
		final int minimumHeight = getSuggestedMinimumHeight();
		int viewWidth = resolveMeasured(widthMeasureSpec, minimumWidth);
		int viewHeight = resolveMeasured(heightMeasureSpec, minimumHeight);

		switch (mAspect) {
		case ASPECT_SQUARE:
			viewWidth = viewHeight = Math.min(viewWidth, viewHeight);
			break;
		case ASPECT_LOCK_WIDTH:
			viewHeight = Math.min(viewWidth, viewHeight);
			break;
		case ASPECT_LOCK_HEIGHT:
			viewWidth = Math.min(viewWidth, viewHeight);
			break;
		}
		Log.v(TAG, "LockPatternView dimensions: " + viewWidth + "x"
				+ viewHeight);
		setMeasuredDimension(viewWidth, viewHeight);
	}

	/**
	 * 确定该点x、y是否会向当前添加一个新点.
	 * 模式（除了找到单元格，也使启发式选择
	 * 如填补空白根据当前模式）。
	 * 
	 * @param x
	 *           X坐标。
	 * @param y
	 *            Y坐标。
	 */
	private Cell detectAndAddHit(float x, float y) {
		final Cell cell = checkForNewHit(x, y);
		if (cell != null) {

			// 检查现有模式的差距
			Cell fillInGapCell = null;
			final ArrayList<Cell> pattern = mPattern;
			if (!pattern.isEmpty()) {
				final Cell lastCell = pattern.get(pattern.size() - 1);
				int dRow = cell.row - lastCell.row;
				int dColumn = cell.column - lastCell.column;

				int fillInRow = lastCell.row;
				int fillInColumn = lastCell.column;

				if (Math.abs(dRow) == 2 && Math.abs(dColumn) != 1) {
					fillInRow = lastCell.row + ((dRow > 0) ? 1 : -1);
				}

				if (Math.abs(dColumn) == 2 && Math.abs(dRow) != 1) {
					fillInColumn = lastCell.column + ((dColumn > 0) ? 1 : -1);
				}

				fillInGapCell = Cell.of(fillInRow, fillInColumn);
			}

			if (fillInGapCell != null
					&& !mPatternDrawLookup[fillInGapCell.row][fillInGapCell.column]) {
				addCellToPattern(fillInGapCell);
			}
			addCellToPattern(cell);
			if (mEnableHapticFeedback) {
				performHapticFeedback(
						HapticFeedbackConstants.VIRTUAL_KEY,
						HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
								| HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
			}
			return cell;
		}
		return null;
	}

	private void addCellToPattern(Cell newCell) {
		mPatternDrawLookup[newCell.getRow()][newCell.getColumn()] = true;
		mPattern.add(newCell);
		notifyCellAdded();
	}

	//帮助查找单元格的哪个点映射到
	private Cell checkForNewHit(float x, float y) {

		final int rowHit = getRowHit(y);
		if (rowHit < 0) {
			return null;
		}
		final int columnHit = getColumnHit(x);
		if (columnHit < 0) {
			return null;
		}

		if (mPatternDrawLookup[rowHit][columnHit]) {
			return null;
		}
		return Cell.of(rowHit, columnHit);
	}

	/**
	 * 帮助查找Y落入的行的辅助方法。
	 * 
	 * @param y
	 *          Y坐标
	 * @return   Y  落入的行，或- 1如果不落。
	 */
	private int getRowHit(float y) {

		final float squareHeight = mSquareHeight;
		float hitSize = squareHeight * mHitFactor;

		float offset = getPaddingTop() + (squareHeight - hitSize) / 2f;
		for (int i = 0; i < 3; i++) {

			final float hitTop = offset + squareHeight * i;
			if (y >= hitTop && y <= hitTop + hitSize) {
				return i;
			}
		}
		return -1;
	}

	/**
	 *辅助方法来找到柱回落到X。
	 * 
	 * @param x
	 *           X坐标。
	 * @return 如果x不在列中，则x下降，或- 1。
	 */
	private int getColumnHit(float x) {
		final float squareWidth = mSquareWidth;
		float hitSize = squareWidth * mHitFactor;

		float offset = getPaddingLeft() + (squareWidth - hitSize) / 2f;
		for (int i = 0; i < 3; i++) {

			final float hitLeft = offset + squareWidth * i;
			if (x >= hitLeft && x <= hitLeft + hitSize) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!mInputEnabled || !isEnabled()) {
			return false;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			handleActionDown(event);  //手指落下时
			return true;
		case MotionEvent.ACTION_UP:
			handleActionUp(event);    //手指抬起时
			return true;
		case MotionEvent.ACTION_MOVE:
			handleActionMove(event);  //手指移动时
			return true;
		case MotionEvent.ACTION_CANCEL:
			resetPattern();
			mPatternInProgress = false;
			notifyPatternCleared();
			if (PROFILE_DRAWING) {
				if (mDrawingProfilingStarted) {
					Debug.stopMethodTracing();
					mDrawingProfilingStarted = false;
				}
			}
			return true;
		}
		return false;
	}

	private void handleActionMove(MotionEvent event) {
		// 处理所有最近的运动事件，所以我们不跳过任何细胞，即使当
		// 该装置
		final int historySize = event.getHistorySize();
		for (int i = 0; i < historySize + 1; i++) {
			final float x = i < historySize ? event.getHistoricalX(i) : event
					.getX();
			final float y = i < historySize ? event.getHistoricalY(i) : event
					.getY();
			final int patternSizePreHitDetect = mPattern.size();
			Cell hitCell = detectAndAddHit(x, y);
			final int patternSize = mPattern.size();
			if (hitCell != null && patternSize == 1) {
				mPatternInProgress = true;
				notifyPatternStarted();
			}
			// 注意当前x和y的橡胶带的进展模式
			final float dx = Math.abs(x - mInProgressX);
			final float dy = Math.abs(y - mInProgressY);
			if (dx + dy > mSquareWidth * 0.01f) {
				float oldX = mInProgressX;
				float oldY = mInProgressY;

				mInProgressX = x;
				mInProgressY = y;

				if (mPatternInProgress && patternSize > 0) {
					final ArrayList<Cell> pattern = mPattern;
					final float radius = mSquareWidth * mDiameterFactor * 0.5f;
					final Cell lastCell = pattern.get(patternSize - 1);
					float startX = getCenterXForColumn(lastCell.column);
					float startY = getCenterYForRow(lastCell.row);
					float left;
					float top;
					float right;
					float bottom;

					final Rect invalidateRect = mInvalidate;

					if (startX < x) {
						left = startX;
						right = x;
					} else {
						left = x;
						right = startX;
					}

					if (startY < y) {
						top = startY;
						bottom = y;
					} else {
						top = y;
						bottom = startY;
					}

					// 无效的模式的最后一个单元格和之间
					// 当前位置
					invalidateRect.set((int) (left - radius),
							(int) (top - radius), (int) (right + radius),
							(int) (bottom + radius));

					if (startX < oldX) {
						left = startX;
						right = oldX;
					} else {
						left = oldX;
						right = startX;
					}

					if (startY < oldY) {
						top = startY;
						bottom = oldY;
					} else {
						top = oldY;
						bottom = startY;
					}

					// 无效的模式的最后一个单元格和之间
					// 以前的位置
					invalidateRect.union((int) (left - radius),
							(int) (top - radius), (int) (right + radius),
							(int) (bottom + radius));

					// 失效模式的新细胞和间
					// 模式的前一个细胞
					if (hitCell != null) {
						startX = getCenterXForColumn(hitCell.column);
						startY = getCenterYForRow(hitCell.row);

						if (patternSize >= 2) {
							// 重新使用旧电池hitcell）
							hitCell = pattern.get(patternSize - 1
									- (patternSize - patternSizePreHitDetect));
							oldX = getCenterXForColumn(hitCell.column);
							oldY = getCenterYForRow(hitCell.row);

							if (startX < oldX) {
								left = startX;
								right = oldX;
							} else {
								left = oldX;
								right = startX;
							}

							if (startY < oldY) {
								top = startY;
								bottom = oldY;
							} else {
								top = oldY;
								bottom = startY;
							}
						} else {
							left = right = startX;
							top = bottom = startY;
						}

						final float widthOffset = mSquareWidth / 2f;
						final float heightOffset = mSquareHeight / 2f;

						invalidateRect.set((int) (left - widthOffset),
								(int) (top - heightOffset),
								(int) (right + widthOffset),
								(int) (bottom + heightOffset));
					}

					invalidate(invalidateRect);
				} else {
					invalidate();
				}
			}
		}
	}

	private void sendAccessEvent(int resId) {
		setContentDescription(getContext().getString(resId));
		sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);
		setContentDescription(null);
	}

	private void handleActionUp(MotionEvent event) {
		// 报告模式检测
		if (!mPattern.isEmpty()) {
			mPatternInProgress = false;
			notifyPatternDetected();
			invalidate();
		}
		if (PROFILE_DRAWING) {
			if (mDrawingProfilingStarted) {
				Debug.stopMethodTracing();
				mDrawingProfilingStarted = false;
			}
		}
	}

	private void handleActionDown(MotionEvent event) {
		resetPattern();
		final float x = event.getX();
		final float y = event.getY();
		final Cell hitCell = detectAndAddHit(x, y);
		if (hitCell != null) {
			mPatternInProgress = true;
			mPatternDisplayMode = DisplayMode.Correct;
			notifyPatternStarted();
		} else {
			mPatternInProgress = false;
			notifyPatternCleared();
		}
		if (hitCell != null) {
			final float startX = getCenterXForColumn(hitCell.column);
			final float startY = getCenterYForRow(hitCell.row);

			final float widthOffset = mSquareWidth / 2f;
			final float heightOffset = mSquareHeight / 2f;

			invalidate((int) (startX - widthOffset),
					(int) (startY - heightOffset),
					(int) (startX + widthOffset), (int) (startY + heightOffset));
		}
		mInProgressX = x;
		mInProgressY = y;
		if (PROFILE_DRAWING) {
			if (!mDrawingProfilingStarted) {
				Debug.startMethodTracing("LockPatternDrawing");
				mDrawingProfilingStarted = true;
			}
		}
	}

	private float getCenterXForColumn(int column) {
		return getPaddingLeft() + column * mSquareWidth + mSquareWidth / 2f;
	}

	private float getCenterYForRow(int row) {
		return getPaddingTop() + row * mSquareHeight + mSquareHeight / 2f;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		final ArrayList<Cell> pattern = mPattern;
		final int count = pattern.size();
		final boolean[][] drawLookup = mPatternDrawLookup;

		if (mPatternDisplayMode == DisplayMode.Animate) {

			//画出哪一圈画

			//1所以我们暂停在完整的模式
			final int oneCycle = (count + 1) * MILLIS_PER_CIRCLE_ANIMATING;
			final int spotInCycle = (int) (SystemClock.elapsedRealtime() - mAnimatingPeriodStart)
					% oneCycle;
			final int numCircles = spotInCycle / MILLIS_PER_CIRCLE_ANIMATING;

			clearPatternDrawLookup();
			for (int i = 0; i < numCircles; i++) {
				final Cell cell = pattern.get(i);
				drawLookup[cell.getRow()][cell.getColumn()] = true;
			}

			//找出进步部分影线

			final boolean needToUpdateInProgressPoint = numCircles > 0
					&& numCircles < count;

			if (needToUpdateInProgressPoint) {
				final float percentageOfNextCircle = ((float) (spotInCycle % MILLIS_PER_CIRCLE_ANIMATING))
						/ MILLIS_PER_CIRCLE_ANIMATING;

				final Cell currentCell = pattern.get(numCircles - 1);
				final float centerX = getCenterXForColumn(currentCell.column);
				final float centerY = getCenterYForRow(currentCell.row);

				final Cell nextCell = pattern.get(numCircles);
				final float dx = percentageOfNextCircle
						* (getCenterXForColumn(nextCell.column) - centerX);
				final float dy = percentageOfNextCircle
						* (getCenterYForRow(nextCell.row) - centerY);
				mInProgressX = centerX + dx;
				mInProgressY = centerY + dy;
			}
			// TODO: Infinite loop here...
			invalidate();
		}

		final float squareWidth = mSquareWidth;
		final float squareHeight = mSquareHeight;

		float radius = (squareWidth * mDiameterFactor * 0.5f);
		mPathPaint.setStrokeWidth(radius);

		final Path currentPath = mCurrentPath;
		currentPath.rewind();

		// TODO: the path should be created and cached every time we hit-detect
		// 一个细胞
		// 只有最后一段的路径应计算在这里
		// 绘制模式的路径（除非用户正在进行，并且
		// 我们在隐形模式下
		final boolean drawPath = (!mInStealthMode || mPatternDisplayMode == DisplayMode.Wrong);

		// 绘制与路径关联的箭头（除非用户处于
		// 的进步，和
		// 我们在隐形模式下
		boolean oldFlag = (mPaint.getFlags() & Paint.FILTER_BITMAP_FLAG) != 0;
		mPaint.setFilterBitmap(true);   // 自我们以来，以更高的质量
										// 渲染与变换
		//画线
		if (drawPath) {
			boolean anyCircles = false;
			for (int i = 0; i < count; i++) {
				Cell cell = pattern.get(i);

				// 只绘制存储在
				// 查找表（这是唯一不同的情况下
				// 动画的）。
				if (!drawLookup[cell.row][cell.column]) {
					break;
				}
				anyCircles = true;

				float centerX = getCenterXForColumn(cell.column);
				float centerY = getCenterYForRow(cell.row);
				if (i == 0) {
					currentPath.moveTo(centerX, centerY);
				} else {
					currentPath.lineTo(centerX, centerY);
				}
			}

			// 添加最后的进度条
			if ((mPatternInProgress || mPatternDisplayMode == DisplayMode.Animate)
					&& anyCircles) {
				currentPath.lineTo(mInProgressX, mInProgressY);
			}
			// 常在不同显示模式的线的颜色
			if (mPatternDisplayMode == DisplayMode.Wrong)
				mPathPaint.setColor(Color.RED);
			else
				mPathPaint.setColor(Color.YELLOW);
			canvas.drawPath(currentPath, mPathPaint);
		}

		//画圆
		final int paddingTop = getPaddingTop();
		final int paddingLeft = getPaddingLeft();

		for (int i = 0; i < 3; i++) {
			float topY = paddingTop + i * squareHeight;
			// float centerY = mPaddingTop + i * mSquareHeight + (mSquareHeight
			// / 2);
			for (int j = 0; j < 3; j++) {
				float leftX = paddingLeft + j * squareWidth;
				drawCircle(canvas, (int) leftX, (int) topY, drawLookup[i][j]);
			}
		}

		mPaint.setFilterBitmap(oldFlag); //恢复默认的flag
	}

	/**
	 * @param canvas
	 * @param leftX
	 * @param topY
	 * @param partOfPattern
	 *            这个圈子是否是模式的一部分.
	 */
	private void drawCircle(Canvas canvas, int leftX, int topY,
			boolean partOfPattern) {
		Bitmap outerCircle;
		Bitmap innerCircle = null;

		if (!partOfPattern
				|| (mInStealthMode && mPatternDisplayMode != DisplayMode.Wrong)) {
			//未选中的圈
			outerCircle = mBitmapCircleDefault;
			innerCircle = null;
		} else if (mPatternInProgress) {
			// user is in middle of drawing a pattern
			outerCircle = mBitmapCircleDefault;
			innerCircle = mBitmapCircleGreen;
		} else if (mPatternDisplayMode == DisplayMode.Wrong) {
			// the pattern is wrong
			outerCircle = mBitmapCircleDefault;
			innerCircle = mBitmapCircleRed;
		} else if (mPatternDisplayMode == DisplayMode.Correct
				|| mPatternDisplayMode == DisplayMode.Animate) {
			// the pattern is correct
			outerCircle = mBitmapCircleDefault;
			innerCircle = mBitmapCircleGreen;
		} else {
			throw new IllegalStateException("unknown display mode "
					+ mPatternDisplayMode);
		}

		final int width = mBitmapWidth;
		final int height = mBitmapHeight;

		final float squareWidth = mSquareWidth;
		final float squareHeight = mSquareHeight;

		int offsetX = (int) ((squareWidth - width) / 2f);
		int offsetY = (int) ((squareHeight - height) / 2f);

		// 如果视图太小无法容纳它们，则允许圆收缩.。
		float sx = Math.min(mSquareWidth / mBitmapWidth, 1.0f);
		float sy = Math.min(mSquareHeight / mBitmapHeight, 1.0f);

		mCircleMatrix.setTranslate(leftX + offsetX, topY + offsetY);
		mCircleMatrix.preTranslate(mBitmapWidth / 2, mBitmapHeight / 2);
		mCircleMatrix.preScale(sx, sy);
		mCircleMatrix.preTranslate(-mBitmapWidth / 2, -mBitmapHeight / 2);

		canvas.drawBitmap(outerCircle, mCircleMatrix, mPaint);
		if (innerCircle != null)
			canvas.drawBitmap(innerCircle, mCircleMatrix, mPaint);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		return new SavedState(superState,
				LockPatternUtils.patternToString(mPattern),
				mPatternDisplayMode.ordinal(), mInputEnabled, mInStealthMode,
				mEnableHapticFeedback);
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		final SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		setPattern(DisplayMode.Correct,
				LockPatternUtils.stringToPattern(ss.getSerializedPattern()));
		mPatternDisplayMode = DisplayMode.values()[ss.getDisplayMode()];
		mInputEnabled = ss.isInputEnabled();
		mInStealthMode = ss.isInStealthMode();
		mEnableHapticFeedback = ss.isTactileFeedbackEnabled();
	}

	/**
	 * 保存和恢复一个锁模式查看parecelable。
	 */
	private static class SavedState extends BaseSavedState {

		private final String mSerializedPattern;
		private final int mDisplayMode;
		private final boolean mInputEnabled;
		private final boolean mInStealthMode;
		private final boolean mTactileFeedbackEnabled;

		/**
		 * Constructor called from {@link LockPatternView#onSaveInstanceState()}
		 */
		private SavedState(Parcelable superState, String serializedPattern,
				int displayMode, boolean inputEnabled, boolean inStealthMode,
				boolean tactileFeedbackEnabled) {
			super(superState);
			mSerializedPattern = serializedPattern;
			mDisplayMode = displayMode;
			mInputEnabled = inputEnabled;
			mInStealthMode = inStealthMode;
			mTactileFeedbackEnabled = tactileFeedbackEnabled;
		}

		/**
		 * Constructor called from {@link #CREATOR}
		 */
		private SavedState(Parcel in) {
			super(in);
			mSerializedPattern = in.readString();
			mDisplayMode = in.readInt();
			mInputEnabled = (Boolean) in.readValue(null);
			mInStealthMode = (Boolean) in.readValue(null);
			mTactileFeedbackEnabled = (Boolean) in.readValue(null);
		}

		public String getSerializedPattern() {
			return mSerializedPattern;
		}

		public int getDisplayMode() {
			return mDisplayMode;
		}

		public boolean isInputEnabled() {
			return mInputEnabled;
		}

		public boolean isInStealthMode() {
			return mInStealthMode;
		}

		public boolean isTactileFeedbackEnabled() {
			return mTactileFeedbackEnabled;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeString(mSerializedPattern);
			dest.writeInt(mDisplayMode);
			dest.writeValue(mInputEnabled);
			dest.writeValue(mInStealthMode);
			dest.writeValue(mTactileFeedbackEnabled);
		}

		@SuppressWarnings("unused")
		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}
