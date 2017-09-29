package com.quanjiakan.net.retrofit.result_entity;

/**
 * Created by Administrator on 2017/9/22.
 */

public class PostSMSEntity {

    /**
     * code : 200
     * message : 返回成功
     * object : {"smscode":714148}
     */

    private String code;
    private String message;
    private ObjectBean object;

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

    public ObjectBean getObject() {
        return object;
    }

    public void setObject(ObjectBean object) {
        this.object = object;
    }

    public static class ObjectBean {
        /**
         * smscode : 714148
         */

        private int smscode;

        public int getSmscode() {
            return smscode;
        }

        public void setSmscode(int smscode) {
            this.smscode = smscode;
        }
    }
}
