package com.quanjiakan.net_presenter;

import com.quanjiakan.activity.common.index.housekeeper.HouseKeeperOrderActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperDetailEntity;
import com.quanjiakan.net.retrofit.service.RxGetHouseKeeperDetailService;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.watch.R;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/10.
 */

public class HouseKeeperOrderPresenter implements IBasePresenter {

    public void doGetHouseKeeperOrder(final HouseKeeperOrderActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.HOUSE_KEEPER_ORDER);
        if (params == null) {
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_ORDER, 200, activityMvp.getString(R.string.error_common_net_invalid_parameters));
            return;
        }
        //
        activityMvp.showMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_ORDER);

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
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_ORDER);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_ORDER);
                        activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_ORDER, 200, e.getMessage());
                    }

                    @Override
                    public void onNext(GetHouseKeeperDetailEntity response) {
                        LogUtil.e("HOUSE_KEEPER_ORDER" + response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_ORDER);
                        activityMvp.onSuccess(IPresenterBusinessCode.HOUSE_KEEPER_ORDER, 200, response);
                    }
                });
    }
}
