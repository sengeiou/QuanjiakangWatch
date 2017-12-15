package com.quanjiakan.activity.common.setting.about;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.common.web.CommonWebActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.callphone.CallPhoneUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/20.
 */

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_version_code)
    TextView tvVersionCode;
    @BindView(R.id.company)
    TextView company;
    @BindView(R.id.developer)
    TextView developer;
    @BindView(R.id.info)
    LinearLayout info;
    @BindView(R.id.develop)
    TextView develop;
    @BindView(R.id.info_line)
    RelativeLayout infoLine;
    @BindView(R.id.tv_wechat)
    TextView tvWechat;
    @BindView(R.id.tv_wechat_value)
    TextView tvWechatValue;
    @BindView(R.id.tv_service)
    TextView tvService;
    @BindView(R.id.tv_service_value)
    TextView tvServiceValue;
    @BindView(R.id.tv_service_value2)
    TextView tvServiceValue2;
    @BindView(R.id.tv_mail)
    TextView tvMail;
    @BindView(R.id.tv_mail_value)
    TextView tvMailValue;
    @BindView(R.id.tv_web)
    TextView tvWeb;
    @BindView(R.id.tv_web_value)
    TextView tvWebValue;


    /**
     * ****************************************************
     * 如果将信息使用接口获取，则构建一个Presenter，通过对应的数据
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_aboutus);
        ButterKnife.bind(this);

        initTitle();
        initView();
    }

    public void initTitle() {
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setText(R.string.setting_about_us_title);
    }

    public void initView() {
        image.setImageResource(R.drawable.ic_launcher);
        tvVersion.setText(R.string.app_name);
        tvVersionCode.setText("");

        tvWechat.setText(R.string.setting_about_us_wechat);
        tvWechatValue.setText(R.string.setting_about_us_wechat_value);

        tvService.setText(R.string.setting_about_us_phone);
        tvServiceValue.setText(R.string.setting_about_us_phone_value);
        tvServiceValue2.setText(R.string.setting_about_us_phone_value2);

        tvMail.setText(R.string.setting_about_us_email);
        tvMailValue.setText(R.string.setting_about_us_email_value);

        tvWeb.setText(R.string.setting_about_us_web);
        tvWebValue.setText(R.string.setting_about_us_web_value);

        company.setText(R.string.setting_about_us_company);
        developer.setText(R.string.setting_about_us_unicom);
        develop.setText(R.string.setting_about_us_coop);
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
     * ***********************************************************
     */

    @Override
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.ABOUT_US_INFO_PHONE:
                HashMap<String, String> params = new HashMap<>();
                return params;
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.ABOUT_US_INFO_PHONE:
                getDialog(this, getString(R.string.hint_common_get_data));//正在获取数据...
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.ABOUT_US_INFO_PHONE:
                break;
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.ABOUT_US_INFO_PHONE:
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.ABOUT_US_INFO_PHONE:
                break;
        }
        if (errorMsg != null && errorMsg.toString().length() > 0) {
            CommonDialogHint.getInstance().showHint(this, errorMsg.toString());
        } else {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_net_request_fail));
        }
    }

    @OnClick({R.id.ibtn_back, R.id.company, R.id.tv_service_value, R.id.tv_service_value2, R.id.tv_web_value})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.company: {
                Intent web = new Intent(this, CommonWebActivity.class);
                web.putExtra(IParamsName.PARAMS_COMMON_WEB_URL,getString(R.string.setting_about_us_web_value_to_activity));
                startActivity(web);
                break;
            }
            case R.id.tv_service_value:
                CallPhoneUtil.callPhoneNumber(this,getString(R.string.setting_about_us_phone_value));
                break;
            case R.id.tv_service_value2:
                CallPhoneUtil.callPhoneNumber(this,getString(R.string.setting_about_us_phone_value2));
                break;
            case R.id.tv_web_value: {
                Intent web = new Intent(this, CommonWebActivity.class);
                web.putExtra(IParamsName.PARAMS_COMMON_WEB_URL,getString(R.string.setting_about_us_web_value_to_activity));
                startActivity(web);
                break;
            }
        }
    }
}
