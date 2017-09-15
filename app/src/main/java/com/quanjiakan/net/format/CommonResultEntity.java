package com.quanjiakan.net.format;

import java.util.List;

/**
 * Created by Administrator on 2017/9/13.
 */

public class CommonResultEntity {

    /**
     * code : 200
     * message : 返回成功
     * object :
     * list : []
     */

    private String code;
    private String message;
    private String object;

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

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }


    @Override
    public String toString() {
        return "CommonResultEntity{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", object='" + object +
                '}';
    }
}
