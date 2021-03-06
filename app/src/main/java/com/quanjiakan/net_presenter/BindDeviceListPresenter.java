package com.quanjiakan.net_presenter;

import com.pingantong.main.R;
import com.quanjiakan.activity.common.main.fragment.MainMapFragment;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.entity.LoginInfoEntity;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.result_entity.GetWatchListEntity;
import com.quanjiakan.net.retrofit.service.get.object.RxGetWatchListService;
import com.quanjiakan.net.retrofit.service.get.string.RxGetWatchListStatusService;
import com.quanjiakan.net.retrofit.service.get.string.RxGetWatchListStringService;
import com.quanjiakan.util.common.LogUtil;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class BindDeviceListPresenter implements IBasePresenter {
    private LoginInfoEntity entity;

    /*
     {"code":"200","list":[{"image":"http://picture.quanjiakan.com:9080/quanjiakan/resources/device/20171010095800_f5fpylvay3wreoov2h21.png","imei":"352315052834187","location":"113.2412882,23.1322843","locationTime":"2017-10-18 15:52:55.0","name":"%E5%88%9A%E7%BB%93%E5%A9%9A","online":0,"phone":"","relationlist":[{"image":"http://picture.quanjiakan.com:9080/quanjiakan/resources/device/20170824161547_jcq1v5eeyv21vdubej71.png","name":"%e7%9d%a1%e7%9c%a0%e7%9b%91%e6%b5%8b%e4%bb%aa","relation":"866104026684665","type":3}],"type":0},{"image":"http://picture.quanjiakan.com:9080/quanjiakan/resources/device/20171012215718_7so22trwnek7mbgso2s1.png","imei":"355637052788452","location":"113.2411955,23.1322491","locationTime":"2017-10-14 15:34:41.0","name":"%E8%83%A1","online":0,"phone":"18620155897","relationlist":[],"type":0},{"image":"http://picture.quanjiakan.com:9080/quanjiakan/resources/device/20171014142816_4d0941glzgvchsocf3ri.png","imei":"355637053077731","location":"113.2704899,22.9896351","locationTime":"2017-10-18 21:02:47.0","name":"%E9%9B%85%E4%B8%BD","online":1,"phone":"18620155897","relationlist":[],"type":0}],"message":"返回成功","rows":3}
     */
    public void getBindDeviceList(final MainMapFragment activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.DEVICE_WATCH_LIST);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.DEVICE_WATCH_LIST,200,activityMvp.getActivity().getString(R.string.error_common_net_invalid_parameters));
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.DEVICE_WATCH_LIST);

        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_DEVICE);
        RxGetWatchListStringService rxGetRequest = retrofit.create(RxGetWatchListStringService.class);
        rxGetRequest.doGetWatchList(params.get(IParamsName.PARAMS_DEVICE_MEMBER_ID),
                params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_LIST);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_LIST);
                        activityMvp.onError(IPresenterBusinessCode.DEVICE_WATCH_LIST,200,e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("DEVICE_WATCH_LIST:"+response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_LIST);
                        activityMvp.onSuccess(IPresenterBusinessCode.DEVICE_WATCH_LIST,200,response);
                    }
                });
    }

    public void getDeviceListStatus(final MainMapFragment activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.DEVICE_WATCH_LIST_STATUS);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.DEVICE_WATCH_LIST_STATUS,200,activityMvp.getActivity().getString(R.string.error_common_net_invalid_parameters));
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.DEVICE_WATCH_LIST_STATUS);

        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_DEVICE);
        RxGetWatchListStatusService rxGetRequest = retrofit.create(RxGetWatchListStatusService.class);
        rxGetRequest.doGetWatchListStatus(params.get(IParamsName.PARAMS_DEVICE_MEMBER_ID),
                params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_LIST_STATUS);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_LIST_STATUS);
                        activityMvp.onError(IPresenterBusinessCode.DEVICE_WATCH_LIST_STATUS,200,e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("DEVICE_WATCH_LIST_STATUS:"+response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.DEVICE_WATCH_LIST_STATUS);
                        activityMvp.onSuccess(IPresenterBusinessCode.DEVICE_WATCH_LIST_STATUS,200,response);
                    }
                });
    }
}
