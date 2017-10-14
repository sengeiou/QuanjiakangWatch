package com.quanjiakan.util.common;

import android.os.Environment;

/**
 * Created by Administrator on 2017/10/13.
 */

public class StorageDeviceUtil {

    /**
     * 是否存在SDCard，并且SDCard可用
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
