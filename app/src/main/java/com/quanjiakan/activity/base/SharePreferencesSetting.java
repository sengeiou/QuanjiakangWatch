package com.quanjiakan.activity.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;

import com.quanjiakan.util.common.MessageDigestUtil;

import java.util.ArrayList;

public class SharePreferencesSetting {
	
	private final String TAG = "LOGUTIL "+SharePreferencesSetting.class.getSimpleName();
	private static SharePreferencesSetting instance = null;
	private static boolean bInited = false;
	private Context context = null;
	private SharedPreferences sharedPreferences;
	private final String WECHAT_PAY = "wechat_pay_result_info";
	private final String WECHAT_PAY_BUSINESS_LAST_PAY = "wechat_pay_last_pay_info";

	private final String WECHAT_PAY_INFO = "wechat_pay_result_info_order";
	private final String PREFERENCES_FILE = "quanjiakang_setting";
	private final String UPDATE_TIME = "update_time";
	private final String SDK_SERVER_STATUS = "sdk_server_status";

	private final String SPLIT = ";";

	public static void init(Context context) {
		if (context == null) {
			throw new NullPointerException();
		}
		if (!bInited) {
			instance = new SharePreferencesSetting(context);
			bInited = true;
		}
	}

	public static SharePreferencesSetting getInstance() {
		return instance;
	}

	private SharePreferencesSetting(Context con) {
		context = con;
		sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
		sharedPreferences
				.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
					public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
							String key) {
						Log.v(TAG, "onSharedPreferenceChanged==>" + key);
					}
				});
	}
	
	/**
	 *************************************************************
	 * SDK 连接状态
     */
	public int getSDKServerStatus() {
		return context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getInt(SDK_SERVER_STATUS,-1);
	}

	public void setSDKServerStatus(int userId) {
		Editor editor = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
		editor.putInt(SDK_SERVER_STATUS, userId);
		editor.commit();
	}

	/**
	 ************************************************************
	 * APP更新时间信息--跟用户无关
	 *
	 */

	public String getUpdateTime() {
		return context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getString(UPDATE_TIME,"");
	}

	public void setUpdateTime(String userName) {
		Editor editor = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
		editor.putString(UPDATE_TIME, userName);
		editor.commit();
	}

	/**
	 ************************************************************
	 * TODO 通用的存储
	 */
	public String getKeyValue(String key) {
		return context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getString(key,"");
	}

	public void setKeyValue(String key, String value) {
		Editor editor = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 ************************************************************
	 * TODO 通用的存储（整型）
	 */

	public int getKeyNumberValue(String key) {
		return context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getInt(key,0);
	}

	public void setKeyNumberValue(String key, int value) {
		Editor editor = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 ************************************************************
	 * 保存微信支付的订单信息（订单号）
	 *
	 *	目前考虑使用方式为
	 *  保存的值格式：     订单ID;订单支付状态
	 *  key值格式：固定前缀;用户ID;业务类型号码
	 */

	public String getWxPayOrder(int businessTypeCode) {
		return context.getSharedPreferences(WECHAT_PAY, Context.MODE_PRIVATE).
				getString(WECHAT_PAY_INFO+SPLIT+BaseApplication.getInstances().getLoginInfo().getUserId()+SPLIT+businessTypeCode,"");
	}

	public void setWxPayOrder(int businessTypeCode,String orderId,WXPayResult result) {
		Editor editor = context.getSharedPreferences(WECHAT_PAY, Context.MODE_PRIVATE).edit();
		editor.putString(WECHAT_PAY_INFO+SPLIT+BaseApplication.getInstances().getLoginInfo().getUserId()+SPLIT+businessTypeCode, generateWxPayOrderInfoString(orderId,result));
		editor.commit();
	}

	/**
	 * 辅助生成需要保存的订单信息
	 *
	 * @param orderId
	 * @param result
     * @return
     */
	public String generateWxPayOrderInfoString(String orderId,WXPayResult result){
		return orderId+SPLIT+result.getResultString();
	}

	/**
	 * 获取上一次支付时的订单号，如果为null，则表示支付流程已经完成，或者尚未进行支付流程
	 *
	 * @param businessTypeCode
	 * @return
     */
	public String getWxPayOrderId(int businessTypeCode) {
		String result = context.getSharedPreferences(WECHAT_PAY, Context.MODE_PRIVATE).
				getString(WECHAT_PAY_INFO+SPLIT+BaseApplication.getInstances().getLoginInfo().getUserId()+SPLIT+businessTypeCode,"");
		if(hasWxPayOrder(businessTypeCode)){
			return result.split(SPLIT)[0];
		}else{
			return null;
		}
	}

	/**
	 * 获取上一次支付时的订单状态，如果为null，则表示支付流程已经完成，或者尚未进行支付流程
	 * 若存在，支付状态可能值在：SUCCESS；FAILURE 中的其中一个
	 *
	 * @param businessTypeCode
	 * @return
     */
	public String getWxPayOrderStatus(int businessTypeCode) {
		String result = context.getSharedPreferences(WECHAT_PAY, Context.MODE_PRIVATE).
				getString(WECHAT_PAY_INFO+SPLIT+BaseApplication.getInstances().getLoginInfo().getUserId()+SPLIT+businessTypeCode,"");
		if(hasWxPayOrder(businessTypeCode)){
			return result.split(SPLIT)[1];
		}else{
			return null;
		}
	}

	public boolean hasWxPayOrder(int businessTypeCode){
		return !"".equals(context.getSharedPreferences(WECHAT_PAY, Context.MODE_PRIVATE).
				getString(WECHAT_PAY_INFO+SPLIT+BaseApplication.getInstances().getLoginInfo().getUserId()+SPLIT+businessTypeCode,"")) &&
				context.getSharedPreferences(WECHAT_PAY, Context.MODE_PRIVATE).
						getString(WECHAT_PAY_INFO+SPLIT+BaseApplication.getInstances().getLoginInfo().getUserId()+SPLIT+businessTypeCode,"").contains(SPLIT);
	}

	public boolean isWxPaySuccess(int businessTypeCode){
		return WXPayResult.SUCCESS.getResultString().equals(getWxPayOrderStatus(businessTypeCode));
	}

	public void clearWxPayOrder(int businessTypeCode){
		Editor editor = context.getSharedPreferences(WECHAT_PAY, Context.MODE_PRIVATE).edit();
		editor.putString(WECHAT_PAY_INFO+SPLIT+BaseApplication.getInstances().getLoginInfo().getUserId()+SPLIT+businessTypeCode, "");
		editor.commit();
	}

	/**
	 * **************************************************************************************************************************
	 * 【辅助在微信支付里的信息设置】
	 *
	 * 保存于获取最近一次的支付信息，在校验订单接口调用完成后需要置空
	 *
	 * Key:固定前缀;用户ID
	 * Value: 业务类型码;订单ID
     */
	public String getWxPayLastPayInfo() {
		return context.getSharedPreferences(WECHAT_PAY, Context.MODE_PRIVATE).
				getString(WECHAT_PAY_BUSINESS_LAST_PAY+SPLIT+BaseApplication.getInstances().getLoginInfo().getUserId(),"");
	}

	public void setWxPayLastPayInfo(int businessTypeCode,String orderId) {
		Editor editor = context.getSharedPreferences(WECHAT_PAY, Context.MODE_PRIVATE).edit();
		editor.putString( WECHAT_PAY_BUSINESS_LAST_PAY+SPLIT+BaseApplication.getInstances().getLoginInfo().getUserId(), businessTypeCode+SPLIT+orderId);
		editor.commit();
	}

	//TODO 置空最近支付（当支付流程完成是调用）
	public void setWxPayLastPayInfoNull() {
		Editor editor = context.getSharedPreferences(WECHAT_PAY, Context.MODE_PRIVATE).edit();
		editor.putString( WECHAT_PAY_BUSINESS_LAST_PAY+SPLIT+BaseApplication.getInstances().getLoginInfo().getUserId(),"");
		editor.commit();
	}

	//TODO 获取最近支付流程的业务类型码
	public String getWxPayLastPayInfoBusinessCode() {
		String result = context.getSharedPreferences(WECHAT_PAY, Context.MODE_PRIVATE).
				getString(WECHAT_PAY_BUSINESS_LAST_PAY+SPLIT+BaseApplication.getInstances().getLoginInfo().getUserId(),"");
		if(!"".equals(result) && result.contains(SPLIT)){
			return result.split(SPLIT)[0];
		}else{
			return null;
		}
	}

	//TODO 获取最近支付流程的业务类型码
	public int getWxPayLastPayInfoBusinessCodeIntValue() {
		String result = context.getSharedPreferences(WECHAT_PAY, Context.MODE_PRIVATE).
				getString(WECHAT_PAY_BUSINESS_LAST_PAY+SPLIT+BaseApplication.getInstances().getLoginInfo().getUserId(),"");
		if(!"".equals(result) && result.contains(SPLIT)){
			return Integer.parseInt(result.split(SPLIT)[0]);
		}else{
			return -1;
		}
	}

	//TODO 获取最近支付流程的订单ID
	public String getWxPayLastPayInfoOrderId() {
		String result = context.getSharedPreferences(WECHAT_PAY, Context.MODE_PRIVATE).
				getString(WECHAT_PAY_BUSINESS_LAST_PAY+SPLIT+BaseApplication.getInstances().getLoginInfo().getUserId(),"");
		if(!"".equals(result) && result.contains(SPLIT)){
			return result.split(SPLIT)[1];
		}else{
			return null;
		}
	}
	
}
