package com.quanjiakan.net.retrofit.result_entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */

public class GetWatchListStatusEntity implements Serializable{

    /**
     * code : 200
     * list : [{"deviceType":0,"deviceid":240207489205306450,"imei":"355637052788452","onTime":"2017-11-15 16:01:02.0","online":"0"},{"deviceType":0,"deviceid":605227481984663699,"imei":"866333030000093","onTime":"2017-11-28 09:16:27.0","online":"0"}]
     * message : 返回成功
     * rows : 2
     */

    private String code;
    private String message;
    private int rows;
    private List<ListBean> list;

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

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean  implements Serializable{
        /**
         * deviceType : 0
         * deviceid : 240207489205306450
         * imei : 355637052788452
         * onTime : 2017-11-15 16:01:02.0
         * online : 0
         */

        private int deviceType;
        private long deviceid;
        private String imei;
        private String onTime;
        private String online;

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

        public String getOnTime() {
            return onTime;
        }

        public void setOnTime(String onTime) {
            this.onTime = onTime;
        }

        public String getOnline() {
            return online;
        }

        public void setOnline(String online) {
            this.online = online;
        }
    }
}
