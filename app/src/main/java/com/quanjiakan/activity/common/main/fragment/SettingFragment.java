package com.quanjiakan.activity.common.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.BaseFragment;
import com.quanjiakan.activity.common.setting.about.AboutUsActivity;
import com.quanjiakan.activity.common.setting.healthdocument.UserHealthDocumentActivity;
import com.quanjiakan.activity.common.setting.housekeeper.HouseKeeperOrderListActivity;
import com.quanjiakan.activity.common.setting.more.SettingMoreActivity;
import com.quanjiakan.constants.ICommonActivityRequestCode;
import com.quanjiakan.constants.ICommonActivityResultCode;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.GetUserInfoEntity;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.net_presenter.UserInfoPresenter;
import com.quanjiakan.util.common.SerializeToObjectUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.widget.CircleTransformation;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/17.
 */

public class SettingFragment extends BaseFragment {

    @BindView(R.id.user_header_img)
    ImageView userHeaderImg;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.layout_myinfo)
    LinearLayout layoutMyinfo;
    @BindView(R.id.tv_medcine)
    TextView tvMedcine;
    @BindView(R.id.tv_fenge)
    View tvFenge;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.ll_housekeeper)
    LinearLayout llHousekeeper;
    @BindView(R.id.ll_insure)
    LinearLayout llInsure;
    @BindView(R.id.tv_div)
    View tvDiv;
    @BindView(R.id.layout_1)
    LinearLayout layout1;
    @BindView(R.id.tv_house_keeper)
    TextView tvHouseKeeper;
    @BindView(R.id.tv_house_keeper_div)
    View tvHouseKeeperDiv;
    @BindView(R.id.tv_health_document)
    TextView tvHealthDocument;
    @BindView(R.id.tv_health_document_div)
    View tvHealthDocumentDiv;
    @BindView(R.id.tv_aboutus)
    TextView tvAboutus;
    @BindView(R.id.tv_aboutus_div)
    View tvAboutusDiv;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.layout_2)
    LinearLayout layout2;
    //***************************************************
    Unbinder unbinder;

    private UserInfoPresenter presenter = null;

    /**
     * ************************************************************************************************************************
     * 生命周期方法
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_setting, container, false);//TODO 实例化整个布局
        unbinder = ButterKnife.bind(this, view);
        //TODO 设置默认值
        setDefaultValue();


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * *****************************************************************************************************************************
     * EventBus 广播事件
     */

    /**
     * ************************************************************************************************************************
     */

    public void setDefaultValue() {
        initTitleBar();
        /**
         * 设置组件的默认值
         */
        setViewValue();
    }

    public void initTitleBar() {
        presenter = new UserInfoPresenter();
    }

    public void setViewValue(){
        userHeaderImg.setImageResource(R.drawable.touxiang_big_icon);

        if(BaseApplication.getInstances().getLoginInfo().getNickName()!=null){
            userName.setText(BaseApplication.getInstances().getLoginInfo().getNickName());
        }else{
            userName.setText(R.string.jmui_username);
        }


        tvHouseKeeper.setText(R.string.setting_entry_my_housekeeper);
        tvHealthDocument.setText(R.string.setting_entry_health);
        tvAboutus.setText(R.string.setting_entry_about_us);
        tvSetting.setText(R.string.setting_entry_more);

        presenter.getUserInfo(this);
    }

    /**
     * ************************************************************************************************************************
     */

    @OnClick({R.id.tv_house_keeper, R.id.tv_health_document, R.id.tv_aboutus, R.id.tv_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_house_keeper:
                toHouseKeeperOrderList();
                break;
            case R.id.tv_health_document:
                toHealthDocument();
                break;
            case R.id.tv_aboutus:
                toAboutUs();
                break;
            case R.id.tv_setting:
                toMoreSetting();
                break;
        }
    }

    /**
     * ************************************************************************************************************************
     */

    public void toHouseKeeperOrderList(){
        Intent intent = new Intent(getActivity(), HouseKeeperOrderListActivity.class);
        startActivity(intent);
    }

    public void toHealthDocument(){
        Intent intent = new Intent(getActivity(), UserHealthDocumentActivity.class);
        startActivity(intent);
    }

    public void toAboutUs(){
        Intent intent = new Intent(getActivity(), AboutUsActivity.class);
        startActivity(intent);
    }

    public void toMoreSetting(){
        Intent intent = new Intent(getActivity(), SettingMoreActivity.class);
        startActivityForResult(intent,ICommonActivityRequestCode.RELOAD_DATA);
    }

    public void setInfoResult(Object result){
        if(result!=null && result instanceof String){
            GetUserInfoEntity entity = (GetUserInfoEntity) SerializeToObjectUtil.getInstances().jsonToObject(result.toString(),new TypeToken<GetUserInfoEntity>(){}.getType());
            if(entity!=null){
                if(entity.getObject()!=null){
                    if(entity.getObject().getNickname()!=null){
                        if(BaseApplication.getInstances().getLoginInfo().getNickName().equals(entity.getObject().getNickname())){

                        }else{
                            //entity.getObject().getNickname()
                            BaseApplication.getInstances().getLoginInfo().setNickName(entity.getObject().getNickname());
                        }
                        userName.setText(entity.getObject().getNickname());
                    }else{
                        if(BaseApplication.getInstances().getLoginInfo().getNickName()!=null){
                            userName.setText(BaseApplication.getInstances().getLoginInfo().getNickName());
                        }else{
                            userName.setText(BaseApplication.getInstances().getLoginInfo().getUserId());
                        }
                    }

                    if(entity.getObject().getPicture()!=null && entity.getObject().getPicture().toLowerCase().startsWith(ICommonData.HTTP_PREFIX)){
                        Picasso.with(getActivity()).load(entity.getObject().getPicture()).transform(new CircleTransformation()).into(userHeaderImg);
                        BaseApplication.getInstances().getLoginInfo().setTempHeadIcon(entity.getObject().getPicture());
                    }else{
                        if(BaseApplication.getInstances().getLoginInfo().getTempHeadIcon()!=null &&
                                BaseApplication.getInstances().getLoginInfo().getTempHeadIcon().toLowerCase().startsWith(ICommonData.HTTP_PREFIX)){
                            Picasso.with(getActivity()).load(BaseApplication.getInstances().getLoginInfo().getTempHeadIcon()).
                                    transform(new CircleTransformation()).into(userHeaderImg);
                        }else{
                            userHeaderImg.setImageResource(R.drawable.touxiang_big_icon);
                        }
                    }
                }
            }
        }
    }
    /**
     * ************************************************************************************************************************
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ICommonActivityRequestCode.RELOAD_DATA:{
                if(resultCode == ICommonActivityResultCode.REQUEST_TO_IMPROVE_USER_INFO){
                    //更新过个人信息，则重新获取用户头像，姓名数据
                    setLocalInfo();
                }else if(resultCode == ICommonActivityResultCode.TO_SIGN_IN){
                    //TODO 关闭MainActivity
                    getActivity().finish();
                }
            }
        }
    }

    public void setLocalInfo(){
        if(BaseApplication.getInstances().getLoginInfo().getNickName()!=null){
            userName.setText(BaseApplication.getInstances().getLoginInfo().getNickName());
        }else{
            userName.setText(BaseApplication.getInstances().getLoginInfo().getUserId());
        }

        if(BaseApplication.getInstances().getLoginInfo().getTempHeadIcon()!=null &&
                BaseApplication.getInstances().getLoginInfo().getTempHeadIcon().toLowerCase().startsWith(ICommonData.HTTP_PREFIX)){
            Picasso.with(getActivity()).load(BaseApplication.getInstances().getLoginInfo().getTempHeadIcon()).
                    transform(new CircleTransformation()).into(userHeaderImg);
        }else{
            userHeaderImg.setImageResource(R.drawable.touxiang_big_icon);
        }
    }

    /**
     * ************************************************************************************************************************
     */

    @Override
    public Object getParamter(int type) {
        switch (type){
            case IPresenterBusinessCode.COMMON_USERINFO:
                HashMap<String,String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_MEMBERID, BaseApplication.getInstances().getLoginInfo().getUserId());
                return params;
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type){
            case IPresenterBusinessCode.COMMON_USERINFO:
                getDialog(getActivity(),getString(R.string.hint_common_get_data));//正在获取数据...
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type){
            case IPresenterBusinessCode.COMMON_USERINFO:
                break;
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type){
            case IPresenterBusinessCode.COMMON_USERINFO:
                setInfoResult(result);
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type){
            case IPresenterBusinessCode.COMMON_USERINFO:
                break;
        }
        if(errorMsg!=null && errorMsg.toString().length()>0){
            CommonDialogHint.getInstance().showHint(getActivity(),errorMsg.toString());
        }else{
            CommonDialogHint.getInstance().showHint(getActivity(),getString(R.string.error_common_net_request_fail));
        }
    }
}
