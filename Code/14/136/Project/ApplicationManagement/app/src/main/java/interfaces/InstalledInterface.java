package interfaces;

/**
 * Created by Administrator on 2017/1/13.
 */

import android.view.View;

/**
 * 接口说明：已安装应用点击事件接口
 */
public interface InstalledInterface {
    void setOpenOnClick(int pos, View v);
    void setUninstallOnClick(int pos, View v);
}
