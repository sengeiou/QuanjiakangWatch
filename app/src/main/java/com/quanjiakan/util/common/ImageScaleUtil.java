package com.quanjiakan.util.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 *
 * 跟图片缩放相关的工具类
 *
 * Created by Administrator on 2017/10/11.
 */

public class ImageScaleUtil {

    public static Bitmap cutBitmap2FitScreen(Context mContext, Bitmap bm) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;// 获取屏幕的宽和高

        int widthDrawable = bm.getWidth();
        int heightDrawable = bm.getHeight();// 获取背景图片的宽和高

        float scaleWidth = (float) width / widthDrawable;
        float scaleHeight = (float) height / heightDrawable;// 宽高

        Bitmap resizeBmp;
        Matrix matrix = new Matrix();
        if (scaleWidth < scaleHeight) {
            float scale = scaleHeight;// 取大�?
            matrix.postScale(scale, scale);// 缩放比例
            int xStart = (int) (widthDrawable - widthDrawable / scale) / 2;

            resizeBmp = Bitmap.createBitmap(bm, xStart, 0, (int) (widthDrawable / scale), heightDrawable, matrix,
                    false);
        } else {
            float scale = scaleWidth;
            matrix.postScale(scale, scale);
            int yStart = (int) (scaleHeight - scaleHeight / scale) / 2;
            resizeBmp = Bitmap.createBitmap(bm, 0, yStart, widthDrawable, (int) (heightDrawable / scale), matrix,
                    false);
        }

        return resizeBmp;
    }
}
