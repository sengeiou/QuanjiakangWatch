package com.quanjiakan.activity.common.login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.QuanjiakanSetting;
import com.quanjiakan.activity.common.main.MainActivity;
import com.quanjiakan.watch.R;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.backpage)
    ImageView backpage;
    private PackageManager packageManager = null;
    private PackageInfo packageInfo = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        ButterKnife.bind(this);

        packageManager = getPackageManager();
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initView();
        }
    };


    public void initView() {
        String version = QuanjiakanSetting.getInstance().getValue("isyindaoye");
        if (version == null || "".equals(version) || packageInfo.versionCode > Integer.parseInt(version)) {
            Intent intent = new Intent(WelcomeActivity.this, GuidePageActivity.class);
            startActivity(intent);
        } else {
            if (QuanjiakanSetting.getInstance().getUserId() == 0 || "0".equals(BaseApplication.getInstances().getUser_id())) {
                Intent intent = new Intent(WelcomeActivity.this, SigninActivity_mvp.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());

        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mHandler.sendEmptyMessage(1);
            }
        }, 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
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
