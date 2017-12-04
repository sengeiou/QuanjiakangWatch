package com.quanjiakan.activity.common.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.BaseFragment;
import com.quanjiakan.constants.ICommonActivityRequestCode;
import com.quanjiakan.constants.ICommonActivityResultCode;
import com.quanjiakan.activity.common.setting.healthinquiry.HealthInquiryFurtherAskActivity;
import com.quanjiakan.adapter.HealthInquiryProblemListAdapter;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.PostLastTenMessage;
import com.quanjiakan.net_presenter.HealthInquiryCreateProblemPresenter;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/17.
 */

public class MessageListFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.listview)
    PullToRefreshListView listview;
    @BindView(R.id.nonedata)
    ImageView nonedata;
    @BindView(R.id.nonedatahint)
    TextView nonedatahint;
    //***************************************************
    private HealthInquiryCreateProblemPresenter presenter;

    private List<PostLastTenMessage.ListBean> mData = new ArrayList<>();

    private HealthInquiryProblemListAdapter mAdapter;

    /**
     * ************************************************************************************************************************
     * 生命周期方法
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_message, container, false);//TODO 实例化整个布局
        unbinder = ButterKnife.bind(this, view);
        //TODO 设置默认值
        setDefaultValue();
        initNoData();
        loadData();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * *****************************************************************************************************************************
     * EventBus 广播事件
     */

    /**
     * ************************************************************************************************************************
     */

    public void setDefaultValue() {
        /**
         * 设置组件的默认值
         */
        showNoData(true);

        presenter = new HealthInquiryCreateProblemPresenter();
    }

    public void initNoData(){
        nonedata.setImageResource(R.drawable.message_group_ico_sel);
        nonedatahint.setText(R.string.hint_common_hint_no_data);
    }

    public void showNoData(boolean isShow){
        if(isShow){
            nonedata.setVisibility(View.VISIBLE);
            nonedatahint.setVisibility(View.VISIBLE);
        }else{
            nonedata.setVisibility(View.GONE);
            nonedatahint.setVisibility(View.GONE);
        }
    }

    public void loadData(){
        presenter.getLastTenMessage(this);
    }

    public void setIntoListView(Object result){
        if(result!=null && result instanceof PostLastTenMessage){
            PostLastTenMessage entity = (PostLastTenMessage) result;
            if(entity.getList()!=null && entity.getList().size()>0){
                showNoData(false);//隐藏无数据提示
                mData.clear();
                mData.addAll(entity.getList());
                resortData();
                setListViewData(mData);
            }else{
                showNoData(true);
                //TODO 置空ListView对应的Adapter
                setListViewEmpty();
            }
        }else{
            showNoData(true);
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
        mAdapter = new HealthInquiryProblemListAdapter(getActivity(),null);
        listview.setAdapter(mAdapter);
        listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
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

    public void setListViewData(final List<PostLastTenMessage.ListBean> dataList){
        mAdapter = new HealthInquiryProblemListAdapter(getActivity(),dataList);
        listview.setAdapter(mAdapter);
        listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
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
                    CommonDialogHint.getInstance().showHint(getActivity(),getString(R.string.health_inquiry_history_append_forbid));
                }
            }
        });
    }

    public void toFurtherAsk(int position,PostLastTenMessage.ListBean entity){
        Intent intent = new Intent(getActivity(), HealthInquiryFurtherAskActivity.class);
        intent.putExtra(IParamsName.PARAMS_COMMON_DATA,entity);
        startActivityForResult(intent, ICommonActivityRequestCode.RELOAD_DATA);
    }

    /**
     * ************************************************************************************************************************
     */
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
                getDialog(getActivity(),getString(R.string.hint_health_inquiry_last_10_problem_hint));
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
            CommonDialogHint.getInstance().showHint(getActivity(),errorMsg.toString());
        }else{
            CommonDialogHint.getInstance().showHint(getActivity(),getString(R.string.error_common_net_request_fail));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ICommonActivityRequestCode.RELOAD_DATA: {
                if (resultCode == ICommonActivityResultCode.RELOAD_DATA) {
                    loadData();
                }
                break;
            }
        }
    }
}
