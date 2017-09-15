package com.quanjiakan.net.retrofit.service;

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
    @POST("/pingan/v1?handler=member&action=signin")
    rx.Observable<PostLoginEntity> doLogin(@Field("password") String password,
                                           @Field("platform") String clientType,
                                           @Field("mobile") String mobilephone);
}
