package com.quanjiakan.activity.common.setting.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.common.setting.more.improveinfo.ImproveUserInfoActivity;
import com.quanjiakan.constants.ICommonActivityRequestCode;
import com.quanjiakan.constants.ICommonActivityResultCode;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.db.entity.DisturbStatusEntity;
import com.quanjiakan.db.manager.DaoManager;
import com.quanjiakan.net.retrofit.result_entity.GetUpdateEntity;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.net_presenter.UpdatePresenter;
import com.quanjiakan.util.common.SerializeToObjectUtil;
import com.quanjiakan.util.common.VersionInfoUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.dialog.update.CommonDownloadDialog;
import com.quanjiakan.util.download.IDownloadCallback;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/5.
 */

public class SettingMoreActivity extends BaseActivity {


    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ibtn_menu)
    ImageButton ibtnMenu;
    @BindView(R.id.menu_text)
    TextView menuText;
    @BindView(R.id.tv_improve_user_info)
    TextView tvImproveUserInfo;
    @BindView(R.id.tv_div)
    View tvDiv;
    @BindView(R.id.tv_modify_password)
    TextView tvModifyPassword;
    @BindView(R.id.tv_fenge)
    View tvFenge;
    @BindView(R.id.tv_feedback)
    TextView tvFeedback;
    @BindView(R.id.tv_fenge3)
    View tvFenge3;
    @BindView(R.id.tv_banben)
    TextView tvBanben;
    @BindView(R.id.tv_banben_value)
    TextView tvBanbenValue;
    @BindView(R.id.rl_current_version)
    RelativeLayout rlCurrentVersion;
    @BindView(R.id.tv_fenge9)
    View tvFenge9;
    @BindView(R.id.tv_disturb)
    TextView tvDisturb;
    @BindView(R.id.ck_disturb)
    CheckBox ckDisturb;
    @BindView(R.id.rl_disturb)
    RelativeLayout rlDisturb;
    @BindView(R.id.tv_fenge2)
    View tvFenge2;
    @BindView(R.id.tv_update)
    TextView tvUpdate;
    @BindView(R.id.tv_tv_update_value)
    TextView tvTvUpdateValue;
    @BindView(R.id.rl_update)
    RelativeLayout rlUpdate;
    @BindView(R.id.tv_quit)
    TextView tvQuit;

    private DisturbStatusEntity entity = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting_more);
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

    @Override
    public void onStop() {
        super.onStop();
    }


    @OnClick({R.id.ibtn_back, R.id.ibtn_menu, R.id.menu_text,
            R.id.tv_improve_user_info, R.id.tv_modify_password, R.id.tv_feedback,
            R.id.rl_current_version, R.id.ck_disturb, R.id.rl_update, R.id.tv_quit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.ibtn_menu:
                break;
            case R.id.menu_text:
                break;
            case R.id.tv_improve_user_info:
                toImproveUserInfo();
                break;
            case R.id.tv_modify_password:
                toModifyPassword();
                break;
            case R.id.tv_feedback:
                toFeedback();
                break;
            case R.id.rl_current_version:
                break;
            case R.id.ck_disturb:
                break;
            case R.id.rl_update:
                checkUpdate();
                break;
            case R.id.tv_quit:
                doLogout();
                break;
        }
    }

    public void toImproveUserInfo(){
        Intent intent = new Intent(this, ImproveUserInfoActivity.class);
        startActivityForResult(intent,ICommonActivityRequestCode.REQUEST_TO_IMPROVE_USER_INFO);
    }

    public void toModifyPassword(){
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivityForResult(intent,ICommonActivityRequestCode.REQUEST_TO_MODIFY_PASSWORD);
    }

    public void toFeedback(){
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivityForResult(intent,ICommonActivityRequestCode.REQUEST_TO_FEEDBACK);
    }

    public void doLogout(){
        BaseApplication.getInstances().onLogout(this);
    }

    public void initTitle(){
        presenter = new UpdatePresenter();

        ibtnBack.setVisibility(View.VISIBLE);
        ibtnBack.setImageResource(R.drawable.back);

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.setting_more_title);

        ibtnMenu.setVisibility(View.GONE);
        menuText.setVisibility(View.GONE);
    }

    public void initView(){
        tvImproveUserInfo.setText(R.string.setting_improve_user_info);
        tvDiv.setVisibility(View.VISIBLE);
        //****************************
        tvModifyPassword.setText(R.string.setting_modify_password);
        tvFenge.setVisibility(View.VISIBLE);
        //****************************
        tvFeedback.setText(R.string.setting_feedback);
        tvFenge3.setVisibility(View.VISIBLE);
        //****************************
        tvBanben.setText(R.string.setting_version);
        tvBanbenValue.setText(VersionInfoUtil.getVersion(this));
        rlCurrentVersion.setVisibility(View.VISIBLE);
        tvFenge9.setVisibility(View.VISIBLE);
        //****************************

        tvDisturb.setText(R.string.setting_disturb);
        rlDisturb.setVisibility(View.VISIBLE);
        tvFenge2.setVisibility(View.VISIBLE);

        loadDisturb();
        if(entity==null){
            ckDisturb.setChecked(false);
            insertDisturb(false);
            loadDisturb();//拿到拥有Id的数据
        }else{
            if(entity.getDisturbStatus()!=0){//TODO 开启了免打扰
                ckDisturb.setChecked(true);
            }else{//TODO 关闭了免打扰
                ckDisturb.setChecked(false);
            }
        }
        //TODO 如果免打扰不是网络同步的话，则需要使用数据库，根据用户区分这个状态
        //****************************
        tvUpdate.setText(R.string.setting_update_check);
        tvTvUpdateValue.setVisibility(View.GONE);
        rlUpdate.setVisibility(View.VISIBLE);
        //****************************
        tvQuit.setText(R.string.setting_more_exit1);
        //****************************

        addListenerToCheckDisturb();
    }

    public void addListenerToCheckDisturb(){
        ckDisturb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    insertDisturb(true);
                }else{
                    insertDisturb(false);
                }
            }
        });
    }

    public void loadDisturb(){
        List<DisturbStatusEntity>  list = DaoManager.getInstances(this).getDaoSession().getDisturbStatusEntityDao().loadAll();
        for(DisturbStatusEntity temp:list){
            if(BaseApplication.getInstances().getLoginInfo().getUserId().equals(temp.getBelongUserId())){
                entity = temp;
                break;
            }
        }
    }

    public void insertDisturb(boolean isOpenDisturb){
        if(entity!=null){
            entity.setBelongUserId(BaseApplication.getInstances().getLoginInfo().getUserId());
            if(isOpenDisturb){//TODO 开启免打扰
                entity.setDisturbStatus(1);
            }else{//TODO 关闭免打扰
                entity.setDisturbStatus(0);
            }
            DaoManager.getInstances(this).getDaoSession().getDisturbStatusEntityDao().insert(entity);
        }else{
            entity = new DisturbStatusEntity();
            entity.setBelongUserId(BaseApplication.getInstances().getLoginInfo().getUserId());
            if(isOpenDisturb){//TODO 开启免打扰
                entity.setDisturbStatus(1);
            }else{//TODO 关闭免打扰
                entity.setDisturbStatus(0);
            }
            DaoManager.getInstances(this).getDaoSession().getDisturbStatusEntityDao().insert(entity);
        }
    }

    /**
     * *****************************************************************************************************************************
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ICommonActivityRequestCode.REQUEST_TO_IMPROVE_USER_INFO:{
                //TODO 若更新了用户的信息，则需要通知上一个页面，刷新或用户的新信息
                if(resultCode == ICommonActivityResultCode.REQUEST_TO_IMPROVE_USER_INFO){
                    setResult(ICommonActivityResultCode.REQUEST_TO_IMPROVE_USER_INFO);
                }
                break;
            }
            case ICommonActivityRequestCode.REQUEST_TO_MODIFY_PASSWORD:{
                //TODO 不需要处理什么
                break;
            }
            case ICommonActivityRequestCode.REQUEST_TO_FEEDBACK:{
                //TODO 不需要处理什么
                break;
            }
        }
    }

    /**
     * *****************************************************************************************************************************
     * 更新版本
     */

    @Override
    public Object getParamter(int type) {
        switch (type){
            case IPresenterBusinessCode.COMMON_UPDATE_APP:{
                HashMap<String,String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_TOKEN,BaseApplication.getInstances().getLoginInfo().getToken());
                return params;
            }
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type){
            case IPresenterBusinessCode.COMMON_UPDATE_APP:{
                break;
            }
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type){
            case IPresenterBusinessCode.COMMON_UPDATE_APP:{

                break;
            }
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type){
            case IPresenterBusinessCode.COMMON_UPDATE_APP:{
                setUpdateResult(result);
                break;
            }
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type){
            case IPresenterBusinessCode.COMMON_UPDATE_APP:{

                break;
            }
        }
        if(errorMsg!=null && errorMsg.toString().length()>0){
            CommonDialogHint.getInstance().showHint(this,errorMsg.toString());
        }else{
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_common_net_request_fail));
        }
    }

    /**
     * *****************************************************************************************************************************
     */

    private UpdatePresenter presenter;

    public void checkUpdate(){
        presenter.checkUpdate(this);
    }

    public void setUpdateResult(Object result){
        if(result!=null && result instanceof String){
            final GetUpdateEntity entity = (GetUpdateEntity) SerializeToObjectUtil.getInstances().jsonToObject(result.toString(),new TypeToken<GetUpdateEntity>(){}.getType());
            //TODO 避免由于接口访问慢，导致中间突然显示了未绑定对话框
            CommonDownloadDialog.getInstance().showUpdateDialog(this, entity.getObject(), new IDownloadCallback() {
                @Override
                public void updateProgress(int progress, String rate) {
                    //TODO 避免由于接口访问慢，导致中间突然显示了未绑定对话框
                    CommonDownloadDialog.getInstance().updateProgress(progress,rate);
                }
            });
        }
    }
}
