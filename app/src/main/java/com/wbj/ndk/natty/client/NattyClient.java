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

import android.os.Environment;

import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.util.common.LogUtil;
import com.wbj.ui.recorder.AudioFileFunc;

import java.io.File;
import java.io.FileOutputStream;

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



	public void onNativeCallback(int count)  { //debug
		LogUtil.e(" aaa onNativeCallback count = " + count);
	}

	//1
	public void ntyNativeLoginAckResult(char[] json, int length) {
		LogUtil.e(" ntyNativeLoginAckResult " + length);
	}
	//2
	public void ntyNativeBindResult(int arg) {
		LogUtil.e(" Bind Result " + arg);
		NattyProtocolFilter.ntyProtocolBind(arg);
	}
	//3
	public void ntyNativeUnBindResult(int arg) {
		LogUtil.e( " UnBind Result " + arg);
		NattyProtocolFilter.ntyProtocolUnBind(arg);
	}
	//4
	public void ntyNativeVoiceDataAckResult(int status) {
		LogUtil.e(" ntyVoiceDataAckResult " + status);
	}
	//5
	public void ntyNativeOfflineMsgAckResult(byte[] json, int length) {
		String str_json = new String(json);
		LogUtil.e( " ntyVoiceDataAckResult : " + str_json);
	}
	//6 主动接收数据  DataRoute 返回数据   ------
	//TODO 即：分两步
	public void ntyNativeDataResult(int status) {
		LogUtil.e( " ntyNativeDataResult " + status);
		LogUtil.e("走了主动拉数据 ------  只有状态");
	}

	//6.1
	public void ntyNativeDataResult(byte[] json,int status) {
		String str_json = new String(json);
		LogUtil.e( " ntyNativeDataResult " + status);
		LogUtil.e( " ntyNativeDataResult String " + str_json);
		NattyProtocolFilter.ntyProtocolTurn(str_json);
		NattyProtocolFilter.ntyDataResult(status,str_json);
	}
	//7
	public void ntyNativeVoiceBroadCastResult(long fromId, byte[] json, int status) {
		String str_json = new String(json);
		LogUtil.e( " ntyVoiceBroadCastResult （fromId）：" + fromId);
		LogUtil.e( " ntyVoiceBroadCastResult （status）" + status);
		LogUtil.e( " ntyVoiceBroadCastResult （byte[] json） " + str_json+ " ---- size :"+
		(json==null? 0: json.length)
		);
		NattyProtocolFilter.ntyProtocolVoiceResult(fromId,str_json);
//		NattyProtocolFilter.ntyProtocolRunTime(fromId,str_json,status);
	}
	//8
	public void ntyNativeLocationBroadCastResult(long fromId, byte[] json, int length) {
		String str_json = new String(json);
		LogUtil.e( " ntyNativeLocationBroadCastResult (fromId)" + fromId);
		LogUtil.e( " ntyNativeLocationBroadCastResult " + length);
		LogUtil.e( " ntyNativeLocationBroadCastResult String " + str_json);
		NattyProtocolFilter.ntyProtocolLocationResult(fromId,str_json);
	}
	//9  主动接收数据  CommonReq 返回数据
	public void ntyNativeCommonBoradCastResult(long fromId, byte[] json, int status) {
		String str_json = new String(json);
		LogUtil.e( " ntyNativeCommonBoradCastResult " + status);
		LogUtil.e( " ntyNativeCommonBoradCastResult String " + str_json);
		try{
			NattyProtocolFilter.ntyCommonBroadcastResult(status,str_json);
			NattyProtocolFilter.ntyProtocolRunTime(str_json);
			NattyProtocolFilter.ntyCommonBroadcastResult(fromId,str_json,status);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	//10
	public void onNativeDisconnect(int arg) {
		LogUtil.e(" Disconnect " + arg);
		NattyProtocolFilter.ntyProtocolDisconnect(arg);
	}
	//11
	public void onNativeReconnect(int arg) {
		LogUtil.e( " Reconnect " + arg);
		NattyProtocolFilter.ntyProtocolReconnect(arg);
	}

	//1.1
	public void ntyNativeLoginAckResult(byte[] json, int length) {
		LogUtil.e(  " ntyNativeLoginAckResult（参数类型变更） " + length);
	}
	//12
	public void ntyNativeDataRoute(long fromId, byte[] json, int status) {
		String str_json = new String(json);
		LogUtil.e( " ntyNativeDataRoute " + status);
		LogUtil.e( "187 ntyNativeDataRoute String " + str_json);
	}

	// 12.1
	public void ntyNativeDataRoute(byte[] json, int status) {
		String str_json = new String(json);
		LogUtil.e(" ntyNativeDataRoute (双参)" + status);
		LogUtil.e("186 ntyNativeDataRoute (双参)String " + str_json);
		NattyProtocolFilter.ntyDataRoute(status,str_json);
		NattyProtocolFilter.ntyProtocolDateRoute(str_json);
	}

	//13
	public void ntyNativePacketRecvResult(long fromId, long gId, int length) {
		LogUtil.e(" ------------------------------------------------------");
		LogUtil.e("Voice ntyNativePacketRecvResult fromId：" + fromId);
		LogUtil.e("Voice  ntyNativePacketRecvResult gId：" + gId);
		LogUtil.e("Voice  ntyNativePacketRecvResult length：" + length);
	}

	public String getReceiveFilePath(long currentTime){
		String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		//String fileBasePath = ctx.getCacheDir().getAbsolutePath();
		File dir = new File(fileBasePath + File.separator+ BaseApplication.getInstances().getPackageName()+ File.separator+"devices"+ File.separator +"voice");
		if(!dir.exists()){
			dir.mkdirs();
		}
		String mAudioAMRPath = dir.getAbsolutePath()+ File.separator+ "REC_"+currentTime+".amr";
		return mAudioAMRPath;
	}

	// 14
	public void ntyBindConfirmResult(long fromId, byte[] json, int status) {
		String str_json = new String(json);
		LogUtil.e(" ntyBindConfirmResult fromId:" + fromId);
		LogUtil.e(" ntyBindConfirmResult json:" + str_json);
		LogUtil.e(" ntyBindConfirmResult status:" + status);
//		NattyProtocolFilter.ntyProtocolBindConfirmResult(fromId,str_json);
		//TODO 这里收到发送的绑定请求
		NattyProtocolFilter.ntyProtocolAdminBindComfirmCallBack(fromId,str_json);
		/**
		 ntyBindConfirmResult fromId:11303
		 ntyBindConfirmResult json:{"Results":{"Proposer":"","UserName":"爸爸","MsgId":"26"}}
		 ntyBindConfirmResult status:60
		 */
	}
}
