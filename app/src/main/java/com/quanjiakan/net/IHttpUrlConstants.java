package com.quanjiakan.net;

/**
 * Created by Administrator on 2017/9/6.
 */

public interface IHttpUrlConstants {
    /**
     *
     */
    String BASEURL_QUANJIAKANG = "http://app.quanjiakan.com";//TODO Work
    String BASEURL_APP = "http://app.quanjiakan.com";//TODO Work
    String BASEURL_WATCH = "http://watch.quanjiakan.com";//TODO Work
    String BASEURL_DEVICE = "http://device.quanjiakan.com";//TODO Work
    String BASEURL_PAY = "http://pay.quanjiakan.com:7080";
    String BASEURL_STATIC = "http://static.quanjiakan.com";//TODO Work
    String BASEURL_PAY_NO_PORT = "http://pay.quanjiakan.com";
    //**********************************************************
    /**
     * 请求方式
     * GET api
     * POST 版本1 v1
     */
    String REQUEST_GET = "api";
    String REQUEST_GET_OLD = "api_get";
    String REQUEST_POST_V1 = "v1";

    //**********************************************************
    /**
     * 老版本中间分类
     */
    String REQUEST_MIDDLE_CORE = "core";

    //**********************************************************
    /**
     * 平安通：pingan
     * 警务联动：jinwu
     *
     *
     */
    String PROJECT_PINGAN = "pingan";
    String PROJECT_DEVICES = "devices";
    String PROJECT_PAY = "familycore-pay";
    String PROJECT_FAMLIY_CARE = "familycare";
    String PROJECT_PAYMENT = "payment";
    //***************************************************
    /**
     * 1.Web,
     * 2.Android
     * 3.iOS
     */
//      String PLATFORM_WEB = "1";
    String PLATFORM_ANDROID = "2";
//      String PLATFORM_IOS = "3";


    String CLIENT_ANDROID = "1";
    String DEVICE_TYPE_ANDROID = "0";
    //***************************************************
    /**
     * Service
     */
    String SERVICE_SMS = "sms";
    //***************************************************
    /**
     * action
     */
    String ACTION_VAILDATE = "validatecode";


    //***************************************************
    /**
     * SMS 类型区分---参数常量
     */
    String SMS_TYPE_SIGNIN = "1";
    String SMS_TYPE_FORGET_PW = "2";
}
