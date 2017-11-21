package com.quanjiakan.net.upload;

import android.content.Context;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/21.
 */

public class UploadUtil {

    public static void uploadFile(final BaseActivity activity,final  String serverUploadUrlPath, final Map<String, String> paramMap, final Map<String, String> fileMap){
        activity.showMyDialog(IPresenterBusinessCode.COMMON_FILE_UPLOAD);
        BaseApplication.getInstances().addThreadTask(new Runnable() {
            @Override
            public void run() {
                final String result = formUpload(activity,serverUploadUrlPath,paramMap,fileMap);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.dismissDialog();
                        activity.onSuccess(IPresenterBusinessCode.COMMON_FILE_UPLOAD,200,result);
                    }
                });
            }
        });
    }

    private static String formUpload(Context context,String urlStr, Map<String, String> paramMap, Map<String, String> fileMap) {
        String res = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "---------------------------123821742118716"; //boundary就是request头和上传文件内容的分隔符
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            if (fileMap != null) {
                Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = entry.getKey();
                    String inputValue = entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    if(file.exists()){
                        String filename = file.getName();
                        String contentType = MimeTypeUtil.getFileContentType(filename);
                        int contentTypeIndex = MimeTypeUtil.getFileContentTypeIndex(filename);
                        StringBuffer strBuf = new StringBuffer();
                        strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                        if(MimeType.FILE_ANY.getIndex()>contentTypeIndex){
                            strBuf.append("Content-Disposition: form-data; name=\"image\"; filename=\"" + filename + "\"\r\n");
                        }else{
                            strBuf.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + filename + "\"\r\n");
                        }
                        strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                        out.write(strBuf.toString().getBytes());
                        //TODO 写入文件数据
                        FileInputStream in = new FileInputStream(file);
                        int bytes = 0;
//                        int sum = 0;
                        byte[] bufferOut = new byte[4096];
                        while ((bytes = in.read(bufferOut)) != -1) {
                            out.write(bufferOut, 0, bytes);
                            //TODO 上传进度
//                            sum+=bytes;
                        }
                        in.close();
                    }
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            conn.connect();
            if(conn!=null && conn.getResponseCode()==HttpURLConnection.HTTP_OK) {
                if (conn != null && conn.getContentLength() >= 0) {
                    byte[] cache = new byte[conn.getContentLength()];
                    InputStream inputStream = conn.getInputStream();
                    inputStream.read(cache);
                    res = new String(cache, "UTF-8");
                } else {
                    return "{\"code\":\"500\",\"message\":\""+context.getString(R.string.hint_common_upload_file_fail)+"\"}";
                }
            }else{
                if(conn!=null){
                    return "{\"code\":\"500\",\"message\":\""+context.getString(R.string.hint_common_upload_file_code)+""+conn.getResponseCode()+"\"}";
                }else{
                    return "{\"code\":\"500\",\"message\":\""+context.getString(R.string.hint_common_upload_file_server_denied)+"\"}";
                }
            }
        } catch (Exception e) {
            if(e instanceof ConnectException){
                return "{\"code\":\"500\",\"message\":\""+context.getString(R.string.hint_common_upload_file_server_denied)+"\"}";
            }else{
                return "{\"code\":\"500\",\"message\":\""+context.getString(R.string.hint_common_upload_file_server_denied)+"\"}";
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }

    private static String formUpload(final BaseActivity context,String urlStr, Map<String, String> paramMap, Map<String, String> fileMap,final IUploadProgress uploadProgress) {
        String res = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "---------------------------123821742118716"; //boundary就是request头和上传文件内容的分隔符
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            if (fileMap != null) {
                Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = entry.getKey();
                    String inputValue = entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    if(file.exists()){
                        String filename = file.getName();
                        String contentType = MimeTypeUtil.getFileContentType(filename);
                        int contentTypeIndex = MimeTypeUtil.getFileContentTypeIndex(filename);
                        StringBuffer strBuf = new StringBuffer();
                        strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                        if(MimeType.FILE_ANY.getIndex()>contentTypeIndex){
                            strBuf.append("Content-Disposition: form-data; name=\"image\"; filename=\"" + filename + "\"\r\n");
                        }else{
                            strBuf.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + filename + "\"\r\n");
                        }
                        strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                        out.write(strBuf.toString().getBytes());
                        //TODO 写入文件数据
                        FileInputStream in = new FileInputStream(file);
                        int bytes = 0;
                        int sum = 0;
                        byte[] bufferOut = new byte[4096];
                        while ((bytes = in.read(bufferOut)) != -1) {
                            out.write(bufferOut, 0, bytes);
                            //TODO 上传进度 ****************************************
                            if(uploadProgress!=null) {
                                sum += bytes;
                                final float progress =  sum / ( file.length()*1f);
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        uploadProgress.progress(progress);
                                    }
                                });
                            }
                            //TODO 上传进度 ****************************************
                        }
                        in.close();
                    }
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            conn.connect();
            if(conn!=null && conn.getResponseCode()==HttpURLConnection.HTTP_OK) {
                if (conn != null && conn.getContentLength() >= 0) {
                    byte[] cache = new byte[conn.getContentLength()];
                    InputStream inputStream = conn.getInputStream();
                    inputStream.read(cache);
                    res = new String(cache, "UTF-8");
                } else {
                    return "{\"code\":\"500\",\"message\":\""+context.getString(R.string.hint_common_upload_file_fail)+"\"}";
                }
            }else{
                if(conn!=null){
                    return "{\"code\":\"500\",\"message\":\""+context.getString(R.string.hint_common_upload_file_code)+""+conn.getResponseCode()+"\"}";
                }else{
                    return "{\"code\":\"500\",\"message\":\""+context.getString(R.string.hint_common_upload_file_server_denied)+"\"}";
                }
            }
        } catch (Exception e) {
            if(e instanceof ConnectException){
                return "{\"code\":\"500\",\"message\":\""+context.getString(R.string.hint_common_upload_file_server_denied)+"\"}";
            }else{
                return "{\"code\":\"500\",\"message\":\""+context.getString(R.string.hint_common_upload_file_server_denied)+"\"}";
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }
}
