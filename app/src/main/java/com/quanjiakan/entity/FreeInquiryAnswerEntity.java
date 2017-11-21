package com.quanjiakan.entity;

import com.google.gson.JsonArray;
import com.quanjiakan.util.common.ParseToGsonUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/10 0010.
 */
public class FreeInquiryAnswerEntity implements Serializable {
    /**
     * {
     "id": "2",
     "price": "0",
     "doctor_id": "clinic_web_a3f76b1b6c3c50d7",
     "created_time_ms": "1473479795111",
     "patient": "{\"type\":\"patient_meta\",\"age\":\"30\",\"sex\":\"男\"}",

     "creator": "10691",
     "createtime": "2016-09-10 11:57:21.0",
     "chunyu_id": "412625718",
     "status": "1",
     "title": "你好！肚子疼了一天，吃不下饭",

     "user_id": "10691",
     "content": "[{\"text\":\"你好，肚子疼之前有吃了什么东西吗？\",\"type\":\"text\"}]",
     "fromtoken": "",
     "totoken": "10691",
     "problem_content_status": "1"
     }

     {
     "chunyuId": 594784542,
     "content": "[{"text":"你好，这种情况多久了了？","type":"text"}]",
     "createMillisecond": 1509347662988,
     "createtime": "2017-10-30 16:30:51.0",
     "creator": 13829,
     "doctorId": "clinic_web_994ce04bb90834b3",
     "fromtoken": "0",
     "id": 414,
     "isreply": 1,
     "patient": "{"type":"patient_meta","age":"28","sex":"男","name":"罗先生"}",
     "price": 0,
     "problemContentStatus": 1,
     "sponsor": 0,
     "status": 1,
     "title": "最近下肢无力，感觉有东西在下肢游动，但很快结束。",
     "totoken": "13829",
     "userId": 13829
     }

     */

    private String id;
    private String chunyuId;
    private String price;
    private String doctorId;
    private String createMillisecond;
    private String patient;

    private String creator;
    private String createtime;
    private String status;
    private String title;

    private String isreply;
    private String userId;
    private String sponsor;//0 医生  1 用户
    private String content;
    private String fromtoken;

    private String totoken;
    private String problemContentStatus;


    private String voiceTime;

    //

    public String getChunyuId() {
        return chunyuId;
    }

    public void setChunyuId(String chunyuId) {
        this.chunyuId = chunyuId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getCreateMillisecond() {
        return createMillisecond;
    }

    public void setCreateMillisecond(String createMillisecond) {
        this.createMillisecond = createMillisecond;
    }

    public String getIsreply() {
        return isreply;
    }

    public void setIsreply(String isreply) {
        this.isreply = isreply;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProblemContentStatus() {
        return problemContentStatus;
    }

    public void setProblemContentStatus(String problemContentStatus) {
        this.problemContentStatus = problemContentStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromtoken() {
        return fromtoken;
    }

    public void setFromtoken(String fromtoken) {
        this.fromtoken = fromtoken;
    }

    public String getTotoken() {
        return totoken;
    }

    public void setTotoken(String totoken) {
        this.totoken = totoken;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(String voiceTime) {
        this.voiceTime = voiceTime;
    }

    public JsonArray getContentString(){
        JsonArray jsonArray = new ParseToGsonUtil(content.replace("\\","")).getJsonArray();
        return jsonArray;
    }

}
