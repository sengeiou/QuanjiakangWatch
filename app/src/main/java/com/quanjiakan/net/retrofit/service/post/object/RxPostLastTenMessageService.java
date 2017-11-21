package com.quanjiakan.net.retrofit.service.post.object;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.PostLastTenMessage;
import com.quanjiakan.net.retrofit.result_entity.PostSMSEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 http://app.quanjiakan.com/pingan/v1?
 handler=chunyu&action=queryproblem&
 memberId=10833&
 platform=1&
 token=d2ab13174e1f166f2fb6283fe1b230a0
 */

public interface RxPostLastTenMessageService {

    @FormUrlEncoded
    @POST("/"+ IHttpUrlConstants.PROJECT_PINGAN+"/"+IHttpUrlConstants.REQUEST_POST_V1+"?handler=chunyu&action=queryproblem")
    rx.Observable<PostLastTenMessage> doPostLastMessage(@Field(IParamsName.PARAMS_COMMON_MEMBERID) String memberId,
                                                        @Field(IParamsName.PARAMS_COMMON_TOKEN) String token,
                                                        @Field(IParamsName.PARAMS_COMMON_PLATFORM) String platform);
}
