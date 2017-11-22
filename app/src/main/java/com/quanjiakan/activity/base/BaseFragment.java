package com.quanjiakan.activity.base;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.util.common.UnitExchangeUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/10/17.
 */

public abstract class BaseFragment extends Fragment implements IBaseActivity{

    /**
     * ************************************************************************************************************************
     * 生命周期方法
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater,container,savedInstanceState);
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
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * *****************************************************************************************************************************
     * EventBus 注册
     */
    public void register(){
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }
    public void unregister(){
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
    /**
     * ************************************************************************************************************************
     */

    @Override
    public Object getParamter(int type) {
        return null;
    }

    @Override
    public void showMyDialog(int type) {

    }

    @Override
    public void dismissMyDialog(int type) {

    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {

    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {

    }

    @Override
    public View getViewComponentByID(int viewID) {
        return null;
    }

    /**
     * ************************************************************************************************************************
     */

    public Dialog mDialog;
    public Dialog getDialog(Context context) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = new Dialog(context, R.style.dialog_loading);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = UnitExchangeUtil.dip2px(context, 80);
        lp.height = UnitExchangeUtil.dip2px(context, 80);
        lp.gravity = Gravity.CENTER;
        mDialog.setContentView(view, lp);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        return mDialog;
    }

    public Dialog getDialog(Context context,String msg) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = new Dialog(context, R.style.dialog_loading);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        if(msg!=null && msg.length()>0){
            tv.setVisibility(View.VISIBLE);
            tv.setText(msg);
        }else{
            tv.setVisibility(View.GONE);
        }
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = UnitExchangeUtil.dip2px(context, 80);
        lp.height = UnitExchangeUtil.dip2px(context, 80);
        lp.gravity = Gravity.CENTER;
        mDialog.setContentView(view, lp);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        return mDialog;
    }

    public void dismissDialog(){
        if(mDialog!=null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    public Dialog getNewDialog(Context context) {
        Dialog mDialog = new Dialog(context, R.style.dialog_loading);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = UnitExchangeUtil.dip2px(context, 80);
        lp.height = UnitExchangeUtil.dip2px(context, 80);
        lp.gravity = Gravity.CENTER;
        mDialog.setContentView(view, lp);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        return mDialog;
    }
}
