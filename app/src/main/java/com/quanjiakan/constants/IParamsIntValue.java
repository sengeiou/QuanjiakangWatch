package com.quanjiakan.constants;

/**
 * 保存某些固定的整型参数值
 * Created by Administrator on 2017/9/6.
 */

public interface IParamsIntValue {
    //TODO 公共整型值
    int COMMON_INVALIDED_VALUE = -1;
    int COMMON_INIT_VALUE = 0;

    int COMMON_SMS_SIGNIN_TYPE = 1;
    int COMMON_SMS_RESET_PASSWORD_TYPE = 2;

    //TODO MainActivity OnNewIntent类型
    int COMMON_MAIN_TYPE_SHOW_SPECIFIC_POINT = 1;
    int COMMON_MAIN_TYPE_SHOW_WARN_SOS = 2;
    int COMMON_MAIN_TYPE_SHOW_WARN_BOUND_OUT = 3;
    int COMMON_MAIN_TYPE_SHOW_WARN_BOUND_IN = 4;
    int COMMON_MAIN_TYPE_SHOW_WARN_UNWEAR = 5;
    int COMMON_MAIN_TYPE_SHOW_WARN_FALLDOWN = 6;
    int COMMON_MAIN_TYPE_SHOW_WARN_BIND = 7;
    int COMMON_MAIN_TYPE_SHOW_CHECK_PERMISSION = 8;
    int COMMON_MAIN_TYPE_SHOW_MATTERSS_WARN = 9;


    int DEVICE_TYPE_0_OLD = 0;
    int DEVICE_TYPE_1_CHILD = 1;
    int DEVICE_TYPE_2_LOCATER = 2;
    int DEVICE_TYPE_3_SLEEP = 3;
    int DEVICE_TYPE_4_STICK = 4;
    int DEVICE_TYPE_5_BREATH = 5;
    int DEVICE_TYPE_6_POSTURE = 6;
}
