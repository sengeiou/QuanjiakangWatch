package com.quanjiakan.activity.common.index.devices.old.health;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/20.
 */

public class HealthDynamicsActivity extends BaseActivity {

    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.viewContainer)
    FrameLayout viewContainer;


    private String IMEI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_health_dynamics);
        ButterKnife.bind(this);
        //TODO 首要的参数，用于获取设备的信息---由于Activity仅为容器，实际展示主要还是由Fragment承担
        initTitle();
        IMEI = getIntent().getStringExtra(IParamsName.PARAMS_DEVICE_ID);
        if (IMEI == null | IMEI.length() < 1) {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_error_activity_parameters), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            return;
        }

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

    private void initTitle() {
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setText(R.string.device_health_dynamics_title);
    }



}
