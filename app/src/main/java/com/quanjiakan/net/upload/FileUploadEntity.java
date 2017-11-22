package com.quanjiakan.net.upload;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/22.
 */

public class FileUploadEntity implements Serializable{

    /**
     * code : 200
     * message : http://picture.quanjiakan.com/quanjiakan/resources/chunyu/images/20171122115337_y8isoq1jzyi3gtvh28rt.png
     */

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
