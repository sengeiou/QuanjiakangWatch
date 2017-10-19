package com.quanjiakan.net;

/**
 * Created by Administrator on 2017/9/6.
 */

public interface IHttpUrlConstants {
    /**
     *
     */
    public static final String BASEURL_QUANJIAKANG = "http://app.quanjiakan.com";//TODO Work
    public static final String BASEURL_APP = "http://app.quanjiakan.com";//TODO Work
    public static final String BASEURL_WATCH = "http://watch.quanjiakan.com";//TODO Work
    public static final String BASEURL_DEVICE = "http://device.quanjiakan.com";//TODO Work
    public static final String BASEURL_PAY = "http://pay.quanjiakan.com:7080";

    //**********************************************************
    /**
     * 请求方式
     * GET api
     * POST 版本1 v1
     */
    public static final String REQUEST_GET = "api";
    public static final String REQUEST_POST_V1 = "v1";


    //**********************************************************
    /**
     * 平安通：pingan
     * 警务联动：jinwu
     */
    public static final String PROJECT_PINGAN = "pingan";
    public static final String PROJECT_DEVICES = "devices";
    //***************************************************
    /**
     * 1.Web,
     * 2.Android
     * 3.iOS
     */
//    public static final String PLATFORM_WEB = "1";
    public static final String PLATFORM_ANDROID = "2";
//    public static final String PLATFORM_IOS = "3";
    //***************************************************
    /**
     * Service
     */
    public static final String SERVICE_SMS = "sms";
    //***************************************************
    /**
     * action
     */
    public static final String ACTION_VAILDATE = "validatecode";


    //***************************************************
    /**
     * SMS 类型区分---参数常量
     */
    public static final String SMS_TYPE_SIGNIN = "1";
    public static final String SMS_TYPE_FORGET_PW = "2";
}
