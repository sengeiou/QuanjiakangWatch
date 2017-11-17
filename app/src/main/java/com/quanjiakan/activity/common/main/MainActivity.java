package com.quanjiakan.activity.common.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.ICommonData;
import com.quanjiakan.activity.base.ISetFragmentBeforeShow;
import com.quanjiakan.activity.common.image.ImageViewerActivity;
import com.quanjiakan.activity.common.index.healthinquiry.HealthInquiryCreateProblemActivity;
import com.quanjiakan.activity.common.index.housekeeper.HouseKeeperListActivity;
import com.quanjiakan.activity.common.main.fragment.MainMapFragment;
import com.quanjiakan.activity.common.main.fragment.MessageListFragment;
import com.quanjiakan.activity.common.main.fragment.SettingFragment;
import com.quanjiakan.constants.IActivityRequestValue;
import com.quanjiakan.constants.IParamsIntValue;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.device.entity.CommonNattyData;
import com.quanjiakan.view.SlidingMenu;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_tab_item_main)
    RelativeLayout mainTabItemMain;
    @BindView(R.id.main_tab_item_msg)
    RelativeLayout mainTabItemMsg;
    @BindView(R.id.main_tab_item_setting)
    RelativeLayout mainTabItemSetting;
    @BindView(R.id.main_tab_item_main_img)
    ImageView mainTabItemMainImg;
    @BindView(R.id.main_tab_item_msg_img)
    ImageView mainTabItemMsgImg;
    @BindView(R.id.main_tab_item_setting_img)
    ImageView mainTabItemSettingImg;
    @BindView(R.id.main_tab_item_main_name)
    TextView mainTabItemMainName;
    @BindView(R.id.main_tab_item_msg_name)
    TextView mainTabItemMsgName;
    @BindView(R.id.main_tab_item_setting_name)
    TextView mainTabItemSettingName;
    @BindView(R.id.main_tab_fragment_container)
    FrameLayout mainTabFragmentContainer;
    //TODO 侧滑菜单
    @BindView(R.id.hint)
    TextView hint;
    @BindView(R.id.inquiry)
    LinearLayout inquiry;
    @BindView(R.id.housekeeper)
    LinearLayout housekeeper;
    @BindView(R.id.old_care)
    LinearLayout oldCare;
    @BindView(R.id.child_missing)
    LinearLayout childMissing;
    @BindView(R.id.go_home)
    LinearLayout goHome;
    @BindView(R.id.shop)
    LinearLayout shop;
    @BindView(R.id.main_menu)
    RelativeLayout mainMenu;
    @BindView(R.id.sliding_menu)
    SlidingMenu slidingMenu;
    //TODO
    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ibtn_menu)
    ImageButton ibtnMenu;
    @BindView(R.id.menu_text)
    TextView menuText;


    /**
     */
    private Handler mHandler = new Handler();

    private int newIntentType;

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    //TODO
    private MainMapFragment fragmentMain;
    private MessageListFragment fragmentMessage;
    private SettingFragment fragmentSetting;
    private int currentFragment = ICommonData.MAIN_TAB_ITEM_NONE;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        ButterKnife.bind(this);


        initView();

        startNattySDK();
    }

    public void startNattySDK(){
        BaseApplication.getInstances().startSDK();
    }


    /**
     * *****************************************************************************************************************************
     * 生命周期函数
     */

    @Override
    public void onStart() {
        super.onStart();
        registerEventBus();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        //TODO 仅在登录后启动Natty---
        startNattyService();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterEventBus();
    }

    //TODO 避免不同业务导致的处理异常
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        newIntentType = intent.getIntExtra(IParamsName.PARAMS_MAIN_TYPE, 0);
        switch (newIntentType) {
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_SPECIFIC_POINT: {
                //TODO 展示某个特定的点

                break;
            }
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_WARN_SOS: {
                //TODO 提示特定点出现了SOS求救

                break;
            }
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_WARN_BOUND_OUT: {
                //TODO 提示某个特定点出现了越界

                break;
            }
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_WARN_BOUND_IN: {
                //TODO 提示某个特定点出现了越界

                break;
            }
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_WARN_UNWEAR: {
                //TODO 提示某个表出现了脱落

                break;
            }
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_WARN_FALLDOWN: {
                //TODO 提示某人发生了跌倒

                break;
            }
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_WARN_BIND: {
                //TODO 提示某人申请绑定

                break;
            }
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_CHECK_PERMISSION: {
                //TODO 提示某人申请查看健康档案（网页版的）

                break;
            }
            case IParamsIntValue.COMMON_MAIN_TYPE_SHOW_MATTERSS_WARN: {
                //TODO 提示睡眠监测仪的警报

                break;
            }
            default: {
                //TODO 暂未录入的类型，不进行处理

                break;
            }
        }
    }

    /**
     * *****************************************************************************************************************************
     * 启动Natty服务
     * 若出现断开连接，则进行Natty服务的重连（重启）
     */
    public void startNattyService(){
        //TODO 尚未启动过，则直接启动-------------SDK中存在重连机制，但仍存在问题尚未解决
        if(BaseApplication.getInstances().isNattyNull()){
            BaseApplication.getInstances().startSDK();
        }else{
        }
        //TODO 启动过，但出现断连了，则重新关闭后启动
//        else if(!BaseApplication.getInstances().isSDKConnected() &&
//                BaseApplication.getInstances().getNattyClient()!=null){
//            //
//            if(BaseApplication.getInstances().getNattyClient()!=null){
//                BaseApplication.getInstances().stopSDK();
//            }
//            BaseApplication.getInstances().startSDK();
//        }
    }
    /**
     * *****************************************************************************************************************************
     * 设置下方导航栏，以及点击交互
     */
    //TODO 设置当前选中的Tab选项
    public void setCurrentTabItem(int item) {
        setCurrentTabImage(item);
        setCurrentTabName(item);
        setCurrentTabFragment(item, null);

        initTitleBar(item);
    }

    /**
     * 方法本身仅做切换Fragment的操作，若在展示fragment前需要设置值或者进行其他操作，
     * 则可以通过传入一个接口实例对象，在接口中设置好相应的准备工作
     *
     * @param item
     * @param fragmentBeforeShow 若相应的Fragment对象在展示前需要进行某些操作，可以在这个对象中进行操作
     */
    public void setCurrentTabFragment(int item, ISetFragmentBeforeShow fragmentBeforeShow) {
        //TODO 特定类型的事务
        switch (item) {
            case ICommonData.MAIN_TAB_ITEM_MAIN: {

                if (currentFragment == ICommonData.MAIN_TAB_ITEM_MAIN) {
                    return;
                }
                startFragmentTransaction();
                hideOther(currentFragment);
                startFragmentTransaction();
                if (fragmentMain == null) {
                    fragmentMain = new MainMapFragment();
                    if (fragmentBeforeShow != null) {
                        fragmentBeforeShow.setFragmentValue();
                    }
                    transaction.add(R.id.main_tab_fragment_container, fragmentMain, ICommonData.MAIN_TAB_ITEM_MAIN_TAG);
                } else {
                    fragmentMain = (MainMapFragment) fragmentManager.findFragmentByTag(ICommonData.MAIN_TAB_ITEM_MAIN_TAG);
                    if (fragmentBeforeShow != null) {
                        fragmentBeforeShow.setFragmentValue();
                    }
                    transaction.show(fragmentMain);
                }

                currentFragment = ICommonData.MAIN_TAB_ITEM_MAIN;
                transaction.commit();
                break;
            }
            case ICommonData.MAIN_TAB_ITEM_MSG: {
                if (currentFragment == ICommonData.MAIN_TAB_ITEM_MSG) {
                    return;
                }
                startFragmentTransaction();
                hideOther(currentFragment);
                startFragmentTransaction();
                if (fragmentMessage == null) {
                    fragmentMessage = new MessageListFragment();
                    if (fragmentBeforeShow != null) {
                        fragmentBeforeShow.setFragmentValue();
                    }
                    transaction.add(R.id.main_tab_fragment_container, fragmentMessage, ICommonData.MAIN_TAB_ITEM_MSG_TAG);
                } else {
                    fragmentMessage = (MessageListFragment) fragmentManager.findFragmentByTag(ICommonData.MAIN_TAB_ITEM_MSG_TAG);
                    if (fragmentBeforeShow != null) {
                        fragmentBeforeShow.setFragmentValue();
                    }
                    transaction.show(fragmentMessage);
                }
                currentFragment = ICommonData.MAIN_TAB_ITEM_MSG;
                transaction.commit();
                break;
            }
            case ICommonData.MAIN_TAB_ITEM_SETTING: {
                if (currentFragment == ICommonData.MAIN_TAB_ITEM_SETTING) {
                    return;
                }
                startFragmentTransaction();
                hideOther(currentFragment);
                startFragmentTransaction();
                if (fragmentSetting == null) {
                    fragmentSetting = new SettingFragment();
                    if (fragmentBeforeShow != null) {
                        fragmentBeforeShow.setFragmentValue();
                    }
                    transaction.add(R.id.main_tab_fragment_container, fragmentSetting, ICommonData.MAIN_TAB_ITEM_SETTING_TAG);
                } else {
                    fragmentSetting = (SettingFragment) fragmentManager.findFragmentByTag(ICommonData.MAIN_TAB_ITEM_SETTING_TAG);
                    if (fragmentBeforeShow != null) {
                        fragmentBeforeShow.setFragmentValue();
                    }
                    transaction.show(fragmentSetting);
                }
                currentFragment = ICommonData.MAIN_TAB_ITEM_SETTING;
                transaction.commit();
                break;
            }
        }
    }

    public void hideOther(int item) {
        switch (item) {
            case ICommonData.MAIN_TAB_ITEM_MAIN: {
                if (fragmentMain != null) {
                    fragmentMain = (MainMapFragment) fragmentManager.findFragmentByTag(ICommonData.MAIN_TAB_ITEM_MAIN_TAG);
                    transaction.hide(fragmentMain);
                    transaction.commit();
                } else {
                    transaction.commit();
                }
                break;
            }
            case ICommonData.MAIN_TAB_ITEM_MSG: {
                if (fragmentMessage != null) {
                    fragmentMessage = (MessageListFragment) fragmentManager.findFragmentByTag(ICommonData.MAIN_TAB_ITEM_MSG_TAG);
                    transaction.hide(fragmentMessage);
                    transaction.commit();
                } else {
                    transaction.commit();
                }
                break;
            }
            case ICommonData.MAIN_TAB_ITEM_SETTING: {
                if (fragmentSetting != null) {
                    fragmentSetting = (SettingFragment) fragmentManager.findFragmentByTag(ICommonData.MAIN_TAB_ITEM_SETTING_TAG);
                    transaction.hide(fragmentSetting);
                    transaction.commit();
                } else {
                    transaction.commit();
                }
                break;
            }
        }
    }

    public void startFragmentTransaction() {
        //TODO 准备开始Fragment事务
        if (fragmentManager == null) {
            fragmentManager = getFragmentManager();
        }
        transaction = fragmentManager.beginTransaction();
    }

    public void setCurrentTabImage(int item) {
        //TODO 重置选中状态
        mainTabItemMainImg.setSelected(false);
        mainTabItemMsgImg.setSelected(false);
        mainTabItemSettingImg.setSelected(false);

        //TODO 设置选中状态
        switch (item) {
            case ICommonData.MAIN_TAB_ITEM_MAIN: {
                mainTabItemMainImg.setSelected(true);
                break;
            }
            case ICommonData.MAIN_TAB_ITEM_MSG: {
                mainTabItemMsgImg.setSelected(true);
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
        mainTabItemMsgName.setTextColor(getResources().getColor(R.color.main_tab_item_default_color));
        mainTabItemSettingName.setTextColor(getResources().getColor(R.color.main_tab_item_default_color));

        //TODO 设置选中状态
        switch (item) {
            case ICommonData.MAIN_TAB_ITEM_MAIN: {
                mainTabItemMainName.setTextColor(getResources().getColor(R.color.color_title_green));
                break;
            }
            case ICommonData.MAIN_TAB_ITEM_MSG: {
                mainTabItemMsgName.setTextColor(getResources().getColor(R.color.color_title_green));
                break;
            }
            case ICommonData.MAIN_TAB_ITEM_SETTING: {
                mainTabItemSettingName.setTextColor(getResources().getColor(R.color.color_title_green));
                break;
            }
        }
    }

    public void setFragment(int item) {
        if (fragmentManager == null) {
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

    public void initTitleBar(int item){
        //TODO 设置选中状态
        switch (item) {
            case ICommonData.MAIN_TAB_ITEM_MAIN: {
                //TODO
                ibtnBack.setVisibility(View.VISIBLE);
                ibtnBack.setImageResource(R.drawable.main_map_menu_item_left);

                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(R.string.main_tab_item_main_name_string);

                ibtnMenu.setVisibility(View.GONE);

                menuText.setVisibility(View.VISIBLE);
                menuText.setText(R.string.main_map_title_right_menu);
                break;
            }
            case ICommonData.MAIN_TAB_ITEM_MSG: {
                ibtnBack.setVisibility(View.GONE);
                ibtnBack.setImageResource(R.drawable.main_map_menu_item_left);

                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(R.string.main_tab_item_main_message_center_string);

                ibtnMenu.setVisibility(View.GONE);

                menuText.setVisibility(View.GONE);
                break;
            }
            case ICommonData.MAIN_TAB_ITEM_SETTING: {
                ibtnBack.setVisibility(View.VISIBLE);
                ibtnBack.setImageResource(R.drawable.main_map_menu_item_left);

                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(R.string.main_tab_item_main_setting_string);

                ibtnMenu.setVisibility(View.GONE);

                menuText.setVisibility(View.GONE);
                break;
            }
        }
    }
    /**
     * TODO 在使用include的情况下，若需要将整个include的layout进行隐藏，将可能出现下方的异常，导致APP出现Crash
     */
    /*
    10-17 11:49:15.083 21223-21223/com.quanjiakan.watch E/AndroidRuntime: FATAL EXCEPTION: main
                                                                      Process: com.quanjiakan.watch, PID: 21223
                                                                      java.lang.RuntimeException: Unable to start activity ComponentInfo{com.quanjiakan.watch/com.quanjiakan.activity.common.main.MainActivity}: java.lang.IllegalStateException: Required view 'rl_title' with ID 2131427522 for field 'rlTitle' was not found. If this view is optional add '@Nullable' (fields) or '@Optional' (methods) annotation.
                                                                          at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2560)
                                                                          at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2640)
                                                                          at android.app.ActivityThread.access$800(ActivityThread.java:182)
                                                                          at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1493)
                                                                          at android.os.Handler.dispatchMessage(Handler.java:111)
                                                                          at android.os.Looper.loop(Looper.java:194)
                                                                          at android.app.ActivityThread.main(ActivityThread.java:5682)
                                                                          at java.lang.reflect.Method.invoke(Native Method)
                                                                          at java.lang.reflect.Method.invoke(Method.java:372)
                                                                          at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:963)
                                                                          at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:758)
                                                                       Caused by: java.lang.IllegalStateException: Required view 'rl_title' with ID 2131427522 for field 'rlTitle' was not found. If this view is optional add '@Nullable' (fields) or '@Optional' (methods) annotation.
                                                                          at butterknife.internal.Utils.findRequiredView(Utils.java:138)
                                                                          at butterknife.internal.Utils.findRequiredViewAsType(Utils.java:150)
                                                                          at com.quanjiakan.activity.common.main.MainActivity_ViewBinding.<init>(MainActivity_ViewBinding.java:72)
                                                                          at java.lang.reflect.Constructor.newInstance(Native Method)
                                                                          at java.lang.reflect.Constructor.newInstance(Constructor.java:288)
                                                                          at butterknife.ButterKnife.createBinding(ButterKnife.java:199)
                                                                          at butterknife.ButterKnife.bind(ButterKnife.java:124)
                                                                          at com.quanjiakan.activity.common.main.MainActivity.onCreate(MainActivity.java:78)
                                                                          at android.app.Activity.performCreate(Activity.java:6161)
                                                                          at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1112)
                                                                          at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2507)
                                                                          at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2640) 
                                                                          at android.app.ActivityThread.access$800(ActivityThread.java:182) 
                                                                          at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1493) 
                                                                          at android.os.Handler.dispatchMessage(Handler.java:111) 
                                                                          at android.os.Looper.loop(Looper.java:194) 
                                                                          at android.app.ActivityThread.main(ActivityThread.java:5682) 
                                                                          at java.lang.reflect.Method.invoke(Native Method) 
                                                                          at java.lang.reflect.Method.invoke(Method.java:372) 
                                                                          at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:963) 
                                                                          at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:758) 
     */

    /**
     * *****************************************************************************************************************************
     */

    public void initView() {
        //TODO 设置默认的选中值--
        setCurrentTabItem(ICommonData.MAIN_TAB_ITEM_MAIN);

        initSlideMenu();
    }

    public void initSlideMenu() {
        //TODO 屏蔽掉点击事件的传递
        slidingMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        //TODO 设置侧滑菜单的自适应宽度
        resetSlideMenuWidth();
    }

    public void resetSlideMenuWidth() {
        mainMenu = (RelativeLayout) findViewById(R.id.main_menu);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mainMenu.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels / 2;
        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
        mainMenu.setLayoutParams(params);
    }

    public void onLogout() {
        BaseApplication.getInstances().onLogout(this);
    }

    /**
     * *****************************************************************************************************************************
     * EventBus 广播事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonNattyData(CommonNattyData result) {
        //TODO 控制无效的广播
        if (result == null) {
            return;
        }
        switch (result.getType()) {

        }
    }

    /**
     * *****************************************************************************************************************************
     */

    //TODO
    public void openSlideMenu() {
        slidingMenu.toggleMenu();
    }

    public void closeSlidemenu(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                slidingMenu.closeToggleMenu();
            }
        },400);
    }

    /**
     * *****************************************************************************************************************************
     * 跳转
     */

    //TODO
    public void goToBindDevice(){

    }

    public void testToImageViewer(){
        Intent intent = new Intent(this, ImageViewerActivity.class);
        // test url :  http://picture.quanjiakan.com:9080/quanjiakan/resources/housekeeper/20161109200708_v5wdnp.jpg
        intent.putExtra(IParamsName.PARAMS_URL,"http://picture.quanjiakan.com:9080/quanjiakan/resources/housekeeper/20161109200708_v5wdnp.jpg");
        startActivityForResult(intent, IActivityRequestValue.REQUEST_TO_IMAGE_VIEWER);
    }

    public void toHealthConsult(){
        Intent intent = new Intent(this, HealthInquiryCreateProblemActivity.class);
        startActivityForResult(intent,IActivityRequestValue.REQUEST_TO_HEALTH_CONSULT);
    }

    public void toHouseKeeper(){
        Intent intent = new Intent(this, HouseKeeperListActivity.class);
        startActivityForResult(intent,IActivityRequestValue.REQUEST_TO_HOUSE_KEEPER);
    }

    public void toRefferal(){
        Intent intent = new Intent(this, HouseKeeperListActivity.class);
        startActivityForResult(intent,IActivityRequestValue.REQUEST_TO_REFERRAL);
    }

    public void toMiss(){
        Intent intent = new Intent(this, HouseKeeperListActivity.class);
        startActivityForResult(intent,IActivityRequestValue.REQUEST_TO_MISS);
    }

    public void toBabyGoHome(){
        Intent intent = new Intent(this, HouseKeeperListActivity.class);
        startActivityForResult(intent,IActivityRequestValue.REQUEST_TO_BABY_GO_HOME);
    }

    public void toMall(){
        Intent intent = new Intent(this, HouseKeeperListActivity.class);
        startActivityForResult(intent,IActivityRequestValue.REQUEST_TO_MALL);
    }

    /**
     * *****************************************************************************************************************************
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case IActivityRequestValue.REQUEST_TO_IMAGE_VIEWER: {
                break;
            }
            case IActivityRequestValue.REQUEST_TO_HEALTH_CONSULT: {
                break;
            }
            case IActivityRequestValue.REQUEST_TO_HOUSE_KEEPER: {
                break;
            }
            case IActivityRequestValue.REQUEST_TO_REFERRAL: {
                break;
            }
            case IActivityRequestValue.REQUEST_TO_MISS: {
                break;
            }
            case IActivityRequestValue.REQUEST_TO_BABY_GO_HOME: {
                break;
            }
            case IActivityRequestValue.REQUEST_TO_MALL: {
                break;
            }
        }
        closeSlidemenu();
    }

    @OnClick({R.id.main_tab_item_main, R.id.main_tab_item_msg, R.id.main_tab_item_setting,
            R.id.hint, R.id.inquiry, R.id.housekeeper,
            R.id.old_care, R.id.child_missing, R.id.go_home,
            R.id.shop,R.id.tv_title,
            R.id.menu_text,R.id.ibtn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title:{
                //TODO 测试性调用
//                if(currentFragment==ICommonData.MAIN_TAB_ITEM_MAIN){
//                    fragmentMain.getBindWatchListFromNet();
//                }
                testToImageViewer();

                break;
            }
            case R.id.ibtn_back: {
                if(currentFragment==ICommonData.MAIN_TAB_ITEM_MAIN){
                    openSlideMenu();//TODO 打开侧边菜单
                }else if(currentFragment==ICommonData.MAIN_TAB_ITEM_SETTING){
                    onLogout();//TODO 测试退出
                }
                break;
            }
            case R.id.menu_text: {
                if(currentFragment==ICommonData.MAIN_TAB_ITEM_MAIN){
                    goToBindDevice();//TODO 跳至绑定设备
                }
                break;
            }
            case R.id.main_tab_item_main: {
                setCurrentTabItem(ICommonData.MAIN_TAB_ITEM_MAIN);
                break;
            }
            case R.id.main_tab_item_msg: {
                setCurrentTabItem(ICommonData.MAIN_TAB_ITEM_MSG);
                break;
            }
            case R.id.main_tab_item_setting: {
                setCurrentTabItem(ICommonData.MAIN_TAB_ITEM_SETTING);
                //TODO
                break;
            }
            //TODO **********************************
            //当处理完侧滑菜单点击事件后，先关闭菜单，然后执行
            case R.id.hint: {
                openSlideMenu();//TODO 直接关闭
                break;
            }
            case R.id.inquiry: {
                //TODO 免费问诊（健康咨询）
                toHealthConsult();
                break;
            }
            case R.id.housekeeper: {
                //TODO 家政
                toHouseKeeper();
                break;
            }
            case R.id.old_care: {
                //TODO 咨询转介
                toRefferal();
                break;
            }
            case R.id.child_missing: {
                //TODO 防走失
                toMiss();
                break;
            }
            case R.id.go_home: {
                //TODO 宝贝回家
                toBabyGoHome();
                break;
            }
            case R.id.shop: {
                //TODO 商城
                toMall();

                break;
            }
        }
    }

    /**
     * *****************************************************************************************************************************
     */

}
