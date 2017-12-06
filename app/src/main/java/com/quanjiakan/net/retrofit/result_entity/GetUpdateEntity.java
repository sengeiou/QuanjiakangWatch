package com.quanjiakan.net.retrofit.result_entity;

import com.quanjiakan.entity.VersionInfoEntity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/4.
 */

public class GetUpdateEntity implements Serializable{

    /**
     * code : 200
     * message : 返回成功
     * object : {"content":"1、优化了部分体验;2、修复了一个兼容性问题","forcedUpdate":1,"function":"平安通Android版本","url":"http://download.quanjiakan.com/familycare-download/apk/pingantong.apk","version":"v1.0.1.8","versionCode":8}
     */

    private String code;
    private String message;
    private VersionInfoEntity object;

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

    public VersionInfoEntity getObject() {
        return object;
    }

    public void setObject(VersionInfoEntity object) {
        this.object = object;
    }

}
