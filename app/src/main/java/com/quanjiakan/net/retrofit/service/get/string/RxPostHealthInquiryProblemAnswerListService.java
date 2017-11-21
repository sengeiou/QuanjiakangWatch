package com.quanjiakan.net.retrofit.service.get.string;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 *
 * http://app.quanjiakan.com/pingan/api?handler=chunyu&action=queryproblemanswer&
 * memberId=" + QuanjiakanSetting.getInstance().getUserId() + "&
 * platform=2&
 * token="
 *
 */

public interface RxPostHealthInquiryProblemAnswerListService {

    @GET("/"+ IHttpUrlConstants.PROJECT_PINGAN+"/"+IHttpUrlConstants.REQUEST_GET+"?handler=chunyu&action=queryproblemanswer")
    rx.Observable<String> doGetAnswerList(@Query(IParamsName.PARAMS_COMMON_MEMBERID) String memberId,
                                           @Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                           @Query(IParamsName.PARAMS_COMMON_TOKEN) String token,
                                           @Query(IParamsName.PARAMS_HEALTH_INQURIRY_PROBLEM_ID) String problemId

    );
}
