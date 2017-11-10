package com.quanjiakan.net.retrofit.service;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperDetailEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 * http://app.quanjiakan.com/pingan/api?
 * handler=services&
 * action=housekeeperdetail&
 * token=849f2bbb87dc5c049f20b85be3047e3b&
 * id=38
 */

public interface RxGetHouseKeeperDetailService {

    @GET("/"+IHttpUrlConstants.PROJECT_PINGAN+"/"+ IHttpUrlConstants.REQUEST_GET+"?handler=services&action=housekeeperdetail")
    rx.Observable<GetHouseKeeperDetailEntity> doGetHouseKeeperDetail(@Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                                                     @Query(IParamsName.PARAMS_COMMON_TOKEN) String token,
                                                                     @Query(IParamsName.PARAMS_HOUSE_KEEPER_DETAIL_ID) String page
    );
}
