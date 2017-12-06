package com.quanjiakan.net.retrofit.result_entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/4.
 */

public class GetUserInfoEntity implements Serializable{

    /**
     * code : 200
     * message : 返回成功
     * object : {"nickname":"小小莫","picture":"http://picture.quanjiakan.com/quanjiakan/resources/doctor/20170110095209_invzijlm0sir8ggr931c.png"}
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
         * nickname : 小小莫
         * picture : http://picture.quanjiakan.com/quanjiakan/resources/doctor/20170110095209_invzijlm0sir8ggr931c.png
         */

        private String nickname;
        private String picture;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
    }
}
