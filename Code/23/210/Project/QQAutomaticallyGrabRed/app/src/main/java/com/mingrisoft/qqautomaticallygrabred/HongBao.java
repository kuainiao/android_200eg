package com.mingrisoft.qqautomaticallygrabred;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * 准备红包服务
 */
public class HongBao extends AccessibilityService {
    private String[] filter = new String[]{"恭喜发财"};  //红包名字的数组
    AccessibilityNodeInfo accessibilityNodeInfo = null;  //辅助节点信息类，获取当前界面信息
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //获取当前界面的信息
        accessibilityNodeInfo = event.getSource();
        //信息为空直接返回
        if (accessibilityNodeInfo == null) {
            return;
        }
        //执行是否打开红包方法
        isOpen(accessibilityNodeInfo);
    }

    /**
     *找到对应名字的红包，判断是否打开红包
     */
    private void isOpen(AccessibilityNodeInfo rootNodeInfo) {
        //将找到的红包名称保存在list中
        List<AccessibilityNodeInfo> list = findByText(rootNodeInfo);
        if (list == null) {
            return;
        }
        //获取红包的位置
        AccessibilityNodeInfo nodeInfo1 = list.get(list.size() - 1);
        if (nodeInfo1 != null) {
            openRed(rootNodeInfo);
        }
    }
    /**
     *查找指定名称的红包
     */
    private List<AccessibilityNodeInfo> findByText(AccessibilityNodeInfo rootNodeInfo) {
        //便利红包名称，并查找该名称的红包
        for (String name : filter) {
            List<AccessibilityNodeInfo> list =
                    rootNodeInfo.findAccessibilityNodeInfosByText(name);
            //如果指定红包名称的信息不是空的  就返回这条信息
            if (list != null && !list.isEmpty()) {
                return list;
            }
        }
        return null;
    }
    /**
     *打开红包
     */
    private void openRed(AccessibilityNodeInfo rootNodeInfo) {
        //获取当前界面中子控件的数量
        int child = rootNodeInfo.getChildCount();
        //便利当前界面中的每个子控件
        for (int i=0;i<child;i++) {
            //获取每个子控件
            AccessibilityNodeInfo info = rootNodeInfo.getChild(i);
            //判断子控件是否为空
            if (info == null) {
                continue;
            }
            if (info.getChildCount() > 0) {
                openRed(info);       //设置新的数据
            } else {    //根据文字判断是否有没拆开的红包
                if ("点击拆开".equals(info.getText())){     //如果有新的红包进行拆红包
                    rootNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }


    @Override
    public void onInterrupt() {

    }
}
