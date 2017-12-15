package com.quanjiakan.net.retrofit.service.get.string;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 *
 http://pay.quanjiakan.com/payment/api?handler=hkpayment&action=produceorder&
 ciphertext=fVzrMGlMpmPNjr6Vh6CcR4QWved/p7S3U3JtlovHJss6luDnQ89wH/ah0I9ZMMg5Ya262GGIzu3qUBzatYSad6%2B5AtH6V4flpOZTqob2tDm/SaSNNH4YwLUR8Bvi/K82zuv4L/jQH8STnLUyqJM/XmticqXs/0vuJ3yX%2BY1z7ac=&
 memberId=11184&
 note=&
 begindate=2016-01-01&
 enddate=2016-02-02&
 companyId=1&
 address=&
 orderUserName=PPPP&
 platform=2&
 token=123123123


 */

public interface RxGetHouseKeeperOrderEncryptService {
    @GET("/"+ IHttpUrlConstants.PROJECT_PINGAN+"/"+IHttpUrlConstants.REQUEST_POST_V1+"?handler=hkpayment&action=produceorder")
    rx.Observable<String> getHouseKeeperOrderEncrypt(@Query(IParamsName.PARAMS_COMMON_MEMBERID) String memberId,
                                   @Query(IParamsName.PARAMS_FEEDBACK_EMAIL) String email,
                                   @Query(IParamsName.PARAMS_FEEDBACK_CONTENT) String content,
                                   @Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                   @Query(IParamsName.PARAMS_COMMON_TOKEN) String token);
}
