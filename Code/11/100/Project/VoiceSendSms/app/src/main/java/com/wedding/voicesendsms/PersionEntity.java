package com.wedding.voicesendsms;

/**
 * Created by Administrator on 2016/11/22.
 */

public class PersionEntity {

    public PersionEntity(String name, String num) {
        this.name = name;
        this.num = num;
    }

    private String name;
    private String num;

    public PersionEntity() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
