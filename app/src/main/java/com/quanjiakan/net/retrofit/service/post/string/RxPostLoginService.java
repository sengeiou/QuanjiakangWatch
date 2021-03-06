package com.quanjiakan.net.retrofit.service.post.string;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public interface RxPostLoginService {
    @FormUrlEncoded
    @POST("/pingan/v1?handler=member&action=signin")
    rx.Observable<String> doLogin(@Field("password") String password,
                                        @Field("platform") String clientType,
                                        @Field("mobile") String mobilephone);
}
