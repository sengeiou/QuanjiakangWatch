package com.quanjiakan.net_presenter;

import com.quanjiakan.activity.common.index.bind.BindStepOneActivity;
import com.quanjiakan.activity.common.setting.more.improveinfo.ImproveUserInfoActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.service.get.string.RxGetModifyUserInfoService;
import com.quanjiakan.net.retrofit.service.post.string.RxPostBindStepOneStringService;
import com.quanjiakan.util.common.LogUtil;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/22.
 */

public class ModifyUserInfoPresenter implements IBasePresenter {
    public void modifyUserInfo(final ImproveUserInfoActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.MODIFY_USER_INFO);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);//TODO
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.MODIFY_USER_INFO);

        //TODO
        //+BaseApplication.getInstances().getSessionID()
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_APP);
        RxGetModifyUserInfoService rxGetRequest = retrofit.create(RxGetModifyUserInfoService.class);
        if(params.get(IParamsName.PARAMS_MODIFY_HEAD_ICON)==null){
            //TODO 不传入头像参数
            rxGetRequest.doGetModifyUserName(params.get(IParamsName.PARAMS_COMMON_IMEI),
                    params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                    params.get(IParamsName.PARAMS_COMMON_TOKEN),
                    params.get(IParamsName.PARAMS_MODIFY_NAME))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            activityMvp.dismissMyDialog(IPresenterBusinessCode.MODIFY_USER_INFO);
                        }

                        @Override
                        public void onError(Throwable e) {
                            activityMvp.dismissMyDialog(IPresenterBusinessCode.MODIFY_USER_INFO);
                            activityMvp.onError(IPresenterBusinessCode.MODIFY_USER_INFO,200,e.getMessage());
                        }

                        @Override
                        public void onNext(String response) {
                            LogUtil.e("MODIFY_USER_INFO:"+response);
                            activityMvp.dismissMyDialog(IPresenterBusinessCode.MODIFY_USER_INFO);
                            activityMvp.onSuccess(IPresenterBusinessCode.MODIFY_USER_INFO,200,response);
                        }
                    });
        }else{
            //TODO 传入头像参数和名称
            rxGetRequest.doGetModifyUserInfo(params.get(IParamsName.PARAMS_COMMON_MEMBERID),
                    params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                    params.get(IParamsName.PARAMS_COMMON_TOKEN),
                    params.get(IParamsName.PARAMS_MODIFY_NAME),
                    params.get(IParamsName.PARAMS_MODIFY_HEAD_ICON))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            activityMvp.dismissMyDialog(IPresenterBusinessCode.MODIFY_USER_INFO);
                        }

                        @Override
                        public void onError(Throwable e) {
                            activityMvp.dismissMyDialog(IPresenterBusinessCode.MODIFY_USER_INFO);
                            activityMvp.onError(IPresenterBusinessCode.MODIFY_USER_INFO,200,e.getMessage());
                        }

                        @Override
                        public void onNext(String response) {
                            LogUtil.e("MODIFY_USER_INFO:"+response);
                            activityMvp.dismissMyDialog(IPresenterBusinessCode.MODIFY_USER_INFO);
                            activityMvp.onSuccess(IPresenterBusinessCode.MODIFY_USER_INFO,200,response);
                        }
                    });
        }
    }
}
