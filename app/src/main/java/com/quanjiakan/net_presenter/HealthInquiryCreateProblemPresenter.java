package com.quanjiakan.net_presenter;

import com.quanjiakan.activity.common.index.healthinquiry.HealthInquiryCreateProblemActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.result_entity.PostLastTenMessage;
import com.quanjiakan.net.retrofit.result_entity.PostSMSEntity;
import com.quanjiakan.net.retrofit.service.RxPostLastTenMessageService;
import com.quanjiakan.net.retrofit.service.RxPostSMSEntityService;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/15.
 */

public class HealthInquiryCreateProblemPresenter implements IBasePresenter {
    
    public void getLastTenMessage(final HealthInquiryCreateProblemActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.HEALTH_INQUIRY_CREATE_PROBLEM);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);//TODO
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.HEALTH_INQUIRY_CREATE_PROBLEM);

        //TODO
        Retrofit retrofit = Retrofit2Util.getRetrofit(IHttpUrlConstants.BASEURL_APP);
        RxPostLastTenMessageService rxGetRequest = retrofit.create(RxPostLastTenMessageService.class);
        rxGetRequest.doPostLastMessage(params.get(IParamsName.PARAMS_COMMON_MEMBERID),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_PLATFORM))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostLastTenMessage>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_INQUIRY_CREATE_PROBLEM);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_INQUIRY_CREATE_PROBLEM);
                        activityMvp.onError(IPresenterBusinessCode.HEALTH_INQUIRY_CREATE_PROBLEM,200,e.getMessage());
                    }

                    @Override
                    public void onNext(PostLastTenMessage response) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_INQUIRY_CREATE_PROBLEM);
                        activityMvp.onSuccess(IPresenterBusinessCode.HEALTH_INQUIRY_CREATE_PROBLEM,200,response);
                    }
                });
    }
}
