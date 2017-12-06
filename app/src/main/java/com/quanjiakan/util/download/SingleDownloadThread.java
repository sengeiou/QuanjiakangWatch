package com.quanjiakan.util.download;

import android.content.Context;
import android.net.Uri;
import android.os.Message;

import com.quanjiakan.net.download.UpdateUtil;
import com.quanjiakan.util.common.MessageDigestUtil;
import com.quanjiakan.util.common.NetTypeCheckUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class SingleDownloadThread extends Thread {

    private final String METHOD_GET = "GET";

    private IDownloadProgressCallback callback;
    private int currentNetworkType;
    private int sumThreadNumber;
    private int threadNumber;
    private long sumSize;
    private long start;
    private long end;
    private long currentPosition;
    private boolean breakPointFlag = false;
    private String savePath;
    private String url_string;
    private String versionName;

    private Context mContext;
    private boolean isAllowMobileNetwork;

    public SingleDownloadThread(Context mContext, String filePath, long fileSumSize, int sumThreadNumber, int threadNumber,
                                long startPosition, long endPosition, String localSavePath, IDownloadProgressCallback callback){
        this.mContext = mContext;
        url_string = filePath;
        sumSize = fileSumSize;
        this.sumThreadNumber = sumThreadNumber;
        this.threadNumber = threadNumber;
        start = startPosition;
        end = endPosition;
        currentPosition = startPosition;
        this.callback = callback;
        savePath = localSavePath;

        isAllowMobileNetwork = false;
    }

    public SingleDownloadThread(Context mContext, String filePath,String versionName, long fileSumSize, int sumThreadNumber, int threadNumber,
                                long startPosition, long endPosition, String localSavePath, IDownloadProgressCallback callback){
        this.mContext = mContext;
        url_string = filePath;
        sumSize = fileSumSize;
        this.sumThreadNumber = sumThreadNumber;
        this.threadNumber = threadNumber;
        start = startPosition;
        end = endPosition;
        currentPosition = startPosition;
        this.callback = callback;
        savePath = localSavePath;

        isAllowMobileNetwork = false;
    }

    public void setBreakPointFlag(boolean flag){
        breakPointFlag = flag;
    }

    public long getDownloadLength(){
        return currentPosition - start;
    }

    protected HttpURLConnection createConnection(String url) throws IOException {
        String encodedUrl = Uri.encode(url, "@#&=*+-_.,:!?()/~\'%");
        HttpURLConnection conn = (HttpURLConnection)(new URL(encodedUrl)).openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(20000);
        return conn;
    }

    @Override
    public void run() {
//        try {
//            URL url = new URL(url_string);
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//            httpURLConnection.setRequestMethod(METHOD_GET);
//            httpURLConnection.setDoInput(true);
////            httpURLConnection.setDoOutput(true);
//            httpURLConnection.setConnectTimeout(5000);
//            httpURLConnection.addRequestProperty("Range", "bytes="+start+"-"+end);
//            httpURLConnection.connect();
//            int code = httpURLConnection.getResponseCode();
//            for(int redirectCount = 0; code / 100 == 3 && redirectCount < 5; ++redirectCount) {
//                httpURLConnection = this.createConnection(httpURLConnection.getHeaderField("Location"));
//                code = httpURLConnection.getResponseCode();
//            }
//
//
//            if(code==HttpURLConnection.HTTP_OK || code==HttpURLConnection.HTTP_PARTIAL){
//                int streamSize = httpURLConnection.getContentLength();
//                if(streamSize>=sumSize){
//                    /**
//                     * 不支持多线程下载
//                     */
//                    Log.e("LOGUTIL","不支持多线程下载");
//                    if(threadNumber>0){
//                        httpURLConnection.getInputStream().close();
//                        httpURLConnection.disconnect();
//                        return;
//                    }else{
//                        start = 0;
//                        end = httpURLConnection.getContentLength();
//                    }
//                }else{
//                    /**
//                     * 支持多线程下载
//                     */
//                    Log.e("LOGUTIL","支持多线程下载");
//                }
//
//                File tempFile = new File(savePath);
//                RandomAccessFile file = new RandomAccessFile(savePath,"rwd");
//                if(!tempFile.exists()){
//                    tempFile.createNewFile();
//                    file.setLength(sumSize);
//                }
//                file.seek(currentPosition);
//
//
//                InputStream inputStream = httpURLConnection.getInputStream();
//                /**
//                 * 若需要断点续传，则需要保存当前
//                 */
//                byte[] cache = new byte[4096];//缓存
//                int len = 0;
//                long tempSum = 0;
//
//                while((len = inputStream.read(cache))!=-1){
//
//                    currentPosition+=len;
//                    tempSum+=len;
//                    file.write(cache,0,len);
//                    if(callback!=null){
//                        callback.threadProgress(threadNumber,tempSum);
//                    }
//                }
//
//                file.close();
//                inputStream.close();
//            }else{
//                Log.e("LOGUTIL","---"+httpURLConnection.getResponseCode());
//            }
//
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        download();
    }

    public void download() {
        RandomAccessFile file = null;
        InputStream inputStream = null;
        try {
            currentNetworkType = NetTypeCheckUtil.checkNetworkType(mContext);
            String tempInfo = UpdateUtil.getFileInfoValue(mContext, MessageDigestUtil.getSHA1String(url_string+((versionName!=null && versionName.length()>0)?
                    versionName : "")+sumSize+"_"+threadNumber));//TODO 在保存和获取断点数据时，必须根据子线程来，所以Key中必须带有子线程编号
            if(tempInfo!=null &&//TODO 不为空
                    !"".equals(tempInfo) && //TODO 不为默认值
                    tempInfo.split(UpdateUtil.SPLIT).length==6){//长度为设置的长度
                currentPosition = Long.parseLong(tempInfo.split(UpdateUtil.SPLIT)[5]);
//                Log.e("MainA"+threadNumber,"threadNumber:"+threadNumber+"\n" +
//                        "currentPosition:"+currentPosition+"\n" +
//                        "----------start:"+start+"\n" +
//                        "************end:"+end);
                if(currentPosition<start){
                    currentPosition=start;
                }else if(currentPosition>=end){
                    currentPosition = end;
                    return;
                }else{
//                    currentPosition=start;//TODO 这里不要进行这个操作，否则当重新启动时，上一次的进度会因为这个操作而被覆盖掉（加上这句相当于重新下载）
                    //TODO 对于需要定制的话，可以考虑在这里加上一个boolean标志，通过判断这个标志来确定是否需要进行重新下载
                }
            }else{
//                Log.e("MainA"+threadNumber,
//                        "threadNumber:"+threadNumber+"\n" +
//                        "currentPosition:"+currentPosition+"\n" +
//                        "----------start:"+start+"\n" +
//                        "++++++++++++end:"+end);
            }
            URL url = new URL(url_string);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(METHOD_GET);
            httpURLConnection.setDoInput(true);
//            httpURLConnection.setDoOutput(true);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.addRequestProperty("Range", "bytes=" + currentPosition + "-" + end);
            httpURLConnection.connect();

            /**
             * TODO 若地址出现重定向，则在进行有限次数的重定向处理
             */
            int code = httpURLConnection.getResponseCode();
            for (int redirectCount = 0; code / 100 == 3 && redirectCount < 5; ++redirectCount) {
                httpURLConnection = this.createConnection(httpURLConnection.getHeaderField("Location"));
                code = httpURLConnection.getResponseCode();
            }
            if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_PARTIAL) {
                int streamSize = httpURLConnection.getContentLength();
                if (streamSize >= sumSize) {
                    /**
                     * 不支持多线程下载
                     */
                    if (threadNumber > 0) {
                        httpURLConnection.getInputStream().close();
                        httpURLConnection.disconnect();
                        return;
                    } else {
                        start = 0;
                        end = httpURLConnection.getContentLength();
                    }
                } else {
                    /**
                     * 支持多线程下载
                     */
                }

                File tempFile = new File(savePath);
                file = new RandomAccessFile(savePath, "rwd");
                if (!tempFile.exists()) {
                    tempFile.createNewFile();
                    file.setLength(sumSize);
                }
                file.seek(currentPosition);


                inputStream = httpURLConnection.getInputStream();
                /**
                 * 若需要断点续传，则需要保存当前
                 */
                byte[] cache = new byte[4096];//缓存
                int len = 0;
                long tempSum = 0;

                while ((len = inputStream.read(cache)) != -1) {
                    //TODO 保存断点规则：Key = sha1(url+文件总大小)
                    //TODO 保存断点规则：Value = 子线程总数;当前线程编号;文件总大小;起点位置;终点位置;当前位置
                    currentPosition += len;
                    tempSum += len;
                    file.write(cache, 0, len);
                    if (callback != null) {
                        callback.threadProgress(threadNumber, tempSum);
                    }
                    //TODO 记录下当前线程的断点
                    UpdateUtil.setFileInfoValue(mContext,MessageDigestUtil.getSHA1String(url_string+((versionName!=null && versionName.length()>0)?
                                    versionName : "")+sumSize+"_"+threadNumber),
                            sumThreadNumber+UpdateUtil.SPLIT+threadNumber+UpdateUtil.SPLIT+
                                    sumSize+UpdateUtil.SPLIT+start+UpdateUtil.SPLIT+
                                    end+UpdateUtil.SPLIT+currentPosition
                    );
                    //TODO 添加控制，避免一直下载
                    if(currentPosition>=end){
                        break;
                    }
                    //TODO 降低Log的输出速度
//                    try {
//                        Thread.sleep(250);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            } else {
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
//            Log.e("MainA"+threadNumber, "MalformedURLException   download " + threadNumber + "      " + url_string);
        } catch (IOException e) {
            e.printStackTrace();
//            Log.e("MainA"+threadNumber, "IOException   download " + threadNumber + "      " + url_string);
            if (currentNetworkType != NetTypeCheckUtil.checkNetworkType(mContext) ||
                    NetTypeCheckUtil.TYPE_WIFI==NetTypeCheckUtil.checkNetworkType(mContext) ||
                    (isAllowMobileNetwork && NetTypeCheckUtil.TYPE_MOBILE== NetTypeCheckUtil.checkNetworkType(mContext))) {
                //TODO 当网络环境改变
                download();
                try {
                    if (file != null) {
                        file.close();
                        file = null;
                    }
                    if (inputStream != null) {
                        inputStream.close();
                        inputStream = null;
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                return;
            } else {
//                Log.e("MainA"+threadNumber, "IOException   download  undo    " + NetCheckUtil.checkNetworkType(mContext) +"   currentNetworkType:"+ currentNetworkType);
            }
        } finally {
            try {
                if (file != null) {
                    file.close();
                    file = null;
                }
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
