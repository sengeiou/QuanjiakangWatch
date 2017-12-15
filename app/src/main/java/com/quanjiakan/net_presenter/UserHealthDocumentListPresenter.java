package com.quanjiakan.net_presenter;

import com.quanjiakan.activity.common.setting.healthdocument.UserHealthDocumentActivity;
import com.quanjiakan.activity.common.setting.healthdocument.UserHealthDocumentAddActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.service.get.string.RxGetUserHealthDocumentListService;
import com.quanjiakan.net.retrofit.service.post.string.RxPostLoginStringService;
import com.quanjiakan.net.retrofit.service.post.string.RxPostUserHealthDocumentService;
import com.quanjiakan.util.common.LogUtil;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/12.
 */

public class UserHealthDocumentListPresenter  implements IBasePresenter{

    public void getUserHealthDocument(final UserHealthDocumentActivity activityMvp){
        /*
        ---TYPE_GET_STRING_NOCACHE
        http://app.quanjiakan.com/pingan/api?handler=archives&action=userarchives&platform=2&token=b04be2446c7644b528f6dfda815a991e&memberId=11303&page=1&rows=10
        Request Result:{"code":"200","list":[{"createtime":"2017-12-12 17:55:08.0","deviceid":0,"id":382,"medicalName":"ceshi","medicalRecord":"http://picture.quanjiakan.com/quanjiakan/resources/medical/20171212175501_gy1onqv4pyzdzjcck3ie.png"}],"message":"返回成功","rows":1}
         */
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.HEALTH_DOCUMENT_USER);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.HEALTH_DOCUMENT_USER);

        //TODO 使用 getRetrofitStringResult 方法在获取JSON格式的数据返回时会现JSON转换的异常
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_APP);
        RxGetUserHealthDocumentListService rxGetRequest = retrofit.create(RxGetUserHealthDocumentListService.class);
        rxGetRequest.doGetUserHealthDocumentList(params.get(IParamsName.PARAMS_COMMON_MEMBERID),
                params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_PAGE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_DOCUMENT_USER);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_DOCUMENT_USER);
                        activityMvp.onError(IPresenterBusinessCode.HEALTH_DOCUMENT_USER,200,e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("HEALTH_DOCUMENT_USER:"+response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_DOCUMENT_USER);
                        activityMvp.onSuccess(IPresenterBusinessCode.HEALTH_DOCUMENT_USER,200,response);
                    }
                });
    }

    public void addUserHealthDocumentInfo(final UserHealthDocumentAddActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.HEALTH_DOCUMENT_USER_ADD);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.HEALTH_DOCUMENT_USER_ADD);

        //
        //TODO 使用 getRetrofitStringResult 方法在获取JSON格式的数据返回时会现JSON转换的异常
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_QUANJIAKANG);
        RxPostUserHealthDocumentService rxGetRequest = retrofit.create(RxPostUserHealthDocumentService.class);
        rxGetRequest.submitUserHealthDocumentInfo(params.get(IParamsName.PARAMS_COMMON_MEMBERID),
                params.get(IParamsName.PARAMS_USER_HEALTH_DOCUMENT_NAME),
                params.get(IParamsName.PARAMS_USER_HEALTH_DOCUMENT_IMAGE),
                params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_DOCUMENT_USER_ADD);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_DOCUMENT_USER_ADD);
                        activityMvp.onError(IPresenterBusinessCode.HEALTH_DOCUMENT_USER_ADD,200,e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("HEALTH_DOCUMENT_USER_ADD:"+response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_DOCUMENT_USER_ADD);
                        activityMvp.onSuccess(IPresenterBusinessCode.HEALTH_DOCUMENT_USER_ADD,200,response);
                    }
                });
    }
}
