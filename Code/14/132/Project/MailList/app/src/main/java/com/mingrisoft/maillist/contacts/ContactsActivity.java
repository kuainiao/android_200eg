package com.mingrisoft.maillist.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.ListView;
import android.widget.TextView;
import com.mingrisoft.maillist.tool.CharacterParser;
import com.mingrisoft.maillist.tool.ClearEditText;
import com.mingrisoft.maillist.PersonEntity;
import com.mingrisoft.maillist.R;
import com.mingrisoft.maillist.tool.PinyinComparator;
import com.mingrisoft.maillist.tool.SideBar;
import com.mingrisoft.maillist.tool.SortModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class ContactsActivity extends AppCompatActivity {
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private AdapterContact adapter;
    private ClearEditText mClearEditText;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    List<SortModel> mSortList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_layout);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
        initData();
        getPhoneContacts();
    }


    private void initData() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mSortList = new ArrayList<SortModel>();
        sideBar.setTextView(dialog);
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });
        SourceDateList = new ArrayList<>();
        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new AdapterContact(this, SourceDateList);
        sortListView.setAdapter(adapter);
        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

                                                  @Override
                                                  public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                      //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                                                      filterData(s.toString());
                                                  }

                                                  @Override
                                                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                  }

                                                  @Override
                                                  public void afterTextChanged(Editable s) {
                                                  }
                                              }
        );
    }


    private void getPhoneContacts() {
        List<PersonEntity> list = new ArrayList();
        final String[] PHONES_PROJECTION = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        ContentResolver resolver = getContentResolver();
        try {
            // 获取手机联系人
            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    PHONES_PROJECTION, null, null, null);
            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {
                    // 得到手机号码
                    String phoneNumber = phoneCursor
                            .getString(1);
                    // 当手机号码为空的或者为空字段 跳过当前循环
                    if (TextUtils.isEmpty(phoneNumber))
                        continue;
                    // 得到联系人名称
                    String contactName = phoneCursor
                            .getString(0);
                    //创建实体类解析联系人
                    PersonEntity mContact = new PersonEntity(contactName,
                            phoneNumber);
                    list.add(mContact);  //添加到集合里
                }
                phoneCursor.close();  //关流
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        String strings[]=new String[list.size()];
        for(int i=0,j=list.size();i<j;i++){
            strings[i]=list.get(i).getName();

        }

        SourceDateList = filledData(strings);
        Collections.sort(mSortList, pinyinComparator);
        adapter.updateListView(mSortList);
        adapter.notifyDataSetChanged();
    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */


    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
        adapter.notifyDataSetChanged();
    }
}
