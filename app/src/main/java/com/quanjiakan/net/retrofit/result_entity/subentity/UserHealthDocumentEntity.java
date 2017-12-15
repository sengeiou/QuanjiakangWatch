package com.quanjiakan.net.retrofit.result_entity.subentity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/13.
 */

public class UserHealthDocumentEntity implements Serializable{

    /**
     * createtime : 2017-12-12 17:55:08.0
     * deviceid : 0
     * id : 382
     * medicalName : ceshi
     * medicalRecord : http://picture.quanjiakan.com/quanjiakan/resources/medical/20171212175501_gy1onqv4pyzdzjcck3ie.png
     */

    private String createtime;
    private int deviceid;
    private int id;
    private String medicalName;
    private String medicalRecord;

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreatetimeFormat() {
        if(createtime!=null && createtime.length()>19){
            return createtime.substring(0,19);
        }
        return createtime;
    }

    public int getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(int deviceid) {
        this.deviceid = deviceid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedicalName() {
        return medicalName;
    }

    public void setMedicalName(String medicalName) {
        this.medicalName = medicalName;
    }

    public String getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(String medicalRecord) {
        this.medicalRecord = medicalRecord;
    }
}
