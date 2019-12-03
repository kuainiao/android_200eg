package com.mingrisoft.imagepj;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends MPermissionsActivity implements View.OnClickListener{
    private ImageView iV1,iV2,iV3,iV4;
    private EditText et;
    public LinearLayout ll;
    //保存图片路径
    private String path = "";
    private String path1= "";
    private String path2 = "";
    private String path3 = "";
    private String path4 = "";
    //图片bitmap
    private Bitmap bm1=null;
    private Bitmap bm2=null;
    private Bitmap bm3=null;
    private Bitmap bm4=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        iV1=(ImageView)findViewById(R.id.iV1);
        iV2=(ImageView)findViewById(R.id.iV2);
        iV3=(ImageView)findViewById(R.id.iV3);
        iV4=(ImageView)findViewById(R.id.iV4);
        iV1.setOnClickListener(this);
        iV2.setOnClickListener(this);
        iV3.setOnClickListener(this);
        iV4.setOnClickListener(this);
        ll=(LinearLayout)findViewById(R.id.ll);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iV1:
                requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 0x0001);
                break;
            case R.id.iV2:
                requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 0x0002);
                break;
            case R.id.iV3:
                requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 0x0003);
                break;
            case R.id.iV4:
                requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 0x0004);
                break;
        }
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
                new MainActivity.PopupWindows(MainActivity.this, ll,1);
                break;
            case 0x0002:
                new MainActivity.PopupWindows(MainActivity.this, ll,2);
                break;
            case 0x0003:
                new MainActivity.PopupWindows(MainActivity.this, ll,3);
                break;
            case 0x0004:
                new MainActivity.PopupWindows(MainActivity.this, ll,4);
                break;
        }

    }
    //分享按钮
    public void onFX(View view){
       if(bm1!=null&&bm2!=null&&bm3==null&&bm4==null){
           //拼接图片
           Bitmap bmhc = ImageUtil.add2Bitmap(bm1,bm2);
           //保存图片到文件
           FileUtils.saveBitmap(bmhc, "hc");
        }else if(bm1!=null&&bm2!=null&&bm3!=null&&bm4==null){
           Bitmap bmhc = ImageUtil.add2Bitmap(bm1,bm2);
           Bitmap bmhc1 = ImageUtil.addBitmap(bmhc,bm3);
           FileUtils.saveBitmap(bmhc1, "hc");
       }else if(bm1!=null&&bm2!=null&&bm3!=null&&bm4!=null){
           Bitmap bmhc = ImageUtil.add2Bitmap(bm1,bm2);
           Bitmap bmhc1 = ImageUtil.add2Bitmap(bm3,bm4);
           Bitmap bmhc2 = ImageUtil.addBitmap(bmhc,bmhc1);
           FileUtils.saveBitmap(bmhc2, "hc");
       }else if(bm1!=null&&bm2==null&&bm3!=null&&bm4==null){
           Bitmap bmhc = ImageUtil.addBitmap(bm1,bm3);
           FileUtils.saveBitmap(bmhc, "hc");
       }else if(bm1!=null&&bm2==null&&bm3!=null&&bm4!=null){
           Bitmap bmhc = ImageUtil.add2Bitmap(bm3,bm4);
           Bitmap bmhc1 = ImageUtil.addBitmap(bm1,bmhc);
           FileUtils.saveBitmap(bmhc1, "hc");
       }else if(bm1==null&&bm2!=null&&bm3!=null&&bm4!=null){
           Bitmap bmhc = ImageUtil.add2Bitmap(bm3,bm4);
           Bitmap bmhc1 = ImageUtil.addBitmap(bm2,bmhc);
           FileUtils.saveBitmap(bmhc1, "hc");
       }else if(bm1==null&&bm2!=null&&bm3!=null&&bm4==null){
           Bitmap bmhc = ImageUtil.addBitmap(bm2,bm3);
           FileUtils.saveBitmap(bmhc, "hc");
       }else if(bm1!=null&&bm2==null&&bm3==null&&bm4!=null){
           Bitmap bmhc = ImageUtil.addBitmap(bm1,bm4);
           FileUtils.saveBitmap(bmhc, "hc");
       }else if(bm1!=null&&bm2!=null&&bm3==null&&bm4!=null){
           Bitmap bmhc = ImageUtil.add2Bitmap(bm1,bm2);
           Bitmap bmhc1 = ImageUtil.addBitmap(bmhc,bm4);
           FileUtils.saveBitmap(bmhc1, "hc");
       }else if(bm1==null&&bm2==null&&bm3!=null&&bm4!=null){
           Bitmap bmhc = ImageUtil.add2Bitmap(bm3,bm4);
           FileUtils.saveBitmap(bmhc, "hc");
       }else{
           Toast.makeText(this, "请选择至少2张图片", Toast.LENGTH_LONG).show();
           return;
       }
        onHC();
    }
    public void onHC(){
        String imagePath = Environment.getExternalStorageDirectory() + File.separator + "hc.jpg";
        //由文件得到uri
        Uri imageUri = Uri.fromFile(new File(imagePath));
        Log.d("share", "uri:" + imageUri);  //输出：file:///storage/emulated/0/hc.jpg
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //传递参数
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }
    public class PopupWindows extends PopupWindow
    {
        public PopupWindows(Context mContext, View parent, final int pth)
        {
            View view = View.inflate(mContext, R.layout.item_popupwindows, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout)view.findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.FILL_PARENT);//设置宽
            setHeight(ViewGroup.LayoutParams.FILL_PARENT);//设置高
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();//跟新popupwindows
            Button bt1 = (Button)view.findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button)view.findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button)view.findViewById(R.id.item_popupwindows_cancel);
            bt1.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    photo(pth);
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                    startActivityForResult(intent, pth+10);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    dismiss();
                }
            });

        }
    }

    public void photo(int pth)
    {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory() ,
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        if (pth==1){
            path1 = file.getPath();
        }else if(pth==2){
            path2 = file.getPath();
        }else if(pth==3){
            path3 = file.getPath();
        }else if(pth==4){
            path4 = file.getPath();
        }
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(openCameraIntent, pth);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        ContentResolver resolver = getContentResolver();
        switch (requestCode) {
            //拍照获取信息
            case 1:
                try {
                    if (Bimp.revitionImageSize(path1) != null) {
                        bm1 = Bimp.revitionImageSize(path1);
                        if (getBitmapDegree(path1) != 0) {
                            bm1 = rotateBitmapByDegree(bm1, getBitmapDegree(path1));
                        }
                        iV1.setImageBitmap(bm1);
                        iV1.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    if (Bimp.revitionImageSize(path2) != null) {
                        bm2 = Bimp.revitionImageSize(path2);//获取bitmap
                        if (getBitmapDegree(path2) != 0) {
                            bm2 = rotateBitmapByDegree(bm2, getBitmapDegree(path2));
                        }
                        iV2.setImageBitmap(bm2);
                        iV2.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    if (Bimp.revitionImageSize(path3) != null) {
                        bm3 = Bimp.revitionImageSize(path3);//获取bitmap
                        if (getBitmapDegree(path3) != 0) {
                            bm3 = rotateBitmapByDegree(bm3, getBitmapDegree(path3));
                        }
                        iV3.setImageBitmap(bm3);
                        iV3.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    if (Bimp.revitionImageSize(path4) != null) {
                        bm4 = Bimp.revitionImageSize(path4);//获取bitmap
                        if (getBitmapDegree(path4) != 0) {
                            bm4 = rotateBitmapByDegree(bm4, getBitmapDegree(path4));
                        }
                        iV4.setImageBitmap(bm4);
                        iV4.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            //相册选择图片
            case 11:

                if (data != null) {//判断数据是否为空
                    Uri url1 = data.getData();        //获得图片的uri
                    try {
//                    bm1 = MediaStore.Images.Media.getBitmap(resolver, url1);
                        String[] proj = {MediaStore.Images.Media.DATA};
                        //好像是Android多媒体数据库的封装接口，具体的看Android文档
                        Cursor cursor = managedQuery(url1, proj, null, null, null);
                        //按我个人理解 这个是获得用户选择的图片的索引值
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        //将光标移至开头 ，这个很重要，不小心很容易引起越界
                        cursor.moveToFirst();
                        //最后根据索引值获取图片路径
                        String path = cursor.getString(column_index);
                        bm1 = Bimp.revitionImageSize(path);//获取bitmap
                        if (getBitmapDegree(path) != 0) {
                            bm1 = rotateBitmapByDegree(bm1, getBitmapDegree(path));
                        }
                        //设置图片
                        iV1.setImageBitmap(bm1);
                        //设置图片占满控件
                        iV1.setScaleType(ImageView.ScaleType.FIT_XY);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 12:
                if (data != null) {
                    Uri url2 = data.getData();        //获得图片的uri
                    try {
//                    bm2 = MediaStore.Images.Media.getBitmap(resolver, url2);
                        String[] proj = {MediaStore.Images.Media.DATA};
                        //好像是Android多媒体数据库的封装接口，具体的看Android文档
                        Cursor cursor = managedQuery(url2, proj, null, null, null);
                        //按我个人理解 这个是获得用户选择的图片的索引值
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        //将光标移至开头 ，这个很重要，不小心很容易引起越界
                        cursor.moveToFirst();
                        //最后根据索引值获取图片路径
                        String path = cursor.getString(column_index);
                        bm2 = Bimp.revitionImageSize(path);//获取bitmap
                        if (getBitmapDegree(path) != 0) {
                            bm2 = rotateBitmapByDegree(bm2, getBitmapDegree(path));
                        }
                        iV2.setImageBitmap(bm2);
                        iV2.setScaleType(ImageView.ScaleType.FIT_XY);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 13:
                if (data!= null) {
                    Uri url3 = data.getData();        //获得图片的uri
                    try {
//                    bm3 = MediaStore.Images.Media.getBitmap(resolver, url3);
                        String[] proj = {MediaStore.Images.Media.DATA};
                        //好像是Android多媒体数据库的封装接口，具体的看Android文档
                        Cursor cursor = managedQuery(url3, proj, null, null, null);
                        //按我个人理解 这个是获得用户选择的图片的索引值
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        //将光标移至开头 ，这个很重要，不小心很容易引起越界
                        cursor.moveToFirst();
                        //最后根据索引值获取图片路径
                        String path = cursor.getString(column_index);
                        bm3 = Bimp.revitionImageSize(path);//获取bitmap
                        if (getBitmapDegree(path) != 0) {
                            bm3 = rotateBitmapByDegree(bm3, getBitmapDegree(path));
                        }
                        iV3.setImageBitmap(bm3);
                        iV3.setScaleType(ImageView.ScaleType.FIT_XY);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 14:
                if (data!= null) {
                    Uri url4 = data.getData();        //获得图片的uri
                    try {
//                    bm4 = MediaStore.Images.Media.getBitmap(resolver, url4);
                        String[] proj = {MediaStore.Images.Media.DATA};
                        //好像是Android多媒体数据库的封装接口，具体的看Android文档
                        Cursor cursor = managedQuery(url4, proj, null, null, null);
                        //按我个人理解 这个是获得用户选择的图片的索引值
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        //将光标移至开头 ，这个很重要，不小心很容易引起越界
                        cursor.moveToFirst();
                        //最后根据索引值获取图片路径
                        String path = cursor.getString(column_index);
                        bm4 = Bimp.revitionImageSize(path);//获取bitmap
                        if (getBitmapDegree(path) != 0) {
                            bm4 = rotateBitmapByDegree(bm4, getBitmapDegree(path));
                        }
                        iV4.setImageBitmap(bm4);
                        iV4.setScaleType(ImageView.ScaleType.FIT_XY);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
        }
    }
//    在部分Android手机（如MT788、Note2）上，使用Camera拍照以后，得到的照片会被自动旋转（90°、180°、270°），
// 这个情况很不符合预期。仔细分析了一下，因为照片属性中是存储了旋转信息的，所以要解决这个问题，可以在onActivityResult方法中，
// 获取到照片数据后，读取它的旋转信息，如果不是0，说明这个照片已经被旋转过了，那么再使用android.graphics.Matrix将照片旋转回去即可。
    /**
     * 读取图片的旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return 图片的旋转角度
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm
     *            需要旋转的图片
     * @param degree
     *            旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }
}
