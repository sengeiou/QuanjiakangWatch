package com.quanjiakan.net_presenter;

import com.pingantong.main.R;
import com.quanjiakan.activity.common.index.housekeeper.HouseKeeperDetailInfoActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperCompanyPhone;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperDetailEntity;
import com.quanjiakan.net.retrofit.service.RxGetHouseKeeperCompanyPhoneService;
import com.quanjiakan.net.retrofit.service.RxGetHouseKeeperDetailService;
import com.quanjiakan.util.common.LogUtil;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/9.
 */

public class HouseKeeperDetailPresenter implements IBasePresenter {
    public void doGetHouseKeeperDetailInfo(final HouseKeeperDetailInfoActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_DETAIL);
        if (params == null) {
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_DETAIL, 200, activityMvp.getString(R.string.error_common_net_invalid_parameters));
            return;
        }
        //
        activityMvp.showMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_DETAIL);

        Retrofit retrofit = Retrofit2Util.getRetrofit(IHttpUrlConstants.BASEURL_APP);
        RxGetHouseKeeperDetailService rxGetRequest = retrofit.create(RxGetHouseKeeperDetailService.class);
        rxGetRequest.doGetHouseKeeperDetail(params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_HOUSE_KEEPER_DETAIL_ID)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetHouseKeeperDetailEntity>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_DETAIL);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_DETAIL);
                        activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_DETAIL, 200, e.getMessage());
                    }

                    @Override
                    public void onNext(GetHouseKeeperDetailEntity response) {
                        LogUtil.e("HOUSE_KEEPER_TYPE_DETAIL" + response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_DETAIL);
                        activityMvp.onSuccess(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_DETAIL, 200, response);
                    }
                });
    }


    //http://app.quanjiakan.com/pingan/api?handler=jugui&action=getfunction&alias=customer_phone&token=12
    public void getHouseKeeperCompanyPhone(final HouseKeeperDetailInfoActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_COMPANY);
        if (params == null) {
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_COMPANY, 200, activityMvp.getString(R.string.error_common_net_invalid_parameters));
            return;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_COMPANY);

        Retrofit retrofit = Retrofit2Util.getRetrofit(IHttpUrlConstants.BASEURL_APP);
        RxGetHouseKeeperCompanyPhoneService rxGetRequest = retrofit.create(RxGetHouseKeeperCompanyPhoneService.class);
        rxGetRequest.doGetHouseKeeperCompanyPhone(params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_HOUSE_KEEPER_DETAIL_ALIAS))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetHouseKeeperCompanyPhone>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_COMPANY);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_COMPANY);
                        activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_COMPANY, 200, e.getMessage());
                    }

                    @Override
                    public void onNext(GetHouseKeeperCompanyPhone response) {
                        LogUtil.e("HOUSE_KEEPER_TYPE_COMPANY" + response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_COMPANY);
                        activityMvp.onSuccess(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_COMPANY, 200, response);
                    }
                });
    }
}
