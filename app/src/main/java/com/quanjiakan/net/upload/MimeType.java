package com.quanjiakan.net.upload;

/**
 * Created by Administrator on 2017/8/14.
 */

public enum MimeType {
    IMAGE_GIF("image/gif",1),
    IMAGE_ICO("image/x-icon",2),
    IMAGE_JFIF("image/jpeg",3),
    IMAGE_JPE("image/jpeg",4),
    IMAGE_JPEG("image/jpeg",5),
    IMAGE_JPG("image/jpeg",6),
    IMAGE_PNG("image/png",7),
    FILE_ANY("application/octet-stream",8),
    AUDIO_MP3("audio/mp3",9),
    AUDIO_MP2("audio/mp2",10),
    AUDIO_MP1("audio/mp1",11),
    AUDIO_MIDI("audio/mid",12),
    AUDIO_MID("audio/mid",13),
    AUDIO_SND("audio/basic",14),
    AUDIO_WMA("audio/x-ms-wma",15),
    AUDIO_WAV("audio/wav",16);

    private String contentType;
    private int index;

    MimeType(String s, int i) {
        this.contentType = s;
        this.index = i;
    }

    public String getContentType(){
        return contentType;
    }

    public int getIndex() {
        return index;
    }
}
