package com.quanjiakan.activity.common.main.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.BaseFragment;
import com.quanjiakan.activity.common.main.MainActivity;
import com.quanjiakan.broadcast.entity.CommonNattyData;
import com.quanjiakan.db.entity.BindWatchInfoEntity;
import com.quanjiakan.db.manager.DaoManager;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.IResponseResultCode;
import com.quanjiakan.net.retrofit.result_entity.GetWatchListEntity;
import com.quanjiakan.net_presenter.BindDeviceListPresenter;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.watch.R;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/17.
 */

public class MainMapFragment extends BaseFragment implements AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener,
        AMap.InfoWindowAdapter,
        GeocodeSearch.OnGeocodeSearchListener {

    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.device_container)
    ListView deviceContainer;
    Unbinder unbinder;

    //***************************************************
    //TODO 高德地图
    private AMap aMap;
    private GeocodeSearch geocoderSearch;
    //TODO 定位
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    //TODO 自己定位的位置信息
    private double selfLatitude = -1000;
    private double selfLongitude = -1000;
    private String selfAdress = "";
    //TODO 地图缩放等级
    private final int SOLID_ZOOM = 18;
    //TODO 定位自己的类型
    private final int LOCATION_TYPE_GET_POSITION = 1;//仅仅获取定位，不进行展示
    private final int LOCATION_TYPE_SHOW_POSITION = 2;//展示位置，但不移动焦点
    private final int LOCATION_TYPE_SHOW_AND_MOVE_POSITION = 3;//展示位置，并移动焦点
    private final int LOCATION_TYPE_MOVE_POSITION_WITHOUT_ICON = 4;//展示位置，并移动焦点,但不显示图标
    //***************************************************
    private BindDeviceListPresenter presenter;

    private List<BindWatchInfoEntity> watchInfoEntityList = new ArrayList<>();

    /**
     * ************************************************************************************************************************
     * 生命周期方法
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_map, container, false);//TODO 实例化整个布局
        unbinder = ButterKnife.bind(this, view);

        //TODO 设置默认值
        setDefaultValue();
        //TODO 初始化地图
        initMap(view, savedInstanceState);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        register();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        mapView.onResume();

        locateSelf(LOCATION_TYPE_SHOW_AND_MOVE_POSITION);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregister();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
        unbinder.unbind();
    }

    /**
     * *****************************************************************************************************************************
     * EventBus 广播事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonNattyData(CommonNattyData result) {
        //TODO 控制无效的广播
        if (result == null) {
            return;
        }
        switch (result.getType()) {

        }
    }

    /**
     * ************************************************************************************************************************
     */

    public void setDefaultValue() {
        /**
         * 设置组件的默认值
         */
        initTitle();
        getBindWatchListFromNet();
    }

    public void initTitle() {

    }

    public void initMap(View view, Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mapView.getMap();
        }

        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);
        //TODO 设置地图面板
        locateSelf(LOCATION_TYPE_SHOW_AND_MOVE_POSITION);

        geocoderSearch = new GeocodeSearch(this.getActivity());
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    /**
     * ************************************************************************************************************************
     * 获取自己的定位，根据传入的参数判断是否需要显示出自己的定位点
     * <p>
     * 这里需要判断一点是，如果将地图移动到展示手表定位的地址上时，需要
     */

    public void locateSelf(final int locateType) {
        stopLocateSelf();
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.main_map_self_location_blue_point));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细

        aMap.setMyLocationStyle(myLocationStyle);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        aMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                mListener = onLocationChangedListener;
                if (mlocationClient == null) {
                    mlocationClient = new AMapLocationClient(getActivity());
                    mLocationOption = new AMapLocationClientOption();
                    //设置定位监听
                    mlocationClient.setLocationListener(new AMapLocationListener() {
                        @Override
                        public void onLocationChanged(AMapLocation aMapLocation) {
                            if (mListener != null && aMapLocation != null) {
                                if (aMapLocation != null
                                        && aMapLocation.getErrorCode() == 0) {

                                    //TODO 不是获取定位，不是移动镜头且不展示图标
                                    if (locateType != LOCATION_TYPE_GET_POSITION &&
                                            locateType != LOCATION_TYPE_MOVE_POSITION_WITHOUT_ICON) {
                                        mListener.onLocationChanged(aMapLocation);//TODO 将会把代表自己位置的蓝点图标展示出来
                                    }
                                    selfLatitude = aMapLocation.getLatitude();
                                    selfLongitude = aMapLocation.getLongitude();
                                    selfAdress = aMapLocation.getAddress();
                                    stopLocateSelf();

                                    //TODO 展示定位图标并移动镜头/移动镜头，但不展示图标
                                    if (locateType == LOCATION_TYPE_SHOW_AND_MOVE_POSITION ||
                                            locateType == LOCATION_TYPE_MOVE_POSITION_WITHOUT_ICON
                                            ) {
                                        moveCamera(new LatLng(selfLatitude, selfLongitude));
                                    }
                                } else {
                                }
                            } else {
                            }
                        }
                    });
                    mLocationOption.setInterval(30000);
                    //设置为高精度定位模式
                    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                    //设置定位参数
                    mlocationClient.setLocationOption(mLocationOption);
                    mlocationClient.startLocation();
                }
            }

            @Override
            public void deactivate() {
                mListener = null;
                if (mlocationClient != null) {
                    mlocationClient.stopLocation();
                    mlocationClient.onDestroy();
                }
                mlocationClient = null;
            }
        });// 设置定位监听
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setCompassEnabled(false);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);
    }

    //终止定位过程
    public void stopLocateSelf() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    protected void moveCamera(LatLng point) {
        if (point == null) {
            return;
        }
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, SOLID_ZOOM));
    }

    /**
     * ************************************************************************************************************************
     * Marker的InfoWindow
     */
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    /**
     * ************************************************************************************************************************
     * Marker点击
     */

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    /**
     * ************************************************************************************************************************
     * 获取查询地理编码，反地理编码结果
     */

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * ************************************************************************************************************************
     * 获取绑定的手表列表
     */
    /**
     * 网络
     */
    public void getBindWatchListFromNet() {
        presenter = new BindDeviceListPresenter();
        presenter.getBindDeviceList(this);
    }

    public void getBindWatchListFromLocalDataBase() {

    }

    /**
     * ************************************************************************************************************************
     */

    @Override
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.DEVICE_WATCH_LIST: {
                HashMap<String, String> params = new HashMap<>();
                params.put("memberId", BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put("platform", IHttpUrlConstants.PLATFORM_ANDROID);
                params.put("token", BaseApplication.getInstances().getLoginInfo().getToken());
                return params;
            }
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.DEVICE_WATCH_LIST: {
                ((MainActivity) getActivity()).getDialog(getActivity());
                break;
            }
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.DEVICE_WATCH_LIST: {
                ((MainActivity) getActivity()).dismissDialog();
                break;
            }
        }
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.DEVICE_WATCH_LIST: {
                if (result != null && result instanceof GetWatchListEntity) {
                    GetWatchListEntity entity = (GetWatchListEntity) result;
                    if (IResponseResultCode.RESPONSE_SUCCESS.equals(entity.getCode())) {
                        saveWatchListDataAndShowList(entity);
                    } else {
                        if (entity.getMessage() != null && entity.getMessage().length() > 0) {
                            CommonDialogHint.getInstance().showHint(getActivity(), entity.getMessage());
                        } else {
                            CommonDialogHint.getInstance().showHint(getActivity(),
                                    getActivity().getString(R.string.error_common_net_request_fail));
                        }
                    }
                } else {
                    CommonDialogHint.getInstance().showHint(getActivity(),
                            getActivity().getString(R.string.error_common_net_request_fail));
                }
                break;
            }
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.DEVICE_WATCH_LIST: {
                if (errorMsg != null) {
                    CommonDialogHint.getInstance().showHint(getActivity(), errorMsg.toString());
                }
                break;
            }
            default: {
                if (errorMsg != null) {
                    CommonDialogHint.getInstance().showHint(getActivity(), errorMsg.toString());
                }
                break;
            }
        }
    }

    /**
     * ************************************************************************************************************************
     */
    //TODO 将数据持久化到本地数据库中，并展示数据到ListView上
    public void saveWatchListDataAndShowList(GetWatchListEntity entity) {
        //TODO 仅在数据有效，且数据数量不为0的情况下保存
        if (entity != null && entity.getList().size() > 0) {
            //TODO
            try {
                //TODO 转换成数据库实体类---list中包含的为绑定过的数据
                List<BindWatchInfoEntity> list = new ArrayList<>();
                for (GetWatchListEntity.ListBean temp : entity.getList()) {
                    list.add(transformToDBEntity(temp));
                }
                //TODO 更新ListView中的数据
                refreshNetData(list);

                //TODO 将更新的数据保存进去
                /*
                先删除原有的部分
                 */
                if(!watchInfoEntityList.isEmpty()){
                    DaoManager.getInstances(getActivity()).getDaoSession().
                            getBindWatchInfoEntityDao().deleteInTx(watchInfoEntityList);
                }
                /*
                将新数据保存进去
                 */
                DaoManager.getInstances(getActivity()).getDaoSession().
                        getBindWatchInfoEntityDao().insertOrReplaceInTx(list);


                //TODO 更新原有的数据
                if(watchInfoEntityList.size()>0){
                    watchInfoEntityList.clear();
                }else{
                    watchInfoEntityList = new ArrayList<>();
                }
                //TODO 存入最新的数据
                watchInfoEntityList.addAll(list);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前用户绑定的手表列表(从本地数据库中获取)
     * @return
     */
    public List<BindWatchInfoEntity> getMyBindWatchListFromDB(){
        List<BindWatchInfoEntity> tempList = DaoManager.getInstances(getActivity()).getDaoSession().getBindWatchInfoEntityDao().loadAll();
        for(int i = tempList.size()-1;i>-1;i--){
            if(!BaseApplication.getInstances().getLoginInfo().getUserId().equals(tempList.get(i).getBindUserID())){
                tempList.remove(i);
            }
        }
        return tempList;
    }

    /**
     * 更新ListView中的数据
     *
     * @param list
     * @return
     */
    public List<BindWatchInfoEntity> refreshNetData(List<BindWatchInfoEntity> list){
        List<BindWatchInfoEntity> tempList = new ArrayList<>();
        List<BindWatchInfoEntity> saveList = new ArrayList<>();
        tempList.addAll(watchInfoEntityList);
        for(BindWatchInfoEntity netData:list){
            int size = tempList.size();
            for(int i=size-1;i>-1;i--){
                BindWatchInfoEntity localData = tempList.get(i);
                //TODO 同一个设备，则更新网络数据
                if(localData.getImei().equals(netData.getImei())){
                    netData.setAlarmTime(localData.getAlarmTime());
                }
            }
        }
        return saveList;
    }

    //TODO 将接口对象转化成数据库持久化对象
    public BindWatchInfoEntity transformToDBEntity(GetWatchListEntity.ListBean entity) throws JSONException, UnsupportedEncodingException {
        BindWatchInfoEntity watchInfoEntity = new BindWatchInfoEntity();

        watchInfoEntity.setAlarmTime("0");
        watchInfoEntity.setHeadImage(entity.getImage());
        watchInfoEntity.setImei(entity.getImei());
        watchInfoEntity.setLocation(entity.getLocation());
        watchInfoEntity.setLocationTime(entity.getLocationTime());

        watchInfoEntity.setOnline(entity.getOnline() + "");
        watchInfoEntity.setType(entity.getType() + "");
        watchInfoEntity.setUnreadMessageNumber(0);
        watchInfoEntity.setRelationlist(entity.getRelationListToJSONArray().toString());
        watchInfoEntity.setName(entity.getFormatName());
        return watchInfoEntity;
    }

    public void loadDataIntoListView(){

    }

}
