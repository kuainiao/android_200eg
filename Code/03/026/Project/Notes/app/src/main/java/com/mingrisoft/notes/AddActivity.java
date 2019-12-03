package com.mingrisoft.notes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.mingrisoft.notes.db.DatabaseOperation;
import com.mingrisoft.notes.view.LineEditText;
import com.mingrisoft.notes.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends Activity {
    private Button bt_back;
    private Button bt_save;
    private TextView tv_title;
    private SQLiteDatabase db;//数据库操作类
    private DatabaseOperation dop;//自定义数据库
    private LineEditText et_Notes;
//    private GridView bottomMenu;
//    // 底部按钮菜单按钮图片集合
//    private int[] bottomItems = {R.drawable.tabbar_handwrite,
//            R.drawable.tabbar_paint, R.drawable.tabbar_microphone,
//            R.drawable.tabbar_photo, R.drawable.tabbar_camera,
//            R.drawable.tabbar_appendix};
    InputMethodManager imm;//控制手机键盘
    Intent intent;
    String editModel = null;
    int item_Id;
    String title;
    String time;
    String context;
    public String datatype = "0";// 判断是否开启记录开启了提醒功能
    public String datatime = "0";// 提醒时间
    public String locktype = "0";// 判断是否打开密码锁
    public String lock = "0";// 密码
    // 记录editText中的图片，用于单击时判断单击的是那一个图片
    private List<Map<String, String>> imgList = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        bt_back = (Button) findViewById(R.id.bt_back);
        bt_back.setOnClickListener(new ClickEvent());
        bt_save = (Button) findViewById(R.id.bt_save);
        bt_save.setOnClickListener(new ClickEvent());
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_Notes = (LineEditText) findViewById(R.id.et_note);

        // 默认关闭软键盘,可以通过失去焦点设置
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_Notes.getWindowToken(), 0);
        dop = new DatabaseOperation(this, db);
        intent = getIntent();
        editModel = intent.getStringExtra("editModel");
        item_Id = intent.getIntExtra("noteId", 0);
        // 加载数据
        loadData();
        // 给editText添加单击事件
        et_Notes.setOnClickListener(new TextClickEvent());
    }

    // 加载数据
    private void loadData() {
        // 如果是新增记事模式，则将editText清空
        if (editModel.equals("newAdd")) {
            et_Notes.setText("");
        }
        // 如果编辑的是已存在的记事，则将数据库的保存的数据取出，并显示在EditText中
        else if (editModel.equals("update")) {
            tv_title.setText("编辑记事");
            dop.create_db();
            Cursor cursor = dop.query_db(item_Id);
            cursor.moveToFirst();
            // 取出数据库中相应的字段内容
            context = cursor.getString(cursor.getColumnIndex("context"));
            datatype = cursor.getString(cursor.getColumnIndex("datatype"));
            datatime = cursor.getString(cursor.getColumnIndex("datatime"));
            locktype = cursor.getString(cursor.getColumnIndex("locktype"));
            lock = cursor.getString(cursor.getColumnIndex("lock"));

            // 定义正则表达式，用于匹配路径
            Pattern p = Pattern.compile("/([^\\.]*)\\.\\w{3}");
            Matcher m = p.matcher(context);
            int startIndex = 0;
            while (m.find()) {
                // 取出路径前的文字
                if (m.start() > 0) {
                    et_Notes.append(context.substring(startIndex, m.start()));
                }
                SpannableString ss = new SpannableString(m.group().toString());
                // 取出路径
                String path = m.group().toString();
                // 取出路径的后缀
                String type = path.substring(path.length() - 3, path.length());
                Bitmap bm = null;
                Bitmap rbm = null;
                // 判断附件的类型，如果是录音文件，则从资源文件中加载图片
                if (type.equals("amr")) {

                } else {
                    // 取出图片
                    bm = BitmapFactory.decodeFile(m.group());
                    // 缩放图片
                    rbm = resize(bm, 480);
                    // 为图片添加边框效果
                    rbm = getBitmapHuaSeBianKuang(rbm);
                }
                ImageSpan span = new ImageSpan(this, rbm);
                ss.setSpan(span, 0, m.end() - m.start(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                System.out.println(m.start() + "-------" + m.end());
                et_Notes.append(ss);
                startIndex = m.end();
                // 用List记录该录音的位置及所在路径，用于单击事件
                Map<String, String> map = new HashMap<String, String>();
                map.put("location", m.start() + "-" + m.end());
                map.put("path", path);
                imgList.add(map);
            }
            // 将最后一个图片之后的文字添加在TextView中
            et_Notes.append(context.substring(startIndex, context.length()));
            dop.close_db();
        }
    }

    // 为EidtText设置监听器
    class TextClickEvent implements OnClickListener {
        @Override
        public void onClick(View v) {
            Spanned s = et_Notes.getText();
            ImageSpan[] imageSpans;
            imageSpans = s.getSpans(0, s.length(), ImageSpan.class);
            int selectionStart = et_Notes.getSelectionStart();
            for (ImageSpan span : imageSpans) {
                        int start = s.getSpanStart(span);
                        int end = s.getSpanEnd(span);
                        // 找到图片
                        if (selectionStart >= start && selectionStart < end) {
                            // 查找当前单击的图片是哪一个图片
                            String path = null;
                            for (int i = 0; i < imgList.size(); i++) {
                                Map map = imgList.get(i);
                                // 找到了
                                if (map.get("location").equals(start + "-" + end)) {
                                    path = imgList.get(i).get("path");
                                    break;
                                }
                            }
                } else
                    // 如果单击的是空白出或文字，则获得焦点，即打开软键盘
                    imm.showSoftInput(et_Notes, 0);
            }
        }
    }

    // 给编辑区域设置触摸监听器
    class TextTouchEvent implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Spanned s = et_Notes.getText();
            ImageSpan[] imageSpans;
            imageSpans = s.getSpans(0, s.length(), ImageSpan.class);
            int selectionStart = et_Notes.getSelectionStart();
            for (ImageSpan span : imageSpans) {
                int start = s.getSpanStart(span);
                int end = s.getSpanEnd(span);
                int inType = et_Notes.getInputType(); // backup the input type
                // 找到图片
                if (selectionStart >= start && selectionStart < end) {
                    Bitmap bitmap = ((BitmapDrawable) span.getDrawable())
                            .getBitmap();
                    et_Notes.setInputType(InputType.TYPE_NULL); // disable soft
                    et_Notes.onTouchEvent(event); // call native handler
                    et_Notes.setInputType(inType); // restore input type
                    AddActivity.this.finish();
                } else {
                    // 如果单击的是空白出或文字，则获得焦点，即打开软键盘
                    imm.showSoftInput(et_Notes, 0);
                    et_Notes.setInputType(inType);
                }
            }
            return true;
        }
    }

    // 设置按钮监听器
    class ClickEvent implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_back:
                    // 当前Activity结束，则返回上一个Activity
                    AddActivity.this.finish();
                    break;
                // 将记事添加到数据库中
                case R.id.bt_save:
                    // 取得EditText中的内容
                    context = et_Notes.getText().toString();
                    if (context.isEmpty()) {
                        Toast.makeText(AddActivity.this, "记事为空!", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        // 取得当前时间
                        SimpleDateFormat formatter = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm");
                        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                        time = formatter.format(curDate);
                        // 截取EditText中的前一部分作为标题，用于显示在主页列表中
                        title = getTitle(context);
                        // 打开数据库
                        dop.create_db();
                        // 判断是更新还是新增记事
                        if (editModel.equals("newAdd")) {
                            // 将记事插入到数据库中
                            dop.insert_db(title, context, time, datatype, datatime,
                                    locktype, lock);
                        }
                        // 如果是编辑则更新记事即可
                        else if (editModel.equals("update")) {
                            dop.update_db(title, context, time, datatype, datatime,
                                    locktype, lock, item_Id);
                        }
                        dop.close_db();
                        // 结束当前activity
                        AddActivity.this.finish();
                    }
                    break;
            }
        }
    }

    // 截取EditText中的前一部分作为标题，用于显示在主页列表中
    private String getTitle(String context) {
        // 定义正则表达式，用于匹配路径
        Pattern p = Pattern.compile("/([^\\.]*)\\.\\w{3}");
        Matcher m = p.matcher(context);
        StringBuffer strBuff = new StringBuffer();
        String title = "";
        int startIndex = 0;
        while (m.find()) {
            // 取出路径前的文字
            if (m.start() > 0) {
                strBuff.append(context.substring(startIndex, m.start()));
            }
            // 取出路径
            String path = m.group().toString();
            // 取出路径的后缀
            String type = path.substring(path.length() - 3, path.length());
            // 判断附件的类型
            if (type.equals("amr")) {
                strBuff.append("[录音]");
            } else {
                // strBuff.append("");
            }
            startIndex = m.end();
            // 只取出前15个字作为标题
            if (strBuff.length() > 15) {
                // 统一将回车,等特殊字符换成空格
                title = strBuff.toString().replaceAll("\r|\n|\t", " ");
                return title;
            }
        }
        strBuff.append(context.substring(startIndex, context.length()));
        // 统一将回车,等特殊字符换成空格
        title = strBuff.toString().replaceAll("\r|\n|\t", " ");
        return title;
    }



    // 等比例缩放图片
    private Bitmap resize(Bitmap bitmap, int S) {
        int imgWidth = bitmap.getWidth();
        int imgHeight = bitmap.getHeight();
        double partion = imgWidth * 1.0 / imgHeight;
        double sqrtLength = Math.sqrt(partion * partion + 1);
        // 新的缩略图大小
        double newImgW = S * (partion / sqrtLength);
        double newImgH = S * (1 / sqrtLength);
        float scaleW = (float) (newImgW / imgWidth);
        float scaleH = (float) (newImgH / imgHeight);
        Matrix mx = new Matrix();
        // 对原图片进行缩放
        mx.postScale(scaleW, scaleH);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, mx,
                true);
        return bitmap;
    }

    // 给图片加边框，并返回边框后的图片
    public Bitmap getBitmapHuaSeBianKuang(Bitmap bitmap) {
        float frameSize = 0.2f;
        Matrix matrix = new Matrix();

        // 用来做底图
        Bitmap bitmapbg = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // 设置底图为画布
        Canvas canvas = new Canvas(bitmapbg);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));

        float scale_x = (bitmap.getWidth() - 2 * frameSize - 2) * 1f
                / (bitmap.getWidth());
        float scale_y = (bitmap.getHeight() - 2 * frameSize - 2) * 1f
                / (bitmap.getHeight());
        matrix.reset();
        matrix.postScale(scale_x, scale_y);
        // 对相片大小处理(减去边框的大小)
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        paint.setStyle(Style.FILL);

        // 绘制底图边框
        canvas.drawRect(
                new Rect(0, 0, bitmapbg.getWidth(), bitmapbg.getHeight()),
                paint);

        // 绘制灰色边框
        paint.setColor(Color.GRAY);
        canvas.drawRect(
                new Rect((int) (frameSize), (int) (frameSize), bitmapbg
                        .getWidth() - (int) (frameSize), bitmapbg.getHeight()
                        - (int) (frameSize)), paint);

        canvas.drawBitmap(bitmap, frameSize + 1, frameSize + 1, paint);
        return bitmapbg;
    }
}
