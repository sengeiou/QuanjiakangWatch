/*
 *  Author : WangBoJing , email : 1989wangbojing@gmail.com
 * 
 *  Copyright Statement:
 *  --------------------
 *  This software is protected by Copyright and the information contained
 *  herein is confidential. The software may not be copied and the information
 *  contained herein may not be used or disclosed except with the written
 *  permission of Author. (C) 2016
 * 
 *
 
****       *****
  ***        *
  ***        *                         *               *
  * **       *                         *               *
  * **       *                         *               *
  *  **      *                        **              **
  *  **      *                       ***             ***
  *   **     *       ******       ***********     ***********    *****    *****
  *   **     *     **     **          **              **           **      **
  *    **    *    **       **         **              **           **      *
  *    **    *    **       **         **              **            *      *
  *     **   *    **       **         **              **            **     *
  *     **   *            ***         **              **             *    *
  *      **  *       ***** **         **              **             **   *
  *      **  *     ***     **         **              **             **   *
  *       ** *    **       **         **              **              *  *
  *       ** *   **        **         **              **              ** *
  *        ***   **        **         **              **               * *
  *        ***   **        **         **     *        **     *         **
  *         **   **        **  *      **     *        **     *         **
  *         **    **     ****  *       **   *          **   *          *
*****        *     ******   ***         ****            ****           *
                                                                       *
                                                                      *
                                                                  *****
                                                                  ****


 *
 */


package com.wbj.ndk.natty.client;

import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.util.common.LogUtil;

import java.io.File;

public class NattyClient {
    static {
        System.loadLibrary("natty-client");
    }


    //App 双十一接口
    public native int ntyGetVoiceBufferSize();

    public native byte[] ntyGetVoiceBuffer();

    public native void ntySetSelfId(long selfId);

    public native byte[] ntyGetVersion();

    public native void ntyClientInitilize();

    public native void nativeInitilize(); //debug

    public native void nativeThreadStart(); //debug

    public native void nativeThreadStop(); //debug

    public native void ntyBindClient(long did, byte[] json, int length);

    public native void ntyUnBindClient(long did);

    public native int ntyBindConfirmReqClient(long proposerId, long devId, int msgId, byte[] json, int length);

    public native int ntyStartupClient();

    public native void ntyShutdownClient();

    public native int ntyCommonReqClient(long gId, byte[] json, int length);

    public native int ntyDataRouteClient(long gId, byte[] json, int length);

    public native int ntyVoiceDataReqClient(long gId, byte[] data, int length);


    //**************************************************


    //**************************************************


    public void onNativeCallback(int count) { //debug
    }

    //1
    public void ntyNativeLoginAckResult(char[] json, int length) {

    }
    //**********************************************************************************************************************************************************
    //2 绑定结果
    public void ntyNativeBindResult(int arg) {
        LogUtil.e(" ntyNativeBindResult " + arg);
        NattyProtocolFilter.ntyProtocolBind(arg);
    }

    //3 解绑结果
    public void ntyNativeUnBindResult(int arg) {
        LogUtil.e(" ntyNativeUnBindResult " + arg);
        NattyProtocolFilter.ntyProtocolUnBind(arg);
    }
    //**********************************************************************************************************************************************************
    //4  语音数据响应结果
    public void ntyNativeVoiceDataAckResult(int status) {
        LogUtil.e(" ntyNativeVoiceDataAckResult " + status);
    }
    //**********************************************************************************************************************************************************
    //5  离线消息响应结果
    public void ntyNativeOfflineMsgAckResult(byte[] json, int length) {
        String str_json = new String(json);
        LogUtil.e(" ntyNativeOfflineMsgAckResult : " + str_json);
    }
    //**********************************************************************************************************************************************************
    //6 仅含有状态的DataResult
    //TODO 即：分两步
    public void ntyNativeDataResult(int status) {

    }

    //6.1  含数据的DataResult
    public void ntyNativeDataResult(byte[] json, int status) {
        String str_json = new String(json);
        LogUtil.e(" ntyNativeDataResult String " + str_json);
        NattyProtocolFilter.ntyDataResult(status, str_json);

    }
    //**********************************************************************************************************************************************************
    //7  语音广播
    public void ntyNativeVoiceBroadCastResult(long fromId, byte[] json, int status) {
        String str_json = new String(json);
        LogUtil.e(" ntyNativeVoiceBroadCastResult （byte[] json） " + str_json);
        NattyProtocolFilter.ntyProtocolVoiceResult(fromId, str_json);
    }
    //**********************************************************************************************************************************************************
    //8  定位广播
    public void ntyNativeLocationBroadCastResult(long fromId, byte[] json, int length) {
        String str_json = new String(json);
        LogUtil.e(" ntyNativeLocationBroadCastResult String " + str_json);
        NattyProtocolFilter.ntyProtocolLocationResult(fromId, str_json);
    }
    //**********************************************************************************************************************************************************
    //9  广播数据
    public void ntyNativeCommonBoradCastResult(long fromId, byte[] json, int status) {
        String str_json = new String(json);
        LogUtil.e(" ntyNativeCommonBoradCastResult String " + str_json);
        NattyProtocolFilter.ntyCommonBroadcastResult(fromId, str_json, status);
    }
    //**********************************************************************************************************************************************************
    //10  SDK 断联
    public void onNativeDisconnect(int arg) {
        LogUtil.e(" Disconnect " + arg);
        NattyProtocolFilter.ntyProtocolDisconnect(arg);
    }

    //11  SDK 重连
    public void onNativeReconnect(int arg) {
        LogUtil.e(" Reconnect " + arg);
        NattyProtocolFilter.ntyProtocolReconnect(arg);
    }
    //**********************************************************************************************************************************************************
    //1.1  登录响应结果
    public void ntyNativeLoginAckResult(byte[] json, int length) {
        String str_json = new String(json);
        LogUtil.e(" SDK Login Result: "+str_json +"\nLength:"+ length);
    }
    //**********************************************************************************************************************************************************

    //12   DataRoute 数据
    public void ntyNativeDataRoute(long fromId, byte[] json, int status) {
        String str_json = new String(json);
        LogUtil.e("187 ntyNativeDataRoute String " + str_json);
        NattyProtocolFilter.ntyDataRoute(status, str_json,status);
    }

    // 12.1
    public void ntyNativeDataRoute(byte[] json, int status) {
        String str_json = new String(json);
        LogUtil.e("186 ntyNativeDataRoute (双参)String " + str_json);
        NattyProtocolFilter.ntyDataRoute(status, str_json);
    }
    //**********************************************************************************************************************************************************
    //13  接收到语音
    public void ntyNativePacketRecvResult(long fromId, long gId, int length) {
        LogUtil.e(" --------------  ntyNativePacketRecvResult ----------------------------------------");
        NattyProtocolFilter.ntyProtocolSaveVoiceFileToLocal(this,fromId,gId,length);
    }

    public String getReceiveFilePath(long currentTime) {
        File dir = BaseApplication.getInstances().getExternalFilesDir("voice");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String mAudioAMRPath = dir.getAbsolutePath() + File.separator + "REC_" + currentTime + ".amr";
        return mAudioAMRPath;
    }
    //**********************************************************************************************************************************************************
    // 14  来自其他用户的绑定申请
    public void ntyBindConfirmResult(long fromId, byte[] json, int status) {
        String str_json = new String(json);
        LogUtil.e(" ntyBindConfirmResult json:" + str_json);
        //TODO 这里收到发送的绑定请求
        NattyProtocolFilter.ntyProtocolDealBindReqesutFromOther(fromId, str_json);
        /**
         ntyBindConfirmResult fromId:11303
         ntyBindConfirmResult json:{"Results":{"Proposer":"","UserName":"爸爸","MsgId":"26"}}
         ntyBindConfirmResult status:60
         */
    }

    //**********************************************************************************************************************************************************
    //TODO  Push 消息
    //TODO
    public void ntyNativeMessagePush(long fromId, byte[] json, int status) {
        NattyProtocolFilter.ntyCommonPushMessageResult(fromId, new String(json), status);
    }

    //TODO
    public void ntySetMessagePushResult(long fromId, byte[] json, int status) {
        NattyProtocolFilter.ntySetPushMessageResult(fromId, new String(json), status);
    }

    //TODO
    public void ntySetCommonReqResult(long fromId, byte[] json, int status) {
        NattyProtocolFilter.ntySetCommonReqResult(fromId, new String(json), status);
    }

    public void ntyNativeCommonReqResult(long fromId, byte[] json, int status) {
        NattyProtocolFilter.ntyNativeCommonReqResult(fromId, new String(json), status);
    }
}
