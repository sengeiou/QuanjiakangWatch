package com.quanjiakan.activity.common.main.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.amap.api.maps.MapView;
import com.quanjiakan.activity.base.BaseFragment;
import com.quanjiakan.broadcast.entity.CommonNattyData;
import com.quanjiakan.watch.R;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/17.
 */

public class VideoFragment extends BaseFragment {
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.device_container)
    ListView deviceContainer;
    Unbinder unbinder;
    //***************************************************

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
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
        unregister();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ibtn_back, R.id.menu_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back: {
                break;
            }
            case R.id.menu_text: {
                break;
            }
        }
    }

    /**
     * *****************************************************************************************************************************
     * EventBus 广播事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonNattyData(CommonNattyData result){
        //TODO 控制无效的广播
        if(result==null){
            return;
        }
        switch (result.getType()){

        }
    }
    /**
     * ************************************************************************************************************************
     */

    public void setDefaultValue() {
        /**
         * 设置组件的默认值
         */

    }

    /**
     * ************************************************************************************************************************
     */
}
