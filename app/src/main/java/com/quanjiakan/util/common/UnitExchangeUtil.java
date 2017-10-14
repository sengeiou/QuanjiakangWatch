package com.quanjiakan.util.common;

import android.content.Context;

/**
 * 单位转换工具类
 *
 *
 * Created by Administrator on 2017/10/11.
 */

public class UnitExchangeUtil {

    //TODO ******************************************************************************************************
    //TODO 将指定大小  组件大小单位或像素 进行转换

    public static int dip2px(Context context, float dipValue) {
        if (dipValue == 0) {
            return 0;
        } else {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        }
    }

    public static int px2dip(Context context, float pxValue) {
        if (pxValue == 0) {
            return 0;
        } else {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }
    }

    //TODO ******************************************************************************************************
    //TODO 将指定大小  字体单位或像素 进行转换

    public static int sp2px(Context context, float dipValue) {
        if (dipValue == 0) {
            return 0;
        } else {
            final float scale = context.getResources().getDisplayMetrics().scaledDensity;
            return (int) (dipValue * scale + 0.5f);
        }
    }

    public static int px2sp(Context context, float pxValue) {
        if (pxValue == 0) {
            return 0;
        } else {
            final float scale = context.getResources().getDisplayMetrics().scaledDensity;
            return (int) (pxValue / scale + 0.5f);
        }
    }
}
