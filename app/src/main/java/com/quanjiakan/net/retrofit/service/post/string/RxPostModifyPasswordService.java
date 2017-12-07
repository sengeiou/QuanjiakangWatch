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
 http://app.quanjiakan.com + "/pingan/v1?handler=member&action=modifypwd&platform=2"

 params.put("memberId", BaseApplication.getInstances().getUser_id());
 params.put("password", BaseApplication.getInstances().getFormatPWString(et_password.getText().toString()));
 params.put("platform", "2");
 params.put("token", BaseApplication.getInstances().getSessionID());


 */

public interface RxPostModifyPasswordService {
    @FormUrlEncoded
    @POST("/"+ IHttpUrlConstants.PROJECT_PINGAN+"/"+IHttpUrlConstants.REQUEST_POST_V1+"?handler=member&action=modifypwd")
    rx.Observable<String> modifyPassword(@Field(IParamsName.PARAMS_COMMON_PASSWORD) String password,
                                         @Field(IParamsName.PARAMS_COMMON_MEMBERID) String memberId,
                                         @Field(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                         @Field(IParamsName.PARAMS_COMMON_TOKEN) String token);
}
