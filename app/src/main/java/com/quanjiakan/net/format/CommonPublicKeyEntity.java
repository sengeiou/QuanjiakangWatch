package com.quanjiakan.net.format;

/**
 * Created by Administrator on 2017/12/15.
 */

public class CommonPublicKeyEntity {

    /**
     * code : 200
     * message : 返回成功
     * object : {"PublicKey":"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdPgkr5keYjnoh0/RTHeS9EoksCqATkvpAmoTr2TrdqF6uq27oWz3m/1GIynFxMDWajjxS5h1VXphtCJhxOAggLi/TnZZdUydQgZddjAxdGDKGpyWNLKpE2hQfzRA+zbhiDA47cy2m65lewAQp11GWWXec67ocrb1uryxoe6YO/QIDAQAB"}
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
         * PublicKey : MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdPgkr5keYjnoh0/RTHeS9EoksCqATkvpAmoTr2TrdqF6uq27oWz3m/1GIynFxMDWajjxS5h1VXphtCJhxOAggLi/TnZZdUydQgZddjAxdGDKGpyWNLKpE2hQfzRA+zbhiDA47cy2m65lewAQp11GWWXec67ocrb1uryxoe6YO/QIDAQAB
         */

        private String PublicKey;

        public String getPublicKey() {
            return PublicKey;
        }

        public void setPublicKey(String PublicKey) {
            this.PublicKey = PublicKey;
        }
    }
}
