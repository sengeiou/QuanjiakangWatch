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

import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.common.main.MainActivity;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.db.entity.LoginUserInfoEntity;
import com.quanjiakan.db.manager.DaoManager;
import com.quanjiakan.net.retrofit.result_entity.PostLoginEntity;
import com.quanjiakan.net_presenter.SigninPresenter;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.common.MessageDigestUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
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

    /**
     * ********************************************************************************************
     * 初始化，设置相关的默认值
     */

    public void initView() {
        tvTitle.setText(R.string.login_title);
    }

    /**
     * ********************************************************************************************
     * 声明周期方法
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
     * 点击事件
     */

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

    /**
     * ********************************************************************************************
     * 获取用户填入的数据
     */

    public String getUsername() {
        return etUsername.getText().toString();
    }

    public String getPassword() {
        return etPassword.getText().toString();
    }

    /**
     * ********************************************************************************************
     * 跳转
     */

    public void signup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void findPassword() {
//        Intent intent = new Intent(this, FindPasswordActivity.class);
//        startActivity(intent);
    }

    /**
     * ********************************************************************************************
     * 根据业务类型，获取对应的参数，若出现参数填入不规范，将传入null，并应该终止业务的执行
     *
     */
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.LOGIN:
                if (etUsername.getText().toString().trim().equals("") || etPassword.getText().toString().trim().equals("")) {
                    CommonDialogHint.getInstance().showHint(SigninActivity_mvp.this,getString(R.string.common_hint_login_params_error));
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

    /**
     *
     * @param type
     */
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
                if(result!=null && result instanceof PostLoginEntity) {//TODO 避免空指针异常与数据类型的异常
                    PostLoginEntity res = (PostLoginEntity) result;
                    //TODO 实际上仍然需要判断数据中是否是返回200 ，得到用户名，Token，ID等数据
                    /**
                     * 用户名，
                     * 电话
                     * 校验用的密码（非原密码，而是加密取SHA1值的密码
                     * Token，
                     * ID，
                     */
                    LogUtil.e(""+res.toString());
                    //TODO 先按照罗工的那个步骤序列化数据
                    /**
                     SharePreferencesSetting.getInstance().setPhone("");
                     SharePreferencesSetting.getInstance().setUserId(1);
                     SharePreferencesSetting.getInstance().setUserName("");
                     SharePreferencesSetting.getInstance().setPwSignature("");
                     SharePreferencesSetting.getInstance().setToken("");
                     */
                    if("200".equals(res.getCode())){
                        //TODO 保存用户的登录信息
                        saveLoginInfo(res);

                        Intent intent = new Intent(SigninActivity_mvp.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        CommonDialogHint.getInstance().showHint(SigninActivity_mvp.this,getString(R.string.error_login_hint));
                    }
                }else{
                    CommonDialogHint.getInstance().showHint(SigninActivity_mvp.this,getString(R.string.error_login_hint));
                }
                break;
        }
    }

    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.LOGIN:
                CommonDialogHint.getInstance().showHint(SigninActivity_mvp.this,"" + errorMsg.toString());
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

    /**
     *************************************************************************************************************************************
     */

    //TODO 保存登录信息，并进行持久化
    public void saveLoginInfo(PostLoginEntity res){
        LoginUserInfoEntity entity = new LoginUserInfoEntity();
        entity.setLoginPhone(getUsername());//保存登录使用的电话，在收到发给自己的广播时进行判断使用
        entity.setPasswordDigest(MessageDigestUtil.getSHA1String(getPassword()));//保存密码的签名
        //TODO 服务器返回的数据
        entity.setNickName(res.getObject().getNickname());
        entity.setUserId(res.getObject().getId()+"");
        entity.setToken(res.getObject().getToken());
        //TODO 保存或替换当前用户数据
        DaoManager.getInstances(this).getDaoSession().getLoginUserInfoEntityDao().insertOrReplaceInTx(entity);
    }
}
