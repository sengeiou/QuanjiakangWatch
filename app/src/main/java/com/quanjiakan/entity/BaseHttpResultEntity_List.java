package com.quanjiakan.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29 0029.
 */
public class BaseHttpResultEntity_List<T> implements Serializable {
    private String code;
    private List<T> list;
    private int total;
    private int rows;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
