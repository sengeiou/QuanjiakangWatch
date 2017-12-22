package com.quanjiakan.util.common;

import android.os.Build;

/**
 * Created by Administrator on 2017/12/20.
 */

public class PermissionCheckUtil {

    private PermissionCheckUtil(){

    }
    /**
     * 静态内部类的单例模式
     * @return
     */
    public static PermissionCheckUtil getInstances(){
        return PermissionCheckUtil.PermissionCheckUtilHolder.instances;
    }

    public boolean isCanCheckPermission(){
        return  Build.VERSION.SDK_INT>=Build.VERSION_CODES.M;
    }

    /**
     * 私有静态内部类持有
     */
    private static class PermissionCheckUtilHolder{
        private static final PermissionCheckUtil instances = new PermissionCheckUtil();
    }
}
