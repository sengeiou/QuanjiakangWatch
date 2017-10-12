package com.quanjiakan.util.common;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 计算得到数据的签名的工具类
 */
public class MessageDigestUtil {

    /**
     * 声明需要用到的签名字符
     */
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 声明摘要编码类型
     */
    public static final String ENCODE = "utf-8";

    /**
     * 声明预设的几种签名类型
     */
    public static final String TYPE_MD5 = "MD5";
    public static final String TYPE_SHA1 = "SHA-1";
    public static final String TYPE_SHA256 = "SHA-256";
    public static final String TYPE_SHA512 = "SHA-512";

    /**
     * 声明特定的消息摘要类型，与公共类型
     */
    private static MessageDigest messageDigest = null;
    private static MessageDigest sha1Digest = null;
    private static MessageDigest commonDigest = null;

    /**
     * 初始化摘要工具
     */
    static {
        try {
            messageDigest = MessageDigest.getInstance(TYPE_MD5);
            sha1Digest = MessageDigest.getInstance(TYPE_SHA1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * ******************************************************************************************************************
     * 获取 指定 类型的摘要
     */

    public static String getSignatureString(String source, String type){
        try {
            commonDigest = MessageDigest.getInstance(type);
            return getTypeString(source.getBytes(ENCODE));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTypeString(byte[] bytes) {
        commonDigest.update(bytes);
        return bytesToHex(commonDigest.digest());
    }

    /**
     * ******************************************************************************************************************
     * 获取SHA1类型的摘要
     */

    public static String getSHA1String(String str) {
        return getSHA1String(str.getBytes());
    }

    public static String getSHA1String(byte[] bytes) {
        sha1Digest.update(bytes);
        return bytesToHex(sha1Digest.digest());
    }

    /**
     * ******************************************************************************************************************
     * 获取SHA1类型的摘要
     */

    public static String getMD5String(String str) {
        return getMD5String(str.getBytes());
    }

    public static String getMD5String(byte[] bytes) {
        messageDigest.update(bytes);
        return bytesToHex(messageDigest.digest());
    }

    /**
     * ******************************************************************************************************************
     */

    /**
     * 获取摘要数据
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte bytes[]) {
        return bytesToHex(bytes, 0, bytes.length);
    }

    public static String bytesToHex(byte bytes[], int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < start + end; i++) {
            sb.append(byteToHex(bytes[i]));
        }
        return sb.toString();
    }

    public static String byteToHex(byte bt) {
        return HEX_DIGITS[(bt & 0xf0) >> 4] + "" + HEX_DIGITS[bt & 0xf];
    }

    /**
     * 将数据进行Base64编码
     *
     * @param enco
     * @return
     */
    public static String toBase64(String enco){
        String result = "";
        try {
            result = Base64.encodeToString(enco.getBytes("utf-8"), Base64.URL_SAFE).replace("\n","");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}  
