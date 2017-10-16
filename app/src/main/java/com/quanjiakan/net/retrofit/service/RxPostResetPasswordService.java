package com.quanjiakan.net.retrofit.service;

import com.quanjiakan.net.retrofit.result_entity.PostResetPasswordEntity;
import com.quanjiakan.net.retrofit.result_entity.PostSignupEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 * http://app.quanjiakan.com/familycare/api?devicetype=0&platform=2
 */

public interface RxPostResetPasswordService {

    @FormUrlEncoded
    @POST("/pingan/v1?handler=member&action=forgetpwd")
    rx.Observable<PostResetPasswordEntity> doLogin(@Field("mobile") String mobile,
                                                   @Field("password") String password,
                                                   @Field("validateCode") String c_password,
                                                   @Field("platform") String client);
}
