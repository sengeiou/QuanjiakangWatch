package com.quanjiakan.activity.common.setting.more.modifypassword;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.format.CommonResultEntity;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.net_presenter.ModifyPasswordPresenter;
import com.quanjiakan.util.common.MessageDigestUtil;
import com.quanjiakan.util.common.SerializeToObjectUtil;
import com.quanjiakan.util.common.StringCheckUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/20.
 */

public class ModifyPasswordActivity extends BaseActivity {

    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_newpassword)
    EditText etNewpassword;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.et_confirmnewpassword)
    EditText etConfirmnewpassword;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    /**
     * *************************************************************************************************
     */

    private ModifyPasswordPresenter presenter;


    /**
     * *************************************************************************************************
     */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_password_modify);
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

    /**
     * *************************************************************************************************
     */
    private void initTitle(){
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setText(R.string.setting_password_modify);

        presenter = new ModifyPasswordPresenter();
    }

    private void initView(){
        etUsername.setHint(R.string.setting_password_origin);

        etNewpassword.setHint(R.string.setting_password_new);

        tvNotice.setText(R.string.setting_password_check15);

        etConfirmnewpassword.setHint(R.string.setting_password_check);
    }

    /**
     * *************************************************************************************************
     */

    @Override
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.MODIFY_PASSWORD:
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_MEMBERID,BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN,BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_PASSWORD,BaseApplication.getInstances().getFormatPWString(etNewpassword.getText().toString()));
                return params;
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.MODIFY_PASSWORD:
                getDialog(this, getString(R.string.hint_common_submit_data));//正在获取数据...
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.MODIFY_PASSWORD:
                break;
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.MODIFY_PASSWORD:
                setResult(result);
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.MODIFY_PASSWORD:
                break;
        }
        if (errorMsg != null && errorMsg.toString().length() > 0) {
            CommonDialogHint.getInstance().showHint(this, errorMsg.toString());
        } else {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_net_request_fail));
        }
    }

    /**
     * *************************************************************************************************
     */

    public void setResult(Object result){
        if(result!=null && result instanceof String){
            CommonResultEntity resultEntity = (CommonResultEntity) SerializeToObjectUtil.getInstances().jsonToObject(result.toString(),
                    new TypeToken<CommonResultEntity>(){}.getType());
            if(ICommonData.HTTP_OK.equals(resultEntity.getCode())){
                CommonDialogHint.getInstance().showHint(this, getResources().getString(R.string.setting_password_check31), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseApplication.getInstances().onLogout(ModifyPasswordActivity.this);
                    }
                });
            }else{
                CommonDialogHint.getInstance().showHint(this,getResources().getString(R.string.setting_password_check4));
            }
        }
    }


    @OnClick({R.id.ibtn_back, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.btn_submit:
                if(isValid()){
                    presenter.modifyPassword(this);
                }
                break;
        }
    }

    /**
     * *************************************************************************************************
     */

    public boolean isValid(){
        if(etUsername.length() == 0 || etNewpassword.length() == 0 || etConfirmnewpassword.length() == 0){
            CommonDialogHint.getInstance().showHint(this,getResources().getString(R.string.setting_password_check1));
            return false;
        }
        if(!BaseApplication.getInstances().getLoginInfo().getPasswordDigest().equals(MessageDigestUtil.getSHA1String(etUsername.getText().toString()))){
            CommonDialogHint.getInstance().showHint(this,getResources().getString(R.string.setting_password_origin_error));
            return false;
        }
        if(etNewpassword.getText().toString().trim().length()<6||etNewpassword.getText().toString().trim().length()>15) {
            CommonDialogHint.getInstance().showHint(this,getResources().getString(R.string.hint_new_pw_number_error));
            return false;
        }
        if(etConfirmnewpassword.getText().toString().trim().length()<6||etConfirmnewpassword.getText().toString().trim().length()>15) {
            CommonDialogHint.getInstance().showHint(this,getResources().getString(R.string.hint_comfirm_pw_number_error));
            return false;
        }
        if(!etNewpassword.getText().toString().equals(etConfirmnewpassword.getText().toString())){
            CommonDialogHint.getInstance().showHint(this,getResources().getString(R.string.setting_password_check2));
            return false;
        }
        if(etNewpassword.getText().toString().equals(etUsername.getText().toString())){
            CommonDialogHint.getInstance().showHint(this,getResources().getString(R.string.setting_password_check13));
            return false;
        }
        if(!StringCheckUtil.checkStringType(etNewpassword.getText().toString())){
            CommonDialogHint.getInstance().showHint(this,getResources().getString(R.string.setting_password_check5));
            return false;
        }
        return true;
    }
}
