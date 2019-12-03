//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mrkj.sample_76;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class MPermissionsActivity extends Activity {
    private final String TAG = "MPermissions";
    private int REQUEST_CODE_PERMISSION = 153;

    public MPermissionsActivity() {
    }

    public void requestPermission(String[] permissions, int requestCode) {
        this.REQUEST_CODE_PERMISSION = requestCode;
        if(this.checkPermissions(permissions)) {
            this.permissionSuccess(this.REQUEST_CODE_PERMISSION);
        } else {
            List needPermissions = this.getDeniedPermissions(permissions);
            ActivityCompat.requestPermissions(this, (String[])needPermissions.toArray(new String[needPermissions.size()]), this.REQUEST_CODE_PERMISSION);
        }

    }

    private boolean checkPermissions(String[] permissions) {
        if(VERSION.SDK_INT < 23) {
            return true;
        } else {
            String[] var2 = permissions;
            int var3 = permissions.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String permission = var2[var4];
                if(ContextCompat.checkSelfPermission(this, permission) != 0) {
                    return false;
                }
            }

            return true;
        }
    }

    private List<String> getDeniedPermissions(String[] permissions) {
        ArrayList needRequestPermissionList = new ArrayList();
        String[] var3 = permissions;
        int var4 = permissions.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String permission = var3[var5];
            if(ContextCompat.checkSelfPermission(this, permission) != 0 || ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissionList.add(permission);
            }
        }

        return needRequestPermissionList;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == this.REQUEST_CODE_PERMISSION) {
            if(this.verifyPermissions(grantResults)) {
                this.permissionSuccess(this.REQUEST_CODE_PERMISSION);
            } else {
                this.permissionFail(this.REQUEST_CODE_PERMISSION);
                this.showTipsDialog();
            }
        }

    }

    private boolean verifyPermissions(int[] grantResults) {
        int[] var2 = grantResults;
        int var3 = grantResults.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            int grantResult = var2[var4];
            if(grantResult != 0) {
                return false;
            }
        }

        return true;
    }

    private void showTipsDialog() {
        (new Builder(this)).setTitle("提示信息").setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。").setNegativeButton("取消", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton("确定", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MPermissionsActivity.this.startAppSettings();
            }
        }).show();
    }

    private void startAppSettings() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + this.getPackageName()));
        this.startActivity(intent);
    }

    public void permissionSuccess(int requestCode) {
        Log.e("------", "获取权限成功=" + requestCode);
    }

    public void permissionFail(int requestCode) {
        Log.d("MPermissions", "获取权限失败=" + requestCode);
    }
}
