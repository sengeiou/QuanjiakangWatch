package com.quanjiakan.net.retrofit.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2017/10/30.
 */

public interface RxCommonGetUrlStringService {
    @GET
    Observable<String> setUrl(@Url String urlPath);
}
