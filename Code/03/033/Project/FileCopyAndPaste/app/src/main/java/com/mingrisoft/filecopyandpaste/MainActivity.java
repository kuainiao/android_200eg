package com.mingrisoft.filecopyandpaste;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {
    //保存显示文件列表的名称
    private List<String> lFileName = null;
    //保存显示的文件列表的相对应的路径
    private List<String> lFilePaths = null;
    // SD卡根目录
    private String lSDCard = Environment.getExternalStorageDirectory().toString();
    //获取底部菜单布局
    LinearLayout exit_layout, paste_layout;
    private final int SDK_PERMISSION = 1;                //申请权限
    private int ACTIVITY_VIEW_ATTACHMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //如果内存卡读写权限没有开启
        if (ContextCompat.checkSelfPermission(this,
                "android.permission.WRITE_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED) {
            getPersimmions();                                   //获取SD卡读写权限
        } else {
            //调用获取相关控件方法
            initView();
            //显示SD卡目录文件
            initFileListInfo(lSDCard);
            //为列表项绑定长按监听器
            getListView().setOnItemLongClickListener(this);
        }
    }


    /**
     * 添加SD卡读写动态权限
     */
    private void getPersimmions() {
        /***
         * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION);
            }
        }
    }

    /**
     * 动态权限的回调方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //调用获取相关控件方法
        initView();
        //显示SD卡目录文件
        initFileListInfo(lSDCard);
        //为列表项绑定长按监听器
        getListView().setOnItemLongClickListener(this);
    }

    /**
     * 获取相关控件
     */
    private void initView() {
        paste_layout = (LinearLayout) findViewById(R.id.laout_paste);
        exit_layout = (LinearLayout) findViewById(R.id.laout_exit);
        exit_layout.setOnClickListener(this);
        paste_layout.setOnClickListener(this);
    }

    /**
     * 底部菜单的单击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.laout_paste:
                onPaste();          //执行粘贴按钮处理方法
                break;
            case R.id.laout_exit:
                MainActivity.this.finish();
                break;
        }
    }


    //当前路径信息
    public static String lCurrentFilePath = "";

    /**
     * 便利文件夹内的文件/文件夹
     */
    private void initFileListInfo(String filePath) {
        isAddBackUp = false;
        lCurrentFilePath = filePath;
        lFileName = new ArrayList<String>();
        lFilePaths = new ArrayList<String>();
        File mFile = new File(filePath);
        //遍历出该文件夹路径下的所有文件/文件夹
        File[] mFiles = mFile.listFiles();
        initAddBackUp(filePath, lSDCard);

        //将所有文件信息添加到集合中
        for (File mCurrentFile : mFiles) {
            lFileName.add(mCurrentFile.getName());
            lFilePaths.add(mCurrentFile.getPath());
        }

        //适配显示的文件名称与路径
        setListAdapter(new FileAdapter(MainActivity.this, lFileName, lFilePaths));
    }

    private boolean isAddBackUp = false;

    /**
     * “返回根目录”和“返回上一级”
     */
    private void initAddBackUp(String filePath, String phone_sdcard) {

        if (!filePath.equals(phone_sdcard)) {
            /*列表项的第一项设置为返回根目录*/
            lFileName.add("BacktoRoot");
            lFilePaths.add(phone_sdcard);
            /*列表项的第二项设置为返回上一级*/
            lFileName.add("BacktoUp");
            //回到当前目录的父目录即回到上级
            lFilePaths.add(new File(filePath).getParent());
            //将添加返回按键标识位置为true
            isAddBackUp = true;
        }

    }


    /**
     * 根据类型筛选可以打开的应用程序
     */
    private void openFile(File file) {
        //获取文件类型匹配
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        //获取当前文件的名称
        String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        //根据文件名称匹配MIME
        String type = mime.getMimeTypeFromExtension(ext);
        try {
            Intent intent = new Intent();
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            //判断系统版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //设置Intent标志
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(this,
                        "com.mingrisoft.filecopyandpaste", file);
                //设置数据与类型
                intent.setDataAndType(contentUri, type);
            } else {
                intent.setDataAndType(Uri.fromFile(file), type);
            }
            //启动跳转
            startActivityForResult(intent, ACTIVITY_VIEW_ATTACHMENT);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(this, "没有可以打开当前文件的应用！", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 列表项单击时的事件监听
     */
    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        final File mFile = new File(lFilePaths.get(position));
        //如果文件可读就打开文件
        if (mFile.canRead()) {
            if (mFile.isDirectory()) {
                //如果是文件夹，则直接进入该文件夹，查看文件目录
                initFileListInfo(lFilePaths.get(position));
            } else {
                //如果是文件，则用相应的打开方式打开
                openFile(mFile);
            }
        } else {
            //不可读提示
            Toast.makeText(MainActivity.this, "对不起，您的访问权限不足!", Toast.LENGTH_SHORT).show();
        }
    }

    private String mOldFilePath = "";
    private String mNewFilePath = "";
    private String mCopyFileName;
    private boolean isCopy = false;

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //如果返回上一级的两列存在
        if (isAddBackUp == true) {
            //屏蔽这两列
            if (position != 0 && position != 1) {
                itemLongClickListener(new File(lFilePaths.get(position)));
            }
        }
        //判断手机SD卡根目录
        if (lCurrentFilePath.equals(lSDCard)) {
            itemLongClickListener(new File(lFilePaths.get(position)));
        }
        return false;
    }

    private void itemLongClickListener(final File file) {
        if (file.canRead()) {                //如果文件可读才可以进行复制
            copyDialog(file);             //调用确认复制对话框
        } else {
            Toast.makeText(this, "无权限复制该文件！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 确认复制对话框
     */
    private void copyDialog(final File file) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("提示!")
                .setMessage("是否要复制该文件？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //复制标志位，表明已复制文件
                        isCopy = true;
                        //取得复制文件的名字
                        mCopyFileName = file.getName();
                        //记录复制文件的路径
                        mOldFilePath = lCurrentFilePath + java.io.File.separator + mCopyFileName;
                        Toast.makeText(MainActivity.this, "已复制!", Toast.LENGTH_SHORT).show();
                        //重新遍历该文件的父目录
                        initFileListInfo(file.getParent());
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    /**
     * 粘贴按钮处理
     */
    private void onPaste() {
        if (!mOldFilePath.equals(mNewFilePath) && isCopy == true) {//在不同路径下复制才起效
            //得到新路径
            mNewFilePath = lCurrentFilePath + java.io.File.separator + mCopyFileName;
            copyFile(mOldFilePath, mNewFilePath);       //调用复制方法
            initFileListInfo(lCurrentFilePath);           //刷新当前目录文件列表
        } else {
            Toast.makeText(MainActivity.this, "未复制文件！", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 实现复制方法
     */
    public int copyFile(String fromFile, String toFile) {
        //要复制的文件目录
        File[] currentFiles;
        File from = new File(fromFile);
        //如同判断SD卡是否存在或者文件是否存在
        //如果不存在则 return出去
        if (!from.exists()) {
            return -1;
        }
        //复制的是文件夹的情况下
        if (from.isDirectory()) {
            //如果存在则获取当前目录下的全部文件 填充数组
            currentFiles = from.listFiles();
            //目标目录
            File targetDir = new File(toFile);
            //创建目录
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            //遍历要复制该目录下的全部文件
            for (int i = 0; i < currentFiles.length; i++) {
                if (currentFiles[i].isDirectory())//如果是子目录就进行递归
                {
                    copyFile(currentFiles[i].getPath() + "/", toFile + "/"
                            + currentFiles[i].getName() + "/");

                } else//如果是文件就粘贴文件
                {
                    CopySdcardFile(currentFiles[i].getPath(), toFile
                            + "/" + currentFiles[i].getName());
                }
            }
        } else {//如果是文件就直接粘贴
            CopySdcardFile(fromFile, toFile);
        }
        return 0;
    }


    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    public int CopySdcardFile(String fromFile, String toFile) {

        try {
            //需要复制文件目录的输入流
            InputStream fos = new FileInputStream(fromFile);
            //文件粘贴目录的输出流
            OutputStream to = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            //根据复制文件目录写入新的粘贴目录
            while ((c = fos.read(bt)) > 0) {
                to.write(bt, 0, c);
            }
            fos.close();
            to.close();
            return 0;

        } catch (Exception ex) {
            return -1;
        }
    }


    //自定义Adapter内部类
    class FileAdapter extends BaseAdapter {
        //各种文件的图标
        private Bitmap lBackRoot;
        private Bitmap lBackUp;
        private Bitmap lImage;
        private Bitmap lAudio;
        private Bitmap lRar;
        private Bitmap lVideo;
        private Bitmap lFolder;
        private Bitmap lApk;
        private Bitmap lOthers;
        private Bitmap lTxt;
        private Bitmap lWeb;

        private Context lContext;
        //文件名列表
        private List<String> lFileNameList;
        //文件对应的路径列表
        private List<String> lFilePathList;

        public FileAdapter(Context context, List<String> fileName, List<String> filePath) {
            lContext = context;
            lFileNameList = fileName;
            lFilePathList = filePath;
            //初始化图片资源
            //返回到根目录
            lBackRoot = BitmapFactory.decodeResource(lContext.getResources(), R.drawable.back_to_root);
            //返回到上一级目录
            lBackUp = BitmapFactory.decodeResource(lContext.getResources(), R.drawable.back_to_up);
            //图片文件对应的icon
            lImage = BitmapFactory.decodeResource(lContext.getResources(), R.drawable.icon_image);
            //音频文件对应的icon
            lAudio = BitmapFactory.decodeResource(lContext.getResources(), R.drawable.icon_audio);
            //视频文件对应的icon
            lVideo = BitmapFactory.decodeResource(lContext.getResources(), R.drawable.icon_video);
            //可执行文件对应的icon
            lApk = BitmapFactory.decodeResource(lContext.getResources(), R.drawable.icon_apk);
            //文本文档对应的icon
            lTxt = BitmapFactory.decodeResource(lContext.getResources(), R.drawable.txt);
            //其他类型文件对应的icon
            lOthers = BitmapFactory.decodeResource(lContext.getResources(), R.drawable.icon_others);
            //文件夹对应的icon
            lFolder = BitmapFactory.decodeResource(lContext.getResources(), R.drawable.icon_folder);
            //zip文件对应的icon
            lRar = BitmapFactory.decodeResource(lContext.getResources(), R.drawable.icon_zip);
            //网页文件对应的icon
            lWeb = BitmapFactory.decodeResource(lContext.getResources(), R.drawable.icon_web);
        }

        //获得文件的总数
        public int getCount() {
            return lFilePathList.size();
        }

        //获得当前位置对应的文件名
        public Object getItem(int position) {
            return lFileNameList.get(position);
        }

        //获得当前的位置
        public long getItemId(int position) {
            return position;
        }

        //获得视图
        public View getView(int position, View convertView, ViewGroup viewgroup) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater mLI = (LayoutInflater) lContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //初始化列表元素界面
                convertView = mLI.inflate(R.layout.list_child, null);
                //获取列表布局界面元素
                viewHolder.lIV = (ImageView) convertView.findViewById(R.id.image_list_childs);
                viewHolder.lTV = (TextView) convertView.findViewById(R.id.text_list_childs);
                //将每一行的元素集合设置成标签
                convertView.setTag(viewHolder);
            } else {
                //获取视图标签
                viewHolder = (ViewHolder) convertView.getTag();
            }

            File mFile = new File(lFilePathList.get(position).toString());
            //如果
            if (lFileNameList.get(position).toString().equals("BacktoRoot")) {
                //添加返回根目录的按钮
                viewHolder.lIV.setImageBitmap(lBackRoot);
                viewHolder.lTV.setText("返回根目录");
            } else if (lFileNameList.get(position).toString().equals("BacktoUp")) {
                //添加返回上一级菜单的按钮
                viewHolder.lIV.setImageBitmap(lBackUp);
                viewHolder.lTV.setText("返回上一级");
            } else {
                String fileName = mFile.getName();
                viewHolder.lTV.setText(fileName);
                if (mFile.isDirectory()) {
                    viewHolder.lIV.setImageBitmap(lFolder);
                } else {
                    String fileEnds = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();//取出文件后缀名并转成小写
                    if (fileEnds.equals("m4a") || fileEnds.equals("mp3") || fileEnds.equals("mid") || fileEnds.equals("xmf") || fileEnds.equals("ogg") || fileEnds.equals("wav")) {
                        viewHolder.lIV.setImageBitmap(lVideo);
                    } else if (fileEnds.equals("3gp") || fileEnds.equals("mp4")) {
                        viewHolder.lIV.setImageBitmap(lAudio);
                    } else if (fileEnds.equals("jpg") || fileEnds.equals("gif") || fileEnds.equals("png") || fileEnds.equals("jpeg") || fileEnds.equals("bmp")) {
                        viewHolder.lIV.setImageBitmap(lImage);
                    } else if (fileEnds.equals("icon_apk")) {
                        viewHolder.lIV.setImageBitmap(lApk);
                    } else if (fileEnds.equals("txt")) {
                        viewHolder.lIV.setImageBitmap(lTxt);
                    } else if (fileEnds.equals("zip") || fileEnds.equals("rar")) {
                        viewHolder.lIV.setImageBitmap(lRar);
                    } else if (fileEnds.equals("html") || fileEnds.equals("htm") || fileEnds.equals("mht")) {
                        viewHolder.lIV.setImageBitmap(lWeb);
                    } else {
                        viewHolder.lIV.setImageBitmap(lOthers);
                    }
                }
            }

            return convertView;
        }

        //用于存储列表每一行元素的图片和文本
        class ViewHolder {
            ImageView lIV;
            TextView lTV;
        }
    }
}
