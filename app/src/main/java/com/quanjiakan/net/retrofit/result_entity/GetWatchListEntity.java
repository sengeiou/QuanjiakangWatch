package com.quanjiakan.net.retrofit.result_entity;

import com.quanjiakan.activity.base.ICommonData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by Administrator on 2017/10/19.
 */

public class GetWatchListEntity {

    /**
     * code : 200
     * list : [{"image":"http://picture.quanjiakan.com:9080/quanjiakan/resources/device/20171010095800_f5fpylvay3wreoov2h21.png","imei":"352315052834187","location":"113.2412882,23.1322843","locationTime":"2017-10-18 15:52:55.0","name":"%E5%88%9A%E7%BB%93%E5%A9%9A","online":0,"phone":"","relationlist":[{"image":"http://picture.quanjiakan.com:9080/quanjiakan/resources/device/20170824161547_jcq1v5eeyv21vdubej71.png","name":"%e7%9d%a1%e7%9c%a0%e7%9b%91%e6%b5%8b%e4%bb%aa","relation":"866104026684665","type":3}],"type":0},{"image":"http://picture.quanjiakan.com:9080/quanjiakan/resources/device/20171012215718_7so22trwnek7mbgso2s1.png","imei":"355637052788452","location":"113.2411955,23.1322491","locationTime":"2017-10-14 15:34:41.0","name":"%E8%83%A1","online":0,"phone":"18620155897","relationlist":[],"type":0},{"image":"http://picture.quanjiakan.com:9080/quanjiakan/resources/device/20171014142816_4d0941glzgvchsocf3ri.png","imei":"355637053077731","location":"113.2704899,22.9896351","locationTime":"2017-10-18 21:02:47.0","name":"%E9%9B%85%E4%B8%BD","online":1,"phone":"18620155897","relationlist":[],"type":0}]
     * message : 返回成功
     * rows : 3
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

    public static class ListBean {
        /**
         * image : http://picture.quanjiakan.com:9080/quanjiakan/resources/device/20171010095800_f5fpylvay3wreoov2h21.png
         * imei : 352315052834187
         * location : 113.2412882,23.1322843
         * locationTime : 2017-10-18 15:52:55.0
         * name : %E5%88%9A%E7%BB%93%E5%A9%9A
         * online : 0
         * phone :
         * relationlist : [{"image":"http://picture.quanjiakan.com:9080/quanjiakan/resources/device/20170824161547_jcq1v5eeyv21vdubej71.png","name":"%e7%9d%a1%e7%9c%a0%e7%9b%91%e6%b5%8b%e4%bb%aa","relation":"866104026684665","type":3}]
         * type : 0
         */

        private String image;
        private String imei;
        private String location;
        private String locationTime;
        private String name;
        private int online;
        private String phone;
        private int type;
        private List<RelationlistBean> relationlist;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLocationTime() {
            return locationTime;
        }

        public void setLocationTime(String locationTime) {
            this.locationTime = locationTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFormatName() throws UnsupportedEncodingException {
            if(name!=null && name.contains("%")){
                return URLDecoder.decode(name, ICommonData.ENCODE_DECODE_UTF_8);
            }else{
                return name;
            }
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<RelationlistBean> getRelationlist() {
            return relationlist;
        }

        public JSONArray getRelationListToJSONArray() throws JSONException {
            JSONArray array = new JSONArray();
            for(RelationlistBean bean : relationlist){
                JSONObject object = new JSONObject();
                object.put("image",bean.getImage());
                object.put("name",bean.getName());
                object.put("relation",bean.getRelation());
                object.put("type",bean.getType());
                array.put(object);
            }
            return array;
        }

        public void setRelationlist(List<RelationlistBean> relationlist) {
            this.relationlist = relationlist;
        }

        public static class RelationlistBean {
            /**
             * image : http://picture.quanjiakan.com:9080/quanjiakan/resources/device/20170824161547_jcq1v5eeyv21vdubej71.png
             * name : %e7%9d%a1%e7%9c%a0%e7%9b%91%e6%b5%8b%e4%bb%aa
             * relation : 866104026684665
             * type : 3
             */

            private String image;
            private String name;
            private String relation;
            private int type;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getRelation() {
                return relation;
            }

            public void setRelation(String relation) {
                this.relation = relation;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            @Override
            public String toString() {
                return "RelationlistBean{" +
                        "image='" + image + '\'' +
                        ", name='" + name + '\'' +
                        ", relation='" + relation + '\'' +
                        ", type=" + type +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "image='" + image + '\'' +
                    ", imei='" + imei + '\'' +
                    ", location='" + location + '\'' +
                    ", locationTime='" + locationTime + '\'' +
                    ", name='" + name + '\'' +
                    ", online=" + online +
                    ", phone='" + phone + '\'' +
                    ", type=" + type +
                    ", relationlist=" + relationlist.toString() +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetWatchListEntity{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", rows=" + rows +
                ", list=" + list.toString() +
                '}';
    }
}
