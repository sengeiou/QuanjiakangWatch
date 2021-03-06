package com.quanjiakan.net.retrofit.service.get.string;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 * http://device.quanjiakan.com/devices/api?handler=watch&action=watchlist&
 * memberId=11303&
 * platform=2&
 * token=8eb7fb77af3d1f7a36eba4d1340af1e1
 */

public interface RxGetWatchListStatusService {

    @GET("/"+IHttpUrlConstants.PROJECT_DEVICES+"/"+ IHttpUrlConstants.REQUEST_GET+"?handler=watch&action=watchlist")
    rx.Observable<String> doGetWatchListStatus(@Query(IParamsName.PARAMS_DEVICE_MEMBER_ID) String memberId,
                                         @Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                         @Query(IParamsName.PARAMS_COMMON_TOKEN) String token);
}
