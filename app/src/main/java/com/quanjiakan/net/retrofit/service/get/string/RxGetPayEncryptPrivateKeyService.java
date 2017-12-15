package com.quanjiakan.net.retrofit.service.get.string;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 http://pay.quanjiakan.com
 /familycare-payment/
 api?handler=rsa&action=androidPublicKey&
 memberId=11184&
 platform=1&
 token=121
 */

public interface RxGetPayEncryptPrivateKeyService {
    @GET("/"+IHttpUrlConstants.PROJECT_PAYMENT+"/"+ IHttpUrlConstants.REQUEST_GET+"?handler=rsa&action=androidPublicKey")
    rx.Observable<String> doGetPrivateKey(@Query(IParamsName.PARAMS_COMMON_MEMBERID) String memberId,
                                          @Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                          @Query(IParamsName.PARAMS_COMMON_TOKEN) String token);
}
