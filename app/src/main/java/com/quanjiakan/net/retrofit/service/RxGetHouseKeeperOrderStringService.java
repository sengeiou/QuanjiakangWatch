package com.quanjiakan.net.retrofit.service;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.


 http://pay.quanjiakan.com:7080
 /familycore-pay
 /core/api_get?
 devicetype=0&
 client=1&
 code=pay&
 action=housekeeper_produceorder&

 platform=2&
 token=849f2bbb87dc5c049f20b85be3047e3b&
 user_id=11303&
 data={"note":"","housekeeperId":38,"companyId":7,"begindate":"2017-11-10","enddate":"2017-11-10","orderUserName":"发古板","mobile":"13212345678","address":"复活币","orderUserId":11303,"userId":11303,"paymentChannel":1}

 */

public interface RxGetHouseKeeperOrderStringService {

    @GET("/"+IHttpUrlConstants.PROJECT_PAY+"/"+IHttpUrlConstants.REQUEST_MIDDLE_CORE+"/" + IHttpUrlConstants.REQUEST_GET_OLD+"?devicetype=0&client=1&code=pay&action=housekeeper_produceorder")
    rx.Observable<String> doGetHouseKeeperOrderString(@Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                                        @Query(IParamsName.PARAMS_COMMON_TOKEN) String token,
                                                        @Query(IParamsName.PARAMS_COMMON_USERID) String userid,
                                                        @Query(IParamsName.PARAMS_COMMON_DATA) String page
    );
}
