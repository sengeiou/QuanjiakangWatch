package com.quanjiakan.activity.common.index.bind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.BaseConstants;
import com.quanjiakan.activity.base.ICommonActivityRequestCode;
import com.quanjiakan.activity.base.ICommonActivityResultCode;
import com.quanjiakan.activity.base.ICommonData;
import com.quanjiakan.activity.common.web.CommonWebForBindChildActivity;
import com.quanjiakan.activity.common.web.CommonWebForBindOldActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.PostCheckIMEIEntity;
import com.quanjiakan.net_presenter.BindStepOnePresenter;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.common.SerializeToObjectUtil;
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


    boolean isGoWebActivation;
    private BindStepOnePresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_device_bind_step_one);
        ButterKnife.bind(this);
        initTitle();
        isGoWebActivation = false;
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
            case IPresenterBusinessCode.DEVICE_BIND_STEP_ONE:
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_IMEI,bindDevice2dcodeValue.getText().toString());
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                return params;
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.DEVICE_BIND_STEP_ONE:
                getDialog(this, getString(R.string.hint_common_get_data));//正在获取数据...
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.DEVICE_BIND_STEP_ONE:
                break;
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.DEVICE_BIND_STEP_ONE:
                setResult(result);
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.DEVICE_BIND_STEP_ONE:
                break;
        }
        if (errorMsg != null && errorMsg.toString().length() > 0) {
            CommonDialogHint.getInstance().showHint(this, errorMsg.toString());
        } else {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_net_request_fail));
        }
    }

    public void setResult(Object result){
        if(result!=null && result instanceof String){
            /**
             {
             "code": "200",
             "message": "返回成功",
             "object": {
             "acTime": "2017-04-25 13:58:11.0",
             "actStatus": 1,   是否已经激活  1已激活  0未激活
             "deviceType": 0,
             //设备类型0.老人手表
             1.儿童手表
             2.定位器
             3.睡眠监测仪
             4.拐杖
             5呼吸监测仪
             6体态监测仪

             "deviceid": 240207489205306450,
             "imei": "355637052788452"
             }
             */
            PostCheckIMEIEntity entity = (PostCheckIMEIEntity) SerializeToObjectUtil.getInstances().jsonToObject(result.toString(),new TypeToken<PostCheckIMEIEntity>(){}.getType());
            if(entity!=null && ICommonData.HTTP_OK.equals(entity.getCode()) && entity.getObject()!=null){
                if(entity.getObject().getActStatus()==1){//TODO 已经激活---跳转至第二步

                }else{//TODO 尚未激活---跳转至激活页面
                    //
                    if(!isGoWebActivation){
                        if(entity.getObject().getDeviceType()==1){//TODO 儿童手表
                            Intent intent = new Intent(BindStepOneActivity.this, CommonWebForBindChildActivity.class);
                            intent.putExtra(IParamsName.PARAMS_COMMON_WEB_URL,"http://static.quanjiakan.com/familycare/activate?IMEI=" + bindDevice2dcodeValue.getText().toString().trim());
                            intent.putExtra(IParamsName.PARAMS_COMMON_WEB_TITLE,getString(R.string.web_bind_old_title));
                            startActivityForResult(intent, ICommonActivityResultCode.RELOAD_DATA);
                            isGoWebActivation = true;
                        }else{//TODO 老人手表  非儿童的设备都在老人激活页进行激活
                            Intent intent = new Intent(BindStepOneActivity.this, CommonWebForBindOldActivity.class);
                            intent.putExtra(IParamsName.PARAMS_COMMON_WEB_URL,"http://static.quanjiakan.com/qjk/pages/qjk/device/activation_login.jsp");
                            intent.putExtra(IParamsName.PARAMS_COMMON_WEB_TITLE,getString(R.string.web_bind_child_title));
                            startActivityForResult(intent, ICommonActivityResultCode.RELOAD_DATA);
                            isGoWebActivation = true;
                        }
                    }else{
                        CommonDialogHint.getInstance().showHint(this,getString(R.string.bind_step_one_device_active_hint));
                    }
                }
            }else{
                onError(IPresenterBusinessCode.DEVICE_BIND_STEP_ONE,200,null);
            }
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
                doSubmitIMEI();
                break;
        }
    }

    public void doSubmitIMEI(){
        if(bindDevice2dcodeValue.length()<0){
            // 请填入设备的IMEI号码，或扫描设备二维码获取IMEI号码
            CommonDialogHint.getInstance().showHint(this,getString(R.string.hint_bind_step_one_no_imei));
            return ;
        }
        if(bindDevice2dcodeValue.length()!=15){
            // 请确认输入的IMEI号码是有效的
            CommonDialogHint.getInstance().showHint(this,getString(R.string.hint_bind_step_one_wrony_imei));
            return ;
        }

        presenter.checkIMEI(this);
    }

    public void toScanActivity(){
        Intent intent = new Intent(this, BindDeviceActivity.class);
        intent.putExtra(BaseConstants.PARAMS_SHOW_HINT, true);
        startActivityForResult(intent, ICommonActivityRequestCode.REQUEST_SCAN);
    }

    public void initTitle(){
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.bind_device_step_one_title);

        menuText.setVisibility(View.GONE);
        ibtnMenu.setVisibility(View.GONE);

        presenter = new BindStepOnePresenter();
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
                            CommonDialogHint.getInstance().showHint(this,getString(R.string.hint_common_scan_error1));
                        }
                        LogUtil.w("二维码设备ID:" + deviceid);
                    } else {
                        CommonDialogHint.getInstance().showHint(this,getString(R.string.hint_common_scan_error2));
                    }
                }
                break;
            }
            case ICommonActivityRequestCode.RELOAD_DATA:{
                presenter.checkIMEI(this);
            }
        }
    }
}
