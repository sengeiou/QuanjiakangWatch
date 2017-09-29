package com.quanjiakan.net.retrofit.service;

import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.PostSMSEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 * http://app.quanjiakan.com/familycare/health/api?code=sms&action=getsms
 */

public interface RxGetSMSService {

    @FormUrlEncoded
    @GET("/"+IHttpUrlConstants.PROJECT_PINGAN+"/"+ IHttpUrlConstants.REQUEST_POST_V1+"?handler=member&action=validatecode")
    rx.Observable<String> doGetSMSCode(@Query("data") String password );
}
