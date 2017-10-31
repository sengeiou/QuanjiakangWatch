package com.quanjiakan.net_presenter;

import com.quanjiakan.activity.common.index.housekeeper.HouseKeeperListActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperListEntity;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperTypeListEntity;
import com.quanjiakan.net.retrofit.service.RxGetHouseKeeperListService;
import com.quanjiakan.net.retrofit.service.RxGetHouseKeeperTypeListService;
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
    public void getHouseKeeperListWithoutPosition(final HouseKeeperListActivity activityMvp) {
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.HOUSE_KEEPER_LIST);
        if (params == null) {
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_LIST, 200, activityMvp.getString(R.string.error_common_net_invalid_parameters));
            return;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST);

        Retrofit retrofit = Retrofit2Util.getRetrofit(IHttpUrlConstants.BASEURL_APP);
        RxGetHouseKeeperListService rxGetRequest = retrofit.create(RxGetHouseKeeperListService.class);

        if (params.containsKey(IParamsName.PARAMS_HOUSE_KEEPER_TYPE)) {
            rxGetRequest.doGetWatchListWithoutLocation(params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                    params.get(IParamsName.PARAMS_COMMON_TOKEN),
                    params.get(IParamsName.PARAMS_HOUSE_KEEPER_TYPE),
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
                            activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_LIST, 200, e.getMessage());
                        }

                        @Override
                        public void onNext(GetHouseKeeperListEntity response) {
                            LogUtil.e("HOUSE_KEEPER_LIST");
                            activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST);
                            activityMvp.onSuccess(IPresenterBusinessCode.HOUSE_KEEPER_LIST, 200, response);
                        }
                    });
        } else {
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
                            activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_LIST, 200, e.getMessage());
                        }

                        @Override
                        public void onNext(GetHouseKeeperListEntity response) {
                            LogUtil.e("HOUSE_KEEPER_LIST");
                            activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST);
                            activityMvp.onSuccess(IPresenterBusinessCode.HOUSE_KEEPER_LIST, 200, response);
                        }
                    });
        }
    }

    public void getHouseKeeperListWithPosition(final HouseKeeperListActivity activityMvp) {
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION);
        if (params == null) {
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION, 200, activityMvp.getString(R.string.error_common_net_invalid_parameters));
            return;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION);


        //TODO 实际中，根据 省市区 三个部分是否存在，仍然需要判断使用哪个方法（即是否含有对应字段的方法）
        Retrofit retrofit = Retrofit2Util.getRetrofit(IHttpUrlConstants.BASEURL_APP);
        RxGetHouseKeeperListService rxGetRequest = retrofit.create(RxGetHouseKeeperListService.class);

        if (params.containsKey(IParamsName.PARAMS_HOUSE_KEEPER_TYPE)) {
            rxGetRequest.doGetWatchListWithoutLocation(params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                    params.get(IParamsName.PARAMS_COMMON_TOKEN),
                    params.get(IParamsName.PARAMS_HOUSE_KEEPER_TYPE),
                    params.get(IParamsName.PARAMS_COMMON_PAGE),
                    params.get(IParamsName.PARAMS_HOUSE_KEEPER_PROVINCE),
                    params.get(IParamsName.PARAMS_HOUSE_KEEPER_CITY),
                    params.get(IParamsName.PARAMS_HOUSE_KEEPER_DIST))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GetHouseKeeperListEntity>() {
                        @Override
                        public void onCompleted() {
                            activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION);
                        }

                        @Override
                        public void onError(Throwable e) {
                            activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION);
                            activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION, 200, e.getMessage());
                        }

                        @Override
                        public void onNext(GetHouseKeeperListEntity response) {
                            LogUtil.e("HOUSE_KEEPER_LIST_WITH_LOCATION" + response);
                            activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION);
                            activityMvp.onSuccess(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION, 200, response);
                        }
                    });
        } else {
            rxGetRequest.doGetWatchListWithoutLocation(params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                    params.get(IParamsName.PARAMS_COMMON_TOKEN),
                    params.get(IParamsName.PARAMS_COMMON_PAGE),
                    params.get(IParamsName.PARAMS_HOUSE_KEEPER_PROVINCE),
                    params.get(IParamsName.PARAMS_HOUSE_KEEPER_CITY),
                    params.get(IParamsName.PARAMS_HOUSE_KEEPER_DIST))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GetHouseKeeperListEntity>() {
                        @Override
                        public void onCompleted() {
                            activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION);
                        }

                        @Override
                        public void onError(Throwable e) {
                            activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION);
                            activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION, 200, e.getMessage());
                        }

                        @Override
                        public void onNext(GetHouseKeeperListEntity response) {
                            LogUtil.e("HOUSE_KEEPER_LIST_WITH_LOCATION" + response);
                            activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION);
                            activityMvp.onSuccess(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION, 200, response);
                        }
                    });
        }

        /**
         http://app.quanjiakan.com/pingan/api?handler=services&action=housekeeperlist&
         platform=2&
         token=86daeff8e1a9a345efbf667ab93fb94d&
         page=1&
         housekeeperType=1&
         serviceCity=%E5%B9%BF%E5%B7%9E&
         serviceProvince=%E5%B9%BF%E4%B8%9C&
         serviceDist=%E8%8D%94%E6%B9%BE%E5%8C%BA&
         rows=15
         */

        //TODO 访问成功过，且数据获取到了
//        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_APP);
//        RxGetHouseKeeperListStringService rxGetRequest = retrofit.create(RxGetHouseKeeperListStringService.class);
//        rxGetRequest.doGetWatchListWithoutLocation(params.get(IParamsName.PARAMS_COMMON_PLATFORM),
//                params.get(IParamsName.PARAMS_COMMON_TOKEN),
//                params.get(IParamsName.PARAMS_COMMON_PAGE),
//                params.get(IParamsName.PARAMS_HOUSE_KEEPER_PROVINCE),
//                params.get(IParamsName.PARAMS_HOUSE_KEEPER_CITY),
//                params.get(IParamsName.PARAMS_HOUSE_KEEPER_DIST))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION);
//                        activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION,200,e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(String response) {
//                        LogUtil.e("HOUSE_KEEPER_LIST_WITH_LOCATION"+response);
//                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION);
//                        activityMvp.onSuccess(IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION,200,response);
//                    }
//                });

        //TODO Get 请求可以这么做---然后自己去进行序列化得到相应的对象（仅限Get请求）
//        Retrofit retrofit2 = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_APP);
//        RxCommonGetUrlStringService rxCommonGetUrlStringService = retrofit2.create(RxCommonGetUrlStringService.class);
//        rxCommonGetUrlStringService.setUrl("http://app.quanjiakan.com/pingan/api?handler=services&action=housekeeperlist&platform=2&token=86daeff8e1a9a345efbf667ab93fb94d&page=1&serviceCity=%E5%B9%BF%E5%B7%9E&serviceProvince=%E5%B9%BF%E4%B8%9C&serviceDist=%E8%8D%94%E6%B9%BE%E5%8C%BA&rows=5")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//                        LogUtil.e("onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtil.e("onError");
//                    }
//
//                    @Override
//                    public void onNext(String response) {
//                        LogUtil.e("onNext:\n"+response);
//                    }
//                });

        //TODO 可用，但针对请求似乎存在问题
//        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_APP);
//        RxGetHouseKeeperListStringService rxGetRequest = retrofit.create(RxGetHouseKeeperListStringService.class);
//        rxGetRequest.doGetWatchListWithoutLocation(params.get(IParamsName.PARAMS_COMMON_PLATFORM),
//                params.get(IParamsName.PARAMS_COMMON_TOKEN),
//                params.get(IParamsName.PARAMS_COMMON_PAGE),
//                params.get(IParamsName.PARAMS_HOUSE_KEEPER_PROVINCE),
//                params.get(IParamsName.PARAMS_HOUSE_KEEPER_CITY),
//                params.get(IParamsName.PARAMS_HOUSE_KEEPER_DIST))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onNext(String response) {
//                        Log.e(TAG,response.toString());
//                    }
//                });


        //TODO 这样的话，反而能够正常获取到数据-----[推测Retrofit2在输入参数为中文会自动进行URLEncode操作]
//        HashMap<String ,String> params = new HashMap<>();
//        params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
//        params.put(IParamsName.PARAMS_COMMON_TOKEN, "86daeff8e1a9a345efbf667ab93fb94d");
//        params.put(IParamsName.PARAMS_COMMON_PAGE, 1+"");
//        try {
//            params.put(IParamsName.PARAMS_HOUSE_KEEPER_PROVINCE, "广东");
//            params.put(IParamsName.PARAMS_HOUSE_KEEPER_CITY,"广州");
//            params.put(IParamsName.PARAMS_HOUSE_KEEPER_DIST, "荔湾区");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_APP);
//        RxGetHouseKeeperListStringService rxGetRequest = retrofit.create(RxGetHouseKeeperListStringService.class);
//        rxGetRequest.doGetWatchListWithoutLocation(params.get(IParamsName.PARAMS_COMMON_PLATFORM),
//                params.get(IParamsName.PARAMS_COMMON_TOKEN),
//                params.get(IParamsName.PARAMS_COMMON_PAGE),
//                params.get(IParamsName.PARAMS_HOUSE_KEEPER_PROVINCE),
//                params.get(IParamsName.PARAMS_HOUSE_KEEPER_CITY),
//                params.get(IParamsName.PARAMS_HOUSE_KEEPER_DIST))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onNext(String response) {
//                        Log.e(TAG,response.toString());
//                    }
//                });


    }


    /**
     * http://app.quanjiakan.com/pingan/api?
     * handler=services&
     * action=housekeerserviceslist&
     * memberId=11303&
     * token=3169fe4e2e712790cb00c0ab9e8122a3
     */
    public void getHouseKeeperTypeList(final HouseKeeperListActivity activityMvp) {
        HashMap<String, String> params = (HashMap<String, String>) activityMvp.getParamter(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_LIST);
        if (params == null) {
            //TODO 控制无效的参数，需要针对不同的业务进行区分，当业务本身即为Get,不需要参数时，这个判断与回调也就不需要了
            activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_LIST, 200, activityMvp.getString(R.string.error_common_net_invalid_parameters));
            return;
        }
        activityMvp.showMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_LIST);

        //TODO 实际中，根据 省市区 三个部分是否存在，仍然需要判断使用哪个方法（即是否含有对应字段的方法）
        Retrofit retrofit = Retrofit2Util.getRetrofit(IHttpUrlConstants.BASEURL_APP);
        RxGetHouseKeeperTypeListService rxGetRequest = retrofit.create(RxGetHouseKeeperTypeListService.class);
        rxGetRequest.doGetWatchListWithoutLocation(params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_MEMBERID)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetHouseKeeperTypeListEntity>() {
                    @Override
                    public void onCompleted() {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_LIST);
                    }

                    @Override
                    public void onError(Throwable e) {
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_LIST);
                        activityMvp.onError(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_LIST, 200, e.getMessage());
                    }

                    @Override
                    public void onNext(GetHouseKeeperTypeListEntity response) {
                        LogUtil.e("HOUSE_KEEPER_TYPE_LIST" + response);
                        activityMvp.dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_LIST);
                        activityMvp.onSuccess(IPresenterBusinessCode.HOUSE_KEEPER_TYPE_LIST, 200, response);
                    }
                });
    }
}
