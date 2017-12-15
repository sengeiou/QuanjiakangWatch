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
 http://pay.quanjiakan.com/payment/v1?
 handler=hkpayment&action=publishorder&

 memberId=10831&
 orderid=QJKKEEPER20171213094128782311&
 platform=3&
 token=123123123


 */

public interface RxPostOrderVerifyStringService {
    @FormUrlEncoded
    @POST("/"+ IHttpUrlConstants.PROJECT_PAYMENT+"/"+IHttpUrlConstants.REQUEST_POST_V1+"?handler=hkpayment&action=publishorder")
    rx.Observable<String> doGetHouseKeeperOrderString(@Field(IParamsName.PARAMS_COMMON_MEMBERID) String memberId,
                                   @Field(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_ORDERID) String orderid,
                                   @Field(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                   @Field(IParamsName.PARAMS_COMMON_TOKEN) String token);
}
