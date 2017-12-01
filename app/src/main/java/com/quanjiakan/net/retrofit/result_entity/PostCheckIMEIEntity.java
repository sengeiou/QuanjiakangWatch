package com.quanjiakan.net.retrofit.result_entity;

import java.io.Serializable;

/**
 * Created by john on 2017-11-24.
 */

public class PostCheckIMEIEntity {

    /**
     * code : 200
     * message : 返回成功
     * object : {"acTime":"2017-04-25 13:58:11.0","actStatus":1,"deviceType":0,"deviceid":240207489205306450,"imei":"355637052788452"}
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

    public static class ObjectBean implements Serializable{
        /**
         * acTime : 2017-04-25 13:58:11.0
         * actStatus : 1
         * deviceType : 0
         * deviceid : 240207489205306450
         * imei : 355637052788452
         */

        private String acTime;
        private int actStatus;
        private int deviceType;
        private long deviceid;
        private String imei;

        public String getAcTime() {
            return acTime;
        }

        public void setAcTime(String acTime) {
            this.acTime = acTime;
        }

        public int getActStatus() {
            return actStatus;
        }

        public void setActStatus(int actStatus) {
            this.actStatus = actStatus;
        }

        public int getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(int deviceType) {
            this.deviceType = deviceType;
        }

        public long getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(long deviceid) {
            this.deviceid = deviceid;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }
    }
}
