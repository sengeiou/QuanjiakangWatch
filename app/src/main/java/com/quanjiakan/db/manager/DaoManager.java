package com.quanjiakan.db.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.greendao.dao.DaoMaster;
import com.example.greendao.dao.DaoSession;
import com.pingantong.main.BuildConfig;

/**
 * Created by Administrator on 2017/10/13.
 */

public class DaoManager {
    /**
     * TODO dao类的统一管理类
     */

    private static DaoManager instances;

    private DaoSession daoSession;
    private DaoMaster.DevOpenHelper helper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;

    /**
     * 初始化数据库
     * @param context
     */
    private DaoManager(Context context) {
        helper = new DaoMaster.DevOpenHelper(context, BuildConfig.DB_NAME,null);
        //获取可写数据库
        db = helper.getWritableDatabase();
//        helper.onUpgrade(db,DaoMaster.SCHEMA_VERSION-1,DaoMaster.SCHEMA_VERSION);
        //获取数据库对象
        daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession(){
        return daoSession;
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    //TODO 双重检查形式的单例
    public static DaoManager getInstances(Context context) {
        if (instances == null) {
            synchronized (DaoManager.class) {
                if (instances == null) {
                    instances = new DaoManager(context.getApplicationContext());
                }
            }
        }
        return instances;
    }

    //TODO 基本用不到
    public void release(){
        instances = null;
    }

}
