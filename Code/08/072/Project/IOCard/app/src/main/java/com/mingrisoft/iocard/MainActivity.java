package com.mingrisoft.iocard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText txt;//显示银行卡的卡号
    private int MY_SCAN_REQUEST_CODE = 100;//请求码
    private ImageView img;//开启银行卡扫描

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //显示返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //初始化控件
        txt = (EditText) findViewById(R.id.txt);
        img = (ImageView) findViewById(R.id.scanButton);
        img.setOnClickListener(this);//点击事件
    }

    /**
     * 按钮的点击事件
     */
    public void onScanPress() {
        //创建意图
        Intent scanIntent = new Intent(this, CardIOActivity.class);
        //关闭键盘显示
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true);
        //跳转界面
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    /**
     * 返回界面
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String resultStr;//用来保存获取的结果
        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            //获取返回内容
            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
            //获取银行卡卡号
            resultStr = scanResult.getFormattedCardNumber();
        } else {
            resultStr = "扫描已取消";
        }
        txt.setText(resultStr);
    }

    @Override
    public void onClick(View v) {
        onScanPress();
    }
}
