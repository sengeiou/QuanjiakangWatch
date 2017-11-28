package com.quanjiakan.activity.common.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.IResponseResultCode;
import com.quanjiakan.net.retrofit.result_entity.PostResetPasswordEntity;
import com.quanjiakan.net.retrofit.result_entity.PostSMSEntity;
import com.quanjiakan.net_presenter.FindPasswordPresenter;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.common.EditTextFilter;
import com.quanjiakan.util.common.StringCheckUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.encrypt.SMSValidateUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindPasswordActivity extends BaseActivity implements OnClickListener {

	@BindView(R.id.ibtn_back)
	ImageButton ibtnBack;
	@BindView(R.id.tv_title)
	TextView tvTitle;
	@BindView(R.id.et_username)
	EditText etUsername;
	@BindView(R.id.btn_yanzhengma)
	TextView btnYanzhengma;
	@BindView(R.id.et_code)
	EditText etCode;
	@BindView(R.id.et_newpassword)
	EditText etNewpassword;
	@BindView(R.id.rl_pwd)
	LinearLayout rlPwd;
	@BindView(R.id.et_confirmnewpassword)
	EditText etConfirmnewpassword;
	@BindView(R.id.btn_submit)
	Button btnSubmit;

	private int total = 120;
	private String lastSMSPhone;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int arg = msg.arg1;
			if (msg.what == 1) {
				if(arg < 1){
					btnYanzhengma.setEnabled(true);
					btnYanzhengma.setText("获取验证码");
				}else {
					btnYanzhengma.setEnabled(false);
					btnYanzhengma.setText("剩余"+arg + "s");
				}
			}
		}

	};

	private FindPasswordPresenter presenter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_findpassword);
		ButterKnife.bind(this);

		init();
		initTitle();
		initView();
	}

	/**
	 * **********************************************************************************
	 * 生命周期方法
	 */

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
	 * **********************************************************************************
	 * 组件初始化方法
	 */

	public void init(){
		presenter = new FindPasswordPresenter();
	}

	public void initTitle() {
		ibtnBack.setVisibility(View.VISIBLE);
		tvTitle.setText(R.string.reset_password);
	}

	public void initView() {
		etCode.setTag("");
		etConfirmnewpassword = (EditText) findViewById(R.id.et_confirmnewpassword);
	}

	public String getPhoneNumber(){
		return etUsername.getText().toString().trim();
	}

	public void showSmsCodeTime() {
		btnYanzhengma.setEnabled(false);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					do {
						Thread.sleep(1000);
						total--;
						Message msg = new Message();
						msg.what = 1;
						msg.arg1 = total;
						mHandler.sendMessage(msg);
					} while (total > 0);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public boolean findPassword() {
		if (etUsername.length() == 0 || etCode.length() == 0 || etNewpassword.length() == 0 || etConfirmnewpassword.length() == 0) {
			CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.setting_password_check1));
			return false;
		}

		if(!EditTextFilter.isPhoneLegal(etUsername.getText().toString().trim())) {
			CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.error_findpassword_error_phone));
			return false;
		}

		if (!etCode.getText().toString().equals(etCode.getTag().toString())) {
			CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.error_findpassword_error_check_code));
			return false;
		}

		if (lastSMSPhone != null && !lastSMSPhone.equals(etUsername.getText().toString())) {
			CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.setting_password_check10));
			return false;
		}


		if(etNewpassword.getText().toString().trim().length()<6||etNewpassword.getText().toString().trim().length()>15) {
			CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.hint_new_pw_number_error));
			return false;
		}

		if(etConfirmnewpassword.getText().toString().trim().length()<6||etConfirmnewpassword.getText().toString().trim().length()>15) {
			CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.hint_comfirm_pw_number_error));
			return false;
		}

		/**
		 * 提升账号安全措施
		 */
		if (!StringCheckUtil.checkStringType(etNewpassword.getText().toString())) {
			CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.setting_password_check5));
			return false;
		}

		if (!etNewpassword.getText().toString().equals(etConfirmnewpassword.getText().toString())) {
			CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.setting_password_check11));
			return false;
		}
		return true;
	}

	/**
	 * 获取短信验证码
	 */
	public boolean getSMSCode() {
		try {
			if(!EditTextFilter.isPhoneLegal(etUsername.getText().toString().trim())) {
				CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.error_findpassword_error_phone));
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * **********************************************************************************
	 * 组件初始化方法
	 */

	@OnClick({R.id.ibtn_back, R.id.btn_submit,R.id.btn_yanzhengma})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.ibtn_back: {
				finish();
				break;
			}
			case R.id.btn_submit: {
				if(!findPassword()){
					return;
				}
				presenter.doResetPassword(this);
				break;
			}
			case R.id.btn_yanzhengma: {
				//TODO 先校验参数正确性
				if(!getSMSCode()){
					return;
				}
				presenter.getSMSCodeEncrypt(this);
				break;
			}
		}
	}

	@Override
	public Object getParamter(int type) {
		switch (type){
			case IPresenterBusinessCode.SMS_CODE_ENCRYPT:{
				HashMap<String, String> params = new HashMap<>();
				params.put(IParamsName.PARAMS_COMMON_ENCRYPT, getEncryptString());
				params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
				return params;
			}
			case IPresenterBusinessCode.SMS_CODE: {
				HashMap<String, String> params = new HashMap<>();
				params.put(IParamsName.PARAMS_COMMON_MOBILE, etUsername.getText().toString());
				params.put(IParamsName.PARAMS_COMMON_VALIDATE_TYPE, IHttpUrlConstants.SMS_TYPE_FORGET_PW);//TODO
				params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
				return params;
			}
			case IPresenterBusinessCode.PASSWORD_RESET: {
				HashMap<String, String> params = new HashMap<>();
				params.put(IParamsName.PARAMS_COMMON_MOBILE, etUsername.getText().toString());
				params.put(IParamsName.PARAMS_COMMON_PASSWORD, BaseApplication.getInstances().getFormatPWString(etNewpassword.getText().toString()));
				params.put(IParamsName.PARAMS_COMMON_VALIDATE_CODE, etCode.getText().toString());
				params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
				return params;
			}
			default: {
				return null;
			}
		}
	}

	@Override
	public void showMyDialog(int type) {
		switch (type){
			case IPresenterBusinessCode.SMS_CODE_ENCRYPT:
				getDialog(this);
				break;
			case IPresenterBusinessCode.SMS_CODE:
				getDialog(this);
				break;
			case IPresenterBusinessCode.PASSWORD_RESET:
				getDialog(this);
				break;
			default:
				break;
		}
	}

	@Override
	public void dismissMyDialog(int type) {
		switch (type){
			case IPresenterBusinessCode.SMS_CODE_ENCRYPT:
				dismissDialog();
				break;
			case IPresenterBusinessCode.SMS_CODE:
				dismissDialog();
				break;
			case IPresenterBusinessCode.PASSWORD_RESET:
				dismissDialog();
				break;
			default:
				break;
		}
	}

	@Override
	public void onSuccess(int type, int httpResponseCode, Object result) {
		switch (type){
			case IPresenterBusinessCode.SMS_CODE_ENCRYPT: {
				//TODO 操作成功，开始倒计时
				//TODO 保存验证码数据
				if(result!=null && result instanceof PostSMSEntity){
					PostSMSEntity sms = (PostSMSEntity) result;
					if (IResponseResultCode.RESPONSE_SUCCESS.equals(sms.getCode())) {
						//TODO 获取验证码成功
						etCode.setTag(sms.getObject().getSmscode());
						lastSMSPhone = getPhoneNumber();
						//TODO 成功获取验证码
						showSmsCodeTime();
					} else {
						if (sms.getMessage() != null && sms.getMessage().length() > 0) {
							CommonDialogHint.getInstance().showHint(FindPasswordActivity.this, sms.getMessage());
						} else {
							CommonDialogHint.getInstance().showHint(FindPasswordActivity.this, getString(R.string.error_sign_up_get_sms_error));
						}
					}
				}
				break;
			}
			case IPresenterBusinessCode.SMS_CODE: {
				//TODO 操作成功，开始倒计时
				//TODO 保存验证码数据
				if(result!=null && result instanceof PostSMSEntity){
					PostSMSEntity sms = (PostSMSEntity) result;
					if (IResponseResultCode.RESPONSE_SUCCESS.equals(sms.getCode())) {
						//TODO 获取验证码成功
						etCode.setTag(sms.getObject().getSmscode());
						lastSMSPhone = getPhoneNumber();
						//TODO 成功获取验证码
						showSmsCodeTime();
					} else {
						if (sms.getMessage() != null && sms.getMessage().length() > 0) {
							CommonDialogHint.getInstance().showHint(FindPasswordActivity.this, sms.getMessage());
						} else {
							CommonDialogHint.getInstance().showHint(FindPasswordActivity.this, getString(R.string.error_sign_up_get_sms_error));
						}
					}
				}
				break;
			}
			case IPresenterBusinessCode.PASSWORD_RESET: {
				if(result!=null && result instanceof PostResetPasswordEntity){
					PostResetPasswordEntity sms = (PostResetPasswordEntity) result;
					if (IResponseResultCode.RESPONSE_SUCCESS.equals(sms.getCode())) {
						CommonDialogHint.getInstance().showHint(FindPasswordActivity.this, getString(R.string.hint_findpassword_reset_success), new OnClickListener() {
							@Override
							public void onClick(View view) {
								//TODO 提示用户后返回
								FindPasswordActivity.this.finish();
							}
						});
					}else{
						if (sms.getMessage() != null && sms.getMessage().length() > 0) {
							CommonDialogHint.getInstance().showHint(FindPasswordActivity.this, sms.getMessage());
						} else {
							CommonDialogHint.getInstance().showHint(FindPasswordActivity.this, getString(R.string.error_findpassword_reset_error));
						}
					}
				}
				break;
			}
			default:
				break;
		}
	}

	@Override
	public void onError(int type, int httpResponseCode, Object errorMsg) {
		switch (type){
			case IPresenterBusinessCode.SMS_CODE_ENCRYPT:
				break;
			case IPresenterBusinessCode.SMS_CODE:
				break;
			case IPresenterBusinessCode.PASSWORD_RESET:
				break;
			default:
				break;
		}
		if(errorMsg!=null && errorMsg.toString().length()>0){
			CommonDialogHint.getInstance().showHint(this,errorMsg.toString());
		}else{
			CommonDialogHint.getInstance().showHint(this,getString(R.string.error_common_net_request_fail));
		}
	}

	public String getEncryptString(){
		try {
			return SMSValidateUtil.getCiphertext(etUsername.getText().toString(),2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
