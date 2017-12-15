package com.quanjiakan.net.retrofit.result_entity;

import com.quanjiakan.net.retrofit.result_entity.subentity.UserHealthDocumentEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/13.
 */

public class GetUserHealthDocumentEntity implements Serializable{

    /**
     * code : 200
     * list : [{"createtime":"2017-12-12 17:55:08.0","deviceid":0,"id":382,"medicalName":"ceshi","medicalRecord":"http://picture.quanjiakan.com/quanjiakan/resources/medical/20171212175501_gy1onqv4pyzdzjcck3ie.png"}]
     * message : 返回成功
     * rows : 1
     */

    private String code;
    private String message;
    private int rows;
    private List<UserHealthDocumentEntity> list;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public List<UserHealthDocumentEntity> getList() {
        return list;
    }

    public void setList(List<UserHealthDocumentEntity> list) {
        this.list = list;
    }

}
