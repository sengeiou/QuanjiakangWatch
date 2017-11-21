package com.quanjiakan.net_presenter;

import com.quanjiakan.activity.common.setting.healthinquiry.HealthInquiryFurtherAskActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.service.get.string.RxPostHealthInquiryProblemAnswerListService;
import com.quanjiakan.net.retrofit.service.post.string.RxPostHealthInquiryDoctorEvaluateService;
import com.quanjiakan.net.retrofit.service.post.string.RxPostHealthInquiryDoctorInfoService;
import com.quanjiakan.net.retrofit.service.post.string.RxPostSendFurtherInquiryService;
import com.quanjiakan.util.common.LogUtil;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/20.
 */

public class HealthInquiryFurtherAskPresenter implements IBasePresenter {

    public void getProblemAnswerList(final HealthInquiryFurtherAskActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_ANSWER_LIST);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);//TODO
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_ANSWER_LIST);

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
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_ANSWER_LIST);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_ANSWER_LIST);
                        activityMvp.onError(IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_ANSWER_LIST,200,e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("HEALTH_INQUIRY_PROBLEM_ANSWER_LIST:"+response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_ANSWER_LIST);
                        activityMvp.onSuccess(IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_ANSWER_LIST,200,response);
                    }
                });
    }

    public void getProblemDoctorInfo(final HealthInquiryFurtherAskActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_INFO);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);//TODO
            return ;
        }

        //TODO
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_APP);
        RxPostHealthInquiryDoctorInfoService rxGetRequest = retrofit.create(RxPostHealthInquiryDoctorInfoService.class);
        rxGetRequest.doPostDoctorInfo(params.get(IParamsName.PARAMS_HEALTH_INQURIRY_TYPE),
                params.get(IParamsName.PARAMS_COMMON_MEMBERID),
                params.get(IParamsName.PARAMS_HEALTH_INQURIRY_DOCTOR_ID),
                params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.onError(IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_INFO,200,e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("HEALTH_INQUIRY_DOCTOR_INFO :"+response);
                        activityMvp.onSuccess(IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_INFO,200,response);
                    }
                });
    }

    public void sendProblem(final HealthInquiryFurtherAskActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_SEND_QUESTION);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);//TODO
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_SEND_QUESTION);

        //TODO
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_APP);
        RxPostSendFurtherInquiryService rxGetRequest = retrofit.create(RxPostSendFurtherInquiryService.class);
        rxGetRequest.doPostSendFurtherInquriry(params.get(IParamsName.PARAMS_HEALTH_INQURIRY_TYPE),
                params.get(IParamsName.PARAMS_COMMON_MEMBERID),
                params.get(IParamsName.PARAMS_HEALTH_INQURIRY_CONTENT),
                params.get(IParamsName.PARAMS_HEALTH_INQURIRY_FROMTOKEN),
                params.get(IParamsName.PARAMS_HEALTH_INQURIRY_TOTOKEN),
                params.get(IParamsName.PARAMS_HEALTH_INQURIRY_CHUNYUID),
                params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_SEND_QUESTION);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_SEND_QUESTION);
                        activityMvp.onError(IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_SEND_QUESTION,200,e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("HEALTH_INQUIRY_PROBLEM_SEND_QUESTION :"+response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_SEND_QUESTION);
                        activityMvp.onSuccess(IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_SEND_QUESTION,200,response);
                    }
                });
    }

    public void evaluateDoctor(final HealthInquiryFurtherAskActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_EVALUATE);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);//TODO
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_EVALUATE);

        //TODO
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_STATIC);
        RxPostHealthInquiryDoctorEvaluateService rxGetRequest = retrofit.create(RxPostHealthInquiryDoctorEvaluateService.class);
        rxGetRequest.doPostDoctorEvaluate(
                params.get(IParamsName.PARAMS_COMMON_DEVICE_TYPE),
                params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_DEVICE_CLIENT),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_USERID),

                params.get(IParamsName.PARAMS_HEALTH_INQUIRY_PROBLEM_ID),
                params.get(IParamsName.PARAMS_HEALTH_INQUIRY_USERNAME),
                params.get(IParamsName.PARAMS_HEALTH_INQUIRY_STAR),
                params.get(IParamsName.PARAMS_HEALTH_INQUIRY_CONTENT))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_EVALUATE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_EVALUATE);
                        activityMvp.onError(IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_EVALUATE,200,e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("HEALTH_INQUIRY_DOCTOR_EVALUATE :"+response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_EVALUATE);
                        activityMvp.onSuccess(IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_EVALUATE,200,response);
                    }
                });
    }
}
