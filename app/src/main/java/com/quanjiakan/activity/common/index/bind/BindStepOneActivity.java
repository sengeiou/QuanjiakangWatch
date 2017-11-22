package com.quanjiakan.activity.common.index.bind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseConstants;
import com.quanjiakan.activity.base.ICommonActivityRequestCode;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.umeng.analytics.MobclickAgent;
import com.zxing.qrcode.BindDeviceActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/20.
 */

public class BindStepOneActivity extends BaseActivity {

    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ibtn_menu)
    ImageButton ibtnMenu;
    @BindView(R.id.menu_text)
    TextView menuText;
    @BindView(R.id.bind_device_id)
    TextView bindDeviceId;
    @BindView(R.id.bind_device_scan_2dcode)
    TextView bindDeviceScan2dcode;
    @BindView(R.id.bind_device_2dcode_value)
    EditText bindDevice2dcodeValue;
    @BindView(R.id.inputline)
    RelativeLayout inputline;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_device_bind_step_one);
        ButterKnife.bind(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    /**
     * ***********************************************************************************************************************************************
     */

    @Override
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.EMPTY:
                HashMap<String, String> params = new HashMap<>();
                return params;
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.EMPTY:
                getDialog(this, getString(R.string.hint_common_get_data));//正在获取数据...
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.EMPTY:
                break;
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.EMPTY:
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.EMPTY:
                break;
        }
        if (errorMsg != null && errorMsg.toString().length() > 0) {
            CommonDialogHint.getInstance().showHint(this, errorMsg.toString());
        } else {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_net_request_fail));
        }
    }

    /**
     * ***********************************************************************************************************************************************
     */

    @OnClick({R.id.ibtn_back, R.id.bind_device_scan_2dcode, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.bind_device_scan_2dcode:
                toScanActivity();
                break;
            case R.id.btn_submit:
                break;
        }
    }

    public void toScanActivity(){
        Intent intent = new Intent(this, BindDeviceActivity.class);
        intent.putExtra(BaseConstants.PARAMS_SHOW_HINT, true);
        startActivityForResult(intent, ICommonActivityRequestCode.REQUEST_SCAN);
    }

    /**
     * ***********************************************************************************************************************************************
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ICommonActivityRequestCode.REQUEST_SCAN: {
                if (resultCode == RESULT_OK) {
                    String deviceid = data.getStringExtra(IParamsName.PARAMS_DEVICE_ID);
                    LogUtil.e("扫描得到的数据:" + deviceid);
                    if (deviceid != null && deviceid.length() > 0) {
                        if (deviceid.lastIndexOf("IMEI=") > 0 || deviceid.toLowerCase().lastIndexOf("imei=") > 0) {
                            if (deviceid.lastIndexOf("IMEI=") > 0) {
                                bindDevice2dcodeValue.setText(deviceid.substring(deviceid.lastIndexOf("IMEI=") + 5, deviceid.lastIndexOf("IMEI=") + 20));
                            } else {
                                bindDevice2dcodeValue.setText(deviceid.substring(deviceid.lastIndexOf("imei=") + 5, deviceid.lastIndexOf("imei=") + 20));
                            }
                        } else {
                            Toast.makeText(this, "无效的二维码，请确认是正确的设备后再次扫描!", Toast.LENGTH_SHORT).show();
                        }
                        LogUtil.w("二维码设备ID:" + deviceid);
                    } else {
                        Toast.makeText(this, "二维码解析失败,请重试!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        }
    }
}
