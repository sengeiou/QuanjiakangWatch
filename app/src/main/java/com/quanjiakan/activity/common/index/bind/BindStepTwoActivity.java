package com.quanjiakan.activity.common.index.bind;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.ICommonActivityRequestCode;
import com.quanjiakan.activity.common.setting.healthinquiry.HealthInquiryFurtherAskActivity;
import com.quanjiakan.constants.IParamsIntValue;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.retrofit.result_entity.PostCheckIMEIEntity;
import com.quanjiakan.net_presenter.BindStepTwoPresenter;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.image.BitmapUtil;
import com.quanjiakan.util.image.IImageCropInterface;
import com.quanjiakan.util.image.ImageCropHandler;
import com.quanjiakan.util.image.ImageUtils;
import com.quanjiakan.view.dialog.BottomSelectImageDialog;
import com.umeng.analytics.MobclickAgent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/30.
 */

public class BindStepTwoActivity extends BaseActivity {

    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.device_type_name)
    TextView deviceTypeName;
    @BindView(R.id.nickname_line)
    LinearLayout nicknameLine;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.image_hint)
    TextView imageHint;
    @BindView(R.id.imageblock)
    RelativeLayout imageblock;
    @BindView(R.id.bind_device_id)
    TextView bindDeviceId;
    @BindView(R.id.bind_device_2dcode_value)
    EditText deviceNickName;
    @BindView(R.id.inputline)
    RelativeLayout inputline;
    @BindView(R.id.relation_hint)
    TextView relationHint;
    @BindView(R.id.relation_1)
    TextView relation1;
    @BindView(R.id.relation_2)
    TextView relation2;
    @BindView(R.id.relation_3)
    TextView relation3;
    @BindView(R.id.relation_4)
    TextView relation4;
    @BindView(R.id.relation_5)
    TextView relation5;
    @BindView(R.id.relation_line1)
    LinearLayout relationLine1;
    @BindView(R.id.relation_6)
    TextView relation6;
    @BindView(R.id.relation_7)
    TextView relation7;
    @BindView(R.id.relation_8)
    TextView relation8;
    @BindView(R.id.relation_9)
    TextView relation9;
    @BindView(R.id.relation_10)
    TextView relation10;
    @BindView(R.id.relation_line2)
    LinearLayout relationLine2;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private BindStepTwoPresenter presenter;

    private PostCheckIMEIEntity.ObjectBean deviceInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_201708_watch_bind_step_two);
        ButterKnife.bind(this);

        initTitle();
        deviceInfo = (PostCheckIMEIEntity.ObjectBean) getIntent().getSerializableExtra(IParamsName.PARAMS_COMMON_DATA);
        if(deviceInfo==null || deviceInfo.getImei().length()<1){
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_error_activity_parameters), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            return;
        }

        initRelationLine(deviceInfo.getDeviceType());

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
     * ***********************************************************************************************************************************************
     */

    public void initTitle(){
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.bind_device_step_one_title);

        presenter = new BindStepTwoPresenter();
    }

    public void initRelationLine(int deviceType){
        switch (deviceType){
            case IParamsIntValue.DEVICE_TYPE_1_CHILD:{
                image.setImageResource(R.drawable.binding_portrait_ico);
                relationLine1.setVisibility(View.VISIBLE);
                relationLine2.setVisibility(View.VISIBLE);
                relation1.setText(R.string.bind_device_step_two_relation_1_child);
                relation2.setText(R.string.bind_device_step_two_relation_2_child);
                relation3.setText(R.string.bind_device_step_two_relation_3_child);
                relation4.setText(R.string.bind_device_step_two_relation_4_child);
                relation5.setText(R.string.bind_device_step_two_relation_5_child);

                relation6.setText(R.string.bind_device_step_two_relation_6_child);
                relation7.setText(R.string.bind_device_step_two_relation_7_child);
                relation8.setText(R.string.bind_device_step_two_relation_8_child);
                relation9.setText(R.string.bind_device_step_two_relation_9_child);
                relation10.setVisibility(View.INVISIBLE);
                break;
            }
            default:{
                image.setImageResource(R.drawable.temp_icon);
                relationLine1.setVisibility(View.VISIBLE);
                relationLine2.setVisibility(View.INVISIBLE);
                relation1.setText(R.string.bind_device_step_two_relation_1);
                relation2.setText(R.string.bind_device_step_two_relation_2);
                relation3.setText(R.string.bind_device_step_two_relation_3);
                relation4.setText(R.string.bind_device_step_two_relation_4);
                relation5.setText(R.string.bind_device_step_two_relation_5);
                break;
            }
        }
    }

    private String imagePath = null;
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
     * ***********************************************************************************************************************************************
     */
    @Override
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.EMPTY:
                HashMap<String, String> params = new HashMap<>();
                return params;
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.EMPTY:
                getDialog(this, getString(R.string.hint_common_get_data));//正在获取数据...
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.EMPTY:
                break;
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.EMPTY:
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.EMPTY:
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
     */

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
                imagePath = null;
                mCurrentPhotoPath = path;
                //TODO 修正上传图片为 400*400的分辨率，而不是默认的160*160
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

    /**
     * ***********************************************************************************************************************************************
     */

    @OnClick({R.id.ibtn_back, R.id.image, R.id.relation_1, R.id.relation_2, R.id.relation_3, R.id.relation_4, R.id.relation_5, R.id.relation_6, R.id.relation_7, R.id.relation_8, R.id.relation_9, R.id.relation_10, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back: {
                finish();
                break;
            }
            case R.id.image: {
                showImageSelectDialog();
                break;
            }
            case R.id.relation_1:
                break;
            case R.id.relation_2:
                break;
            case R.id.relation_3:
                break;
            case R.id.relation_4:
                break;
            case R.id.relation_5:
                break;
            case R.id.relation_6:
                break;
            case R.id.relation_7:
                break;
            case R.id.relation_8:
                break;
            case R.id.relation_9:
                break;
            case R.id.relation_10:
                break;
            case R.id.btn_submit:
                break;
        }
    }
}
