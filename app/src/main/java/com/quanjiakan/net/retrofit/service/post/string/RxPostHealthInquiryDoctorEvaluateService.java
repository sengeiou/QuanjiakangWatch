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
 http://static.quanjiakan.com/familycare/api?
 devicetype=0&
 platform=2&
 client=1
 "&token="+ BaseApplication.getInstances().getSessionID() +
 "&user_id="+QuanjiakanSetting.getInstance().getUserId() + "&m=consultant&type=cy&a=evaluate";

 *
 */

public interface RxPostHealthInquiryDoctorEvaluateService {

    @FormUrlEncoded
    @POST("/"+ IHttpUrlConstants.PROJECT_FAMLIY_CARE+"/"+IHttpUrlConstants.REQUEST_GET+"?m=consultant&type=cy&a=evaluate")
    rx.Observable<String> doPostDoctorEvaluate(@Field(IParamsName.PARAMS_COMMON_DEVICE_TYPE) String type,
                                               @Field(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                               @Field(IParamsName.PARAMS_COMMON_DEVICE_CLIENT) String client,
                                               @Field(IParamsName.PARAMS_COMMON_TOKEN) String token,
                                               @Field(IParamsName.PARAMS_COMMON_USERID) String user_id,

                                               @Field(IParamsName.PARAMS_HEALTH_INQUIRY_PROBLEM_ID) String id,
                                               @Field(IParamsName.PARAMS_HEALTH_INQUIRY_USERNAME) String userid,
                                               @Field(IParamsName.PARAMS_HEALTH_INQUIRY_STAR) String star,
                                               @Field(IParamsName.PARAMS_HEALTH_INQUIRY_CONTENT) String content);
}
