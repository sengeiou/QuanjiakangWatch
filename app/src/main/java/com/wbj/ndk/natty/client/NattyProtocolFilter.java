package com.wbj.ndk.natty.client;

import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.device.entity.CommonBindResult;
import com.quanjiakan.device.entity.CommonNattyData;
import com.quanjiakan.device.entity.CommonVoiceData;
import com.quanjiakan.util.common.LogUtil;

import org.greenrobot.eventbus.EventBus;

public class NattyProtocolFilter {

    public final static int DISPLAY_UPDATE_CONTROL_POWER = 0x11;
    public final static int DISPLAY_UPDATE_CONTROL_SIGNAL = 0x12;
    public final static int DISPLAY_UPDATE_CONTROL_LOCATION = 0x13;
    public final static int DISPLAY_UPDATE_CONTROL_FALL = 0x14;
    public final static int DISPLAY_UPDATE_CONTROL_STEP = 0x15;
    public final static int DISPLAY_UPDATE_CONTROL_HEARTRATE = 0x16;
    public final static int DISPLAY_UPDATE_CONTROL_RECONNECTED = 0x17;
    public final static int DISPLAY_UPDATE_CONTROL_DISCONNECT = 0x18;
    public final static int DISPLAY_UPDATE_CONTROL_NOEXIST = 0x19;

    public final static int DISPLAY_UPDATE_CONTROL_BIND_RESULT = 0x1A;
    public final static int DISPLAY_UPDATE_CONTROL_UNBIND_RESULT = 0x1B;
    public final static int DISPLAY_UPDATE_STATUS = 0x1C;
    public final static int DISPLAY_UPDATE_CONFIG = 0x1D;

    public final static int VOICE_UPLOAD = 0x2A;
    public final static int VOICE_RECORD = 0x2B;
    public final static int VOICE_STOP = 0x2C;
    public final static int DISPLAY_VOICE_PLAY = 0x2D;

    public final static int DISPLAY_VOICE_SEND_RESULT = 0x2E;

    public final static int DISPLAY_EFENCE_RESULT = 0x30;
    public final static int DISPLAY_EFENCELIST_RESULT = 0x31;


    public final static int DISPLAY_UPDATE_CONTROL_LOCATION_NEW = 0x41;
    public final static int DISPLAY_UPDATE_DATA_RESULT = 0x42;
    public final static int DISPLAY_UPDATE_DATA_ROUTE = 0x43;
    public final static int DISPLAY_UPDATE_DATA_COMMON_BROADCAST = 0x44;
    public final static int DISPLAY_UPDATE_DATA_ADMIN_BIND_INFO = 0x45;



    public static void ntyProtocolFilter(String recv) {
    }

    public static void ntyProtocolFilter(String id, String recv) {
    }

    /**
     * case 1:
     * [MBProgressHUD showError:@"该UserID不存在"];
     * break;
     * case 2:
     * [MBProgressHUD showError:@"该设备ID不存在"];
     * break;
     * case 3:
     * [MBProgressHUD showError:@"UserId与DeviceId已经绑定过了"];
     * break;
     * case 4:
     * [MBProgressHUD showError:@"该设备未激活，请激活"];
     * break;
     * <p>
     * case 0:  绑定成功
     *
     * @param len
     */
    public static void ntyProtocolBind(int len) {
        EventBus.getDefault().post(new CommonNattyData(DISPLAY_UPDATE_CONTROL_BIND_RESULT,len + ""));
    }

    public static void ntyProtocolUnBind(int len) {
        EventBus.getDefault().post(new CommonNattyData(DISPLAY_UPDATE_CONTROL_UNBIND_RESULT,len + ""));
    }


    public static void ntyProtocolReconnect(int len) {
        BaseApplication.getInstances().setSDKServerStatus("1");
    }

    public static void ntyProtocolDisconnect(int len) {
        BaseApplication.getInstances().setSDKServerStatus("-1");
    }

    //{"Results":{"IMEI":"352315052834187","Proposer":"18011935659","UserName":"爸爸","MsgId":"40"}}
    public static void ntyProtocolAdminBindComfirmCallBack(long fromID,String info) {
        EventBus.getDefault().post(new CommonBindResult(DISPLAY_UPDATE_DATA_ADMIN_BIND_INFO,fromID,info));
        LogUtil.e("绑定申请的数据-------------------------"+info);
        //{"Results":{"IMEI":"352315052834187","Proposer":"18011935659","UserName":"爸爸","MsgId":"40"}}
    }

    public static void ntyProtocolNoExist(int len) {
    }

    public static void ntyProtocolPlayVoice(int len, byte[] buffer, long fromId) {
        EventBus.getDefault().post(new CommonVoiceData(DISPLAY_VOICE_PLAY,buffer,len+"",fromId+""));
    }

    public static void ntyProtocolSendVoiceResult(int len, long fromId) {
    }

    public static void ntyProtocolSendSuccess(long fromId, int len) {
    }

    public static void ntyProtocolSendFail(long fromId, int len) {
    }

    public static void ntyProtocolLocationResult(long id, String data) {
        EventBus.getDefault().post(new CommonNattyData(DISPLAY_UPDATE_CONTROL_LOCATION_NEW,data));
    }

    public static void ntyProtocolVoiceResult(long id, String data) {
    }

    public static void ntyDataResult(long id, String data) {
        EventBus.getDefault().post(new CommonNattyData(DISPLAY_UPDATE_DATA_RESULT,data));
    }

    public static void ntyDataRoute(long id, String data) {
        EventBus.getDefault().post(new CommonNattyData(DISPLAY_UPDATE_DATA_ROUTE,data));
    }

    public static void ntyCommonBroadcastResult(long id, String data) {
        EventBus.getDefault().post(new CommonNattyData(DISPLAY_UPDATE_DATA_COMMON_BROADCAST,data));
    }

    /**
     * 保存广播数据
     *
     * @param id
     * @param data
     * @param status
     */
    public static void ntyCommonBroadcastResult(long id, String data, int status)  {
        EventBus.getDefault().post(new CommonNattyData(DISPLAY_UPDATE_DATA_COMMON_BROADCAST,data));
    }

    /******
     * Turn and runtime
     *********/

    public static void ntyProtocolTurn(String json) {

    }

    /******
     * Turn and runtime
     *********/

    public static void ntyProtocolRunTime(String json) {

    }

    public static void ntyProtocolDateRoute(String json) {
    }

    public static void ntyProtocolBindConfirmResult(long fromId,String json) {

    }

}
