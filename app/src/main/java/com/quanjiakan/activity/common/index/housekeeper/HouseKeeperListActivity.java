package com.quanjiakan.activity.common.index.housekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.ICommonActivityRequestCode;
import com.quanjiakan.activity.base.ICommonActivityResultCode;
import com.quanjiakan.adapter.HouseKeeperListAdapter;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperListEntity;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperTypeListEntity;
import com.quanjiakan.net_presenter.HouseKeeperListPresenter;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.dialog.ChangeAddressDetailDialog;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.watch.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/25.
 */

public class HouseKeeperListActivity extends BaseActivity implements GeocodeSearch.OnGeocodeSearchListener  {

    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    //TODO 条件栏----地址
    @BindView(R.id.select_condition_position)
    TextView selectConditionPosition;
    @BindView(R.id.arrow_position)
    ImageView arrowPosition;
    @BindView(R.id.condition_line_position)
    LinearLayout conditionLinePosition;
    //TODO 条件栏----服务类型
    @BindView(R.id.select_condition_type)
    TextView selectConditionType;
    @BindView(R.id.arrow_type)
    ImageView arrowType;
    @BindView(R.id.condition_line_type)
    LinearLayout conditionLineType;

    //TODO 家政提示
    @BindView(R.id.listview)
    PullToRefreshListView listView;
    //TODO 无数据的提示
    @BindView(R.id.nonedata)
    ImageView nonedata;
    @BindView(R.id.nonedatahint)
    TextView nonedatahint;
    @BindView(R.id.nodata_line)
    LinearLayout nodataLine;

    //TODO 无数据的提示
    @BindView(R.id.select_condition_list_position)
    TextView selectConditionListPosition;
    @BindView(R.id.select_condition_list_type)
    ListView selectConditionListType;
    @BindView(R.id.select_condition_line)
    LinearLayout selectConditionLine;

    //TODO 定位以及获取位置地址信息的组件
    private AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mlocationClient = null;
    private GeocodeSearch geocoderSearch = null;

    //TODO 默认每次请求获取10条数据
    private HouseKeeperListPresenter presenter;

    private String mProvince;
    private String mCity;
    private String mSection;

    //TODO 每次加载获取的是第几页的数据
    private int currentPage = 1;
    private List<GetHouseKeeperListEntity.ListBean> mList = new ArrayList<>();
    private HouseKeeperListAdapter mAdapter;
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(HouseKeeperListActivity.this, HouseKeeperDetailInfoActivity.class);
            intent.putExtra(IParamsName.PARAMS_HOUSE_KEEPER_DETAIL_ID, mList.get((int)arg3).getId());
            intent.putExtra(IParamsName.PARAMS_HOUSE_KEEPER_DETAIL_INFO,mList.get((int)arg3));
            startActivityForResult(intent, ICommonActivityRequestCode.BACK_TO_MAIN);
//            CommonDialogHint.getInstance().showHint(HouseKeeperListActivity.this,""+arg3);
        }
    };

    //TODO 服务类型
    private boolean isShowTypeList = false;
    private List<GetHouseKeeperTypeListEntity.ListBean> typeListData = null;
    private String currentServiceType = null;

    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_housekeeperlist);
        ButterKnife.bind(this);

        initTitleBar();
        initConditionDataView();
        initPositionFind();

//        //TODO 加载我地址的数据列表
    }

    /**
     * *************************************************************************************
     */

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRestart() {
        super.onRestart();
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


    @Override
    public Object getParamter(int type) {
        switch (type){
            case IPresenterBusinessCode.HOUSE_KEEPER_TYPE_LIST: {
                HashMap<String,String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN,BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_MEMBERID,BaseApplication.getInstances().getLoginInfo().getUserId());
                return params;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_LOCATE: {
                return null;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_LIST: {
                //TODO 不带有定位地址的请求
                HashMap<String,String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_PAGE, currentPage+"");
                if(currentServiceType!=null){
                    params.put(IParamsName.PARAMS_HOUSE_KEEPER_TYPE, currentServiceType);
                }
                return params;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION:{
                //TODO 带有定位地址的请求
                HashMap<String,String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_PAGE, currentPage+"");

                //TODO 由于后台匹配的地址的问题，省份中不能够含有 省字，市的名字中，不能含有市字
                if(mProvince != null){//(mProvince != null ? mProvince.replace("省", "")
                    params.put(IParamsName.PARAMS_HOUSE_KEEPER_PROVINCE, mProvince.replace("省", ""));
                }
                if(mCity != null){
                    params.put(IParamsName.PARAMS_HOUSE_KEEPER_CITY, mCity.replace("市", ""));
                }
                params.put(IParamsName.PARAMS_HOUSE_KEEPER_DIST, mSection);

                //TODO 插入地址信息----使用Encode的方式，似乎反而无法获得数据---【推测Retrofit 自动进行中文字符的URLEncode】
//                try {
//                    params.put(IParamsName.PARAMS_HOUSE_KEEPER_PROVINCE, URLUtil.urlEncode(mProvince));
//                    params.put(IParamsName.PARAMS_HOUSE_KEEPER_CITY, URLUtil.urlEncode(mCity));
//                    params.put(IParamsName.PARAMS_HOUSE_KEEPER_DIST, URLUtil.urlEncode(mSection));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                if(!params.containsKey(IParamsName.PARAMS_HOUSE_KEEPER_PROVINCE)){
//                    params.put(IParamsName.PARAMS_HOUSE_KEEPER_PROVINCE, mProvince);
//                }
//                if(!params.containsKey(IParamsName.PARAMS_HOUSE_KEEPER_CITY)){
//                    params.put(IParamsName.PARAMS_HOUSE_KEEPER_CITY, mCity);
//                }
//                if(!params.containsKey(IParamsName.PARAMS_HOUSE_KEEPER_DIST)){
//                    params.put(IParamsName.PARAMS_HOUSE_KEEPER_DIST, mSection);
//                }

                if(currentServiceType!=null){
                    params.put(IParamsName.PARAMS_HOUSE_KEEPER_TYPE, currentServiceType);
                }
                return params;
            }
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type){
            case IPresenterBusinessCode.HOUSE_KEEPER_LOCATE: {
                getDialog(this,getString(R.string.hint_locating));
                break;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_LIST: {
                getDialog(this,getString(R.string.hint_get_house_keeper_data));
                break;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION: {
                getDialog(this,getString(R.string.hint_get_house_keeper_data));
                break;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_TYPE_LIST: {
                getDialog(this,getString(R.string.hint_get_house_keeper_type_data));
                break;
            }
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type){
            case IPresenterBusinessCode.HOUSE_KEEPER_LOCATE: {
                break;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_LIST: {
                break;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION: {
                break;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_TYPE_LIST: {
                break;
            }
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type){
            case IPresenterBusinessCode.HOUSE_KEEPER_LOCATE: {
                break;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION: {
                setDataIntoListView(result);
                break;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_LIST: {
                setDataIntoListView(result);
                break;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_TYPE_LIST: {
                setDataIntoTypeList(result);
                break;
            }
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type){
            case IPresenterBusinessCode.HOUSE_KEEPER_LOCATE: {
                break;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_LIST: {
                break;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_LIST_WITH_LOCATION: {
                break;
            }
            case IPresenterBusinessCode.HOUSE_KEEPER_TYPE_LIST: {
                break;
            }
        }
        if(errorMsg!=null && errorMsg.toString().length()>0){
            CommonDialogHint.getInstance().showHint(this,errorMsg.toString());
        }else{
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_common_net_request_fail));
        }
    }

    /**
     * *************************************************************************************
     */

    public void initTitleBar(){
        tvTitle.setText(R.string.housekeeper_list_title);

        ibtnBack.setVisibility(View.VISIBLE);

        presenter = new HouseKeeperListPresenter();
    }

    public void initConditionDataView(){
        selectConditionLine.setVisibility(View.GONE);
    }

    public void showNodataHint(boolean isShow){
        if(isShow){
            nodataLine.setVisibility(View.VISIBLE);
        }else{
            nodataLine.setVisibility(View.GONE);
        }
    }

    public void initPositionFind(){
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                //TODO 不论是否真正获取到了定位信息

                //TODO 避免高德出现多次回调导致的重复获取网络数据
                if(mlocationClient==null){
                    return;
                }

                if (aMapLocation == null) {
                    //****** 查询无定位的
                    //TODO 关闭定位对话框
                    dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LOCATE);

                    currentPage = 1;
                    mList.clear();
                    //TODO
                    loadHouseKeeperListDataWithoutPosition();

                    //TODO -------  终止单次的定位回调
                    mlocationClient.stopLocation();
                    mlocationClient = null;
                    return;
                }

                //TODO 检查到了当前定位
                //TODO -------  终止单次的定位回调
                mlocationClient.stopLocation();
                mlocationClient = null;

                LatLonPoint latLonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 100,
                        GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                geocoderSearch.getFromLocationAsyn(query);
            }

        });

        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(2000);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();

        //TODO 开启定位对话框
        showMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LOCATE);
    }

    public void loadHouseKeeperListDataWithoutPosition(){
        presenter.getHouseKeeperListWithoutPosition(this);
    }

    public void loadHouseKeeperListDataWithPositioin(){
        presenter.getHouseKeeperListWithPosition(this);
    }

    public void setDataIntoListView(Object object){
        if(object!=null && object instanceof GetHouseKeeperListEntity){
            GetHouseKeeperListEntity result = (GetHouseKeeperListEntity) object;
            if(currentPage==1){
                //TODO 判断是否应该显示 无数据
                if(result==null || result.getList()==null || result.getList().size()<1){
                    if(mList!=null && mList.size()>0){//TODO 有数据时则不进行操作（不论是否出现访问失败，还是没拿到数据）

                    }else{//TODO 在尚未获取到过数据时显示无数据提示
                        showNodataHint(true);
                    }
                    return;
                }else{
                    showNodataHint(false);

                    //********************
                    mList.clear();
                    mList.addAll(result.getList());
                    if(result.getList().size()>=10){
                        listView.setMode(PullToRefreshBase.Mode.BOTH);
                    }else{
                        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                    mAdapter = new HouseKeeperListAdapter(this, mList);
                    listView.setAdapter(mAdapter);
                    listView.setOnItemClickListener(onItemClickListener);
                    listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                        @Override
                        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                            completeRefresh();
                            if (mProvince != null) {
                                listView.setMode(PullToRefreshBase.Mode.BOTH);
                                currentPage = 1;
                                mList.clear();
                                loadHouseKeeperListDataWithPositioin();
                            } else {
                                loadHouseKeeperListDataWithoutPosition();
                            }
                        }
                        @Override
                        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                            completeRefresh();
                            currentPage += 1;
                            if (mProvince != null) {
                                listView.setMode(PullToRefreshBase.Mode.BOTH);
                                loadHouseKeeperListDataWithPositioin();
                            } else {
                                loadHouseKeeperListDataWithoutPosition();
                            }
                        }
                    });
                }
            }else{
                //TODO 判断是否应该显示 无数据
                if((result==null || result.getList()==null || result.getList().size()<1) && mList.size()<1){
                    showNodataHint(true);
                    return;
                }else{
                    showNodataHint(false);
                    //TODO 当没有更多的数据加载时，关闭上拉加载更多
                    if(result.getList().size()<1){
                        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                }

                mList.addAll(result.getList());
                mAdapter.notifyDataSetChanged();
                //TODO 关闭刷新
                completeRefresh();
            }
        }else{

            //TODO 判断是否应该显示 无数据
            if(mList.size()<1){
                showNodataHint(true);

                mList.clear();
                listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                mAdapter = new HouseKeeperListAdapter(this, mList);
                listView.setAdapter(mAdapter);
                listView.setOnItemClickListener(onItemClickListener);
                listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                        completeRefresh();
                        listView.setMode(PullToRefreshBase.Mode.BOTH);
                        currentPage = 1;
                        mList.clear();
                        if (mProvince != null) {
                            loadHouseKeeperListDataWithPositioin();
                        } else {
                            loadHouseKeeperListDataWithoutPosition();
                        }
                    }
                    @Override
                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                        completeRefresh();
                    }
                });

            }else{
                showNodataHint(false);
            }
        }
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

    @OnClick({R.id.ibtn_back, R.id.tv_title, R.id.condition_line_position, R.id.condition_line_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back: {
                finish();
                break;
            }
            case R.id.tv_title: {

                break;
            }
            case R.id.condition_line_position: {
                showAddressDialog();
                break;
            }
            case R.id.condition_line_type: {
                showTypeList();
                break;
            }
        }
    }

    /**
     ********************************************************************************************
     */

    //TODO
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

        //TODO 关闭定位对话框
        dismissMyDialog(IPresenterBusinessCode.HOUSE_KEEPER_LOCATE);

        if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null) {
            /**
             *
             */
            mProvince = regeocodeResult.getRegeocodeAddress().getProvince();
            mCity = regeocodeResult.getRegeocodeAddress().getCity();
            mSection = regeocodeResult.getRegeocodeAddress().getDistrict();

            LogUtil.e(" Position:"+mProvince+"\n"+mCity+"\n"+mSection+"\n");

            //TODO 依次检查，如果存在则显示在地址栏上面
            if (regeocodeResult.getRegeocodeAddress().getDistrict() != null && regeocodeResult.getRegeocodeAddress().getDistrict().length() > 0) {
                selectConditionPosition.setText(regeocodeResult.getRegeocodeAddress().getDistrict());
            } else if (regeocodeResult.getRegeocodeAddress().getCity() != null && regeocodeResult.getRegeocodeAddress().getCity().length() > 0) {
                selectConditionPosition.setText(regeocodeResult.getRegeocodeAddress().getCity());
            } else if (regeocodeResult.getRegeocodeAddress().getProvince() != null && regeocodeResult.getRegeocodeAddress().getProvince().length() > 0) {
                selectConditionPosition.setText(regeocodeResult.getRegeocodeAddress().getProvince().replace("省", ""));
            }
            /**
             * 是否应该再次执行一遍获取数据的请求--------还是说等定位获取到了之后再请求数据
             */
            currentPage = 1;
            mList.clear();
            loadHouseKeeperListDataWithPositioin();
        } else {
            currentPage = 1;
            mList.clear();
            loadHouseKeeperListDataWithoutPosition();
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    public void showAddressDialog() {
        ChangeAddressDetailDialog dialog = new ChangeAddressDetailDialog(HouseKeeperListActivity.this);
        dialog.setAddresskListener(new ChangeAddressDetailDialog.OnAddressCListener() {
            @Override
            public void onClick(String province, String city, String section) {
                if (mProvince != null && mProvince.equals(province)
                        && mCity != null && mCity.equals(city)
                        && mSection != null && mSection.equals(section)) {

                } else {

                }
                mProvince = province;
                mCity = city;
                mSection = section;

                if (section != null && section.length() > 0) {
                    selectConditionPosition.setText(section);
                } else if (city != null && city.length() > 0) {
                    selectConditionPosition.setText(city);
                } else if (province != null && province.length() > 0) {
                    selectConditionPosition.setText(province);
                }
                getDataAfterPositionChange();
            }
        });
        dialog.show();
    }

    //TODO 在改变
    public void getDataAfterPositionChange(){
        currentPage = 1;
        mList.clear();
        loadHouseKeeperListDataWithPositioin();
    }

    //TODO 展示服务类型列表
    public void showTypeList(){
        if(isShowTypeList){//TODO 服务类型已经展示出来了
            isShowTypeList = false;
            selectConditionLine.setVisibility(View.GONE);
        }else{//TODO 服务类型尚未展示
            isShowTypeList = true;
            selectConditionLine.setVisibility(View.VISIBLE);
            if(typeListData!=null && typeListData.size()>0){
                //TODO 直接展示数据
            }else{
                //TODO 访问接口获取数据后，展示
                getHouseKeeperTypeData();
            }
        }
    }

    public void getHouseKeeperTypeData(){
        presenter.getHouseKeeperTypeList(this);
    }

    public void setDataIntoTypeList(Object result){
        if(result!=null && result instanceof GetHouseKeeperTypeListEntity){
            GetHouseKeeperTypeListEntity data = (GetHouseKeeperTypeListEntity) result;
            if(data.getList()!=null && data.getList().size()>0){
                typeListData = data.getList();
                //TODO
                ArrayList<String> nameList = new ArrayList<String>();
                for (GetHouseKeeperTypeListEntity.ListBean temp : typeListData) {
                    nameList.add(temp.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_select_condition, nameList);
                selectConditionListType.setAdapter(adapter);
                selectConditionListType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        if (typeListData.get(position).equals(selectConditionType.getText().toString())) {
                            showTypeList();
                        } else {
                            currentServiceType = typeListData.get((int) l).getId()+"";
                            selectConditionType.setText(typeListData.get((int) l).getName());

                            currentPage = 1;
                            showTypeList();
                            filter();
                        }
                    }
                });
            }
        }
    }

    public void filter(){
        if(mProvince!=null){
            loadHouseKeeperListDataWithPositioin();
        }else{
            loadHouseKeeperListDataWithoutPosition();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ICommonActivityRequestCode.BACK_TO_MAIN:
                if(resultCode== ICommonActivityResultCode.BACK_TO_MAIN){
                    finish();
                }
                break;
        }
    }
}
