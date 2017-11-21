package com.quanjiakan.net.retrofit.service.post.string;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
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
    @POST("/"+ IHttpUrlConstants.PROJECT_PINGAN+"/"+IHttpUrlConstants.REQUEST_POST_V1+"?handler=member&action=forgetpwd")
    rx.Observable<PostResetPasswordEntity> doLogin(@Field(IParamsName.PARAMS_COMMON_MOBILE) String mobile,
                                                   @Field(IParamsName.PARAMS_COMMON_PASSWORD) String password,
                                                   @Field(IParamsName.PARAMS_COMMON_VALIDATE_CODE) String c_password,
                                                   @Field(IParamsName.PARAMS_COMMON_PLATFORM) String client);
}
