package com.quanjiakan.activity.common.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.ICommonData;
import com.quanjiakan.constants.IParamsIntValue;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.watch.R;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.logout)
    TextView logout;
    @BindView(R.id.main_tab_item_main)
    RelativeLayout mainTabItemMain;
    @BindView(R.id.main_tab_item_setting)
    RelativeLayout mainTabItemSetting;
    @BindView(R.id.main_tab_item_main_img)
    ImageView mainTabItemMainImg;
    @BindView(R.id.main_tab_item_setting_img)
    ImageView mainTabItemSettingImg;
    @BindView(R.id.main_tab_item_main_name)
    TextView mainTabItemMainName;
    @BindView(R.id.main_tab_item_setting_name)
    TextView mainTabItemSettingName;
    @BindView(R.id.main_tab_fragment_container)
    FrameLayout mainTabFragmentContainer;

    private int newIntentType;

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        ButterKnife.bind(this);


        initView();
    }

    /**
     * *****************************************************************************************************************************
     * 生命周期函数
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        newIntentType = intent.getIntExtra(IParamsName.PARAMS_MAIN_TYPE,0);
        switch (newIntentType){
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_SPECIFIC_POINT:{
                //TODO 展示某个特定的点

                break;
            }
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_WARN_SOS:{
                //TODO 提示特定点出现了SOS求救

                break;
            }
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_WARN_BOUND_OUT:{
                //TODO 提示某个特定点出现了越界

                break;
            }
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_WARN_BOUND_IN:{
                //TODO 提示某个特定点出现了越界

                break;
            }
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_WARN_UNWEAR:{
                //TODO 提示某个表出现了脱落

                break;
            }
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_WARN_FALLDOWN:{
                //TODO 提示某人发生了跌倒

                break;
            }
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_WARN_BIND:{
                //TODO 提示某人申请绑定

                break;
            }
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_CHECK_PERMISSION:{
                //TODO 提示某人申请查看健康档案（网页版的）

                break;
            }
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_MATTERSS_WARN:{
                //TODO 提示睡眠监测仪的警报

                break;
            }
            default:{
                //TODO 暂未录入的类型，不进行处理

                break;
            }
        }
    }
    /**
     * *****************************************************************************************************************************
     * 设置下方导航栏，以及点击交互
     */
    //TODO 设置当前选中的Tab选项
    public void setCurrentTabItem(int item) {
        setCurrentTabImage(item);
        setCurrentTabName(item);
        setCurrentTabFragment(item);
    }

    public void setCurrentTabFragment(int item) {
        switch (item) {
            case ICommonData.MAIN_TAB_ITEM_MAIN: {
                break;
            }
            case ICommonData.MAIN_TAB_ITEM_MSG: {
                break;
            }
            case ICommonData.MAIN_TAB_ITEM_SETTING: {
                break;
            }
        }
    }

    public void setCurrentTabImage(int item) {
        //TODO 重置选中状态
        mainTabItemMainImg.setSelected(false);
        mainTabItemSettingImg.setSelected(false);

        //TODO 设置选中状态
        switch (item) {
            case ICommonData.MAIN_TAB_ITEM_MAIN: {
                mainTabItemMainImg.setSelected(true);
                break;
            }
            case ICommonData.MAIN_TAB_ITEM_MSG: {
                break;
            }
            case ICommonData.MAIN_TAB_ITEM_SETTING: {
                mainTabItemSettingImg.setSelected(true);
                break;
            }
        }
    }

    public void setCurrentTabName(int item) {
        //TODO 重置选中状态
        mainTabItemMainName.setTextColor(getResources().getColor(R.color.main_tab_item_default_color));
        mainTabItemSettingName.setTextColor(getResources().getColor(R.color.main_tab_item_default_color));

        //TODO 设置选中状态
        switch (item) {
            case ICommonData.MAIN_TAB_ITEM_MAIN: {
                mainTabItemMainName.setTextColor(getResources().getColor(R.color.color_title_green));
                break;
            }
            case ICommonData.MAIN_TAB_ITEM_MSG: {
                break;
            }
            case ICommonData.MAIN_TAB_ITEM_SETTING: {
                mainTabItemSettingName.setTextColor(getResources().getColor(R.color.color_title_green));
                break;
            }
        }
    }

    public void setFragment(int item){
        if(fragmentManager==null){
            fragmentManager = getFragmentManager();
        }
        transaction = fragmentManager.beginTransaction();

        switch (item) {
            case ICommonData.MAIN_TAB_ITEM_MAIN: {
                break;
            }
            case ICommonData.MAIN_TAB_ITEM_MSG: {
                break;
            }
            case ICommonData.MAIN_TAB_ITEM_SETTING: {
                break;
            }
        }
    }



    /**
     * *****************************************************************************************************************************
     */

    public void initView() {
        //TODO 设置默认的选中值--
        setCurrentTabItem(ICommonData.MAIN_TAB_ITEM_MAIN);
    }

    @OnClick(R.id.main_tab_item_setting)
    public void onClick() {
        //TODO
        onLogout();
    }

    public void onLogout() {
        BaseApplication.getInstances().onLogout(this);
    }

    /**
     * *****************************************************************************************************************************
     */

    @OnClick({R.id.main_tab_item_main, R.id.main_tab_item_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_tab_item_main:
                setCurrentTabItem(ICommonData.MAIN_TAB_ITEM_MAIN);
                break;
            case R.id.main_tab_item_setting:
                setCurrentTabItem(ICommonData.MAIN_TAB_ITEM_SETTING);
                break;
        }
    }
}
