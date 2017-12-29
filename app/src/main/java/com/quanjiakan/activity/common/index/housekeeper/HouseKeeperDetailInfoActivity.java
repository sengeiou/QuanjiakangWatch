package com.quanjiakan.activity.common.index.housekeeper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.constants.ICommonActivityRequestCode;
import com.quanjiakan.constants.ICommonActivityResultCode;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperCompanyPhone;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperDetailEntity;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperListEntity;
import com.quanjiakan.net_presenter.HouseKeeperDetailPresenter;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.widget.RoundTransform;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/9.
 */

public class HouseKeeperDetailInfoActivity extends BaseActivity {

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
    @BindView(R.id.rbar)
    RatingBar rbar;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_region)
    TextView tvRegion;
    @BindView(R.id.tv_experience)
    TextView tvExperience;
    @BindView(R.id.tv_items)
    TextView tvItems;
    @BindView(R.id.tv_info_company)
    TextView tvInfoCompany;
    @BindView(R.id.tv_name_company)
    TextView tvNameCompany;
    @BindView(R.id.tv_address_company)
    TextView tvAddressCompany;
    @BindView(R.id.image_call)
    ImageView imageCall;
    @BindView(R.id.tv_info_service)
    TextView tvInfoService;
    @BindView(R.id.tv_service_items)
    TextView tvServiceItems;
    @BindView(R.id.tv_buy)
    TextView tvBuy;

    private HouseKeeperDetailPresenter presenter;

    private GetHouseKeeperListEntity.ListBean entity;

    //*************************************************************************************************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_housekeeper_detail);
        ButterKnife.bind(this);

        initTitleBar();
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
        //********************************************
        //TODO 参数有效，进行

        presenter.doGetHouseKeeperDetailInfo(this);
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

    //*************************************************************************************************
    @Override
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.HOUSE_KEEPER_TYPE_DETAIL: {
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_HOUSE_KEEPER_DETAIL_ID, entity.getId()+"");
                return params;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_TYPE_COMPANY: {
                HashMap<String, String> params = new HashMap<>();
                return params;
            }
            default:
                return null;
        }
    }


    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.HOUSE_KEEPER_TYPE_DETAIL:
                getDialog(this, getString(R.string.hint_get_house_keeper_detail_data));
                break;
            case IPresenterBusinessCode.HOUSE_KEEPER_TYPE_COMPANY:
                break;
            default:
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.HOUSE_KEEPER_TYPE_DETAIL:
            case IPresenterBusinessCode.HOUSE_KEEPER_TYPE_COMPANY:
            default:
                break;
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.HOUSE_KEEPER_TYPE_DETAIL:
                setDetailDataIntoView(result);
                break;
            case IPresenterBusinessCode.HOUSE_KEEPER_TYPE_COMPANY:
                setCompanyPhone(result);
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.HOUSE_KEEPER_TYPE_DETAIL:
            case IPresenterBusinessCode.HOUSE_KEEPER_TYPE_COMPANY:
            default:
                if (errorMsg != null) {
                    CommonDialogHint.getInstance().showHint(this, errorMsg.toString());
                } else {
                    CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_net_request_fail));
                }
                break;
        }
    }

    //*************************************************************************************************

    @OnClick({R.id.ibtn_back, R.id.image_call, R.id.tv_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.image_call:
                callCompanyPhone();
                break;
            case R.id.tv_buy:
                jumpToOrder();
                break;
        }
    }

    //*************************************************************************************************

    public void initTitleBar() {
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.housekeeper_detail_title);

        ibtnMenu.setVisibility(View.GONE);
        menuText.setVisibility(View.GONE);

        presenter = new HouseKeeperDetailPresenter();
    }

    /**
     * 将数据展示到页面上
     */
    public void setDetailDataIntoView(Object object) {
        if(object!=null && object instanceof GetHouseKeeperDetailEntity &&
                ((GetHouseKeeperDetailEntity)object).getObject()!=null){
            GetHouseKeeperDetailEntity.ObjectBean entity = ((GetHouseKeeperDetailEntity) object).getObject();
            //TODO 使用访问得到的数据进行展示
            // 头像
            Picasso.with(this).load(entity.getImage()).
                    fit().
                    placeholder(R.drawable.image_placeholder).
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
            tvAge.setText(getString(R.string.hint_house_keeper_detail_age_prefix)+entity.getAge());
            //价格
            tvPrice.setText(getString(R.string.hint_house_keeper_detail_price_prefix)+
                    entity.getPrice()+
                    getString(R.string.hint_house_keeper_detail_price_postfix));
            //评分
            rbar.setProgress(entity.getStar());
            rbar.setRating(entity.getStar());
            //TODO ***************************保姆信息
            tvInfo.setText(getString(R.string.hint_house_keeper_detail_info_title));
            //公司地址
            tvCompany.setText(getString(R.string.hint_house_keeper_detail_company_prefix)+
                    getString(R.string.hint_house_keeper_detail_company_postfix));
            //区域
            tvRegion.setText(getString(R.string.hint_house_keeper_detail_region_prefix)+entity.getServiceRegion());
            //工龄
            tvExperience.setText(getString(R.string.hint_house_keeper_detail_experience_prefix)+
                    entity.getExperience()+
                    getString(R.string.hint_house_keeper_detail_experience_postfix));
            //是否住家
            int model = entity.getModel();
            switch (model) {
                case 1:
                    tvItems.setText(getString(R.string.hint_house_keeper_detail_stay_prefix)+
                            getString(R.string.hint_house_keeper_detail_stay_postfix1));
                    break;
                default:// model  0  是住家
                    tvItems.setText(getString(R.string.hint_house_keeper_detail_stay_prefix)+
                            getString(R.string.hint_house_keeper_detail_stay_postfix2));
                    break;
            }
            //TODO ***************************商家信息
            tvInfoCompany.setText(getString(R.string.hint_house_keeper_detail_company_title));
            //
            tvNameCompany.setText(getString(R.string.hint_house_keeper_detail_company_postfix));

            tvAddressCompany.setText(getString(R.string.hint_house_keeper_detail_company_location));
            //TODO ***************************服务范围

            tvInfoService.setText(getString(R.string.hint_house_keeper_detail_service_title));

            tvServiceItems.setText(entity.getServiceItem());

            tvBuy.setText(getString(R.string.hint_house_keeper_detail_buy));

            if(entity.getMobile()!=null && entity.getMobile().length()>0){
                imageCall.setTag(entity.getMobile());
            }else if(HouseKeeperDetailInfoActivity.this.entity.getMobile()!=null &&
                    HouseKeeperDetailInfoActivity.this.entity.getMobile().length()>0){
                imageCall.setTag(HouseKeeperDetailInfoActivity.this.entity.getMobile());
            }else{
                imageCall.setTag(null);
            }

        }else{
            //TODO 使用传递得到的数据进行展示

            // 头像
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
            tvAge.setText(getString(R.string.hint_house_keeper_detail_age_prefix)+entity.getAge());
            //价格
            tvPrice.setText(getString(R.string.hint_house_keeper_detail_price_prefix)+
                    entity.getPrice()+
            getString(R.string.hint_house_keeper_detail_price_postfix));
            //评分
            rbar.setProgress(entity.getStar());
            rbar.setRating(entity.getStar());
            //TODO ***************************保姆信息
            tvInfo.setText(getString(R.string.hint_house_keeper_detail_info_title));
            //公司地址
            tvCompany.setText(getString(R.string.hint_house_keeper_detail_company_prefix)+
                    getString(R.string.hint_house_keeper_detail_company_postfix));
            //区域
            tvRegion.setText(getString(R.string.hint_house_keeper_detail_region_prefix)+entity.getServiceRegion());
            //工龄
            tvExperience.setText(getString(R.string.hint_house_keeper_detail_experience_prefix)+
                    entity.getExperience()+
                    getString(R.string.hint_house_keeper_detail_experience_postfix));
            //是否住家
            int model = entity.getModel();
            switch (model) {
                case 1:
                    tvItems.setText(getString(R.string.hint_house_keeper_detail_stay_prefix)+
                            getString(R.string.hint_house_keeper_detail_stay_postfix1));
                    break;
                default:// model  0  是住家
                    tvItems.setText(getString(R.string.hint_house_keeper_detail_stay_prefix)+
                            getString(R.string.hint_house_keeper_detail_stay_postfix2));
                    break;
            }
            //TODO ***************************商家信息
            tvInfoCompany.setText(getString(R.string.hint_house_keeper_detail_company_title));
            //
            tvNameCompany.setText(getString(R.string.hint_house_keeper_detail_company_postfix));

            tvAddressCompany.setText(getString(R.string.hint_house_keeper_detail_company_location));
            //TODO ***************************服务范围

            tvInfoService.setText(getString(R.string.hint_house_keeper_detail_service_title));

            tvServiceItems.setText(entity.getServiceItem());

            tvBuy.setText(getString(R.string.hint_house_keeper_detail_buy));

            if(entity.getMobile()!=null && entity.getMobile().length()>0){
                imageCall.setTag(entity.getMobile());
            }else{
                imageCall.setTag(null);
            }
        }
    }

    /**
     * 跳转至发布订单页
     */
    public void jumpToOrder() {
        Intent intent = new Intent(this,HouseKeeperOrderActivity.class);
        intent.putExtra(IParamsName.PARAMS_HOUSE_KEEPER_DETAIL_INFO,entity);
        startActivityForResult(intent, ICommonActivityRequestCode.BACK_TO_MAIN);
    }

    public void setCompanyPhone(Object object){
        if(object!=null && object instanceof GetHouseKeeperCompanyPhone && ((GetHouseKeeperCompanyPhone)object).getObject()!=null){
            GetHouseKeeperCompanyPhone.ObjectBean entity = (GetHouseKeeperCompanyPhone.ObjectBean) object;
            if(entity.getData()!=null && entity.getData().length()>0){
                imageCall.setTag(entity.getData());
                callCompanyPhone();
            }else{
                imageCall.setTag("");
                callCompanyPhone();
            }
        }else{
            imageCall.setTag("");
            callCompanyPhone();
        }
    }

    public void callCompanyPhone() {
        if (imageCall.getTag() == null) {
            presenter.getHouseKeeperCompanyPhone(this);
            return;
        }else if("".equals(imageCall.getTag().toString())){
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_housekeeper_wrong_phonenumber));
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        //TODO 检查电话是否正确
//        if(StringCheckUtil.isPhoneNumber(imageCall.getTag().toString())){
//
//        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + imageCall.getTag().toString()));
        startActivity(intent);
    }

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
