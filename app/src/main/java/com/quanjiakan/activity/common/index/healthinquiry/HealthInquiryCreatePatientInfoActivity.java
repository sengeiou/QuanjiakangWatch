package com.quanjiakan.activity.common.index.healthinquiry;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.ICommonActivityResultCode;
import com.quanjiakan.db.entity.HealthInquiryPatientDataEntity;
import com.quanjiakan.db.manager.DaoManager;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/16.
 */

public class HealthInquiryCreatePatientInfoActivity extends BaseActivity {


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
    @BindView(R.id.layout_name)
    RelativeLayout layoutName;
    @BindView(R.id.tv_gender_key)
    TextView tvGenderKey;
    @BindView(R.id.rbtn_1)
    RadioButton rbtn1;
    @BindView(R.id.rbtn_2)
    RadioButton rbtn2;
    @BindView(R.id.rgp)
    RadioGroup rgp;
    @BindView(R.id.layout_gender)
    RelativeLayout layoutGender;
    @BindView(R.id.tv_birth_key)
    TextView tvBirthKey;
    @BindView(R.id.et_birth_value)
    EditText etBirthValue;
    @BindView(R.id.layout_birth)
    RelativeLayout layoutBirth;
    @BindView(R.id.layout_new)
    LinearLayout layoutNew;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private List<HealthInquiryPatientDataEntity> mData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * SendProblemActivity收到成功的回调后需要执行一个刷新的操作来保证数据的及时更新
         */
        setContentView(R.layout.layout_create_patient_data);
        ButterKnife.bind(this);
        initTitle();
        initView();
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

    @OnClick({R.id.ibtn_back, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back: {
                finish();
                break;
            }
            case R.id.tv_submit: {
                if(checkValue()){//
                    saveData();
                }
                break;
            }
        }
    }

    public void initTitle(){
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setText(R.string.health_inquiry_create_patient_info_title);

        ibtnMenu.setVisibility(View.GONE);
        menuText.setVisibility(View.GONE);
    }

    public void initView(){
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

    /**
     * 返回true 则代表填入的数据符合要求
     * @return
     */
    public boolean checkValue(){
        if(etNameValue.length()<1){
            CommonDialogHint.getInstance().showHint(this,getString(R.string.health_inquiry_create_patient_info_name_hint));
            return false;
        }

        if(etBirthValue.length()<1){
            CommonDialogHint.getInstance().showHint(this,getString(R.string.health_inquiry_create_patient_info_age_hint));
            return false;
        }

        //TODO 检查数据库中是否存在重复数据
        mData.clear();
        List<HealthInquiryPatientDataEntity> temp = DaoManager.getInstances(this).getDaoSession().getHealthInquiryPatientDataEntityDao().loadAll();
        //TODO 过滤出属于当前用户的数据
        for(HealthInquiryPatientDataEntity entity:temp){
            if(BaseApplication.getInstances().getLoginInfo().getUserId().equals(entity.getBelongUserid())){
                mData.add(entity);
            }
        }

        if(mData.size()<1){
            return true;
        }

        for(HealthInquiryPatientDataEntity tempEntity:mData){
            if(tempEntity.equals(etNameValue.getText().toString(),
                    (rbtn1.isChecked()?
                            getString(R.string.health_inquiry_create_patient_info_male):
                            getString(R.string.health_inquiry_create_patient_info_female) ),
                    etBirthValue.getText().toString(),BaseApplication.getInstances().getLoginInfo().getUserId())){
                return false;
            }
        }

        return true;
    }

    /**
     * 暂时保存在本地，
     */
    public void saveData(){
        HealthInquiryPatientDataEntity temp = new HealthInquiryPatientDataEntity();
        temp.setAge(etBirthValue.getText().toString());
        temp.setName(etNameValue.getText().toString());
        temp.setGender((rbtn1.isChecked()?
                getString(R.string.health_inquiry_create_patient_info_male):
                getString(R.string.health_inquiry_create_patient_info_female) ));
        temp.setBelongUserid(BaseApplication.getInstances().getLoginInfo().getUserId());

        DaoManager.getInstances(this).getDaoSession().getHealthInquiryPatientDataEntityDao().insert(temp);

        setResult(ICommonActivityResultCode.RELOAD_DATA);
        finish();
    }
}
