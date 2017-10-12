package com.quanjiakan.activity.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.quanjiakan.watch.R;

public abstract class BaseActivity extends AppCompatActivity implements IBaseActivity{

    //TODO 全部Activity共用的Dialog
    public Dialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
    }

    @Override
    public void onPause() {
        super.onPause();
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
        lp.width = QuanjiakanUtil.dip2px(context, 80);
        lp.height = QuanjiakanUtil.dip2px(context, 80);
        lp.gravity = Gravity.CENTER;
        mDialog.setContentView(view, lp);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        return mDialog;
    }

    public Dialog getNewDialog(Context context) {
        Dialog mDialog = new Dialog(context, R.style.dialog_loading);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = QuanjiakanUtil.dip2px(context, 80);
        lp.height = QuanjiakanUtil.dip2px(context, 80);
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
}
