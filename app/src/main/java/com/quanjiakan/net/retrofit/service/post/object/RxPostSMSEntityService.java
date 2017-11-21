package com.quanjiakan.net.retrofit.service.post.object;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
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
    @POST("/"+ IHttpUrlConstants.PROJECT_PINGAN+"/"+IHttpUrlConstants.REQUEST_POST_V1+"?handler=member&action=validatecode")
    rx.Observable<PostSMSEntity> doGetSMSCode(@Field(IParamsName.PARAMS_COMMON_MOBILE) String mobile ,
                                              @Field(IParamsName.PARAMS_COMMON_VALIDATE_TYPE) String validateType,
                                              @Field(IParamsName.PARAMS_COMMON_PLATFORM) String platform);
}
