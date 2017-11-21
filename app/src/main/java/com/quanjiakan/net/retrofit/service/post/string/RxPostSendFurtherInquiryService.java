package com.quanjiakan.net.retrofit.service.post.string;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 http://app.quanjiakan.com/pingan/v1?handler=chunyu&action=persist
 params.put("type", "cy");
 params.put("memberId", BaseApplication.getInstances().getLoginInfo().getUserId());
 params.put("content", content + "");//内容jsonArray格式
 params.put("fromtoken", BaseApplication.getInstances().getLoginInfo().getUserId()); //自己的UserID
 params.put("totoken", problemID.getDoctorId());//咨询医生的UserID
 params.put("chunyuId", problemID.getChunyuId() + "");//咨询的问题id
 params.put("platform", "2");
 params.put("token", BaseApplication.getInstances().getLoginInfo().getToken());

 */

public interface RxPostSendFurtherInquiryService {

    @FormUrlEncoded
    @POST("/"+ IHttpUrlConstants.PROJECT_PINGAN+"/"+IHttpUrlConstants.REQUEST_POST_V1+"?handler=chunyu&action=createproblem")
    rx.Observable<String> doPostSendFurtherInquriry(@Field(IParamsName.PARAMS_HEALTH_INQURIRY_TYPE) String type,
                                             @Field(IParamsName.PARAMS_COMMON_MEMBERID) String memberId,
                                             @Field(IParamsName.PARAMS_HEALTH_INQURIRY_CONTENT) String content,
                                             @Field(IParamsName.PARAMS_HEALTH_INQURIRY_FROMTOKEN) String fromtoken,
                                             @Field(IParamsName.PARAMS_HEALTH_INQURIRY_TOTOKEN) String totoken,
                                             @Field(IParamsName.PARAMS_HEALTH_INQURIRY_CHUNYUID) String chunyuId,
                                             @Field(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                             @Field(IParamsName.PARAMS_COMMON_TOKEN) String token);
}
