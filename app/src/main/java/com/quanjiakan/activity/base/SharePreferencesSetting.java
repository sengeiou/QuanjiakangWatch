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
	private final String PREFERENCES_FILE = "quanjiakang_setting";
	private final String UPDATE_TIME = "update_time";
	private final String SDK_SERVER_STATUS = "sdk_server_status";

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
	 */
	
}
