package com.mingrisoft.autocompletetextviewdemo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/12 0012.
 */

public class MyAdapter extends BaseAdapter implements Filterable {
    private ArrayFilter  mArrayFilter;
    private List<Book> books;
    private Context context;
    private ArrayList<Book> mFilterBooks ;
   //初始化方法
    public MyAdapter(List<Book> mList, Context context) {
        this.books = mList;
        this.context = context;
    }
    //数组长度
    @Override
    public int getCount() {
        return books==null ? 0:books.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if(convertView==null){
            view = View.inflate(context, R.layout.list_item, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) view.findViewById(R.id.bookname);
            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        Book bookname = books.get(position);
        holder.tv_name.setText(bookname.getBookname());
        return view;
    }

    static class ViewHolder{
        public TextView tv_name;

    }
//同时，由于我们实现了Filterable接口，所以还要实现该接口里边的一个方法：
    @Override
    public Filter getFilter() {
        if(mArrayFilter==null){
            mArrayFilter = new ArrayFilter ();
        }
        return mArrayFilter;
    }
    //ArrayFilter是我们实现数据过滤的一个关键类，该类继承自Filter，实现其中的两个方法，
    // 第一方法时数据的过滤逻辑，第二个方法是把过滤结果赋值给数据源。
    private class ArrayFilter  extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (mFilterBooks == null) {
                mFilterBooks = new ArrayList<Book>(books);
            }
            //如果没有过滤条件则不过滤
            if (constraint == null || constraint.length() == 0) {
                results.values = mFilterBooks;
                results.count = mFilterBooks.size();
            } else {
                List<Book> retList = new ArrayList<Book>();
                //过滤条件
                String str = constraint.toString().toLowerCase();
                //循环变量数据源，如果有属性满足过滤条件，则添加到result中
                for (Book book : mFilterBooks) {
                    if (book.getBookname().contains(str)) {
                        retList.add(book);
                    }
                }
                results.values = retList;
                results.count = retList.size();
            }
            return results;
        }

        //在这里返回过滤结果
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
//          notifyDataSetInvalidated()，会重绘控件（还原到初始状态）
//          notifyDataSetChanged()，重绘当前可见区域
            books = (List<Book>) results.values;
            if(results.count>0){
                notifyDataSetChanged();
            }else{
                notifyDataSetInvalidated();
            }
        }

    }
}