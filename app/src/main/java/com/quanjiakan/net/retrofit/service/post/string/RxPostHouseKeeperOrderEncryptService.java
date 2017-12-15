package com.quanjiakan.net.retrofit.service.post.string;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 *
 http://pay.quanjiakan.com/payment/api?handler=hkpayment&action=produceorder&

 memberId=11184&
 platform=2&
 token=123123123


 note=&
 begindate=2016-01-01&
 enddate=2016-02-02&
 companyId=1&
 address=&
 orderUserName=PPPP&
 ciphertext=fVzrMGlMpmPNjr6Vh6CcR4QWved/p7S3U3JtlovHJss6luDnQ89wH/ah0I9ZMMg5Ya262GGIzu3qUBzatYSad6%2B5AtH6V4flpOZTqob2tDm/SaSNNH4YwLUR8Bvi/K82zuv4L/jQH8STnLUyqJM/XmticqXs/0vuJ3yX%2BY1z7ac=&


 */

public interface RxPostHouseKeeperOrderEncryptService {
    @FormUrlEncoded
    @POST("/"+ IHttpUrlConstants.PROJECT_PAYMENT+"/"+IHttpUrlConstants.REQUEST_POST_V1+"?handler=hkpayment&action=produceorder")
    rx.Observable<String> getHouseKeeperOrderEncrypt(@Field(IParamsName.PARAMS_COMMON_MEMBERID) String memberId,
                                                     @Field(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                                     @Field(IParamsName.PARAMS_COMMON_TOKEN) String token,

                                                     @Field(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_NOTE) String note,
                                                     @Field(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_BEGINDATE) String beginDate,
                                                     @Field(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_ENDDATE) String endDate,
                                                     @Field(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_COMPANYID) String companyId,
                                                     @Field(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_ADDRESS) String address,
                                                     @Field(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_ORDER_USERNAME) String orderUserName,
                                                     @Field(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_CIPHERTEXT) String cipherText


    );
}
