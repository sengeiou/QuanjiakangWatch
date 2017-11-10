package com.quanjiakan.activity.common.index.housekeeper;

import android.app.Dialog;
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
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperListEntity;
import com.quanjiakan.net_presenter.HouseKeeperOrderPresenter;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.common.StringCheckUtil;
import com.quanjiakan.util.common.UnitExchangeUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.widget.RoundTransform;
import com.quanjiakan.view.dialog.ChangeBirthDialog;
import com.quanjiakan.watch.R;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

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

    private JsonObject orderDetail = new JsonObject();

    private Dialog noticeDialog;

    private GetHouseKeeperListEntity.ListBean entity;

    private HouseKeeperOrderPresenter presenter;

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
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER: {
                //TYPE_GET_STRING_NOCACHE
                //http://pay.quanjiakan.com:7080/familycore-pay/core/api_get?devicetype=0&platform=2&client=1&token=849f2bbb87dc5c049f20b85be3047e3b&user_id=11303&code=pay&action=housekeeper_produceorder&data={"note":"","housekeeperId":38,"companyId":7,"begindate":"2017-11-10","enddate":"2017-11-10","orderUserName":"发古板","mobile":"13212345678","address":"复活币","orderUserId":11303,"userId":11303,"paymentChannel":1}
                //Request Result:{"code":"200","message":"{\"orderid\":\"QJKKEEPER20171110053936135398\",\"paidinfo\":{\"partner\":\"2088021868486397\",\"seller_id\":\"13510237554@163.com\",\"out_trade_no\":\"QJKKEEPER20171110053936135398\",\"subject\":\"全家康支付\",\"body\":\"支付家政人员服务费用\",\"total_fee\":\"200.0\",\"notify_url\":\"http://pay.quanjiakan.com:7080/familycore-pay/notify_alipay.jsp\",\"service\":\"mobile.securitypay.pay\",\"payment_type\":\"1\",\"_input_charset\":\"utf-8\",\"it_b_pay\":\"30m\",\"return_url\":\"m.alipay.com\",\"sign\":\"CRKOZfxyv/KgU9EAUf/W7Ki/KmvKrm4dZIEe3bnbDg6IQ4ZDis5ebANLZhsuDfKW7YFoIxeWQFR6wOBLnyROEbmA/O5nW08VIUCQBNqWjSO6NDyphBQ2y76LMiO7r87CPlparrLQtwLCUX5BFRfVWF8+yLGJGuYFCWZ3gqOL5g8=\",\"code\":\"200\"}}"}
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_HOUSE_KEEPER_DETAIL_ID, entity.getId()+"");
                return params;
            }
            default:
                return null;
        }
    }

    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER:
                getDialog(this, getString(R.string.hint_get_house_keeper_order));
                break;
            default:
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER:
            default:
                break;
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER:
            default:
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {

        switch (type) {
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER:
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

        if(etMobile.length() == 0 || etContactName.length() == 0 || etAddress.length() == 0){
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_house_keeper_order_no_info));
            return;
        }
        if(!StringCheckUtil.isPhoneNumber(etMobile.getText().toString())){
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_house_keeper_order_error_mobile));
            return;
        }
        if(!StringCheckUtil.isAllChineseChar(etContactName.getText().toString())){
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_house_keeper_order_error_name));
            return;
        }

        presenter.doGetHouseKeeperOrder(this);

    }

    //***********************************************************************
    public void initTitleBar() {
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.hint_house_keeper_order_title));

        menuText.setVisibility(View.VISIBLE);
        menuText.setText(getString(R.string.hint_house_keeper_order_user_agreement));

        presenter = new HouseKeeperOrderPresenter();
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
    }

    //***********************************************************************
}
