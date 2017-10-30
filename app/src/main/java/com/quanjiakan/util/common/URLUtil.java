package com.quanjiakan.util.common;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/10/30.
 */

public class URLUtil {
    private static final String DEFAULT_ENCODE_TYPE = "utf-8";

    public static String urlEncode(String msg) throws UnsupportedEncodingException {
        return URLEncoder.encode(msg,DEFAULT_ENCODE_TYPE);
    }

    public static String urlDecode(String msg) throws UnsupportedEncodingException {
        return URLDecoder.decode(msg,DEFAULT_ENCODE_TYPE);
    }
}
