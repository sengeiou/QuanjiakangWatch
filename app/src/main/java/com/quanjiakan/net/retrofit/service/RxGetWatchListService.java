package com.quanjiakan.net.retrofit.service;

import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.GetWatchListEntity;

import retrofit2.http.FormUrlEncoded;
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

public interface RxGetWatchListService {

    @GET("/"+IHttpUrlConstants.PROJECT_DEVICES+"/"+ IHttpUrlConstants.REQUEST_GET+"?handler=watch&action=watchlist")
    rx.Observable<GetWatchListEntity> doGetWatchList(@Query("memberId") String memberId,
                                                     @Query("platform") String platform,
                                                     @Query("token") String token);
}
