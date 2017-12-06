package com.quanjiakan.net.retrofit.service.get.string;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 * http://app.quanjiakan.com
 /pingan/api?handler=member&action=getmemberinfo&platform=2"+
 "&token="+ BaseApplication.getInstances().getSessionID() +
 "&memberId="+QuanjiakanSetting.getInstance().getUserId()
 */

public interface RxGetUserHeadIconAndNameService {

    @GET("/"+IHttpUrlConstants.PROJECT_PINGAN+"/"+ IHttpUrlConstants.REQUEST_GET+"?handler=member&action=getmemberinfo")
    rx.Observable<String> doGetHeadIconAndName(@Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                          @Query(IParamsName.PARAMS_COMMON_TOKEN) String token,
                                          @Query(IParamsName.PARAMS_COMMON_MEMBERID) String memberId);
}
