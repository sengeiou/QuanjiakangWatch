package com.quanjiakan.activity.common.index.devices.old.health.report;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.BaseFragment;
import com.quanjiakan.constants.ICommonActivityRequestCode;
import com.quanjiakan.constants.ICommonActivityResultCode;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net_presenter.DeviceHealthReportPresenter;
import com.quanjiakan.net_presenter.DeviceInfoPresenter;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.view.dialog.ChangeBirthDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Administrator on 2017/10/17.
 */

public class HealthReportFragment extends BaseFragment {
    @BindView(R.id.tvSearchTimer)
    TextView tvSearchTimer;
    @BindView(R.id.tvStartSearch)
    TextView tvStartSearch;
    @BindView(R.id.rlSearchLine)
    RelativeLayout rlSearchLine;
    @BindView(R.id.health_step_today_img)
    ImageView healthStepTodayImg;
    @BindView(R.id.health_step_today_hint)
    TextView healthStepTodayHint;
    @BindView(R.id.health_step_today_value)
    TextView healthStepTodayValue;
    @BindView(R.id.health_step_today)
    LinearLayout healthStepToday;
    @BindView(R.id.health_distance_today_img)
    ImageView healthDistanceTodayImg;
    @BindView(R.id.health_distance_today_hint)
    TextView healthDistanceTodayHint;
    @BindView(R.id.health_distance_today_value)
    TextView healthDistanceTodayValue;
    @BindView(R.id.health_distance_today)
    LinearLayout healthDistanceToday;
    @BindView(R.id.health_calorie_today_img)
    ImageView healthCalorieTodayImg;
    @BindView(R.id.health_calorie_today_hint)
    TextView healthCalorieTodayHint;
    @BindView(R.id.health_calorie_today_value)
    TextView healthCalorieTodayValue;
    @BindView(R.id.health_calorie_today)
    LinearLayout healthCalorieToday;
    @BindView(R.id.ll_data_today)
    LinearLayout llDataToday;
    @BindView(R.id.tvHeartName)
    TextView tvHeartName;
    @BindView(R.id.tvHeartHint)
    TextView tvHeartHint;
    @BindView(R.id.line_chart_heart)
    LineChartView lineChartHeart;
    @BindView(R.id.tvNodataHeartRate)
    TextView tvNodataHeartRate;
    @BindView(R.id.rlHeartChart)
    RelativeLayout rlHeartChart;
    @BindView(R.id.tvBloodPressureName)
    TextView tvBloodPressureName;
    @BindView(R.id.tvSystolic)
    TextView tvSystolic;
    @BindView(R.id.viewSystolic)
    View viewSystolic;
    @BindView(R.id.tvDiastolic)
    TextView tvDiastolic;
    @BindView(R.id.viewDiastolic)
    View viewDiastolic;
    @BindView(R.id.llBloodPressure)
    LinearLayout llBloodPressure;
    @BindView(R.id.llBloodPressureHint)
    TextView llBloodPressureHint;
    @BindView(R.id.line_chart_blood)
    LineChartView lineChartBlood;
    @BindView(R.id.tvNodataBloodPressure)
    TextView tvNodataBloodPressure;
    @BindView(R.id.rlBloodPressure)
    RelativeLayout rlBloodPressure;
    //***************************************************
    Unbinder unbinder;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat simpleDateFormatForSearch = new SimpleDateFormat("yyyy.MM.dd");
    //***************************************************
    private DeviceHealthReportPresenter deviceHealthReportPresenter;
    private DeviceInfoPresenter deviceInfoPresenter;
    private ChangeBirthDialog day_dialog;

    private String IMEI;

    //TODO 实际数据
    private List<Long> rates = new ArrayList<>();
    private List<Long> diastoles = new ArrayList<>();
    private List<Long> shrinks = new ArrayList<>();
    //TODO 下方显示的市价
    private List<String> rateTimes = new ArrayList<>();
    private List<String> bloodTimes = new ArrayList<>();

    private Handler mHandler = new Handler();
    private boolean isFirstLoadHeartRate =false;
    private boolean isFirstLoadBloodPressure =false;
    private LineChartData lineData;


    /**
     * ************************************************************************************************************************
     * 生命周期方法
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_health_report, container, false);//TODO 实例化整个布局
        unbinder = ButterKnife.bind(this, view);
        //TODO 设置默认值
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    /**
     * ************************************************************************************************************************
     */

    public void initView(){
        //TODO 查询栏
        rlSearchLine.setVisibility(View.VISIBLE);
        tvSearchTimer.setText(simpleDateFormat.format(new Date()));
        tvSearchTimer.setTag(simpleDateFormatForSearch.format(new Date()));
        tvStartSearch.setText(R.string.device_health_dynamics_report_search_btn);

        llDataToday.setVisibility(View.VISIBLE);
        //TODO 今日步数
        healthStepToday.setVisibility(View.VISIBLE);
        healthStepTodayImg.setImageResource(R.drawable.presentation_ico_steps);
        healthStepTodayHint.setText(R.string.device_health_dynamics_report_today_data_step);
        healthStepTodayValue.setText(R.string.device_health_dynamics_report_today_data_step_init);
        //TODO 今日里程
        healthDistanceToday.setVisibility(View.VISIBLE);
        healthDistanceTodayImg.setImageResource(R.drawable.presentation_ico_mileage);
        healthDistanceTodayHint.setText(R.string.device_health_dynamics_report_today_data_distance);
        healthDistanceTodayValue.setText(R.string.device_health_dynamics_report_today_data_distance_init);
        //TODO 今日卡路里
        healthCalorieToday.setVisibility(View.VISIBLE);
        healthCalorieTodayImg.setImageResource(R.drawable.presentation_ico_calorie);
        healthCalorieTodayHint.setText(R.string.device_health_dynamics_report_today_data_calorie);
        healthCalorieTodayValue.setText(R.string.device_health_dynamics_report_today_data_calorie_init);

        //TODO 心率图表
        rlHeartChart.setVisibility(View.VISIBLE);
        tvHeartName.setText(R.string.device_health_dynamics_report_today_data_heartrate_chart_name);
        tvHeartHint.setText(R.string.device_health_dynamics_report_today_data_heartrate_chart_hint);
        // 默认不显示不在线提醒
        tvNodataHeartRate.setVisibility(View.GONE);
        tvNodataHeartRate.setText(R.string.device_health_dynamics_report_today_data_device_no_online);
        lineChartHeart.setVisibility(View.VISIBLE);

        //TODO 血压图表
        rlBloodPressure.setVisibility(View.VISIBLE);
        tvBloodPressureName.setText(R.string.device_health_dynamics_report_today_data_bloodpressure_chart_name);
        //TODO 图表显示提示
        llBloodPressure.setVisibility(View.VISIBLE);
        tvSystolic.setText(R.string.device_health_dynamics_report_today_data_bloodpressure_chart_systolic);
        viewSystolic.setBackgroundColor(getResources().getColor(R.color.color_f46a16));
        tvDiastolic.setText(R.string.device_health_dynamics_report_today_data_bloodpressure_chart_diastolic);
        viewDiastolic.setBackgroundColor(getResources().getColor(R.color.color_00b2b2));
        llBloodPressureHint.setText(R.string.device_health_dynamics_report_today_data_bloodpressure_chart_hint);
        // 默认不显示不在线提醒
        tvNodataBloodPressure.setVisibility(View.GONE);
        tvNodataBloodPressure.setText(R.string.device_health_dynamics_report_today_data_device_no_online);
        lineChartBlood.setVisibility(View.VISIBLE);

        deviceHealthReportPresenter = new DeviceHealthReportPresenter();
        deviceInfoPresenter = new DeviceInfoPresenter();


        //TODO 获取初始数据
        deviceHealthReportPresenter.getHeartRateAndBloodPressure(this);
        deviceHealthReportPresenter.getCurrentData(this);
    }

    //TODO 设置值
    public void setStepValue(String todaySteps){
        healthStepTodayValue.setText(todaySteps+getString(R.string.device_health_dynamics_report_today_data_step_unit));
    }

    public void setDistanceValue(String todaySteps){
        healthDistanceTodayValue.setText(todaySteps+getString(R.string.device_health_dynamics_report_today_data_distance_unit));
    }

    public void setCalorieValue(String todaySteps){
        healthCalorieTodayValue.setText(todaySteps+getString(R.string.device_health_dynamics_report_today_data_calorie_unit));
    }

    //*****************************************************************


    /**
     * ************************************************************************************************************************
     */
    @Override
    public Object getParamter(int type) {
        switch (type) {
            case IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CHART: {
                /*
                params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_IMEI_BIG),
                params.get(IParamsName.PARAMS_HEALTH_REPORT_HISTORY_TIME)
                 */
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_IMEI_BIG, IMEI);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                params.put(IParamsName.PARAMS_HEALTH_REPORT_HISTORY_TIME, tvSearchTimer.getTag().toString());
                return params;
            }
            case IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CURRENT_DATA: {
                /*
                params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_IMEI_BIG)
                 */
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_IMEI_BIG, IMEI);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                return params;
            }
            case IPresenterBusinessCode.DEVICE_WATCH_INFO: {
                /*
                params.get(IParamsName.PARAMS_COMMON_PLATFORM),
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_IMEI)
                 */
                HashMap<String, String> params = new HashMap<>();
                params.put(IParamsName.PARAMS_COMMON_IMEI, IMEI);
                params.put(IParamsName.PARAMS_COMMON_TOKEN, BaseApplication.getInstances().getLoginInfo().getToken());
                params.put(IParamsName.PARAMS_COMMON_PLATFORM, IHttpUrlConstants.PLATFORM_ANDROID);
                return params;
            }
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.DEVICE_WATCH_INFO: {
                getDialog(getActivity(), getString(R.string.hint_health_inquiry_last_10_problem_hint));
                break;
            }
            case IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CURRENT_DATA: {
                getDialog(getActivity(), getString(R.string.hint_health_inquiry_last_10_problem_hint));
                break;
            }
            case IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CHART: {
                getDialog(getActivity(), getString(R.string.hint_health_inquiry_last_10_problem_hint));
                break;
            }
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type) {
            case IPresenterBusinessCode.DEVICE_WATCH_INFO: {
                break;
            }
            case IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CURRENT_DATA: {
                break;
            }
            case IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CHART: {
                break;
            }
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type) {
            case IPresenterBusinessCode.DEVICE_WATCH_INFO: {
                setDeviceInfoResult(result);
                break;
            }
            case IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CURRENT_DATA: {
                setCurrentResult(result);
                break;
            }
            case IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CHART: {
                setChartResult(result);
                break;
            }
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type) {
            case IPresenterBusinessCode.DEVICE_WATCH_INFO: {
                break;
            }
            case IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CURRENT_DATA: {
                break;
            }
            case IPresenterBusinessCode.DEVICE_WATCH_HEALTH_REPORT_CHART: {
                break;
            }
        }
        if (errorMsg != null && errorMsg.toString().length() > 0) {
            CommonDialogHint.getInstance().showHint(getActivity(), errorMsg.toString());
        } else {
            CommonDialogHint.getInstance().showHint(getActivity(), getString(R.string.error_common_net_request_fail));
        }
    }


    /**
     * 需要变更下，这里没有适应，限制时间选择
     */
    public void showTimeDialog(){
        day_dialog = new ChangeBirthDialog(getActivity());
        day_dialog.setBirthdayListener(new ChangeBirthDialog.OnBirthListener() {
            @Override
            public void onClick(String year, String month, String day) {
                if ("".equals(year) || "".equals(month) || "".equals(day)) {

                } else {
                    String yearSub = year.substring(0, 4);
                    String monthSub = month.substring(0, 2);
                    String daySub = day.substring(0, 2);
                    tvSearchTimer.setText(yearSub + "-" + monthSub + "-" + daySub);
                    tvSearchTimer.setTag(""+yearSub + "." + monthSub + "." + daySub);

                }
            }
        });

        day_dialog.show();
    }

    @OnClick({R.id.tvSearchTimer, R.id.tvStartSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvSearchTimer: {
                //TODO 弹出时间选择对话框
                showTimeDialog();
                break;
            }
            case R.id.tvStartSearch: {
                if(isValid()){
                    deviceHealthReportPresenter.getHeartRateAndBloodPressure(this);
                }
                break;
            }
        }
    }

    public boolean isValid(){
        if(tvSearchTimer.getTag()==null || tvSearchTimer.getTag().toString().length()<1){
            CommonDialogHint.getInstance().showHint(getActivity(),getString(R.string.device_health_dynamics_report_today_data_chart_hint));
            return false;
        }
        return true;
    }

    public void setChartResult(Object result){
        if(result!=null && result instanceof String){
            try {
                JSONObject object = new JSONObject(result.toString());
                JSONObject jsonObject = object.getJSONObject("object");
                if (jsonObject.has("bloodreport") && jsonObject.has("heartreport")) {
                    JSONArray heartreportArray = jsonObject.getJSONArray("heartreport");
                    if (heartreportArray.length() > 0) {
                        rates.clear();
                        rateTimes.clear();
                        tvNodataHeartRate.setVisibility(View.GONE);
                        lineChartHeart.setVisibility(View.VISIBLE);
                        if (heartreportArray.length()>=10) {
                            for (int i = 9; i >=0; i--) {
                                Long value = heartreportArray.getJSONObject(i).getLong("value");
                                if (value<500) {
                                    rates.add(value);
                                    rateTimes.add(changeTime(heartreportArray.getJSONObject(i).getString("time")));
                                }
                            }
                        }else {
                            for (int i = heartreportArray.length()-1; i >=0; i--) {
                                Long value = heartreportArray.getJSONObject(i).getLong("value");
                                if (value<500) {
                                    rates.add(value);
                                    rateTimes.add(changeTime(heartreportArray.getJSONObject(i).getString("time")));
                                }
                            }
                        }
                        generateInitialLineDataHeartRate();
                    } else {
                        rates.clear();
                        rateTimes.clear();
                        tvNodataHeartRate.setVisibility(View.VISIBLE);
                        lineChartHeart.setVisibility(View.GONE);
                    }

                    JSONArray bloodreportArray = jsonObject.getJSONArray("bloodreport");
                    if (bloodreportArray.length() > 0) {
                        diastoles.clear();
                        shrinks.clear();
                        bloodTimes.clear();
                        tvNodataBloodPressure.setVisibility(View.GONE);
                        lineChartBlood.setVisibility(View.VISIBLE);
                        if (bloodreportArray.length()>=10) {
                            for (int i = 9; i >=0; i--) {
                                long diastole = bloodreportArray.getJSONObject(i).getLong("diastole");
                                long shrink = bloodreportArray.getJSONObject(i).getLong("shrink");
                                if (diastole<500&&shrink<500) {
                                    diastoles.add(diastole);
                                    shrinks.add(shrink);
                                    bloodTimes.add(changeTime(bloodreportArray.getJSONObject(i).getString("time")));
                                }
                            }
                        }else {
                            for (int i = bloodreportArray.length()-1; i >=0; i--) {
                                long diastole = bloodreportArray.getJSONObject(i).getLong("diastole");
                                long shrink = bloodreportArray.getJSONObject(i).getLong("shrink");
                                if (diastole<500&&shrink<500) {
                                    diastoles.add(diastole);
                                    shrinks.add(shrink);
                                    bloodTimes.add(changeTime(bloodreportArray.getJSONObject(i).getString("time")));
                                }
                            }
                        }
                        generateInitialLineDataBloodPressure();
                    }else {
                        diastoles.clear();
                        shrinks.clear();
                        bloodTimes.clear();
                        tvNodataBloodPressure.setVisibility(View.VISIBLE);
                        lineChartBlood.setVisibility(View.GONE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{

        }
    }

    //TODO 设置当前数据
    public void setCurrentResult(Object result){
        if(result!=null && result instanceof String){
            try {
                JSONObject jsonObject = new JSONObject(result.toString());
                if (jsonObject.has("code") && ICommonData.HTTP_OK.equals(jsonObject.getString("code")) && jsonObject.has("object")) {
                    JSONObject object = jsonObject.getJSONObject("object");
                    if (object.has("calorie") && object.has("mileage") && object.has("steps")) {
                        //老年人手表数据:{"code":"200","message":"返回成功","object":{"calorie":0,"mileage":0,"steps":0}}
                        long calorie = object.getLong("calorie");
                        long mileage = object.getLong("mileage");
                        long steps = object.getLong("steps");

                        healthStepTodayValue.setText(steps + getString(R.string.device_health_dynamics_report_today_data_step_unit));
                        if (mileage>=1000) {
                            healthDistanceTodayValue.setText((double)mileage / 1000 + getString(R.string.device_health_dynamics_report_today_data_distance_unit_kilo));
                        } else {
                            healthDistanceTodayValue.setText(mileage + getString(R.string.device_health_dynamics_report_today_data_distance_unit));
                        }
                        healthCalorieTodayValue.setText(calorie + getString(R.string.device_health_dynamics_report_today_data_calorie_unit));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setDeviceInfoResult(Object result){
        if(result!=null && result instanceof String){

        }
    }

    private String changeTime(String time) {
        int index = time.lastIndexOf(":");
        return time.substring(index - 5, index);
    }

    private void generateInitialLineData() {
        generateInitialLineDataHeartRate();
        generateInitialLineDataBloodPressure();
    }


    private void generateInitialLineDataHeartRate() {
        if (rates.size()>0){
            //心率
            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int i = 0; i < rates.size(); i++) {
                values.add(new PointValue(i, rates.get(i)));
                axisValues.add(new AxisValue(i).setLabel(rateTimes.get(i)));
            }
            Line line = new Line(values);

            line.setShape(ValueShape.CIRCLE);
            line.setColor(Color.parseColor("#2ebbbb")).setCubic(false);
            line.setStrokeWidth(1);
            line.setPointRadius(3);
            line.setHasLabels(false);
            line.setHasLabelsOnlyForSelected(true);
            line.setHasPoints(true);

            List<Line> lines = new ArrayList<Line>();
            lines.add(line);


            lineData = new LineChartData(lines);
            lineData.setValueLabelBackgroundEnabled(false);
            lineData.setValueLabelsTextColor(getActivity().getResources().getColor(R.color.font_color_999999));
            lineData.setValueLabelTextSize(8);
            lineData.setAxisXBottom(new Axis(axisValues).setHasLines(false).setTextColor(getActivity().getResources().getColor(R.color.font_color_999999)).setMaxLabelChars(3).setTextSize(8));
            lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(4).setTextSize(8).setTextColor(getActivity().getResources().getColor(R.color.font_color_999999)));

            lineChartHeart.setLineChartData(lineData);
            lineChartHeart.setViewportCalculationEnabled(true);
            resetViewportHeart();
        }
        delaySetHeartRate();
    }


    private void generateInitialLineDataBloodPressure() {
        //血压
        if (shrinks.size()>0){
            List<AxisValue> axisValuesP = new ArrayList<AxisValue>();
            List<PointValue> valueD = new ArrayList<PointValue>();
            List<PointValue> valueS = new ArrayList<PointValue>();


            for (int i = 0; i < diastoles.size(); ++i) {
                valueD.add(new PointValue(i, diastoles.get(i)));
                valueS.add(new PointValue(i, shrinks.get(i)));
                axisValuesP.add(new AxisValue(i).setLabel(bloodTimes.get(i)));
            }

            Line lineD = new Line(valueD);
            lineD.setShape(ValueShape.CIRCLE);
            lineD.setColor(getActivity().getResources().getColor(R.color.color_f46a16)).setCubic(false);
            lineD.setStrokeWidth(1);
            lineD.setPointRadius(3);
            lineD.setHasLabels(false);
            lineD.setHasLabelsOnlyForSelected(true);
            lineD.setHasPoints(true);

            Line lineS = new Line(valueS);
            lineS.setShape(ValueShape.CIRCLE);
            lineS.setColor(getActivity().getResources().getColor(R.color.color_00b2b2)).setCubic(false);
            lineS.setStrokeWidth(1);
            lineS.setPointRadius(3);
            lineS.setHasLabels(false);
            lineS.setHasLabelsOnlyForSelected(true);
            lineS.setHasPoints(true);


            List<Line> linesP = new ArrayList<Line>();
            linesP.add(lineD);
            linesP.add(lineS);


            lineData = new LineChartData(linesP);
            lineData.setValueLabelBackgroundEnabled(false);
            lineData.setValueLabelsTextColor(getActivity().getResources().getColor(R.color.font_color_999999));
            lineData.setValueLabelTextSize(8);
            lineData.setAxisXBottom(new Axis(axisValuesP).setHasLines(false).setTextColor(getActivity().getResources().getColor(R.color.font_color_999999)).setMaxLabelChars(3).setTextSize(8));
            lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(4).setTextSize(8).setTextColor(getActivity().getResources().getColor(R.color.font_color_999999)));

            lineChartBlood.setLineChartData(lineData);
            lineChartBlood.setViewportCalculationEnabled(true);
            resetViewportBlood();

        }

        delaySetBloolPressure();
    }

    private void resetViewportHeart() {
        final Viewport v = new Viewport(lineChartHeart.getMaximumViewport());
        v.bottom = 0;
        v.top = Collections.max(rates) + 50;
        v.left = 0;
        v.right = rates.size();
        lineChartHeart.setMaximumViewport(v);
        lineChartHeart.setCurrentViewport(v);
    }

    private void resetViewportBlood() {
        final Viewport v = new Viewport(lineChartBlood.getMaximumViewport());
        v.bottom = 0;
        v.top = Collections.max(shrinks) + 50;
        v.left = 0;
        v.right = shrinks.size();
        lineChartBlood.setMaximumViewport(v);
        lineChartBlood.setCurrentViewport(v);
    }

    public void delaySetHeartRate(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //TODO 延迟设置，直接设置存在ChartTouchHandler中isClickPersist设置不成功的问题
                //TODO 设置显示多行
//                lineChart.setShowMultiLineValue(true);
                //TODO 设置点击持久显示
                lineChartHeart.setClickPersist(true);
                if(!isFirstLoadHeartRate){//TODO 仅多走一次，让设置生效
                    isFirstLoadHeartRate = true;
                    generateInitialLineDataHeartRate();
                }
            }
        },500);
    }

    public void delaySetBloolPressure(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //TODO 延迟设置，直接设置存在ChartTouchHandler中isClickPersist设置不成功的问题
                //TODO 设置显示多行
                lineChartBlood.setShowMultiLineValue(true);
                //TODO 设置点击持久显示
                lineChartBlood.setClickPersist(true);
                if(!isFirstLoadBloodPressure){//TODO 仅多走一次，让设置生效
                    isFirstLoadBloodPressure = true;
                    generateInitialLineDataBloodPressure();
                }
            }
        },500);
    }
}
