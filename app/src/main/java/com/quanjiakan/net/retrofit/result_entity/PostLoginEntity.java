package com.quanjiakan.net.retrofit.result_entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/15.
 */

public class PostLoginEntity implements Serializable {

    /**
     * {"code":"200","message":"返回成功","object":{"id":13884,"nickname":"摩提","token":"bf8473c2b114defc27d48d6715428de6"}}
     *
     * code : 200
     * message : 返回成功
     * object : {"id":11303,"nickname":"小小莫","token":"89f53006959a0e10f340747e2a938c8b"}
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
         *
         * id : 11303
         * nickname : 小小莫
         * token : 89f53006959a0e10f340747e2a938c8b
         */

        private int id;
        private String nickname;
        private String token;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return "\nObjectBean{" +
                    "id=" + id +
                    ", nickname='" + nickname + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PostLoginEntity{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", object=" + object.toString() +
                '}';
    }
}
