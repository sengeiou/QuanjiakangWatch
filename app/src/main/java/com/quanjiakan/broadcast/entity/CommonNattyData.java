package com.quanjiakan.broadcast.entity;

/**
 * Created by Administrator on 2017/10/17.
 */

public class CommonNattyData {
    private int type;
    private String data;

    public CommonNattyData(int type, String data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
