package com.quanjiakan.activity.common.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.BaseFragment;
import com.quanjiakan.activity.common.main.MainActivity;
import com.quanjiakan.watch.R;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/17.
 */

public class SettingFragment extends BaseFragment {
    Unbinder unbinder;
    //***************************************************

    /**
     * ************************************************************************************************************************
     * 生命周期方法
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_setting, container, false);//TODO 实例化整个布局
        unbinder = ButterKnife.bind(this, view);
        //TODO 设置默认值
        setDefaultValue();


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
        initTitleBar();
        /**
         * 设置组件的默认值
         */
    }

    public void initTitleBar(){

    }

    /**
     * ************************************************************************************************************************
     */
}
