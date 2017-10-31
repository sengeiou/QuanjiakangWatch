package com.quanjiakan.net_presenter;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public interface IPresenterBusinessCode {
    /**
     * TODO 标识各种业务的操作
     */
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
}
