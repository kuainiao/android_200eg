package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.mingrisoft.level.MainActivity;
import com.mingrisoft.level.R;

/**
 * Created by Administrator on 2016/12/7.
 */

public class MyView extends View {
    // 定义水平仪圆盘图片
    public Bitmap compass,compass1;
    // 定义水平仪中的小球图标
    public Bitmap ball;
    // 定义水平仪中小球的X、Y座标
    public int ballX;
    public int ballY;
    //记录水平仪是否处于中心点水平状态,true为水平状态，false为倾斜状态
    public boolean accuracy=true;
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 加载水平状态仪圆盘图片
        compass = BitmapFactory.decodeResource(getResources()
                , R.drawable.img_disc);
        // 加载倾斜状态仪圆盘图片
        compass1=BitmapFactory.decodeResource(getResources()
                , R.drawable.img_disc1);
        //加载小球图片
        ball = BitmapFactory
                .decodeResource(getResources(), R.drawable.img_ball);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);  //获取窗口服务
        int width = wm.getDefaultDisplay().getWidth();       //获取窗口宽度
        int height = wm.getDefaultDisplay().getHeight();     //获取窗口高度
        int imgHeight = compass.getHeight();                //获取圆盘图片高度
        int cy = (height - imgHeight) / 2;                  //计算绘制圆盘y坐标
        if (accuracy==false){       //如果手机处于倾斜状态
            // 绘制水平仪圆盘图片
            canvas.drawBitmap(compass1, 0, cy, null);
            // 根据小球坐标绘制小球
            canvas.drawBitmap(ball, ballX, ballY, null);
        }else {                      //手机处于水平状态
            // 绘制水平仪圆盘图片
            canvas.drawBitmap(compass, 0, cy, null);
        }

    }
}
