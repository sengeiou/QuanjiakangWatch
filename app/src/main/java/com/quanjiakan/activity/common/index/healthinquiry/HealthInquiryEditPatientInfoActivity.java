package com.quanjiakan.activity.common.index.healthinquiry;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.constants.ICommonActivityResultCode;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.db.entity.HealthInquiryPatientDataEntity;
import com.quanjiakan.db.manager.DaoManager;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/16.
 */

public class HealthInquiryEditPatientInfoActivity extends BaseActivity {

    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ibtn_menu)
    ImageButton ibtnMenu;
    @BindView(R.id.menu_text)
    TextView menuText;
    //*****************************************
    @BindView(R.id.tv_name_key)
    TextView tvNameKey;
    @BindView(R.id.et_name_value)
    EditText etNameValue;
    @BindView(R.id.tv_gender_key)
    TextView tvGenderKey;
    @BindView(R.id.rbtn_1)
    RadioButton rbtn1;
    @BindView(R.id.rbtn_2)
    RadioButton rbtn2;
    @BindView(R.id.tv_birth_key)
    TextView tvBirthKey;
    @BindView(R.id.et_birth_value)
    EditText etBirthValue;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private HealthInquiryPatientDataEntity entity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_create_patient_data);
        ButterKnife.bind(this);
        initTitle();
        initView();
        entity = (HealthInquiryPatientDataEntity) getIntent().getSerializableExtra(IParamsName.PARAMS_COMMON_DATA);
        if (entity == null) {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_error_activity_parameters), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            return;
        }
        resetView();
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

    public void initTitle() {
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setText(R.string.health_inquiry_create_patient_info_title);

        ibtnMenu.setVisibility(View.GONE);
        menuText.setVisibility(View.GONE);
    }

    public void initView() {
        //*************************************
        tvNameKey.setText(R.string.health_inquiry_create_patient_info_name);
        etNameValue.setHint(R.string.health_inquiry_create_patient_info_name_hint);
        //*************************************
        tvGenderKey.setText(R.string.health_inquiry_create_patient_info_gender);
        rbtn1.setText(R.string.health_inquiry_create_patient_info_male);
        rbtn2.setText(R.string.health_inquiry_create_patient_info_female);

        rbtn1.setChecked(true);//默认选男
        rbtn2.setChecked(false);
        //*************************************
        tvBirthKey.setText(R.string.health_inquiry_create_patient_info_age);
        etBirthValue.setHint(R.string.health_inquiry_create_patient_info_age_hint);
        //*************************************
        tvSubmit.setText(R.string.common_confirm);
    }

    public void resetView() {
        etNameValue.setText(entity.getName());

        if (getString(R.string.health_inquiry_create_patient_info_male).equals(entity.getGender())) {
            rbtn1.setChecked(true);//默认选男
            rbtn2.setChecked(false);
        } else {
            rbtn1.setChecked(false);//默认选男
            rbtn2.setChecked(true);
        }

        etBirthValue.setText(entity.getAge());
    }

    @OnClick({R.id.ibtn_back, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back: {
                finish();
                break;
            }
            case R.id.tv_submit: {
                save();
                break;
            }
        }
    }

    public void save(){
        if(isValidValue()){
            entity.setName(etNameValue.getText().toString());
            entity.setGender((rbtn1.isChecked()?
                    getString(R.string.health_inquiry_create_patient_info_male):
                    getString(R.string.health_inquiry_create_patient_info_female)));
            entity.setAge(etBirthValue.getText().toString());
            entity.setBelongUserid(BaseApplication.getInstances().getLoginInfo().getUserId());

            DaoManager.getInstances(this).getDaoSession().getHealthInquiryPatientDataEntityDao().update(entity);

            setResult(ICommonActivityResultCode.RELOAD_DATA);
            finish();
        }else{
            CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_health_inquiry_no_change));
        }
    }

    public boolean isValidValue(){
        if(entity.equals(etNameValue.getText().toString(),
                (rbtn1.isChecked()?
                    getString(R.string.health_inquiry_create_patient_info_male):
                    getString(R.string.health_inquiry_create_patient_info_female)),
                    etBirthValue.getText().toString(),
                BaseApplication.getInstances().getLoginInfo().getUserId())){
           return false;
        }
        return true;
    }
}
