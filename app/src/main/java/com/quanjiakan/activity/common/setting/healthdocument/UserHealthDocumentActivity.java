package com.quanjiakan.activity.common.setting.healthdocument;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.adapter.UserHealthDocumentAdapter;
import com.quanjiakan.constants.ICommonActivityRequestCode;
import com.quanjiakan.constants.ICommonActivityResultCode;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.GetUserHealthDocumentEntity;
import com.quanjiakan.net.retrofit.result_entity.subentity.UserHealthDocumentEntity;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.net_presenter.UserHealthDocumentListPresenter;
import com.quanjiakan.util.common.SerializeToObjectUtil;
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
 * Created by Administrator on 2017/11/20.
 */

public class UserHealthDocumentActivity extends BaseActivity {

    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ibtn_menu)
    ImageButton ibtnMenu;
    @BindView(R.id.menu_text)
    TextView menuText;
    @BindView(R.id.dataList)
    PullToRefreshListView listView;
    @BindView(R.id.nonedata)
    ImageView nonedata;
    @BindView(R.id.nonedatahint)
    TextView nonedatahint;

    /**
     * *******************************************************************************************************************************
     */
    private Handler mHandler = new Handler();

    private UserHealthDocumentListPresenter presenter;
    private int pageIndex;

    private UserHealthDocumentAdapter mAdapter;
    private List<UserHealthDocumentEntity> mData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_health_document_for_user);
        ButterKnife.bind(this);

        initTitle();
        showNoDataHint(true);
        setView();
        loadDataUserData();
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
     * *******************************************************************************************************************************
     */

    public void initTitle(){
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setText(R.string.setting_user_health_document_title);

        ibtnMenu.setVisibility(View.GONE);

        menuText.setVisibility(View.VISIBLE);
        menuText.setText(R.string.setting_user_health_document_menu_add);

        presenter = new UserHealthDocumentListPresenter();
        pageIndex = 1;
    }


    public void showNoDataHint(boolean isShow){
        if(isShow){
            nonedata.setVisibility(View.VISIBLE);
            nonedata.setImageResource(R.drawable.message_group_ico_sel);

            nonedatahint.setVisibility(View.VISIBLE);
            nonedatahint.setText(R.string.hint_common_hint_no_data);
        }else{
            nonedata.setVisibility(View.GONE);
            nonedata.setImageResource(R.drawable.message_group_ico_sel);

            nonedatahint.setVisibility(View.GONE);
            nonedatahint.setText(R.string.hint_common_hint_no_data);
        }
    }

    public void setView(){
        //TODO
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 1;
                loadDataUserData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex++;
                loadDataUserData();
            }
        });
        mAdapter = new UserHealthDocumentAdapter(this,mData);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO 看是否进行处理（但是图片已经单独处理了点击事件）
            }
        });

    }

    public void completeRefresh(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(listView.isRefreshing()){
                    listView.onRefreshComplete();
                }
            }
        },1000);
    }

    public void loadDataUserData(){
        presenter.getUserHealthDocument(this);
    }

    /**
     * *******************************************************************************************************************************
     */

    @Override
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.HEALTH_DOCUMENT_USER:
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_MEMBERID, BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_PAGE, pageIndex+"");
                return params;
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.HEALTH_DOCUMENT_USER:
                getDialog(this, getString(R.string.hint_common_get_data));//正在获取数据...
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.HEALTH_DOCUMENT_USER:
                break;
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.HEALTH_DOCUMENT_USER:
                setResult(result);
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.HEALTH_DOCUMENT_USER:
                listView.onRefreshComplete();//TODO 终止刷新动作
                break;
        }
        if (errorMsg != null && errorMsg.toString().length() > 0) {
            CommonDialogHint.getInstance().showHint(this, errorMsg.toString());
        } else {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_net_request_fail));
        }
    }

    /**
     * *******************************************************************************************************************************
     */

    public void setResult(Object result){
        listView.onRefreshComplete();//TODO 终止刷新动作
        if(result!=null && result instanceof String){
            GetUserHealthDocumentEntity entity = (GetUserHealthDocumentEntity) SerializeToObjectUtil.getInstances().
                    jsonToObject(result.toString(),new TypeToken<GetUserHealthDocumentEntity>(){}.getType());
            //TODO 将数据加载进Adapter并刷新数据
            if(pageIndex==1){
                mData.clear();
                mData.addAll(entity.getList());
                if(entity.getList().size()<10){
                    listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }else{
                    listView.setMode(PullToRefreshBase.Mode.BOTH);
                }
                resortData();
                mAdapter.setData(mData);
                mAdapter.notifyDataSetChanged();
            }else{
                mData.addAll(entity.getList());
                if(entity.getList().size()<10){
                    listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }else{
                    listView.setMode(PullToRefreshBase.Mode.BOTH);
                }
                resortData();
                mAdapter.setData(mData);
                mAdapter.notifyDataSetChanged();
            }
        }
        //TODO 设置无数据提示
        if(mData.size()>0){
            showNoDataHint(false);
        }else{
            showNoDataHint(true);
        }
    }

    public void resortData(){
        Collections.sort(mData, new Comparator<UserHealthDocumentEntity>() {
            @Override
            public int compare(UserHealthDocumentEntity o1, UserHealthDocumentEntity o2) {
                long first = Long.parseLong(o1.getCreatetime().replace("-","").replace(":","").replace(".","").replace(" ",""));
                long second = Long.parseLong(o2.getCreatetime().replace("-","").replace(":","").replace(".","").replace(" ",""));
                if(first>=second){
                    return -1;//TODO -1 代表排在前面
                }else{
                    return 1;//TODO 排在后面
                }
            }
        });
    }

    /**
     * *******************************************************************************************************************************
     */

    @OnClick({R.id.ibtn_back, R.id.ibtn_menu, R.id.menu_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.ibtn_menu:
                break;
            case R.id.menu_text:
                toAddUserHealthDocument();
                break;
        }
    }

    public void toAddUserHealthDocument(){
        Intent intent = new Intent(this,UserHealthDocumentAddActivity.class);
        startActivityForResult(intent, ICommonActivityRequestCode.RELOAD_DATA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ICommonActivityRequestCode.RELOAD_DATA:{
                if(resultCode==ICommonActivityResultCode.RELOAD_DATA){
                    pageIndex = 1;
                    loadDataUserData();
                }
                break;
            }
        }
    }
}
