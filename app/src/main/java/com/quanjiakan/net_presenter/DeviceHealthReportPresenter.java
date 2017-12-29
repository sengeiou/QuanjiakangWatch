package com.quanjiakan.net_presenter;

import com.quanjiakan.activity.common.index.devices.old.health.report.HealthReportFragment;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.service.get.string.RxGetDeviceCurrentInfoService;
import com.quanjiakan.net.retrofit.service.get.string.RxGetDeviceHeartRateBloodPressureService;
import com.quanjiakan.util.common.LogUtil;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/27.
 */

public class DeviceHealthReportPresenter implements IBasePresenter {

    public void getHeartRateAndBloodPressure(final HealthReportFragment activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CHART);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);//TODO
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CHART);

        //TODO
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_DEVICE);
        RxGetDeviceHeartRateBloodPressureService rxGetRequest = retrofit.create(RxGetDeviceHeartRateBloodPressureService.class);
        rxGetRequest.doGetHeartRateAndBloodPressure(params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_IMEI_BIG),
                params.get(IParamsName.PARAMS_HEALTH_REPORT_HISTORY_TIME))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CHART);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CHART);
                        activityMvp.onError(IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CHART,200,e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("DEVICE_WATCH_HEALTH_REPORT_CHART:"+response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CHART);
                        activityMvp.onSuccess(IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CHART,200,response);
                    }
                });
    }

    public void getCurrentData(final HealthReportFragment activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CURRENT_DATA);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.NONE,200,null);//TODO
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CURRENT_DATA);

        //TODO
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_DEVICE);
        RxGetDeviceCurrentInfoService rxGetRequest = retrofit.create(RxGetDeviceCurrentInfoService.class);
        rxGetRequest.doGetDeviceCurrentInfo(params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_IMEI_BIG))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CURRENT_DATA);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CURRENT_DATA);
                        activityMvp.onError(IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CURRENT_DATA,200,e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("DEVICE_WATCH_HEALTH_REPORT_CURRENT_DATA:"+response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CURRENT_DATA);
                        activityMvp.onSuccess(IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CURRENT_DATA,200,response);
                    }
                });
    }
}
