package com.quanjiakan.activity.common.index.devices.old.location;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.db.entity.BindWatchInfoEntity;
import com.quanjiakan.db.manager.DaoManager;
import com.quanjiakan.device.entity.CommonNattyData;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.umeng.analytics.MobclickAgent;
import com.wbj.ndk.natty.client.NattyProtocolFilter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/20.
 */

public class FreshLocationActivity extends BaseActivity implements GeocodeSearch.OnGeocodeSearchListener,
        AMap.InfoWindowAdapter, AMap.OnMarkerClickListener{

    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.location_icon)
    ImageView locationIcon;
    @BindView(R.id.fresh_location)
    RelativeLayout freshLocation;


    private AMap aMap;
    private GeocodeSearch geocoderSearch;

    private ArrayList<BitmapDescriptor> iconList = new ArrayList<>();

    private long idInDatabase;
    private String IMEI;

    private BindWatchInfoEntity entity;

    private final int SOLID_ZOOM = 16;

    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_watch_fresh_location);
        ButterKnife.bind(this);

        initTitle();

        idInDatabase = getIntent().getLongExtra(IParamsName.PARAMS_COMMON_ID_IN_DB, -1);
        IMEI = getIntent().getStringExtra(IParamsName.PARAMS_DEVICE_ID);
        if (idInDatabase == -1 || IMEI == null) {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_error_activity_parameters), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            return;
        }
        getDataInDataBase();

        initMap(savedInstanceState);

        commandGetLocation();
    }

    @Override
    public void onStart() {
        super.onStart();
        registerEventBus();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
        map.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterEventBus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.EMPTY:
                HashMap<String, String> params = new HashMap<>();
                return params;
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.EMPTY:
                getDialog(this, getString(R.string.hint_common_get_data));//正在获取数据...
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.EMPTY:
                break;
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.EMPTY:
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.EMPTY:
                break;
        }
        if (errorMsg != null && errorMsg.toString().length() > 0) {
            CommonDialogHint.getInstance().showHint(this, errorMsg.toString());
        } else {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_net_request_fail));
        }
    }

    @OnClick({R.id.ibtn_back, R.id.fresh_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.fresh_location:
                commandGetLocation();
                break;
        }
    }

    /**
     * *****************************************************************************************************************************
     */
    public void getDataInDataBase() {
        //TODO 获取对应的实体
        entity = DaoManager.getInstances(this).getDaoSession().getBindWatchInfoEntityDao().load(idInDatabase);
    }

    /**
     * *****************************************************************************************************************************
     */

    public void initTitle() {
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setText(R.string.device_fresh_location_title);
    }

    public void initMap(Bundle savedInstanceState) {
        map.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = map.getMap();
        }

        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setCompassEnabled(false);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.setInfoWindowAdapter(this);
        aMap.setOnMarkerClickListener(this);

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        if(entity!=null && entity.getLocation()!=null && entity.getLocation().contains(",")){
            //TODO 包含地址
            formatLocationAndShowMarker(entity.getLocation());
        }else{
            //不含地址 则定位自己的位置，并展示出来
            locateSelfPosition();
        }

    }

    public void locateSelfPosition() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.poi_marker_pressed));// 设置小蓝点的图标
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
                    mlocationClient = new AMapLocationClient(FreshLocationActivity.this);
                    mLocationOption = new AMapLocationClientOption();
                    //设置定位监听
                    mlocationClient.setLocationListener(new AMapLocationListener() {
                        @Override
                        public void onLocationChanged(AMapLocation aMapLocation) {

                            if (mListener != null && aMapLocation != null) {

                                if (aMapLocation != null
                                        && aMapLocation.getErrorCode() == 0) {
                                    mListener.onLocationChanged(aMapLocation);

                                    double selfLatitude = aMapLocation.getLatitude();
                                    double selfLongitude = aMapLocation.getLongitude();
                                    stopLocateSelf();
                                    moveCamera(new LatLng(selfLatitude, selfLongitude));
                                }
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

    public void stopLocateSelf() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    public void getAddressByLatlng(final LatLng latlng) {
        LatLonPoint latLonPoint = new LatLonPoint(latlng.latitude, latlng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 100,
                GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }


    public void formatLocationAndShowMarker(String location) {
        if (location != null && location.split(",").length == 2) {
            String[] locationString = location.split(",");
            try {
                LatLng locTemp = new LatLng(Double.parseDouble(locationString[1]), Double.parseDouble(locationString[0]));
                showMarker(locTemp);

                getAddressByLatlng(locTemp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showMarker(LatLng latlng) {
        if (aMap != null) {
            if (ICommonData.DEVICE_TYPE_CHILD.equals(entity.getType())) {//TODO 儿童
                if (iconList == null || iconList.size() < 1) {
                    iconList = new ArrayList<>();
                } else {
                    iconList.clear();
                }
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_1));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_2));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_3));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_4));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_5));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_6));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_7));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_8));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_9));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_10));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_11));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_12));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_13));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_14));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_15));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_16));
            } else {//TODO 老人
                if (iconList == null || iconList.size() < 1) {
                    iconList = new ArrayList<>();
                } else {
                    iconList.clear();
                }
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_1));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_2));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_3));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_4));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_5));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_6));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_7));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_8));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_9));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_10));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_11));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_12));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_13));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_14));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_15));
                iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_16));
            }
            aMap.clear();
            aMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title("1")
                    .period(1)
                    .icons(iconList)
                    .draggable(false));
            moveCamera(latlng);
        }
    }

    protected void moveCamera(LatLng point) {
        if (point == null) {
            return;
        }
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, SOLID_ZOOM));
    }

    public void commandGetLocation() {
        try {
            if (!BaseApplication.getInstances().isSDKConnected()) {
                CommonDialogHint.getInstance().showDisConnectDeviceServerDialog(this);
                return;
            }
            JSONObject jsonObject = new JSONObject();
            long devid = Long.parseLong(entity.getImei(), 16);
            jsonObject.put("IMEI", entity.getImei());
            jsonObject.put("Action", "Get");
            jsonObject.put("Category", "Location");
            BaseApplication.getInstances().getNattyClient().ntyDataRouteClient(devid, jsonObject.toString().getBytes(), jsonObject.toString().length());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            case NattyProtocolFilter.DISPLAY_UPDATE_DATA_RESULT: {
                //TODO 有返回，则是命令的发送结果
                String stringData = result.getData();
                try {
                    JSONObject boardcastResult = new JSONObject(stringData);
                    /*
                    {Results : {"Code":"10001","Message":"10001"}}
                     */
                    if (boardcastResult != null &&
                            boardcastResult.has("Results") &&
                            boardcastResult.getJSONObject("Results") != null) {//TODO
                        if (boardcastResult.getJSONObject("Results").has("Code") &&
                                ICommonData.HTTP_OK.equals(boardcastResult.getJSONObject("Results").getString("Code"))) {
                            //TODO 与服务器连接上了
                            //TODO 显示断联提示对话框
                        } else if (boardcastResult.getJSONObject("Results").has("Code") &&
                                ICommonData.HTTP_DEVICE_NOT_ONLINE.equals(boardcastResult.getJSONObject("Results").getString("Code"))) {//
                            CommonDialogHint.getInstance().showNotOnlineHintDialog(this);
                        } else {
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case NattyProtocolFilter.DISPLAY_UPDATE_DATA_ROUTE: {
                //TODO GetConfig   GetLocation
                String stringData = result.getData();
                try {
                    JSONObject boardcastResult = new JSONObject(stringData);
                    if (boardcastResult != null &&
                            boardcastResult.has("Results") &&
                            boardcastResult.getJSONObject("Results") != null) {//TODO
                        if (boardcastResult.getJSONObject("Results").has("Category")
                                && boardcastResult.getJSONObject("Results").has("IMEI")
                                && boardcastResult.getJSONObject("Results").getString("IMEI") != null
                                && boardcastResult.getJSONObject("Results").getString("IMEI").length() > 0
                                && boardcastResult.getJSONObject("Results").getString("IMEI").equals(entity.getImei())

                                && boardcastResult.getJSONObject("Results").has("Location")
                                && boardcastResult.getJSONObject("Results").getString("Location") != null
                                && boardcastResult.getJSONObject("Results").getString("Location").length() > 3
                                && boardcastResult.getJSONObject("Results").getString("Location").contains(",")) {//TODO 判断数据的类型
                            formatLocationAndShowMarker(boardcastResult.getJSONObject("Results").getString("Location"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * *****************************************************************************************************************************\
     * 不显示InfoWindow，仅展示Marker
     */

    @Override
    public View getInfoWindow(Marker marker) {
        marker.hideInfoWindow();
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        marker.hideInfoWindow();
        return null;
    }

    /**
     * *******************************
     */

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    /**
     * *******************************
     */

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                String district = result.getRegeocodeAddress().getDistrict();
                String province = result.getRegeocodeAddress().getProvince();
                String city = result.getRegeocodeAddress().getCity();
                String addressName = result.getRegeocodeAddress().getFormatAddress()
                        /*+ "附近"*/;
//                mAddress = addressName.replace(province,"").replace(district,"").replace(city,"");
                location.setText(getString(R.string.device_fresh_location_show_address) +
                        addressName.replace(province, ""));
//                LogUtil.e("经纬度解析成功!");
            } else {

            }
        } else {
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * *******************************
     */
    /**
     * *******************************
     */
}
