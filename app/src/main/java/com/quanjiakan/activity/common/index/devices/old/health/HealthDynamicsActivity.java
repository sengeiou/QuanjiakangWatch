package com.quanjiakan.activity.common.index.devices.old.health;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.ISetFragmentBeforeShow;
import com.quanjiakan.activity.common.index.devices.old.health.bloodpressure.BloodPressureFragment;
import com.quanjiakan.activity.common.index.devices.old.health.heartrate.HeartRateFragment;
import com.quanjiakan.activity.common.index.devices.old.health.report.HealthReportFragment;
import com.quanjiakan.activity.common.index.devices.old.health.steps.StepFragment;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.rbt_report)
    RadioButton rbtReport;
    @BindView(R.id.rbt_step)
    RadioButton rbtStep;
    @BindView(R.id.rbt_heartrate)
    RadioButton rbtHeartrate;
    @BindView(R.id.rbt_blood)
    RadioButton rbtBlood;
    @BindView(R.id.rg_health)
    RadioGroup rgHealth;
    @BindView(R.id.tabline)
    LinearLayout tabline;
    @BindView(R.id.ibtn_menu)
    ImageButton ibtnMenu;
    @BindView(R.id.menu_text)
    TextView menuText;


    private String IMEI;

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;

    private HealthReportFragment fragmentReport;
    private StepFragment fragmentStep;
    private HeartRateFragment fragmentHeartRate;
    private BloodPressureFragment fragmentBloodPressure;
    private int currentFragment = ICommonData.DEVICE_HEALTH_DYNAMICS_NONE;

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

        initView();
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

        ibtnMenu.setVisibility(View.GONE);
        menuText.setVisibility(View.GONE);
    }

    public void initView() {

        //TODO 设置显示文字
        rbtReport.setText(R.string.device_health_dynamics_title_tab_report);
        rbtStep.setText(R.string.device_health_dynamics_title_tab_step);
        rbtHeartrate.setText(R.string.device_health_dynamics_title_tab_heartrate);
        rbtBlood.setText(R.string.device_health_dynamics_title_tab_bloodpressure);

        //TODO 设置切换动作
        rgHealth.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbt_report: {
                        //TODO 加载对应的Fragment
                        setCurrentTabFragment(ICommonData.DEVICE_HEALTH_DYNAMICS_REPORT,null);
                        break;
                    }
                    case R.id.rbt_step: {
                        setCurrentTabFragment(ICommonData.DEVICE_HEALTH_DYNAMICS_STEP,null);
                        break;
                    }
                    case R.id.rbt_heartrate: {
                        setCurrentTabFragment(ICommonData.DEVICE_HEALTH_DYNAMICS_HEARTRATE,null);
                        break;
                    }
                    case R.id.rbt_blood: {
                        setCurrentTabFragment(ICommonData.DEVICE_HEALTH_DYNAMICS_BLOOD_PRESSURE,null);
                        break;
                    }

                }
            }
        });

        //TODO 设置默认选中项
        rbtReport.setChecked(true);//TODO 虽然效果显示了，但Fragment没有展示出来
        setCurrentTabFragment(ICommonData.DEVICE_HEALTH_DYNAMICS_REPORT,null);//TODO 避免由于RadioButton.setChecked()无效导致的Fragment不显示
    }

    public void setCurrentTabFragment(int item, ISetFragmentBeforeShow fragmentBeforeShow) {
        //TODO 特定类型的事务
        switch (item) {
            case ICommonData.DEVICE_HEALTH_DYNAMICS_REPORT: {

                if (currentFragment == ICommonData.DEVICE_HEALTH_DYNAMICS_REPORT) {
                    return;
                }
                startFragmentTransaction();
                hideOther(currentFragment);
                startFragmentTransaction();
                if (fragmentReport == null) {
                    fragmentReport = new HealthReportFragment();
                    if (fragmentBeforeShow != null) {
                        fragmentBeforeShow.setFragmentValue();
                    }
                    transaction.add(R.id.viewContainer, fragmentReport, ICommonData.DEVICE_HEALTH_DYNAMICS_REPORT_TAG);
                } else {
                    fragmentReport = (HealthReportFragment) fragmentManager.findFragmentByTag(ICommonData.DEVICE_HEALTH_DYNAMICS_REPORT_TAG);
                    if (fragmentBeforeShow != null) {
                        fragmentBeforeShow.setFragmentValue();
                    }
                    transaction.show(fragmentReport);
                }
                fragmentReport.setIMEI(IMEI);

                currentFragment = ICommonData.DEVICE_HEALTH_DYNAMICS_REPORT;
                transaction.commit();
                break;
            }
            case ICommonData.DEVICE_HEALTH_DYNAMICS_STEP: {
                if (currentFragment == ICommonData.DEVICE_HEALTH_DYNAMICS_STEP) {
                    return;
                }
                startFragmentTransaction();
                hideOther(currentFragment);
                startFragmentTransaction();
                if (fragmentStep == null) {
                    fragmentStep = new StepFragment();
                    if (fragmentBeforeShow != null) {
                        fragmentBeforeShow.setFragmentValue();
                    }
                    transaction.add(R.id.viewContainer, fragmentStep, ICommonData.DEVICE_HEALTH_DYNAMICS_STEP_TAG);
                } else {
                    fragmentStep = (StepFragment) fragmentManager.findFragmentByTag(ICommonData.DEVICE_HEALTH_DYNAMICS_STEP_TAG);
                    if (fragmentBeforeShow != null) {
                        fragmentBeforeShow.setFragmentValue();
                    }
                    transaction.show(fragmentStep);
                }
                fragmentStep.setIMEI(IMEI);
                currentFragment = ICommonData.DEVICE_HEALTH_DYNAMICS_STEP;
                transaction.commit();
                break;
            }
            case ICommonData.DEVICE_HEALTH_DYNAMICS_HEARTRATE: {
                if (currentFragment == ICommonData.DEVICE_HEALTH_DYNAMICS_HEARTRATE) {
                    return;
                }
                startFragmentTransaction();
                hideOther(currentFragment);
                startFragmentTransaction();
                if (fragmentHeartRate == null) {
                    fragmentHeartRate = new HeartRateFragment();
                    if (fragmentBeforeShow != null) {
                        fragmentBeforeShow.setFragmentValue();
                    }
                    transaction.add(R.id.viewContainer, fragmentHeartRate, ICommonData.DEVICE_HEALTH_DYNAMICS_HEARTRATE_TAG);
                } else {
                    fragmentHeartRate = (HeartRateFragment) fragmentManager.findFragmentByTag(ICommonData.DEVICE_HEALTH_DYNAMICS_HEARTRATE_TAG);
                    if (fragmentBeforeShow != null) {
                        fragmentBeforeShow.setFragmentValue();
                    }
                    transaction.show(fragmentHeartRate);
                }
                fragmentHeartRate.setIMEI(IMEI);
                currentFragment = ICommonData.DEVICE_HEALTH_DYNAMICS_HEARTRATE;
                transaction.commit();
                break;
            }
            case ICommonData.DEVICE_HEALTH_DYNAMICS_BLOOD_PRESSURE: {
                if (currentFragment == ICommonData.DEVICE_HEALTH_DYNAMICS_BLOOD_PRESSURE) {
                    return;
                }
                startFragmentTransaction();
                hideOther(currentFragment);
                startFragmentTransaction();
                if (fragmentBloodPressure == null) {
                    fragmentBloodPressure = new BloodPressureFragment();
                    if (fragmentBeforeShow != null) {
                        fragmentBeforeShow.setFragmentValue();
                    }
                    transaction.add(R.id.viewContainer, fragmentBloodPressure, ICommonData.DEVICE_HEALTH_DYNAMICS_BLOOD_PRESSURE_TAG);
                } else {
                    fragmentBloodPressure = (BloodPressureFragment) fragmentManager.findFragmentByTag(ICommonData.DEVICE_HEALTH_DYNAMICS_BLOOD_PRESSURE_TAG);
                    if (fragmentBeforeShow != null) {
                        fragmentBeforeShow.setFragmentValue();
                    }
                    transaction.show(fragmentBloodPressure);
                }
                fragmentBloodPressure.setIMEI(IMEI);
                currentFragment = ICommonData.DEVICE_HEALTH_DYNAMICS_BLOOD_PRESSURE;
                transaction.commit();
                break;
            }
        }
    }

    public void hideOther(int item) {
        switch (item) {
            case ICommonData.DEVICE_HEALTH_DYNAMICS_REPORT: {
                if (fragmentReport != null) {
                    fragmentReport = (HealthReportFragment) fragmentManager.findFragmentByTag(ICommonData.DEVICE_HEALTH_DYNAMICS_REPORT_TAG);
                    transaction.hide(fragmentReport);
                    transaction.commit();
                } else {
                    transaction.commit();
                }
                break;
            }
            case ICommonData.DEVICE_HEALTH_DYNAMICS_STEP: {
                if (fragmentStep != null) {
                    fragmentStep = (StepFragment) fragmentManager.findFragmentByTag(ICommonData.DEVICE_HEALTH_DYNAMICS_STEP_TAG);
                    transaction.hide(fragmentStep);
                    transaction.commit();
                } else {
                    transaction.commit();
                }
                break;
            }
            case ICommonData.DEVICE_HEALTH_DYNAMICS_HEARTRATE: {
                if (fragmentHeartRate != null) {
                    fragmentHeartRate = (HeartRateFragment) fragmentManager.findFragmentByTag(ICommonData.DEVICE_HEALTH_DYNAMICS_HEARTRATE_TAG);
                    transaction.hide(fragmentHeartRate);
                    transaction.commit();
                } else {
                    transaction.commit();
                }
                break;
            }
            case ICommonData.DEVICE_HEALTH_DYNAMICS_BLOOD_PRESSURE: {
                if (fragmentBloodPressure != null) {
                    fragmentBloodPressure = (BloodPressureFragment) fragmentManager.findFragmentByTag(ICommonData.DEVICE_HEALTH_DYNAMICS_BLOOD_PRESSURE_TAG);
                    transaction.hide(fragmentBloodPressure);
                    transaction.commit();
                } else {
                    transaction.commit();
                }
                break;
            }
        }
    }

    public void startFragmentTransaction() {
        //TODO 准备开始Fragment事务
        if (fragmentManager == null) {
            fragmentManager = getFragmentManager();
        }
        transaction = fragmentManager.beginTransaction();
    }


    @OnClick({R.id.ibtn_back, R.id.ibtn_menu, R.id.menu_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.ibtn_menu:
                break;
            case R.id.menu_text:
                break;
        }
    }
}
