package view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.mingrisoft.iosdialog.R;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;
/**
 * Created by Administrator on 2016/11/25.
 */

public class Mdialog extends Dialog {
    private Button button_cancel,button_exit;        //定义取消与确认按钮
    private TextView tv;                                   //定义标题文字
    //自定义构造方法
    public Mdialog(Context context) {
        super(context, R.style.mdialog);
        View view = LayoutInflater.from(getContext()).
                inflate(R.layout.mdialoglayout, null);  //通过LayoutInflater获取布局
        tv = (TextView) view.findViewById(R.id.title);   //获取显示标题的文本框控件
        button_cancel = (Button) view.findViewById(R.id.btn_cancel);    //获取取消按钮
        button_exit = (Button) view.findViewById(R.id.btn_exit);  //获取确认退出按钮
        setContentView(view);  //设置显示的视图
    }
    //设置显示的标题文字
    public void setTv(String content) {
        tv.setText(content);
    }
    //取消按钮监听
    public void setOnCancelListener(View.OnClickListener listener){
        button_cancel.setOnClickListener(listener);
    }

        //退出按钮监听
        public void setOnExitListener(View.OnClickListener listener){
        button_exit.setOnClickListener(listener);
    }
}
