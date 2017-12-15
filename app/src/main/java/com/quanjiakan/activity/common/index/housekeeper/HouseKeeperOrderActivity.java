package com.quanjiakan.activity.common.index.housekeeper;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.constants.ICommonActivityRequestCode;
import com.quanjiakan.constants.ICommonActivityResultCode;
import com.quanjiakan.activity.common.pay.PaymentResultActivity;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.format.CommonPublicKeyEntity;
import com.quanjiakan.net.format.CommonResultEntity;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperListEntity;
import com.quanjiakan.net_presenter.HouseKeeperOrderPresenter;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.net_presenter.PayEncryptPresenter;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.common.ParseToGsonUtil;
import com.quanjiakan.util.common.SerializeToObjectUtil;
import com.quanjiakan.util.common.StringCheckUtil;
import com.quanjiakan.util.common.UnitExchangeUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.encrypt.SMSValidateUtil;
import com.quanjiakan.util.pay.AlipayHandler;
import com.quanjiakan.util.pay.WeixinPayHandler;
import com.quanjiakan.util.widget.RoundTransform;
import com.quanjiakan.view.dialog.ChangeBirthDialog;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/10.
 */

public class HouseKeeperOrderActivity extends BaseActivity {
    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.menu_text)
    TextView menuText;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.tv_heji)
    TextView tvHeji;

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.rbar)
    RatingBar rbar;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_line)
    TextView tvLine;
    @BindView(R.id.tv_begin_date)
    TextView tvBeginDate;
    @BindView(R.id.tv_begin_date_value)
    TextView tvBeginDateValue;
    @BindView(R.id.tv_end_date)
    TextView tvEndDate;
    @BindView(R.id.tv_end_date_value)
    TextView tvEndDateValue;
    @BindView(R.id.tv_info_service)
    TextView tvInfoService;
    @BindView(R.id.et_contact_name)
    EditText etContactName;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.tv_info_pay)
    TextView tvInfoPay;
    @BindView(R.id.rbtn_1)
    RadioButton rbtn1;
    @BindView(R.id.rbtn_2)
    RadioButton rbtn2;

    enum TimeType{
        START,END
    }

    public enum PAY_CHANNEL{
        DEFAULT(0,"DEFAULT"),
        ALI(1,"ALI"),
        WECHAT(2,"WECHAT");

        private int value;
        private String type;

        PAY_CHANNEL(int i,String type) {
            this.value = i;
            this.type = type;
        }

        public int getValue(){
            return value;
        }

        public String getType(){
            return type;
        }
    }

    public enum PAY_RESULT{
        SUCCESS(0,"SUCCESS"),
        FAILURE(1,"FAILURE");

        private int value;
        private String result;

        PAY_RESULT(int i,String result) {
            this.value = i;
            this.result = result;
        }

        public int getValue(){
            return value;
        }

        public String getResult(){
            return result;
        }
    }

    private PAY_CHANNEL selectedPayType;
    private PAY_RESULT currentPayResult;

    private final int defaultPrePay = 200;
    private String aliOrderId;
    private String wechatOrderId;

    private String publicKey;

    private JsonObject orderDetail = new JsonObject();

    private Dialog noticeDialog;

    private GetHouseKeeperListEntity.ListBean entity;

    private HouseKeeperOrderPresenter presenter;

    private PayEncryptPresenter payEncryptPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_housekeeper_order);
        ButterKnife.bind(this);
        initTitleBar();

        //TODO 检查参数
        entity = (GetHouseKeeperListEntity.ListBean) getIntent().
                getSerializableExtra(IParamsName.PARAMS_HOUSE_KEEPER_DETAIL_INFO);
        if (entity == null) {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_error_activity_parameters), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            return;
        }

        setInfo();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());

        if(BaseApplication.getInstances().hasWxPayLastInfo()){
            vertifyWechatPayment(wechatOrderId);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //***********************************************************************

    @Override
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.PAY_GET_PRIVATE_KEY:{
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_MEMBERID, BaseApplication.getInstances().getLoginInfo().getUserId());
                return params;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER_ENCRYPT: {

                //TODO 构建数据
                if(rbtn1.isChecked()){
                    selectedPayType = PAY_CHANNEL.ALI;
                }else if(rbtn2.isChecked()){
                    selectedPayType = PAY_CHANNEL.WECHAT;
                }else{
                    selectedPayType = PAY_CHANNEL.ALI;
                }

                String jsonString = "{" +
                        "\"housekeeperId\":"+entity.getId()+
                        ",\"mobile\":"+"\""+etMobile.getText().toString()+"\""+
                        ",\"orderUserId\":"+BaseApplication.getInstances().getLoginInfo().getUserId()+
                        ",\"paymentChannel\":"+selectedPayType.getValue()+"}";

                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_MEMBERID, BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());

                params.put(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_NOTE, "");
                params.put(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_BEGINDATE, tvBeginDateValue.getTag().toString());
                params.put(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_ENDDATE, tvEndDateValue.getTag().toString());
                params.put(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_COMPANYID, entity.getCompanyId()+"");
                params.put(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_ADDRESS, etAddress.getText().toString());
                params.put(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_ORDER_USERNAME, etContactName.getText().toString());
                try {
                    params.put(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_CIPHERTEXT, SMSValidateUtil.getCiphertextHouseKeeperOrder(
                            entity.getId(),
                            etMobile.getText().toString(),
                            Integer.parseInt(BaseApplication.getInstances().getLoginInfo().getUserId()),
                            selectedPayType.getValue(),
                            publicKey));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER: {
                //TYPE_GET_STRING_NOCACHE
                //http://pay.quanjiakan.com:7080/familycore-pay/core/api_get?devicetype=0&platform=2&client=1&token=849f2bbb87dc5c049f20b85be3047e3b&user_id=11303&code=pay&action=housekeeper_produceorder&
                //data={"note":"","housekeeperId":38,"companyId":7,"begindate":"2017-11-10","enddate":"2017-11-10","orderUserName":"发古板","mobile":"13212345678","address":"复活币","orderUserId":11303,"userId":11303,"paymentChannel":1}
                //Request Result:{"code":"200","message":"{\"orderid\":\"QJKKEEPER20171110053936135398\",\"paidinfo\":{\"partner\":\"2088021868486397\",\"seller_id\":\"13510237554@163.com\",\"out_trade_no\":\"QJKKEEPER20171110053936135398\",\"subject\":\"全家康支付\",\"body\":\"支付家政人员服务费用\",\"total_fee\":\"200.0\",\"notify_url\":\"http://pay.quanjiakan.com:7080/familycore-pay/notify_alipay.jsp\",\"service\":\"mobile.securitypay.pay\",\"payment_type\":\"1\",\"_input_charset\":\"utf-8\",\"it_b_pay\":\"30m\",\"return_url\":\"m.alipay.com\",\"sign\":\"CRKOZfxyv/KgU9EAUf/W7Ki/KmvKrm4dZIEe3bnbDg6IQ4ZDis5ebANLZhsuDfKW7YFoIxeWQFR6wOBLnyROEbmA/O5nW08VIUCQBNqWjSO6NDyphBQ2y76LMiO7r87CPlparrLQtwLCUX5BFRfVWF8+yLGJGuYFCWZ3gqOL5g8=\",\"code\":\"200\"}}"}

                //TODO 构建数据
                if(rbtn1.isChecked()){
                    selectedPayType = PAY_CHANNEL.ALI;
                }else if(rbtn2.isChecked()){
                    selectedPayType = PAY_CHANNEL.WECHAT;
                }else{
                    selectedPayType = PAY_CHANNEL.ALI;
                }

                String jsonString = "{\"note\":"+ "\"\"" +
                        ",\"housekeeperId\":"+entity.getId()+
                        ",\"companyId\":"+ entity.getCompanyId()+
                        ",\"begindate\":"+"\""+tvBeginDateValue.getTag()+"\""+
                        ",\"enddate\":"+"\""+tvEndDateValue.getTag()+"\""+
                        ",\"orderUserName\":"+"\""+etContactName.getText().toString()+"\""+
                        ",\"mobile\":"+"\""+etMobile.getText().toString()+"\""+
                        ",\"address\":"+"\""+etAddress.getText().toString()+"\""+
                        ",\"orderUserId\":"+BaseApplication.getInstances().getLoginInfo().getUserId()+
                        ",\"userId\":"+ BaseApplication.getInstances().getLoginInfo().getUserId()+",\"paymentChannel\":"+selectedPayType.getValue()+"}";

                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_USERID, BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_COMMON_DATA, jsonString);
                return params;
            }
            case IPresenterBusinessCode.ALI_PAY: {
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_USERID, BaseApplication.getInstances().getLoginInfo().getUserId());
                return params;
            }
            case IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT: {
                HashMap<String, String> params = new HashMap<>();
                String jsonString = "{\"userId\":"+ BaseApplication.getInstances().getLoginInfo().getUserId()+
                        ",\"orderid\":"+"\""+aliOrderId+"\""+"}";
                params.put(IParamsName.PARAMS_COMMON_USERID, BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_DATA, jsonString);
                return params;
            }
            case IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT_ENCRYPT: {
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_MEMBERID, BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_ORDERID, aliOrderId);
                return params;
            }
            case IPresenterBusinessCode.WECHAT_PAY: {
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_USERID, BaseApplication.getInstances().getLoginInfo().getUserId());
                return params;
            }
            case IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT: {
                HashMap<String, String> params = new HashMap<>();
                String jsonString = "{\"userId\":"+ BaseApplication.getInstances().getLoginInfo().getUserId()+
                        ",\"orderid\":"+"\""+wechatOrderId+"\""+"}";

                //TODO 若上次微信支付成功后，但校验未成功，则再次点击确定是，直接校验订单，不再进行支付
                if(BaseApplication.getInstances().hasWxPayLastInfo() &&//TODO 校验当次支付是否完成----存在则表明进行过
                        BaseApplication.getInstances().isCurrentOrder(IPresenterBusinessCode.HOUSE_KEEPER_ORDER)){
                    jsonString = "{\"userId\":"+ BaseApplication.getInstances().getLoginInfo().getUserId()+
                            ",\"orderid\":"+"\""+BaseApplication.getInstances().getWxPayLastInfoOrderId()+"\""+"}";
                }
                params.put(IParamsName.PARAMS_COMMON_USERID, BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_DATA, jsonString);
                return params;
            }
            case IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT_ENCRYPT: {
                HashMap<String, String> params = new HashMap<>();
                //TODO 若上次微信支付成功后，但校验未成功，则再次点击确定是，直接校验订单，不再进行支付
                params.put(IParamsName.PARAMS_COMMON_MEMBERID, BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_ORDERID, wechatOrderId);
                if(BaseApplication.getInstances().hasWxPayLastInfo() &&//TODO 校验当次支付是否完成----存在则表明进行过
                        BaseApplication.getInstances().isCurrentOrder(IPresenterBusinessCode.HOUSE_KEEPER_ORDER)){
                    params.put(IParamsName.PARAMS_HOUSE_KEEPER_ENCRYPT_ORDERID, BaseApplication.getInstances().getWxPayLastInfoOrderId());
                }
                return params;
            }
            default:
                return null;
        }
    }

    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.PAY_GET_PRIVATE_KEY:{
                getDialog(this, getString(R.string.hint_common_get_data));
                break;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER_ENCRYPT:
                getDialog(this, getString(R.string.hint_get_house_keeper_order));
                break;
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER:
                getDialog(this, getString(R.string.hint_get_house_keeper_order));
                break;
            case IPresenterBusinessCode.ALI_PAY:
                getDialog(this, getString(R.string.hint_get_house_keeper_order_ali));
                break;
            case IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT:
                getDialog(this, getString(R.string.hint_get_house_keeper_order_ali_verify));
                break;
            case IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT_ENCRYPT:
                getDialog(this, getString(R.string.hint_get_house_keeper_order_ali_verify));
                break;
            case IPresenterBusinessCode.WECHAT_PAY:
                getDialog(this, getString(R.string.hint_get_house_keeper_order_wechat));
                break;
            case IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT:
                getDialog(this, getString(R.string.hint_get_house_keeper_order_wechat_verify));
                break;
            case IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT_ENCRYPT:
                getDialog(this, getString(R.string.hint_get_house_keeper_order_wechat_verify));
                break;
            default:
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.PAY_GET_PRIVATE_KEY:
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER_ENCRYPT:
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER:
            case IPresenterBusinessCode.ALI_PAY:
            case IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT:
            case IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT_ENCRYPT:
            case IPresenterBusinessCode.WECHAT_PAY:
            case IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT:
            case IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT_ENCRYPT:
            default:
                break;
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.PAY_GET_PRIVATE_KEY:{
                setPrivateKey(result);
                break;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER_ENCRYPT:
                setServerOrder(result);
                break;
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER:
                setServerOrder(result);
                break;
            case IPresenterBusinessCode.ALI_PAY:
                break;
            case IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT:
                setAliResult(result);
                break;
            case IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT_ENCRYPT:
                setAliResult(result);
                break;
            case IPresenterBusinessCode.WECHAT_PAY:
                break;
            case IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT:
                setWechatResult(result);
                break;
            case IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT_ENCRYPT:
                setWechatResult(result);
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {

        switch (type) {
            case IPresenterBusinessCode.PAY_GET_PRIVATE_KEY: {
                if (errorMsg != null) {
                    CommonDialogHint.getInstance().showHint(this, errorMsg.toString());
                } else {
                    CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_pay_public_key));
                }
                break;
            }
            case IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT:
                currentPayResult = PAY_RESULT.FAILURE;
                goToPaymentResult(aliOrderId);
                break;
            case IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT:
                currentPayResult = PAY_RESULT.FAILURE;
                goToPaymentResult(wechatOrderId);
                break;
            case IPresenterBusinessCode.ALI_PAY_VERIFY_RESULT_ENCRYPT:
                currentPayResult = PAY_RESULT.FAILURE;
                goToPaymentResult(aliOrderId);
                break;
            case IPresenterBusinessCode.WECHAT_PAY_VERIFY_RESULT_ENCRYPT:
                currentPayResult = PAY_RESULT.FAILURE;
                goToPaymentResult(wechatOrderId);
                break;
            //**************************************************** 以上为支付结果校验
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER_ENCRYPT:
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER:
            case IPresenterBusinessCode.ALI_PAY:
            case IPresenterBusinessCode.WECHAT_PAY:
            default:
                if (errorMsg != null) {
                    CommonDialogHint.getInstance().showHint(this, errorMsg.toString());
                } else {
                    CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_net_request_fail));
                }
                break;
        }
    }

    @OnClick({R.id.ibtn_back,R.id.tv_title, R.id.menu_text, R.id.tv_order,R.id.tv_begin_date_value, R.id.tv_end_date_value})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title:
                break;
            case R.id.ibtn_back:
                //TODO 返回
                finish();
                break;
            case R.id.menu_text:
                //TODO 用户须知
                showUserAgreementDialog();
                break;
            case R.id.tv_order:
                orderHouseKeeper();
                break;
            case R.id.tv_begin_date_value:
                showTimeSelectorDialog(TimeType.START);
                break;
            case R.id.tv_end_date_value:
                showTimeSelectorDialog(TimeType.END);
                break;
        }
    }
    //***********************************************************************

    private void setPrivateKey(Object result){
        if(result!=null && result instanceof String){
            CommonPublicKeyEntity entity = (CommonPublicKeyEntity) SerializeToObjectUtil.getInstances().
                    jsonToObject(result.toString(),new TypeToken<CommonPublicKeyEntity>(){}.getType());
            if(entity!=null && ICommonData.HTTP_OK.equals(entity.getCode()) && entity.getObject()!=null){
                publicKey = entity.getObject().getPublicKey();
            }else{
                publicKey = null;
            }
        }else{
            publicKey = null;
        }
    }
    /**
     * 展示用户须知对话框
     */
    public void showUserAgreementDialog(){
        if(noticeDialog!=null && noticeDialog.isShowing()){
            noticeDialog.dismiss();
        }else{
            noticeDialog = new Dialog(this,R.style.dialog_loading);
            View view = LayoutInflater.from(HouseKeeperOrderActivity.this).inflate(R.layout.dialog_medicine_introduce,null);
            /**
             * 数据赋值
             */
            RelativeLayout rl_exit = (RelativeLayout) view.findViewById(R.id.rl_exit);
            rl_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    noticeDialog.dismiss();
                }
            });
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            title.setText(getString(R.string.hint_house_keeper_order_user_agreement));//
            TextView include_value = (TextView) view.findViewById(R.id.indroduce);
            include_value.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            include_value.setText(R.string.house_keeper_user_notice);

            WindowManager.LayoutParams lp = noticeDialog.getWindow().getAttributes();
            lp.width = UnitExchangeUtil.dip2px(this, 300);
            lp.height = lp.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            noticeDialog.setContentView(view, lp);
            noticeDialog.setCanceledOnTouchOutside(false);
            noticeDialog.show();
        }
    }

    public void showTimeSelectorDialog(final TimeType type){
        ChangeBirthDialog day_dialog = new ChangeBirthDialog(this);
        day_dialog.show();
        day_dialog.setBirthdayListener(new ChangeBirthDialog.OnBirthListener() {

            @Override
            public void onClick(String year, String month, String day) {
                // TODO Auto-generated method stub
                if (Integer.parseInt(month) < 10) {
                    month = "0" + month;
                }
                if (Integer.parseInt(day) < 10) {
                    day = "0" + day;
                }
                switch (type){
                    case START:
                        tvBeginDateValue.setText(year+"/"+month+"/"+day);
                        tvBeginDateValue.setTag(year+"-"+month+"-"+day);
                        break;
                    case END:
                        tvEndDateValue.setText(year+"/"+month+"/"+day);
                        tvEndDateValue.setTag(year+"-"+month+"-"+day);
                        break;
                }
            }
        });
    }

    /**
     * 预约家政
     */
    public void orderHouseKeeper(){

        orderDetail.addProperty("begindate",tvBeginDateValue.getTag()+"");
        orderDetail.addProperty("enddate",tvEndDateValue.getTag()+"");
        orderDetail.addProperty("orderUserName",etContactName.getText().toString());
        orderDetail.addProperty("mobile",etMobile.getText().toString());
        orderDetail.addProperty("address",etAddress.getText().toString());

        //检查是否未选择开始或结束日期
        if(tvBeginDateValue.getTag() == null || tvEndDateValue.getTag() == null){
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_house_keeper_order_no_time));
            return;
        }
        //校验开始结束日期的先后 2
        if (Long.parseLong(tvEndDateValue.getTag().toString().replace("-","")) <
                Long.parseLong(tvBeginDateValue.getTag().toString().replace("-",""))
                ) {
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_house_keeper_order_time_select_error));
            return;
        }
        //*******************************************
        //检查预约人信息
        if(etMobile.length() == 0 || etContactName.length() == 0 || etAddress.length() == 0){
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_house_keeper_order_no_info));
            return;
        }
        //检查预约电话是否正确
        if(!StringCheckUtil.isPhoneNumber(etMobile.getText().toString())){
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_house_keeper_order_error_mobile));
            return;
        }
        //检查联系人姓名是否全为中文字符
        if(!StringCheckUtil.isAllChineseChar(etContactName.getText().toString())){
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_house_keeper_order_error_name));
            return;
        }
        //获取订单数据

        //TODO 当存在之前支付过的订单
        if(BaseApplication.getInstances().hasWxPayLastInfo() &&//TODO 校验当次支付是否完成----存在则表明进行过
                BaseApplication.getInstances().isCurrentOrder(IPresenterBusinessCode.HOUSE_KEEPER_ORDER)){
            vertifyWechatPayment(BaseApplication.getInstances().getWxPayLastInfoOrderId());
        }else{
            presenter.doGetHouseKeeperOrderEncrypt(this);
        }
    }

    public void setServerOrder(Object result){

        if(result!=null && result instanceof String){
            String resultString = result.toString();
            LogUtil.e("订单：\n"+resultString);

            /*
{
  "code": "200",
  "message": "{\"orderid\":\"QJKKEEPER20171113110956262592\",\"paidinfo\":{\"partner\":\"2088021868486397\",\"seller_id\":\"13510237554@163.com\",\"out_trade_no\":\"QJKKEEPER20171113110956262592\",\"subject\":\"全家康支付\",\"body\":\"支付家政人员服务费用\",\"total_fee\":\"200.0\",\"notify_url\":\"http://pay.quanjiakan.com:7080/familycore-pay/notify_alipay.jsp\",\"service\":\"mobile.securitypay.pay\",\"payment_type\":\"1\",\"_input_charset\":\"utf-8\",\"it_b_pay\":\"30m\",\"return_url\":\"m.alipay.com\",\"sign\":\"FToxLQaeEBeNh9O/JpyCxoaLl7UHm2IGb44L8cFL0ZzNTmp3aH1niFRdKYyntE2HUoRDrBqqVNNHwPM5u5mYDINqXe6fsr9/m19dgtZBV20ccWhwH0/jfRN9EgcAaaETLtnXVeJKHG5skC4RoyKmEuo7NBY9PZYSRw17RmHtAP0=\",\"code\":\"200\"}}"
}

------------------------------------- 新数据格式
{
  "code": "200",
  "message": "返回成功",
  "object": {
    "orderid": "QJKKEEPER20171215053415442757",
    "paidinfo": "{\"return_code\":\"SUCCESS\",\"return_msg\":\"OK\",\"appid\":\"wx513e10652f1e89b6\",\"mch_id\":\"1491214032\",\"nonce_str\":\"6eafebe9722244e990f44580a7ea2a45\",\"sign\":\"48320BBD8D3B570A4838548E110CDB24\",\"result_code\":\"SUCCESS\",\"prepay_id\":\"wx2017121517341617c3b9d1d40171709351\",\"trade_type\":\"APP\",\"code\":\"200\",\"package\":\"Sign=WXPay\",\"timestamp\":\"1513330456\"}"
  }
}
             */
            try{
                JSONObject resultJson = new JSONObject(resultString);
                //TODO 老数据格式---- 不建议使用了
//                if(resultJson.has("message") && resultJson.getString("message").length()>0){
//                    JsonObject resultMessage = new ParseToGsonUtil(resultJson.getString("message")).getJsonObject();
//
//                    //TODO
//                    orderDetail.addProperty("id",resultJson.getString("message"));
//                    orderDetail.addProperty("orderid",resultMessage.get("orderid").getAsString());
//                    orderDetail.addProperty("createtime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//
//                    if(selectedPayType==PAY_CHANNEL.ALI){
//                        //进行支付宝支付
//                        goAliPay(resultMessage.get("orderid").getAsString(),resultMessage.get("paidinfo").getAsJsonObject());
//                    }else if(selectedPayType==PAY_CHANNEL.WECHAT){
//                        //进行微信支付
//                        goWechatPay(resultMessage.get("orderid").getAsString(),resultMessage.get("paidinfo").getAsJsonObject());
//                    }else{
//                        //TODO 作为默认选择（支付宝）
//                        goAliPay(resultMessage.get("orderid").getAsString(),resultMessage.get("paidinfo").getAsJsonObject());
//                    }
//                }else{
//                    CommonDialogHint.getInstance().showHint(this,getString(R.string.hint_order_invalid));
//                }
                //TODO 修改后的
                if(resultJson.has("object") && resultJson.getString("object").length()>0){
                    JsonObject resultMessage = new ParseToGsonUtil(resultJson.getString("object")).getJsonObject();

                    //TODO
                    orderDetail.addProperty("id",resultJson.getString("object"));
                    orderDetail.addProperty("orderid",resultMessage.get("orderid").getAsString());
                    orderDetail.addProperty("createtime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

                    if(selectedPayType==PAY_CHANNEL.ALI){
                        //进行支付宝支付
                        goAliPay(resultMessage.get("orderid").getAsString(),new ParseToGsonUtil(resultMessage.get("paidinfo").getAsString()).getJsonObject());
                    }else if(selectedPayType==PAY_CHANNEL.WECHAT){
                        //进行微信支付
                        goWechatPay(resultMessage.get("orderid").getAsString(),new ParseToGsonUtil(resultMessage.get("paidinfo").getAsString()).getJsonObject());
                    }else{
                        //TODO 作为默认选择（支付宝）
                        goAliPay(resultMessage.get("orderid").getAsString(),new ParseToGsonUtil(resultMessage.get("paidinfo").getAsString()).getJsonObject());
                    }
                }else{
                    CommonDialogHint.getInstance().showHint(this,getString(R.string.hint_order_invalid));
                }
            }catch (JSONException jsonExecption){
                jsonExecption.printStackTrace();
            }
        }else{
            //无效的结果
            CommonDialogHint.getInstance().showHint(this,getString(R.string.hint_order_invalid));
        }
    }

    public void goAliPay(final String orderId,JsonObject payInfo){
        new AlipayHandler(this, new AlipayHandler.AlipayCallback() {

            @Override
            public void paidComplete(int type, String msg, String result) {
                // TODO Auto-generated method stub
                if(type == AlipayHandler.PAID_SUCCESS){
                    //支付成功，并将支付提交提交到后台
                    JsonObject object = new ParseToGsonUtil(result).getJsonObject();
                    vertifyAliPayment(object.get("orderid").getAsString());
                }else if (type == AlipayHandler.PAID_FAILED) {
                    //支付失败
                    orderDetail.addProperty("status","1");
                    currentPayResult = PAY_RESULT.FAILURE;
                    goToPaymentResult(orderId);
                }
            }
        }).pay(payInfo);
    }

    public void goWechatPay(final String orderId, JsonObject payInfo){
        wechatOrderId = orderId;
        //TODO 拿到订单后，保存该次订单信息----去支付
        BaseApplication.getInstances().saveWxPayLastInfo(IPresenterBusinessCode.HOUSE_KEEPER_ORDER,orderId);
        new WeixinPayHandler(this).pay(payInfo);
    }

    public void vertifyAliPayment(String orderId){
        aliOrderId = orderId;
        presenter.verifyAliPaymentEncryptResult(this);
    }

    public void vertifyWechatPayment(String orderId){
        if(BaseApplication.getInstances().hasWxPayLastInfo() &&//TODO 校验当次支付是否完成----存在则表明进行过
                BaseApplication.getInstances().isCurrentOrder(IPresenterBusinessCode.HOUSE_KEEPER_ORDER)){//TODO
            presenter.verifyWechatPaymentEncryptResult(this);
        }else{
            //TODO 清除最近的支付信息
            currentPayResult = PAY_RESULT.FAILURE;
            goToPaymentResult(orderId);
        }
    }

    public void setAliResult(Object result){
        if(result!=null && result instanceof String){
            CommonResultEntity entity = (CommonResultEntity) SerializeToObjectUtil.getInstances().
                    jsonToObject(result.toString(),new TypeToken<CommonResultEntity>(){}.getType());
            if(entity!=null && ICommonData.HTTP_OK.equals(entity.getCode())){
                currentPayResult = PAY_RESULT.SUCCESS;
                goToPaymentResult(aliOrderId);
            }else{
                CommonDialogHint.getInstance().showHint(this,getString(R.string.hint_house_keeper_check_order));
            }
        }else{
            CommonDialogHint.getInstance().showHint(this,getString(R.string.hint_house_keeper_check_order));
        }
    }

    public void setWechatResult(Object result){
        if(result!=null && result instanceof String){
            CommonResultEntity entity = (CommonResultEntity) SerializeToObjectUtil.getInstances().
                    jsonToObject(result.toString(),new TypeToken<CommonResultEntity>(){}.getType());
            if(entity!=null && ICommonData.HTTP_OK.equals(entity.getCode())){
                currentPayResult = PAY_RESULT.SUCCESS;
                goToPaymentResult(wechatOrderId);
            }else{
                CommonDialogHint.getInstance().showHint(this,getString(R.string.hint_house_keeper_check_order));
            }
        }else{
            CommonDialogHint.getInstance().showHint(this,getString(R.string.hint_house_keeper_check_order));
        }
    }

    public void goToPaymentResult(final String orderId){
        if(currentPayResult==PAY_RESULT.SUCCESS){
            orderDetail.addProperty("status","2");
        }else{
            orderDetail.addProperty("status","1");
        }
        BaseApplication.getInstances().clearWxPayLastInfo();

        Intent intent = new Intent(this,PaymentResultActivity.class);
        intent.putExtra(IParamsName.PARAMS_COMMON_DATA,orderDetail.toString());
        intent.putExtra(IParamsName.PARAMS_PAY_RESULT_TYPE, selectedPayType==PAY_CHANNEL.ALI? getString(R.string.hint_pay_type_ali):
                getString(R.string.hint_pay_type_wechat));
        intent.putExtra(IParamsName.PARAMS_PAY_RESULT_CODE, currentPayResult.getValue());
        intent.putExtra(IParamsName.PARAMS_PAY_MONEY, defaultPrePay);
        intent.putExtra(IParamsName.PARAMS_PAY_ORDERID, orderId);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);//TODO 最开始的这种方式会导致返回从订单详情页返回首页的效果失效
        startActivityForResult(intent, ICommonActivityRequestCode.BACK_TO_MAIN);
    }

    //***********************************************************************
    public void initTitleBar() {
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.hint_house_keeper_order_title));

        menuText.setVisibility(View.VISIBLE);
        menuText.setText(getString(R.string.hint_house_keeper_order_user_agreement));

        presenter = new HouseKeeperOrderPresenter();
        payEncryptPresenter = new PayEncryptPresenter();
    }

    /**
     * 设置显示的默认值
     */
    public void setInfo() {
        //TODO 头像
        Picasso.with(this).load(entity.getImage()).
                error(R.drawable.ic_patient).fit().
                transform(new RoundTransform()).into(image);
        //名称
        tvName.setText(entity.getName());
        //等级
        int level = entity.getLevel();
        switch (level) {
            case 1:
                tvLevel.setText(getString(R.string.hint_house_keeper_detail_level1));
                break;
            case 2:
                tvLevel.setText(getString(R.string.hint_house_keeper_detail_level2));
                break;
            case 3:
                tvLevel.setText(getString(R.string.hint_house_keeper_detail_level3));
                break;
            default:
                tvLevel.setText(getString(R.string.hint_house_keeper_detail_level1));
                break;
        }
        //年龄
        tvAge.setText(getString(R.string.hint_house_keeper_detail_age_prefix) + entity.getAge());
        //价格
        tvPrice.setText(getString(R.string.hint_house_keeper_detail_price_prefix) +
                entity.getPrice() +
                getString(R.string.hint_house_keeper_detail_price_postfix));
        //评分
        rbar.setProgress(entity.getStar());
        rbar.setRating(entity.getStar());
        //TODO ************************************************************** 预约时间
        tvInfo.setText(getString(R.string.hint_house_keeper_order_info_time_title));

        tvBeginDate.setText(getString(R.string.hint_house_keeper_order_info_time_start));
        tvEndDate.setText(getString(R.string.hint_house_keeper_order_info_time_end));

        tvBeginDateValue.setText(getString(R.string.hint_house_keeper_order_info_time_start_hint));
        tvEndDateValue.setText(getString(R.string.hint_house_keeper_order_info_time_end_hint));

        //TODO ************************************************************** 预约人信息
        tvInfoService.setText(getString(R.string.hint_house_keeper_order_info_person_title));

        etContactName.setHint(getString(R.string.hint_house_keeper_order_info_person_name));
        etMobile.setHint(getString(R.string.hint_house_keeper_order_info_person_mobile_phone));
        etAddress.setHint(getString(R.string.hint_house_keeper_order_info_person_address));

        //TODO ************************************************************** 支付方式
        tvInfoPay.setText(getString(R.string.hint_house_keeper_order_info_pay_title));

        rbtn1.setText(getString(R.string.hint_house_keeper_order_info_pay_ali));
        rbtn2.setText(getString(R.string.hint_house_keeper_order_info_pay_wechat));

        tvHeji.setText(getString(R.string.hint_house_keeper_order_info_pay_sum));
        tvOrder.setText(getString(R.string.hint_house_keeper_order_info_pay_comfirm));


        //TODO 设置订单必要的的参数数据
        orderDetail.addProperty("housekeeperId",entity.getId()+"");
        orderDetail.addProperty("companyName",entity.getCompanyName());
        orderDetail.addProperty("housekeeperName",entity.getName());
        orderDetail.addProperty("image",entity.getImage());
        orderDetail.addProperty("price",entity.getPrice());
        orderDetail.addProperty("age",entity.getAge()+"");
        orderDetail.addProperty("serviceItem",entity.getServiceItem());
        orderDetail.addProperty("experience",entity.getExperience());
        orderDetail.addProperty("fromRegion",entity.getFromRegion());
        orderDetail.addProperty("evaluation",(String)null);

        payEncryptPresenter.getPublicKey(this);
    }

    //***********************************************************************


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ICommonActivityRequestCode.BACK_TO_MAIN:
                if(resultCode== ICommonActivityResultCode.BACK_TO_MAIN){
                    setResult(ICommonActivityResultCode.BACK_TO_MAIN);
                    finish();
                }
                break;
        }
    }
}
