package com.quanjiakan.activity.base;

import android.view.View;

/**
 * Created by Administrator on 2017/9/6.
 */

public interface IBaseActivity {
    /**
     *
     * @param type 都是引用自IPresenterBusinessCode中的值，用于标识特定的业务，进行分别的处理
     * @return
     */
    Object getParamter(int type);
    void showMyDialog(int type);
    void dismissMyDialog(int type);
    void onSuccess(int type,int httpResponseCode,Object result);
    void onError(int type,int httpResponseCode,Object errorMsg);
    View getViewComponentByID(int viewID);
}
