package com.mingrisoft.selectimagecode.code;

import java.util.List;

/**
 * Author LYJ
 * Created on 2017/1/22.
 * Time 18:15
 */

public class CodeList {
    private List<Code> codeList;//所有集合
    private List<CodeType> typeList;//类型集合

    public List<Code> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<Code> codeList) {
        this.codeList = codeList;
    }

    public List<CodeType> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<CodeType> typeList) {
        this.typeList = typeList;
    }
}
