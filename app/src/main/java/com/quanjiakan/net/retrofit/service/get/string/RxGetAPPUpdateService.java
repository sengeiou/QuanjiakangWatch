package com.quanjiakan.net.retrofit.service.get.string;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 http://app.quanjiakan.com
 /pingan/api?handler=jugui&action=getversion&alias=pingan_android_version" + "&token="+ BaseApplication.getInstances().getSessionID();
 */

public interface RxGetAPPUpdateService {

    @GET("/"+IHttpUrlConstants.PROJECT_PINGAN+"/"+ IHttpUrlConstants.REQUEST_GET+"?handler=jugui&action=getversion&alias=pingan_android_version")
    rx.Observable<String> doGetUpdateInfo(@Query(IParamsName.PARAMS_COMMON_TOKEN) String token);
}
