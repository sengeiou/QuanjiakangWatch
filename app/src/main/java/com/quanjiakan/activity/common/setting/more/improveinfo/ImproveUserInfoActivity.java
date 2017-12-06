package com.quanjiakan.activity.common.setting.more.improveinfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.constants.ICommonActivityRequestCode;
import com.quanjiakan.constants.ICommonActivityResultCode;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.upload.UploadUtil;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.net_presenter.ModifyUserInfoPresenter;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.common.StringCheckUtil;
import com.quanjiakan.util.dialog.CommonDialog;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.dialog.IDialogCallback;
import com.quanjiakan.util.image.BitmapUtil;
import com.quanjiakan.util.image.IImageCropInterface;
import com.quanjiakan.util.image.ImageCropHandler;
import com.quanjiakan.util.image.ImageUtils;
import com.quanjiakan.util.widget.CircleTransformation;
import com.quanjiakan.view.dialog.BottomSelectImageDialog;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/20.
 */

public class ImproveUserInfoActivity extends BaseActivity {

    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_communicate)
    TextView tvCommunicate;
    @BindView(R.id.arrow_right)
    ImageView arrowRight;
    @BindView(R.id.head_image)
    ImageView headImage;
    @BindView(R.id.rl_image)
    RelativeLayout rlImage;
    @BindView(R.id.tv_fenge)
    TextView tvFenge;
    @BindView(R.id.tv_banben)
    TextView tvBanben;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.name_line)
    RelativeLayout nameLine;
    @BindView(R.id.comfirm)
    TextView comfirm;

    private ModifyUserInfoPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_modify_info);
        ButterKnife.bind(this);
//        BaseApplication.getInstances().getLoginInfo();
        initTitle();
        initView();
        setCurrentInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }
    /**
     * *************************************************************************************************
     */

    public void initTitle(){
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.setting_improve_user_info_title);

        presenter = new ModifyUserInfoPresenter();
    }

    public void initView(){
        tvCommunicate.setText(R.string.setting_improve_user_info_head_icon_name);
        tvBanben.setText(R.string.setting_improve_user_info_nick_name);
        comfirm.setText(R.string.setting_improve_user_info_save);
    }

    public void setCurrentInfo(){
        if(BaseApplication.getInstances().getLoginInfo().getNickName()!=null){
            tvNickName.setText(BaseApplication.getInstances().getLoginInfo().getNickName());
        }else{
            tvNickName.setText(BaseApplication.getInstances().getLoginInfo().getUserId());
        }

        if(BaseApplication.getInstances().getLoginInfo().getTempHeadIcon()!=null &&
                BaseApplication.getInstances().getLoginInfo().getTempHeadIcon().toLowerCase().startsWith("http")){
            Picasso.with(this).load(BaseApplication.getInstances().getLoginInfo().getTempHeadIcon()).
                    transform(new CircleTransformation()).into(headImage);
        }else{
            headImage.setImageResource(R.drawable.touxiang_big_icon);
        }
    }

    /**
     * *************************************************************************************************
     */



    /**
     * ***********************************************************************************************************************************************
     */
    @Override
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD: {
                HashMap<String, String> params = new HashMap<>();
                return params;
            }
            case IPresenterBusinessCode.MODIFY_USER_INFO:{
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_MEMBERID,BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM,IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN,BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_MODIFY_NAME,tvNickName.getText().toString());
                params.put(IParamsName.PARAMS_MODIFY_HEAD_ICON,(headImage.getTag()==null? null:headImage.getTag().toString() ));
                return params;
            }
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD:
                getDialog(this, getString(R.string.hint_common_submit_file_image));//正在获取数据...
                break;
            case IPresenterBusinessCode.MODIFY_USER_INFO:
                getDialog(this, getString(R.string.hint_common_submit_data));//正在获取数据...
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD:
                break;
            case IPresenterBusinessCode.MODIFY_USER_INFO:
                break;
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD:
                afterUpload(result);
                break;
            case IPresenterBusinessCode.MODIFY_USER_INFO:
                afterModifySuccess(result);
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD:
                break;
            case IPresenterBusinessCode.MODIFY_USER_INFO:
                break;
        }
        if (errorMsg != null && errorMsg.toString().length() > 0) {
            CommonDialogHint.getInstance().showHint(this, errorMsg.toString());
        } else {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_net_request_fail));
        }
    }

    /**
     * ***********************************************************************************************************************************************
     *
     * 文件上传
     */

    private File file;
    private String mCurrentPhotoPath = null;
    protected void showImageSelectDialog() {
        BottomSelectImageDialog dialog = new BottomSelectImageDialog(this);
        dialog.setmCameraResultListener(new BottomSelectImageDialog.onCameraResultListener() {
            @Override
            public void OnFilePath(String path) {
                mCurrentPhotoPath = path;
            }
        });
        dialog.show();
    }

    //TODO 处理图片上传完成后的数据
    public void afterUpload(Object result) {
        if (result != null && result instanceof String) {
            //  {"code":"200","message":"http://picture.quanjiakan.com/quanjiakan/resources/chunyu/images/20171122115337_y8isoq1jzyi3gtvh28rt.png"}
            LogUtil.e("afterUpload:" + result.toString());
            if (result.toString() != null && !result.toString().equals("") && result.toString().toLowerCase().startsWith("{")) {
                //上传文件成功
                try {
                    JSONObject json = new JSONObject(result.toString());
                    if (json.has("code") && "200".equals(json.getString("code"))) {
                        /**
                         * 上传完成后获取的地址
                         * json.getString("message")
                         *
                         *
                         * 将地址传递到第二个页面
                         */
                        if (json.has("message")) {
                            Picasso.with(getApplicationContext()).load(json.get("message").toString())
                                    .transform(new CircleTransformation())
                                    .into(headImage);
                            headImage.setTag(json.get("message").toString());
                            LogUtil.e("得到了上传后的地址："+json.get("message").toString());
                        }else{
                            headImage.setTag(null);
                            LogUtil.e("上传失败!");
                        }
                    } else {
                        if(json.has("message") && json.getString("message")!=null){
                            CommonDialogHint.getInstance().showHint(this, json.getString("message"));
                        }else{
                            CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_common_submit_upload_fail));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    MobclickAgent.reportError(this, e);
                }

            } else {
                //文件上传失败
                CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_common_submit_upload_fail));
            }

            if (file.exists()) {
                file.delete();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ICommonActivityRequestCode.REQUEST_CODE_CAPTURE_CAMEIA:
                if (resultCode == RESULT_OK) {
                    if (mCurrentPhotoPath != null) {
                        ImageCropHandler.beginCrop(this, Uri.fromFile(new File(mCurrentPhotoPath)));
                        /**
                         * 是否进行裁剪：裁剪则进行上面的操作，否则，直接进行文件上传
                         */
                    }
                }
                break;
            case ICommonActivityRequestCode.REQUEST_PICK:
                if (resultCode == RESULT_OK) {
                    ImageCropHandler.beginCrop(this, data.getData());
                    /**
                     * sourcePath = ImageUtils.uri2Path(data, HealthInquiryFurtherAskActivity.this);
                     */
                }
            case ICommonActivityRequestCode.REQUEST_CROP:
                doCrop(requestCode, resultCode, data);
                break;
        }
    }

    //TODO 处理图片的裁剪
    public void doCrop(final int requestCode, final int resultCode, final Intent data) {
        ImageCropHandler.handleCrop(null, resultCode, data, new IImageCropInterface() {
            @Override
            public void getFilePath(String path) {
                mCurrentPhotoPath = path;
                //TODO 修正上
                // 传图片为 400*400的分辨率，而不是默认的160*160
                Bitmap smallBitmap = BitmapUtil.getSmallBitmap(mCurrentPhotoPath, 400, 400);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                smallBitmap.compress(Bitmap.CompressFormat.PNG, 25, baos);
                String filename = System.currentTimeMillis() + ".png";
                String file_path = ImageUtils.saveBitmapToStorage(filename, smallBitmap);
                file = new File(file_path);

                uploadImageFile(null,file_path.toString());
            }
        });
    }

    public void uploadImageFile(String name,String path){
        String url = "http://picture.quanjiakan.com:9080/familycare-binary/upload?&platform=2&project=1" +
                "&token=" + BaseApplication.getInstances().getLoginInfo().getToken() +
                "&memberId=" + BaseApplication.getInstances().getLoginInfo().getUserId() +
                "&storage=2";//TODO 这个需要---如果没有可能会报参数错误的异常

        HashMap<String, String> paramsFile = new HashMap<>();
        paramsFile.put("file", path);
        paramsFile.put("filename", file.getName());
        paramsFile.put("image", file.getAbsolutePath());
        UploadUtil.uploadFile(this, url, null, paramsFile);
    }

    /**
     * *************************************************************************************************
     */

    public void afterModifySuccess(Object result) {
        if (result != null && result instanceof String) {
            if (result != null && !result.equals("") && result.toString().toLowerCase().startsWith("{")) {
                //上传文件成功
                try {
                    JSONObject json = new JSONObject(result.toString());
                    if (json.has("code") && "200".equals(json.getString("code"))) {

                        BaseApplication.getInstances().getLoginInfo().setNickName(tvNickName.getText().toString());
                        if(headImage.getTag()!=null && headImage.getTag().toString().toLowerCase().startsWith("http")){
                            BaseApplication.getInstances().getLoginInfo().setTempHeadIcon(headImage.getTag().toString());
                        }

                        CommonDialog.getInstance().getCommonConfirmDialog(this, getString(R.string.hint_common_hint_title), getString(R.string.hint_common_update_success), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setResult(ICommonActivityResultCode.REQUEST_TO_IMPROVE_USER_INFO);
                                finish();
                            }
                        });
                        return;
                    }else{
                        if(json.has("message")){
                            onError(IPresenterBusinessCode.MODIFY_USER_INFO,200,json.getString("message"));
                        }else{
                            onError(IPresenterBusinessCode.MODIFY_USER_INFO,200,null);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void modifyName(){
        CommonDialog.getInstance().getCommonModifyDialog(this, getString(R.string.setting_improve_user_info_name_dialog_title),
                getString(R.string.setting_improve_user_info_name_dialog_hint), new IDialogCallback() {
            @Override
            public void getString(String message) {
                if(message==null || message.trim().length()<1){
                    CommonDialogHint.getInstance().showHint(ImproveUserInfoActivity.this,
                            ImproveUserInfoActivity.this.getString(R.string.setting_improve_user_info_name_dialog_invaild_name));
                }else if(!StringCheckUtil.isChar(message)){//检查是否存在特殊字符
                    CommonDialogHint.getInstance().showHint(ImproveUserInfoActivity.this,
                            ImproveUserInfoActivity.this.getString(R.string.setting_improve_user_info_name_dialog_contain_specfic));
                }else{
                    tvNickName.setText("" + message);
                }
            }
        });
    }

    /**
     * *************************************************************************************************
     */

    public boolean isValid(){
        //TODO 目前，接口必须要传两个参数
        if(headImage.getTag()==null && BaseApplication.getInstances().getLoginInfo().getNickName().equals(tvNickName.getText().toString())){
            CommonDialogHint.getInstance().showHint(ImproveUserInfoActivity.this,
                    ImproveUserInfoActivity.this.getString(R.string.setting_improve_user_info_name_submit_no_modify));
            return false;
        }

        if(headImage.getTag()==null || BaseApplication.getInstances().getLoginInfo().getNickName().equals(tvNickName.getText().toString())){
            CommonDialogHint.getInstance().showHint(ImproveUserInfoActivity.this,
                    ImproveUserInfoActivity.this.getString(R.string.setting_improve_user_info_name_submit_all_modify));
            return false;
        }
        return true;
    }

    @OnClick({R.id.ibtn_back, R.id.rl_image, R.id.name_line, R.id.comfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.rl_image:
                showImageSelectDialog();
                break;
            case R.id.name_line:
                modifyName();
                break;
            case R.id.comfirm:
                if(isValid()){
                    presenter.modifyUserInfo(this);
                }
                break;
        }
    }

    /**
     * *************************************************************************************************
     */
}
