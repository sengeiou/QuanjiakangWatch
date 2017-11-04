package com.quanjiakan.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/11/1.
 */

@Entity
public class MircoChatRecordEntity {
    //TODO 语音微聊记录实体类
    @Id
    private Long id;

    private String unreadFlag; //TODO 未读的标识---所有该手机用户统一

    private Integer voiceLength;//TODO 语音记录的长度，单位为毫秒（最小有效值为1）,本地通过MediaPlayer获取到时间后，直接赋值

    @NotNull
    private String voiceRecordLocalPath;//TODO 记录这个文件的保存路径

    @NotNull
    private String fromUserID;//TODO 由哪个用户发出---可能是手表的IMEI，此时表示由手表发出的语音

    @NotNull
    private String belongDeviceIMEI;//TODO 属于哪个设备的语音（可理解为群组）

    @NotNull
    private Long receivedTimeStamp;//TODO 接收语音的时间戳

    @Transient
    private String showName;//TODO 需要展示的名字，但不同的用户，其实这个名字会不同，所以不能进行持久化存储

    public Long getReceivedTimeStamp() {
        return this.receivedTimeStamp;
    }

    public void setReceivedTimeStamp(Long receivedTimeStamp) {
        this.receivedTimeStamp = receivedTimeStamp;
    }

    public String getBelongDeviceIMEI() {
        return this.belongDeviceIMEI;
    }

    public void setBelongDeviceIMEI(String belongDeviceIMEI) {
        this.belongDeviceIMEI = belongDeviceIMEI;
    }

    public String getFromUserID() {
        return this.fromUserID;
    }

    public void setFromUserID(String fromUserID) {
        this.fromUserID = fromUserID;
    }

    public String getVoiceRecordLocalPath() {
        return this.voiceRecordLocalPath;
    }

    public void setVoiceRecordLocalPath(String voiceRecordLocalPath) {
        this.voiceRecordLocalPath = voiceRecordLocalPath;
    }

    public Integer getVoiceLength() {
        return this.voiceLength;
    }

    public void setVoiceLength(Integer voiceLength) {
        this.voiceLength = voiceLength;
    }

    public String getUnreadFlag() {
        return this.unreadFlag;
    }

    public void setUnreadFlag(String unreadFlag) {
        this.unreadFlag = unreadFlag;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1499520555)
    public MircoChatRecordEntity(Long id, String unreadFlag, Integer voiceLength,
            @NotNull String voiceRecordLocalPath, @NotNull String fromUserID,
            @NotNull String belongDeviceIMEI, @NotNull Long receivedTimeStamp) {
        this.id = id;
        this.unreadFlag = unreadFlag;
        this.voiceLength = voiceLength;
        this.voiceRecordLocalPath = voiceRecordLocalPath;
        this.fromUserID = fromUserID;
        this.belongDeviceIMEI = belongDeviceIMEI;
        this.receivedTimeStamp = receivedTimeStamp;
    }

    @Generated(hash = 1810873324)
    public MircoChatRecordEntity() {
    }

}
