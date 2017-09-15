package com.quanjiakan.net.retrofit.service;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 * http://app.quanjiakan.com/familycare/health/api?code=sms&action=getsms
 */

public interface RxPostGetSMSService {

    @FormUrlEncoded
    @POST("/familycare/health/api?code=sms&action=getsms")
    rx.Observable<String> doGetSMSCode(@Field("data") String password);
}
