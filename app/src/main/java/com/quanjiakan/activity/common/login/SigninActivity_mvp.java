package com.quanjiakan.activity.common.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.QuanjiakanSetting;
import com.quanjiakan.activity.common.main.MainActivity;
import com.quanjiakan.constants.IPresenterBusinessCode;
import com.quanjiakan.net.format.CommonResultEntity;
import com.quanjiakan.net.retrofit.result_entity.PostLoginEntity;
import com.quanjiakan.net_presenter.SigninPresenter;
import com.quanjiakan.util.common.SerialUtil;
import com.quanjiakan.watch.R;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SigninActivity_mvp extends BaseActivity {


    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ibtn_menu)
    ImageButton ibtnMenu;
    @BindView(R.id.menu_text)
    TextView menuText;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.login_account)
    TextView loginAccount;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.login_password)
    TextView loginPassword;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_signup)
    TextView tvSignup;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.tv_findpassword)
    TextView tvFindpassword;
    @BindView(R.id.layout_bottom)
    RelativeLayout layoutBottom;


    private SigninPresenter signinPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signin);
        ButterKnife.bind(this);
        initView();
        signinPresenter = new SigninPresenter();
    }

    public void initView() {
        tvTitle.setText(R.string.login_title);
    }

    public void signup() {
//        Intent intent = new Intent(this, SignupActivity.class);
//        startActivity(intent);
    }

    public void findPassword() {
//        Intent intent = new Intent(this, FindPasswordActivity.class);
//        startActivity(intent);
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

    @OnClick({R.id.btn_submit, R.id.tv_signup, R.id.tv_findpassword})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                signinPresenter.doLogin(this);
                break;
            case R.id.tv_signup:
                signup();
                break;
            case R.id.tv_findpassword:
                findPassword();
                break;
        }
    }

    public String getUsername() {
        return etUsername.getText().toString();
    }

    public String getPassword() {
        return etPassword.getText().toString();
    }

    //********************************************************************************************

    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.LOGIN:
                if (etUsername.getText().toString().trim().equals("") || etPassword.getText().toString().trim().equals("")) {
                    Toast.makeText(SigninActivity_mvp.this, getString(R.string.common_hint_login_params_error), Toast.LENGTH_SHORT).show();
                    return null;
                }
                HashMap<String, String> params = new HashMap<>();
                params.put("mobile", etUsername.getText().toString());
                params.put("password", BaseApplication.getInstances().getFormatPWString(etPassword.getText().toString()));
                params.put("platform", "2");
                return params;
            default:
                break;
        }
        return null;
    }

    public void showMyDialog(int type) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = getDialog(SigninActivity_mvp.this);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    mDialog.dismiss();
                    /**
                     * 正式环境需要在这里开放
                     */
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public void dismissMyDialog(int type) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.LOGIN:
                //TODO 使用String格式的Retrofit
                if(result!=null && result.toString().startsWith("{")) {
                    CommonResultEntity res = (CommonResultEntity) SerialUtil.jsonToObject(result.toString(),new TypeToken<CommonResultEntity>(){}.getType());
                    //TODO 实际上仍然需要判断数据中是否是返回200 ，得到用户名，Token，ID等数据
                    /**
                     * 用户名，
                     * 电话
                     * 校验用的密码（非原密码，而是加密取SHA1值的密码
                     * Token，
                     * ID，
                     */
                    //TODO 先按照罗工的那个步骤序列化数据
                    /**
                     QuanjiakanSetting.getInstance().setPhone("");
                     QuanjiakanSetting.getInstance().setUserId(1);
                     QuanjiakanSetting.getInstance().setUserName("");
                     QuanjiakanSetting.getInstance().setPwSignature("");
                     QuanjiakanSetting.getInstance().setToken("");
                     */
                    if("200".equals(res.getCode())){
                        Intent intent = new Intent(SigninActivity_mvp.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                //TODO 使用JSON格式的Retrofit
//                if (result != null) {
//                    PostLoginEntity response = (PostLoginEntity) result;
//                    if ("200".equals(response.getCode())) {
//                        QuanjiakanSetting.getInstance().setPhone(etUsername.getText().toString());
//                        QuanjiakanSetting.getInstance().setUserId(response.getObject().getId());
//                        QuanjiakanSetting.getInstance().setUserName(response.getObject().getNickname());
//                        QuanjiakanSetting.getInstance().setPwSignature(etPassword.getText().toString());
//                        QuanjiakanSetting.getInstance().setToken(response.getObject().getToken());
//                        Intent intent = new Intent(SigninActivity_mvp.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
                break;
        }
    }

    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.LOGIN:
                Toast.makeText(this, "" + errorMsg.toString(), Toast.LENGTH_SHORT).show();//
                break;
            case IPresenterBusinessCode.NONE:
                //TODO 不做任何事情，在获取参数为空时调用并返回，调用前会有对应参数相关的提示
                break;
        }
    }

    @Override
    public View getViewComponentByID(int viewID) {
        return null;
    }
}
