package com.quanjiakan.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/16.
 */
@Entity
public class HealthInquiryPatientDataEntity implements Serializable{
    @Id
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String gender;
    @NotNull
    private String age;
    @NotNull
    private String belongUserid;
    @Transient
    private String jsonString;//TODO 用于将用户数据直接转化为JSON对象

    public String getAge() {
        return this.age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBelongUserid() {
        return this.belongUserid;
    }
    public void setBelongUserid(String belongUserid) {
        this.belongUserid = belongUserid;
    }
    @Generated(hash = 237890926)
    public HealthInquiryPatientDataEntity(Long id, @NotNull String name,
            @NotNull String gender, @NotNull String age,
            @NotNull String belongUserid) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.belongUserid = belongUserid;
    }
    @Generated(hash = 1997543923)
    public HealthInquiryPatientDataEntity() {
    }

    @Override
    public boolean equals(Object other) {
        if(other!=null && other instanceof HealthInquiryPatientDataEntity){
            HealthInquiryPatientDataEntity temp = (HealthInquiryPatientDataEntity) other;
            if(age.equals(temp.getAge()) && gender.equals(temp.getGender()) &&
                    name.equals(temp.getName()) && belongUserid.equals(temp.getBelongUserid())){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public boolean equals(String name,String gender,String age,String belongUserid){
        if(this.age.equals(age) && this.gender.equals(gender) &&
                this.name.equals(name) && this.belongUserid.equals(belongUserid)){
            return true;
        }else{
            return false;
        }
    }
}
