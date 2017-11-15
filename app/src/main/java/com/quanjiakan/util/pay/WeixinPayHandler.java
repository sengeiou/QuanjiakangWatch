package com.quanjiakan.util.pay;

import android.content.Context;

import com.google.gson.JsonObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WeixinPayHandler {

	private Context context;
	private IWXAPI api;
	public static final String app_id = "wx513e10652f1e89b6";//wx513e10652f1e89b6

	public WeixinPayHandler(Context context){
		this.context = context;
		init();
	}

	protected void init(){
		api = WXAPIFactory.createWXAPI(context, WeixinPayHandler.app_id,false);
		api.registerApp(app_id);
	}

	public void pay(JsonObject json){
		PayReq req = new PayReq();
		req.appId			= json.get("appid").getAsString();
		req.partnerId		= json.get("mch_id").getAsString();
		req.prepayId		= json.get("prepay_id").getAsString();
		req.nonceStr		= json.get("nonce_str").getAsString();
		req.packageValue	= "Sign=WXPay";
		req.timeStamp		= json.get("timestamp").getAsString();
		req.sign			= json.get("sign").getAsString();
		req.extData			= "APP"; // optional  app data
		api.sendReq(req);
	}

}
