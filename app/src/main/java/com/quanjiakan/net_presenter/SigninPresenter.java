package com.quanjiakan.net_presenter;

import com.quanjiakan.activity.common.login.SigninActivity_mvp;
import com.quanjiakan.entity.LoginInfoEntity;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.result_entity.PostLoginEntity;
import com.quanjiakan.net.retrofit.service.RxPostLoginEntityService;
import com.quanjiakan.util.common.LogUtil;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class SigninPresenter implements IBasePresenter {
    private LoginInfoEntity entity;
    public void doLogin(final SigninActivity_mvp activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.LOGIN);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.LOGIN);

        //TODO 使用 getRetrofitStringResult 方法在获取JSON格式的数据返回时会现JSON转换的异常
        Retrofit retrofit = Retrofit2Util.getRetrofit(IHttpUrlConstants.BASEURL_QUANJIAKANG);
        RxPostLoginEntityService rxGetRequest = retrofit.create(RxPostLoginEntityService.class);
        rxGetRequest.doLogin(params.get("password"),
                params.get("platform"),
                params.get("mobile"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostLoginEntity>() {
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
                    public void onNext(PostLoginEntity response) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.LOGIN);
                        activityMvp.onSuccess(IPresenterBusinessCode.LOGIN,200,response);
                    }
                });

    }
}
