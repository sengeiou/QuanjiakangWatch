package com.quanjiakan.activity.base;

/**
 * Created by Administrator on 2017/11/14.
 */

public enum WXPayResult {
    SUCCESS(1,"SUCCESS"),
    FAILURE(2,"FAILURE"),
    USER_CANCEL(3,"FAILURE");

    private int resultCode;
    private String resultString;

    WXPayResult(int resultCode, String resultString) {
        this.resultCode = resultCode;
        this.resultString = resultString;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getResultString() {
        return resultString;
    }
}
