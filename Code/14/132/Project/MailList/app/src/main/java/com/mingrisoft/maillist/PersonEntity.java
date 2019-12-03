package com.mingrisoft.maillist;



public class PersonEntity {

    public PersonEntity(String name, String num) {
        this.name = name;
        this.num = num;
    }

    private String name;
    private String num;

    public PersonEntity() {

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
