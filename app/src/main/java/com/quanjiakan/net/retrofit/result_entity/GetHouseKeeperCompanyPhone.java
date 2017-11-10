package com.quanjiakan.net.retrofit.result_entity;

/**
 * Created by Administrator on 2017/11/10.
 */

public class GetHouseKeeperCompanyPhone {

    /**
     * code : 200
     * message : 返回成功
     * object : {"data":"02081772777","function":"售后电话"}
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
         * data : 02081772777
         * function : 售后电话
         */

        private String data;
        private String function;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getFunction() {
            return function;
        }

        public void setFunction(String function) {
            this.function = function;
        }
    }
}
