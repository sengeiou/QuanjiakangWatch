package com.quanjiakan.net.retrofit.service.get.string;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.

 http://device.quanjiakan.com/
 devices/api?
 handler=watch&action=deviceinfo&
 imei=866333030025652&
 platform=2&
 token=0f2e12393c171091337ac6956cc4d1a1

 */

public interface RxGetDeviceInfoService {

    @GET("/"+IHttpUrlConstants.PROJECT_DEVICES+"/"+IHttpUrlConstants.REQUEST_GET+"?handler=watch&action=deviceinfo")
    rx.Observable<String> doGetDeviceInfo(@Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                           @Query(IParamsName.PARAMS_COMMON_TOKEN) String token,
                                           @Query(IParamsName.PARAMS_COMMON_IMEI) String imei
    );
}
