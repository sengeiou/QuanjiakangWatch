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

import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.QuanjiakanUtil;
import com.quanjiakan.constants.IPresenterBusinessCode;
import com.quanjiakan.net_presenter.SignupPresenter;
import com.quanjiakan.util.common.StringCheckUtil;
import com.quanjiakan.util.common.EditTextFilter;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.common.MessageDigestUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.widget.OrderClickSpan;
import com.quanjiakan.watch.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

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

    Handler mHandler = new Handler() {

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
                showDialog();
            }
        });
        spannableString.setSpan(orderClickSpan, 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signupClauseText.append(spannableString);
        signupClauseText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void showDialog() {
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
        lp.width = QuanjiakanUtil.dip2px(this, 300);
        lp.height = lp.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        detailDialog.setContentView(view, lp);
        detailDialog.setCanceledOnTouchOutside(false);
        detailDialog.show();
    }

    private void resetFlag() {
        read_flag = !read_flag;
        if (read_flag) {
            signupClauseCk.setBackgroundResource(R.drawable.doctor_pay_list_light);
        } else {
            signupClauseCk.setBackgroundResource(R.drawable.doctor_pay_list_nor);
        }
    }

    /**
     * 获取短信验证码
     */
    public void getSMSCode() {
        if (!EditTextFilter.isPhoneLegal(etUsername.getText().toString())) {
            CommonDialogHint.getInstance().showHint(SignupActivity.this,getResources().getString(R.string.hint_input_right_mobilephone));
            return;
        }
        try {

            StringBuilder sb = new StringBuilder();
            sb.append(1).append("@");
            sb.append("signup").append("@");
            sb.append(etUsername.getText().toString()).append("@");
            LogUtil.e("encodeString:" + sb.toString());

            //JSON
            JSONObject jsonData = new JSONObject();
            jsonData.put("client", 1);
            jsonData.put("type", "signup");
            jsonData.put("mobile", etUsername.getText().toString());
            jsonData.put("sign", MessageDigestUtil.getMD5String(sb.toString()));

            HashMap<String, String> params = new HashMap<>();
            params.put("data", jsonData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void showSmsCodeTime() {
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

    public String getPhoneNumber() {
        return etUsername.getText().toString();
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
    public Object getParamter(int type) {
        try {
            switch (type) {
                case IPresenterBusinessCode.SMS_CODE: {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("mobile", etUsername.getText().toString());
                    params.put("validateType", "1");
                    params.put("platform", "2");
                    return params;
                }
                case IPresenterBusinessCode.SIGNUP: {
                    HashMap<String, String> params_signup = new HashMap<>();
                    params_signup.put("mobile", etUsername.getText().toString());
                    params_signup.put("password", BaseApplication.getInstances().getFormatPWString(etPassword.getText().toString()));
                    params_signup.put("c_password", etConfirmpassword.getText().toString());
                    params_signup.put("nickname", etName.getText().toString());
                    params_signup.put("client", "1");
                    return params_signup;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        switch (type) {
            case IPresenterBusinessCode.SMS_CODE:

                break;
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

    @Override
    public View getViewComponentByID(int viewID) {
        return null;
    }

    @OnClick({R.id.ibtn_back, R.id.btn_yanzhengma, R.id.signup_clause_ck, R.id.btn_submit, R.id.ll_signup_clause_ck})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back: {
                finish();
                break;
            }
            case R.id.btn_yanzhengma: {
                if (((TextView) view).getText().toString().equals(getString(R.string.hint_signup_check_code))) {
                    getSMSCode();

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
