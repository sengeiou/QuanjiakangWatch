package com.quanjiakan.activity.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;

import com.google.gson.JsonObject;
import com.quanjiakan.util.common.SerialUtil;
import com.quanjiakan.util.common.SignatureUtil;

import java.util.ArrayList;

public class QuanjiakanSetting {
	
	private final String TAG = "LOGUTIL "+QuanjiakanSetting.class.getSimpleName();
	private static QuanjiakanSetting instance = null;
	private static boolean bInited = false;
	private Context context = null;
	private SharedPreferences sharedPreferences;
	private final String PREFERENCES_FILE = "quanjiakang_setting";
	private ArrayList<String> list;

	private boolean mbShortcutCreated = false;

	private final String USER_ID_KEY = "user_id";
	private final String PHONE_KEY = "phone";
	private final String USER_NAME = "user_name";
	private final String USER_TOKEN = "user_token";
	private final String USER_PW_SIGNATURE = "pw_signature";
	private final String DEVICE = "device";
	private final String UPDATE_TIME = "update_time";

	private final String SHORTCUT_CREATED = "shortcut_created";
	private final String SDK_SERVER_STATUS = "sdk_server_status";

	public static final String KEY_SIGNAL = "key_signal";

	public static void init(Context context) {
		if (context == null) {
			throw new NullPointerException();
		}
		if (!bInited) {
			instance = new QuanjiakanSetting(context);
			bInited = true;
		}
	}
	
	public static boolean isInitialized(){
		return (instance != null);
	}
	
	private QuanjiakanSetting(Context con) {
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
	 *************************************************************
	 * 当前登录用户的ID
	 */
	public int getUserId() {
		return context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getInt(USER_ID_KEY,0);
	}

	public void setUserId(int userId) {
		Editor editor = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
		editor.putInt(USER_ID_KEY, userId);
		editor.commit();
	}

	/**
	 ************************************************************
	 * 设备信息---暂时没有用到
	 */

	public String getDevice() {
		return context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getString(DEVICE,"");
	}

	public void setDevice(String userId) {
		Editor editor = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
		editor.putString(DEVICE, userId);
		editor.commit();
	}

	/**
	 ************************************************************
	 * 用户名信息
	 *
	 */
	
	public String getUserName() {
		return context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getString(USER_NAME,"");
	}

	public void setUserName(String userName) {
		Editor editor = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
		editor.putString(USER_NAME, userName);
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
	 * 用户电话---相当于账号
	 *
	 */

	public String getPhone() {
		return context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getString(PHONE_KEY,"");
	}

	public void setPhone(String phone) {
		Editor editor = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
		editor.putString(PHONE_KEY, phone);
		editor.commit();
	}

	/**
	 ************************************************************
	 * 用户Token
	 *
	 */

	public String getToken() {
		return context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getString(USER_TOKEN,"");
	}

	public void setToken(String phone) {
		Editor editor = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
		editor.putString(USER_TOKEN, phone);
		editor.commit();
	}

	/**
	 ************************************************************
	 * 用户密码签名
	 *
	 */

	public String getPwSignature() {
		return context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getString(USER_PW_SIGNATURE,"");
	}

	public void setPwSignature(String phone) {
		Editor editor = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
		editor.putString(USER_PW_SIGNATURE, SignatureUtil.getSHA1String(phone));
		editor.commit();
	}

	public boolean isEqualToOriginalPw(String password){
		return getPwSignature().equals(SignatureUtil.getSHA1String(password));
	}

	/**
	 ************************************************************
	 */
	
	public boolean isShortcutCreated() {
		return context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getBoolean(SHORTCUT_CREATED,false);
	}
	
	public String getSourceCode() {
		return "1";
	}
	
	public static QuanjiakanSetting getInstance() {
		return instance;
	}

	public void setShortcutCreated(boolean isShortcutCreated) {
		mbShortcutCreated = isShortcutCreated;
		Editor editor = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
		editor.putBoolean(SHORTCUT_CREATED, mbShortcutCreated);
		editor.commit();
	}

	
	/** 清除本地，个人信�?*/
	public void logout() {
		Log.i(TAG, "logout and clear all info...");
		Editor editor = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
		editor.putInt(USER_ID_KEY, 0);
		editor.putString(PHONE_KEY, null);
		editor.putString(USER_NAME, null);
		editor.commit();
		/**
		 * 停止Push通知
		 */
		setUserId(0);
	}

	/**
	 ************************************************************
	 */

	private void setSharedPreference(String key, String value){
		Editor editor = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}	
	
	public void setValue(String key, String value){
		setSharedPreference(key, value);
	}
	
	public String getValue(String key){
		return sharedPreferences.getString(key, "");
	}


	/**
	 ************************************************************
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
	 */
	
}
