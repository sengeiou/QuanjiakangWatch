package com.quanjiakan.util.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.quanjiakan.watch.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/21 0021.
 */

public class NotificationUtil {

    // NotificationManager ： 是状态栏通知的管理类，负责发通知、清楚通知等。
    private NotificationManager manager;
    // 定义Map来保存Notification对象
    private Map<Integer, Integer> map = new HashMap<Integer, Integer>();

    private static NotificationUtil instances;

    private NotificationUtil(Context context) {
        if(manager==null){
            // NotificationManager 是一个系统Service，必须通过 getSystemService()方法来获取。
            manager = (NotificationManager) context.getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            map.clear();
        }
    }

    public static NotificationUtil getInstances(Context context){
        if(instances==null){
            synchronized (NotificationUtil.class){
                if(instances==null){
                    instances = new NotificationUtil(context.getApplicationContext());
                }
            }
        }
        return instances;
    }

    public void release(){
        instances = null;
    }

    /**
     * **********************************************************************************************
     * 取消通知操作
     */
    public void cancel(int notificationId) {
        manager.cancel(notificationId);
        map.remove(notificationId);
    }

    public void cancelAll() {
        manager.cancelAll();
        map.clear();
    }

    /**
     * **********************************************************************************************
     * 将通知加入散列表中
     */

    public void put(Context mContext,int id){
        if(map.containsKey(id)){
            NotificationManager manager = (NotificationManager) mContext
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(id);
        }
        map.put(id,id);
    }

    /**
     * **********************************************************************************************
     * 通用的通知---在通知栏上点击此通知后自动清除此通知
     */
    public int commonNotificationAutoCancel(Context context, String title, String text, Intent jump) {
        // 创建一个通知
        int time = (int) System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setContentText(text);
            builder.setContentTitle(title);
            builder.setAutoCancel(true);
            if(jump!=null){
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, jump, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(contentIntent);
            }
            builder.setWhen(System.currentTimeMillis());

            Notification mNotification = builder.build();
            mNotification.tickerText = text;
            mNotification.when = System.currentTimeMillis();
            mNotification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.beep);
            mNotification.sound = sound;
            mNotification.flags = Notification.FLAG_AUTO_CANCEL;

            //TODO 发送通知
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(time, mNotification);
            put(context,time);
        } else {
            Notification mNotification = new Notification();
            // 设置属性值
            mNotification.icon = R.drawable.ic_launcher;
            mNotification.tickerText = text;
            mNotification.when = System.currentTimeMillis(); // 立即发生此通知
            // 添加声音效果
            mNotification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.beep);
            mNotification.sound = sound;
            mNotification.flags = Notification.FLAG_AUTO_CANCEL;
            PendingIntent contentIntent = PendingIntent.getActivity
                    (context, 0, jump, 0);
//            mNotification.setLatestEventInfo(context, title,
//                    text, contentIntent);
            mNotification.contentIntent = contentIntent;
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(time, mNotification);
        }
        return time;
    }

    public int commonNotificationAutoCancel(Context context, String title, String text, Intent jump,int notifyID) {
        // 创建一个通知
        int time = (int) System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setContentText(text);
            builder.setContentTitle(title);
            builder.setAutoCancel(true);
            if(jump!=null){
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, jump, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(contentIntent);
            }
            builder.setWhen(System.currentTimeMillis());

            Notification mNotification = builder.build();
            mNotification.tickerText = text;
            mNotification.when = System.currentTimeMillis();
            mNotification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.beep);
            mNotification.sound = sound;
            mNotification.flags = Notification.FLAG_AUTO_CANCEL;

            //TODO 发送通知
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notifyID, mNotification);

            put(context,notifyID);
        } else {
            Notification mNotification = new Notification();
            // 设置属性值
            mNotification.icon = R.drawable.ic_launcher;
            mNotification.tickerText = text;
            mNotification.when = System.currentTimeMillis(); // 立即发生此通知
            // 添加声音效果
            mNotification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.beep);
            mNotification.sound = sound;
            mNotification.flags = Notification.FLAG_AUTO_CANCEL;
            PendingIntent contentIntent = PendingIntent.getActivity
                    (context, 0, jump, 0);
//            mNotification.setLatestEventInfo(context, title,
//                    text, contentIntent);
            mNotification.contentIntent = contentIntent;
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(time, mNotification);
        }
        return time;
    }

    /**
     * **********************************************************************************************
     * 通用的通知---重复发出声音，直到用户响应此通知
     */
    public int commonNotificationInsistent(Context context, String title, String text, Intent jump) {
        // 创建一个通知
        int time = (int) System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setContentText(text);
            builder.setContentTitle(title);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, jump, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(contentIntent);
            builder.setWhen(System.currentTimeMillis());

            Notification mNotification = builder.build();
            mNotification.tickerText = text;
            mNotification.when = System.currentTimeMillis();
            mNotification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.beep);
            mNotification.sound = sound;
            mNotification.flags = Notification.FLAG_INSISTENT;

            //TODO 发送通知
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(time, mNotification);

            put(context,time);
        } else {
            Notification mNotification = new Notification();
            // 设置属性值
            mNotification.icon = R.drawable.ic_launcher;
            mNotification.tickerText = text;
            mNotification.when = System.currentTimeMillis(); // 立即发生此通知
            // 添加声音效果
            mNotification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.beep);
            mNotification.sound = sound;
            mNotification.flags = Notification.FLAG_INSISTENT;
            PendingIntent contentIntent = PendingIntent.getActivity
                    (context, 0, jump, 0);
//            mNotification.setLatestEventInfo(context, title,
//                    text, contentIntent);
            mNotification.contentIntent = contentIntent;
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(time, mNotification);
        }
        return time;
    }

    public int commonNotificationInsistent(Context context, String title, String text, Intent jump,int notifyID) {
        // 创建一个通知
        int time = (int) System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setContentText(text);
            builder.setContentTitle(title);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, jump, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(contentIntent);
            builder.setWhen(System.currentTimeMillis());

            Notification mNotification = builder.build();
            mNotification.tickerText = text;
            mNotification.when = System.currentTimeMillis();
            mNotification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.beep);
            mNotification.sound = sound;
            mNotification.flags = Notification.FLAG_INSISTENT;

            //TODO 发送通知
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notifyID, mNotification);

            put(context,notifyID);
        } else {
            Notification mNotification = new Notification();
            // 设置属性值
            mNotification.icon = R.drawable.ic_launcher;
            mNotification.tickerText = text;
            mNotification.when = System.currentTimeMillis(); // 立即发生此通知
            // 添加声音效果
            mNotification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.beep);
            mNotification.sound = sound;
            mNotification.flags = Notification.FLAG_INSISTENT;
            PendingIntent contentIntent = PendingIntent.getActivity
                    (context, 0, jump, 0);
//            mNotification.setLatestEventInfo(context, title,
//                    text, contentIntent);
            mNotification.contentIntent = contentIntent;
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(time, mNotification);
        }
        return time;
    }

    /**
     * **********************************************************************************************
     * 通用的通知---仅响一次
     */
    public int commonNotificationAlertOnce(Context context, String title, String text, Intent jump) {
        // 创建一个通知
        int time = (int) System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setContentText(text);
            builder.setContentTitle(title);
            if(jump!=null){
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, jump, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(contentIntent);
            }
            builder.setWhen(System.currentTimeMillis());

            Notification mNotification = builder.build();
            mNotification.tickerText = text;
            mNotification.when = System.currentTimeMillis();
            mNotification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.beep);
            mNotification.sound = sound;
            mNotification.flags = Notification.FLAG_ONLY_ALERT_ONCE;

            //TODO 发送通知
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(time, mNotification);

            put(context,time);
        } else {
            Notification mNotification = new Notification();
            // 设置属性值
            mNotification.icon = R.drawable.ic_launcher;
            mNotification.tickerText = text;
            mNotification.when = System.currentTimeMillis(); // 立即发生此通知
            // 添加声音效果
            mNotification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.beep);
            mNotification.sound = sound;
            mNotification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
            PendingIntent contentIntent = PendingIntent.getActivity
                    (context, 0, jump, 0);
//            mNotification.setLatestEventInfo(context, title,
//                    text, contentIntent);
            mNotification.contentIntent = contentIntent;

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(time, mNotification);
        }
        return time;
    }

    public int commonNotificationAlertOnce(Context context, String title, String text, Intent jump,int notifyID) {
        // 创建一个通知
        int time = (int) System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setContentText(text);
            builder.setContentTitle(title);
            if(jump!=null){
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, jump, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(contentIntent);
            }
            builder.setWhen(System.currentTimeMillis());

            Notification mNotification = builder.build();
            mNotification.tickerText = text;
            mNotification.when = System.currentTimeMillis();
            mNotification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.beep);
            mNotification.sound = sound;
            mNotification.flags = Notification.FLAG_ONLY_ALERT_ONCE;

            //TODO 发送通知
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notifyID, mNotification);

            put(context,notifyID);
        } else {
            Notification mNotification = new Notification();
            // 设置属性值
            mNotification.icon = R.drawable.ic_launcher;
            mNotification.tickerText = text;
            mNotification.when = System.currentTimeMillis(); // 立即发生此通知
            // 添加声音效果
            mNotification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.beep);
            mNotification.sound = sound;
            mNotification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
            PendingIntent contentIntent = PendingIntent.getActivity
                    (context, 0, jump, 0);
//            mNotification.setLatestEventInfo(context, title,
//                    text, contentIntent);
            mNotification.contentIntent = contentIntent;
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(time, mNotification);
        }
        return time;
    }

    /**
     * **********************************************************************************************
     */
}
