package com.quanjiakan.net;

/**
 * Created by Administrator on 2017/10/12.
 */

public interface IResponseResultCode {
    /**
     * ************************************************
     * 网络请求返回的 Code （由一次访问成功后，服务器给出）
     */
    int RESPONSE_SUCCESS_INT = 200;
    int RESPONSE_EMPTY_DATA_INT = 201;

    /**
     * ************************************************
     * 网络请求返回的 Code （由一次访问成功后，服务器给出）
     */

    String RESPONSE_SUCCESS = "200";
    String RESPONSE_EMPTY_DATA = "201";
}
