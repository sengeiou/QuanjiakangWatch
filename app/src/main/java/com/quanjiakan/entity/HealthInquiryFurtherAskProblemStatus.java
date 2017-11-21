package com.quanjiakan.entity;

/**
 * Created by Administrator on 2017/11/21.
 */

public enum HealthInquiryFurtherAskProblemStatus {
    NO_RESPONSE(0,"NO_RESPONSE"),
    RESPONSE(1,"RESPONSE");

    private int value;
    private String status;

    HealthInquiryFurtherAskProblemStatus(int i,String status) {
        this.value = i;
        this.status = status;
    }

    public int getValue() {
        return value;
    }

    public String getStatus() {
        return status;
    }
}
