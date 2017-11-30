package com.quanjiakan.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.util.image.IImageCropInterface;
import com.quanjiakan.util.image.ImageCropHandler;

/**
 * 作者：Administrator on 2017/9/26 14:54
 * <p> 底部弹出的用于选择图片的dialog
 * 邮箱：liuzj@hi-board.com
 */


public class BottomSelectImageDialog extends Dialog implements View.OnClickListener {

    private Activity activity;

    public BottomSelectImageDialog(Activity activity) {
        super(activity, R.style.ShareDialog);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_modify_info_new);
        initView();
    }

    private void initView() {
        TextView camera = (TextView) findViewById(R.id.camera);/* 相机*/
        TextView album = (TextView) findViewById(R.id.album); /* 相册*/
        TextView cancel = (TextView) findViewById(R.id.cancel);

        camera.setOnClickListener(this);
        album.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.camera:
                dismiss();
                //从相机拍摄照片
                ImageCropHandler.getImageFromCamera(activity, new IImageCropInterface() {
                    @Override
                    public void getFilePath(String path) {
                        mCameraResultListener.OnFilePath(path);
                    }
                });
                break;

            case R.id.album :
                dismiss();
                //从相册选取照片
                ImageCropHandler.pickImage(activity);
                break;

            case R.id.cancel:
                dismiss();
                break;

        }
    }

    public interface onCameraResultListener {
        void OnFilePath(String path);
    }

    private onCameraResultListener mCameraResultListener;

    public void setmCameraResultListener(onCameraResultListener mCameraResultListener) {
        this.mCameraResultListener = mCameraResultListener;
    }
}
