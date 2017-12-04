package com.quanjiakan.activity.common.index.bind;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.constants.ICommonActivityRequestCode;
import com.quanjiakan.activity.common.web.CommonWebForBindChildActivity;
import com.quanjiakan.activity.common.web.CommonWebForBindOldActivity;
import com.quanjiakan.constants.IParamsIntValue;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.device.entity.CommonNattyData;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.device.ContactsListBean;
import com.quanjiakan.net.retrofit.result_entity.PostCheckIMEIEntity;
import com.quanjiakan.net.upload.UploadUtil;
import com.quanjiakan.net_presenter.BindStepTwoPresenter;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.common.ParseToGsonUtil;
import com.quanjiakan.util.common.StringCheckUtil;
import com.quanjiakan.util.common.UnitExchangeUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.image.BitmapUtil;
import com.quanjiakan.util.image.IImageCropInterface;
import com.quanjiakan.util.image.ImageCropHandler;
import com.quanjiakan.util.image.ImageUtils;
import com.quanjiakan.util.widget.CircleTransformation;
import com.quanjiakan.view.dialog.BottomSelectImageDialog;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.wbj.ndk.natty.client.NattyProtocolFilter;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private int currentSelectedPosition = 1;
    private String currentRelationName;
    private Dialog remindDialog;
    private HashMap<String, String> nameMap = new HashMap<>();
    private List<ContactsListBean> contacts = new ArrayList<>();

    private String sendName;

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
        initValue();

    }

    @Override
    public void onStart() {
        super.onStart();
        registerEventBus();
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

    @Override
    public void onStop() {
        super.onStop();
        unregisterEventBus();
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

    public void initValue() {
        currentSelectedPosition = 1;

        //TODO 赋值显示
        /**
         0.老人手表
         1.儿童手表
         2.定位器
         3.睡眠监测仪
         4.拐杖
         5 呼吸监测仪
         6 体态监测仪
         */
        switch (deviceInfo.getDeviceType()) {
            case IParamsIntValue.DEVICE_TYPE_0_OLD:
                deviceTypeName.setText(R.string.bind_device_step_two_device_type_1);
                break;
            case IParamsIntValue.DEVICE_TYPE_1_CHILD:
                deviceTypeName.setText(R.string.bind_device_step_two_device_type_2);
                break;
            case IParamsIntValue.DEVICE_TYPE_2_LOCATER:
                deviceTypeName.setText(R.string.bind_device_step_two_device_type_3);
                break;
            case IParamsIntValue.DEVICE_TYPE_3_SLEEP:
                deviceTypeName.setText(R.string.bind_device_step_two_device_type_4);
                break;
            case IParamsIntValue.DEVICE_TYPE_4_STICK:
                deviceTypeName.setText(R.string.bind_device_step_two_device_type_5);
                break;
            case IParamsIntValue.DEVICE_TYPE_5_BREATH:
                deviceTypeName.setText(R.string.bind_device_step_two_device_type_6);
                break;
            case IParamsIntValue.DEVICE_TYPE_6_POSTURE://
                deviceTypeName.setText(R.string.bind_device_step_two_device_type_7);
                break;
            default:
                deviceTypeName.setText(R.string.bind_device_step_two_device_type_1);
                break;
        }
        initRelationLine(deviceInfo.getDeviceType());

        //TODO 设置初始选择的数据
        currentSelectedPosition = 0;
        setCurrentRelation(currentSelectedPosition);
        setCurrentRelationName(relation1);
    }

    public void setCurrentRelationName(TextView textView){
        currentRelationName = textView.getText().toString();
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

    /**
     * TODO 切换文件选中的颜色
     */
    public void setCurrentRelation(int position) {
        resetAllRelationTextColor();
        switch (position) {
            case 0: {
                relation1.setTextColor(getResources().getColor(R.color.font_color_15A9A9));
                relation1.setBackgroundResource(R.drawable.selecter_device_bind_selected);
                break;
            }
            case 1: {
                relation2.setTextColor(getResources().getColor(R.color.font_color_15A9A9));
                relation2.setBackgroundResource(R.drawable.selecter_device_bind_selected);
                break;
            }
            case 2: {
                relation3.setTextColor(getResources().getColor(R.color.font_color_15A9A9));
                relation3.setBackgroundResource(R.drawable.selecter_device_bind_selected);
                break;
            }
            case 3: {
                relation4.setTextColor(getResources().getColor(R.color.font_color_15A9A9));
                relation4.setBackgroundResource(R.drawable.selecter_device_bind_selected);
                break;
            }
            case 4: {
                relation5.setTextColor(getResources().getColor(R.color.font_color_15A9A9));
                relation5.setBackgroundResource(R.drawable.selecter_device_bind_selected);
                //TODO 弹出对话框填写自定义名称
                if(deviceInfo.getDeviceType()!=IParamsIntValue.DEVICE_TYPE_1_CHILD){
                    showEditRemindDialog(relation5);
                }
                break;
            }
            case 5: {
                relation6.setTextColor(getResources().getColor(R.color.font_color_15A9A9));
                relation6.setBackgroundResource(R.drawable.selecter_device_bind_selected);
                break;
            }
            case 6: {
                relation7.setTextColor(getResources().getColor(R.color.font_color_15A9A9));
                relation7.setBackgroundResource(R.drawable.selecter_device_bind_selected);
                break;
            }
            case 7: {
                relation8.setTextColor(getResources().getColor(R.color.font_color_15A9A9));
                relation8.setBackgroundResource(R.drawable.selecter_device_bind_selected);
                break;
            }
            case 8: {
                relation9.setTextColor(getResources().getColor(R.color.font_color_15A9A9));
                relation9.setBackgroundResource(R.drawable.selecter_device_bind_selected);
                //TODO 弹出对话框填写自定义名称
                if(deviceInfo.getDeviceType()==IParamsIntValue.DEVICE_TYPE_1_CHILD){
                    showEditRemindDialog(relation9);
                }
                break;
            }
            default: {
                relation1.setTextColor(getResources().getColor(R.color.font_color_15A9A9));
                relation1.setBackgroundResource(R.drawable.selecter_device_bind_selected);
                break;
            }
        }
    }

    public void resetAllRelationTextColor() {
        relation1.setTextColor(getResources().getColor(R.color.font_color_999999));
        relation1.setBackgroundResource(R.drawable.selecter_device_bind_unselected);

        relation2.setTextColor(getResources().getColor(R.color.font_color_999999));
        relation2.setBackgroundResource(R.drawable.selecter_device_bind_unselected);

        relation3.setTextColor(getResources().getColor(R.color.font_color_999999));
        relation3.setBackgroundResource(R.drawable.selecter_device_bind_unselected);

        relation4.setTextColor(getResources().getColor(R.color.font_color_999999));
        relation4.setBackgroundResource(R.drawable.selecter_device_bind_unselected);

        relation5.setTextColor(getResources().getColor(R.color.font_color_999999));
        relation5.setBackgroundResource(R.drawable.selecter_device_bind_unselected);
        //***************************************************************************************
        relation6.setTextColor(getResources().getColor(R.color.font_color_999999));
        relation6.setBackgroundResource(R.drawable.selecter_device_bind_unselected);

        relation7.setTextColor(getResources().getColor(R.color.font_color_999999));
        relation7.setBackgroundResource(R.drawable.selecter_device_bind_unselected);

        relation8.setTextColor(getResources().getColor(R.color.font_color_999999));
        relation8.setBackgroundResource(R.drawable.selecter_device_bind_unselected);

        relation9.setTextColor(getResources().getColor(R.color.font_color_999999));
        relation9.setBackgroundResource(R.drawable.selecter_device_bind_unselected);
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
     * ***********************************************************************************************************************************************
     */
    @Override
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD: {
                HashMap<String, String> params = new HashMap<>();
                return params;
            }
            case IPresenterBusinessCode.DEVICE_GET_WATCH_CONTACT: {
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_IMEI, deviceInfo.getImei());
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
            case IPresenterBusinessCode.DEVICE_GET_WATCH_CONTACT:
                getDialog(this, getString(R.string.hint_device_get_contacts));//正在获取数据...
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD:
                break;
            case IPresenterBusinessCode.DEVICE_GET_WATCH_CONTACT:
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
            case IPresenterBusinessCode.DEVICE_GET_WATCH_CONTACT:
                setContactsResult(result);
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD:
                break;
            case IPresenterBusinessCode.DEVICE_GET_WATCH_CONTACT:
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

    public void setContactsResult(Object result){
        if(result!=null && result instanceof String){
            String data = result.toString();
            if (data != null && data.length() > 2) {
                JsonObject jsonObject = new ParseToGsonUtil(data).getJsonObject();
                if (jsonObject.has("code") && "200".equals(jsonObject.get("code").getAsString())) {
                    JsonArray list = jsonObject.getAsJsonArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        JsonObject object = list.get(i).getAsJsonObject();
                        ContactsListBean contactsBean = new ContactsListBean();
                        if (object.has("admin")) {
                            contactsBean.setAdmin(object.get("admin").getAsString());
                        }

                        if (object.has("group")) {
                            contactsBean.setGroup(object.get("group").getAsString());
                        }

                        if (object.has("group") && "1".equals(object.get("group").getAsString()) && object.has("userid")) {
                            nameMap.put(object.get("userid").getAsString() + "Name",
                                    (object.get("name") != null ? object.get("name").getAsString() : object.get("userid").getAsString()
                                            //TODO 若存在不含名字这个字段，则使用Userid进行替代
                                    ));
                            nameMap.put(object.get("userid").getAsString() + "Image",
                                    (object.get("image") != null ? object.get("image").getAsString() : "8"//TODO
                                            //TODO 若存在不含图像这个字段，则使用 默认的 自定义头像 进行替代*****

                                            //TODO 2017-05-05发现存在Image字段不存在的情况
                                            //TODO 请求链接 http://app.quanjiakan.com/device/service?code=childWatch&type=contracts&imei=355637050066828
                                            //TODO 返回的数据 {"Results":{"Category":"Contacts","Contacts":[{"Admin":"0","App":"1","Id":"984","Image":"8","Name":"%E5%86%AF%E5%B7%A9","Tel":"13650703987","Userid":"11178"},{"Admin":"0","App":"1","Id":"986","Image":"0","Name":"%E7%88%B8%E7%88%B8","Tel":"15218293347","Userid":"13469"},{"Admin":"0","App":"1","Id":"1015","Image":"1","Name":"%E5%A6%88%E5%A6%88","Tel":"15820233638","Userid":"11825"},{"Admin":"1","App":"1","Id":"1025","Image":"8","Name":"%E5%B0%8FA","Tel":"13432992552","Userid":"13625"},{"Admin":"0","App":"0","Id":"1038","Image":"8","Name":"%E5%B0%8F%E6%98%8E","Tel":"18718717141","Userid":"13469"},{"Admin":"0","App":"1","Id":"1050","Name":"%E7%88%B7%E7%88%B7","Tel":"13802735616","Userid":"11931"}],"IMEI":"355637050066828","Num":6}}
                                    ));
                        }

                        if (object.has("id")) {
                            contactsBean.setId(object.get("id").getAsInt());
                        }

                        if (object.has("image")) {
                            contactsBean.setImage(object.get("image").getAsString());
                        }

                        if (object.has("name")) {
                            contactsBean.setName(object.get("name").getAsString());
                        }

                        if (object.has("pnum")) {
                            contactsBean.setPnum(object.get("pnum").getAsString());
                        }

                        if (object.has("userid")) {
                            contactsBean.setUserid(object.get("userid").getAsInt());
                        }
                        contacts.add(contactsBean);
                    }

                }

            }
            String tempName;
            String decodeName;
            try {
                sendName = URLEncoder.encode(currentRelationName, "utf-8");
                if (contacts.size() > 0) {
                    for (int i = 0; i < contacts.size(); i++) {
                        tempName = contacts.get(i).getName();
                        decodeName = URLDecoder.decode(tempName, "utf-8");
                        if (sendName.equals(tempName) || data.contains(sendName)
                                || currentRelationName.equals(tempName) ||
                                currentRelationName.equals(decodeName)
                                ) {
                            CommonDialogHint.getInstance().showHint(this,getString(R.string.hint_device_get_contacts_ready_has));
                            break;
                        }
                        if (i == contacts.size() - 1) {
                            bindDevice(deviceInfo.getImei());
                        }
                    }
                } else {
                    bindDevice(deviceInfo.getImei());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    protected void bindDevice(final String deviceid) {
        if (StringCheckUtil.isEmpty(deviceid) || (deviceid.length() != 15 && deviceid.length() != 16)) {
            CommonDialogHint.getInstance().showHint(this,getString(R.string.hint_bind_step_illegal_imei));
            return;
        }
        if (!BaseApplication.getInstances().isSDKConnected()) {
            CommonDialogHint.getInstance().showHint(this,getString(R.string.hint_loss_device_server_connection));
        } else {
            try {
                //TODO Old
                long devid = Long.parseLong(deviceid, 16);
                JSONObject jsonObject = new JSONObject();
                JSONObject jsonObjectSub = new JSONObject();
                jsonObjectSub.put("WatchName", URLEncoder.encode(deviceNickName.getText().toString(), "utf-8"));
                jsonObjectSub.put("WatchImage", image.getTag().toString());
                jsonObjectSub.put("UserName", URLEncoder.encode(currentRelationName, "utf-8"));
                jsonObjectSub.put("UserImage", currentSelectedPosition + "");
                jsonObject.put("IMEI", deviceid);
                jsonObject.put("Category", "BindReq");
                jsonObject.put("BindReq", jsonObjectSub);
                LogUtil.e("BindData:" + jsonObject.toString());
                int size = jsonObject.toString().getBytes().length;
                BaseApplication.getInstances().getNattyClient().ntyBindClient(devid, jsonObject.toString().getBytes(), size);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                "&storage=16";//TODO 这个需要---如果没有可能会报参数错误的异常

        HashMap<String, String> paramsFile = new HashMap<>();
        paramsFile.put("file", path);
        paramsFile.put("filename", file.getName());
        paramsFile.put("image", file.getAbsolutePath());
        UploadUtil.uploadFile(this, url, null, paramsFile);
    }

    private void showEditRemindDialog(final TextView relation) {
        remindDialog = new Dialog(this, R.style.ShareDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_child_plan_edit, null);
        final EditText tv_content = (EditText) view.findViewById(R.id.tv_content);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("输入自定义名称");
        TextView btn_confirm = (TextView) view.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_content.length() < 1) {
                    CommonDialogHint.getInstance().showHint(BindStepTwoActivity.this,getString(R.string.hint_bind_step_two_no_name));
                    return;
                }

                if ((StringCheckUtil.isAllChineseChar(tv_content.getText().toString()) && tv_content.getText().toString().length() > 4)) {
                    CommonDialogHint.getInstance().showHint(BindStepTwoActivity.this,getString(R.string.hint_bind_step_two_right_name));
                    return;
                }

                if (!StringCheckUtil.isChar(tv_content.getText().toString())) {
                    CommonDialogHint.getInstance().showHint(BindStepTwoActivity.this,getString(R.string.hint_bind_step_two_illegal_char));
                    return;
                }

                if (tv_content.getText().toString().length() > 8) {
                    CommonDialogHint.getInstance().showHint(BindStepTwoActivity.this,getString(R.string.hint_bind_step_two_length_limit));
                    return;
                }
                relation.setText(tv_content.getText().toString());
                currentRelationName = tv_content.getText().toString();
                if (remindDialog != null) {
                    remindDialog.dismiss();
                }
            }
        });

        TextView btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remindDialog != null) {
                    remindDialog.dismiss();
                }
            }
        });

        WindowManager.LayoutParams lp = remindDialog.getWindow().getAttributes();
        lp.width = UnitExchangeUtil.dip2px(this, 300);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        remindDialog.setContentView(view, lp);
        remindDialog.show();
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
                currentSelectedPosition = 0;
                setCurrentRelation(currentSelectedPosition);
                setCurrentRelationName(relation1);
                break;
            case R.id.relation_2:
                currentSelectedPosition = 1;
                setCurrentRelation(currentSelectedPosition);
                setCurrentRelationName(relation2);
                break;
            case R.id.relation_3:
                currentSelectedPosition = 2;
                setCurrentRelation(currentSelectedPosition);
                setCurrentRelationName(relation3);
                break;
            case R.id.relation_4:
                currentSelectedPosition = 3;
                setCurrentRelation(currentSelectedPosition);
                setCurrentRelationName(relation4);
                break;
            case R.id.relation_5:
                currentSelectedPosition = 4;
                setCurrentRelation(currentSelectedPosition);
                setCurrentRelationName(relation5);
                break;
//*************************************************************************
            case R.id.relation_6:
                currentSelectedPosition = 5;
                setCurrentRelation(currentSelectedPosition);
                setCurrentRelationName(relation6);
                break;
            case R.id.relation_7:
                currentSelectedPosition = 6;
                setCurrentRelation(currentSelectedPosition);
                setCurrentRelationName(relation7);
                break;
            case R.id.relation_8:
                currentSelectedPosition = 7;
                setCurrentRelation(currentSelectedPosition);
                setCurrentRelationName(relation8);
                break;
            case R.id.relation_9:
                currentSelectedPosition = 8;
                setCurrentRelation(currentSelectedPosition);
                setCurrentRelationName(relation9);
                break;
            case R.id.btn_submit: {
                if(isValidInfo()){
                    presenter.getContactInfo(this);
                }
                break;
            }
        }
    }



    public boolean isValidInfo(){
        if (deviceNickName.getText().toString() == null || deviceNickName.getText().toString().length() < 1) {
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_bind_step_two_no_nick_name));
            return false;
        }
        if (deviceNickName.getText().toString().contains("%") || !StringCheckUtil.isChar(deviceNickName.getText().toString())) {
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_bind_step_two_nick_name_contain_illegal_char));
            return false;
        }

        if (image.getTag() == null) {
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_bind_step_two_no_head_icon));
            return false;
        }
        if (!image.getTag().toString().toLowerCase().startsWith("http")) {
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_bind_step_two_head_icon_upload_fail));
            return false;
        }
        return true;
    }

    @Subscribe(threadMode = org.greenrobot.eventbus.ThreadMode.MAIN)
    public void onCommonNattyData(CommonNattyData msg) {
        switch (msg.getType()) {
            case NattyProtocolFilter.DISPLAY_UPDATE_CONTROL_BIND_RESULT:{
                //0：代表成功，1：代表 UserId不存在，2：代表DeviceId不存在，3：代表UserId与DeviceId已经绑定过了  4: 设备未激活  5:管理员
                String res = msg.getData();
                if ("0".equals(res)) {
                    /**
                     * 确认绑定成功后
                     */
                    /**
                     * 调用创建/添加群 TODO 暂时不
                     */
                    //TODO 发送成功后跳转到首页，等待管理员的结果

                    CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_bind_step_two_success), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(BindStepTwoActivity.this, BindStateActivity.class);
                            intent.putExtra(IParamsName.PARAMS_COMMON_BIND_STATE, BindStateActivity.WAIT);
                            intent.putExtra(IParamsName.PARAMS_COMMON_IMEI, deviceInfo.getImei());
                            startActivityForResult(intent,ICommonActivityRequestCode.BIND_STEP_TWO_RESULT);
                        }
                    });
                } else if ("1".equals(res)) {
                    CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_bind_step_two_no_user));
                } else if ("2".equals(res)) {
                    CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_bind_step_two_no_imei));
                } else if ("3".equals(res)) {
                    //TODO 已经绑定过，直接修改头像与名称
                    //TODO 2017-04-10 王博靖邮件确认
                    CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_bind_step_two_applied), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            switch (deviceInfo.getDeviceType()) {
                                case IParamsIntValue.DEVICE_TYPE_1_CHILD:
                                    //TODO 现在家璧邮件里说的地址 2017-10-11  （儿童）
                                    intent.setClass(BindStepTwoActivity.this, CommonWebForBindChildActivity.class);
                                    intent.putExtra(IParamsName.PARAMS_COMMON_WEB_URL, "http://static.quanjiakan.com/familycare/activate?IMEI=" + deviceInfo.getImei());
                                    break;
                                default:
                                    //TODO 现在家璧邮件里说的地址 2017-10-11  （老人）
                                    intent.setClass(BindStepTwoActivity.this, CommonWebForBindOldActivity.class);
                                    intent.putExtra(IParamsName.PARAMS_COMMON_WEB_URL, "http://static.quanjiakan.com/qjk/pages/qjk/device/watcharchives.jsp?type=0&IMEI="+deviceInfo.getImei());
                                    break;
                            }
                            startActivityForResult(intent,ICommonActivityRequestCode.BIND_STEP_TWO_NET_3);//TODO 返回需要返回到这个界面,而不是首页----经 谢东 确认 2017-04-24
                        }
                    });
                } else if ("4".equals(res)) {
                    //TODO 2017-04-10 王博靖邮件确认
                    CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_bind_step_two_unactive), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            switch (deviceInfo.getDeviceType()) {
                                case IParamsIntValue.DEVICE_TYPE_1_CHILD:
                                    //TODO 现在家璧邮件里说的地址 2017-10-11  （儿童）
                                    intent.setClass(BindStepTwoActivity.this, CommonWebForBindChildActivity.class);
                                    intent.putExtra(IParamsName.PARAMS_COMMON_WEB_URL, "http://static.quanjiakan.com/familycare/activate?version=1&IMEI=" + deviceInfo.getImei());
                                    break;
                                default:
                                    //TODO 现在家璧邮件里说的地址 2017-10-11  （老人）
                                    intent.setClass(BindStepTwoActivity.this, CommonWebForBindOldActivity.class);
                                    intent.putExtra(IParamsName.PARAMS_COMMON_WEB_URL, "http://static.quanjiakan.com/qjk/pages/qjk/device/activation_login.jsp");
                                    break;
                            }
                            startActivityForResult(intent,ICommonActivityRequestCode.BIND_STEP_TWO_NET_4);//TODO 返回需要返回到这个界面,而不是首页----经 谢东 确认 2017-04-24
                        }
                    });
                } else if ("5".equals(res)) {//第一个绑定
                    //TODO 发送成功后跳转到首页，等待管理员的结果
                    CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_bind_step_two_admin), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(BindStepTwoActivity.this, BindStateActivity.class);
                            intent.putExtra(IParamsName.PARAMS_COMMON_BIND_STATE, BindStateActivity.ACCESS);
                            intent.putExtra(IParamsName.PARAMS_COMMON_IMEI, deviceInfo.getImei());
                            startActivityForResult(intent,ICommonActivityRequestCode.BIND_STEP_TWO_RESULT);
                        }
                    });
                } else if ("6".equals(res)) {//拒绝
                    CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_bind_step_two_unactive), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(BindStepTwoActivity.this, BindStateActivity.class);
                            intent.putExtra(IParamsName.PARAMS_COMMON_BIND_STATE, BindStateActivity.REJECT);
                            intent.putExtra(IParamsName.PARAMS_COMMON_IMEI, deviceInfo.getImei());
                            startActivityForResult(intent,ICommonActivityRequestCode.BIND_STEP_TWO_RESULT);
                        }
                    });
                } else {
                    CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_bind_step_two_unknown_state_code)+res+
                                    getString(R.string.hint_bind_step_two_unknown_state_code_2)
                            , new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
                break;
            }
        }
    }

}
