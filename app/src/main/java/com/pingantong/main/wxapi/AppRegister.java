package com.pingantong.main.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.quanjiakan.util.pay.WeixinPayHandler;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
		// 
		msgApi.registerApp(WeixinPayHandler.app_id);
	}
}
