package com.quanjiakan.activity.base;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;

import com.quanjiakan.util.common.MessageDigestUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


public class BaseApplication extends MultiDexApplication {

    /**
     * 打包时需要变更
     */
    private BaseActivity currentActivity;

    private static final String JCHAT_CONFIGS = "JChat_configs";
    public static final String TARGET_APP_KEY = "targetAppKey";
    public static final String TARGET_ID = "targetId";
    private final static String TAG = "Quanjiakan";

    private static BaseApplication instances;

    public static BaseApplication getInstances() {
        return instances;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;

        initJpush();

        QuanjiakanSetting.init(this);

        initUmeng();
//        stat();
        //配置数据库
//        setupDatabase();

    }

    public void initUmeng(){
        MobclickAgent.enableEncrypt(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setCatchUncaughtExceptions(true);
    }

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

    public void initJpush() {
        //初始化JMessage-sdk
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(getApplicationContext());
//
////        JMessageClient.setDebugMode(true);
//        JMessageClient.init(getApplicationContext());
//
//
//        SharePreferenceManager.init(getApplicationContext(), JCHAT_CONFIGS);
//        //设置Notification的模式
//        JMessageClient.setNotificationMode(JMessageClient.NOTI_MODE_DEFAULT);//NOTI_MODE_DEFAULT NOTI_MODE_NO_NOTIFICATION
//        //注册Notification点击的接收器
//        new NotificationClickEventReceiver(getApplicationContext());
//
//        JPushInterface.setPushNotificationBuilder(11,new BasicPushNotificationBuilder(this));
    }

    /**
     * ****************************************************
     * Toast 展示提醒
     */

    public void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void toastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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
     * 保存用户名
     */

    public void setUsername(String username) {
        QuanjiakanSetting.getInstance().setUserName(username);
    }

    public String getUsername() {
        return QuanjiakanSetting.getInstance().getUserName();
    }


    /**
     * ****************************************************
     * 保存是否检查过更新
     */

    public boolean isCheckedUpdateToday() {
        boolean bool = false;
        String time = QuanjiakanSetting.getInstance().getUpdateTime();
        if (time == null || "".equals(time)) {
            bool = false;
        } else {
            long longTime = Long.parseLong(time);
            if ((System.currentTimeMillis() - longTime) < 24 * 60 * 60 * 1000) {
                //今天已经检查过更新了
                bool = true;
            } else {
                //今天尚未检查更新
//                QuanjiakanSetting.getInstance().setUpdateTime(System.currentTimeMillis()+"");
                bool = false;
            }
        }
        return bool;
    }


    public void updateCheckTime() {
        QuanjiakanSetting.getInstance().setUpdateTime(System.currentTimeMillis() + "");
    }

    /**
     * ****************************************************
     * 保存密码签名信息
     */

    public String getPw_signature() {
        return QuanjiakanSetting.getInstance().getPwSignature();
    }

    public void setPw_signature(String pw_signature) {
        if(pw_signature==null){
            QuanjiakanSetting.getInstance().setPwSignature(MessageDigestUtil.getSHA1String(pw_signature+""));
            return;
        }
        QuanjiakanSetting.getInstance().setPwSignature(MessageDigestUtil.getSHA1String(pw_signature));
    }

    public boolean isEqualToOriginal(String pw_signature){
        if(pw_signature==null){
            return false;
        }
        return getPw_signature().equals(MessageDigestUtil.getSHA1String(pw_signature));
    }

    /**
     * ****************************************************
     * 保存UserID信息
     */

    public String getUser_id() {
        return QuanjiakanSetting.getInstance().getUserId() + "";
    }

    public void setUser_id(String user_id) {
        QuanjiakanSetting.getInstance().setUserId(Integer.parseInt(user_id));
    }

    /**
     * ****************************************************
     *
     * 检测SDK状态
     *
     */
    public String getSDKServerStatus() {
        return QuanjiakanSetting.getInstance().getSDKServerStatus() + "";
    }

    public boolean isSDKConnected() {
        return QuanjiakanSetting.getInstance().getSDKServerStatus()>=0;
    }

    public void setSDKServerStatus(String sdk_status) {
        QuanjiakanSetting.getInstance().setSDKServerStatus(Integer.parseInt(sdk_status));
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
     */
    @Override
    public void onTerminate() {
        Log.e(TAG, ">>>>onTerminate<<<<");
        super.onTerminate();
    }

    /**
     * *********************************************************************************************
     * SharePreference 通用存取方法
     *
     */

    public String getKeyValue(String key) {
        return QuanjiakanSetting.getInstance().getKeyValue(key);
    }

    public void setKeyValue(String key, String value) {
        QuanjiakanSetting.getInstance().setKeyValue(key,value);
    }

    public int getKeyNumberValue(String key) {
        return QuanjiakanSetting.getInstance().getKeyNumberValue(key);
    }

    public void setKeyNumberValue(String key, int value) {
        QuanjiakanSetting.getInstance().setKeyNumberValue(key,value);
    }

    /**
     * *********************************************************************************************
     */

    public String getDefaultDevice() {
        return QuanjiakanSetting.getInstance().getKeyValue(getUser_id()+"DefaultDevice");
    }

    public void setDefaultDevice(String IMEI_16radix) {
        QuanjiakanSetting.getInstance().setKeyValue(getUser_id()+"DefaultDevice",IMEI_16radix);
    }

    public String getMode(){
        return QuanjiakanSetting.getInstance().getKeyValue(getUser_id()+"mode");
    }

    public void setMode(String mode) {
        QuanjiakanSetting.getInstance().setKeyValue(getUser_id()+"mode",mode);
    }

    /**
     * *********************************************************************************************
     */


    // *******************************************************  GreenDao

//    private static DaoSession daoSession;
//    private DaoMaster.DevOpenHelper helper;
//    private SQLiteDatabase db;
//    private DaoMaster daoMaster;
//
//    /**
//     * 配置数据库
//     */
//    private void setupDatabase() {
//        //创建数据库
//
//        helper = new DaoMaster.DevOpenHelper("watch.db", null);
//        //获取可写数据库
//        db = helper.getWritableDatabase();
////        helper.onUpgrade(db,DaoMaster.SCHEMA_VERSION-1,DaoMaster.SCHEMA_VERSION);
//        //获取数据库对象
//        daoMaster = new DaoMaster(db);
//        //获取Dao对象管理者
//        daoSession = daoMaster.newSession();
//    }
//
//    public static DaoSession getDaoInstant() {
//        return daoSession;
//    }
//
//    public SQLiteDatabase getDb() {
//        return db;
//    }


    /**
     * *********************************************************************************************
     */

    public BaseActivity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(BaseActivity currentActivity) {
        this.currentActivity = currentActivity;
    }
}
