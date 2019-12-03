package com.mingrisoft.autocompletetextviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class  MainActivity extends AppCompatActivity {
   private List<Book> booknames;
    private MyAdapter bookAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //装载数据到数组
        initData();
        //初始化控件
        initView();
    }
    private void initView() {
        //找到相应的控件
        AutoCompleteTextView autoCompleteTextView
                = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
        //配置Adaptor
        bookAdapter = new MyAdapter(booknames,this);
        //填充数据到控件
        autoCompleteTextView.setAdapter(bookAdapter);
    }
    //装载数据，这里为手动录入数据
    private void initData() {
        Book books= new Book();
        books.setBookname("书籍：Android移动开发");
        Book books1= new Book();
        books1.setBookname("书籍：Android移动开发");
        Book books2= new Book();
        books2.setBookname("书籍：Java从入门到精通");
        Book books3= new Book();
        books3.setBookname("书籍：JavaWeb程序设计");
        Book books4= new Book();
        books4.setBookname("书籍：JSP程序设计");
        Book books5= new Book();
        books5.setBookname("书籍：PHP程序设计");
        Book books6= new Book();
        books6.setBookname("书籍：c#程序设计");
        Book books7= new Book();
        books7.setBookname("书籍：Sql Server数据库管理与开发");
        Book books8= new Book();
        books8.setBookname("书籍：Oracle数据库管理与开发");
        Book books9= new Book();
        books9.setBookname("书籍：Java开发实例大全");
        Book books10= new Book();
        books10.setBookname("书籍：Java Web开发实例大全");
        Book books11= new Book();
        books11.setBookname("书籍：ASP.NET开发实例大全");
        Book books12= new Book();
        books12.setBookname("书籍：Visual Basic开发实例大全");
        Book books13= new Book();
        books13.setBookname("书籍：Visual C++开发实例大全");
        Book books14= new Book();
        books14.setBookname("书籍：C#开发实例大全");
        Book books15= new Book();
        books15.setBookname("书籍：Android程序开发范例宝典");
        Book books16= new Book();
        books16.setBookname("书籍：HTML5程序开发范例宝典");
        booknames= new ArrayList<>();
        //添加数据到数组
        booknames.add(books);
        booknames.add(books1);
        booknames.add(books2);
        booknames.add(books3);
        booknames.add(books4);
        booknames.add(books5);
        booknames.add(books6);
        booknames.add(books7);
        booknames.add(books8);
        booknames.add(books9);
        booknames.add(books10);
        booknames.add(books11);
        booknames.add(books12);
        booknames.add(books13);
        booknames.add(books14);
        booknames.add(books15);
        booknames.add(books16);
    }

}
