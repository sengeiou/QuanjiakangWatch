package com.quanjiakan.activity.base;

import android.os.Bundle;
import android.view.View;

import com.pingantong.main.R;
import com.umeng.analytics.MobclickAgent;

/**
 *
 * 类似于模板的Activity，重新创建的Activity可直接复制后，使用
 * Created by Administrator on 2017/10/25.
 */

public class CommonActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_empty);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRestart() {
        super.onRestart();
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
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public Object getParamter(int type) {
        return null;
    }

    @Override
    public void showMyDialog(int type) {

    }

    @Override
    public void dismissMyDialog(int type) {

    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {

    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {

    }

    @Override
    public View getViewComponentByID(int viewID) {
        return null;
    }
}
