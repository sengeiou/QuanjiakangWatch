package com.quanjiakan.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/14 0014.
 */
public class VersionInfoEntity implements Serializable {

    /**
     * content : 1、优化了部分体验;2、修复了一个兼容性问题
     * forcedUpdate : 1
     * function : 平安通Android版本
     * url : http://download.quanjiakan.com/familycare-download/apk/pingantong.apk
     * version : v1.0.1.8
     * versionCode : 8
     */

    private String content;
    private int forcedUpdate;
    private String function;
    private String url;
    private String version;
    private int versionCode;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getForcedUpdate() {
        return forcedUpdate;
    }

    public void setForcedUpdate(int forcedUpdate) {
        this.forcedUpdate = forcedUpdate;
    }

    public boolean isForceUpdate(){
        return forcedUpdate==1;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}
