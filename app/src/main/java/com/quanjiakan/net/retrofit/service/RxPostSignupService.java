package com.quanjiakan.net.retrofit.service;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 * http://app.quanjiakan.com/familycare/api?devicetype=0&platform=2
 */

public interface RxPostSignupService {

    @FormUrlEncoded
    @POST("/familycare/api?devicetype=0&platform=2&m=member&a=signup")
    rx.Observable<String> doLogin(@Field("mobile") String mobile,
                                  @Field("password") String password,
                                  @Field("c_password") String c_password,
                                  @Field("nickname") String nickname,
                                  @Field("client") String client);
}
