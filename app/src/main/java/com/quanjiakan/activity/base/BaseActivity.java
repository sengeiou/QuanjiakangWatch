package com.quanjiakan.activity.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.util.common.UnitExchangeUtil;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseActivity extends AppCompatActivity implements IBaseActivity{

    //TODO 全部Activity共用的Dialog
    public Dialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_error_activity_parameters), new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        return;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
//        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
//        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

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

    //TODO 每个子类必须去实现这个接口方法，来为网络访问提供便利

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

    public void registerEventBus(){
        //TODO 没有注册则进行注册
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    public void unregisterEventBus(){
        //TODO 注册了，则反注册
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
