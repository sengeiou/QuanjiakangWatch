package com.quanjiakan.net.retrofit.service;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 * http://app.quanjiakan.com/pingan/api?handler=services&action=housekeeperlist&
 * platform=2&
 * token=12fb866ee916cbe9b91d4c232ace1d90&
 * page=1&
 * serviceCity=%E5%B9%BF%E5%B7%9E&
 * serviceProvince=%E5%B9%BF%E4%B8%9C&
 * serviceDist=%E8%8D%94%E6%B9%BE%E5%8C%BA&rows=15
 *
 http://app.quanjiakan.com/pingan/api?handler=services&action=housekeeperlist&
 platform=2&
 token=86daeff8e1a9a345efbf667ab93fb94d&
 page=1&
 serviceCity=%E5%B9%BF%E5%B7%9E&
 serviceProvince=%E5%B9%BF%E4%B8%9C&
 serviceDist=%E8%8D%94%E6%B9%BE%E5%8C%BA
 &rows=15
 */

public interface RxGetHouseKeeperOrderStringService {

    @GET("/"+IHttpUrlConstants.PROJECT_PINGAN+"/"+ IHttpUrlConstants.REQUEST_GET+"?handler=services&action=housekeeperlist&rows=10")
    rx.Observable<String> doGetWatchListWithoutLocation(@Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                                        @Query(IParamsName.PARAMS_COMMON_TOKEN) String token,
                                                        @Query(IParamsName.PARAMS_COMMON_PAGE) String page
    );
}
