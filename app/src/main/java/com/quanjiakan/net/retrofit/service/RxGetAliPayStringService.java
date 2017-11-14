package com.quanjiakan.net.retrofit.service;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.
 http://static.quanjiakan.com/familycare/api?devicetype=0&platform=2&client=1
 "&token="+ BaseApplication.getInstances().getSessionID() +
 "&user_id="+QuanjiakanSetting.getInstance().getUserId() +
 "&m=alipay&a=getpaidinfo"
 */

public interface RxGetAliPayStringService {

    @GET("/"+IHttpUrlConstants.PROJECT_FAMLIY_CARE+"/" + IHttpUrlConstants.REQUEST_GET+"?devicetype=0&platform=2&client=1&m=alipay&a=getpaidinfo")
    rx.Observable<String> doPostAliPayInfoString(@Query(IParamsName.PARAMS_COMMON_TOKEN) String platform,
                                                 @Query(IParamsName.PARAMS_COMMON_USERID) String page
    );
}
