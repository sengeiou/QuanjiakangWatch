package com.quanjiakan.net_presenter;

import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseFragment;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.service.get.string.RxGetDeviceInfoService;
import com.quanjiakan.util.common.LogUtil;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/27.
 */

public class DeviceInfoPresenter implements IBasePresenter {
    public void getDeviceInfo(final BaseActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.DEVICE_WATCH_INFO);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);//TODO
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.DEVICE_WATCH_INFO);

        //TODO
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_DEVICE);
        RxGetDeviceInfoService rxGetRequest = retrofit.create(RxGetDeviceInfoService.class);
        rxGetRequest.doGetDeviceInfo(params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_IMEI))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_INFO);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_INFO);
                        activityMvp.onError(IPresenterBusinessCode.DEVICE_WATCH_INFO,200,e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("DEVICE_WATCH_INFO:"+response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_INFO);
                        activityMvp.onSuccess(IPresenterBusinessCode.DEVICE_WATCH_INFO,200,response);
                    }
                });
    }

    public void getDeviceInfo(final BaseFragment activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.DEVICE_WATCH_INFO);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);//TODO
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.DEVICE_WATCH_INFO);

        //TODO
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_DEVICE);
        RxGetDeviceInfoService rxGetRequest = retrofit.create(RxGetDeviceInfoService.class);
        rxGetRequest.doGetDeviceInfo(params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_IMEI))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_INFO);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_INFO);
                        activityMvp.onError(IPresenterBusinessCode.DEVICE_WATCH_INFO,200,e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("DEVICE_WATCH_INFO:"+response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_INFO);
                        activityMvp.onSuccess(IPresenterBusinessCode.DEVICE_WATCH_INFO,200,response);
                    }
                });
    }
}
