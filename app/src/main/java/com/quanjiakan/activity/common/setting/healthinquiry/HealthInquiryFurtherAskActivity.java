package com.quanjiakan.activity.common.setting.healthinquiry;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.czt.mp3recorder.MP3Recorder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.constants.ICommonActivityRequestCode;
import com.quanjiakan.constants.ICommonActivityResultCode;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.adapter.FreeInquiryAppendAdapter;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.entity.BaseHttpResultEntity_List;
import com.quanjiakan.entity.FreeInquiryAnswerEntity;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.retrofit.result_entity.HealthInquiryFurtherAskDoctorInfoEntity;
import com.quanjiakan.net.retrofit.result_entity.PostLastTenMessage;
import com.quanjiakan.net.upload.UploadUtil;
import com.quanjiakan.net_presenter.HealthInquiryFurtherAskPresenter;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.common.IdHelper;
import com.quanjiakan.util.common.KeyBoardUtil;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.common.ParseToGsonUtil;
import com.quanjiakan.util.common.SerializeToObjectUtil;
import com.quanjiakan.util.common.StorageDeviceUtil;
import com.quanjiakan.util.common.StringDecodeUtil;
import com.quanjiakan.util.common.UnitExchangeUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.image.BitmapUtil;
import com.quanjiakan.util.image.IImageCropInterface;
import com.quanjiakan.util.image.ImageCropHandler;
import com.quanjiakan.util.image.ImageUtils;
import com.quanjiakan.util.widget.CircleTransformation;
import com.quanjiakan.view.message.RecordVoiceButton;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/20.
 */

public class HealthInquiryFurtherAskActivity extends BaseActivity {


    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ibtn_menu)
    ImageButton ibtnMenu;
    @BindView(R.id.menu_text)
    TextView menuText;
    @BindView(R.id.jmui_switch_voice_ib)
    ImageButton jmuiSwitchVoiceIb;
    @BindView(R.id.jmui_chat_input_et)
    EditText jmuiChatInputEt;
    @BindView(R.id.jmui_expression_btn)
    ImageButton jmuiExpressionBtn;
    @BindView(R.id.jmui_voice_btn)
    RecordVoiceButton jmuiVoiceBtn;
    @BindView(R.id.jmui_send_msg_btn)
    Button jmuiSendMsgBtn;
    @BindView(R.id.jmui_add_file_btn)
    ImageButton jmuiAddFileBtn;
    @BindView(R.id.jmui_pick_from_local_btn)
    ImageButton jmuiPickFromLocalBtn;
    @BindView(R.id.jmui_pick_from_camera_btn)
    ImageButton jmuiPickFromCameraBtn;
    @BindView(R.id.jmui_send_video_btn)
    ImageButton jmuiSendVideoBtn;
    @BindView(R.id.jmui_send_location_btn)
    ImageButton jmuiSendLocationBtn;
    @BindView(R.id.jmui_business_card_btn)
    ImageButton jmuiBusinessCardBtn;
    @BindView(R.id.jmui_more_menu_tl)
    TableLayout jmuiMoreMenuTl;
    @BindView(R.id.send_msg_layout)
    LinearLayout sendMsgLayout;
    @BindView(R.id.docter_info_head)
    ImageView docterInfoHead;
    @BindView(R.id.docter_info_name)
    TextView docterInfoName;
    @BindView(R.id.docter_info_hospital)
    TextView docterInfoHospital;
    @BindView(R.id.docter_info_hospital_value)
    TextView docterInfoHospitalValue;
    @BindView(R.id.docter_info_clinic)
    TextView docterInfoClinic;
    @BindView(R.id.docter_info_clinic_value)
    TextView docterInfoClinicValue;
    @BindView(R.id.docter_info_rank)
    TextView docterInfoRank;
    @BindView(R.id.docter_info_rank_value)
    TextView docterInfoRankValue;
    @BindView(R.id.evaluate)
    TextView evaluate;
    @BindView(R.id.docter_info_layout)
    RelativeLayout docterInfoLayout;
    @BindView(R.id.divider_info)
    View dividerInfo;
    @BindView(R.id.divider_line)
    View dividerLine;
    @BindView(R.id.line)
    LinearLayout line;
    @BindView(R.id.divider_line_2)
    View dividerLine2;
    @BindView(R.id.list)
    PullToRefreshListView list;

    private HealthInquiryFurtherAskPresenter presenter;

    private PostLastTenMessage.ListBean problemID;

    private TextWatcher watcher = new TextWatcher() {
        private CharSequence temp = "";
        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
            if (temp.length() > 0) {
                jmuiAddFileBtn.setVisibility(View.GONE);
                jmuiSendMsgBtn.setVisibility(View.VISIBLE);
            }else {
                jmuiAddFileBtn.setVisibility(View.VISIBLE);
                jmuiSendMsgBtn.setVisibility(View.GONE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
            temp = s;
        }

    };

    //TODO 评价Dialog
    private Dialog evaluateDialg;
    private View view;

    private float evaluateRank;
    private String evaluateContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_health_inquiry_append);
        ButterKnife.bind(this);

        initTitle();
        initView();

        problemID = (PostLastTenMessage.ListBean) getIntent().getSerializableExtra(IParamsName.PARAMS_COMMON_DATA);

        if (problemID == null) {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_error_activity_parameters), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            return;
        }

        initListView();
        getDoctorInfo();
        getAnswer();
        initInputLayout();
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
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.HEALTH_INQUIRY_APPEND_PROBLEM: {
                HashMap<String, String> params = new HashMap<>();
                return params;
            }
            case IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_INFO: {
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_HEALTH_INQURIRY_TYPE, IParamsName.PARAMS_HEALTH_INQURIRY_TYPE_VALUE);
                params.put(IParamsName.PARAMS_COMMON_MEMBERID, BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_HEALTH_INQURIRY_DOCTOR_ID, problemID.getDoctorId());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                return params;
            }
            case IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_ANSWER_LIST: {
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_MEMBERID, BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_HEALTH_INQURIRY_PROBLEM_ID, problemID.getId() + "");
                return params;
            }
            case IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_EVALUATE: {
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_DEVICE_TYPE, IHttpUrlConstants.DEVICE_TYPE_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_DEVICE_CLIENT, IHttpUrlConstants.CLIENT_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_USERID, BaseApplication.getInstances().getLoginInfo().getUserId());

                params.put(IParamsName.PARAMS_HEALTH_INQUIRY_PROBLEM_ID, problemID.getChunyuId() + "");
                params.put(IParamsName.PARAMS_HEALTH_INQUIRY_USERNAME, BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_HEALTH_INQUIRY_STAR, Math.round(evaluateRank) + "");
                String content = "[{\"text\":\"" + evaluateContent + "" + "\",\"type\":\"text\"}]";
                params.put(IParamsName.PARAMS_HEALTH_INQUIRY_CONTENT, content);
                return params;
            }
            case IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_SEND_QUESTION: {
                content = new JsonArray();
                JsonObject object = new JsonObject();

                if (jmuiChatInputEt.getText().toString().length() > 0) {
                    object.addProperty("type", "text");
                    object.addProperty("text", jmuiChatInputEt.getText().toString());
                } else if (voicePath != null) {
                    object.addProperty("type", "audio");
                    object.addProperty("file", voicePath);
                } else if (imagePath != null) {
                    object.addProperty("type", "image");
                    object.addProperty("file", imagePath);
                }

                content.add(object);

                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_HEALTH_INQURIRY_TYPE, IParamsName.PARAMS_HEALTH_INQURIRY_TYPE_VALUE);
                params.put(IParamsName.PARAMS_COMMON_MEMBERID, BaseApplication.getInstances().getLoginInfo().getUserId());
                params.put(IParamsName.PARAMS_HEALTH_INQURIRY_CONTENT, content + "");//内容jsonArray格式
                params.put(IParamsName.PARAMS_HEALTH_INQURIRY_FROMTOKEN, BaseApplication.getInstances().getLoginInfo().getUserId()); //自己的UserID
                params.put(IParamsName.PARAMS_HEALTH_INQURIRY_TOTOKEN, problemID.getDoctorId());//咨询医生的UserID
                params.put(IParamsName.PARAMS_HEALTH_INQURIRY_CHUNYUID, problemID.getChunyuId() + "");//咨询的问题id
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                return params;
            }
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.HEALTH_INQUIRY_APPEND_PROBLEM:
                getDialog(this, getString(R.string.hint_common_get_data));//正在获取数据...
                break;
            case IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_INFO:
//                getDialog(this, getString(R.string.hint_common_get_data));//正在获取数据...
                break;
            case IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_ANSWER_LIST:
                getDialog(this, getString(R.string.hint_common_get_data));//正在获取数据...
                break;
            case IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_EVALUATE: {
                getDialog(this, getString(R.string.hint_common_submit_data));//正在获取数据...
                break;
            }
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD: {
                getDialog(this, getString(R.string.hint_common_submit_file_image));//正在获取数据...
                break;
            }
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD_VOICE:{
                getDialog(this, getString(R.string.hint_common_submit_file_voice));//正在获取数据...
                break;
            }
            case IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_SEND_QUESTION: {
                getDialog(this, getString(R.string.hint_common_submit_data));//正在获取数据...
                break;
            }
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.HEALTH_INQUIRY_APPEND_PROBLEM:
                break;
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.HEALTH_INQUIRY_APPEND_PROBLEM:
                break;
            case IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_INFO:
                setDoctorInfo(result);
                break;
            case IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_ANSWER_LIST:
                loadInfoListView(result);
                break;
            case IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_EVALUATE:
                setEvaluateResult(result);
                break;
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD:
                afterUpload(result);
                break;
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD_VOICE:
                afterVoiceUpload(result);
                break;
            case IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_SEND_QUESTION: {
                setFurtherInquiryResult(result);
                break;
            }
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.HEALTH_INQUIRY_APPEND_PROBLEM:
                break;
            case IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_INFO:
                break;
            case IPresenterBusinessCode.HEALTH_INQUIRY_PROBLEM_ANSWER_LIST:
                list.onRefreshComplete();
                break;
            case IPresenterBusinessCode.HEALTH_INQUIRY_DOCTOR_EVALUATE:
                break;
        }
        if (errorMsg != null && errorMsg.toString().length() > 0) {
            CommonDialogHint.getInstance().showHint(this, errorMsg.toString());
        } else {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_net_request_fail));
        }
    }

    @OnClick({R.id.ibtn_back, R.id.jmui_switch_voice_ib, R.id.evaluate,
            R.id.jmui_add_file_btn, R.id.jmui_pick_from_local_btn, R.id.jmui_pick_from_camera_btn,
            R.id.jmui_send_msg_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //****************************************************************
            case R.id.jmui_add_file_btn: {
                jmuiSwitchVoiceIb.setBackgroundResource(R.drawable.jmui_voice);
                jmuiChatInputEt.setVisibility(View.VISIBLE);
                jmuiVoiceBtn.setVisibility(View.GONE);
                if (jmuiMoreMenuTl.getVisibility() == View.VISIBLE) {
                    jmuiMoreMenuTl.setVisibility(View.GONE);
                    jmuiChatInputEt.requestFocus();
                    showSoftInput(jmuiChatInputEt);
                } else {
                    jmuiMoreMenuTl.setVisibility(View.VISIBLE);
                    hideSoftInput(jmuiChatInputEt);
                }
                break;
            }
            case R.id.jmui_pick_from_local_btn: {
                ImageCropHandler.pickImage(HealthInquiryFurtherAskActivity.this);
                break;
            }
            case R.id.jmui_pick_from_camera_btn: {
                ImageCropHandler.getImageFromCamera(HealthInquiryFurtherAskActivity.this, new IImageCropInterface() {
                    @Override
                    public void getFilePath(String path) {
                        mCurrentPhotoPath = path;
                    }
                });
                break;
            }
            case R.id.jmui_send_msg_btn: {
                sendAppendProblem();
                break;
            }
            case R.id.jmui_switch_voice_ib: {
                jmuiMoreMenuTl.setVisibility(View.GONE);

                if (jmuiChatInputEt.getVisibility() == View.VISIBLE) {
                    //将该按钮背景图变成键盘，并隐藏文本输入框，显示录音按钮 jmui_keyboard
                    jmuiSwitchVoiceIb.setBackgroundResource(R.drawable.jmui_keyboard);
                    jmuiChatInputEt.setVisibility(View.GONE);
                    jmuiVoiceBtn.setVisibility(View.VISIBLE);

                    hideSoftInput(jmuiChatInputEt);
                } else {
                    jmuiSwitchVoiceIb.setBackgroundResource(R.drawable.jmui_voice);
                    jmuiChatInputEt.setVisibility(View.VISIBLE);
                    jmuiVoiceBtn.setVisibility(View.GONE);
                    //输入框获取焦点，同时软键盘显示出来
                    jmuiChatInputEt.requestFocus();

                    showSoftInput(jmuiChatInputEt);
                }
                break;
            }
            //****************************************************************
            case R.id.ibtn_back: {
                finish();
                break;
            }
            case R.id.evaluate: {
                doEvaluate();
                break;
            }
        }
    }

    public void initTitle() {
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.health_inquiry_append_title);

        ibtnMenu.setVisibility(View.GONE);
        menuText.setVisibility(View.GONE);

        presenter = new HealthInquiryFurtherAskPresenter();
    }

    public void initView() {
        docterInfoHospital.setText(R.string.health_inquiry_append_belong_hospital);
        docterInfoClinic.setText(R.string.health_inquiry_append_belong_clinic);
        docterInfoRank.setText(R.string.health_inquiry_append_hospital_rank);

        jmuiChatInputEt.addTextChangedListener(watcher);
    }

    public void initListView(){
        FreeInquiryAnswerEntity questioin = new FreeInquiryAnswerEntity();
        questioin.setSponsor("1");
        questioin.setCreateMillisecond(problemID.getCreateMillisecond()+"");
        questioin.setTitle(problemID.getTitle());
        freeInquiryAnswerEntityList.add(0,questioin);

        appendAdapter = new FreeInquiryAppendAdapter(this,freeInquiryAnswerEntityList);
        list.setAdapter(appendAdapter);
        list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                appendAdapter.release();
                getAnswer();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                list.onRefreshComplete();
            }
        });
    }

    public void getDoctorInfo() {
        presenter.getProblemDoctorInfo(this);
    }

    public void getAnswer() {
        presenter.getProblemAnswerList(this);
    }

    public void loadInfoListView(Object result){
        if (result != null && result instanceof String) {
            LogUtil.e("loadInfoListView:"+result);
            list.onRefreshComplete();
            if(result.toString()!=null && result.toString().length()>0 && !"null".equals(result.toString().toLowerCase())){
                answerEntityBaseHttpResultEntity_list = (BaseHttpResultEntity_List<FreeInquiryAnswerEntity>)
                        SerializeToObjectUtil.getInstances().jsonToObject(result.toString(),new TypeToken<BaseHttpResultEntity_List<FreeInquiryAnswerEntity>>(){}.getType());

                if(answerEntityBaseHttpResultEntity_list!=null && answerEntityBaseHttpResultEntity_list.getList()!=null &&
                        answerEntityBaseHttpResultEntity_list.getList().size()>0){
                    //单个问题--多个回答
                    freeInquiryAnswerEntityList.clear();
                    freeInquiryAnswerEntityList.addAll(answerEntityBaseHttpResultEntity_list.getList());
                    /**
                     * 单独增加一个数据项用户表示发送的第一个问题
                     */
                    FreeInquiryAnswerEntity questioin = new FreeInquiryAnswerEntity();
                    questioin.setSponsor("1");
                    questioin.setCreateMillisecond(answerEntityBaseHttpResultEntity_list.getList().get(0).getCreateMillisecond());
                    questioin.setTitle(answerEntityBaseHttpResultEntity_list.getList().get(0).getTitle());
                    freeInquiryAnswerEntityList.add(0,questioin);//单独抽出显示-------放到第一位
                    //TODO 其实返回的回答数据中存在一个问题（排序问题---由于JOSN本身的数据是乱序的形式，）
                    appendAdapter.setData(freeInquiryAnswerEntityList);
                    appendAdapter.notifyDataSetChanged();
                    list.getRefreshableView().setSelection(list.getBottom());
                }
            }
        }
    }

    public void setDoctorInfo(Object result) {
        //{"code":"200","message":"返回成功","object":{"chunyuId":"529198576","clinic":"口腔颌面科","doctorId":"clinic_web_444096a1ee022669","hospital":"哈尔滨医科大学附属第一医院","image":"http://media2.chunyuyisheng.com/@/media/images/2016/09/28/735b401109f4_w312_h312_.jpg?imageMogr2/thumbnail/150x","levelTitle":"三级甲等医院 医师","name":"苑芳连","title":"医师"}}
        if (result != null && result instanceof String) {
            HealthInquiryFurtherAskDoctorInfoEntity doctorInfoEntity = (HealthInquiryFurtherAskDoctorInfoEntity) SerializeToObjectUtil.
                    getInstances().jsonToObject(result.toString(),
                    new TypeToken<HealthInquiryFurtherAskDoctorInfoEntity>() {
                    }.getType());
            //头像
            Picasso.with(this).load(doctorInfoEntity.getObject().getImage()).transform(new CircleTransformation()).into(docterInfoHead);

            docterInfoHospitalValue.setText(doctorInfoEntity.getObject().getHospital());
            docterInfoClinicValue.setText(doctorInfoEntity.getObject().getClinic());
            docterInfoRankValue.setText(doctorInfoEntity.getObject().getLevelTitle());
        }
    }

    public void setEvaluateResult(Object result) {
        if (result != null && result instanceof String) {
            try {
                JSONObject jsonResult = new JSONObject(result.toString());
                if (jsonResult.has("error")) {
                    if (jsonResult.getInt("error") == 0) {
                        CommonDialogHint.getInstance().showHint(this, getString(R.string.health_inquiry_append_evaluate_result_success));
                    } else {
                        if (jsonResult.has("error_msg")) {
                            CommonDialogHint.getInstance().showHint(this, jsonResult.getString("error_msg"));
                        } else {

                        }
                    }
                } else {
                    CommonDialogHint.getInstance().showHint(this, getString(R.string.health_inquiry_append_evaluate_result_server_error));
                }
            } catch (Exception e) {
                MobclickAgent.reportError(this, e);
                e.printStackTrace();
            }
        } else {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.health_inquiry_append_evaluate_result_server_error));
        }
    }

    public void doEvaluate() {
        if (problemID.getDoctorId() == null || "0".endsWith(problemID.getDoctorId())) {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_health_inquiry_evaluate_hint));
            return;
        } else {
            if (evaluateDialg != null) {
                evaluateDialg.dismiss();
                evaluateDialg = null;
            }
            evaluateDialg = new Dialog(this, R.style.dialog_loading);

            view = LayoutInflater.from(this).inflate(R.layout.dialog_free_inquiry_evaluate_problem, null);
            //TODO 标题
            final TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(R.string.health_inquiry_append_evaluate_dialog_title);
            //TODO 评级
            final RatingBar mRatingBar = (RatingBar) view.findViewById(R.id.rating);
            mRatingBar.setRating(5);
            mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (rating < 1) {
                        mRatingBar.setRating(1);
                    } else {
                        mRatingBar.setRating(Math.round(rating));
                    }
                }
            });
            //TODO 评价内容
            final EditText mEvaluate = (EditText) view.findViewById(R.id.evaluate);
            mEvaluate.setHint(R.string.health_inquiry_append_evaluate_dialog_content_hint);
            //TODO 标题
            TextView mConfirm = (TextView) view.findViewById(R.id.btn_confirm);
            mConfirm.setText(R.string.confirm);
            //TODO 标题
            TextView mCancel = (TextView) view.findViewById(R.id.btn_cancel);
            mCancel.setText(R.string.cancel);

            mConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    evaluateDialg.dismiss();
                    evaluateRank = mRatingBar.getRating();
                    evaluateContent = mEvaluate.getText().toString();
                    submitEvaluate(mEvaluate);
                }
            });
            mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    evaluateDialg.dismiss();
                }
            });

            WindowManager.LayoutParams lp = evaluateDialg.getWindow().getAttributes();
            lp.width = UnitExchangeUtil.dip2px(BaseApplication.getInstances(), 280);
            lp.height = lp.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;

            evaluateDialg.setContentView(view, lp);
            evaluateDialg.setCanceledOnTouchOutside(false);
            evaluateDialg.show();
        }
    }

    public void submitEvaluate(EditText content) {
        if (content.length() < 1) {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.health_inquiry_append_evaluate_dialog_content_hint1));
            return;
        }
        presenter.evaluateDoctor(this);
    }

    //************************************************************************************************************************
    //TODO 输入框交互（文字、语音、图片）

    private final static int CANCEL_RECORD = 5;
    private final static int START_RECORD = 7;

    public boolean mIsPressed = false;

    private int[] res;
    private boolean isTimerCanceled = false;
    private boolean mTimeUp = false;

    private Timer timer = new Timer();
    private Timer mCountTimer;

    private String voicePath;
    private ImageView mVolumeIv;
    private TextView mRecordHintTv;

    private long startTime, time1, time2;
    float mTouchY1, mTouchY2, mTouchY;
    private final float MIN_CANCEL_DISTANCE = 300f;
    private static final int MIN_INTERVAL_TIME = 1000;// 1s

    private MediaRecorder recorder;
    private ObtainDecibelThread mThread;
    private Dialog recordIndicator;
    private Handler mVolumeHandler;
    private File myRecAudioFile;
    private File file;
    private String imagePath;
    private String mCurrentPhotoPath = null;

    private JsonArray content;

    private BaseHttpResultEntity_List<FreeInquiryAnswerEntity> answerEntityBaseHttpResultEntity_list;
    private List<FreeInquiryAnswerEntity> freeInquiryAnswerEntityList = new ArrayList<>();
    private FreeInquiryAppendAdapter appendAdapter;


    private final MHandler myHandler = new MHandler();

    private class MHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_RECORD:
                    if (mIsPressed) {
                        initDialogAndStartRecord();
                    }
                    break;
            }
        }
    }

    //刷新 音频录制Dialog提示的Handler
    private class ShowVolumeHandler extends Handler {

        private final WeakReference<View> mButton;

        public ShowVolumeHandler(View button) {
            mButton = new WeakReference<>(button);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            View controller = mButton.get();
            if (controller != null) {
                int restTime = msg.getData().getInt("restTime", -1);
                // 若restTime>0, 进入倒计时
                if (restTime > 0) {
                    mTimeUp = true;
                    android.os.Message msg1 = mVolumeHandler.obtainMessage();
                    msg1.what = 60 - restTime + 1;
                    Bundle bundle = new Bundle();
                    bundle.putInt("restTime", restTime - 1);
                    msg1.setData(bundle);
                    //创建一个延迟一秒执行的HandlerMessage，用于倒计时
                    mVolumeHandler.sendMessageDelayed(msg1, 1000);
                    mRecordHintTv.setText(String.format(getString(IdHelper
                            .getString(HealthInquiryFurtherAskActivity.this,
                                    "jmui_rest_record_time_hint")), restTime));
                    // 倒计时结束，发送语音, 重置状态
                } else if (restTime == 0) {
                    finishRecord();
                    mTimeUp = false;
                    // restTime = -1, 一般情况
                } else {
                    // 没有进入倒计时状态
                    if (!mTimeUp) {
                        if (msg.what < CANCEL_RECORD) {
                            mRecordHintTv.setText(HealthInquiryFurtherAskActivity.this.getString(IdHelper
                                    .getString(HealthInquiryFurtherAskActivity.this, "jmui_move_to_cancel_hint")));
                        } else {
                            mRecordHintTv.setText(HealthInquiryFurtherAskActivity.this.getString(IdHelper
                                    .getString(HealthInquiryFurtherAskActivity.this, "jmui_cancel_record_voice_hint")));
                        }
                        // 进入倒计时
                    } else {
                        if (msg.what == CANCEL_RECORD) {
                            mRecordHintTv.setText(HealthInquiryFurtherAskActivity.this.getString(IdHelper
                                    .getString(HealthInquiryFurtherAskActivity.this, "jmui_cancel_record_voice_hint")));
                            if (!mIsPressed) {
                                cancelRecord();
                            }
                        }
                    }
                    mVolumeIv.setImageResource(res[msg.what]);
                }
            } else {

            }
        }
    }

    public void initInputLayout() {
        mVolumeHandler = new ShowVolumeHandler(jmuiVoiceBtn);
        jmuiVoiceBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mIsPressed = true;
                        time1 = System.currentTimeMillis();
                        mTouchY1 = event.getY();
                        if (StorageDeviceUtil.isSdCardExist()) {
                            /**
                             * 显示录音的效果Dialog
                             */
                            if (isTimerCanceled) {
                                timer = createTimer();
                            }
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    android.os.Message msg = myHandler.obtainMessage();
                                    msg.what = START_RECORD;
                                    msg.sendToTarget();
                                }
                            }, 500);
                        } else {
                            CommonDialogHint.getInstance().showHint(HealthInquiryFurtherAskActivity.this,getString(R.string.jmui_sdcard_not_exist_toast));
                            mIsPressed = false;
                            return false;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mTouchY = event.getY();
                        //手指上滑到超出限定后，显示松开取消发送提示
                        if (mTouchY1 - mTouchY > MIN_CANCEL_DISTANCE) {
                            mVolumeHandler.sendEmptyMessage(CANCEL_RECORD);
                            if (mThread != null) {
                                mThread.exit();
                            }
                            mThread = null;
                        } else {
                            if (mThread == null) {
                                mThread = new HealthInquiryFurtherAskActivity.ObtainDecibelThread();
                                mThread.start();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mIsPressed = false;
                        time2 = System.currentTimeMillis();
                        mTouchY2 = event.getY();

                        if (time2 - time1 < 500) {
                            cancelTimer();
                            return true;
                        } else if (time2 - time1 < 1000) {
                            cancelRecord();
                        } else if (mTouchY1 - mTouchY2 > MIN_CANCEL_DISTANCE) {
                            cancelRecord();
                        } else if (time2 - time1 < 60000) {
                            finishRecord();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        cancelRecord();
                        break;
                }
                return true;
            }
        });
    }

    //初始化音频录制对话框，并进行音频录制准备工作
    File rootDir = Environment.getExternalStorageDirectory();

    private void initDialogAndStartRecord() {
        res = new int[]{
                R.drawable.jmui_mic_1,
                R.drawable.jmui_mic_2,
                R.drawable.jmui_mic_3,
                R.drawable.jmui_mic_4,
                R.drawable.jmui_mic_5,
                R.drawable.jmui_cancel_record};
        //存放录音文件目录
        if (rootDir == null) {
            rootDir = Environment.getExternalStorageDirectory();
        }

        String fileDir = rootDir.getAbsolutePath() + File.separator + getPackageName() + "/voice";
        File destDir = new File(fileDir);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        //录音文件的命名格式
        myRecAudioFile = new File(fileDir,
                new DateFormat().format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".mp3");
        if (myRecAudioFile == null) {
            cancelTimer();
            stopRecording();
            CommonDialogHint.getInstance().showHint(HealthInquiryFurtherAskActivity.this,getString(R.string.jmui_create_file_failed));
        }
        recordIndicator = new Dialog(this, R.style.jmui_record_voice_dialog);
        recordIndicator.setContentView(R.layout.jmui_dialog_record_voice);
        mVolumeIv = (ImageView) recordIndicator.findViewById(R.id.jmui_volume_hint_iv);
        mRecordHintTv = (TextView) recordIndicator.findViewById(R.id.jmui_record_voice_tv);
        mRecordHintTv.setText(this.getString(R.string.jmui_move_to_cancel_hint));
        startRecording();
        recordIndicator.show();
    }

    //关闭音频录制的提示对话框
    private void dismissRecordDialog() {
        if (recordIndicator != null) {
            recordIndicator.dismiss();
        }
    }

    //取消定时器---刷新提示对话框
    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            isTimerCanceled = true;
        }
        if (mCountTimer != null) {
            mCountTimer.cancel();
            mCountTimer.purge();
        }
    }

    // 创建定时器---刷新提示对话框
    private Timer createTimer() {
        timer = new Timer();
        isTimerCanceled = false;
        return timer;
    }

    //开始录制音频
    private MP3Recorder mRecorder;

    private void startRecording() {
        try {
            //----------------------------------------
            myRecAudioFile.createNewFile();

            mRecorder = new MP3Recorder(myRecAudioFile, mVolumeHandler);
            mRecorder.start();
            //----------------------------------------
            startTime = System.currentTimeMillis();
            mCountTimer = new Timer();
            mCountTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mTimeUp = true;
                    android.os.Message msg = mVolumeHandler.obtainMessage();
                    msg.what = 55;
                    Bundle bundle = new Bundle();
                    bundle.putInt("restTime", 5);
                    msg.setData(bundle);
                    msg.sendToTarget();
                    mCountTimer.cancel();
                }
            }, 56000);

        } catch (IOException e) {
            e.printStackTrace();
            MobclickAgent.reportError(HealthInquiryFurtherAskActivity.this, e);
        } catch (RuntimeException e) {
            MobclickAgent.reportError(HealthInquiryFurtherAskActivity.this, e);
            cancelTimer();
            dismissRecordDialog();
            if (mThread != null) {
                mThread.exit();
                mThread = null;
            }
            if (myRecAudioFile != null) {
                myRecAudioFile.delete();
            }
            if (recorder != null) {
                recorder.release();
                recorder = null;
            }
        }
        mThread = new ObtainDecibelThread();
        mThread.start();
    }

    //取消录制
    private void cancelRecord() {
        //可能在消息队列中还存在HandlerMessage，移除剩余消息
        mVolumeHandler.removeMessages(56, null);
        mVolumeHandler.removeMessages(57, null);
        mVolumeHandler.removeMessages(58, null);
        mVolumeHandler.removeMessages(59, null);
        mTimeUp = false;
        cancelTimer();
        stopRecording();
        if (recordIndicator != null) {
            recordIndicator.dismiss();
        }
        if (myRecAudioFile != null) {
            myRecAudioFile.delete();
        }
    }

    //停止录制并将文件上传
    private void finishRecord() {
        voicePath = null;
        cancelTimer();
        stopRecording();
        if (recordIndicator != null) {
            recordIndicator.dismiss();
        }

        long intervalTime = System.currentTimeMillis() - startTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            CommonDialogHint.getInstance().showHint(HealthInquiryFurtherAskActivity.this,getString(R.string.jmui_time_too_short_toast));
            myRecAudioFile.delete();
        } else {
            if (myRecAudioFile != null && myRecAudioFile.exists()) {
                MediaPlayer mp = new MediaPlayer();
                try {
                    FileInputStream fis = new FileInputStream(myRecAudioFile);
                    mp.setDataSource(fis.getFD());
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                    MobclickAgent.reportError(HealthInquiryFurtherAskActivity.this, e);
                }
                //某些手机会限制录音，如果用户拒接使用录音，则需判断mp是否存在
                if (mp != null) {
                    int duration = mp.getDuration() / 1000;//即为时长 是s
                    if (duration < 1) {
                        duration = 1;
                    } else if (duration > 60) {
                        duration = 60;
                    }
                    try {
                        /**
                         * 将文件上传，并将地址传递到服务器，并传递到另一个页面
                         */
                        uploadVoiceFile(myRecAudioFile.getName(),myRecAudioFile.getAbsolutePath());
                        //********************************************************************************************************************************************************

                    } catch (Exception e) {
                        e.printStackTrace();
                        MobclickAgent.reportError(HealthInquiryFurtherAskActivity.this, e);
                    }
                } else {
                    CommonDialogHint.getInstance().showHint(HealthInquiryFurtherAskActivity.this,getString(R.string.jmui_record_voice_permission_request));
                }

            }
        }
    }

    //停止录音，隐藏录音动画
    private void stopRecording() {
        if (mThread != null) {
            mThread.exit();
            mThread = null;
        }
        releaseRecorder();
    }

    //释放资源
    public void releaseRecorder() {
        if (recorder != null) {
            try {
                recorder.stop();
            } catch (Exception e) {
                MobclickAgent.reportError(HealthInquiryFurtherAskActivity.this, e);
            } finally {
                recorder.release();
                recorder = null;
            }
        }

        if (mRecorder != null) {
            mRecorder.stop();
        }
    }

    //***********************************************************************音频录制
    // 刷新 提示Dialog的线程
    public class ObtainDecibelThread extends Thread {

        private volatile boolean running = true;

        public void exit() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    MobclickAgent.reportError(HealthInquiryFurtherAskActivity.this, e);
                }
                if (recorder == null || !running) {
                    break;
                }
                try {
                    int x = recorder.getMaxAmplitude();
                    if (x != 0) {
                        int f = (int) (10 * Math.log(x) / Math.log(10));
                        if (f < 20) {
                            mVolumeHandler.sendEmptyMessage(0);
//                            LogUtil.e("发送的动画------recorder.getMaxAmplitude():"+x + "f(f < 20):"+f+"    ****   sendEmptyMessage(0)");
                        } else if (f < 26) {
                            mVolumeHandler.sendEmptyMessage(1);
//                            LogUtil.e("发送的动画------recorder.getMaxAmplitude():"+x + "f(f < 26):"+f+"    ****   sendEmptyMessage(1)");
                        } else if (f < 32) {
                            mVolumeHandler.sendEmptyMessage(2);
//                            LogUtil.e("发送的动画------recorder.getMaxAmplitude():"+x + "f(f < 32):"+f+"    ****   sendEmptyMessage(2)");
                        } else if (f < 38) {
                            mVolumeHandler.sendEmptyMessage(3);
//                            LogUtil.e("发送的动画------recorder.getMaxAmplitude():"+x + "f(f < 38):"+f+"    ****   sendEmptyMessage(3)");
                        } else {
                            mVolumeHandler.sendEmptyMessage(4);
//                            LogUtil.e("发送的动画------recorder.getMaxAmplitude():"+x + "f(f >= 38):"+f+"    ****   sendEmptyMessage(4)");
                        }
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    MobclickAgent.reportError(HealthInquiryFurtherAskActivity.this, e);
                }

            }
        }

    }

    //*****************************  *****************************  *****************************  *****************************  *****************************
    private KeyBoardUtil keyBoardUtil;
    public void showSoftInput(View view) {
        if(keyBoardUtil==null){
            keyBoardUtil = new KeyBoardUtil();
        }
        keyBoardUtil.showSoftInput(this,view);
    }

    public void hideSoftInput(View view) {
        if(keyBoardUtil==null){
            keyBoardUtil = new KeyBoardUtil();
        }
        keyBoardUtil.hideSoftInput(this,view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ICommonActivityRequestCode.REQUEST_CODE_CAPTURE_CAMEIA:
                if (resultCode == RESULT_OK) {
                    if (mCurrentPhotoPath != null) {
                        ImageCropHandler.beginCrop(HealthInquiryFurtherAskActivity.this, Uri.fromFile(new File(mCurrentPhotoPath)));
                        /**
                         * 是否进行裁剪：裁剪则进行上面的操作，否则，直接进行文件上传
                         */
                    }
                }
                break;
            case ICommonActivityRequestCode.REQUEST_PICK:
                if (resultCode == RESULT_OK) {
                    ImageCropHandler.beginCrop(HealthInquiryFurtherAskActivity.this, data.getData());
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

    //********************************************************************************************************************
    //TODO 发送追问问题
    protected void sendAppendProblem() {
        if (jmuiChatInputEt.length() < 1 && voicePath == null && imagePath == null) {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_health_inquiry_append_no_question));
            return;
        }

        if (voicePath != null && !voicePath.toLowerCase().startsWith(ICommonData.HTTP_PREFIX)) {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_health_inquiry_append_voice_upload_fail));
            return;
        }

        if (imagePath != null && !imagePath.toLowerCase().startsWith(ICommonData.HTTP_PREFIX)) {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_health_inquiry_append_image_upload_fail));
            return;
        }

        presenter.sendProblem(this);
    }

    //TODO 设置追问结果
    public void setFurtherInquiryResult(Object results) {
        if (results != null && results instanceof String) {
            //{"code":"200","message":"返回成功","object":{"errmsg":"首问字数过少"}}
            //
            JsonObject result = new ParseToGsonUtil(results.toString()).getJsonObject();
            if(result.has("code") && ICommonData.HTTP_OK.equals(result.get("code").getAsString()) &&
                    result.has("object") && result.get("object").getAsJsonObject().has("errmsg")){
                CommonDialogHint.getInstance().showHint(HealthInquiryFurtherAskActivity.this,StringDecodeUtil.
                        decodeUnicode(result.get("object").getAsJsonObject().get("errmsg").getAsString()));
            }else if (result.has("code") && ICommonData.HTTP_OK.equals(result.get("code").getAsString())) {
                jmuiChatInputEt.setText("");
                voicePath = null;
                imagePath = null;
                getAnswer();
                setResult(ICommonActivityResultCode.RELOAD_DATA);
            } else {
                if (result.has("error_msg")) {
                    CommonDialogHint.getInstance().showHint(HealthInquiryFurtherAskActivity.this,
                            StringDecodeUtil.decodeUnicode(result.get("error_msg").getAsString()));
                } else if (result.has("code") && ICommonData.HTTP_OK.equals(result.get("code").getAsString())) {
                    jmuiChatInputEt.setText("");
                    voicePath = null;
                    imagePath = null;
                    getAnswer();
                    setResult(ICommonActivityResultCode.RELOAD_DATA);
                } else if (result.has("code") && !ICommonData.HTTP_OK.equals(result.get("code").getAsString())) {
                    CommonDialogHint.getInstance().showHint(this,result.get("message").getAsString());
                }else{
                    CommonDialogHint.getInstance().showHint(this,getString(R.string.hint_common_submit_data_fail));
                }
            }
        }
    }
    //********************************************************************************************************************

    public void uploadVoiceFile(String name,String imagePath){

        String url = "http://picture.quanjiakan.com:9080/familycare-binary/upload?&platform=2&project=1" +
                "&token=" + BaseApplication.getInstances().getLoginInfo().getToken() +
                "&memberId=" + BaseApplication.getInstances().getLoginInfo().getUserId() +
                "&storage=14";//TODO 这个需要---如果没有可能会报参数错误的异常

        HashMap<String, String> paramsFile = new HashMap<>();
        paramsFile.put("file", imagePath);
        paramsFile.put("filename", name);
        paramsFile.put("audio", imagePath);
        UploadUtil.uploadVoiceFile(HealthInquiryFurtherAskActivity.this, url, null, paramsFile);
    }

    //TODO 已经可以提取到Presenter中，不过，URL则需要从Params中进行获取
    public void uploadImageFile(String name,String path){
        String url = "http://picture.quanjiakan.com:9080/familycare-binary/upload?&platform=2&project=1" +
                "&token=" + BaseApplication.getInstances().getLoginInfo().getToken() +
                "&memberId=" + BaseApplication.getInstances().getLoginInfo().getUserId() +
                "&storage=13";//TODO 这个需要---如果没有可能会报参数错误的异常

        HashMap<String, String> paramsFile = new HashMap<>();
        paramsFile.put("file", path);
        paramsFile.put("filename", file.getName());
        paramsFile.put("image", file.getAbsolutePath());
        UploadUtil.uploadFile(HealthInquiryFurtherAskActivity.this, url, null, paramsFile);
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
                        imagePath = json.getString("message");
                        jmuiChatInputEt.setText("");
                        voicePath = null;
                        sendAppendProblem();
                    } else {
                        if(json.has("message") && json.getString("message")!=null){
                            CommonDialogHint.getInstance().showHint(this, json.getString("message"));
                        }else{
                            CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_common_submit_upload_fail));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    MobclickAgent.reportError(HealthInquiryFurtherAskActivity.this, e);
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

    // TODO 处理语音上传完成后的数据
    public void afterVoiceUpload(Object result) {
        if (result != null && result instanceof String) {
            //{"code":"200","message":"http://picture.quanjiakan.com/quanjiakan/resources/chunyu/audios/20171122115357_97cjyywlk28ey8uv5i5n.mp3"}
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
                        voicePath = json.getString("message");
                        imagePath = null;
                        jmuiChatInputEt.setText("");
                        sendAppendProblem();
                    } else {
                        if(json.has("message") && json.getString("message")!=null){
                            CommonDialogHint.getInstance().showHint(this, json.getString("message"));
                        }else{
                            CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_common_submit_upload_fail));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    MobclickAgent.reportError(HealthInquiryFurtherAskActivity.this, e);
                }

            } else {
                //文件上传失败
                CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_common_submit_upload_fail));
            }
        }
    }
}
