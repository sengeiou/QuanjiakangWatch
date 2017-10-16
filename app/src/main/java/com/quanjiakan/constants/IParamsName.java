package com.quanjiakan.constants;

/**
 * 保存跳转间的参数名称
 * Created by Administrator on 2017/9/6.
 */

public interface IParamsName {
    //TODO 公共的参数名
    String PARAMS_COMMON_USER_ID = "user_id";
    String PARAMS_COMMON_USERNAME = "username";

    //TODO 首页-进入时需要进行的操作
    String PARAMS_MAIN_TYPE = "type";

    String PARAMS_LAT = "lat";
    String PARAMS_LON = "lon";
    String PARAMS_DEVICE_ID = "";

    //TODO 电子围栏-进入需要进行的操作的类型
    String PARAMS_FANCE_TYPE = "type";
}
