package com.quanjiakan.net.retrofit.result_entity;

/**
 * Created by Administrator on 2017/10/12.
 */

public class PostSignupEntity {

    /**
     * {"code":"200","message":"返回成功","object":{"memberId":13884}}
     *
     * code : 200
     * message : 返回成功
     * object : {"memberId":13884}
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
         * memberId : 13884
         */

        private int memberId;

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }
    }
}
