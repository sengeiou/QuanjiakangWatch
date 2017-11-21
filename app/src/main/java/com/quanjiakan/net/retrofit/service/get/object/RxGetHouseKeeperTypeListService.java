package com.quanjiakan.net.retrofit.service.get.object;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperTypeListEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 * http://app.quanjiakan.com/pingan/api?
 * handler=services&
 * action=housekeerserviceslist&
 * memberId=11303&
 * token=3169fe4e2e712790cb00c0ab9e8122a3
 */

public interface RxGetHouseKeeperTypeListService {

    @GET("/"+IHttpUrlConstants.PROJECT_PINGAN+"/"+ IHttpUrlConstants.REQUEST_GET+"?handler=services&action=housekeerserviceslist")
    rx.Observable<GetHouseKeeperTypeListEntity> doGetWatchListWithoutLocation(@Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                                                              @Query(IParamsName.PARAMS_COMMON_TOKEN) String token,
                                                                              @Query(IParamsName.PARAMS_COMMON_MEMBERID) String memberId
    );
}
