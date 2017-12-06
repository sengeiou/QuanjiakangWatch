package com.quanjiakan.net.retrofit.service.get.string;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.
 *
 http://app.quanjiakan.com +"/pingan/api?handler=member&action=modifyprofile" +
 "&memberId=" + BaseApplication.getInstances().getUser_id() +
 "&nickname=" + nickName +
 "&icon=" + picPath +
 "&platform=2" +
 "&token="+ BaseApplication.getInstances().getSessionID()
 */

public interface RxGetModifyUserInfoService {

    @GET("/"+IHttpUrlConstants.PROJECT_PINGAN+"/"+ IHttpUrlConstants.REQUEST_GET+"?handler=member&action=modifyprofile")
    rx.Observable<String> doGetModifyUserInfo(@Query(IParamsName.PARAMS_COMMON_MEMBERID) String memberId,
                                              @Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                              @Query(IParamsName.PARAMS_COMMON_TOKEN) String token,
                                              @Query(IParamsName.PARAMS_MODIFY_NAME) String nickName,
                                              @Query(IParamsName.PARAMS_MODIFY_HEAD_ICON) String icon);

    @GET("/"+IHttpUrlConstants.PROJECT_PINGAN+"/"+ IHttpUrlConstants.REQUEST_GET+"?handler=member&action=modifyprofile")
    rx.Observable<String> doGetModifyUserIcon(@Query(IParamsName.PARAMS_COMMON_MEMBERID) String memberId,
                                              @Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                              @Query(IParamsName.PARAMS_COMMON_TOKEN) String token,
                                              @Query(IParamsName.PARAMS_MODIFY_HEAD_ICON) String icon);

    @GET("/"+IHttpUrlConstants.PROJECT_PINGAN+"/"+ IHttpUrlConstants.REQUEST_GET+"?handler=member&action=modifyprofile")
    rx.Observable<String> doGetModifyUserName(@Query(IParamsName.PARAMS_COMMON_MEMBERID) String memberId,
                                              @Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                              @Query(IParamsName.PARAMS_COMMON_TOKEN) String token,
                                              @Query(IParamsName.PARAMS_MODIFY_NAME) String nickName
    );
}
