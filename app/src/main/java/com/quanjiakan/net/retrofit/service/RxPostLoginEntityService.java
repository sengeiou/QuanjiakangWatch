package com.quanjiakan.net.retrofit.service;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.PostLoginEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public interface RxPostLoginEntityService {
    //TODO  get请求为api   post为v1
    @FormUrlEncoded
    @POST("/"+ IHttpUrlConstants.PROJECT_PINGAN+"/"+IHttpUrlConstants.REQUEST_POST_V1+"?handler=member&action=signin")
    rx.Observable<PostLoginEntity> doLogin(@Field(IParamsName.PARAMS_COMMON_PASSWORD) String password,
                                           @Field(IParamsName.PARAMS_COMMON_PLATFORM) String clientType,
                                           @Field(IParamsName.PARAMS_COMMON_MOBILE) String mobilephone);
}
