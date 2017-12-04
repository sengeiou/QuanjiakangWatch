package com.quanjiakan.activity.common.index.bind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.common.main.MainActivity;
import com.quanjiakan.constants.IParamsIntValue;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.device.entity.CommonNattyData;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.umeng.analytics.MobclickAgent;
import com.wbj.ndk.natty.client.NattyProtocolFilter;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/20.
 */

public class BindStateActivity extends BaseActivity {

    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.state_img)
    ImageView stateImg;
    @BindView(R.id.state_hint)
    TextView stateHint;
    @BindView(R.id.state_hint_subtext)
    TextView stateHintSubtext;
    @BindView(R.id.state_btn)
    TextView stateBtn;

    public static final int WAIT = 1;
    public static final int ACCESS = 2;
    public static final int REJECT = 3;
    public static final int NET_ERROR = 4;

    private int currentBindState = 0;
    private String deviceIMEI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bind_state);
        ButterKnife.bind(this);
        initTitle();
        currentBindState = getIntent().getIntExtra(IParamsName.PARAMS_COMMON_BIND_STATE,REJECT);
        initView();
        deviceIMEI = getIntent().getStringExtra(IParamsName.PARAMS_COMMON_IMEI);
        if(deviceIMEI==null || deviceIMEI.length()<1){
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_error_activity_parameters), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            return;
        }
        initView();
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

    @OnClick({R.id.ibtn_back, R.id.state_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back: {
                switch (currentBindState) {
                    case WAIT:
                        setResult(RESULT_CANCELED);
                        break;
                    case ACCESS:
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra(IParamsName.PARAMS_MAIN_TYPE, IParamsIntValue.COMMON_MAIN_TYPE_SHOW_FRESH_LIST);
                        startActivity(intent);
                        finish();
                        return;
                    case REJECT:
                        setResult(RESULT_CANCELED);
                        break;
                    case NET_ERROR:
                        setResult(RESULT_CANCELED);
                        break;
                    default:
                        setResult(RESULT_CANCELED);
                        break;
                }
                finish();
                break;
            }
            case R.id.state_btn: {
                switch (currentBindState) {
                    case WAIT:
                        setResult(RESULT_CANCELED);
                        break;
                    case ACCESS:
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra(IParamsName.PARAMS_MAIN_TYPE, IParamsIntValue.COMMON_MAIN_TYPE_SHOW_FRESH_LIST);
                        startActivity(intent);
                        finish();
                        return;
                    case REJECT:
                        setResult(RESULT_CANCELED);
                        break;
                    case NET_ERROR:
                        setResult(RESULT_CANCELED);
                        break;
                    default:
                        setResult(RESULT_CANCELED);
                        break;
                }
                finish();
                break;
            }
        }
    }

    public void initTitle(){
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setVisibility(View.VISIBLE);
    }

    public void initView(){
        switch (currentBindState) {
            case WAIT:
                tvTitle.setText(R.string.hint_bind_state_title_wait);
                stateHint.setText(R.string.hint_bind_state_hint_wait);
                stateHintSubtext.setVisibility(View.VISIBLE);
                stateHintSubtext.setText(R.string.hint_bind_state_result_wait);
                stateBtn.setVisibility(View.GONE);
                stateImg.setImageResource(R.drawable.bind_state_moment);
                break;
            case ACCESS:
                tvTitle.setText(R.string.hint_bind_state_title_success);
                stateHint.setText(R.string.hint_bind_state_hint_success);
                stateHintSubtext.setVisibility(View.GONE);
                stateBtn.setVisibility(View.VISIBLE);
                stateBtn.setText(R.string.hint_bind_state_result_success);
                stateBtn.setTextColor(getResources().getColor(R.color.color_00b2b2));
                stateImg.setImageResource(R.drawable.bind_state_ok);
                break;
            case REJECT:
                tvTitle.setText(R.string.hint_bind_state_title_wait);
                stateHint.setText(R.string.hint_bind_state_hint_fail);
                stateHintSubtext.setVisibility(View.GONE);
                stateBtn.setVisibility(View.VISIBLE);
                stateBtn.setText(R.string.hint_bind_state_result_fail);
                stateBtn.setTextColor(getResources().getColor(R.color.color_00b2b2));
                stateImg.setImageResource(R.drawable.bind_state_no);
                break;
            case NET_ERROR:
                tvTitle.setText(R.string.hint_bind_state_title_wait);
                stateHint.setText(R.string.hint_bind_state_hint_net_error);
                stateHintSubtext.setVisibility(View.GONE);
                stateBtn.setVisibility(View.VISIBLE);
                stateBtn.setText(R.string.hint_bind_state_result_net_error);
                stateBtn.setTextColor(getResources().getColor(R.color.font_color_CCCCCC));
                stateImg.setImageResource(R.drawable.bind_state_connect);
                break;
            default:
                tvTitle.setText(R.string.hint_bind_state_title_wait);
                stateHint.setText(R.string.hint_bind_state_hint_wait);
                stateHintSubtext.setVisibility(View.VISIBLE);
                stateHintSubtext.setText(R.string.hint_bind_state_result_wait);
                stateBtn.setVisibility(View.GONE);
                stateImg.setImageResource(R.drawable.bind_state_moment);
                break;
        }
    }

    @Subscribe(threadMode = org.greenrobot.eventbus.ThreadMode.MAIN)
    public void onCommonNattyData(CommonNattyData msg) {
        switch (msg.getType()) {
            case NattyProtocolFilter.DISPLAY_UPDATE_DATA_COMMON_BROADCAST:
                //{"IMEI":"355637053077723","Category":"BindConfirmReq","Proposer":"","Answer":"Agree"}
                //{"IMEI":"355637053077723","Category":"BindConfirmReq","Answer":"Agree"}
                //{"IMEI":"355637053995130","Category": "BindConfirmReq","Proposer":"","Answer":"Agree"}}
                //{"IMEI":"355637052788452","Category":"BindConfirmReq","Proposer":"18011935659","Answer":"Agree"}
                String stringData = msg.getData();
                try{
                    if(stringData!=null && stringData.contains(deviceIMEI) &&
                            (       stringData.contains("BindConfirmReq") ||
                                    stringData.contains("BindConfirm") ||
                                    stringData.toLowerCase().contains("bindconfirmreq") ||
                                    stringData.toLowerCase().contains("bindconfirm")  )
                            ){
                        if((stringData.contains("Agree") ||stringData.toLowerCase().contains("agree"))){
                            currentBindState = ACCESS;
                            initTitle();
                            initView();
                        }else if(( stringData.contains("Reject") || stringData.toLowerCase().contains("reject"))){
                            currentBindState = REJECT;
                            initTitle();
                            initView();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }
}
