package com.quanjiakan.net_presenter;

import com.pingantong.main.R;
import com.quanjiakan.activity.common.index.housekeeper.HouseKeeperListActivity;
import com.quanjiakan.activity.common.setting.housekeeper.HouseKeeperOrderListActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperListEntity;
import com.quanjiakan.net.retrofit.service.get.string.RxGetHouseKeeperOrderListService;
import com.quanjiakan.util.common.LogUtil;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/10/26.
 */

public class HouseKeeperOrderListPresenter implements IBasePresenter {
    public void getHouseKeeperOrderList(final HouseKeeperOrderListActivity activityMvp) {
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.HOUSE_KEEPER_ORDER_LIST);
        if (params == null) {
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_ORDER_LIST, 200, activityMvp.getString(R.string.error_common_net_invalid_parameters));
            return;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_ORDER_LIST);
        //
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_APP);
        RxGetHouseKeeperOrderListService rxGetRequest = retrofit.create(RxGetHouseKeeperOrderListService.class);
        rxGetRequest.doGetHouseKeeperOrderList(params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_MEMBERID),
                params.get(IParamsName.PARAMS_COMMON_PAGE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_ORDER_LIST);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_ORDER_LIST);
                        activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_ORDER_LIST, 200, e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("HOUSE_KEEPER_ORDER_LIST:"+response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_ORDER_LIST);
                        activityMvp.onSuccess(IPresenterBusinessCode.HOUSE_KEEPER_ORDER_LIST, 200, response);
                    }
                });
    }
}
