package com.quanjiakan.net_presenter;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public interface IPresenterBusinessCode {
    /**
     * TODO 标识各种业务的操作
     */
    int EMPTY = 60000;
    int NONE = 30000;
    int LOGIN = 30001;
    int SMS_CODE = 30002;
    int SIGNUP = 30003;
    int LANGUAGE_SWITCH = 30004;
    int SIGNIN_AGREEMENT = 30005;
    int SIGNIN_CODE = 30006;
    int PASSWORD_RESET = 30007;

    int DEVICE_WATCH_LIST = 30100;


    int HOUSE_KEEPER_LIST = 30101;
    int HOUSE_KEEPER_LOCATE = 30102;
    int HOUSE_KEEPER_LIST_WITH_LOCATION = 30103;
    int HOUSE_KEEPER_TYPE_LIST = 30104;

    int HOUSE_KEEPER_TYPE_DETAIL = 30105;
    int HOUSE_KEEPER_TYPE_COMPANY = 30106;

    int HOUSE_KEEPER_ORDER = 30107;

    int HEALTH_INQUIRY_CREATE_PROBLEM = 30108;
    int HEALTH_INQUIRY_SEND_PROBLEM = 30109;

    int HEALTH_INQUIRY_APPEND_PROBLEM = 30110;
    int HEALTH_INQUIRY_PROBLEM_ANSWER_LIST = 30111;
    int HEALTH_INQUIRY_PROBLEM_SEND_QUESTION = 30112;
    int HEALTH_INQUIRY_DOCTOR_INFO = 30113;
    int HEALTH_INQUIRY_DOCTOR_EVALUATE = 30114;


    int COMMON_FILE_UPLOAD = 30301;
    int COMMON_FILE_DOWNLOAD = 30302;
    int COMMON_FILE_UPLOAD_VOICE = 30303;
    int COMMON_FILE_UPLOAD_IMAGE = 30304;

    //*******************************************************************************
    int ALI_PAY = 31100;
    int ALI_PAY_VERIFY_RESULT = 31101;
    int WECHAT_PAY = 31102;
    int WECHAT_PAY_VERIFY_RESULT = 31103;




}
