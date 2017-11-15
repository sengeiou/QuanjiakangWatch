package com.quanjiakan.watch.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
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
		if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
			BaseApplication.getInstances().setWxPayResultSuccess();
		}else if (resp.errCode == BaseResp.ErrCode.ERR_COMM) {
			BaseApplication.getInstances().setWxPayResultFailure();
		}else if(resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL){
			BaseApplication.getInstances().setWxPayResultCancel();
		}else{
			BaseApplication.getInstances().setWxPayResultFailure();
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