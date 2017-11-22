package com.quanjiakan.activity.common.index.healthinquiry;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.ICommonActivityRequestCode;
import com.quanjiakan.activity.base.ICommonActivityResultCode;
import com.quanjiakan.activity.base.ICommonData;
import com.quanjiakan.adapter.HealthInquriryPatientInfoAdapter;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.db.entity.HealthInquiryPatientDataEntity;
import com.quanjiakan.db.manager.DaoManager;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net_presenter.HealthInquiryCreateProblemPresenter;
import com.quanjiakan.net_presenter.HealthInquirySendProblemPresenter;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.common.ParseToGsonUtil;
import com.quanjiakan.util.common.UnitExchangeUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/16.
 */

public class HealthInquirySendProblemActivity extends BaseActivity {


    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edit)
    TextView edit;
    @BindView(R.id.edit_line)
    LinearLayout editLine;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.layout_items)
    ListView layoutItems;
    @BindView(R.id.layout_datas)
    RelativeLayout layoutDatas;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(android.R.id.tabhost)
    RelativeLayout tabhost;
    @BindView(R.id.hint)
    TextView hint;
    @BindView(R.id.chk)
    TextView chk;
    @BindView(R.id.add)
    RelativeLayout add;

    private List<HealthInquiryPatientDataEntity> mData = new ArrayList<>();
    private String problemData;

    private HealthInquriryPatientInfoAdapter mAdapter;

    private HealthInquirySendProblemPresenter presenter;

    //病人资料格式  {"type":"patient_meta","age":"23","sex":"男","name":"小莫"}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_get_patient_data);
        ButterKnife.bind(this);
        initTitle();
        problemData = getIntent().getStringExtra(IParamsName.PARAMS_COMMON_DATA);
        if (problemData == null || problemData.length() < 1) {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_error_activity_parameters), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            return;
        }

        initView();

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
    public Object getParamter(int type) {
        switch (type){
            case IPresenterBusinessCode.HEALTH_INQUIRY_SEND_PROBLEM: {
                HashMap<String,String> params = new HashMap<>();

                JsonArray array = new ParseToGsonUtil(problemData).getJsonArray();
                JsonObject patient = new JsonObject();
                HealthInquiryPatientDataEntity entity = mAdapter.getByPosition(mAdapter.getCheckedPosition());
                patient.addProperty(ICommonData.HEALTH_INQUIRY_SEND_TYPE,ICommonData.HEALTH_INQUIRY_SEND_TYPE_VALUE);
                patient.addProperty(ICommonData.HEALTH_INQUIRY_SEND_AGE,entity.getAge());
                patient.addProperty(ICommonData.HEALTH_INQUIRY_SEND_GENDER,entity.getGender());
                patient.addProperty(ICommonData.HEALTH_INQUIRY_SEND_NAME,entity.getName());
                array.add(patient);

                params.put(IParamsName.PARAMS_HEALTH_INQURIRY_TYPE, ICommonData.HEALTH_INQUIRY_TYPE);
                params.put(IParamsName.PARAMS_COMMON_MEMBERID,BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_HEALTH_INQURIRY_CONTENT,array.toString());
                params.put(IParamsName.PARAMS_HEALTH_INQURIRY_FROMTOKEN,BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN,BaseApplication.getInstances().getLoginInfo().getToken());
                return params;
            }
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type){
            case IPresenterBusinessCode.HEALTH_INQUIRY_SEND_PROBLEM: {
                getDialog(this,getString(R.string.hint_health_inquiry_sending));
                break;
            }
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type){
            case IPresenterBusinessCode.HEALTH_INQUIRY_SEND_PROBLEM: {
                break;
            }
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type){
            case IPresenterBusinessCode.HEALTH_INQUIRY_SEND_PROBLEM: {
                setResult(result);
                break;
            }
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type){
            case IPresenterBusinessCode.HEALTH_INQUIRY_SEND_PROBLEM: {
                break;
            }
        }
        if(errorMsg!=null && errorMsg.toString().length()>0){
            CommonDialogHint.getInstance().showHint(this,errorMsg.toString());
        }else{
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_common_net_request_fail));
        }
    }

    @OnClick({R.id.edit, R.id.tv_submit, R.id.ibtn_back, R.id.add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add:
                toAddPatientData();
                break;
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.edit:
                //判断是否选中一个，或者没有的话，则提示创建一个
                if (isValidToEdit()) {
                    toEditPatientData();
                }
                break;
            case R.id.tv_submit:
                sendProblem();
                break;
        }
    }

    public void sendProblem(){
        if(isValidToEdit()){
            presenter.sendInquiryProblem(this);
        }
    }

    public boolean isValidToEdit() {
        if (mData.size() < 1) {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_health_inquiry_please_create_one));
            return false;
        }
        //TODO---检查存在选择的

        if (mAdapter.getByPosition(mAdapter.getCheckedPosition()) == null) {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_health_inquiry_please_choose_one));
            return false;
        }

        return true;
    }

    public void toAddPatientData() {
        Intent intent = new Intent(this, HealthInquiryCreatePatientInfoActivity.class);
        startActivityForResult(intent, ICommonActivityRequestCode.HEALTH_INQUIRY_CREATE_PATIENT_INFO);
    }

    public void toEditPatientData() {
        //TODO 获取到选择的档案信息，并传递过去
        Intent intent = new Intent(this, HealthInquiryEditPatientInfoActivity.class);
        intent.putExtra(IParamsName.PARAMS_COMMON_DATA,mAdapter.getByPosition(mAdapter.getCheckedPosition()));
        startActivityForResult(intent, ICommonActivityRequestCode.HEALTH_INQUIRY_EDIT_PATIENT_INFO);
    }

    public void initTitle() {
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.health_inquiry_send_problem_title);

        presenter = new HealthInquirySendProblemPresenter();
    }

    //加载
    public void initView() {
        hint.setText(R.string.health_inquiry_send_problem_hint);

        edit.setText(R.string.health_inquiry_send_problem_edit);

        tvSubmit.setText(R.string.common_submit);

        loadDataIntoListView();
    }

    //TODO 将保存的用户对应信息展示出来
    public void loadDataIntoListView() {
        loadAndFilterData();//
        loadInfoListView();
        resetListViewHeight();
    }

    public void loadAndFilterData() {
        mData.clear();
        List<HealthInquiryPatientDataEntity> temp = DaoManager.getInstances(this).getDaoSession().getHealthInquiryPatientDataEntityDao().loadAll();
        //TODO 过滤出属于当前用户的数据
        for (HealthInquiryPatientDataEntity entity : temp) {
            if (BaseApplication.getInstances().getLoginInfo().getUserId().equals(entity.getBelongUserid())) {
                mData.add(entity);
            }
        }
    }

    public void loadInfoListView() {
        if (mAdapter == null) {
            mAdapter = new HealthInquriryPatientInfoAdapter(this, mData);
        }
        layoutItems.setAdapter(mAdapter);
        layoutItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mAdapter.clickPosition((int) l);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    //TODO 限制ListView的高度---根据数据的数量限制

    public void resetListViewHeight() {
        if (mData.size() == 0) {
            layoutItems.setVisibility(View.GONE);
        } else if (mData.size() < 5) {
            layoutItems.setVisibility(View.VISIBLE);
            //如果数据条数在4个以下
            ListAdapter la = layoutItems.getAdapter();
            if (null == la) {
                return;
            }
            // calculate height of all items.*********************   其实会存在问题，以
            int h = 0;
            final int cnt = la.getCount();
            for (int i = 0; i < cnt; i++) {
                View item = la.getView(i, null, layoutItems);
                item.measure(0, 0);
                h += item.getMeasuredHeight();
            }
            // reset ListView height
            ViewGroup.LayoutParams lp = layoutItems.getLayoutParams();
            lp.height = h + (layoutItems.getDividerHeight() * (cnt - 1)) +
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.3f * cnt, getResources().getDisplayMetrics());//缓冲距离
            layoutItems.setLayoutParams(lp);
        } else {
            layoutItems.setVisibility(View.VISIBLE);
            //数据条数在4个以上，使用固定高度---避免超出显示
            ViewGroup.LayoutParams lp = layoutItems.getLayoutParams();
            lp.width = lp.MATCH_PARENT;
            lp.height = UnitExchangeUtil.dip2px(this, 240);
            layoutItems.setLayoutParams(lp);
        }
    }

    public void setResult(Object object){
        if(object!=null && object instanceof String){
            JsonObject jsonObject = new ParseToGsonUtil(object.toString()).getJsonObject();
            if(jsonObject.has("code") && ICommonData.HTTP_OK.equals(jsonObject.get("code").getAsString())){
                CommonDialogHint.getInstance().showHint(this, getString(R.string.error_send_inquiry_success), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setResult(ICommonActivityResultCode.BACK_TO_MAIN);
                        finish();
                    }
                });
            }else{
                CommonDialogHint.getInstance().showHint(this, getString(R.string.error_send_inquiry_fail));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ICommonActivityRequestCode.HEALTH_INQUIRY_CREATE_PATIENT_INFO:
                if (resultCode == ICommonActivityResultCode.RELOAD_DATA) {
                    loadDataIntoListView();
                }
                break;
            case ICommonActivityRequestCode.HEALTH_INQUIRY_EDIT_PATIENT_INFO:
                if (resultCode == ICommonActivityResultCode.RELOAD_DATA) {
                    loadDataIntoListView();
                }
                break;
        }
    }
}
