package com.quanjiakan.net.retrofit.service;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 *
 http://app.quanjiakan.com/familycare-pay/core/api_get?devicetype=0&platform=2&client=1
 "&code=pay&action=housekeeper_publishorder"

 "&token="+ BaseApplication.getInstances().getSessionID() +
 "&user_id="+QuanjiakanSetting.getInstance().getUserId() +
 "&data="+jsonString
 */

public interface RxGetOrderVerifyStringService {

    @GET("/"+IHttpUrlConstants.PROJECT_PAY+"/"+IHttpUrlConstants.REQUEST_MIDDLE_CORE+"/" + IHttpUrlConstants.REQUEST_GET_OLD+"?devicetype=0&platform=2&client=1&code=pay&action=housekeeper_publishorder")
    rx.Observable<String> doGetHouseKeeperOrderString(@Query(IParamsName.PARAMS_COMMON_TOKEN) String token,
                                                      @Query(IParamsName.PARAMS_COMMON_USERID) String userid,
                                                      @Query(IParamsName.PARAMS_COMMON_DATA) String page
    );
}
