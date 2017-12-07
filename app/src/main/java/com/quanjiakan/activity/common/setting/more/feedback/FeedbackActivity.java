package com.quanjiakan.activity.common.setting.more.feedback;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.format.CommonResultEntity;
import com.quanjiakan.net_presenter.FeedbackPresenter;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
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

public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_mail)
    EditText etMail;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private FeedbackPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_feedback);
        ButterKnife.bind(this);

//        CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_error_activity_parameters), new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        return;

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

    public void initTitle() {
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setText(R.string.setting_feedback_title);

        presenter = new FeedbackPresenter();
    }

    public void initView(){
        etMail.setHint(R.string.setting_feedback_mail);
        etMail.setText("");

        etContent.setHint(R.string.setting_feedback_content);
        etContent.setText("");
    }


    @Override
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.FEEDBACK:
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_MEMBERID, BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_FEEDBACK_EMAIL, etMail.getText().toString());
                params.put(IParamsName.PARAMS_FEEDBACK_CONTENT, etContent.getText().toString());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                return params;
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.FEEDBACK:
                getDialog(this, getString(R.string.hint_common_submit_data));//正在提交数据...
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.FEEDBACK:
                break;
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.FEEDBACK:
                setResult(result);
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.FEEDBACK:
                break;
        }
        if (errorMsg != null && errorMsg.toString().length() > 0) {
            CommonDialogHint.getInstance().showHint(this, errorMsg.toString());
        } else {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_net_request_fail));
        }
    }

    public void setResult(Object result){
        if(result!=null && result instanceof String){
            CommonResultEntity resultEntity = (CommonResultEntity) SerializeToObjectUtil.getInstances().
                    jsonToObject(result.toString(),new TypeToken<CommonResultEntity>(){}.getType());
            if(ICommonData.HTTP_OK.equals(resultEntity.getCode())){
                CommonDialogHint.getInstance().showHint(this, getString(R.string.setting_feedback_commit_success), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                initView();
            }else{
                if(resultEntity.getMessage()!=null && resultEntity.getMessage().length()>0){
                    CommonDialogHint.getInstance().showHint(this, resultEntity.getMessage());
                }else{
                    CommonDialogHint.getInstance().showHint(this, getString(R.string.setting_feedback_commit_fail));

                }
            }
        }
    }

    public boolean isValid(){
        if(etMail.length() == 0 || etContent.length() == 0){
            CommonDialogHint.getInstance().showHint(this,getString(R.string.setting_feedback_check1));
            return false;
        }

        if(!StringCheckUtil.isEmail(etMail.getText().toString())){
            CommonDialogHint.getInstance().showHint(this,getString(R.string.setting_feedback_check2));
            return false;
        }
        return true;
    }

    @OnClick({R.id.ibtn_back, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.tv_submit:
                if(isValid()){
                    presenter.doFeedback(this);
                }
                break;
        }
    }
}
