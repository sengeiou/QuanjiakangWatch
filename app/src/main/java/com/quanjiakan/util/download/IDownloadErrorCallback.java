package com.quanjiakan.util.download;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public interface IDownloadErrorCallback {
    void onError();
    void onError(int type);
}
