package com.quanjiakan.net_presenter;

import com.quanjiakan.activity.common.login.SignupActivity;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.result_entity.PostSMSEntity;
import com.quanjiakan.net.retrofit.result_entity.PostSignupEntity;
import com.quanjiakan.net.retrofit.service.RxPostSMSEntityService;
import com.quanjiakan.net.retrofit.service.RxPostSignupService;
import com.quanjiakan.util.common.LogUtil;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/13.
 */

public class SignupPresenter implements IBasePresenter {

    public void getSMSCode(final SignupActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.SMS_CODE);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);//TODO
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.SMS_CODE);

        //TODO
        Retrofit retrofit = Retrofit2Util.getRetrofit(IHttpUrlConstants.BASEURL_QUANJIAKANG);
        RxPostSMSEntityService rxGetRequest = retrofit.create(RxPostSMSEntityService.class);
        rxGetRequest.doGetSMSCode(activityMvp.getPhoneNumber(),"1",IHttpUrlConstants.PLATFORM_ANDROID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostSMSEntity>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.SMS_CODE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.SMS_CODE);
                        activityMvp.onError(IPresenterBusinessCode.SMS_CODE,200,e.getMessage());
                    }

                    @Override
                    public void onNext(PostSMSEntity response) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.SMS_CODE);
                        activityMvp.onSuccess(IPresenterBusinessCode.SMS_CODE,200,response);
                    }
                });
    }

    public void doSignup(final SignupActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.SIGNUP);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.SIGNUP);

        //TODO
        Retrofit retrofit = Retrofit2Util.getRetrofit(IHttpUrlConstants.BASEURL_QUANJIAKANG);
        RxPostSignupService rxGetRequest = retrofit.create(RxPostSignupService.class);
        rxGetRequest.doLogin(params.get("mobile"),
                params.get("password"),
                params.get("validateCode"),
                params.get("nickname"),
                params.get("platform"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostSignupEntity>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.SIGNUP);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(" -- Http RxPostSignupService onError:"+e.getMessage());
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.SIGNUP);
                        activityMvp.onError(IPresenterBusinessCode.SIGNUP,200,e.getMessage());
                    }

                    @Override
                    public void onNext(PostSignupEntity response) {
                        LogUtil.e(" -- Http RxPostSignupService onSuccess:"+response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.SIGNUP);
                        activityMvp.onSuccess(IPresenterBusinessCode.SIGNUP,200,response);
                    }
                });

    }
}
