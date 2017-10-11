package com.quanjiakan.util.common;

import android.util.Log;

import com.quanjiakan.watch.BuildConfig;

/**
 *
 * 输出日志，根据当前配置的等级来判断
 *
 * Created by Administrator on 2017/9/12.
 */

public class LogUtil {
    private static final String TAG = "LogUtil";
    private static final int CURRENT_LEVEL = BuildConfig.LOG_LEVEL;
    private static final int LEVEL_INFO = 1;
    private static final int LEVEL_WARN = 2;
    private static final int LEVEL_ERROR = 3;

    public static final void i(String msg){
        if(CURRENT_LEVEL<=LEVEL_INFO){
            Log.i(TAG,msg+"");
        }
    }

    public static final void w(String msg){
        if(CURRENT_LEVEL<=LEVEL_WARN){
            Log.w(TAG,msg+"");
        }
    }

    public static final void e(String msg){
        if(CURRENT_LEVEL<=LEVEL_ERROR){
            Log.e(TAG,msg+"");
        }
    }
}
