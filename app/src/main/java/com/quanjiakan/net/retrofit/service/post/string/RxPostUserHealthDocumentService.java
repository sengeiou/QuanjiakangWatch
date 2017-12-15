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
 pingan/v1?handler=archives&action=addarchives

 params.put(IParamsName.PARAMS_COMMON_MEMBERID,BaseApplication.getInstances().getLoginInfo().getUserId());
 params.put(IParamsName.PARAMS_USER_HEALTH_DOCUMENT_NAME,input.getText().toString());
 params.put(IParamsName.PARAMS_USER_HEALTH_DOCUMENT_IMAGE,image.getTag().toString());
 params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
 params.put(IParamsName.PARAMS_COMMON_TOKEN,BaseApplication.getInstances().getLoginInfo().getToken());


 */

public interface RxPostUserHealthDocumentService {
    @FormUrlEncoded
    @POST("/"+ IHttpUrlConstants.PROJECT_PINGAN+"/"+IHttpUrlConstants.REQUEST_POST_V1+"?handler=archives&action=addarchives")
    rx.Observable<String> submitUserHealthDocumentInfo(@Field(IParamsName.PARAMS_COMMON_MEMBERID) String memberId,
                                   @Field(IParamsName.PARAMS_USER_HEALTH_DOCUMENT_NAME) String name,
                                   @Field(IParamsName.PARAMS_USER_HEALTH_DOCUMENT_IMAGE) String image,
                                   @Field(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                   @Field(IParamsName.PARAMS_COMMON_TOKEN) String token);
}
