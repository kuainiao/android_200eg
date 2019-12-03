package com.mingrisoft.randomdraw;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    //接收该消息 进行图片更换
    public static final int MESSAGE_1 = 1;
    public static final int MESSAGE_2 = 2;
    public static final int MESSAGE_3 = 3;
    public static final int MESSAGE_4 = 4;
    public static final int MESSAGE_5 = 5;
    public static final int MESSAGE_6 = 6;
    public static final int MESSAGE_7 = 7;
    public static final int MESSAGE_8 = 8;


    //停止消息
    public static final int MESSAGE_STOP = 10;
    //一层随机数
    int number = 8;
    //图片控件
    ImageView imageView;
    int width;
    int height;
    //图片0~8，0就是没有抽奖时的默认图片
    Bitmap wheel_0;
    Bitmap wheel_1;
    Bitmap wheel_2;
    Bitmap wheel_3;
    Bitmap wheel_4;
    Bitmap wheel_5;
    Bitmap wheel_6;
    Bitmap wheel_7;
    Bitmap wheel_8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        //获取显示图片的控件
        imageView = (ImageView)findViewById(R.id.iv);

        width=getWindowManager().getDefaultDisplay().getWidth();       //手机屏幕的宽度
        height=getWindowManager().getDefaultDisplay().getHeight()+90;      //手机屏幕的高度
        //获取图片资源
        wheel_0 = BitmapFactory.decodeResource(getResources(), R.drawable.img_0);
        wheel_1 = BitmapFactory.decodeResource(getResources(), R.drawable.img_1);
        wheel_2 = BitmapFactory.decodeResource(getResources(), R.drawable.img_2);
        wheel_3 = BitmapFactory.decodeResource(getResources(), R.drawable.img_3);
        wheel_4 = BitmapFactory.decodeResource(getResources(), R.drawable.img_4);
        wheel_5 = BitmapFactory.decodeResource(getResources(), R.drawable.img_5);
        wheel_6 = BitmapFactory.decodeResource(getResources(), R.drawable.img_6);
        wheel_7 = BitmapFactory.decodeResource(getResources(), R.drawable.img_7);
        wheel_8 = BitmapFactory.decodeResource(getResources(), R.drawable.img_8);

    }

    //根据接收到的消息 进行图片的更换，并进行停止时中奖的提示
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //根据接收到的消息，进行相对应图片的显示
                case MESSAGE_1:
                    imageView.setImageBitmap(wheel_1);
                    break;
                case MESSAGE_2:
                    imageView.setImageBitmap(wheel_2);
                    break;
                case MESSAGE_3:
                    imageView.setImageBitmap(wheel_3);
                    break;
                case MESSAGE_4:
                    imageView.setImageBitmap(wheel_4);
                    break;
                case MESSAGE_5:
                    imageView.setImageBitmap(wheel_5);
                    break;
                case MESSAGE_6:
                    imageView.setImageBitmap(wheel_6);
                    break;
                case MESSAGE_7:
                    imageView.setImageBitmap(wheel_7);
                    break;
                case MESSAGE_8:
                    imageView.setImageBitmap(wheel_8);
                    break;

                case MESSAGE_STOP:
                    Bundle bundle = new Bundle();
                    bundle = msg.getData();
                    int i = bundle.getInt("msg");
                    switch (i%8) {
                        case 0:
                            Toast.makeText(MainActivity.this, "钢笔一支", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(MainActivity.this, "C#完全手册一本", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(MainActivity.this, "运动鞋一双", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(MainActivity.this, "JavaWeb入门一本", Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            Toast.makeText(MainActivity.this, "纪念版玩偶一个", Toast.LENGTH_SHORT).show();
                            break;
                        case 5:
                            Toast.makeText(MainActivity.this, "C#完全手册一本", Toast.LENGTH_SHORT).show();
                            break;
                        case 6:
                            Toast.makeText(MainActivity.this, "热水袋一个", Toast.LENGTH_SHORT).show();
                            break;
                        case 7:
                            Toast.makeText(MainActivity.this, "ASP.NET技术大全一本", Toast.LENGTH_SHORT).show();
                            break;

                        default:
                            break;
                    }

                    break;

            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        if(event.getAction()==MotionEvent.ACTION_DOWN){
            //按下中间的位置时，60位中心点上下左右的距离
            if(event.getX()>width/2-60 && event.getX()<width/2+60
                    && event.getY()>height/2-60 && event.getY()<height/2+60){
                Random random = new Random();       //创建一层随机数
                number = random.nextInt((8))+1;     //产生1~8随机数
                new Thread(new Runnable() {         //创建线程
                    public void run() {
                        Random random = new Random();   //创建二层随机数
                        int num = random.nextInt((8));  //产生0~7随机数
                        while(number<60+num){          //循环次数
                            Message message = new Message();    //更换图片的消息
                            switch (number%8) {        //计算圈数与余数为中奖位置
                                case 0:
                                    //根据一层随机数与8的余数进行消息的发送，起到更换背景图片的作用
                                    message.what = MESSAGE_8;
                                    break;
                                case 1:
                                    message.what = MESSAGE_1;
                                    break;
                                case 2:
                                    message.what = MESSAGE_2;
                                    break;
                                case 3:
                                    message.what = MESSAGE_3;
                                    break;
                                case 4:
                                    message.what = MESSAGE_4;
                                    break;
                                case 5:
                                    message.what = MESSAGE_5;
                                    break;
                                case 6:
                                    message.what = MESSAGE_6;
                                    break;
                                case 7:
                                    message.what = MESSAGE_7;
                                    break;
                                case 8:
                                    message.what = MESSAGE_8;
                                    break;
                                default:
                                    break;
                            }
                            number++;       //循环+1产生轮盘转动效果
                            mHandler.sendMessage(message);  //发送消息
                            //减速设置，
                            if(number<50){                  //随机数小于50
                                try {
                                    Thread.sleep(100);      //线程休眠时间为0.1秒
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }else if(number>50 && number<60){       //随之说在大于50小于60
                                try {
                                    Thread.sleep(300);      //线程休眠时间为0.3秒
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }else{
                                try {
                                    Thread.sleep(400);        //线程休眠时间为0.4面
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        //进行停止消息的发送
                        Message message = new Message();      //创建停止消息
                        message.what = MESSAGE_STOP;        //设置要发送的停止消息
                        Bundle bundle = new Bundle();        //创建bundle
                        if ((number-1)%8==0){               //跳出循环后number++，所以这里需要减1
                            bundle.putInt("msg",0);       //保存停止的位置
                        }else {
                            bundle.putInt("msg",(number-1)%8);       //保存停止的位置
                        }
                        message.setData(bundle);             //将位置信息保存在消息中
                        mHandler.sendMessage(message);      //发送消息
                    }
                        }).start();
            }
        }

        return super.onTouchEvent(event);

    }





}
