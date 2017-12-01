package com.quanjiakan.net.retrofit.service.get.string;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.

 http://device.quanjiakan.com/devices/api?
 handler=watch&action=selcontracts
 &platform=2
 &token=123
 &imei=12345678

 */

public interface RxGetDeviceContactInfoService {

    @GET("/"+IHttpUrlConstants.PROJECT_DEVICES+"/"+IHttpUrlConstants.REQUEST_GET+"?handler=watch&action=selcontracts")
    rx.Observable<String> doGetContactInfo(@Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                                      @Query(IParamsName.PARAMS_COMMON_TOKEN) String token,
                                                      @Query(IParamsName.PARAMS_COMMON_IMEI) String imei
    );
}
