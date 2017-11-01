package com.quanjiakan.device.entity;

/**
 * Created by Administrator on 2017/11/1.
 */

public class CommonBindResult {
    private int bindResultCode;

    public CommonBindResult(int bindResultCode) {
        this.bindResultCode = bindResultCode;
    }

    public int getBindResultCode() {
        return bindResultCode;
    }

    public void setBindResultCode(int bindResultCode) {
        this.bindResultCode = bindResultCode;
    }
}
