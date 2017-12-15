package com.quanjiakan.net_presenter;

import com.quanjiakan.activity.common.setting.more.modifypassword.ModifyPasswordActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.service.post.string.RxPostModifyPasswordService;
import com.quanjiakan.util.common.LogUtil;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/22.
 */

public class AExamplePostPresenter implements IBasePresenter {
    public void modifyPassword(final ModifyPasswordActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.MODIFY_PASSWORD);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);//TODO
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.MODIFY_PASSWORD);

        //TODO
        //+BaseApplication.getInstances().getSessionID()
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_APP);
        RxPostModifyPasswordService rxGetRequest = retrofit.create(RxPostModifyPasswordService.class);
        rxGetRequest.modifyPassword(params.get(IParamsName.PARAMS_COMMON_PASSWORD),
                params.get(IParamsName.PARAMS_COMMON_MEMBERID),
                params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.MODIFY_PASSWORD);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.MODIFY_PASSWORD);
                        activityMvp.onError(IPresenterBusinessCode.MODIFY_PASSWORD,200,e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("MODIFY_PASSWORD:"+response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.MODIFY_PASSWORD);
                        activityMvp.onSuccess(IPresenterBusinessCode.MODIFY_PASSWORD,200,response);
                    }
                });
    }
}
