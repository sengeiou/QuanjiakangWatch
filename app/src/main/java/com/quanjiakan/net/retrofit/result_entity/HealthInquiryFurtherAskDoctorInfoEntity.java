package com.quanjiakan.net.retrofit.result_entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/20.
 */

public class HealthInquiryFurtherAskDoctorInfoEntity implements Serializable{

    /**
     * code : 200
     * message : 返回成功
     * object : {"chunyuId":"529198576","clinic":"口腔颌面科","doctorId":"clinic_web_444096a1ee022669","hospital":"哈尔滨医科大学附属第一医院","image":"http://media2.chunyuyisheng.com/@/media/images/2016/09/28/735b401109f4_w312_h312_.jpg?imageMogr2/thumbnail/150x","levelTitle":"三级甲等医院 医师","name":"苑芳连","title":"医师"}
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
         * chunyuId : 529198576
         * clinic : 口腔颌面科
         * doctorId : clinic_web_444096a1ee022669
         * hospital : 哈尔滨医科大学附属第一医院
         * image : http://media2.chunyuyisheng.com/@/media/images/2016/09/28/735b401109f4_w312_h312_.jpg?imageMogr2/thumbnail/150x
         * levelTitle : 三级甲等医院 医师
         * name : 苑芳连
         * title : 医师
         */

        private String chunyuId;
        private String clinic;
        private String doctorId;
        private String hospital;
        private String image;
        private String levelTitle;
        private String name;
        private String title;

        public String getChunyuId() {
            return chunyuId;
        }

        public void setChunyuId(String chunyuId) {
            this.chunyuId = chunyuId;
        }

        public String getClinic() {
            return clinic;
        }

        public void setClinic(String clinic) {
            this.clinic = clinic;
        }

        public String getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(String doctorId) {
            this.doctorId = doctorId;
        }

        public String getHospital() {
            return hospital;
        }

        public void setHospital(String hospital) {
            this.hospital = hospital;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLevelTitle() {
            return levelTitle;
        }

        public void setLevelTitle(String levelTitle) {
            this.levelTitle = levelTitle;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
