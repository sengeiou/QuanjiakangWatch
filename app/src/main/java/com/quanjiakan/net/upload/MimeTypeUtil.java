package com.quanjiakan.net.upload;

/**
 * Created by Administrator on 2017/8/14.
 */

public class MimeTypeUtil {

    //TODO 局限性，仅根据后缀判断文件的MIMEType并不是十分准确，若文件后缀名变更了，则会出现MIMETYPE与实际
    public static String getFileContentType(String nameOrPath) {
        if (nameOrPath == null || "".endsWith(nameOrPath) || nameOrPath.length() < 1) {
            return null;
        } else if (nameOrPath.lastIndexOf(".") < 0) {
            return MimeType.FILE_ANY.getContentType();
        }
        //TODO 在增加判别文件的类型时，需要手动增加
        String postfix = nameOrPath.substring(nameOrPath.lastIndexOf("."));
        switch (postfix) {
            case IPostFix.GIF:
                return MimeType.IMAGE_GIF.getContentType();
            case IPostFix.ICO:
                return MimeType.IMAGE_ICO.getContentType();
            case IPostFix.JFIF:
                return MimeType.IMAGE_JFIF.getContentType();
            case IPostFix.JPE:
                return MimeType.IMAGE_JPE.getContentType();
            case IPostFix.JPEG:
                return MimeType.IMAGE_JPEG.getContentType();
            case IPostFix.JPG:
                return MimeType.IMAGE_JPG.getContentType();
            case IPostFix.PNG:
                return MimeType.IMAGE_PNG.getContentType();
            case IPostFix.MID:
                return MimeType.AUDIO_MID.getContentType();
            case IPostFix.MIDI:
                return MimeType.AUDIO_MIDI.getContentType();
            case IPostFix.MP1:
                return MimeType.AUDIO_MP1.getContentType();
            case IPostFix.MP2:
                return MimeType.AUDIO_MP2.getContentType();
            case IPostFix.MP3:
                return MimeType.AUDIO_MP3.getContentType();
            case IPostFix.SND:
                return MimeType.AUDIO_SND.getContentType();
            case IPostFix.WAV:
                return MimeType.AUDIO_WAV.getContentType();
            case IPostFix.WMA:
                return MimeType.AUDIO_WMA.getContentType();
            default:
                return MimeType.FILE_ANY.getContentType();
        }
    }

    public static int getFileContentTypeIndex(String nameOrPath) {
        if (nameOrPath == null || "".endsWith(nameOrPath) || nameOrPath.length() < 1) {
            return MimeType.FILE_ANY.getIndex();
        } else if (nameOrPath.lastIndexOf(".") < 0) {
            return MimeType.FILE_ANY.getIndex();
        }
        //TODO 在增加判别文件的类型时，需要手动增加
        String postfix = nameOrPath.substring(nameOrPath.lastIndexOf("."));
        switch (postfix) {
            case IPostFix.GIF:
                return MimeType.IMAGE_GIF.getIndex();
            case IPostFix.ICO:
                return MimeType.IMAGE_ICO.getIndex();
            case IPostFix.JFIF:
                return MimeType.IMAGE_JFIF.getIndex();
            case IPostFix.JPE:
                return MimeType.IMAGE_JPE.getIndex();
            case IPostFix.JPEG:
                return MimeType.IMAGE_JPEG.getIndex();
            case IPostFix.JPG:
                return MimeType.IMAGE_JPG.getIndex();
            case IPostFix.PNG:
                return MimeType.IMAGE_PNG.getIndex();
            case IPostFix.MID:
                return MimeType.AUDIO_MID.getIndex();
            case IPostFix.MIDI:
                return MimeType.AUDIO_MIDI.getIndex();
            case IPostFix.MP1:
                return MimeType.AUDIO_MP1.getIndex();
            case IPostFix.MP2:
                return MimeType.AUDIO_MP2.getIndex();
            case IPostFix.MP3:
                return MimeType.AUDIO_MP3.getIndex();
            case IPostFix.SND:
                return MimeType.AUDIO_SND.getIndex();
            case IPostFix.WAV:
                return MimeType.AUDIO_WAV.getIndex();
            case IPostFix.WMA:
                return MimeType.AUDIO_WMA.getIndex();
            default:
                return MimeType.FILE_ANY.getIndex();
        }
    }

    public static String getFileContentType(String nameOrPath, IPostFix postFix, boolean isOnlyDoInterfaceMethod) {
        if (nameOrPath == null || "".endsWith(nameOrPath) || nameOrPath.length() < 1) {
            return null;
        } else if (nameOrPath.lastIndexOf(".") < 0) {
            return MimeType.FILE_ANY.getContentType();
        }
        //TODO 在增加判别文件的类型时，需要手动增加
        String postfix = nameOrPath.substring(nameOrPath.lastIndexOf("."));
        //TODO 增加扩展性，文件后缀与ContentType的映射对应处理全由调用者自己实现
        if(isOnlyDoInterfaceMethod){
            return postFix.getMimeTypeFromPostfix(nameOrPath);
        }
        switch (postfix) {
            case IPostFix.GIF:
                return MimeType.IMAGE_GIF.getContentType();
            case IPostFix.ICO:
                return MimeType.IMAGE_ICO.getContentType();
            case IPostFix.JFIF:
                return MimeType.IMAGE_JFIF.getContentType();
            case IPostFix.JPE:
                return MimeType.IMAGE_JPE.getContentType();
            case IPostFix.JPEG:
                return MimeType.IMAGE_JPEG.getContentType();
            case IPostFix.JPG:
                return MimeType.IMAGE_JPG.getContentType();
            case IPostFix.PNG:
                return MimeType.IMAGE_PNG.getContentType();
            case IPostFix.MID:
                return MimeType.AUDIO_MID.getContentType();
            case IPostFix.MIDI:
                return MimeType.AUDIO_MIDI.getContentType();
            case IPostFix.MP1:
                return MimeType.AUDIO_MP1.getContentType();
            case IPostFix.MP2:
                return MimeType.AUDIO_MP2.getContentType();
            case IPostFix.MP3:
                return MimeType.AUDIO_MP3.getContentType();
            case IPostFix.SND:
                return MimeType.AUDIO_SND.getContentType();
            case IPostFix.WAV:
                return MimeType.AUDIO_WAV.getContentType();
            case IPostFix.WMA:
                return MimeType.AUDIO_WMA.getContentType();
            default:
                if (postFix != null) {
                    //TODO 增加扩展性，文件后缀与ContentType的映射对应处理在除去已有的处理步骤之后由用户实现进行处理
                    return postFix.getMimeTypeFromPostfix(nameOrPath);
                } else {
                    return MimeType.FILE_ANY.getContentType();
                }
        }
    }
}
