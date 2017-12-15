package com.quanjiakan.activity.common.setting.healthdocument;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.constants.ICommonActivityRequestCode;
import com.quanjiakan.constants.ICommonActivityResultCode;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.format.CommonResultEntity;
import com.quanjiakan.net.upload.UploadUtil;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.net_presenter.UserHealthDocumentListPresenter;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.common.SerializeToObjectUtil;
import com.quanjiakan.util.dialog.CommonDialog;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.image.BitmapUtil;
import com.quanjiakan.util.image.IImageCropInterface;
import com.quanjiakan.util.image.ImageCropHandler;
import com.quanjiakan.util.image.ImageUtils;
import com.quanjiakan.view.dialog.BottomSelectImageDialog;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/20.
 */

public class UserHealthDocumentAddActivity extends BaseActivity {


    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.publish_time)
    TextView publishTime;
    @BindView(R.id.headimg)
    ImageView image;
    @BindView(R.id.input)
    EditText input;
    @BindView(R.id.input_line)
    LinearLayout inputLine;
    @BindView(R.id.input_hint)
    TextView inputHint;
    @BindView(R.id.tv_book)
    TextView tvBook;

    /**
     ********************************************************************************
     */

    private UserHealthDocumentListPresenter presenter;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_health_document_add);
        ButterKnife.bind(this);

        initTitle();
        initView();
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
     ********************************************************************************
     */

    public void initTitle(){
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setText(R.string.setting_user_health_document_menu_add);

        presenter = new UserHealthDocumentListPresenter();
    }

    public void initView(){
        Date  publishData= new Date();
        publishTime = (TextView) findViewById(R.id.publish_time);
        publishTime.setText(getString(R.string.setting_user_health_document_publish_time_prefix) + sdf.format(publishData));

        //TODO 限制文字长度
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s != null && s.length() > 100) {
                    CommonDialogHint.getInstance().showHint(UserHealthDocumentAddActivity.this,getString(R.string.setting_user_health_document_word_limit));
                    s = s.subSequence(0, 100);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputHint.setText(R.string.setting_user_health_document_word_limit_hint);
    }

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

    /**
     ********************************************************************************
     */

    public void submit(){
        if(image.getTag()==null || !image.getTag().toString().toLowerCase().startsWith(ICommonData.HTTP_PREFIX)){
            CommonDialogHint.getInstance().showHint(this,getString(R.string.setting_user_health_document_image_hint));
            return ;
        }
        if(input.getText().length()<1){
            CommonDialogHint.getInstance().showHint(this,getString(R.string.setting_user_health_document_word_hint));
            return ;
        }
        presenter.addUserHealthDocumentInfo(this);
    }

    public void setResult(Object result){
        if(result!=null && result instanceof String){
            CommonResultEntity entity = (CommonResultEntity) SerializeToObjectUtil.getInstances().jsonToObject(result.toString(),
                    new TypeToken<CommonResultEntity>(){}.getType());
            if(entity!=null && ICommonData.HTTP_OK.equals(entity.getCode())){
                CommonDialogHint.getInstance().showHint(this, getString(R.string.setting_user_health_document_success), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(ICommonActivityResultCode.RELOAD_DATA);
                        finish();
                    }
                });
            }else{
                CommonDialogHint.getInstance().showHint(this, getString(R.string.setting_user_health_document_fail));
            }
        }
    }

    /**
     ********************************************************************************
     */


    @Override
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD: {
                HashMap<String, String> params = new HashMap<>();
                return params;
            }
            case IPresenterBusinessCode.HEALTH_DOCUMENT_USER_ADD: {
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_MEMBERID,BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_USER_HEALTH_DOCUMENT_NAME,input.getText().toString());
                params.put(IParamsName.PARAMS_USER_HEALTH_DOCUMENT_IMAGE,image.getTag().toString());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN,BaseApplication.getInstances().getLoginInfo().getToken());
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
            case IPresenterBusinessCode.HEALTH_DOCUMENT_USER_ADD:
                getDialog(this, getString(R.string.hint_common_submit_data));//正在获取数据...
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD:
                break;
            case IPresenterBusinessCode.HEALTH_DOCUMENT_USER_ADD:
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
            case IPresenterBusinessCode.HEALTH_DOCUMENT_USER_ADD:
                setResult(result);
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD:
                break;
            case IPresenterBusinessCode.HEALTH_DOCUMENT_USER_ADD:
                break;
        }
        if (errorMsg != null && errorMsg.toString().length() > 0) {
            CommonDialogHint.getInstance().showHint(this, errorMsg.toString());
        } else {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_net_request_fail));
        }
    }

    @OnClick({R.id.ibtn_back, R.id.headimg, R.id.tv_book})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.headimg:
                showImageSelectDialog();
                break;
            case R.id.tv_book:
                submit();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ICommonActivityRequestCode.BIND_STEP_TWO_NET_3:{
                break;
            }
            case ICommonActivityRequestCode.BIND_STEP_TWO_NET_4:{
                break;
            }
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
                "&storage=15";//TODO 这个需要---如果没有可能会报参数错误的异常

        HashMap<String, String> paramsFile = new HashMap<>();
        paramsFile.put("file", path);
        paramsFile.put("filename", file.getName());
        paramsFile.put("image", file.getAbsolutePath());
        UploadUtil.uploadFile(this, url, null, paramsFile);
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
//                                    .transform(new CircleTransformation())
                                    .into(image);
                            image.setTag(json.get("message").toString());
                        }else{
                            image.setTag(null);
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
}
