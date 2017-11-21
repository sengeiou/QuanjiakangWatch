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
 * http://app.quanjiakan.com/pingan/v1?handler=chunyu&action=doctordetail
 *
 HashMap<String,String> params = new HashMap<>();
 params.put("type", "cy");
 params.put("memberId", BaseApplication.getInstances().getUser_id());
 params.put("doctorId", info.getDoctorId());
 params.put("platform", "2");
 params.put("token", BaseApplication.getInstances().getSessionID());
 */

public interface RxPostHealthInquiryDoctorInfoService {

    @FormUrlEncoded
    @POST("/"+ IHttpUrlConstants.PROJECT_PINGAN+"/"+IHttpUrlConstants.REQUEST_POST_V1+"?handler=chunyu&action=doctordetail")
    rx.Observable<String> doPostDoctorInfo(@Field(IParamsName.PARAMS_HEALTH_INQURIRY_TYPE) String type,
                                             @Field(IParamsName.PARAMS_COMMON_MEMBERID) String memberId,
                                             @Field(IParamsName.PARAMS_HEALTH_INQURIRY_DOCTOR_ID) String content,
                                             @Field(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                             @Field(IParamsName.PARAMS_COMMON_TOKEN) String token);
}
