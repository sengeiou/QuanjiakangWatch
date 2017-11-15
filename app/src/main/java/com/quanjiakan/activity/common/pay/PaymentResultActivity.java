package com.quanjiakan.activity.common.pay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.ICommonActivityRequestCode;
import com.quanjiakan.activity.base.ICommonActivityResultCode;
import com.quanjiakan.activity.common.index.housekeeper.HouseKeeperOrderActivity;
import com.quanjiakan.activity.common.setting.orders.housekeeper.HouseKeeperOrderDetailActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.watch.R;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/14.
 */

public class PaymentResultActivity extends BaseActivity {

    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ibtn_menu)
    ImageButton ibtnMenu;
    @BindView(R.id.menu_text)
    TextView menuText;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.tv_pay_channel)
    TextView tvPayChannel;
    @BindView(R.id.tv_orderlist)
    TextView tvOrderlist;
    @BindView(R.id.tv_homepage)
    TextView tvHomepage;

    private String data;
    private String typeCode;
    private HouseKeeperOrderActivity.PAY_RESULT resultCode;
    private int money;
    private String orderId;
    private final int defaultPayMoney = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payresult);
        ButterKnife.bind(this);
        data = getIntent().getStringExtra(IParamsName.PARAMS_COMMON_DATA);
        typeCode = getIntent().getStringExtra(IParamsName.PARAMS_PAY_RESULT_TYPE);
        int result = getIntent().getIntExtra(IParamsName.PARAMS_PAY_RESULT_CODE, HouseKeeperOrderActivity.PAY_RESULT.FAILURE.getValue());
        if (result == HouseKeeperOrderActivity.PAY_RESULT.SUCCESS.getValue()) {
            resultCode = HouseKeeperOrderActivity.PAY_RESULT.SUCCESS;
        } else {
            resultCode = HouseKeeperOrderActivity.PAY_RESULT.FAILURE;
        }
        money = getIntent().getIntExtra(IParamsName.PARAMS_PAY_MONEY, defaultPayMoney);
        orderId = getIntent().getStringExtra(IParamsName.PARAMS_PAY_ORDERID);

        if (data == null || typeCode == null || orderId == null) {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_error_activity_parameters), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            return;
        }

        initTitle();

        setView();
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

    public void initTitle() {
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setText(R.string.pay_result_title);

        ibtnMenu.setVisibility(View.GONE);
        menuText.setVisibility(View.GONE);
    }

    public void setView() {
        tvHomepage.setText(R.string.pay_result_back_home);

        tvOrderlist.setText(R.string.pay_result_order_detail);


        //TODO 根据支付状态判断支付结果，并展示对应的提醒
        if (resultCode == HouseKeeperOrderActivity.PAY_RESULT.SUCCESS) {
            //支付成功
            image.setImageResource(R.drawable.payment_success);
            tvPayChannel.setText(getString(R.string.pay_result_order_detail_1) + "\n" +
                    orderId + "\n" + getString(R.string.pay_result_order_detail_2) + typeCode +
                    getString(R.string.pay_result_order_detail_success) + "\n" +
                    getString(R.string.pay_result_order_detail_money_symbol_China_yuan) + money +
                    getString(R.string.pay_result_order_detail_money_yuan));
        } else {
            //支付失败
            image.setImageResource(R.drawable.payment_failure);
            tvPayChannel.setText(getString(R.string.pay_result_order_detail_1) + "\n" +
                    orderId + "\n" + getString(R.string.pay_result_order_detail_2) + typeCode +
                    getString(R.string.pay_result_order_detail_failure) + "\n" +
                    getString(R.string.pay_result_order_detail_money_symbol_China_yuan) + money +
                    getString(R.string.pay_result_order_detail_money_yuan));
        }
    }

    @OnClick({R.id.ibtn_back, R.id.tv_orderlist, R.id.tv_homepage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back: {
                finish();
                break;
            }
            case R.id.tv_orderlist: {
                toOrderDetail();
                break;
            }
            case R.id.tv_homepage: {
                setResult(ICommonActivityResultCode.BACK_TO_MAIN);
                finish();
                break;
            }
        }
    }

    public void toOrderDetail() {
        Intent intent = new Intent(this, HouseKeeperOrderDetailActivity.class);
        intent.putExtra(IParamsName.PARAMS_COMMON_DATA, data);
        startActivityForResult(intent, ICommonActivityRequestCode.BACK_TO_MAIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ICommonActivityRequestCode.BACK_TO_MAIN:
                setResult(ICommonActivityResultCode.BACK_TO_MAIN);
                finish();
                break;
        }
    }
}
