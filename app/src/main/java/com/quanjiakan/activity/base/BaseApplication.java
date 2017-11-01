package com.quanjiakan.activity.base;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.quanjiakan.activity.common.login.SigninActivity_mvp;
import com.quanjiakan.db.entity.LoginUserInfoEntity;
import com.quanjiakan.db.manager.DaoManager;
import com.quanjiakan.service.DevicesService;
import com.quanjiakan.util.common.MessageDigestUtil;
import com.quanjiakan.util.notification.NotificationUtil;
import com.umeng.analytics.MobclickAgent;
import com.wbj.ndk.natty.client.NattyClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BaseApplication extends MultiDexApplication {

    /**
     * 打包时需要变更
     */

    private NattyClient nattyClient;
    private ExecutorService cachedThreadPool;

    private static BaseApplication instances;

    public static BaseApplication getInstances() {
        return instances;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;

        SharePreferencesSetting.init(this);

        //TODO 初始化友盟组件
        initUmeng();
        //TODO 初始化数据库
        initDB();

        initNatty();
        // TODO 初始化线程
        initThreadPool();
    }

    /**
     * *********************************************************************************************
     * 生命周期方法
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
    /**
     * *********************************************************************************************
     * 友盟组件初始化
     */
    public void initUmeng(){
        MobclickAgent.enableEncrypt(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setCatchUncaughtExceptions(true);
    }

    /**
     * ****************************************************
     * 启动Natty
     */

    public void initNatty(){
        startSDK();
    }

    /**
     * ****************************************************
     * 将assets里面的文件复制到SD卡中
     */

    private void copyAssetsToSD(String assetsFile, String sdFile) throws IOException {
        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(sdFile);
        myInput = this.getAssets().open(assetsFile);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0) {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }

        myOutput.flush();
        myInput.close();
        myOutput.close();
    }

    /**
     * ****************************************************
     * 格式化密码
     */

    public String getFormatPWString(String formatString){
        return MessageDigestUtil.getMD5String("@@"+formatString+"@@hi-board@@");
    }

    /**
     * ****************************************************
     * 保存是否检查过更新
     */

    public boolean isCheckedUpdateToday() {
        boolean bool = false;
        String time = SharePreferencesSetting.getInstance().getUpdateTime();
        if (time == null || "".equals(time)) {
            bool = false;
        } else {
            long longTime = Long.parseLong(time);
            if ((System.currentTimeMillis() - longTime) < 24 * 60 * 60 * 1000) {
                //今天已经检查过更新了
                bool = true;
            } else {
                //今天尚未检查更新
                SharePreferencesSetting.getInstance().setUpdateTime(System.currentTimeMillis()+"");
                bool = false;
            }
        }
        return bool;
    }


    public void updateCheckTime() {
        SharePreferencesSetting.getInstance().setUpdateTime(System.currentTimeMillis() + "");
    }

    /**
     * ****************************************************
     * 检测SDK状态
     */
    public void startSDK(){
        Intent intent = new Intent(this, DevicesService.class);
        startService(intent);
    }

    public void stopSDK(){
        Intent intent = new Intent(this, DevicesService.class);
        stopService(intent);
        //TODO 关闭Natty 的连接
        nattyClient.ntyShutdownClient();
    }

    public void initNattyClient(){
        nattyClient = new NattyClient();
        long appid = Long.parseLong(getLoginInfo().getUserId());
        nattyClient.ntyClientInitilize();
        nattyClient.ntySetSelfId(appid);
    }

    public NattyClient getNattyClient(){
        return nattyClient;
    }

    public String getSDKServerStatus() {
        return SharePreferencesSetting.getInstance().getSDKServerStatus() + "";
    }

    public boolean isSDKConnected() {
        return SharePreferencesSetting.getInstance().getSDKServerStatus()>=0;
    }

    public void setSDKServerStatus(String sdk_status) {
        SharePreferencesSetting.getInstance().setSDKServerStatus(Integer.parseInt(sdk_status));
    }
    /**
     * ****************************************************
     */

    /**
     * clear all param when exit the app
     */
    public static void onAppExit() {

    }

    /**
     * *********************************************************************************************
     * SharePreference 通用存取方法
     *
     */

    public String getKeyValue(String key) {
        return SharePreferencesSetting.getInstance().getKeyValue(key);
    }

    public void setKeyValue(String key, String value) {
        SharePreferencesSetting.getInstance().setKeyValue(key,value);
    }

    /**
     * *********************************************************************************************
     *  初始化数据库
     */
    public void initDB(){
        DaoManager.getInstances(this);
    }

    public void toast(Context context,String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void toastLong(Context context,String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * *********************************************************************************************
     * 可能会较常用到的一些数据
     */
    public void onLogout(BaseActivity context){

        //TODO 释放发送通知占用的资源（如果登录前不需要发送通知的话）
        NotificationUtil.getInstances(this).release();

        //TODO 退出时执行这个操作，保证当前登录用户的数据唯一，且不需要进行
        //TODO 删除用户登录信息
        DaoManager.getInstances(this).getDaoSession().getLoginUserInfoEntityDao().deleteAll();
        entity = null;

        //TODO 退出到登录界面-----
        Intent intent = new Intent(context, SigninActivity_mvp.class);
        context.startActivity(intent);
        context.finish();

        //TODO 关闭SDK的连接
        stopSDK();
    }

    //TODO 登录信息 获取
    private LoginUserInfoEntity entity;
    public LoginUserInfoEntity getLoginInfo(){
        if(entity==null){
            List<LoginUserInfoEntity> list = DaoManager.getInstances(this).getDaoSession().getLoginUserInfoEntityDao().loadAll();
            if(list!=null && list.size()>0){
                //TODO 由于这个表实际上仅会保存当前用户的登录相关信息，所以直接取第一个就可以了
                entity = list.get(0);
            }else{
                entity = null;
            }
        }
        return entity;
    }
    //TODO 直接判断是否存在登录，且尚未执行退出操作  （在Session过期的情况下，需要执行退出操作）
    public boolean isLogin(){
        if(BaseApplication.getInstances().getLoginInfo()!=null &&
                BaseApplication.getInstances().getLoginInfo().getUserId()!=null &&
                BaseApplication.getInstances().getLoginInfo().getUserId().length()>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * *********************************************************************************************
     *
     */
    public void initThreadPool(){
        cachedThreadPool = Executors.newCachedThreadPool();
    }

    public void addThreadTask(Runnable runnable){
        cachedThreadPool.submit(runnable);
    }


    /**
     * *********************************************************************************************
     *
     */

}
