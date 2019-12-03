package adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.mingrisoft.searchapplication.R;

import java.util.List;

import util.AppInfo;
import util.IUninstall;
import util.Utils;

/**
 * 显示应用程序信息列表
 */
public class MyAdapter extends BaseAdapter {

    List<AppInfo> list;
    LayoutInflater inflater;
    PackageManager pm;// 包管理器


    public void setUninstall(IUninstall uninstall) {
        this.uninstall = uninstall;
    }

    // 声明接口变量
    IUninstall uninstall;

    public MyAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.pm = context.getPackageManager();
    }

    public void setList(List<AppInfo> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return (list == null) ? 0 : list.size();// 三元运算
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item, null);
            holder = new ViewHolder();
            holder.logo = (ImageView) convertView.findViewById(R.id.logo);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.version = (TextView) convertView.findViewById(R.id.version);
            holder.size = (TextView) convertView.findViewById(R.id.size);
            holder.btn = (Button) convertView.findViewById(R.id.btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AppInfo map = list.get(position);
        final String pkgName = map.packageName;
        // 拿到图标 OOM=out of Memory
        if (map.icon == null) {// 优化
            map.icon = map.applicationInfo.loadIcon(pm);//57
        }
        holder.logo.setImageDrawable(map.icon);
        //对下面的应用名进行高亮处理
        // holder.title.setText((String) map.appName);

        String w = (String) map.appName;// 原始应用名
        String key = Utils.KEY;
        int start = w.toLowerCase().indexOf(key.toLowerCase());//高亮文字的起始位置
        if (start > -1) {// 有

            int end = start + key.length();//高亮文字的终止位置
            // 字符串样式对象
            SpannableStringBuilder style
                    = new SpannableStringBuilder((String) map.appName);
            style.setSpan(// 设定样式
                    new ForegroundColorSpan(Color.BLUE),// 前景样式
                    start,// 起始坐标
                    end,// 终止坐标
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE// 旗标
            );

            holder.title.setText(style);// 将样式设置给TextView
        } else {
            holder.title.setText(w);// 将原来的文字赋值
        }

        holder.version.setText((String) map.versionName + "(" + map.versionCode + ")");
        holder.size.setText((String) map.size + "M  " + Utils.getTime(map.lastUpdateTime));
// 点击"卸载"按钮的点击事件
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uninstall.onBtnClick(position, pkgName);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        ImageView logo;
        TextView title;
        TextView version;
        TextView size;
        Button btn;// 按钮
    }
}
