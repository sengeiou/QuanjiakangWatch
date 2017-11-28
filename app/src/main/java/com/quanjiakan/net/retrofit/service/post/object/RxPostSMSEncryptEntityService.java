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
 http://app.quanjiakan.com/pingan/v1?handler=member&action=verificationcode
 Params:{"platform":"2","ciphertext":"P1kiGztvytjTFmyolX2nqSdcyRvxujhXGfDVIVya5FbAkaXADtYg5xwSMHjS1KbfItSDHPR9vWDP\nBP6aJ71lbiNzpy1XkGGQFrZ7H5kew82AjDdqQNcMz8xz6IYHqHs8zFrowuIBFrZpuEsP2QI4kkd9\nEGpaNqd14F2dA6FAz%2BY\u003d\n"}
 Request Result:{"code":"200","message":"返回成功","object":{"smscode":483181}}
 */

public interface RxPostSMSEncryptEntityService {

    @FormUrlEncoded
    @POST("/"+ IHttpUrlConstants.PROJECT_PINGAN+"/"+IHttpUrlConstants.REQUEST_POST_V1+"?handler=member&action=verificationcode")
    rx.Observable<PostSMSEntity> doGetSMSCode(@Field(IParamsName.PARAMS_COMMON_ENCRYPT) String ciphertext,
                                              @Field(IParamsName.PARAMS_COMMON_PLATFORM) String platform);
}
