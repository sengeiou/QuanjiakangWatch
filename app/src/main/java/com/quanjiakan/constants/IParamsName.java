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

    //TODO 图片预览
    String PARAMS_URL = "url";

    //***************************************************************************************************************************************


    //TODO 网络请求参数
    String PARAMS_COMMON_PLATFORM = "platform";
    String PARAMS_COMMON_TOKEN = "token";
    String PARAMS_COMMON_PAGE = "page";
    String PARAMS_COMMON_MEMBERID = "memberId";
    String PARAMS_COMMON_USERID = "user_id";
    String PARAMS_COMMON_DATA = "data";

    String PARAMS_COMMON_MOBILE = "mobile";
    String PARAMS_COMMON_PASSWORD = "password";

    String PARAMS_COMMON_VALIDATE_TYPE = "validateType";
    String PARAMS_COMMON_VALIDATE_CODE = "validateCode";

    String PARAMS_COMMON_NICKNAME = "nickname";

    //TODO 家政护理
    String PARAMS_HOUSE_KEEPER_PROVINCE = "serviceProvince";
    String PARAMS_HOUSE_KEEPER_CITY = "serviceCity";
    String PARAMS_HOUSE_KEEPER_DIST = "serviceDist";
    String PARAMS_HOUSE_KEEPER_TYPE = "housekeeperType";

    String PARAMS_HOUSE_KEEPER_DETAIL_ID = "housekeeperDetailID";
    String PARAMS_HOUSE_KEEPER_DETAIL_INFO = "housekeeperDetailInfo";

    String PARAMS_HOUSE_KEEPER_DETAIL_ALIAS = "housekeeperDetailAlias";


    String PARAMS_PAY_RESULT_TYPE = "pay_type";
    String PARAMS_PAY_RESULT_CODE = "pay_result_code";
    String PARAMS_PAY_MONEY = "pay_sum_money";
    String PARAMS_PAY_ORDERID = "pay_orderid";


    //TODO 手表相关
    String PARAMS_DEVICE_MEMBER_ID = "memberId";



    //TODO 健康咨询
    String PARAMS_HEALTH_INQURIRY_TYPE = "type";
    String PARAMS_HEALTH_INQURIRY_MEMBER_ID = "memberId";
    String PARAMS_HEALTH_INQURIRY_CONTENT = "content";
    String PARAMS_HEALTH_INQURIRY_FROMTOKEN = "fromtoken";
}
