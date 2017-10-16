package com.quanjiakan.net.retrofit.result_entity;

/**
 * Created by Administrator on 2017/10/16.
 */

public class PostResetPasswordEntity {

    /**
     * code : 200
     * message : 返回成功
     */

    private String code;
    private String message;

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
}
