package com.quanjiakan.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by Administrator on 2017/10/13.
 */

@Entity
public class BindWatchInfoEntity {
    //TODO **************************************************************************
    /**
     * 以下属性由用户编写，使用build后，对应的Dao将会自动生成，set/get方法也会自动生成
     */
    @Id
    private Long id;
    @NotNull
    private String imei;//TODO 设备IMEI号
    @NotNull
    private String headImage; // TODO 绑定设备的用户ID号
    @NotNull
    private String name; // TODO 绑定设备名
    @NotNull
    private String location;//TODO 定位
    @NotNull
    private String locationTime;//TODO 定位时间
    @NotNull
    private String phone;//TODO 手表电话号码
    @NotNull
    private String online;//TODO 在线状态
    @NotNull
    private String relationlist; //TODO 关联关系0 relationlist
    /**
     * type 可能的值
     0.老人手表
     1.儿童手表
     2.定位器
     3.睡眠监测仪
     4.拐杖
     5 呼吸监测仪
     6 体态监测仪
     */
    @NotNull
    private String type;//TODO 设备类型
    @NotNull
    private String alarmTime;//TODO 报警时间（标记收到SOS、跌倒、）
    @NotNull
    private Integer unreadMessageNumber;//TODO 未读消息数量
    @NotNull
    private String bindUserID;//TODO 绑定这个手表的用户的ID


    @Transient
    private String tempPS;//TODO 临时数据，不会进行持久化


    public String getAlarmTime() {
        return this.alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRelationlist() {
        return this.relationlist;
    }

    public void setRelationlist(String relationlist) {
        this.relationlist = relationlist;
    }

    public String getOnline() {
        return this.online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocationTime() {
        return this.locationTime;
    }

    public void setLocationTime(String locationTime) {
        this.locationTime = locationTime;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHeadImage() {
        return this.headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getImei() {
        return this.imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnreadMessageNumber(int unreadMessageNumber) {
        this.unreadMessageNumber = unreadMessageNumber;
    }

    public void setUnreadMessageNumber(Integer unreadMessageNumber) {
        this.unreadMessageNumber = unreadMessageNumber;
    }

    public Integer getUnreadMessageNumber() {
        return this.unreadMessageNumber;
    }

    public String getBindUserID() {
        return this.bindUserID;
    }

    public void setBindUserID(String bindUserID) {
        this.bindUserID = bindUserID;
    }

    @Generated(hash = 1546949030)
    public BindWatchInfoEntity(Long id, @NotNull String imei, @NotNull String headImage,
            @NotNull String name, @NotNull String location, @NotNull String locationTime,
            @NotNull String phone, @NotNull String online, @NotNull String relationlist,
            @NotNull String type, @NotNull String alarmTime,
            @NotNull Integer unreadMessageNumber, @NotNull String bindUserID) {
        this.id = id;
        this.imei = imei;
        this.headImage = headImage;
        this.name = name;
        this.location = location;
        this.locationTime = locationTime;
        this.phone = phone;
        this.online = online;
        this.relationlist = relationlist;
        this.type = type;
        this.alarmTime = alarmTime;
        this.unreadMessageNumber = unreadMessageNumber;
        this.bindUserID = bindUserID;
    }

    @Generated(hash = 91845110)
    public BindWatchInfoEntity() {
    }

}
