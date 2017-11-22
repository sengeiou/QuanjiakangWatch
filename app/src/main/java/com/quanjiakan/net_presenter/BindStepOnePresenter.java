package com.quanjiakan.net_presenter;

import com.quanjiakan.activity.common.index.bind.BindStepOneActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.service.get.string.RxPostHealthInquiryProblemAnswerListService;
import com.quanjiakan.util.common.LogUtil;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/22.
 */

public class BindStepOnePresenter implements IBasePresenter {
    public void checkIMEI(final BindStepOneActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.DEVICE_BIND_STEP_ONE);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);//TODO
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.DEVICE_BIND_STEP_ONE);

        //TODO
        //+BaseApplication.getInstances().getSessionID()
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_APP);
        RxPostHealthInquiryProblemAnswerListService rxGetRequest = retrofit.create(RxPostHealthInquiryProblemAnswerListService.class);
        rxGetRequest.doGetAnswerList(params.get(IParamsName.PARAMS_COMMON_MEMBERID),
                params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_HEALTH_INQURIRY_PROBLEM_ID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_BIND_STEP_ONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_BIND_STEP_ONE);
                        activityMvp.onError(IPresenterBusinessCode.DEVICE_BIND_STEP_ONE,200,e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("DEVICE_BIND_STEP_ONE:"+response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_BIND_STEP_ONE);
                        activityMvp.onSuccess(IPresenterBusinessCode.DEVICE_BIND_STEP_ONE,200,response);
                    }
                });
    }
}
