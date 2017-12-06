package com.quanjiakan.activity.common.main.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.BaseFragment;
import com.quanjiakan.constants.ICommonActivityRequestCode;
import com.quanjiakan.constants.ICommonActivityResultCode;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.constants.ICommonSharePreferencesKey;
import com.quanjiakan.activity.common.index.bind.BindStepOneActivity;
import com.quanjiakan.activity.common.index.devices.WatchEntryActivity_old;
import com.quanjiakan.activity.common.main.MainActivity;
import com.quanjiakan.adapter.DeviceContainerAdapter;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.db.entity.BindWatchInfoEntity;
import com.quanjiakan.db.manager.DaoManager;
import com.quanjiakan.device.entity.CommonNattyData;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.IResponseResultCode;
import com.quanjiakan.net.retrofit.result_entity.GetWatchListEntity;
import com.quanjiakan.net_presenter.BindDeviceListPresenter;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.common.StringCheckUtil;
import com.quanjiakan.util.common.UnitExchangeUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.dialog.CommonDialog;
import com.quanjiakan.util.map.MapUtil;
import com.quanjiakan.util.map.NaviMapUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private DeviceContainerAdapter bindWatchListAdapter;
    private int currentClickPosition = 0;//TODO 记录当前点击的位置

    private List<LatLng> markerList = new ArrayList<>();
    //*    * InfoWindow 组件
    private TextView info_location;
    private TextView phone;
    private ImageView call_phone;
    private LinearLayout guide_line;//导航
    private LinearLayout phone_line;
    //******************
    private Dialog noBindDialog;

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
        locateSelf(LOCATION_TYPE_GET_POSITION);//TODO 仅获取自己的位置

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
    public View getInfoWindow(final Marker marker) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_watch_child_infowindow_new, null);
        info_location = (TextView) view.findViewById(R.id.info_location);
        info_location.setTag(null);
        info_location.setText("");
        phone = (TextView) view.findViewById(R.id.phone);
        phone_line = (LinearLayout) view.findViewById(R.id.phone_line);
        call_phone = (ImageView) view.findViewById(R.id.call_phone);
        guide_line = (LinearLayout) view.findViewById(R.id.guide_line);
        //TODO 临时对位置进行查询
        getAddressByLatlng(marker.getPosition());

        if(watchInfoEntityList.get(Integer.parseInt(marker.getTitle()))!=null &&
                watchInfoEntityList.get(Integer.parseInt(marker.getTitle())).getPhone()!=null &&
                StringCheckUtil.isPhoneNumber(watchInfoEntityList.get(Integer.parseInt(marker.getTitle())).getPhone())){
            phone.setText("电话:" + watchInfoEntityList.get(Integer.parseInt(marker.getTitle())).getPhone());
            phone_line.setVisibility(View.VISIBLE);
        }else{
            phone_line.setVisibility(View.GONE);
        }

        call_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (watchInfoEntityList.get(Integer.parseInt(marker.getTitle())).getPhone() != null &&
                        watchInfoEntityList.get(Integer.parseInt(marker.getTitle())).getPhone().length() > 0) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText().toString().substring(phone.getText().toString().indexOf(":") + 1)));
                    startActivity(intent);
                } else {
                    showSetNumDialog();
                }
            }
        });
        guide_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //导航
                openGudieDialog(marker.getPosition());
            }
        });
        return view;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_watch_child_infowindow_new, null);
        info_location = (TextView) view.findViewById(R.id.info_location);
        info_location.setTag(null);
        info_location.setText("");

        phone = (TextView) view.findViewById(R.id.phone);
        call_phone = (ImageView) view.findViewById(R.id.call_phone);
        guide_line = (LinearLayout) view.findViewById(R.id.guide_line);
        //TODO 临时对位置进行查询
        getAddressByLatlng(marker.getPosition());
        if(watchInfoEntityList.get(Integer.parseInt(marker.getTitle()))!=null &&
                watchInfoEntityList.get(Integer.parseInt(marker.getTitle())).getPhone()!=null &&
                StringCheckUtil.isPhoneNumber(watchInfoEntityList.get(Integer.parseInt(marker.getTitle())).getPhone())){
            phone.setText("电话:" + watchInfoEntityList.get(Integer.parseInt(marker.getTitle())).getPhone());
            phone_line.setVisibility(View.VISIBLE);
        }else{
            phone_line.setVisibility(View.GONE);
        }
        call_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (watchInfoEntityList.get(Integer.parseInt(marker.getTitle())).getPhone() != null &&
                        watchInfoEntityList.get(Integer.parseInt(marker.getTitle())).getPhone().length() > 0) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText().toString().substring(phone.getText().toString().indexOf(":") + 1)));
                    startActivity(intent);
                } else {
                    showSetNumDialog();
                }

            }
        });
        guide_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //导航
                openGudieDialog(marker.getPosition());
            }
        });
        return view;
    }

    private ExecutorService mExecutorService;

    public void getAddressByLatlng(final LatLng latlng) {
        final LatLonPoint point = new LatLonPoint(latlng.latitude, latlng.longitude);

        if (mExecutorService == null) {
            mExecutorService = Executors.newSingleThreadExecutor();
        }

        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    RegeocodeQuery query = new RegeocodeQuery(point, 200,
                            GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                    RegeocodeAddress result = geocoderSearch.getFromLocation(query);// 设置同步逆地理编码请求
                    if (result != null && result.getFormatAddress() != null) {
                        String addressName = result.getFormatAddress();
                        String province = result.getProvince();
                        Message message = new Message();
                        message.what = INFOR_ADDRESS;
                        Bundle bundle = new Bundle();
                        bundle.putString("province", province);
                        bundle.putString("addressName", addressName);
                        message.setData(bundle);
                        mHandler.sendMessage(message);

                    }
                } catch (AMapException e) {
                    Message msg = msgHandler.obtainMessage();
                    msg.arg1 = e.getErrorCode();
                    msgHandler.sendMessage(msg);
                }
            }
        });
    }
    public static final int FRESH_MARK_LIST = 16;
    public static final int INFOR_ADDRESS = 101;
    private Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FRESH_MARK_LIST: {
                    clearMapMarker();
                    showMarkerOnMap(markerList, currentClickPosition);
                    break;
                }
                case INFOR_ADDRESS:
                    Bundle data = msg.getData();
                    String province = data.getString("province");
                    String addressName = data.getString("addressName");
                    info_location.setText(getString(R.string.hint_addr) + addressName.replace(province, ""));
                    info_location.setTag(addressName.replace(province, ""));

                    break;
            }
        }
    };

    private void showSetNumDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.dialog_loading);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete_manager, null);

        TextView title = (TextView) view.findViewById(R.id.tv_dialog_title);
        title.setText(R.string.hint_common_sim_error);

        view.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // TODO: 2017/6/6 去设置电话号码
            }
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CommonDialogHint.getInstance().showHint(getActivity(), getString(R.string.cancel));

            }
        });
        TextView content = (TextView) view.findViewById(R.id.tv_content);
        content.setText(R.string.hint_common_sim_read_error);
        content.setGravity(Gravity.CENTER);


        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = UnitExchangeUtil.dip2px(getActivity(), 300);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(view, lp);
        dialog.show();
    }

    public void openGudieDialog(final LatLng latLng) {

        final Dialog selectNaviDialog = CommonDialog.getInstance().getCardDialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_select_navimap, null);

        //判断是否安装高德地图,处理点击事件
        if (NaviMapUtil.isAvilible(getActivity(), "com.autonavi.minimap")) {
            TextView gaodeNavi = (TextView) view.findViewById(R.id.tv_gaodenavi);
            View view1 = view.findViewById(R.id.line1);
            view1.setVisibility(View.VISIBLE);
            gaodeNavi.setVisibility(View.VISIBLE);
            gaodeNavi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selfLatitude != -1000 && selfLongitude != -1000) {
                        NaviMapUtil.GotoGaoDeNaviMap(getActivity(), "全家康用户端", selfLatitude + "", selfLongitude + "", selfAdress, latLng.latitude + "", latLng.longitude + "",
                                (info_location.getTag() != null ? info_location.getTag().toString() : ""), "1", "0", "2");
                    } else {
                        locateSelf(LOCATION_TYPE_GET_POSITION);
                    }
                    selectNaviDialog.dismiss();
                }

            });
        }

        //判断是否安装百度地图,并处理点击事件
        if (NaviMapUtil.isAvilible(getActivity(), "com.baidu.BaiduMap")) {
            TextView baiduNavi = (TextView) view.findViewById(R.id.tv_baidunavi);
            baiduNavi.setVisibility(View.VISIBLE);
            View view2 = view.findViewById(R.id.line2);
            view2.setVisibility(View.VISIBLE);
            //先将火星坐标转换为百度坐标

            baiduNavi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selfLatitude != -1000 && selfLongitude != -1000) {
                        Map<String, String> selfPoint = MapUtil.bd_encrypt(selfLatitude, selfLongitude);
                        Map<String, String> patitentPoint = MapUtil.bd_encrypt(latLng.latitude, latLng.longitude);
                        final String selfBdlat = selfPoint.get("mgLat");
                        final String selfBdlon = selfPoint.get("mgLon");
                        final String patitentBdLat = patitentPoint.get("mgLat");
                        final String patitentBdLon = patitentPoint.get("mgLon");
                        NaviMapUtil.GotoBaiDuNaviMap(getActivity(), selfBdlat + "," + selfBdlon, patitentBdLat + "," + patitentBdLon, "driving", null, null, null, null, null, "thirdapp.navi." + "巨硅科技" + R.string.app_name);
                    } else {

                    }
                    selectNaviDialog.dismiss();
                }
            });

        }

        //没有百度和高德地图
        if (!(NaviMapUtil.isAvilible(getActivity(), "com.baidu.BaiduMap") || NaviMapUtil.isAvilible(getActivity(), "com.autonavi.minimap"))) {
            TextView no_map = (TextView) view.findViewById(R.id.tv_no_map);
            View view3 = view.findViewById(R.id.line3);
            view3.setVisibility(View.VISIBLE);
            no_map.setVisibility(View.VISIBLE);
            no_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectNaviDialog.dismiss();
                }
            });
        }

        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectNaviDialog.dismiss();
            }
        });

        WindowManager.LayoutParams params = selectNaviDialog.getWindow().getAttributes();
        params.width = UnitExchangeUtil.dip2px(getActivity(), 300);
        params.height = params.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;

        selectNaviDialog.setContentView(view, params);
        selectNaviDialog.setCanceledOnTouchOutside(false);
        selectNaviDialog.show();

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
        //TODO 跳转至手表首页---根据对应的Marker
        int markIndex = Integer.parseInt(marker.getTitle());
        if(ICommonData.DEVICE_TYPE_OLD.equals(watchInfoEntityList.get(markIndex).getType())){
            Intent intent = new Intent(getActivity(), WatchEntryActivity_old.class);
            startActivityForResult(intent,ICommonActivityRequestCode.MAP_TO_DEVICE_OLD);
        }else if(ICommonData.DEVICE_TYPE_CHILD.equals(watchInfoEntityList.get(markIndex).getType())){
            Intent intent = new Intent(getActivity(), WatchEntryActivity_old.class);
            startActivityForResult(intent,ICommonActivityRequestCode.MAP_TO_DEVICE_CHILD);
        }
        return true;
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
     * 获取查询地理编码，反地理编码结果
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ICommonActivityRequestCode.MAP_TO_DEVICE_CHILD: {

                break;
            }
            case ICommonActivityRequestCode.MAP_TO_DEVICE_OLD: {

                break;
            }
            case ICommonActivityRequestCode.RELOAD_DATA: {
                if(resultCode== ICommonActivityResultCode.RELOAD_DATA){
                    presenter.getBindDeviceList(this);
                }
                break;
            }
        }
    }

    /**
     * ************************************************************************************************************************
     * 获取绑定的手表列表
     */
    /**
     * 网络
     */
    public void getBindWatchListFromNet() {
        if(presenter==null){
            presenter = new BindDeviceListPresenter();
        }
        presenter.getBindDeviceList(this);
    }

    /**
     * ************************************************************************************************************************
     */

    @Override
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.DEVICE_WATCH_LIST: {
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_DEVICE_MEMBER_ID, BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                return params;
            }
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.DEVICE_WATCH_LIST: {
                break;
            }
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.DEVICE_WATCH_LIST: {
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
                    }else if(IResponseResultCode.RESPONSE_EMPTY_DATA.equals(entity.getCode())){
                        locateSelf(LOCATION_TYPE_SHOW_AND_MOVE_POSITION);
                        //TODO 提示需要进行绑定的对话框
                        isShowNoBindDialog(true);
                    } else {
                        locateSelf(LOCATION_TYPE_SHOW_AND_MOVE_POSITION);
                        if (entity.getMessage() != null && entity.getMessage().length() > 0) {
                            CommonDialogHint.getInstance().showHint(getActivity(), entity.getMessage());
                        } else {
                            CommonDialogHint.getInstance().showHint(getActivity(),
                                    getActivity().getString(R.string.error_common_net_request_fail));
                        }
                    }
                } else {
                    locateSelf(LOCATION_TYPE_SHOW_AND_MOVE_POSITION);
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
        //TODO 由于仅一个
        locateSelf(LOCATION_TYPE_SHOW_AND_MOVE_POSITION);
    }

    /**
     * ************************************************************************************************************************
     */
    public void isShowNoBindDialog(boolean isShow){
        if(isShow && !ICommonSharePreferencesKey.KEY_SHOW_NO_BIND_NOT_SHOW.equals(BaseApplication.getInstances().getKeyValue(ICommonSharePreferencesKey.KEY_SHOW_NO_BIND))){
            noBindDialog = new Dialog(getActivity(), R.style.dialog);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_main_map_entry_tips, null);
            RelativeLayout exit = (RelativeLayout) view.findViewById(R.id.exit);
            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.check_status);
            TextView btn_confirm = (TextView) view.findViewById(R.id.btn_confirm);
            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        BaseApplication.getInstances().setKeyValue(ICommonSharePreferencesKey.KEY_SHOW_NO_BIND, ICommonSharePreferencesKey.KEY_SHOW_NO_BIND_NOT_SHOW);
                    }
                    if (noBindDialog != null) {
                        noBindDialog.dismiss();
                        //TODO 跳转至绑定页
                        Intent intent = new Intent(getActivity(), BindStepOneActivity.class);
                        startActivityForResult(intent, ICommonActivityRequestCode.RELOAD_DATA);
                    }
                }
            });
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        BaseApplication.getInstances().setKeyValue(ICommonSharePreferencesKey.KEY_SHOW_NO_BIND, ICommonSharePreferencesKey.KEY_SHOW_NO_BIND_NOT_SHOW);
                    }
                    if (noBindDialog != null) {
                        noBindDialog.dismiss();
                    }
                }
            });

            WindowManager.LayoutParams lp = noBindDialog.getWindow().getAttributes();
            lp.width = UnitExchangeUtil.dip2px(getActivity(), 300);
            lp.height = lp.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            noBindDialog.setContentView(view, lp);
            noBindDialog.setCanceledOnTouchOutside(false);
            if (noBindDialog!=null&&!noBindDialog.isShowing()) {
                noBindDialog.show();
            }
        }else{
            if(noBindDialog!=null){
                noBindDialog.dismiss();
            }
        }
    }

    public void dimissNoBindDialog(){
        if(noBindDialog!=null && noBindDialog.isShowing()){
            noBindDialog.dismiss();
        }
    }



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
                //TODO ----融合更新后的数据（可以存储到数据库中的）
                List<BindWatchInfoEntity> newRestoreBindWatchList = refreshNetData(list);

                //TODO 将更新的数据保存进去----删除原有数据，将上一步更新的数据保存进去
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
                        getBindWatchInfoEntityDao().insertOrReplaceInTx(newRestoreBindWatchList);


                //TODO 清除原有数据引用
                if(watchInfoEntityList.size()>0){
                    watchInfoEntityList.clear();
                }else{
                    watchInfoEntityList = new ArrayList<>();
                }
                //TODO 存入最新的数据
                watchInfoEntityList.addAll(newRestoreBindWatchList);

                //TODO 将数据展示到ListView中

                loadDataIntoListView();
                resetMarkerData(true);
                clearMapMarker();
                showMarkerOnMap(markerList,currentClickPosition);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            //TODO 展示提示人进行绑定的Dialog
            locateSelf(LOCATION_TYPE_SHOW_AND_MOVE_POSITION);
        }
    }

    /**
     * TODO <B>获取当前用户绑定的手表列表</B>   (从本地数据库中获取)
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
     * TODO 更新ListView中的数据
     *
     * @param list
     * @return
     */
    public List<BindWatchInfoEntity> refreshNetData(List<BindWatchInfoEntity> list){
        List<BindWatchInfoEntity> tempList = new ArrayList<>();
        tempList.addAll(watchInfoEntityList);
        for(BindWatchInfoEntity netData:list){
            int size = tempList.size();
            for(int i=size-1;i>-1;i--){
                BindWatchInfoEntity localData = tempList.get(i);
                //TODO 同一个设备，则更新网络数据
                if(localData.getImei().equals(netData.getImei())){
                    //TODO 主要更新数据为---报警时间与语音未读数
                    netData.setAlarmTime(localData.getAlarmTime());
                    netData.setUnreadMessageNumber(localData.getUnreadMessageNumber());
                    tempList.remove(i);//TODO 更新完后删除该数据
                }
            }
        }
        return list;
    }

    //TODO 将接口对象转化成数据库持久化对象
    public BindWatchInfoEntity transformToDBEntity(GetWatchListEntity.ListBean entity) throws JSONException, UnsupportedEncodingException {
        BindWatchInfoEntity watchInfoEntity = new BindWatchInfoEntity();

        watchInfoEntity.setImei(entity.getImei());
        watchInfoEntity.setHeadImage(entity.getImage());
        watchInfoEntity.setName(entity.getFormatName());
        watchInfoEntity.setLocation(entity.getLocation());
        watchInfoEntity.setLocationTime(entity.getLocationTime());

        watchInfoEntity.setPhone(entity.getPhone());
        watchInfoEntity.setOnline(entity.getOnline() + "");
        watchInfoEntity.setRelationlist(entity.getRelationListToJSONArray().toString());
        watchInfoEntity.setType(entity.getType() + "");
        watchInfoEntity.setAlarmTime("0");

        watchInfoEntity.setUnreadMessageNumber(0);
        //TODO 设置绑定的用户
        watchInfoEntity.setBindUserID(BaseApplication.getInstances().getLoginInfo().getUserId());
        return watchInfoEntity;
    }

    /**
     * 将watchInfoEntityList中的数据在ListView中展示出来
     */
    public void loadDataIntoListView(){
//        watchInfoEntityList
        if(bindWatchListAdapter==null){
            bindWatchListAdapter = new DeviceContainerAdapter(getActivity(),watchInfoEntityList);
            deviceContainer.setAdapter(bindWatchListAdapter);
            bindWatchListAdapter.setSelectedPosition(currentClickPosition);
            deviceContainer.setOnItemClickListener(itemClickListener);
        }else{
            bindWatchListAdapter.setDevices(watchInfoEntityList);
            bindWatchListAdapter.setSelectedPosition(currentClickPosition);
            bindWatchListAdapter.notifyDataSetChanged();
        }
    }

    //TODO 展示特定IMEI号的点---报警点---(由MainActivity收到SOS等报警广播后调用展示数据)
    public void showAlarmWatchPoint(String imei,double lat,double lng){
        //TODO
        if(imei!=null && imei.length()!= ICommonData.VALID_IMEI_LENGTH && lat>0 && lng>0){//TODO 控制参数的有效性
            int size = watchInfoEntityList.size();
            for(int i=0;i<size;i++){
                if(imei.equals(watchInfoEntityList.get(i).getImei())){//TODO 找到对应的设备
                    //TODO 更新设备定位与报警时间信息
                    watchInfoEntityList.get(i).setLocation(lng+","+lat);
                    watchInfoEntityList.get(i).setAlarmTime(System.currentTimeMillis()+"");
                    //TODO 更新当前点为报警点
                    currentClickPosition = i;
                    //TODO 更新持久化数据信息
                    DaoManager.getInstances(getActivity()).getDaoSession().getBindWatchInfoEntityDao().insertOrReplace(watchInfoEntityList.get(i));
                    break;
                }
            }
            //TODO 刷新ListView
            bindWatchListAdapter.notifyDataSetChanged();
            //TODO 刷新地图的各个Marker
            resetMarkerData(true);
            clearMapMarker();
            showAlarmMarkerOnMap(markerList,currentClickPosition);
        }
    }

    //TODO 开始警报计时，若存在其他响应时，则先取消当前的定时器
    private Timer sosTimer;
    private TimerTask timerTask;
    //TODO
    public void startAlarmTimer(){
        if (sosTimer != null) {
            sosTimer.cancel();
            sosTimer = null;
        }
        sosTimer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clearMapMarker();
                        showMarkerOnMap(markerList,currentClickPosition);
                    }
                });
                sosTimer.cancel();
                sosTimer = null;
            }
        };
        sosTimer.schedule(timerTask, 15000);//TODO 展示15秒的的
    }

    public void resetMarkerData(boolean isClearOriginalData){
        //TODO 若需要，则清除地图原有的数据
        if(isClearOriginalData){
            clearMapMarker();
        }

        if(watchInfoEntityList!=null && watchInfoEntityList.size()>0){
            //TODO 根据 实际绑定的用户地址，对应出Marker的位置

            //TODO 清理原来的数据地址
            if(markerList==null){
                markerList = new ArrayList<>();
            }else{
                markerList.clear();
            }
            //TODO

            for (BindWatchInfoEntity dataTemp : watchInfoEntityList) {
                if (dataTemp != null) {
                    if (dataTemp.getLocation() != null && dataTemp.getLocation().split(",").length == 2) {
                        String[] location = dataTemp.getLocation().split(",");
                        try {
                            LatLng locTemp = new LatLng(Double.parseDouble(location[1]), Double.parseDouble(location[0]));
                            markerList.add(locTemp);
                        } catch (Exception e) {
                            e.printStackTrace();
                            markerList.add(null);
                        }

                    } else {
                        markerList.add(null);
                    }
                }else{
                    markerList.add(null);
                }
            }
        }else{
            locateSelf(LOCATION_TYPE_SHOW_AND_MOVE_POSITION);//TODO 若没有绑定数据，则展示自己的定位位置
        }
    }

    /**
     * 刷新前需要删除已经存在数据---地图上的marker
     *
     * @param list
     * @param infoWindowPosition
     */
    public void showMarkerOnMap(List<LatLng> list, int infoWindowPosition){
        if (list != null && list.size() > 0) {
            initMarkerImageContainerList();
            if (clickedMarker != null && clickedMarker.isInfoWindowShown()) {
                clickedMarker.hideInfoWindow();
            }
            addMarker(infoWindowPosition);
        }else{//TODO 若没有绑定则展示自己的位置
            return;
        }
    }

    public void addMarker(int infoWindowPosition){
        if(aMap!=null){
            int size = markerList.size();
            for (int i = 0; i < size; i++) {
                final LatLng temp = markerList.get(i);
                if (temp != null) {
                    if (i == infoWindowPosition) {
                        //TODO 需要区分下是老人还是儿童
                        if (ICommonData.DEVICE_TYPE_OLD.equals(watchInfoEntityList.get(i).getType())) {
                            clickedMarker = aMap.addMarker(new MarkerOptions()
                                    .position(temp)
                                    .title("" + i)
                                    .icons(iconOldList)
                                    .period(2)
                                    .draggable(false));
                        } else if (ICommonData.DEVICE_TYPE_CHILD.equals(watchInfoEntityList.get(i).getType())) {
                            clickedMarker = aMap.addMarker(new MarkerOptions()
                                    .position(temp)
                                    .title("" + i)
                                    .icons(iconChildList)
                                    .period(2)
                                    .draggable(false));
                        } else {
                            clickedMarker = aMap.addMarker(new MarkerOptions()
                                    .position(temp)
                                    .title("" + i)
                                    .icons(iconOldList)
                                    .draggable(false));
                        }
                        if(clickedMarker!=null){
                            clickedMarker.showInfoWindow();
                        }
                    } else {

                        if (ICommonData.DEVICE_TYPE_OLD.equals(watchInfoEntityList.get(i).getType())) {
                            aMap.addMarker(new MarkerOptions()
                                    .position(temp)
                                    .title("" + i)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.main_map_marker_old))
                                    .draggable(false));
                        } else if (ICommonData.DEVICE_TYPE_CHILD.equals(watchInfoEntityList.get(i).getType())) {
                            aMap.addMarker(new MarkerOptions()
                                    .position(temp)
                                    .title("" + i)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.main_map_marker_child))
                                    .draggable(false));
                        } else {
                            aMap.addMarker(new MarkerOptions()
                                    .position(temp)
                                    .title("" + i)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.main_map_marker_old))
                                    .draggable(false));
                        }
                    }
                    if (infoWindowPosition == i) {
                        moveCamera(temp);
                    }
                } else {
                    //TODO 经纬度为空，无效的定位点，不进行处理
                }
            }
        }
    }

    /**
     * 刷新前需要删除已经存在数据
     * @param list
     * @param infoWindowPosition
     */
    public void showAlarmMarkerOnMap(List<LatLng> list, int infoWindowPosition){
        if (list != null && list.size() > 0) {
            initMarkerAlarmImageContainerList();
            //TODO 当前展示的InfoWindow如果展示的话
            if (clickedMarker != null && clickedMarker.isInfoWindowShown()) {
                clickedMarker.hideInfoWindow();
            }
            addAlarmMarker(infoWindowPosition);
        }else{//TODO 若没有绑定则展示自己的位置
            return;
        }
    }
    public void addAlarmMarker(int infoWindowPosition){
        if(aMap!=null){
            int size = markerList.size();
            for (int i = 0; i < size; i++) {
                final LatLng temp = markerList.get(i);
                if (temp != null) {
                    if (i == infoWindowPosition) {
                        //TODO 需要区分下是老人还是儿童
                        if (ICommonData.DEVICE_TYPE_OLD.equals(watchInfoEntityList.get(i).getType())) {
                            clickedMarker = aMap.addMarker(new MarkerOptions()
                                    .position(temp)
                                    .title("" + i)
                                    .icons(iconOldList)
                                    .period(2)
                                    .draggable(false));
                        } else if (ICommonData.DEVICE_TYPE_CHILD.equals(watchInfoEntityList.get(i).getType())) {
                            clickedMarker = aMap.addMarker(new MarkerOptions()
                                    .position(temp)
                                    .title("" + i)
                                    .icons(iconChildList)
                                    .period(2)
                                    .draggable(false));
                        } else {
                            clickedMarker = aMap.addMarker(new MarkerOptions()
                                    .position(temp)
                                    .title("" + i)
                                    .icons(iconOldList)
                                    .draggable(false));
                        }
                        if(clickedMarker!=null){
                            clickedMarker.showInfoWindow();
                        }
                    } else {

                        if (ICommonData.DEVICE_TYPE_OLD.equals(watchInfoEntityList.get(i).getType())) {
                            aMap.addMarker(new MarkerOptions()
                                    .position(temp)
                                    .title("" + i)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.main_map_marker_old))
                                    .draggable(false));
                        } else if (ICommonData.DEVICE_TYPE_CHILD.equals(watchInfoEntityList.get(i).getType())) {
                            aMap.addMarker(new MarkerOptions()
                                    .position(temp)
                                    .title("" + i)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.main_map_marker_child))
                                    .draggable(false));
                        } else {
                            aMap.addMarker(new MarkerOptions()
                                    .position(temp)
                                    .title("" + i)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.main_map_marker_old))
                                    .draggable(false));
                        }
                    }
                    if (infoWindowPosition == i) {
                        moveCamera(temp);
                    }
                } else {
                    //TODO 经纬度为空，无效的定位点，不进行处理
                }
            }

            startAlarmTimer();
        }
    }


    private Marker clickedMarker;
    private ArrayList<BitmapDescriptor> iconChildList = new ArrayList<>();
    private ArrayList<BitmapDescriptor> iconOldList = new ArrayList<>();
    //TODO 初始化Marker图标容器(普通)
    public void initMarkerImageContainerList(){
        if (iconChildList == null || iconChildList.size() < 1) {
            iconChildList = new ArrayList<>();
        } else {
            iconChildList.clear();
        }
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_1));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_2));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_3));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_4));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_5));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_6));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_7));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_8));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_9));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_10));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_11));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_12));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_13));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_14));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_15));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_child_16));

        if (iconOldList == null || iconOldList.size() < 1) {
            iconOldList = new ArrayList<>();
        } else {
            iconOldList.clear();
        }
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_1));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_2));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_3));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_4));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_5));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_6));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_7));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_8));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_9));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_10));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_11));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_12));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_13));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_14));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_15));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_location_old_16));
    }

    //TODO 初始化Marker图标容器(报警)
    public void initMarkerAlarmImageContainerList(){
        if (iconChildList == null || iconChildList.size() < 1) {
            iconChildList = new ArrayList<>();
        } else {
            iconChildList.clear();
        }
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_child_point_1));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_child_point_2));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_child_point_3));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_child_point_4));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_child_point_5));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_child_point_6));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_child_point_7));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_child_point_8));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_child_point_9));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_child_point_10));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_child_point_11));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_child_point_12));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_child_point_13));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_child_point_14));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_child_point_15));
        iconChildList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_child_point_16));

        if (iconOldList == null || iconOldList.size() < 1) {
            iconOldList = new ArrayList<>();
        } else {
            iconOldList.clear();
        }
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_old_point_1));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_old_point_2));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_old_point_3));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_old_point_4));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_old_point_5));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_old_point_6));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_old_point_7));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_old_point_8));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_old_point_9));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_old_point_10));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_old_point_11));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_old_point_12));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_old_point_13));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_old_point_14));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_old_point_15));
        iconOldList.add(BitmapDescriptorFactory.fromResource(R.drawable.alarm_old_point_16));
    }

    public void clearMapMarker(){
        if (aMap != null) {
            aMap.clear();
        }
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            currentClickPosition = (int)l;
            if(watchInfoEntityList.size()>currentClickPosition){//TODO 确保点击的部分有效
                //TODO
                if (30000 > (System.currentTimeMillis() - Long.parseLong(watchInfoEntityList.get(currentClickPosition).getAlarmTime()))) {
                    clearMapMarker();
                    showAlarmMarkerOnMap(markerList, currentClickPosition);
                } else {
                    clearMapMarker();
                    showMarkerOnMap(markerList, currentClickPosition);
                }
                //TODO 切换点击
                bindWatchListAdapter.setSelectedPosition(currentClickPosition);//TODO 这个用于显示点击时的切换效果
                bindWatchListAdapter.notifyDataSetChanged();

                //TODO 重新展示Marker并
                if(markerList.get(currentClickPosition)!=null){
                    moveCamera(markerList.get(currentClickPosition));
                }else{
                    locateSelf(LOCATION_TYPE_SHOW_AND_MOVE_POSITION);
                }
            }
        }
    };

}
