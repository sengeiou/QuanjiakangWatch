package com.pingantong.main.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.WXPayResult;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.pay.WeixinPayHandler;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
	
	private static final String TAG = "LOGUTIL WXPayEntry";
    private IWXAPI api;

	private TextView tv_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payresult);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("WXPayEntry");
		api = WXAPIFactory.createWXAPI(this, WeixinPayHandler.app_id,false);
		api.registerApp(WeixinPayHandler.app_id);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		LogUtil.e( "onPayFinish, errCode = " + resp.errCode+"errorString:"+resp.errStr);
		if (resp.errCode == BaseResp.ErrCode.ERR_OK) {//TODO 需要保存支付状态（需要获取最近的支付信息）
			BaseApplication.getInstances().saveWxPayResult(
					BaseApplication.getInstances().getWxPayLastInfoBusinessTypeCode()
					,BaseApplication.getInstances().getWxPayLastInfoOrderId()
					, WXPayResult.SUCCESS);
		}else if (resp.errCode == BaseResp.ErrCode.ERR_COMM) {
			BaseApplication.getInstances().saveWxPayResult(
					BaseApplication.getInstances().getWxPayLastInfoBusinessTypeCode()
					,BaseApplication.getInstances().getWxPayLastInfoOrderId()
					, WXPayResult.FAILURE);
		}else if(resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL){
			BaseApplication.getInstances().saveWxPayResult(
					BaseApplication.getInstances().getWxPayLastInfoBusinessTypeCode()
					,BaseApplication.getInstances().getWxPayLastInfoOrderId()
					, WXPayResult.USER_CANCEL);
		}else{
			BaseApplication.getInstances().saveWxPayResult(
					BaseApplication.getInstances().getWxPayLastInfoBusinessTypeCode()
					,BaseApplication.getInstances().getWxPayLastInfoOrderId()
					, WXPayResult.FAILURE);
		}
		//TODO 收到回调后关闭页面
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode){
			default:
				finish();
				break;
		}
	}
}