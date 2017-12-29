package com.quanjiakan.activity.common.index.devices.old;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.BaseFragment;
import com.quanjiakan.activity.common.index.devices.old.health.HealthDynamicsActivity;
import com.quanjiakan.activity.common.index.devices.old.location.FreshLocationActivity;
import com.quanjiakan.constants.ICommonActivityRequestCode;
import com.quanjiakan.constants.ICommonActivityResultCode;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.db.entity.BindWatchInfoEntity;
import com.quanjiakan.device.entity.CommonNattyData;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.callphone.CallPhoneUtil;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.widget.CircleTransformation;
import com.quanjiakan.view.MaterialBadgeTextView;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.wbj.ndk.natty.client.NattyProtocolFilter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/17.
 */

public class WatchOldEntryFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.disconnected)
    LinearLayout disconnected;
    @BindView(R.id.status_line_4)
    ImageView statusLine4;
    @BindView(R.id.watch_battery_img)
    ImageView watchBatteryImg;
    @BindView(R.id.watch_battery_text)
    TextView watchBatteryText;
    @BindView(R.id.watch_battery_line)
    LinearLayout watchBatteryLine;
    @BindView(R.id.fall_battery_text)
    TextView fallBatteryText;
    @BindView(R.id.fall_battery_img)
    ImageView fallBatteryImg;
    @BindView(R.id.fall_battery_line)
    LinearLayout fallBatteryLine;
    @BindView(R.id.state_text)
    TextView stateText;
    @BindView(R.id.state_img)
    ImageView stateImg;
    @BindView(R.id.disconnect_img)
    ImageView disconnectImg;
    @BindView(R.id.state_line)
    LinearLayout stateLine;
    @BindView(R.id.user_header_img)
    ImageView userHeaderImg;
    @BindView(R.id.status)
    RelativeLayout status;
    @BindView(R.id.relate_equipment_icon)
    ImageView relateEquipmentIcon;
    @BindView(R.id.relate_equipment_name)
    TextView relateEquipmentName;
    @BindView(R.id.relate_equipment)
    LinearLayout relateEquipment;
    @BindView(R.id.tv_health)
    TextView tvHealth;
    @BindView(R.id.step_value)
    TextView stepValue;
    @BindView(R.id.health_dynamic_line)
    LinearLayout healthDynamicLine;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.health_value)
    TextView healthValue;
    @BindView(R.id.health_line)
    LinearLayout healthLine;
    @BindView(R.id.functionline1)
    LinearLayout functionline1;
    @BindView(R.id.wear_state_line)
    LinearLayout wearStateLine;
    @BindView(R.id.plan_line)
    LinearLayout planLine;
    @BindView(R.id.fance_line)
    LinearLayout fanceLine;
    @BindView(R.id.contact_line)
    LinearLayout contactLine;
    @BindView(R.id.function_items)
    LinearLayout functionItems;
    @BindView(R.id.contact_line2)
    LinearLayout contactLine2;
    @BindView(R.id.fare_line2)
    LinearLayout fareLine2;
    @BindView(R.id.manage_line2)
    LinearLayout manageLine2;
    @BindView(R.id.function_items2)
    LinearLayout functionItems2;
    @BindView(R.id.location)
    ImageView location;
    @BindView(R.id.call)
    ImageView call;
    @BindView(R.id.chat)
    ImageView chat;
    @BindView(R.id.notice)
    MaterialBadgeTextView notice;
    @BindView(R.id.fresh)
    SwipeRefreshLayout fresh;
    //*********************************
    @BindView(R.id.disconnectedImage)
    ImageView disconnectedImage;
    @BindView(R.id.disconnectedHint)
    TextView disconnectedHint;
    //*********************************
    @BindView(R.id.wear_state_line_img)
    ImageView wearStateLineImg;
    @BindView(R.id.wear_state_line_name)
    TextView wearStateLineName;
    @BindView(R.id.plan_line_img)
    ImageView planLineImg;
    @BindView(R.id.plan_line_name)
    TextView planLineName;
    @BindView(R.id.fance_line_img)
    ImageView fanceLineImg;
    @BindView(R.id.fance_line_name)
    TextView fanceLineName;
    @BindView(R.id.contact_line_img)
    ImageView contactLineImg;
    @BindView(R.id.contact_line_name)
    TextView contactLineName;
    @BindView(R.id.watch_fare_img)
    ImageView watchFareImg;
    @BindView(R.id.watch_fare_name)
    TextView watchFareName;
    @BindView(R.id.watch_fare)
    LinearLayout watchFare;
    @BindView(R.id.watch_manager_img)
    ImageView watchManagerImg;
    @BindView(R.id.watch_manager_name)
    TextView watchManagerName;
    @BindView(R.id.watch_manager)
    LinearLayout watchManager;
    @BindView(R.id.contact_line2_img)
    ImageView contactLine2Img;
    @BindView(R.id.contact_line2_name)
    TextView contactLine2Name;
    @BindView(R.id.fare_line2_img)
    ImageView fareLine2Img;
    @BindView(R.id.fare_line2_name)
    TextView fareLine2Name;
    @BindView(R.id.manage_line2_img)
    ImageView manageLine2Img;
    @BindView(R.id.manage_line2_name)
    TextView manageLine2Name;
    @BindView(R.id.locationName)
    TextView locationName;
    @BindView(R.id.callName)
    TextView callName;
    @BindView(R.id.chatName)
    TextView chatName;
    //***************************************************

    private BindWatchInfoEntity entity;

    /**
     * ************************************************************************************************************************
     * 生命周期方法
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_old_watch, container, false);//TODO 实例化整个布局
        unbinder = ButterKnife.bind(this, view);
        //TODO 设置默认值
        initView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        register();
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
        unregister();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * ************************************************************************************************************************
     */

    public void setEntity(BindWatchInfoEntity entity) {
        this.entity = entity;
    }

    /**
     * ************************************************************************************************************************
     */

    public void getConfig() {
        try {
            if (!BaseApplication.getInstances().isSDKConnected()) {
                CommonDialogHint.getInstance().showDisConnectDeviceServerDialog(getActivity());
                return;
            }
            JSONObject jsonObject = new JSONObject();
            long devid = Long.parseLong(entity.getImei(), 16);
            jsonObject.put("IMEI", entity.getImei());
            jsonObject.put("Action", "Get");
            jsonObject.put("Category", "Config");
            LogUtil.e("Config:" + jsonObject.toString());
            BaseApplication.getInstances().getNattyClient().ntyDataRouteClient(devid, jsonObject.toString().getBytes(), jsonObject.toString().length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initView() {
        //TODO 设置刷新
        fresh.setColorSchemeResources(R.color.holo_blue_light, R.color.holo_green_light, R.color.holo_orange_light);
        fresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO 刷新重新获取Config数据
                getConfig();
                fresh.setRefreshing(false);
            }
        });
        //TODO 在线状态
        initDisconnectView();
        setOnlineStatus(false);
        //TODO 设置默认头像、电量、信号
        initHeadImageAndBattery();
        //TODO 设置健康档案，健康动态页
        initHealth();
        //TODO 其他功能选项
        initOtherItem();


        getConfig();
    }


    //***********************************************************************
    //TODO 设置手表是否连接
    public void initDisconnectView() {
        disconnectedImage.setImageResource(R.drawable.old_ico_not_connected);
        disconnectedHint.setText(R.string.hint_device_connect_status);
    }

    public void setOnlineStatus(boolean isOnline) {
        if (isOnline) {
            disconnected.setVisibility(View.GONE);
        } else {
            disconnected.setVisibility(View.VISIBLE);
        }
    }

    //***********************************************************************
    // 初始化头像与电池
    public void initHeadImageAndBattery() {
        status.setVisibility(View.VISIBLE);

        statusLine4.setImageResource(R.drawable.old_bg_portrait);

        watchBatteryLine.setVisibility(View.VISIBLE);
        setBattery(ICommonData.BATTERY1);

        fallBatteryLine.setVisibility(View.VISIBLE);
        setBatteryFall(ICommonData.BATTERY1);

        stateLine.setVisibility(View.VISIBLE);
        stateImg.setImageResource(R.drawable.old_ico_down);
        stateText.setText(R.string.hint_device_fall_status);
        setWatchAndFallConnectionStatus(false);

        if(entity!=null && entity.getHeadImage()!=null && entity.getHeadImage().toLowerCase().startsWith(ICommonData.HTTP_PREFIX)){
            //TODO 需要使用圆形的裁剪
            Picasso.with(getActivity()).load(entity.getHeadImage()).
                    transform(new CircleTransformation()).into(userHeaderImg);
        }else{
            userHeaderImg.setImageResource(R.drawable.old_pic_portrait);
        }
    }

    public void setWatchAndFallConnectionStatus(boolean isConnected) {
        if (isConnected) {
            disconnectImg.setImageResource(R.drawable.old_ico_connect_down);
        } else {
            disconnectImg.setImageResource(R.drawable.old_ico_no_connect_down);
        }
    }

    public void setBattery(int status) {
        switch (status) {
            case ICommonData.BATTERY1:
                watchBatteryImg.setImageResource(R.drawable.baby_dainchi01_iocn);
                watchBatteryText.setText(R.string.hint_device_watch_battery);
                break;
            case ICommonData.BATTERY2:
                watchBatteryImg.setImageResource(R.drawable.baby_dainchi02_iocn);
                watchBatteryText.setText(R.string.hint_device_watch_battery);
                break;
            case ICommonData.BATTERY3:
                watchBatteryImg.setImageResource(R.drawable.baby_dainchi03_iocn);
                watchBatteryText.setText(R.string.hint_device_watch_battery);
                break;
            case ICommonData.BATTERY4:
                watchBatteryImg.setImageResource(R.drawable.baby_dainchi04_iocn);
                watchBatteryText.setText(R.string.hint_device_watch_battery);
                break;
            case ICommonData.BATTERY5:
                watchBatteryImg.setImageResource(R.drawable.baby_dainchi05_iocn);
                watchBatteryText.setText(R.string.hint_device_watch_battery);
                break;
            case ICommonData.BATTERY6:
                watchBatteryImg.setImageResource(R.drawable.baby_dainchi06_iocn);
                watchBatteryText.setText(R.string.hint_device_watch_battery);
                break;
            default:
                watchBatteryImg.setImageResource(R.drawable.baby_dainchi01_iocn);
                watchBatteryText.setText(R.string.hint_device_watch_battery);
                break;
        }
    }

    public void setBatteryFall(int status) {
        switch (status) {
            case ICommonData.BATTERY1:
                fallBatteryImg.setImageResource(R.drawable.baby_dainchi01_iocn);
                fallBatteryText.setText(R.string.hint_device_fall_battery);
                break;
            case ICommonData.BATTERY2:
                fallBatteryImg.setImageResource(R.drawable.baby_dainchi02_iocn);
                fallBatteryText.setText(R.string.hint_device_fall_battery);
                break;
            case ICommonData.BATTERY3:
                fallBatteryImg.setImageResource(R.drawable.baby_dainchi03_iocn);
                fallBatteryText.setText(R.string.hint_device_fall_battery);
                break;
            case ICommonData.BATTERY4:
                fallBatteryImg.setImageResource(R.drawable.baby_dainchi04_iocn);
                fallBatteryText.setText(R.string.hint_device_fall_battery);
                break;
            case ICommonData.BATTERY5:
                fallBatteryImg.setImageResource(R.drawable.baby_dainchi05_iocn);
                fallBatteryText.setText(R.string.hint_device_fall_battery);
                break;
            case ICommonData.BATTERY6:
                fallBatteryImg.setImageResource(R.drawable.baby_dainchi06_iocn);
                fallBatteryText.setText(R.string.hint_device_fall_battery);
                break;
            default:
                fallBatteryImg.setImageResource(R.drawable.baby_dainchi01_iocn);
                fallBatteryText.setText(R.string.hint_device_fall_battery);
                break;
        }
    }

    public void initHealth() {
        //TODO 其实这里健康动态，健康档案都应该 直接使用


        stepValue.setText(R.string.hint_device_health_check);
        healthValue.setText(R.string.hint_device_health_check);
    }

    public void initOtherItem() {
        functionItems.setVisibility(View.VISIBLE);
        //TODO 佩戴检测
        wearStateLine.setVisibility(View.VISIBLE);
        wearStateLineImg.setVisibility(View.VISIBLE);
        wearStateLineImg.setImageResource(R.drawable.baby_icon_testing);
        wearStateLineName.setText(R.string.hint_device_wear_check);
        //TODO 作息计划
        planLine.setVisibility(View.VISIBLE);
        planLineImg.setVisibility(View.VISIBLE);
        planLineImg.setImageResource(R.drawable.baby_icon_schedule);
        planLineName.setText(R.string.hint_device_plan);
        //TODO 电子围栏
        fanceLine.setVisibility(View.VISIBLE);
        fanceLineImg.setVisibility(View.VISIBLE);
        fanceLineImg.setImageResource(R.drawable.baby_icon_fence);
        fanceLineName.setText(R.string.hint_device_fance);
        //TODO 手表通讯录
        contactLine.setVisibility(View.VISIBLE);
        contactLineImg.setVisibility(View.VISIBLE);
        contactLineImg.setImageResource(R.drawable.baby_icon_list);
        contactLineName.setText(R.string.hint_device_contacts);

        functionItems2.setVisibility(View.VISIBLE);

        //TODO 手表话费
        watchFare.setVisibility(View.VISIBLE);
        watchFareImg.setVisibility(View.VISIBLE);
        watchFareImg.setImageResource(R.drawable.baby_icon_bill);
        watchFareName.setText(R.string.hint_device_fare);
        //TODO 手表管理
        watchManager.setVisibility(View.VISIBLE);
        watchManagerImg.setVisibility(View.VISIBLE);
        watchManagerImg.setImageResource(R.drawable.baby_icon_management);
        watchManagerName.setText(R.string.hint_device_manage);
    }

    /**
     * ************************************************************************************************************************
     */
    @Override
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.HEALTH_INQUIRY_CREATE_PROBLEM: {
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_MEMBERID, BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                return params;
            }
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.HEALTH_INQUIRY_CREATE_PROBLEM: {
                getDialog(getActivity(), getString(R.string.hint_health_inquiry_last_10_problem_hint));
                break;
            }
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.HEALTH_INQUIRY_CREATE_PROBLEM: {
                break;
            }
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.HEALTH_INQUIRY_CREATE_PROBLEM: {
                break;
            }
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.HEALTH_INQUIRY_CREATE_PROBLEM: {
                break;
            }
        }
        if (errorMsg != null && errorMsg.toString().length() > 0) {
            CommonDialogHint.getInstance().showHint(getActivity(), errorMsg.toString());
        } else {
            CommonDialogHint.getInstance().showHint(getActivity(), getString(R.string.error_common_net_request_fail));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ICommonActivityRequestCode.RELOAD_DATA: {
                if (resultCode == ICommonActivityResultCode.RELOAD_DATA) {

                }
                break;
            }
            case ICommonActivityRequestCode.RELOAD_LOCATION: {
                if (resultCode == ICommonActivityResultCode.RELOAD_LOCATION) {
                    //TODO 退出时，刷新数据库中的数据（更新地址）
                }
                break;
            }
        }
    }

    @OnClick({R.id.location, R.id.call, R.id.chat,
            R.id.health_dynamic_line, R.id.health_line,
            R.id.wear_state_line, R.id.plan_line, R.id.fance_line,
            R.id.contact_line, R.id.watch_fare, R.id.watch_manager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.location: {
                Intent intent = new Intent(getActivity(), FreshLocationActivity.class);
                intent.putExtra(IParamsName.PARAMS_DEVICE_ID,entity.getImei());
                intent.putExtra(IParamsName.PARAMS_COMMON_ID_IN_DB,entity.getId());
                startActivityForResult(intent,ICommonActivityRequestCode.RELOAD_LOCATION);
                break;
            }
            case R.id.call: {
                //TODO 电话，优先顺序为：先Config中的，然后WatchList中的
                if(call.getTag()!=null && call.getTag().toString().length()>0){
                    CallPhoneUtil.callPhoneNumber(this,call.getTag().toString());
                }else if(entity!=null && entity.getPhone()!=null && entity.getPhone().length()>0){
                    CallPhoneUtil.callPhoneNumber(this,entity.getPhone());
                }else{
                    CommonDialogHint.getInstance().showHint(getActivity(),getString(R.string.error_common_no_phone));
                }
                break;
            }
            case R.id.chat: {

                break;
            }
//*******************************************************************************************
            case R.id.health_dynamic_line: {//TODO 健康动态
                Intent intent = new Intent(getActivity(),HealthDynamicsActivity.class);
                intent.putExtra(IParamsName.PARAMS_DEVICE_ID,entity.getImei());
                startActivity(intent);
                break;
            }
            case R.id.health_line: {//TODO 健康档案
                break;
            }
//*******************************************************************************************
            case R.id.wear_state_line: {//TODO 佩戴检测
                break;
            }
            case R.id.plan_line: {//TODO 作息计划
                break;
            }
            case R.id.fance_line: {//TODO 电子围栏
                break;
            }
            case R.id.contact_line: {//TODO 手表通讯录
                break;
            }
//*******************************************************************************************
            case R.id.watch_fare: {//TODO 手表话费
                break;
            }
            case R.id.watch_manager: {//TODO 手表管理
                break;
            }
        }
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
            case NattyProtocolFilter.DISPLAY_UPDATE_DATA_COMMON_BROADCAST: {
                String stringData = result.getData();
                /**
                 {
                 "Result" : {
                 "IMEI": "355637052788650",
                 "Category": "Service",
                 "Service": {
                 "CustomerNum":"02039434080",
                 "DoctorNum":"01059131209",
                 }}}

                 获取客服电话----好像没什么用，业务上，打电话是直接打给，手表的（除非这个业务流程修改）
                 */
                try {
                    JSONObject boardcastResult = new JSONObject(stringData);
                    if (boardcastResult != null &&
                            boardcastResult.has("Results") &&
                            boardcastResult.getJSONObject("Results") != null) {
                        //TODO 实时报告

                        //TODO 定位推送
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case NattyProtocolFilter.DISPLAY_UPDATE_DATA_RESULT: {
                //TODO 有返回，则是命令的发送结果
                String stringData = result.getData();
                try {
                    JSONObject boardcastResult = new JSONObject(stringData);
                    /*
                    {Results : {"Code":"10001","Message":"10001"}}
                     */
                    if (boardcastResult != null &&
                            boardcastResult.has("Results") &&
                            boardcastResult.getJSONObject("Results") != null) {//TODO
                        if(boardcastResult.getJSONObject("Results").has("Code") &&
                                ICommonData.HTTP_OK.equals(boardcastResult.getJSONObject("Results").getString("Code"))){
                            //TODO 与服务器连接上了
                            setOnlineStatus(true);
                            //TODO 显示断联提示对话框
                        }else if(boardcastResult.getJSONObject("Results").has("Code") &&
                                ICommonData.HTTP_DEVICE_NOT_ONLINE.equals(boardcastResult.getJSONObject("Results").getString("Code"))){//
                            setOnlineStatus(false);
                            CommonDialogHint.getInstance().showNotOnlineHintDialog(getActivity());
                        }else{
                            setOnlineStatus(false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case NattyProtocolFilter.DISPLAY_UPDATE_DATA_ROUTE: {
                //TODO GetConfig   GetLocation
                String stringData = result.getData();
                try {
                    JSONObject boardcastResult = new JSONObject(stringData);
                    if (boardcastResult != null &&
                            boardcastResult.has("Results") &&
                            boardcastResult.getJSONObject("Results") != null) {//TODO
                        if(boardcastResult.getJSONObject("Results").has("Category")){//TODO 判断数据的类型
                            //TODO Config
                            if("Config".equals(boardcastResult.getJSONObject("Results").getString("Category")) &&
                                    boardcastResult.getJSONObject("Results").has("Config")){
                                //判定数据分类为Config
                                setOnlineStatus(true);//TODO 设置在线状态为在线

                                //TODO 设置手表电量
                                if(boardcastResult.getJSONObject("Results").getJSONObject("Config")!=null &&
                                        boardcastResult.getJSONObject("Results").getJSONObject("Config").has("Power")){//TODO 避免协议修改导致的空指针异常
                                    setBattery(Integer.parseInt(boardcastResult.getJSONObject("Results").getJSONObject("Config").getString("Power")));
                                }else{
                                    setBattery(ICommonData.BATTERY1);
                                }

                                //TODO 设置跌倒器电量
                                if(boardcastResult.getJSONObject("Results").getJSONObject("Config")!=null &&
                                        boardcastResult.getJSONObject("Results").getJSONObject("Config").has("TumblePower")){//TODO 避免协议修改导致的空指针异常
                                    setBatteryFall(Integer.parseInt(boardcastResult.getJSONObject("Results").getJSONObject("Config").getString("TumblePower")));
                                }else{
                                    setBatteryFall(ICommonData.BATTERY1);
                                }

                                //TODO 设置跌倒器佩戴状态
                                if(boardcastResult.getJSONObject("Results").getJSONObject("Config")!=null &&
                                        boardcastResult.getJSONObject("Results").getJSONObject("Config").has("TumbleStatus") &&
                                        //TODO 兼容下原来的老协议  1 为老协议中 佩戴时的状态
                                        (  "On".equals(boardcastResult.getJSONObject("Results").getJSONObject("Config").getString("TumbleStatus")) ||
                                          "1".equals(boardcastResult.getJSONObject("Results").getJSONObject("Config").getString("TumbleStatus")) )){//TODO 避免协议修改导致的空指针异常
                                    setWatchAndFallConnectionStatus(true);
                                }else{
                                    setWatchAndFallConnectionStatus(false);
                                }

                                //TODO 电话号码
                                if(boardcastResult.getJSONObject("Results").getJSONObject("Config")!=null &&
                                        boardcastResult.getJSONObject("Results").getJSONObject("Config").has("PhoneNum") &&
                                        boardcastResult.getJSONObject("Results").getJSONObject("Config").getString("PhoneNum").length()>0){//TODO 避免协议修改导致的空指针异常
                                    call.setTag(boardcastResult.getJSONObject("Results").getJSONObject("Config").getString("PhoneNum"));
                                }else{
                                    call.setTag(null);
                                }

                            }else{//TODO 其他数据

                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     *
     DISPLAY_UPDATE_DATA_ROUTE 数据回调 Config
     {
     "Results": {
     "IMEI": "355637052788650",
     "Category": "Config",
     "Config": {
     "Power": "5",
     "Signal": "86",
     "Steps": "2400",
     "WearStatus": "On",
     "TumblePower":"5",
     "TumbleStatus":"On",
     "PhoneNum": "15023450028",
     "Location": "113.2409402,23.1326885",
     }}}


     老人手表可能存在的数据回调
     ntyNativeCommonBoradCastResult String
     {"Results":{"IMEI":"355637053077723","Category": "RealTimeReport","RealTimeReport": {"Power":"2","Signal":"100","TumblePower":"255","TumbleStatus":"0","Steps":"1770","WearStatus":"1","Heart":"80","SBlood":"108","DBlood":"76","Type":"WIFI","Location":"113.2417166,23.1321026"}}}

     ntyNativeCommonBoradCastResult String
     {"Results":{"IMEI":"355637053077723","Category": "LocationReport","LocationReport": {"Type":"WIFI","Radius":"38","Location":"113.2417166,23.1321026"}}}
     */
}
