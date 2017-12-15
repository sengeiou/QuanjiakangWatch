package com.quanjiakan.net_presenter;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseFragment;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.service.get.string.RxGetPayEncryptPrivateKeyService;
import com.quanjiakan.util.common.LogUtil;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/15.
 */

public class PayEncryptPresenter implements IBasePresenter {
    public void getPublicKey(final BaseActivity activityMvp) {
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.PAY_GET_PRIVATE_KEY);
        if (params == null) {
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.PAY_GET_PRIVATE_KEY, 200, activityMvp.getString(R.string.error_common_net_invalid_parameters));
            return;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.PAY_GET_PRIVATE_KEY);
        //
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_PAY_NO_PORT);
        RxGetPayEncryptPrivateKeyService rxGetRequest = retrofit.create(RxGetPayEncryptPrivateKeyService.class);
        rxGetRequest.doGetPrivateKey(params.get(IParamsName.PARAMS_COMMON_MEMBERID),
                params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.PAY_GET_PRIVATE_KEY);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.PAY_GET_PRIVATE_KEY);
                        activityMvp.onError(IPresenterBusinessCode.PAY_GET_PRIVATE_KEY, 200, e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("PAY_GET_PRIVATE_KEY:" + response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.PAY_GET_PRIVATE_KEY);
                        activityMvp.onSuccess(IPresenterBusinessCode.PAY_GET_PRIVATE_KEY, 200, response);
                    }
                });
    }

    public void getPublicKey(final BaseFragment activityMvp) {
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.PAY_GET_PRIVATE_KEY);
        if (params == null) {
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.PAY_GET_PRIVATE_KEY, 200, activityMvp.getString(R.string.error_common_net_invalid_parameters));
            return;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.PAY_GET_PRIVATE_KEY);
        //
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_PAY_NO_PORT);
        RxGetPayEncryptPrivateKeyService rxGetRequest = retrofit.create(RxGetPayEncryptPrivateKeyService.class);
        rxGetRequest.doGetPrivateKey(params.get(IParamsName.PARAMS_COMMON_MEMBERID),
                params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.PAY_GET_PRIVATE_KEY);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.PAY_GET_PRIVATE_KEY);
                        activityMvp.onError(IPresenterBusinessCode.PAY_GET_PRIVATE_KEY, 200, e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("PAY_GET_PRIVATE_KEY:" + response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.PAY_GET_PRIVATE_KEY);
                        activityMvp.onSuccess(IPresenterBusinessCode.PAY_GET_PRIVATE_KEY, 200, response);
                    }
                });
    }
}
