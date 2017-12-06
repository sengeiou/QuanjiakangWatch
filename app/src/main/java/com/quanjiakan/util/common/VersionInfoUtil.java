package com.quanjiakan.util.common;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 *  获取APK版本相关信息
 *
 * Created by Administrator on 2017/10/12.
 */

public class VersionInfoUtil {

    /**
     * ****************************************************
     * 获取版本信息
     */

    public static String getVersion(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(context.getPackageName(), 0).versionName + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(context.getPackageName(), 0).versionCode + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getVersionCodeInt(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
