package com.mingrisoft.selectimagecode.code;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Author LYJ
 * Created on 2017/1/22.
 * Time 17:50
 */

public class CodeDataUtils {
    /**
     * 标记值
     */
    private static String[] tagName = {
            "鼠","牛","虎","兔",
            "蛇","龙","马","羊",
            "猴","鸡","狗","猪"
    };
    /**
     * 资源名
     */
    private static String[] resName = {
            "shu","niu","hu","tu",
            "she","long","ma","yang",
            "hou","ji","gou","zhu"
    };

    /**
     * 获取资源中的图片资源
     * @param context
     * @return
     */
    public static CodeList getDataList(Context context){
        CodeList codeList = new CodeList();
        List<CodeType> type = new ArrayList<>();//类别总数
        List<Code> all = new ArrayList<>();//数据总数
        //获取上下文
        Context codeContext = context.getApplicationContext();
        //获取资源
        Resources resources = codeContext.getResources();
        //获取包名
        String packageName = codeContext.getPackageName();
        //遍历资源
        int count = 0;//计数
        for (int i = 0 ; i < tagName.length ; i++){
            List<Code> codes = new ArrayList<>();//按类别存储
            for (int j = 0;j < 2;j++){
                //获取指定的资源id
                int resID = resources.getIdentifier(resName[i] + (j + 1),"mipmap",packageName);
                //获取图片
                Bitmap bitmap = BitmapFactory.decodeResource(resources,resID);
                Code code = new Code(bitmap,tagName[i],count);
                codes.add(code);//存入类别集合
                all.add(code);//存入数据总结和
                count++;
            }
            CodeType codeType = new CodeType();//类别
            codeType.setType(tagName[i]);//类别名称
            codeType.setCode(codes);//存入类别数据
            type.add(codeType);//存入类别
        }
        codeList.setTypeList(type);//类型集合
        codeList.setCodeList(all);//全部数据
        return codeList;
    }
}
