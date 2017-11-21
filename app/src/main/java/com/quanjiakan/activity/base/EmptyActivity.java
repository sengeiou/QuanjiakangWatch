package com.quanjiakan.activity.base;

import android.os.Bundle;

import com.pingantong.main.R;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/11/20.
 */

public class EmptyActivity extends BaseActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_empty);

//        CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_error_activity_parameters), new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        return;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public Object getParamter(int type) {
        switch (type){
            case IPresenterBusinessCode.EMPTY:
                HashMap<String,String> params = new HashMap<>();
                return params;
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type){
            case IPresenterBusinessCode.EMPTY:
                getDialog(this,getString(R.string.hint_common_get_data));//正在获取数据...
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type){
            case IPresenterBusinessCode.EMPTY:
                break;
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type){
            case IPresenterBusinessCode.EMPTY:
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type){
            case IPresenterBusinessCode.EMPTY:
                break;
        }
        if(errorMsg!=null && errorMsg.toString().length()>0){
            CommonDialogHint.getInstance().showHint(this,errorMsg.toString());
        }else{
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_common_net_request_fail));
        }
    }
}
