package com.quanjiakan.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/11/2.
 */
@Entity
public class MircoChatUnreadNumberRecordEntity {
    @Id
    private Long id;

    @NotNull
    private String belongDeviceIMEI;//TODO 属于哪个设备的语音未读（可理解为群组）

    @NotNull
    private Integer unreadNumber;//TODO 未读数量

    @NotNull
    private String belongUserID;//

    public String getBelongUserID() {
        return this.belongUserID;
    }

    public void setBelongUserID(String belongUserID) {
        this.belongUserID = belongUserID;
    }

    public Integer getUnreadNumber() {
        return this.unreadNumber;
    }

    public void setUnreadNumber(Integer unreadNumber) {
        this.unreadNumber = unreadNumber;
    }

    public String getBelongDeviceIMEI() {
        return this.belongDeviceIMEI;
    }

    public void setBelongDeviceIMEI(String belongDeviceIMEI) {
        this.belongDeviceIMEI = belongDeviceIMEI;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1409463915)
    public MircoChatUnreadNumberRecordEntity(Long id,
            @NotNull String belongDeviceIMEI, @NotNull Integer unreadNumber,
            @NotNull String belongUserID) {
        this.id = id;
        this.belongDeviceIMEI = belongDeviceIMEI;
        this.unreadNumber = unreadNumber;
        this.belongUserID = belongUserID;
    }

    @Generated(hash = 1006844272)
    public MircoChatUnreadNumberRecordEntity() {
    }
}
