package com.mingrisoft.nearby.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者： LYJ
 * 功能： 返回数据的实体类
 * 创建日期： 2017/3/17
 */

public class Result implements Parcelable {
    private String address;//地址
    private String city;//城市
    private String name;//名称
    private String phoneNum;//电话

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeString(this.city);
        dest.writeString(this.name);
        dest.writeString(this.phoneNum);
    }

    public Result() {
    }

    protected Result(Parcel in) {
        this.address = in.readString();
        this.city = in.readString();
        this.name = in.readString();
        this.phoneNum = in.readString();
    }

    public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel source) {
            return new Result(source);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}
