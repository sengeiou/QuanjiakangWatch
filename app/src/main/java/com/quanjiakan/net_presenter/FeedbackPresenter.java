package com.quanjiakan.net_presenter;

import com.quanjiakan.activity.common.setting.more.feedback.FeedbackActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.result_entity.PostSMSEntity;
import com.quanjiakan.net.retrofit.service.post.string.RxPostFeedbackService;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/7.
 */

public class FeedbackPresenter implements IBasePresenter {
    public void doFeedback(final FeedbackActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.FEEDBACK);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);//TODO
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.FEEDBACK);

        //TODO
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_APP);
        RxPostFeedbackService rxGetRequest = retrofit.create(RxPostFeedbackService.class);
        rxGetRequest.feedback(params.get(IParamsName.PARAMS_COMMON_MEMBERID),
                params.get(IParamsName.PARAMS_FEEDBACK_EMAIL),
                params.get(IParamsName.PARAMS_FEEDBACK_CONTENT),
                params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.FEEDBACK);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.FEEDBACK);
                        activityMvp.onError(IPresenterBusinessCode.FEEDBACK,200,e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.FEEDBACK);
                        activityMvp.onSuccess(IPresenterBusinessCode.FEEDBACK,200,response);
                    }
                });
    }
}
