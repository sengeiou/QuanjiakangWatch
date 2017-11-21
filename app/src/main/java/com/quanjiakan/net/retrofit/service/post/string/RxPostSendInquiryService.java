package com.quanjiakan.net.retrofit.service.post.string;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.PostLastTenMessage;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 http://app.quanjiakan.com/pingan/v1?handler=chunyu&action=createproblem
 params.put("type", "cy");
 params.put("memberId", BaseApplication.getInstances().getUser_id());
 params.put("content", array+"");
 params.put("fromtoken", QuanjiakanSetting.getInstance().getUserId()+"");
 params.put("platform", "2");
 params.put("token", BaseApplication.getInstances().getSessionID());
 */

public interface RxPostSendInquiryService {

    @FormUrlEncoded
    @POST("/"+ IHttpUrlConstants.PROJECT_PINGAN+"/"+IHttpUrlConstants.REQUEST_POST_V1+"?handler=chunyu&action=createproblem")
    rx.Observable<String> doPostSendInquriry(@Field(IParamsName.PARAMS_HEALTH_INQURIRY_TYPE) String type,
                                                        @Field(IParamsName.PARAMS_COMMON_MEMBERID) String memberId,
                                                        @Field(IParamsName.PARAMS_HEALTH_INQURIRY_CONTENT) String content,
                                                        @Field(IParamsName.PARAMS_HEALTH_INQURIRY_FROMTOKEN) String fromtoken,
                                                        @Field(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                                        @Field(IParamsName.PARAMS_COMMON_TOKEN) String token);
}
