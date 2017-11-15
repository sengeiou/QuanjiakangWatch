package com.quanjiakan.activity.common.setting.orders.housekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.common.image.ImageViewerActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.entity.HouseKeeperOrderDetailEntity;
import com.quanjiakan.util.common.ParseToGsonUtil;
import com.quanjiakan.util.common.SerializeToObjectUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.widget.RoundTransform;
import com.quanjiakan.watch.R;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/14.
 */

public class HouseKeeperOrderDetailActivity extends BaseActivity {

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
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_rate_key)
    TextView tvRateKey;
    @BindView(R.id.rbar)
    RatingBar rbar;
    @BindView(R.id.tv_mark)
    TextView tvMark;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_line)
    TextView tvLine;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_region)
    TextView tvRegion;
    @BindView(R.id.tv_experience)
    TextView tvExperience;
    @BindView(R.id.layout_desc)
    RelativeLayout layoutDesc;
    @BindView(R.id.tv_mark_company)
    TextView tvMarkCompany;
    @BindView(R.id.tv_info_company)
    TextView tvInfoCompany;
    @BindView(R.id.tv_line_company)
    TextView tvLineCompany;
    @BindView(R.id.tv_order_name)
    TextView tvOrderName;
    @BindView(R.id.tv_order_address)
    TextView tvOrderAddress;


    private String data;
    private HouseKeeperOrderDetailEntity entity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_housekeeper_order_detail);
        ButterKnife.bind(this);
        data = getIntent().getStringExtra(IParamsName.PARAMS_COMMON_DATA);

        initTitle();

        if (data == null) {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_error_activity_parameters), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            return;
        }
        setView();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    public void initTitle() {
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setText(R.string.order_detail_title);

        ibtnMenu.setVisibility(View.GONE);
        menuText.setVisibility(View.GONE);
    }

    public void setView() {
//        JsonObject jsonObject = new ParseToGsonUtil(data).getJsonObject();
//        bind(jsonObject);
        bindViewValue();
    }

    protected void bind(final JsonObject housekeeper) {
        //{"housekeeper_id":"1241","company_name":"广州爱妈妈教育信息咨询有限公司","housekeeper_name":"陈小金","image":"http://picture.quanjiakan.com:9080/quanjiakan/resources/housekeeper/20161109201249_5vuets.png","price":"3500-4000","age":"52","service_item":"照顾老人家，一般家务，做饭，做卫生。带小孩。","experience":"5","from_region":"湖南,长沙,芙蓉区","evaluation":null,"begindate":"2017-09-29","enddate":"2017-09-29","order_user_name":"复活币","mobile":"18011935659","address":"也不能","id":"{\"orderid\":\"QJKKEEPER20170929060311512037\",\"paidinfo\":{\"partner\":\"2088021868486397\",\"seller_id\":\"13510237554@163.com\",\"out_trade_no\":\"QJKKEEPER20170929060311512037\",\"subject\":\"全家康支付\",\"body\":\"支付家政人员服务费用\",\"total_fee\":\"200.0\",\"notify_url\":\"http://pay.quanjiakan.com:7080/familycore-pay/notify_alipay.jsp\",\"service\":\"mobile.securitypay.pay\",\"payment_type\":\"1\",\"_input_charset\":\"utf-8\",\"it_b_pay\":\"30m\",\"return_url\":\"m.alipay.com\",\"sign\":\"enZIYqn/6WDgI0B3RjxdMrgNcHuwUiHFk7oD8JX7lp0Q3BoPDRC/DB4mAPM0pcTi+gT54oY++WW1xpYCdBNtGWoWM5y94KShlfAlQCSW3P0uPNSf/3dHZDu8XzI8ecXX1k7fWVauMeA5KxKlW2S0v+q9gFrgTSRZReDP1xntb48=\",\"code\":\"200\"}}","orderid":"QJKKEEPER20170929060311512037","createtime":"2017-09-29 18:03:14","status":"1"}
        tvName.setText(housekeeper.get("housekeeperName").getAsString());
        if (!housekeeper.has("price") || housekeeper.get("price").getAsString() == null || housekeeper.get("price").getAsString().length() < 1) {
            tvPrice.setVisibility(View.GONE);
        } else {
            tvPrice.setText(getString(R.string.order_detail_price_1) + housekeeper.get("price").getAsString() + getString(R.string.order_detail_price_2) );
        }
        tvAge.setText(getString(R.string.order_detail_age_1) + housekeeper.get("age").getAsInt() + getString(R.string.order_detail_age_2));

        if (housekeeper.has("star") && housekeeper.get("star").getAsString() != null && housekeeper.get("star").getAsString().length() > 1) {
            int star = housekeeper.get("star").getAsInt();
            rbar.setRating(star);
            rbar.setProgress(housekeeper.get("star").getAsInt());
        } else {
            rbar.setVisibility(View.GONE);
            tvRateKey.setVisibility(View.GONE);
        }

        if (housekeeper.get("begindate").getAsString().length() > 10 && housekeeper.get("enddate").getAsString().length() > 10) {
            tvCompany.setText(getString(R.string.order_detail_time) + housekeeper.get("begindate").getAsString().substring(0, 10) +
                    getString(R.string.order_detail_duration) + housekeeper.get("enddate").getAsString().substring(0, 10));
        } else {
            tvCompany.setText(getString(R.string.order_detail_time) + housekeeper.get("begindate").getAsString() +
                    getString(R.string.order_detail_duration) + housekeeper.get("enddate").getAsString());
        }


        int status = housekeeper.get("status").getAsInt();
        if (status == 2) {//已付款
            /**
             * 需要给出费用的算式
             *
             * housekeeper.get("service_region").getAsString()
             */
            tvRegion.setText(getString(R.string.order_detail_sum_money) + "");

            /**
             * housekeeper.get("experience").getAsString()+"年"
             * 实付定金
             *
             * 是否付款
             */
            tvExperience.setText(getString(R.string.order_detail_has_pay_prefix) + getString(R.string.order_detail_has_pay_success));
        } else {
            /**
             * 需要给出费用的算式
             *
             * housekeeper.get("service_region").getAsString()
             */
            tvRegion.setText(getString(R.string.order_detail_sum_money) + "");

            /**
             * housekeeper.get("experience").getAsString()+"年"
             */
            tvExperience.setText(getString(R.string.order_detail_has_pay_prefix) + getString(R.string.order_detail_has_pay_fail));
        }


        /**
         * 预约人名称    电话号码
         */
        tvOrderName.setText(housekeeper.get("orderUserName").getAsString() + getString(R.string.order_detail_space) + housekeeper.get("mobile").getAsString());

        /**
         * 预约人地址
         */
        tvOrderAddress.setText(housekeeper.get("address").getAsString());

        Picasso.with(this).load(housekeeper.get("image").getAsString()).
                error(R.drawable.ic_patient).fit().
                transform(new RoundTransform()).into(image);
    }


    public void bindViewValue(){
        entity = (HouseKeeperOrderDetailEntity) SerializeToObjectUtil.getInstances().jsonToObject(data,
                new TypeToken<HouseKeeperOrderDetailEntity>(){}.getType());

        tvName.setText(entity.getHousekeeperName());
        if (entity.getPrice()==null || entity.getPrice().length() < 1) {
            tvPrice.setVisibility(View.GONE);
        } else {
            tvPrice.setText(getString(R.string.order_detail_price_1) + entity.getPrice() +
                    getString(R.string.order_detail_price_2) );
        }
        tvAge.setText(getString(R.string.order_detail_age_1) + entity.getAge() + getString(R.string.order_detail_age_2));

        //TODO 数据中未含有评级数据
        rbar.setVisibility(View.GONE);
        tvRateKey.setVisibility(View.GONE);

        if (entity.getBegindate().length() > 10 && entity.getEnddate().length() > 10) {
            tvCompany.setText(getString(R.string.order_detail_time) + entity.getBegindate().substring(0, 10) + getString(R.string.order_detail_duration) + entity.getEnddate().substring(0, 10));
        } else {
            tvCompany.setText(getString(R.string.order_detail_time) + entity.getBegindate() + getString(R.string.order_detail_duration) + entity.getEnddate());
        }


        int status = Integer.parseInt(entity.getStatus());
        if (status == 2) {//已付款
            /**
             * 需要给出费用的算式
             *
             * housekeeper.get("service_region").getAsString()
             */
            tvRegion.setText(getString(R.string.order_detail_sum_money) + "");

            /**
             * housekeeper.get("experience").getAsString()+"年"
             * 实付定金
             *
             * 是否付款
             */
            tvExperience.setText(getString(R.string.order_detail_has_pay_prefix) + getString(R.string.order_detail_has_pay_success));
        } else {
            /**
             * 需要给出费用的算式
             *
             * housekeeper.get("service_region").getAsString()
             */
            tvRegion.setText(getString(R.string.order_detail_sum_money) + "");

            /**
             * housekeeper.get("experience").getAsString()+"年"
             */
            tvExperience.setText(getString(R.string.order_detail_has_pay_prefix) + getString(R.string.order_detail_has_pay_fail));
        }


        /**
         * 预约人名称    电话号码
         */
        tvOrderName.setText(entity.getOrderUserName() + "    " + entity.getMobile());

        /**
         * 预约人地址
         */
        tvOrderAddress.setText(entity.getAddress());

        Picasso.with(this).load(entity.getImage()).
                error(R.drawable.ic_patient).fit().
                transform(new RoundTransform()).into(image);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @OnClick({R.id.ibtn_back,R.id.image})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.ibtn_back: {
                finish();
                break;
            }
            case R.id.image: {
                if(entity!=null){
                    Intent intent = new Intent(HouseKeeperOrderDetailActivity.this, ImageViewerActivity.class);
                    intent.putExtra(IParamsName.PARAMS_URL, entity.getImage());
                    startActivity(intent);
                }else{
                    JsonObject jsonObject = new ParseToGsonUtil(data).getJsonObject();
                    Intent intent = new Intent(HouseKeeperOrderDetailActivity.this, ImageViewerActivity.class);
                    intent.putExtra(IParamsName.PARAMS_URL, jsonObject.get("image").getAsString());
                    startActivity(intent);
                }
                break;
            }
        }

    }
}
