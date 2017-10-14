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
public class LoginUserInfoEntity {
    //TODO **************************************************************************
    /**
     * 以下属性由用户编写，使用build后，对应的Dao将会自动生成，set/get方法也会自动生成
     */
    @Id
    private Long id;

    @Index(unique = true)
    private String loginPhone;//TODO 登录时使用的电话 (注解，设置为索引字段，并且唯一)

    @Unique
    private String userId; // TODO 设置数据字段唯一
    @NotNull
    private String passwordDigest;//TODO 密码的摘要信息，理论上是不会重复（映射后的位数会更多）
    @NotNull
    private String nickName;//TODO 注册时使用的 昵称
    @NotNull
    private String token;//TODO 登录时获得的Token

    @Transient
    private String tempPS;//TODO 临时数据，不会进行持久化

    //TODO **************************************************************************

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPasswordDigest() {
        return this.passwordDigest;
    }

    public void setPasswordDigest(String passwordDigest) {
        this.passwordDigest = passwordDigest;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginPhone() {
        return this.loginPhone;
    }

    public void setLoginPhone(String loginPhone) {
        this.loginPhone = loginPhone;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1155984786)
    public LoginUserInfoEntity(Long id, String loginPhone, String userId,
            @NotNull String passwordDigest, @NotNull String nickName, @NotNull String token) {
        this.id = id;
        this.loginPhone = loginPhone;
        this.userId = userId;
        this.passwordDigest = passwordDigest;
        this.nickName = nickName;
        this.token = token;
    }

    @Generated(hash = 432241)
    public LoginUserInfoEntity() {
    }
}
