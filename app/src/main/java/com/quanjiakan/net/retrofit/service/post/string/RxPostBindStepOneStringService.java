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
 * http://device.quanjiakan.com/devices/v1?handler=watch&action=devicetype
 */

public interface RxPostBindStepOneStringService {
    //TODO  get请求为api   post为v1
    @FormUrlEncoded
    @POST("/"+ IHttpUrlConstants.PROJECT_DEVICES+"/"+IHttpUrlConstants.REQUEST_POST_V1+"?handler=watch&action=devicetype")
    rx.Observable<String> doCheckIMEI(@Field(IParamsName.PARAMS_COMMON_IMEI) String imei,
                                      @Field(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                      @Field(IParamsName.PARAMS_COMMON_TOKEN) String token);
}
