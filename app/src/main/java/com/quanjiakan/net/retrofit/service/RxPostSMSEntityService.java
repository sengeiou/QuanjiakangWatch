package com.quanjiakan.net.retrofit.service;

import com.quanjiakan.net.retrofit.result_entity.PostSMSEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 * http://app.quanjiakan.com/familycare/health/api?code=sms&action=getsms
 */

public interface RxPostSMSEntityService {

    @FormUrlEncoded
    @POST("/pingan/v1?handler=member&action=validatecode")
    rx.Observable<PostSMSEntity> doGetSMSCode(@Field("mobile") String mobile ,
                                              @Field("validateType") String validateType,
                                              @Field("platform") String platform);
}
