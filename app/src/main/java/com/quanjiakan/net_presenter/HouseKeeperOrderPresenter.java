package com.quanjiakan.net_presenter;

import com.pingantong.main.R;
import com.quanjiakan.activity.common.index.housekeeper.HouseKeeperOrderActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.service.RxGetHouseKeeperOrderStringService;
import com.quanjiakan.net.retrofit.service.RxGetOrderVerifyStringService;
import com.quanjiakan.util.common.LogUtil;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/10.
 *
 *
 http://pay.quanjiakan.com:7080/familycore-pay/core/api_get?
 devicetype=0&
 platform=2&
 client=1&
 token=849f2bbb87dc5c049f20b85be3047e3b&
 user_id=11303&
 code=pay&
 action=housekeeper_produceorder&
 data={"note":"","housekeeperId":38,"companyId":7,"begindate":"2017-11-10","enddate":"2017-11-10","orderUserName":"发古板","mobile":"13212345678","address":"复活币","orderUserId":11303,"userId":11303,"paymentChannel":1}

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

        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_PAY);
        RxGetHouseKeeperOrderStringService rxGetRequest = retrofit.create(RxGetHouseKeeperOrderStringService.class);
        rxGetRequest.doGetHouseKeeperOrderString(params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_USERID),
                params.get(IParamsName.PARAMS_COMMON_DATA)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
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
                    public void onNext(String response) {
                        LogUtil.e("HOUSE_KEEPER_ORDER" + response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_ORDER);
                        activityMvp.onSuccess(IPresenterBusinessCode.HOUSE_KEEPER_ORDER, 200, response);
                    }
                });
    }

    public void verifyAliPaymentResult(final HouseKeeperOrderActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT);
        if (params == null) {
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT, 200, activityMvp.getString(R.string.error_common_net_invalid_parameters));
            return;
        }
        //
        activityMvp.showMyDialog(IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT);

        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_APP);
        RxGetOrderVerifyStringService rxGetRequest = retrofit.create(RxGetOrderVerifyStringService.class);
        rxGetRequest.doGetHouseKeeperOrderString(params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_USERID),
                params.get(IParamsName.PARAMS_COMMON_DATA)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT);
                        activityMvp.onError(IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT, 200, e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("ALI_PAY_VERIFY_RESULT" + response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT);
                        activityMvp.onSuccess(IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT, 200, response);
                    }
                });
    }

    public void verifyWechatPaymentResult(final HouseKeeperOrderActivity activityMvp){
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT);
        if (params == null) {
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT, 200, activityMvp.getString(R.string.error_common_net_invalid_parameters));
            return;
        }
        //
        activityMvp.showMyDialog(IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT);

        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_APP);
        RxGetOrderVerifyStringService rxGetRequest = retrofit.create(RxGetOrderVerifyStringService.class);
        rxGetRequest.doGetHouseKeeperOrderString(params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_USERID),
                params.get(IParamsName.PARAMS_COMMON_DATA)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT);
                        activityMvp.onError(IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT, 200, e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("WECHAT_PAY_VERIFY_RESULT" + response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT);
                        activityMvp.onSuccess(IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT, 200, response);
                    }
                });
    }
}
