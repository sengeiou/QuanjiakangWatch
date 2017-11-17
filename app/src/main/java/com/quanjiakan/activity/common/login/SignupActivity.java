package com.quanjiakan.activity.common.login;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.IResponseResultCode;
import com.quanjiakan.net.retrofit.result_entity.PostSMSEntity;
import com.quanjiakan.net.retrofit.result_entity.PostSignupEntity;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.net_presenter.SignupPresenter;
import com.quanjiakan.util.common.EditTextFilter;
import com.quanjiakan.util.common.StringCheckUtil;
import com.quanjiakan.util.common.UnitExchangeUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.widget.OrderClickSpan;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignupActivity extends BaseActivity {


    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ibtn_menu)
    ImageButton ibtnMenu;
    @BindView(R.id.menu_text)
    TextView menuText;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.btn_yanzhengma)
    TextView btnYanzhengma;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.rl_pwd)
    LinearLayout rlPwd;
    @BindView(R.id.et_confirmpassword)
    EditText etConfirmpassword;
    @BindView(R.id.signup_clause_ck)
    View signupClauseCk;
    @BindView(R.id.ll_signup_clause_ck)
    LinearLayout llSignupClauseCk;
    @BindView(R.id.signup_clause_text)
    TextView signupClauseText;
    @BindView(R.id.signup_clause_line)
    LinearLayout signupClauseLine;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private int total = 120;
    private boolean read_flag = false;
    private String lastSMSPhone;

    private SignupPresenter presenter;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int arg = msg.arg1;
            if (msg.what == 1) {
                if (arg < 1) {
                    btnYanzhengma.setEnabled(true);
                    btnYanzhengma.setText(getString(R.string.hint_signup_check_code));
                } else {
                    btnYanzhengma.setEnabled(false);
                    btnYanzhengma.setText(getString(R.string.hint_signup_check_code_remain_pre) + arg +
                            getString(R.string.hint_signup_check_code_remain_suf));
                }
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup);
        ButterKnife.bind(this);
        initTitle();
        initView();
        resetFlag();

        presenter = new SignupPresenter();
    }

    /**
     * ********************************************************************************************
     * 初始化，设置相关的默认值
     */

    public void initTitle() {
        tvTitle.setText(R.string.signup_title);
        ibtnBack.setVisibility(View.VISIBLE);
    }

    public void initView() {
        initFlag();
    }

    private void initFlag() {
        signupClauseCk.setBackgroundResource(R.drawable.doctor_pay_list_nor);
        signupClauseText.setText(R.string.singup_clause_text1);
        String string = getResources().getString(R.string.singup_clause_text2);
        SpannableString spannableString = new SpannableString(string);
        OrderClickSpan orderClickSpan = new OrderClickSpan(string, getResources().getColor(R.color.color_title_green), new OnClickListener() {
            @Override
            public void onClick(View viewss) {
                showAgreementDialog();
            }
        });
        spannableString.setSpan(orderClickSpan, 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signupClauseText.append(spannableString);
        signupClauseText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * ********************************************************************************************
     * 显示条款Dialog
     */
    private void showAgreementDialog() {
        /**
         * 对话框样式《注册声明》
         */
        final Dialog detailDialog = getNewDialog(SignupActivity.this);
        View view = LayoutInflater.from(SignupActivity.this).inflate(R.layout.dialog_medicine_introduce, null);
        /**
         * 数据赋值
         */
        RelativeLayout rl_exit = (RelativeLayout) view.findViewById(R.id.rl_exit);
        rl_exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                detailDialog.dismiss();
                signupClauseText.invalidate();
            }
        });
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        title.setText(getString(R.string.hint_signup_agreement_title));//
        TextView include_value = (TextView) view.findViewById(R.id.indroduce);
        include_value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        include_value.setText(R.string.signup_rules_new);
        WindowManager.LayoutParams lp = detailDialog.getWindow().getAttributes();
        lp.width = UnitExchangeUtil.dip2px(this, 300);
        lp.height = lp.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        detailDialog.setContentView(view, lp);
        detailDialog.setCanceledOnTouchOutside(false);
        detailDialog.show();
    }

    /**
     * ******************
     * 切换显示效果
     */

    private void resetFlag() {
        read_flag = !read_flag;
        if (read_flag) {
            signupClauseCk.setBackgroundResource(R.drawable.doctor_pay_list_light);
        } else {
            signupClauseCk.setBackgroundResource(R.drawable.doctor_pay_list_nor);
        }
    }

    /**
     * ******************
     * 注册的检查条件
     */

    public boolean signup() {
        //先判断一系列条件
        if (StringCheckUtil.isEmpty(etUsername.getText().toString())) {
            CommonDialogHint.getInstance().showHint(SignupActivity.this,getResources().getString(R.string.setting_password_check6));
            return false;
        }
        if (!StringCheckUtil.isPhoneNumber(etUsername.getText().toString())) {
            CommonDialogHint.getInstance().showHint(SignupActivity.this,getResources().getString(R.string.setting_password_check7));
            return false;
        }

        if (StringCheckUtil.isEmpty(etCode.getText().toString())) {
            CommonDialogHint.getInstance().showHint(SignupActivity.this,getResources().getString(R.string.setting_password_check8));
            return false;
        }

        if (StringCheckUtil.isEmpty(etCode.getTag().toString()) || !etCode.getTag().toString().equals(etCode.getText().toString())) {
            CommonDialogHint.getInstance().showHint(SignupActivity.this,getResources().getString(R.string.setting_password_check9));
            return false;
        }

        if (lastSMSPhone != null && !lastSMSPhone.equals(etUsername.getText().toString())) {
            CommonDialogHint.getInstance().showHint(SignupActivity.this,getResources().getString(R.string.setting_password_check10));
            return false;
        }

        //et_name
        if (StringCheckUtil.isEmpty(etName.getText().toString())) {
            CommonDialogHint.getInstance().showHint(SignupActivity.this,getResources().getString(R.string.setting_password_check14));
            return false;
        }

        if (StringCheckUtil.isEmpty(etPassword.getText().toString()) || StringCheckUtil.isEmpty(etConfirmpassword.getText().toString())) {
            CommonDialogHint.getInstance().showHint(SignupActivity.this,getResources().getString(R.string.setting_password_check12));
            return false;
        }


        if (etPassword.getText().toString().trim().length() < 6 || etPassword.getText().toString().trim().length() > 15) {
            CommonDialogHint.getInstance().showHint(SignupActivity.this,getResources().getString(R.string.hint_new_pw_number_error));
            return false;
        }

        if (etConfirmpassword.getText().toString().trim().length() < 6 || etConfirmpassword.getText().toString().trim().length() > 15) {
            CommonDialogHint.getInstance().showHint(SignupActivity.this,getResources().getString(R.string.hint_comfirm_pw_number_error));
            return false;
        }


        if (!etPassword.getText().toString().equals(etConfirmpassword.getText().toString())) {
            CommonDialogHint.getInstance().showHint(SignupActivity.this,getResources().getString(R.string.setting_password_check11));
            return false;
        }

        /**
         * 提升账号安全措施
         */
        if (!StringCheckUtil.checkStringType(etPassword.getText().toString())) {
            CommonDialogHint.getInstance().showHint(SignupActivity.this,getResources().getString(R.string.setting_password_check5));
            return false;
        }

        if (!read_flag) {
            CommonDialogHint.getInstance().showHint(SignupActivity.this,getResources().getString(R.string.singup_clause_hint));
            return false;
        }
        return true;
    }

    /**
     * ******************
     * 开始展示 验证码的倒计时
     */

    public void showSmsCodeTime() {
        if(total>0){
            return;
        }
        total = 120;
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

    /**
     * ********************************************************************************************
     * 获取用户填入的参数参数信息
     */

    public String getPhoneNumber() {
        return etUsername.getText().toString();
    }

    /**
     * ********************************************************************************************
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
     * ********************************************************************************************
     * 根据业务类型返回对应的参数
     */

    @Override
    public Object getParamter(int type) {
        try {
            switch (type) {
                case IPresenterBusinessCode.SMS_CODE: {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(IParamsName.PARAMS_COMMON_MOBILE, etUsername.getText().toString());
                    params.put(IParamsName.PARAMS_COMMON_VALIDATE_TYPE, IHttpUrlConstants.SMS_TYPE_SIGNIN);
                    params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                    return params;
                }
                case IPresenterBusinessCode.SIGNUP: {
                    HashMap<String, String> params_signup = new HashMap<>();
                    params_signup.put(IParamsName.PARAMS_COMMON_MOBILE, etUsername.getText().toString());
                    params_signup.put(IParamsName.PARAMS_COMMON_PASSWORD, BaseApplication.getInstances().getFormatPWString(etPassword.getText().toString()));
                    params_signup.put(IParamsName.PARAMS_COMMON_VALIDATE_CODE, etCode.getText().toString());
                    params_signup.put(IParamsName.PARAMS_COMMON_NICKNAME, etName.getText().toString());
                    params_signup.put(IParamsName.PARAMS_COMMON_PLATFORM,IHttpUrlConstants.PLATFORM_ANDROID);
                    return params_signup;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.SMS_CODE: {
                if (result != null && result instanceof PostSMSEntity) {
                    PostSMSEntity sms = (PostSMSEntity) result;
                    if (IResponseResultCode.RESPONSE_SUCCESS.equals(sms.getCode())) {
                        //TODO 获取验证码成功
                        etCode.setTag(sms.getObject().getSmscode());
                        lastSMSPhone = getPhoneNumber();
                        //TODO 成功获取验证码
                        showSmsCodeTime();
                    } else {
                        if (sms.getMessage() != null && sms.getMessage().length() > 0) {
                            CommonDialogHint.getInstance().showHint(SignupActivity.this, sms.getMessage());
                        } else {
                            CommonDialogHint.getInstance().showHint(SignupActivity.this, getString(R.string.error_sign_up_get_sms_error));
                        }
                    }
                } else {
                    CommonDialogHint.getInstance().showHint(SignupActivity.this, getString(R.string.error_sign_up_get_sms_error));
                }
                break;
            }
            case IPresenterBusinessCode.SIGNUP:{
                if (result != null && result instanceof PostSignupEntity) {
                    PostSignupEntity signupEntity = (PostSignupEntity) result;
                    if (IResponseResultCode.RESPONSE_SUCCESS.equals(signupEntity.getCode())) {
                        //TODO 注册成功
                        CommonDialogHint.getInstance().showHint(SignupActivity.this, getString(R.string.jmui_register_success), new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //TODO 提示用户后返回
                                SignupActivity.this.finish();
                            }
                        });
                    } else {
                        if (signupEntity.getMessage() != null && signupEntity.getMessage().length() > 0) {
                            CommonDialogHint.getInstance().showHint(SignupActivity.this, signupEntity.getMessage());
                        } else {
                            CommonDialogHint.getInstance().showHint(SignupActivity.this, getString(R.string.error_sign_up_fail));
                        }
                    }
                } else {
                    CommonDialogHint.getInstance().showHint(SignupActivity.this, getString(R.string.error_sign_up_fail));
                }
                break;
            }
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.SMS_CODE:
                if(errorMsg!=null){
                    CommonDialogHint.getInstance().showHint(SignupActivity.this,errorMsg.toString());
                }
                break;
            default:
                if(errorMsg!=null){
                    CommonDialogHint.getInstance().showHint(SignupActivity.this,errorMsg.toString());
                }
                break;
        }
    }

    /**
     * ********************************************************************************************
     * 点击事件响应
     */

    @OnClick({R.id.ibtn_back, R.id.btn_yanzhengma, R.id.signup_clause_ck, R.id.btn_submit, R.id.ll_signup_clause_ck})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back: {
                finish();
                break;
            }
            case R.id.btn_yanzhengma: {
                if (((TextView) view).getText().toString().equals(getString(R.string.hint_signup_check_code))) {//TODO 未在验证码缓冲事件内
                    if (!EditTextFilter.isPhoneLegal(etUsername.getText().toString())) {
                        CommonDialogHint.getInstance().showHint(SignupActivity.this,getString(R.string.error_findpassword_error_phone));
                        return;
                    }
                    presenter.getSMSCode(this);
                }
                break;
            }
            case R.id.signup_clause_ck:
                break;
            case R.id.btn_submit: {
                if(!signup()){
                    return;
                }
                presenter.doSignup(this);
                break;
            }
            case R.id.ll_signup_clause_ck: {
                resetFlag();
                break;
            }
        }
    }
}
