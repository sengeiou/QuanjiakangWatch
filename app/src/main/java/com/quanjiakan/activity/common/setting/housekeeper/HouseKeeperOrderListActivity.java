package com.quanjiakan.activity.common.setting.housekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.common.setting.orders.housekeeper.HouseKeeperOrderDetailActivity;
import com.quanjiakan.adapter.HouseKeeperOrderListAdapter;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.entity.HouseKeeperOrderDetailEntity;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperOrderListEntity;
import com.quanjiakan.net_presenter.HouseKeeperOrderListPresenter;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.common.SerializeToObjectUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/20.
 */

public class HouseKeeperOrderListActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.listview)
    PullToRefreshListView listView;
    @BindView(R.id.nonedata)
    ImageView nonedata;
    @BindView(R.id.nonedatahint)
    TextView nonedatahint;
    @BindView(R.id.nodata_line)
    LinearLayout nodataLine;

    private HouseKeeperOrderListPresenter presenter;
    private int pageIndex;
    private ArrayList<HouseKeeperOrderDetailEntity> mData = new ArrayList<>();

    private HouseKeeperOrderListAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_housekeeper_order_list);
        ButterKnife.bind(this);

        initTitle();
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


    public void initTitle(){
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setText(R.string.setting_house_keeper_order_list_title);

        presenter = new HouseKeeperOrderListPresenter();
    }

    public void initView(){
        showNoData(true);

        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 1;
                load(pageIndex);

                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.onRefreshComplete();
                    }
                },500);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex++;
                load(pageIndex);

                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.onRefreshComplete();
                    }
                },500);
            }
        });

        mAdapter = new HouseKeeperOrderListAdapter(this,mData);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        listView.onRefreshComplete();
        pageIndex = 1;//初始化
        load(pageIndex);
    }

    public void load(int pageIndex){
        presenter.getHouseKeeperOrderList(this);
    }

    public void showNoData(boolean isShow){
        if(isShow){
            nodataLine.setVisibility(View.VISIBLE);

            nonedatahint.setVisibility(View.VISIBLE);
            nonedatahint.setText(R.string.hint_common_hint_no_data);

            nonedata.setVisibility(View.VISIBLE);
            nonedata.setImageResource(R.drawable.message_hint_nodata);
        }else{
            nodataLine.setVisibility(View.GONE);

            nonedatahint.setVisibility(View.VISIBLE);
            nonedatahint.setText(R.string.hint_common_hint_no_data);

            nonedata.setVisibility(View.VISIBLE);
            nonedata.setImageResource(R.drawable.message_hint_nodata);
        }
    }

    public void setResult(Object result){
        if(result!=null && result instanceof String){
            try{

            }catch (Exception e){
                e.printStackTrace();
            }
            GetHouseKeeperOrderListEntity resultEntity = (GetHouseKeeperOrderListEntity) SerializeToObjectUtil.getInstances().
                    jsonToObject(result.toString(),new TypeToken<GetHouseKeeperOrderListEntity>(){}.getType());
            if(pageIndex==1){

                mData.clear();
                mData.addAll(resultEntity.getList());

                mAdapter.notifyDataSetChanged();
                if(mAdapter.getCount()>0){
                    showNoData(false);
                }else{
                    showNoData(true);
                }
            }else{
                mData.addAll(resultEntity.getList());

                mAdapter.notifyDataSetChanged();
                if(mAdapter.getCount()>0){
                    showNoData(false);
                }else{
                    showNoData(true);
                }
            }
        }else {
            if(pageIndex==1){
                showNoData(true);
            }else{
                showNoData(false);
            }
        }
    }


    @Override
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER_LIST:
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_MEMBERID,BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_COMMON_PAGE,pageIndex+"");
                return params;
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER_LIST:
                getDialog(this, getString(R.string.hint_common_get_data));//正在获取数据...
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER_LIST:
                break;
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER_LIST:
                setResult(result);
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.HOUSE_KEEPER_ORDER_LIST:
                break;
        }
        if (errorMsg != null && errorMsg.toString().length() > 0) {
            CommonDialogHint.getInstance().showHint(this, errorMsg.toString());
        } else {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_net_request_fail));
        }
    }

    @OnClick(R.id.ibtn_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,HouseKeeperOrderDetailActivity.class);
        intent.putExtra(IParamsName.PARAMS_COMMON_ENTITY,mAdapter.getmData().get((int)id));
        startActivity(intent);
    }
}
