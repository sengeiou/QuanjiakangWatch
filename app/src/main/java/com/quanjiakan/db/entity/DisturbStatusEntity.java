package com.quanjiakan.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/12/5.
 */
@Entity
public class DisturbStatusEntity {
    @Id
    private Long id;
    @NotNull
    private int disturbStatus;//TODO 规定0为不开启，非0为开启
    @NotNull
    private String belongUserId;

    public int getDisturbStatus() {
        return this.disturbStatus;
    }
    public void setDisturbStatus(int disturbStatus) {
        this.disturbStatus = disturbStatus;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBelongUserId() {
        return this.belongUserId;
    }
    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
    }
    @Generated(hash = 921023409)
    public DisturbStatusEntity(Long id, int disturbStatus,
            @NotNull String belongUserId) {
        this.id = id;
        this.disturbStatus = disturbStatus;
        this.belongUserId = belongUserId;
    }
    @Generated(hash = 1842169655)
    public DisturbStatusEntity() {
    }
}
