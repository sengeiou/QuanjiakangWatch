package com.quanjiakan.net_presenter;

import android.util.Log;

import com.quanjiakan.activity.common.index.housekeeper.HouseKeeperListActivity;
import com.quanjiakan.activity.common.login.SigninActivity_mvp;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperListEntity;
import com.quanjiakan.net.retrofit.result_entity.GetWatchListEntity;
import com.quanjiakan.net.retrofit.result_entity.PostLoginEntity;
import com.quanjiakan.net.retrofit.service.RxCommonGetUrlStringService;
import com.quanjiakan.net.retrofit.service.RxGetHouseKeeperListService;
import com.quanjiakan.net.retrofit.service.RxGetHouseKeeperListStringService;
import com.quanjiakan.net.retrofit.service.RxGetWatchListService;
import com.quanjiakan.net.retrofit.service.RxPostLoginEntityService;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.watch.R;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/10/26.
 */

public class HouseKeeperListPresenter implements IBasePresenter {
    public void getHouseKeeperListWithoutPosition(final HouseKeeperListActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.HOUSE_KEEPER_LIST);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_LIST,200,activityMvp.getString(R.string.error_common_net_invalid_parameters));
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST);

        Retrofit retrofit = Retrofit2Util.getRetrofit(IHttpUrlConstants.BASEURL_APP);
        RxGetHouseKeeperListService rxGetRequest = retrofit.create(RxGetHouseKeeperListService.class);
        rxGetRequest.doGetWatchListWithoutLocation(params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_PAGE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetHouseKeeperListEntity>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST);
                        activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_LIST,200,e.getMessage());
                    }

                    @Override
                    public void onNext(GetHouseKeeperListEntity response) {
                        LogUtil.e("HOUSE_KEEPER_LIST");
                        if(response!=null && response.getList().size()>0){
                            for (GetHouseKeeperListEntity.ListBean entity: response.getList()) {
                                if(entity!=null){
                                    LogUtil.e(entity.toString());
                                }
                            }
                        }
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST);
                        activityMvp.onSuccess(IPresenterBusinessCode.HOUSE_KEEPER_LIST,200,response);
                    }
                });

    }

    public void getHouseKeeperListWithPosition(final HouseKeeperListActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION);
        if(params==null){
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION,200,activityMvp.getString(R.string.error_common_net_invalid_parameters));
            return ;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION);

        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_APP);
        RxGetHouseKeeperListStringService rxGetRequest = retrofit.create(RxGetHouseKeeperListStringService.class);
        rxGetRequest.doGetWatchListWithoutLocation(params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_PAGE),
                params.get(IParamsName.PARAMS_HOUSE_KEEPER_PROVINCE),
                params.get(IParamsName.PARAMS_HOUSE_KEEPER_CITY),
                params.get(IParamsName.PARAMS_HOUSE_KEEPER_DIST))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION);
                        activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION,200,e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("HOUSE_KEEPER_LIST_WITH_LOCATION"+response);
//                        if(response!=null && response.getList().size()>0){
//                            for (GetHouseKeeperListEntity.ListBean entity: response.getList()) {
//                                if(entity!=null){
//                                    LogUtil.e(entity.toString());
//                                }
//                            }
//                        }
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION);
                        activityMvp.onSuccess(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION,200,response);
                    }
                });

        //TODO Get 请求可以这么做---然后自己去进行序列化得到相应的对象（仅限Get请求）
        Retrofit retrofit2 = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_APP);
        RxCommonGetUrlStringService rxCommonGetUrlStringService = retrofit2.create(RxCommonGetUrlStringService.class);
        rxCommonGetUrlStringService.setUrl("http://app.quanjiakan.com/pingan/api?handler=services&action=housekeeperlist&platform=2&token=86daeff8e1a9a345efbf667ab93fb94d&page=1&serviceCity=%E5%B9%BF%E5%B7%9E&serviceProvince=%E5%B9%BF%E4%B8%9C&serviceDist=%E8%8D%94%E6%B9%BE%E5%8C%BA&rows=5")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("onError");
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("onNext:\n"+response);
                    }
                });


    }
}
