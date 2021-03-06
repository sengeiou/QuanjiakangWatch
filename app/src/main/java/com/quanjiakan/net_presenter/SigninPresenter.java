package com.quanjiakan.net_presenter;

import com.quanjiakan.activity.common.login.SigninActivity_mvp;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.entity.LoginInfoEntity;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.result_entity.PostLoginEntity;
import com.quanjiakan.net.retrofit.service.post.string.RxPostLoginStringService;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class SigninPresenter implements IBasePresenter {
    public void doLogin(final SigninActivity_mvp activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.LOGIN);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.LOGIN);

        //TODO 使用 getRetrofitStringResult 方法在获取JSON格式的数据返回时会现JSON转换的异常
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_QUANJIAKANG);
        RxPostLoginStringService rxGetRequest = retrofit.create(RxPostLoginStringService.class);
        rxGetRequest.doLogin(params.get(IParamsName.PARAMS_COMMON_PASSWORD),
                params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_MOBILE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.LOGIN);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.LOGIN);
                        activityMvp.onError(IPresenterBusinessCode.LOGIN,200,e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.LOGIN);
                        activityMvp.onSuccess(IPresenterBusinessCode.LOGIN,200,response);
                    }
                });

    }
}
