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

import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.util.common.StringCheckUtil;
import com.quanjiakan.util.common.EditTextFilter;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.common.MessageDigestUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.watch.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_findpassword);
		ButterKnife.bind(this);
		initTitle();
		initView();

	}

	public void initTitle() {
		ibtnBack.setVisibility(View.VISIBLE);
		tvTitle.setText(R.string.reset_password);
	}

	public void initView() {
		etCode.setTag("");
		etConfirmnewpassword = (EditText) findViewById(R.id.et_confirmnewpassword);
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

	@OnClick({R.id.ibtn_back, R.id.btn_submit,R.id.btn_yanzhengma})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.ibtn_back:
				finish();
				break;
			case R.id.btn_submit:
				findPassword();
				break;
			case R.id.btn_yanzhengma:
				getSMSCode();
				break;
		}
	}

	public void findPassword() {
		if (etUsername.length() == 0 || etCode.length() == 0 || etNewpassword.length() == 0 || etConfirmnewpassword.length() == 0) {
			CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.setting_password_check1));
			return;
		}

		if(!EditTextFilter.isPhoneLegal(etUsername.getText().toString().trim())) {
			CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.error_findpassword_error_phone));
			return;
		}

		if (!etCode.getText().toString().equals(etCode.getTag().toString())) {
			CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.error_findpassword_error_check_code));
			return;
		}

		if (lastSMSPhone != null && !lastSMSPhone.equals(etUsername.getText().toString())) {
			CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.setting_password_check10));
			return;
		}


		if(etNewpassword.getText().toString().trim().length()<6||etNewpassword.getText().toString().trim().length()>15) {
			CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.hint_new_pw_number_error));
			return;
		}

		if(etConfirmnewpassword.getText().toString().trim().length()<6||etConfirmnewpassword.getText().toString().trim().length()>15) {
			CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.hint_comfirm_pw_number_error));
			return;
		}

		/**
		 * 提升账号安全措施
		 */
		if (!StringCheckUtil.checkStringType(etNewpassword.getText().toString())) {
			CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.setting_password_check5));
			return;
		}

		if (!etNewpassword.getText().toString().equals(etConfirmnewpassword.getText().toString())) {
			CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.setting_password_check11));
			return;
		}

		HashMap<String, String> params = new HashMap<>();
		params.put("mobile", etUsername.getText().toString());
		params.put("password", BaseApplication.getInstances().getFormatPWString(etNewpassword.getText().toString()));
		params.put("confirmpassword", BaseApplication.getInstances().getFormatPWString(etNewpassword.getText().toString()));
	}

	/**
	 * 获取短信验证码
	 */
	public void getSMSCode() {
		try {
			if(!EditTextFilter.isPhoneLegal(etUsername.getText().toString().trim())) {
				CommonDialogHint.getInstance().showHint(FindPasswordActivity.this,getResources().getString(R.string.error_findpassword_error_phone));
				return;
			}

			StringBuilder sb = new StringBuilder();
			sb.append(1).append("@");
			sb.append("forget").append("@");
			sb.append(etUsername.getText().toString()).append("@");
			LogUtil.e("encodeString:" + sb.toString());

			//JSON
			JSONObject jsonData = new JSONObject();
			jsonData.put("client", 1);
			jsonData.put("type", "forget");
			jsonData.put("mobile", etUsername.getText().toString());
			jsonData.put("sign", MessageDigestUtil.getMD5String(sb.toString()));
			//STRING
			String stringParams = "{\"client\":1,\"type\":\"forget\"," +
					"\"mobile\":\"" + etUsername.getText().toString() + "\"" +
					"\"sign\":\"" + MessageDigestUtil.getMD5String(sb.toString()) + "\"" +
					"}";

			HashMap<String, String> params = new HashMap<>();
			params.put("data", jsonData.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
