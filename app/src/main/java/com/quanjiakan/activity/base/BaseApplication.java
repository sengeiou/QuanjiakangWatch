package com.quanjiakan.activity.base;

import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import com.quanjiakan.activity.common.login.SigninActivity_mvp;
import com.quanjiakan.activity.common.main.MainActivity;
import com.quanjiakan.constants.ICommonActivityRequestCode;
import com.quanjiakan.db.entity.LoginUserInfoEntity;
import com.quanjiakan.db.manager.DaoManager;
import com.quanjiakan.service.DevicesService;
import com.quanjiakan.util.common.LogUtil;
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

    private WXPayResult payResult;

    private static BaseApplication instances;

    public static BaseApplication getInstances() {
        return instances;
    }

    private MainActivity mainActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;

        SharePreferencesSetting.init(this);

        //TODO 初始化友盟组件
        initUmeng();
        //TODO 初始化数据库
        initDB();
        // TODO 初始化线程
        initThreadPool();
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
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
     * Natty 相关
     */
    public void startSDK(){
        Intent intent = new Intent(this, DevicesService.class);
        startService(intent);
    }

    public void stopSDK(BaseActivity activity){

        //TODO 关闭Natty 的连接
        if(nattyClient!=null){
            nattyClient.ntyShutdownClient();
            nattyClient = null;
        }

        Intent intent = new Intent(activity, DevicesService.class);
        activity.stopService(intent);

    }

    public void initNattyClient(){
        nattyClient = new NattyClient();
        long appid = Long.parseLong(getLoginInfo().getUserId());
        nattyClient.ntyClientInitilize();
        nattyClient.ntySetSelfId(appid);
        LogUtil.e("Start Natty Client");
    }

    public NattyClient getNattyClient(){
        return nattyClient;
    }

    public int startupNattyClient(){
        return nattyClient.ntyStartupClient();
    }

    public boolean isNattyNull(){
        return nattyClient==null;
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

    /**
     * *********************************************************************************************
     * 可能会较常用到的一些数据
     */
    public void onLogout(BaseActivity context){
        //TODO 退出到登录界面-----
        Intent intent = new Intent(context, SigninActivity_mvp.class);
        context.startActivityForResult(intent, ICommonActivityRequestCode.TO_SIGN_IN);
        context.finish();

        if(mainActivity!=null){
            mainActivity.finish();
            mainActivity = null;
        }

        //TODO 释放发送通知占用的资源（如果登录前不需要发送通知的话）
        NotificationUtil.getInstances(this).release();

        //TODO 退出时执行这个操作，保证当前登录用户的数据唯一，且不需要进行
        //TODO 删除用户登录信息
        DaoManager.getInstances(this).getDaoSession().getLoginUserInfoEntityDao().deleteAll();
        entity = null;

        //TODO 关闭SDK的连接
        stopSDK(context);
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
     * 缓冲线程池任务---适合不需要获取返回的任务
     */
    public void initThreadPool(){
        cachedThreadPool = Executors.newCachedThreadPool();
    }

    public void addThreadTask(Runnable runnable){
        cachedThreadPool.submit(runnable);
    }


    /**
     * *********************************************************************************************
     * 微信支付----支付状态
     */

    /**
     * *********************************************************************************************
     *
     * 微信支付----最近支付的信息
     *
     */


    //TODO 保存最近一次微信支付订单与所进行的业务类型信息-------在调起支付时调用
    public void saveWxPayLastInfo(int businessTypeCode,String orderId){
        SharePreferencesSetting.getInstance().setWxPayLastPayInfo(businessTypeCode,orderId);
    }

    /**
     * @return true 表示拥有最近的支付信息
     */
    public boolean hasWxPayLastInfo(){
        return !"".equals(SharePreferencesSetting.getInstance().getWxPayLastPayInfo());
    }

    /**
     * 最近支付信息中的业务码
     * @return
     */
    public int getWxPayLastInfoBusinessTypeCode(){
        if(hasWxPayLastInfo()){
            return SharePreferencesSetting.getInstance().getWxPayLastPayInfoBusinessCodeIntValue();
        }else{
            return -1;
        }
    }

    /**
     * 最近支付信息中的订单ID
     * @return
     */
    public String getWxPayLastInfoOrderId(){
        if(hasWxPayLastInfo()){
            return SharePreferencesSetting.getInstance().getWxPayLastPayInfoOrderId();
        }else{
            return null;
        }
    }

    /**
     * 置空最近的一次支付信息(同时也清空订单信息)
     */
    public void clearWxPayLastInfo(){

        SharePreferencesSetting.getInstance().clearWxPayOrder(getWxPayLastInfoBusinessTypeCode());

        SharePreferencesSetting.getInstance().setWxPayLastPayInfoNull();
    }


    /**
     *
     * ******************************************************************
     * 保存订单结果
     *
     * @param businessTypeCode
     * @param orderId
     * @param result
     */
    public void saveWxPayResult(int businessTypeCode,String orderId,WXPayResult result){
        SharePreferencesSetting.getInstance().setWxPayOrder(businessTypeCode,orderId,result);
    }

    public boolean isWxPaySuccess(int businessTypeCode){
        return SharePreferencesSetting.getInstance().isWxPaySuccess(businessTypeCode);
    }

    public boolean isCurrentOrder(int businessTypeCode){
        if(SharePreferencesSetting.getInstance().hasWxPayOrder(businessTypeCode)){
            return SharePreferencesSetting.getInstance().getWxPayOrderId(businessTypeCode).
                    equals(getWxPayLastInfoOrderId());
        }else{
            return  false;
        }
    }

    /**
     *
     * 使用流程说明
     *
     * 获取订单后，保存业务码 和 订单号  saveWxPayLastInfo()
     *
     * 支付结果处，通过 getWxPayLastInfoBusinessTypeCode() 和  getWxPayLastInfoOrderId() 和 saveWxPayResult()
     * 保存这次的支付结果
     *
     * 最后在校验订单前，判读订单是否一致，
     * 成功，则校验定单，在拿到结果后，清除该次的支付信息
     *
     * *********************************************************************************************
     *
     */

}
