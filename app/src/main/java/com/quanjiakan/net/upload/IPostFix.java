package com.quanjiakan.net.upload;

/**
 * Created by Administrator on 2017/8/14.
 */

public interface IPostFix {
    String GIF = ".gif";
    String ICO = ".ico";
    String JFIF = ".jfif";
    String JPE = ".jpe";
    String JPEG = ".jpeg";
    String JPG = ".jpg";
    String PNG = ".png";
    String MP3 = ".mp3";
    String MP2 = ".mp2";
    String MP1 = ".mp1";
    String MIDI = ".midi";
    String MID = ".mid";
    String SND = ".snd";
    String WMA = ".wma";
    String WAV = ".wav";

    //TODO 使用接口来扩展使用的方式
    String getMimeTypeFromPostfix(String postfix);
}
