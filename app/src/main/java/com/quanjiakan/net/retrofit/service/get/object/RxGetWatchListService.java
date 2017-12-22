package com.quanjiakan.net.retrofit.service.get.object;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.GetWatchListEntity;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 *
 *
 http://device.quanjiakan.com+  //TODO 正式环境
 "/devices/api?handler=watch&action=devicestatus&memberId="+BaseApplication.getInstances().getUser_id()+
 "&platform=2&token="+BaseApplication.getInstances().getSessionID();
 */

public interface RxGetWatchListService {

    @GET("/"+IHttpUrlConstants.PROJECT_DEVICES+"/"+ IHttpUrlConstants.REQUEST_GET+"?handler=watch&action=devicestatus")
    rx.Observable<GetWatchListEntity> doGetWatchList(@Query(IParamsName.PARAMS_DEVICE_MEMBER_ID) String memberId,
                                                     @Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                                     @Query(IParamsName.PARAMS_COMMON_TOKEN) String token);
}
