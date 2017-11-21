package com.quanjiakan.activity.common.index.healthinquiry;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.ICommonActivityRequestCode;
import com.quanjiakan.activity.base.ICommonActivityResultCode;
import com.quanjiakan.activity.common.setting.healthinquiry.HealthInquiryFurtherAskActivity;
import com.quanjiakan.adapter.HealthInquiryProblemListAdapter;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.PostLastTenMessage;
import com.quanjiakan.net_presenter.HealthInquiryCreateProblemPresenter;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.common.KeyBoardUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/15.
 */

public class HealthInquiryCreateProblemActivity extends BaseActivity {
    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ibtn_menu)
    ImageButton ibtnMenu;
    @BindView(R.id.menu_text)
    TextView menuText;
    //****************************************************************
    @BindView(R.id.et_text)
    EditText etText;
    @BindView(R.id.input_number_hint)
    TextView inputNumberHint;
    @BindView(R.id.item_main_listview_title_name)
    TextView itemMainListviewTitleName;
    @BindView(R.id.listview)
    PullToRefreshListView listview;
    //****************************************************************
    @BindView(R.id.nonedata)
    ImageView nonedata;
    @BindView(R.id.nonedatahint)
    TextView nonedatahint;

    private KeyBoardUtil keyBoardUtil;
    private CharSequence tempString;

    private List<PostLastTenMessage.ListBean> mData = new ArrayList<>();

    private HealthInquiryProblemListAdapter mAdapter;

    private HealthInquiryCreateProblemPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_create_problem);
        ButterKnife.bind(this);


        initTitleBar();
        //TODO 先展示无数据提示
        isShowNoDataHint(true);

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
            case IPresenterBusinessCode.HEALTH_INQUIRY_CREATE_PROBLEM: {
                HashMap<String,String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_MEMBERID, BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                return params;
            }
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type){
            case IPresenterBusinessCode.HEALTH_INQUIRY_CREATE_PROBLEM: {
                getDialog(this,getString(R.string.hint_health_inquiry_last_10_problem_hint));
                break;
            }
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type){
            case IPresenterBusinessCode.HEALTH_INQUIRY_CREATE_PROBLEM: {
                break;
            }
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type){
            case IPresenterBusinessCode.HEALTH_INQUIRY_CREATE_PROBLEM: {
                listview.onRefreshComplete();
                setIntoListView(result);
                break;
            }
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type){
            case IPresenterBusinessCode.HEALTH_INQUIRY_CREATE_PROBLEM: {
                listview.onRefreshComplete();
                break;
            }
        }
        if(errorMsg!=null && errorMsg.toString().length()>0){
            CommonDialogHint.getInstance().showHint(this,errorMsg.toString());
        }else{
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_common_net_request_fail));
        }
    }

    @OnClick({R.id.ibtn_back, R.id.menu_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.menu_text:
                //TODO 下一步
                if(isValidProblem()){
                    toSendProblem();
                }
                break;
        }
    }

    //TODO 校验问题的
    public boolean isValidProblem(){
        if(etText.getText().toString().length()<1 ){
            CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_health_inquiry_please_input));
            return false;
        }

        if(etText.getText().toString().length()>0 && etText.getText().toString().length()<10 ){
            CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_health_inquiry_less_then_10));
            return false;
        }

        if(etText.getText().toString().length()>1000){
            CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_health_inquiry_more_then_1000));
            return false;
        }
        return true;
    }

    public void toSendProblem(){
        //TODO 构建春雨医生的问题
        JsonArray array = new JsonArray();
        JsonObject problem = new JsonObject();
        /**
         * 文字消息的格式
         type:text
         text:problem
         */
        problem.addProperty("type","text");
        problem.addProperty("text",etText.getText().toString());
        /**
         语音格式为
         type:text
         file:path   ---------------外链形式的链接--MP3格式

         图片格式为:
         type:image
         file:path   ---------------外链形式的链接

         每一次发送都是以数组形式发送
         */
        array.add(problem);

        Intent intent = new Intent(this,HealthInquirySendProblemActivity.class);
        intent.putExtra(IParamsName.PARAMS_COMMON_DATA,array.toString());
        startActivityForResult(intent,ICommonActivityRequestCode.BACK_TO_MAIN);
    }

    public void initTitleBar(){
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setText(R.string.health_inquiry_create_problem_title);

        ibtnMenu.setVisibility(View.GONE);

        menuText.setVisibility(View.VISIBLE);
        menuText.setText(R.string.health_inquiry_create_problem_menu_next);

        keyBoardUtil = new KeyBoardUtil();
        presenter = new HealthInquiryCreateProblemPresenter();
    }

    public void isShowNoDataHint(boolean isShow){
        if(isShow){
            nonedata.setVisibility(View.VISIBLE);
            nonedatahint.setVisibility(View.VISIBLE);

            nonedata.setImageResource(R.drawable.message_group_ico_sel);
            nonedatahint.setText(R.string.hint_common_hint_no_data);
        }else{
            nonedata.setVisibility(View.GONE);
            nonedatahint.setVisibility(View.GONE);
        }
    }

    public void initView(){
        //
        inputNumberHint.setText(R.string.health_inquiry_create_problem_init_number_hint);

        etText.setHint(R.string.health_inquiry_create_problem_hint);

        etText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(focus){
                    keyBoardUtil.showSoftInput(HealthInquiryCreateProblemActivity.this,etText);
                }else{
                    keyBoardUtil.hideSoftInput(HealthInquiryCreateProblemActivity.this,etText);
                }
            }
        });

        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tempString = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //TODO 变更文字字数
                if(tempString==null || tempString.length()<1){
                    inputNumberHint.setText(R.string.health_inquiry_create_problem_init_number_hint);
                }else{
                    inputNumberHint.setText(tempString.length()+
                            getString(R.string.health_inquiry_create_problem_init_number_middle)+
                            getString(R.string.health_inquiry_create_problem_init_number_last));
                    //TODO 增加对超出长度的提示
                    if(tempString.length()>1000){
                        etText.setText(tempString.subSequence(0,1000));
                        CommonDialogHint.getInstance().showHint(HealthInquiryCreateProblemActivity.this,
                                getString(R.string.hint_health_inquiry_length_limit));
                    }
                }
            }
        });

        itemMainListviewTitleName.setText(R.string.health_inquiry_create_problem_history);

        loadLastTenMessage();
    }

    //TODO 获取，并加载最后10条数据
    public void loadLastTenMessage(){
        presenter.getLastTenMessage(this);
    }

    public void setIntoListView(Object result){
        if(result!=null && result instanceof PostLastTenMessage){
            PostLastTenMessage entity = (PostLastTenMessage) result;
            if(entity.getList()!=null && entity.getList().size()>0){
                isShowNoDataHint(false);//隐藏无数据提示
                mData.clear();
                mData.addAll(entity.getList());
                resortData();
                setListViewData(mData);
            }else{
                isShowNoDataHint(true);
                //TODO 置空ListView对应的Adapter
                setListViewEmpty();
            }
        }else{
            isShowNoDataHint(true);
            //TODO 置空ListView对应的Adapter
            setListViewEmpty();
        }
    }

    public void resortData(){
        Collections.sort(mData, new Comparator<PostLastTenMessage.ListBean>() {
            @Override
            public int compare(PostLastTenMessage.ListBean patientProblemInfoEntity, PostLastTenMessage.ListBean t1) {
                if(patientProblemInfoEntity.getCreateMillisecond()>t1.getCreateMillisecond()){
                    return -1;
                }else{
                    return 1;
                }
            }
        });

        if(mData.size()>10){
            List<PostLastTenMessage.ListBean> temp;
            temp = mData;
            mData = new ArrayList<>();
            for(int i = 0;i<10;i++){
                mData.add(temp.get(i));
            }
        }


    }

    public void setListViewEmpty(){
        mAdapter = new HealthInquiryProblemListAdapter(this,null);
        listview.setAdapter(mAdapter);
        listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadLastTenMessage();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    public void toFurtherAsk(int position,PostLastTenMessage.ListBean entity){
        Intent intent = new Intent(this, HealthInquiryFurtherAskActivity.class);
        intent.putExtra(IParamsName.PARAMS_COMMON_DATA,entity);
        startActivityForResult(intent,ICommonActivityRequestCode.RELOAD_DATA);
    }

    public void setListViewData(final List<PostLastTenMessage.ListBean> dataList){
        mAdapter = new HealthInquiryProblemListAdapter(this,dataList);
        listview.setAdapter(mAdapter);
        listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadLastTenMessage();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long position) {
                if(mAdapter.isReplied((int) position)){
                    //TODO 跳转至追问界面
                    toFurtherAsk((int) position,dataList.get((int) position));
                }else{
                    //TODO 提示未回复的无法进行追问
                    CommonDialogHint.getInstance().showHint(HealthInquiryCreateProblemActivity.this,getString(R.string.health_inquiry_history_append_forbid));
                }
            }
        });
    }

    //TODO 在收到通知消息时可能需要重新获取列表数据，并刷新视图---暂时不确定后台如何实现这个功能


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ICommonActivityRequestCode.BACK_TO_MAIN: {
                if (resultCode == ICommonActivityResultCode.BACK_TO_MAIN) {
                    finish();
                } else if (resultCode == ICommonActivityResultCode.RELOAD_DATA) {
                    loadLastTenMessage();
                }
                break;
            }
            case ICommonActivityRequestCode.RELOAD_DATA: {
                if (resultCode == ICommonActivityResultCode.RELOAD_DATA) {
                    loadLastTenMessage();
                }
                break;
            }
        }
    }
}
