package com.quanjiakan.net;

import com.quanjiakan.net.retrofit.depandence.StringConverterFactory;
import com.quanjiakan.net.retrofit.depandence.StringConverterFactory2;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/9/6.
 */

public class Retrofit2Util {
    private static final String baseUrl = IHttpUrlConstants.BASEURL_QUANJIAKANG;

    //TODO 返回的数据需要使用对象格式的数据【本身数据格式为JSON】，返回后会自动序列化成对象
    public static Retrofit getRetrofit(String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    public static Retrofit getRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    //TODO 返回的数据直接为String格式，需要自己的进行数据的序列化操作，能够更灵活，但相对需要更多的处理
    public static Retrofit getRetrofitStringResult(String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(StringConverterFactory2.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }
}
