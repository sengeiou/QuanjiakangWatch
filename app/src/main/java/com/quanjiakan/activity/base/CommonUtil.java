
package com.quanjiakan.activity.base;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

	public static String getDayFromGMT(String gmt) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
		try {
			Date date = sdf.parse(gmt);
			return date.getDay() + "";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

   /**
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	@SuppressLint("SimpleDateFormat")
	public static int getMonthSpace(String date1, String date2){
        int result = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
			c1.setTime(sdf.parse(date1));
			c2.setTime(sdf.parse(date2));
			result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
			return result == 0 ? 1 : Math.abs(result);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
    }

}
