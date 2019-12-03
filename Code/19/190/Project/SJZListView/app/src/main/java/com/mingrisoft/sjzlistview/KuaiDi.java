package com.mingrisoft.sjzlistview;
//列表实体类
public class KuaiDi {
    //内容信息
    private String content;
    //时间信息
    private String time;

    public KuaiDi(String time, String content) {
        this.content = content;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}