package com.mingrisoft.setblacklist;

import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mingrisoft.setblacklist.entity.DaoMaster;
import com.mingrisoft.setblacklist.entity.DaoSession;
import com.mingrisoft.setblacklist.entity.PersonEntity;
import com.mingrisoft.setblacklist.entity.PersonEntityDao;

import java.lang.reflect.Method;

public class MainActivity extends MPermissionsActivity {

    // 数据库
    private SQLiteDatabase db;
    // 管理者
    private DaoMaster mDaoMaster;
    // 会话
    private DaoSession mDaoSession;
    private PersonEntityDao entityDao;
    private EditText editText;
    private Button button;
    private TextView textView;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 动态申请权限
         * */
        requestPermission(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.WRITE_CALL_LOG,}, 1000);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
        preferences = getSharedPreferences("sp",CONTEXT_RESTRICTED);
        editor = preferences.edit();
        // 此DevOpenHelper类继承自SQLiteOpenHelper,第一个参数Context,第二个参数数据库名字,第三个参数CursorFactory
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "persondao.db", null);
        db = helper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
        entityDao = mDaoSession.getPersonEntityDao();
        editText = (EditText) findViewById(R.id.num_et);
        button = (Button) findViewById(R.id.add_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().trim().length() > 0) {
                    saveData(editText.getText().toString());
                } else {
                    Toast.makeText(MainActivity.this, "电话号不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        textView = (TextView) findViewById(R.id.see_list);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListViewActivity.class);
                startActivity(intent);

            }
        });
    }
/**
 * 保存到数据库
 * */
    private void saveData(String num) {
        //初始化实体类
        PersonEntity personEntity = new PersonEntity(preferences.getLong("id",0),preferences.getLong("id",0), null, num);
        entityDao.insert(personEntity);  //将数据添加到数据库中
        editor.putLong("id",preferences.getLong("id",0)+1);  //数据库自增的id
        editor.commit();                 //保存
        Toast.makeText(this, "成功添加到黑名单", Toast.LENGTH_SHORT).show();  //弹出保存完成的提示框
        editText.setText("");   //清空输入框
    }


}
