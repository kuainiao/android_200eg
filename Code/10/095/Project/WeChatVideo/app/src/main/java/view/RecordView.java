package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;


import com.mingrisoft.wechatvideo.MainActivity;
import com.mingrisoft.wechatvideo.R;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
   录制视频的自定义控件
 */
public class RecordView extends SurfaceView implements MediaRecorder.OnErrorListener {

    private SurfaceHolder surfaceHolder;        //定义surfaceHolder用于显示图像
    private MediaRecorder mediaRecorder;        //定义mediaRecorder用于视频音频的录制
    private Camera camera;                       //定义摄像头
    private Timer timer;                         //定义计时器
    private OnRecordStausChangeListener onRecordStausChangeListener;   //录制状态变化回调接口

    private int xPx;                                                   //视频分辨率宽度
    private int yPx;                                                   //视频分辨率高度
    private int outFormat;                                             //0是mp4，1是3gp
    private int recordMaxTime;                                         //一次拍摄最长时间
    private int videoEncodingBitRate;                                  //视频编码的比特率
    private int videoFrameRate;                                        //录制的视频帧率

    private String outputDirPath = Environment.getExternalStorageDirectory()
            + File.separator + "My/video/";                             //输出目录
    private String suffix;                                              //视频文件后缀

    private boolean isOpenCamera;                                  //是否一开始就打开摄像头
    private int timeCount;                                          //时间计数
    private File vecordFile = null;                                 //视频输出文件

    public RecordView(Context context) {
        this(context, null);
    }

    public RecordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecordView, defStyleAttr, 0);
        xPx = typedArray.getInteger(R.styleable.RecordView_x_px, 320);// 默认320
        yPx = typedArray.getInteger(R.styleable.RecordView_y_px, 240);// 默认240
        outFormat = typedArray.getInteger(R.styleable.RecordView_out_format,0);//默认MP4
        recordMaxTime = typedArray.getInteger(R.styleable.RecordView_record_max_time, 10);//默认最长10秒
        videoEncodingBitRate = typedArray.getInteger(R.styleable.RecordView_video_encoding_bit_rate, 1 * 1024 * 1024);
        videoFrameRate = typedArray.getInteger(R.styleable.RecordView_video_frame_rate, 10);
        isOpenCamera = typedArray.getBoolean(R.styleable.RecordView_is_open_camera, true);
        typedArray.recycle();

        switch (outFormat) {
            case 0:
                suffix = ".mp4";
                break;
            case 1:
                suffix = ".3gp";
                break;
        }

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(new CustomCallBack());
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    private class CustomCallBack implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (!isOpenCamera)
                return;
            openCamera();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (!isOpenCamera)
                return;
            freeCameraResource();
        }
    }

    /**
     * 初始化摄像头
     */
    public void openCamera() {
        try {
            if (camera != null) {
                freeCameraResource();
            }
            try {
                camera = Camera.open();
            } catch (Exception e) {
                e.printStackTrace();
                freeCameraResource();
            }
            if (camera == null)
                return;

            setCameraParams();
            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            camera.unlock();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置摄像头为竖屏
     */
    private void setCameraParams() {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size preSize = getCloselyPreSize(true, getWidth(), getHeight(), parameters.getSupportedPreviewSizes());
            parameters.setPreviewSize(preSize.width, preSize.height);
            camera.setParameters(parameters);
        }
    }

    /**
     * 通过对比得到与宽高比最接近的预览尺寸（如果有相同尺寸，优先选择）
     *
     * @param isPortrait    是否竖屏
     * @param surfaceWidth  需要被进行对比的原宽
     * @param surfaceHeight 需要被进行对比的原高
     * @param preSizeList   需要对比的预览尺寸列表
     * @return 得到与原宽高比例最接近的尺寸
     */
    public static Camera.Size getCloselyPreSize(boolean isPortrait, int surfaceWidth, int surfaceHeight, List<Camera.Size> preSizeList) {
        int reqTmpWidth;
        int reqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        if (isPortrait) {
            reqTmpWidth = surfaceHeight;
            reqTmpHeight = surfaceWidth;
        } else {
            reqTmpWidth = surfaceWidth;
            reqTmpHeight = surfaceHeight;
        }
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for (Camera.Size size : preSizeList) {
            if ((size.width == reqTmpWidth) && (size.height == reqTmpHeight)) {
                return size;
            }
        }

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) reqTmpWidth) / reqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : preSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }

        return retSize;
    }

    /**
     * 释放摄像头资源
     */
    private void freeCameraResource() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.lock();
            camera.release();
            camera = null;
        }
    }
    //创建录制视频的目录
    private void createRecordDir() {
        //录制的视频保存文件夹
        File sampleDir = new File(outputDirPath);//录制视频的保存地址
        if (!sampleDir.exists()) {  //如果没有这个目录
            sampleDir.mkdirs();     //创建目录
        }
        try {
            //创建视频输出的目录与名称
            vecordFile = File.createTempFile("recording", suffix, sampleDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRecord() throws IOException {
        mediaRecorder = new MediaRecorder();

        try {
            mediaRecorder.reset();
            if (camera != null)
                mediaRecorder.setCamera(camera);
            mediaRecorder.setOnErrorListener(this);
            mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//视频源
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);//音频源
            mediaRecorder.setOrientationHint(90);//输出旋转90度，保持坚屏录制

            switch (outFormat) {
                case 0:
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// 视频输出格式
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 音频格式
                    mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);// 视频录制格式
                    break;
                case 1:
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                    break;
            }

            //设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
            mediaRecorder.setVideoSize(xPx, yPx);
            // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
            mediaRecorder.setVideoFrameRate(videoFrameRate);
            mediaRecorder.setVideoEncodingBitRate(videoEncodingBitRate);
            mediaRecorder.setOutputFile(vecordFile.getAbsolutePath());

            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始录制视频
     */
    public void record(final OnRecordStausChangeListener onRecordStausChangeListener) {
        this.onRecordStausChangeListener = onRecordStausChangeListener;
        createRecordDir();          //创建视频保存的路径
        try {
            if (onRecordStausChangeListener != null)
                //开启录制回调接口
                onRecordStausChangeListener.onRecordStart();
            if (!isOpenCamera)//如果未打开摄像头，则打开
                openCamera();   //打开摄像头
            initRecord();       //进行录制初始化
            timeCount = 0;     //时间计数器重新赋值
            timer = new Timer();    //创建计时器
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    timeCount++;    //加1秒
                    if (onRecordStausChangeListener != null)
                        //设置拍摄的最大时间
                        onRecordStausChangeListener.onRecording(timeCount, recordMaxTime);
                    if (timeCount == recordMaxTime) {//达到指定时间，停止拍摄
                        stop();     //停止录制
                    }
                }
            }, 0, 1000);            //1秒执行一次
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录制并释放相机资源
     */
    public void stop() {
        stopRecord();
        releaseRecord();
        freeCameraResource();
    }

    /**
     * 停止录制
     */
    public void stopRecord() {
        if (timer != null)
            timer.cancel();
        if (mediaRecorder != null) {
            //设置后不会崩
            mediaRecorder.setOnErrorListener(null);
            mediaRecorder.setPreviewDisplay(null);
            try {
                mediaRecorder.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 释放资源
     */
    private void releaseRecord() {
        if (mediaRecorder != null) {
            mediaRecorder.setOnErrorListener(null);
            try {
                mediaRecorder.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mediaRecorder = null;
    }

    /**
     * 录制状态变化回调接口
     */
    public interface OnRecordStausChangeListener {
        public void onRecrodFinish();

        public void onRecording(int timeCount, int recordMaxTime);

        public void onRecordStart();
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        try {
            if (mr != null)
                mr.reset();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 已经录制的秒数
     */
    public int getTimeCount() {
        return timeCount;
    }

    /**
     * @return 录制的视频文件
     */
    public File getVecordFile() {
        return vecordFile;
    }

    public boolean isOpenCamera() {
        return isOpenCamera;
    }

    public void setOpenCamera(boolean openCamera) {
        isOpenCamera = openCamera;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        suffix = suffix;
    }

    public String getOutputDirPath() {
        return outputDirPath;
    }

    public void setOutputDirPath(String outputDirPath) {
        outputDirPath = outputDirPath;
    }

    public int getVideoFrameRate() {
        return videoFrameRate;
    }

    public void setVideoFrameRate(int videoFrameRate) {
        videoFrameRate = videoFrameRate;
    }

    public int getVideoEncodingBitRate() {
        return videoEncodingBitRate;
    }

    public void setVideoEncodingBitRate(int videoEncodingBitRate) {
        videoEncodingBitRate = videoEncodingBitRate;
    }

    public int getRecordMaxTime() {
        return recordMaxTime;
    }

    public void setRecordMaxTime(int recordMaxTime) {
        recordMaxTime = recordMaxTime;
    }

    public int getOutFormat() {
        return outFormat;
    }

    public void setOutFormat(int outFormat) {
        outFormat = outFormat;
    }

    public int getYpx() {
        return yPx;
    }

    public void setYpx(int ypx) {
        yPx = ypx;
    }

    public int getXpx() {
        return xPx;
    }

    public void setXpx(int xpx) {
        xPx = xpx;
    }
}





