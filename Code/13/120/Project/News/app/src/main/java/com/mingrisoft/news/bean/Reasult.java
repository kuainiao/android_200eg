package com.mingrisoft.news.bean;

/**
 * Author LYJ
 * Created on 2016/12/6.
 * Time 14:10
 */

public class Reasult {
    private String ctime;//时间
    private String title;//标题
    private String description;//来源
    private String picUrl;//图片
    private String url;//新闻网址链接

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
