package com.quanjiakan.net.retrofit.service.get.string;

import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/22 0022.
 * <p>
 * http://device.quanjiakan.com/
 * devices/api?
 * handler=elder&action=healthreport&
 * platform=2&
 * token=0f2e12393c171091337ac6956cc4d1a1&
 * IMEI=866333030025652&
 * histime=2017.12.27
 * <p>
 * <p>
 * PARAMS_HEALTH_REPORT_HISTORY_TIME
 */

public interface RxGetDeviceHeartRateBloodPressureService {

    @GET("/" + IHttpUrlConstants.PROJECT_DEVICES + "/" + IHttpUrlConstants.REQUEST_GET + "?handler=elder&action=healthreport")
    rx.Observable<String> doGetHeartRateAndBloodPressure(@Query(IParamsName.PARAMS_COMMON_PLATFORM) String platform,
                                                         @Query(IParamsName.PARAMS_COMMON_TOKEN) String token,
                                                         @Query(IParamsName.PARAMS_COMMON_IMEI_BIG) String imei,
                                                         @Query(IParamsName.PARAMS_HEALTH_REPORT_HISTORY_TIME) String histime
    );
}
