package com.quanjiakan.net.retrofit.service.get.string;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 http://app.quanjiakan.com/pingan/api?
 handler=archives&action=userarchives&
 rows=10&


 platform=2&
 token=b04be2446c7644b528f6dfda815a991e&
 memberId=11303&
 page=1

 */

public interface RxGetUserHealthDocumentListService {

    @GET("/"+IHttpUrlConstants.PROJECT_PINGAN+"/"+ IHttpUrlConstants.REQUEST_GET+"?handler=archives&action=userarchives&rows=10")
    rx.Observable<String> doGetUserHealthDocumentList(@Query(IParamsName.PARAMS_COMMON_MEMBERID) String memberId,
                                                      @Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                                      @Query(IParamsName.PARAMS_COMMON_TOKEN) String token,
                                                      @Query(IParamsName.PARAMS_COMMON_PAGE) String page);
}
