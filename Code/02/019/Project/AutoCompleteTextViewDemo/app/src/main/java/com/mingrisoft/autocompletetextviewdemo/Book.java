package com.mingrisoft.autocompletetextviewdemo;

/**
 * Created by Administrator on 2016/12/12 0012.
 */
//书籍实体类
public class Book {
    private String bookname;

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }
    //必须添加方法，否则输出的不为中文
    @Override
    public String toString() {
        return  bookname;
    }
}
