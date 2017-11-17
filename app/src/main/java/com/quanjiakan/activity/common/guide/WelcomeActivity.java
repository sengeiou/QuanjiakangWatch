package com.quanjiakan.activity.common.guide;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.ICommonSharePreferencesKey;
import com.quanjiakan.activity.common.login.SigninActivity_mvp;
import com.quanjiakan.activity.common.main.MainActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.backpage)
    ImageView backpage;

    private PackageManager packageManager = null;
    private PackageInfo packageInfo = null;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            jumpPath();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        ButterKnife.bind(this);
        initPackageInfo();
    }

    public void initPackageInfo(){
        packageManager = getPackageManager();
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void jumpPath() {
        String version = BaseApplication.getInstances().getKeyValue(ICommonSharePreferencesKey.KEY_ACCESSED_WELCOME);

        if (version == null || "".equals(version) || packageInfo.versionCode > Integer.parseInt(version)) {
            //TODO 如果更新了，将重新进入一次引导页
            Intent intent = new Intent(WelcomeActivity.this, GuidePageActivity.class);
            startActivity(intent);
        } else {
            //TODO
            if (!BaseApplication.getInstances().isLogin()) {
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
        /**
         * 延迟一秒钟进行跳转
         */
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
}
