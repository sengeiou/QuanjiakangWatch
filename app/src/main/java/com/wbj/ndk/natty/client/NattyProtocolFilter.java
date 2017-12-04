package com.wbj.ndk.natty.client;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.db.entity.MircoChatRecordEntity;
import com.quanjiakan.db.entity.MircoChatUnreadNumberRecordEntity;
import com.quanjiakan.db.manager.DaoManager;
import com.quanjiakan.device.entity.CommonBindRequest;
import com.quanjiakan.device.entity.CommonNattyData;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.common.MessageDigestUtil;
import com.quanjiakan.util.notification.NotificationUtil;
import com.wbj.ui.recorder.AudioFileFunc;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class NattyProtocolFilter {

    public final static int DISPLAY_UPDATE_CONTROL_POWER = 0x11;
    public final static int DISPLAY_UPDATE_CONTROL_SIGNAL = 0x12;
    public final static int DISPLAY_UPDATE_CONTROL_LOCATION = 0x13;
    public final static int DISPLAY_UPDATE_CONTROL_FALL = 0x14;
    public final static int DISPLAY_UPDATE_CONTROL_STEP = 0x15;
    public final static int DISPLAY_UPDATE_CONTROL_HEARTRATE = 0x16;
    public final static int DISPLAY_UPDATE_CONTROL_RECONNECTED = 0x17;
    public final static int DISPLAY_UPDATE_CONTROL_DISCONNECT = 0x18;
    public final static int DISPLAY_UPDATE_CONTROL_NOEXIST = 0x19;

    public final static int DISPLAY_UPDATE_CONTROL_BIND_RESULT = 0x1A;
    public final static int DISPLAY_UPDATE_CONTROL_UNBIND_RESULT = 0x1B;
    public final static int DISPLAY_UPDATE_STATUS = 0x1C;
    public final static int DISPLAY_UPDATE_CONFIG = 0x1D;

    public final static int VOICE_UPLOAD = 0x2A;
    public final static int VOICE_RECORD = 0x2B;
    public final static int VOICE_STOP = 0x2C;
    public final static int DISPLAY_VOICE_PLAY = 0x2D;

    public final static int DISPLAY_VOICE_RESULT = 0x2E;

    public final static int DISPLAY_EFENCE_RESULT = 0x30;
    public final static int DISPLAY_EFENCELIST_RESULT = 0x31;


    public final static int DISPLAY_UPDATE_CONTROL_LOCATION_NEW = 0x41;
    public final static int DISPLAY_UPDATE_DATA_RESULT = 0x42;
    public final static int DISPLAY_UPDATE_DATA_ROUTE = 0x43;
    public final static int DISPLAY_UPDATE_DATA_COMMON_BROADCAST = 0x44;
    public final static int DISPLAY_UPDATE_DATA_ADMIN_BIND_INFO = 0x45;

    public final static int DISPLAY_UPDATE_ERROR = 0xFF;
    public final static int DISPLAY_UPDATE_SUCCESS = 0xF0;

    /**
     * ***********************************************************************************************
     * case 1:
     * [MBProgressHUD showError:@"该UserID不存在"];
     * break;
     * case 2:
     * [MBProgressHUD showError:@"该设备ID不存在"];
     * break;
     * case 3:
     * [MBProgressHUD showError:@"UserId与DeviceId已经绑定过了"];
     * break;
     * case 4:
     * [MBProgressHUD showError:@"该设备未激活，请激活"];
     * break;
     * <p>
     * case 0:  绑定成功
     *
     * @param len
     */
    public static void ntyProtocolBind(int len) {
        judgeMessageBindResult(len);
    }

    /**
     * ***********************************************************************************************
     */
    public static void ntyProtocolUnBind(int len) {
        judgeMessageUnBindResult(len);
    }

    /**
     * ***********************************************************************************************
     */

    public static void ntyProtocolReconnect(int len) {
        judgeMessageReconnect(len);
    }

    public static void ntyProtocolDisconnect(int len) {
        judgeMessageDisconnect(len);

    }

    /**
     * ***********************************************************************************************
     */
    //{"Results":{"IMEI":"352315052834187","Proposer":"18011935659","UserName":"爸爸","MsgId":"40"}}
    //TODO 作为管理员，收到其他人的绑定申请
    public static void ntyProtocolDealBindReqesutFromOther(long fromID, String info) {
        judgeMessageBindRequestFromOther(fromID, info);
    }

    /**
     * ***********************************************************************************************
     * <p>
     * 定位------单独的数据通道
     */

    public static void ntyProtocolLocationResult(long id, String data) {
        judgeMessageLocation(data);
    }

    /**
     * ***********************************************************************************************
     * <p>
     * 语音------单独的数据通道
     */

    public static void ntyProtocolVoiceResult(long id, String data) {
//        EventBus.getDefault().post(new CommonNattyData(DISPLAY_VOICE_RESULT,data));
    }

    public static void ntyProtocolSaveVoiceFileToLocal(NattyClient client, long fromId, long gId, int length) {
        //TODO
        handleVoiceMessage(client, fromId, gId, length);
    }

    /**
     * ***********************************************************************************************
     */
    public static void ntyDataResult(long id, String data) {
        judgeMessageDataResult(data);
    }


    /**
     * ***********************************************************************************************
     * 推送消息-----忘记会从哪一个回调走了
     */

    public static void ntyCommonPushMessageResult(long id, String data, int status) {
        judgeMessagePush(1, data);
    }

    public static void ntySetPushMessageResult(long id, String data, int status) {
        judgeMessagePush(2, data);
    }

    public static void ntySetCommonReqResult(long id, String data, int status) {
        judgeMessagePush(3, data);
    }

    public static void ntyNativeCommonReqResult(long id, String data, int status) {
        judgeMessagePush(4, data);
    }

    /**
     * ***********************************************************************************************
     * 广播数据
     *
     * @param id
     * @param data
     * @param status
     */
    public static void ntyCommonBroadcastResult(long id, String data, int status) {
        /** 根据不同内容保存相应数据 **/
        judgeMessageBroadcastType(data);
    }

    /**
     * ***********************************************************************************************
     * Turn and runtime
     *********/

    public static void ntyProtocolTurn(String json) {
        judgeMessageTurnType(json);
    }

    /******
     * Turn and runtime
     *********/

    public static void ntyProtocolRunTime(String json) {
        judgeMessageRunTimeType(json);
    }


    /**
     * ***********************************************************************************************
     * DataRoute 数据------------
     *
     */

    /**
     * ***********************************************************************************************
     *
     * @param id
     * @param data
     * @param status
     */
    public static void ntyDataRoute(long id, String data, int status) {
        judgeMessageDateRouteType(data);
    }

    public static void ntyDataRoute(long id, String data) {
        judgeMessageDateRouteType(data);
    }

    //TODO 应该预先进行 数据的分类，然后派发对应的实体，可以省去，使用共同的部分来
    public static void ntyProtocolDateRoute(String json) {
        //这里对数据进行过滤----判断属于那种类型的消息
        judgeMessageDateRouteType(json);
    }

    /**
     * ***********************************************************************************************
     */

    public static void judgeMessageBroadcastType(String json) {
        //TODO 里面仍会有Runtime
        //通过JSON 判断是那种协议类型

        EventBus.getDefault().post(new CommonNattyData(DISPLAY_UPDATE_DATA_COMMON_BROADCAST, json));
    }

    /**
     * ***********************************************************************************************
     */
    public static void judgeMessageBindResult(int resultCode) {
        EventBus.getDefault().post(new CommonNattyData(DISPLAY_UPDATE_CONTROL_BIND_RESULT, resultCode + ""));
    }

    public static void judgeMessageUnBindResult(int resultCode) {
        EventBus.getDefault().post(new CommonNattyData(DISPLAY_UPDATE_CONTROL_UNBIND_RESULT, resultCode + ""));
    }

    /**
     * ***********************************************************************************************
     */
    public static void judgeMessageReconnect(int resultCode) {
        BaseApplication.getInstances().setSDKServerStatus("1");
    }

    public static void judgeMessageDisconnect(int resultCode) {
        BaseApplication.getInstances().setSDKServerStatus("-1");
    }

    /**
     * ***********************************************************************************************
     * 作为管理员，收到其他用户的绑定申请
     */
    public static void judgeMessageBindRequestFromOther(long fromID, String data) {
        //{"Results":{"IMEI":"352315052834187","Proposer":"18011935659","UserName":"爸爸","MsgId":"40"}}
        EventBus.getDefault().post(new CommonBindRequest(DISPLAY_UPDATE_DATA_ADMIN_BIND_INFO, fromID, data));
    }

    /**
     * ***********************************************************************************************
     */
    public static void judgeMessagePush(int from, String data) {
        LogUtil.e("FromType:" + from + "\n Data:" + data);
//        EventBus.getDefault().post(new CommonNattyPushMessage(data));
    }

    /**
     * ***********************************************************************************************
     */
    public static void judgeMessageLocation(String data) {
        LogUtil.e("judgeMessageLocation:" + data);
//        EventBus.getDefault().post(new CommonNattyData(DISPLAY_UPDATE_CONTROL_LOCATION_NEW,data));
    }

    /**
     * ***********************************************************************************************
     */
    public static void judgeMessageDataResult(String data) {
        EventBus.getDefault().post(new CommonNattyData(DISPLAY_UPDATE_DATA_RESULT, data));
    }

    //TODO 判断JSON属于哪一种类型的数据，如果不属于则跳过（未进行协议定义）
    public static void judgeMessageDateRouteType(String json) {
        EventBus.getDefault().post(new CommonNattyData(DISPLAY_UPDATE_DATA_ROUTE, json));
    }

    /**
     * ***********************************************************************************************
     */
    public static void judgeMessageRunTimeType(String json) {

    }

    public static void judgeMessageTurnType(String json) {

    }

    /**
     * ***********************************************************************************************
     */
    private static final int NO_SDCARD_NOTIFY = 1000;

    public static void handleVoiceMessage(final NattyClient client, final long fromId, final long gId, final int length) {
        /**
         * 通过Natty的引用找到这个文件的，并将文件保存下来
         *
         ntyNativePacketRecvResult fromId：13469
         ntyNativePacketRecvResult gId：240207489224233264
         ntyNativePacketRecvResult length：2182
         */
        if (length < 1) {
            //TODO 排除无效的文件
            return;
        }
        if (Integer.parseInt(BaseApplication.getInstances().getLoginInfo().getUserId()) == fromId) {
            //TODO 排除收到了自己的语音
            return;
        }
        //TODO 存在外存，则进行存储
        if (AudioFileFunc.isSdcardExit()) {
            //TODO 保存语音数据，存储到用户的外存
            //TODO 获取语音数据

            //TODO 向线程池中提交任务，由线程完成文件IO操作，并记录语音记录
            BaseApplication.getInstances().addThreadTask(new Runnable() {
                @Override
                public void run() {
                    try {
                        //*****************************************************************************************
                        final String savePath = saveVoiceFile(client,fromId,gId,length);
                        if(savePath==null){
                            return;//TODO 保存操作中出现异常，导致操作未正常完成则终止后续操作
                        }
                        // save record
                        final String device_id = Long.toHexString(gId);//watch device ID
                        //*****************************************************************************************
                        // 将语音记录保存到数据库中
                        getVoiceMessageLength(savePath,device_id,fromId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //TODO -- 保存记录
                }
            });
        } else {
            //TODO 否则发出通知，未检测到可用外存，无法接收语音数据
            NotificationUtil.getInstances(BaseApplication.getInstances()).
                    commonNotificationAlertOnce(BaseApplication.getInstances(),
                            BaseApplication.getInstances().getString(R.string.hint_no_storage_title),
                            BaseApplication.getInstances().getString(R.string.hint_no_storage_content),
                            null, NO_SDCARD_NOTIFY);
        }
    }

    public static String saveVoiceFile(final NattyClient client, final long fromId, final long gId, final int length){
        //TODO
        try{
            byte[] temp = client.ntyGetVoiceBuffer();
            final long currentTime = System.currentTimeMillis();
            final String savePath = getReceiveFilePath(currentTime);
            //TODO 保存文件
            File saveFile = new File(savePath);
            if (!saveFile.exists()) {
                saveFile.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
            fileOutputStream.write(temp, 0, length);
            fileOutputStream.flush();
            fileOutputStream.close();
            return savePath;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void getVoiceMessageLength(final String saveFile,final String device_id,final long fromId){
        try{
            final MediaPlayer player = new MediaPlayer();
            FileInputStream fileInputStream = new FileInputStream(saveFile);
            player.setDataSource(fileInputStream.getFD());
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.prepareAsync();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer player) {
                    int minute;
                    int seconds;
                    minute = player.getDuration() / 60000;
                    seconds = (player.getDuration() / 1000) % 60;
                    if (seconds < 1) {
                        seconds = 1;
                    }
                    //TODO 将文件的数据保存到数据库记录中
                    saveVoiceRecord(device_id,fromId,saveFile,player.getDuration());

                    saveVoiceRecordUnreadNumber(device_id);
                    //TODO 释放MediaPlayer资源
                    player.reset();
                    player.release();
                    player = null;
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void saveVoiceRecord(String device_id,long fromId,String saveFile,int length){
        MircoChatRecordEntity entity = new MircoChatRecordEntity();
        entity.setBelongDeviceIMEI(device_id);//IMEI号
        entity.setFromUserID(fromId+"");
        entity.setReceivedTimeStamp(System.currentTimeMillis());//收到的时间戳
        entity.setVoiceRecordLocalPath(saveFile);
        entity.setVoiceLength(length);//直接赋值语音长度
        entity.setUnreadFlag(BaseApplication.getInstances().getLoginInfo().getUserId()+ ICommonData.COMMON_UNREAD_VOICE_RECORD_SUFFIX);
        DaoManager.getInstances(BaseApplication.getInstances()).getDaoSession().getMircoChatRecordEntityDao().insert(entity);

        EventBus.getDefault().post(entity);//TODO 通知需要刷新语音数据的地方（语音微聊界面）
    }

    public static void saveVoiceRecordUnreadNumber(String device_id){
        //TODO 记录下这个IMEI号的未读消息数量
        List<MircoChatUnreadNumberRecordEntity> allData = DaoManager.getInstances(BaseApplication.getInstances()).
                getDaoSession().getMircoChatUnreadNumberRecordEntityDao().loadAll();
        MircoChatUnreadNumberRecordEntity currentUnread = null;
        //TODO 找出对应的
        for (MircoChatUnreadNumberRecordEntity temp:
                allData) {
            if(BaseApplication.getInstances().getLoginInfo().getUserId().equals(temp.getBelongUserID()) &&
                    device_id.equals(temp.getBelongDeviceIMEI())){
                //TODO 找出对应的
                currentUnread = temp;//
                int unreadNumber =  temp.getUnreadNumber();
                if(unreadNumber<0){
                    unreadNumber = 0;
                }
                unreadNumber++;
                temp.setUnreadNumber(unreadNumber);
                DaoManager.getInstances(BaseApplication.getInstances()).
                        getDaoSession().getMircoChatUnreadNumberRecordEntityDao().update(temp);
                break;
            }
        }
        //TODO 当数据库中尚未存在该用户该设备的记录时，重新创建一个然后插入进去
        if(currentUnread==null){
            currentUnread = new MircoChatUnreadNumberRecordEntity();
            currentUnread.setUnreadNumber(1);
            currentUnread.setBelongUserID(BaseApplication.getInstances().getLoginInfo().getUserId());
            currentUnread.setBelongDeviceIMEI(device_id);
            DaoManager.getInstances(BaseApplication.getInstances()).
                    getDaoSession().getMircoChatUnreadNumberRecordEntityDao().insert(currentUnread);

            EventBus.getDefault().post(currentUnread);//TODO 通知需要刷新未读数据的地方----首页--地图；手表首页----微聊
        }
    }

    private static final String COMMON_VOICE_FILE_SUFFIX = ".amr";

    public static String getReceiveFilePath(long currentTime) {
        File dir = BaseApplication.getInstances().getExternalFilesDir("voice");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String mAudioAMRPath = dir.getAbsolutePath() + File.separator + MessageDigestUtil.getSHA1String(currentTime + "") + COMMON_VOICE_FILE_SUFFIX;
        return mAudioAMRPath;
    }

}
