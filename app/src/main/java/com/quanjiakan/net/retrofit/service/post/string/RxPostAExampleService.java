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
 http://app.quanjiakan.com + /pingan/v1?handler=jugui&action=feedback

 params.put("memberId", QuanjiakanSetting.getInstance().getUserId()+"");
 params.put("mail", et_mail.getText().toString());
 params.put("content", et_content.getText().toString());
 params.put("platform", "2");
 params.put("token", BaseApplication.getInstances().getSessionID());


 */

public interface RxPostAExampleService {
    @FormUrlEncoded
    @POST("/"+ IHttpUrlConstants.PROJECT_PINGAN+"/"+IHttpUrlConstants.REQUEST_POST_V1+"?handler=jugui&action=feedback")
    rx.Observable<String> feedback(@Field(IParamsName.PARAMS_COMMON_MEMBERID) String memberId,
                                   @Field(IParamsName.PARAMS_FEEDBACK_EMAIL) String email,
                                   @Field(IParamsName.PARAMS_FEEDBACK_CONTENT) String content,
                                   @Field(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                   @Field(IParamsName.PARAMS_COMMON_TOKEN) String token);
}
