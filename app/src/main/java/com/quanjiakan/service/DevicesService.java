package com.quanjiakan.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.util.common.LogUtil;

public class DevicesService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        initBindDevice();
        LogUtil.e("DevicesService onCreate");
    }

    public void initBindDevice(){
        BaseApplication.getInstances().setSDKServerStatus(-1+"");
        if(BaseApplication.getInstances().isNattyNull()){
            BaseApplication.getInstances().addThreadTask(new Runnable() {
                @Override
                public void run() {
                    /**
                     * SDK 独立于用户的设备运行
                     *
                     * TODO 在子线程中启动，避免在主线程中影响交互 2017-04-22 改
                     */
                    LogUtil.e("Start Natty Service");
                    BaseApplication.getInstances().initNattyClient();
                    int res = BaseApplication.getInstances().startupNattyClient();
                    BaseApplication.getInstances().setSDKServerStatus(res+"");
                }
            });
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        LogUtil.e("DevicesService onStart");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e("DevicesService onStartCommand");
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
