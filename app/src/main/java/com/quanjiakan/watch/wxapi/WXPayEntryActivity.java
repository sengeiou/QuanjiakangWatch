package com.quanjiakan.watch.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.WXPayResult;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.pay.WeixinPayHandler;
import com.quanjiakan.watch.R;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
	
	private static final String TAG = "LOGUTIL WXPayEntry";
    private IWXAPI api;

	private TextView tv_title;

	public static WXPayResult wxPayResult;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payresult);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("WXPayEntry");
        api = WXAPIFactory.createWXAPI(this, WeixinPayHandler.app_id);
        api.handleIntent(getIntent(), this);

		wxPayResult = WXPayResult.FAILURE;
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);

		wxPayResult = WXPayResult.FAILURE;
	}

	@Override
	public void onStart() {
		super.onStart();

		wxPayResult = WXPayResult.FAILURE;
	}

	@Override
	public void onReq(BaseReq req) {

	}

	@Override
	public void onResp(BaseResp resp) {
		LogUtil.e( "onPayFinish, errCode = " + resp.errCode+"errorString:"+resp.errStr);
		if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
			wxPayResult = WXPayResult.SUCCESS;
		}else if (resp.errCode == BaseResp.ErrCode.ERR_COMM) {
//			BaseApplication.getInstances().toast("支付失败      WXPayEntryActivity IWXAPIEventHandler 回调");
			wxPayResult = WXPayResult.FAILURE;
		}else if(resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL){
			wxPayResult = WXPayResult.USER_CANCEL;
		}else{
			wxPayResult = WXPayResult.FAILURE;
		}
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