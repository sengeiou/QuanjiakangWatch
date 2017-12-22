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
import com.quanjiakan.constants.ICommonActivityRequestCode;
import com.quanjiakan.constants.ICommonActivityResultCode;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.device.entity.CommonNattyData;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.view.MaterialBadgeTextView;
import com.umeng.analytics.MobclickAgent;
import com.wbj.ndk.natty.client.NattyProtocolFilter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.plan_line2)
    LinearLayout planLine2;
    @BindView(R.id.fance_line2)
    LinearLayout fanceLine2;
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
    @BindView(R.id.disconnectedImage)
    ImageView disconnectedImage;
    @BindView(R.id.disconnectedHint)
    TextView disconnectedHint;
    //***************************************************

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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case NattyProtocolFilter.DISPLAY_UPDATE_DATA_RESULT: {
                //TODO 有返回，则是命令的发送结果
                String stringData = result.getData();
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case NattyProtocolFilter.DISPLAY_UPDATE_DATA_ROUTE: {
                //TODO GetConfig   GetLocation
                String stringData = result.getData();
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * ************************************************************************************************************************
     */

    public void getConfig() {
        try {
            if (!BaseApplication.getInstances().isSDKConnected()) {
                LogUtil.e("SDK 断联");
                CommonDialogHint.getInstance().showDisConnectDeviceServerDialog(getActivity());
                return;
            }
//            JSONObject jsonObject = new JSONObject();
//            long devid = Long.parseLong(device_id, 16);
//            jsonObject.put("IMEI", device_id);
//            jsonObject.put("Action", "Get");
//            jsonObject.put("Category", "Config");
//            LogUtil.e("Config:" + jsonObject.toString());
//            BaseApplication.getInstances().getNattyClient().ntyDataRouteClient(devid, jsonObject.toString().getBytes(), jsonObject.toString().length());
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


        getConfig();
    }
    //***********************************************************************
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
    public void initHeadImageAndBattery(){
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


        userHeaderImg.setImageResource(R.drawable.old_pic_portrait);
    }

    public void setWatchAndFallConnectionStatus(boolean isConnected){
        if (isConnected){
            disconnectImg.setImageResource(R.drawable.old_ico_connect_down);
        }else{
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

    public void initHealth(){
        //TODO 其实这里健康动态，健康档案都应该 直接使用



        stepValue.setText(R.string.hint_device_health_check);
        healthValue.setText(R.string.hint_device_health_check);
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
        }
    }

    @OnClick({R.id.location, R.id.call, R.id.chat,R.id.health_dynamic_line,
    R.id.health_line})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.location: {
                break;
            }
            case R.id.call: {
                break;
            }
            case R.id.chat: {
                break;
            }
            case R.id.health_dynamic_line: {//TODO 健康动态
                break;
            }
            case R.id.health_line: {//TODO 健康档案
                break;
            }
        }
    }
}
