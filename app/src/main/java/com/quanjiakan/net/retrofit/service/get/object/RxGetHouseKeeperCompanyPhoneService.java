package com.quanjiakan.net.retrofit.service.get.object;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperCompanyPhone;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperDetailEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 * http://app.quanjiakan.com/
 * pingan/api?
 * handler=jugui&
 * action=getfunction&
 * alias=customer_phone&
 * token=12
 */

public interface RxGetHouseKeeperCompanyPhoneService {

    @GET("/"+IHttpUrlConstants.PROJECT_PINGAN+"/"+ IHttpUrlConstants.REQUEST_GET+"?handler=jugui&action=getfunction")
    rx.Observable<GetHouseKeeperCompanyPhone> doGetHouseKeeperCompanyPhone(@Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                                                           @Query(IParamsName.PARAMS_COMMON_TOKEN) String token,
                                                                           @Query(IParamsName.PARAMS_HOUSE_KEEPER_DETAIL_ALIAS) String page
    );
}
