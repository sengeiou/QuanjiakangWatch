package com.quanjiakan.constants;

/**
 * Created by Administrator on 2017/10/12.
 */

public interface ICommonData {
    //TODO ************************************************
    //TODO 公共 String 类型数据

    String HTTP_OK = "200";

    String HTTP_PREFIX = "http";

    String ENCODE_DECODE_UTF_8 = "utf-8";
    String ENCODE_DECODE_UTF_8_BIG = "UTF-8";
    String ENCODE_DECODE_URL_PREFIX = "%";


    String FORBID_USER = "0";

    String COMMON_DIVIDER_SYMBOL = ",";
    String COMMON_UNREAD_VOICE_RECORD_SUFFIX = "_unread,";
    String COMMON_READ_VOICE_RECORD_SUFFIX = "_read,";


    String MAIN_TAB_ITEM_MAIN_TAG = "main_tag";
    String MAIN_TAB_ITEM_MSG_TAG = "msg_tag";
    String MAIN_TAB_ITEM_SETTING_TAG = "setting_tag";

    String DEVICE_TYPE_OLD = "0";
    String DEVICE_TYPE_CHILD = "1";


    String PICTURE_COMMON_PREFIX = "http";
    String ENCODE_ALLOW = "@#&=*+-_.,:!?()/~\'%";
    String HTTP_HEADER_LOCATION = "Location";
    String HTTP_HEADER_ENCODE_GZIP = "gzip";
    String EMPTY = "";
    String FILE_PATH_PREFIX1 = "file:///";
    String FILE_PATH_PREFIX2 = "file://";
    String FILE_PATH_PREFIX3 = "file:/";

    String ALI_PAY_RESULT_9000 = "9000";
    String ALI_PAY_RESULT_8000 = "8000";

    String HEALTH_INQUIRY_TYPE = "cy";

    String HEALTH_INQUIRY_SEND_TYPE = "type";
    String HEALTH_INQUIRY_SEND_TYPE_VALUE = "patient_meta";
    String HEALTH_INQUIRY_SEND_AGE = "age";
    String HEALTH_INQUIRY_SEND_GENDER = "sex";
    String HEALTH_INQUIRY_SEND_NAME = "name";

    //TODO ************************************************
    //TODO 公共 数值 类型数据

    int FORBID_USER_INT = 0;

    int MAIN_TAB_ITEM_NONE = 0;
    int MAIN_TAB_ITEM_MAIN = 1;
    int MAIN_TAB_ITEM_MSG = 2;
    int MAIN_TAB_ITEM_SETTING = 3;

    int VALID_IMEI_LENGTH = 15;



}
