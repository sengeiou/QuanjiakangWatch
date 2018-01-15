package com.quanjiakan.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.widget.wheelview.AbstractWheelTextAdapter;
import com.quanjiakan.util.widget.wheelview.OnWheelChangedListener;
import com.quanjiakan.util.widget.wheelview.OnWheelScrollListener;
import com.quanjiakan.util.widget.wheelview.WheelView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 日期选择对话框
 *
 * @author ywl
 */
public class CommonTimeSelectDialog extends Dialog implements android.view.View.OnClickListener {

    private Context context;
    private WheelView wvYear;
    private WheelView wvMonth;
    private WheelView wvDay;

    private View vChangeBirth;
    private View vChangeBirthChild;
    private TextView btnSure;
    private TextView btnCancel;

    private ArrayList<String> arry_years = new ArrayList<String>();
    private ArrayList<String> arry_months = new ArrayList<String>();
    private ArrayList<String> arry_days = new ArrayList<String>();

    private CalendarTextAdapter mYearAdapter;
    private CalendarTextAdapter mMonthAdapter;
    private CalendarTextAdapter mDaydapter;

    private int monthRange;
    private int dayRange;

    //TODO 当前的年月
    private final int currentYear = getCurrentYear();
    private final int currentMonth = getCurrentMonth();
    private final int currentDay = getCurrentDay();

    private int maxTextSize = 24;
    private int minTextSize = 14;

    //TODO 当前选中的时间
    private String selectedYearString;
    private String selectedMonthString;
    private String selectedDayString;

    //TODO 当前选择的时间 ---- 整形值
    private int selectedYearIntValue = getCurrentYear();
    private int selectedMonthIntValue = getCurrentMonth();
    private int selectedDayIntValue = getCurrentDay();

    private OnBirthListener onBirthListener;

    /**
     * TODO 限制类型
     * 当选择before时，限制
     */
    public enum DIALOG_TYPE {
        BEFORE,//TODO 往前选择日期，包含当天
        BEFORE_EXCEPT_TODAY,//TODO 往前选择日期，不包含当天
        AFTER,//TODO 往后选择日期，包含当天
        AFTER_EXCEPT_TODAY,//TODO 往后选择日期，不包含当天
        NORMAL,//TODO 前后皆可选择，含当天时间
        NOLIMIT //TODO 默认值（前后120年）
    }

    private static final int LIMIT_0 = 0;//TODO 限制为当前范围(使用默认的限制长度)，即当前，年月日

    //TODO 无限制时的默认限制长度
    private static final int LIMIT_120 = 120;//TODO 限制默认范围为一百二十---为年龄选择做兼容
    private static final int LIMIT_12 = 12;//TODO 限制默认范围为一百二十---为年龄选择做兼容

    //TODO 年月日的限制时间---初始化范围时使用  往前算的时间范围
    private int firstFinalDay = LIMIT_0;//TODO 最后一个月 限制的日期范围（从月初，或月末算起，当总的限制天数在当前月内时除外）
    private int firstStartDay = LIMIT_0;//TODO 当前月的日期范围（从当前日开始算起）
    private int firstFinalYear = LIMIT_0;//TODO 限制的年份上限
    private int firstFinalMonth = LIMIT_0;//TODO 限制的月份的上限

    //TODO 往后算的时间范围----规范后的数据
    /**
     * 分别是
     * 当前月的日期范围（需要根据类型来看：1号到现在；现在到月底）
     * 最后一月的日期范围（也需要根据类型来看是那种计算方式）
     * 总共的间隔的月数
     * 年份的上限或下限
     * 月份的上限或下限（需要跟年配合使用）
     */
    private int secondFinalDay = LIMIT_0;
    private int secondStartDay = LIMIT_0;
    private int secondFinalYear = LIMIT_0;//TODO 限制的年份上限
    private int secondFinalMonth = LIMIT_0;//TODO 限制的月份的上限

    private final DIALOG_TYPE userSelectedTimeType;
    /**
     * 用户设置的时间间隔----初始化完成后，即为用户选择的时间
     */
    private int userYearRange_before;
    private int userMonthRange_before;
    private int userDayRange_before;

    private int userYearRange_after;
    private int userMonthRange_after;
    private int userDayRange_after;

    // 初始化   最终的年月时间（上下限的值）

    public CommonTimeSelectDialog(Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;
        userSelectedTimeType = DIALOG_TYPE.NOLIMIT;
        initCountRangeValue();
    }

    //TODO 当限制为0时，限制当前的选择的范围，如果后面的数据超出了，当前范围，则关联的上一级的范围
    public CommonTimeSelectDialog(Context context, DIALOG_TYPE type, int yearLength, int monthLength, int dayLength) {
        super(context, R.style.ShareDialog);
        this.context = context;
        userSelectedTimeType = type;

        if (yearLength > 0) {
            userYearRange_before = yearLength;
        } else {
            userYearRange_before = LIMIT_0;
        }

        if (monthLength > 0) {
            userMonthRange_before = monthLength;
        } else {
            userMonthRange_before = LIMIT_0;
        }

        if (dayLength > 0) {
            userDayRange_before = dayLength;
        } else {
            userDayRange_before = LIMIT_0;
        }

        //*************************************************************
        if (yearLength > 0) {
            userYearRange_after = yearLength;
        } else {
            userYearRange_after = LIMIT_0;
        }

        if (monthLength > 0) {
            userMonthRange_after = monthLength;
        } else {
            userMonthRange_after = LIMIT_0;
        }

        if (dayLength > 0) {
            userDayRange_after = dayLength;
        } else {
            userDayRange_after = LIMIT_0;
        }

        //TODO 初始化时间相关参数
        initCountRangeValue();
        //TODO 计算得到年月的范围
        sortTimeLimit();
    }

    //TODO 当限制为0时，限制当前的选择的范围，如果后面的数据超出了，当前范围，则关联的上一级的范围
    public CommonTimeSelectDialog(Context context, DIALOG_TYPE type, int yearBefore, int monthBefore, int dayBefore,
                                  int yearAfter, int monthAfter, int dayAfter) {//TODO 前后自定义范围（提供更加灵活的操作）
        super(context, R.style.ShareDialog);
        this.context = context;
        userSelectedTimeType = type;

        if (yearBefore > 0) {
            userYearRange_before = yearBefore;
        } else {
            userYearRange_before = LIMIT_0;
        }

        if (monthBefore > 0) {
            userMonthRange_before = monthBefore;
        } else {
            userMonthRange_before = LIMIT_0;
        }

        if (dayBefore > 0) {
            userDayRange_before = dayBefore;
        } else {
            userDayRange_before = LIMIT_0;
        }
        //*************************************************************
        if (yearAfter > 0) {
            userYearRange_after = yearAfter;
        } else {
            userYearRange_after = LIMIT_0;
        }

        if (monthAfter > 0) {
            userMonthRange_after = monthAfter;
        } else {
            userMonthRange_after = LIMIT_0;
        }

        if (dayAfter > 0) {
            userDayRange_after = dayAfter;
        } else {
            userDayRange_after = LIMIT_0;
        }

        //TODO 初始化时间相关参数
        initCountRangeValue();
        //TODO 计算得到年月的范围
        sortTimeLimit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_myinfo_changebirth);
        wvYear = (WheelView) findViewById(R.id.wv_birth_year);
        wvMonth = (WheelView) findViewById(R.id.wv_birth_month);
        wvDay = (WheelView) findViewById(R.id.wv_birth_day);

        vChangeBirth = findViewById(R.id.ly_myinfo_changebirth);
        vChangeBirthChild = findViewById(R.id.ly_myinfo_changebirth_child);
        btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
        btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

        vChangeBirth.setOnClickListener(this);
        vChangeBirthChild.setOnClickListener(this);
        btnSure.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        //TODO 初始化时间
        initData();

        //TODO 初始化年份时间----设置限制范围
        setYearRange();
        setYearAdapter();
        //TODO 初始化月份时间-----设置限制范围
        setMonthRange(getCurrentYear());
        setMonthAdapter();
        //TODO 初始化年份时间-----设置限制范围
        setDayRange(getCurrentYear(), getCurrentMonth());
        setDayAdapter();
//******************************************************************************************************************************
        //TODO 设置月和日的联动
        wvYear.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mYearAdapter);
                setYearAdapterIndex(Integer.parseInt(currentText));
                selectedYearString = currentText;
                selectedYearIntValue = Integer.parseInt(currentText);
                setMonthRange(selectedYearIntValue);//TODO 当前选择的年

                mMonthAdapter = new CalendarTextAdapter(context, arry_months, 0, maxTextSize, minTextSize);
                wvMonth.setVisibleItems(5);
                wvMonth.setViewAdapter(mMonthAdapter);
                wvMonth.setCurrentItem(0);

                //TODO 级联变更日期的数据
                selectedMonthIntValue = Integer.parseInt(arry_months.get(0));//TODO 默认设置到月份范围的第一个
                setDayRange(selectedYearIntValue, selectedMonthIntValue);
                mDaydapter = new CalendarTextAdapter(context, arry_days, 0, maxTextSize, minTextSize);
                wvDay.setVisibleItems(5);
                wvDay.setViewAdapter(mDaydapter);
                wvDay.setCurrentItem(0);
            }
        });

        wvYear.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mYearAdapter);
            }
        });
        //******************************************************************************************************************************
        //TODO 设置日的联动
        wvMonth.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mMonthAdapter);
                setMonthAdapterIndex(Integer.parseInt(currentText));
                selectedMonthString = currentText;
                selectedMonthIntValue = Integer.parseInt(currentText);

                setDayRange(selectedYearIntValue, selectedMonthIntValue);
                mDaydapter = new CalendarTextAdapter(context, arry_days, 0, maxTextSize, minTextSize);
                wvDay.setVisibleItems(5);
                wvDay.setViewAdapter(mDaydapter);
                wvDay.setCurrentItem(0);
            }
        });

        wvMonth.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mMonthAdapter);
            }
        });
        //******************************************************************************************************************************
        //TODO 设置日的选择，不需要联动
        wvDay.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mDaydapter);
                selectedDayString = currentText;
                selectedDayIntValue = Integer.parseInt(currentText);
            }
        });

        wvDay.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mDaydapter);
            }
        });
    }

    /**
     * 初始化上下限的范围值
     */
    public void initCountRangeValue() {
        firstStartDay = getCurrentDay();
        firstFinalDay = getCurrentDay();
        firstFinalMonth = getCurrentMonth();
        firstFinalYear = getCurrentYear();

        secondStartDay = getCurrentDay();
        secondFinalDay = getCurrentDay();
        secondFinalMonth = getCurrentMonth();
        secondFinalYear = getCurrentYear();
    }

    private void setYearAdapter() {
        mYearAdapter = new CalendarTextAdapter(context, arry_years, setYearAdapterIndex(selectedYearIntValue), maxTextSize, minTextSize);
        wvYear.setVisibleItems(5);
        wvYear.setViewAdapter(mYearAdapter);
        wvYear.setCurrentItem(setYearAdapterIndex(selectedYearIntValue));
    }

    private void setMonthAdapter() {
        mMonthAdapter = new CalendarTextAdapter(context, arry_months, setMonthAdapterIndex(currentMonth), maxTextSize, minTextSize);
        wvMonth.setVisibleItems(5);
        wvMonth.setViewAdapter(mMonthAdapter);
        wvMonth.setCurrentItem(setMonthAdapterIndex(currentMonth));
    }

    private void setDayAdapter() {
        mDaydapter = new CalendarTextAdapter(context, arry_days, currentDay - 1, maxTextSize, minTextSize);
        wvDay.setVisibleItems(5);
        wvDay.setViewAdapter(mDaydapter);
        wvDay.setCurrentItem(currentDay - 1);
    }

    //TODO 设置年月日的范围
    protected void setYearRange() {
        arry_years.clear();
        switch (userSelectedTimeType) {
            case NORMAL: {
                //TODO 默认从下限，到上限
                for (int i = firstFinalYear; i < secondFinalYear; i++) {
                    arry_years.add(i + "");
                }
                break;
            }
            case BEFORE: {
                //
                for (int i = firstFinalYear; i <= getCurrentYear(); i++) {
                    arry_years.add(i + "");
                }
                break;
            }
            case BEFORE_EXCEPT_TODAY: {
                for (int i = firstFinalYear; i <= getCurrentYear(); i++) {
                    arry_years.add(i + "");
                }
                break;
            }
            case AFTER: {
                for (int i = getCurrentYear(); i <= secondFinalYear; i++) {
                    arry_years.add(i + "");
                }
                break;
            }
            case AFTER_EXCEPT_TODAY: {
                for (int i = getCurrentYear(); i <= secondFinalYear; i++) {
                    arry_years.add(i + "");
                }
                break;
            }
            case NOLIMIT: {
                for (int i = getCurrentYear() - LIMIT_120; i <= getCurrentYear() + LIMIT_120; i++) {
                    arry_years.add(i + "");
                }
                break;
            }
        }
    }

    //TODO 仅设置当前这一年的范围
    protected void setMonthRange(int year) {
        arry_months.clear();
        switch (userSelectedTimeType) {
            case NORMAL: {
                //TODO
                if (year == firstFinalYear && year == secondFinalYear) {
                    for (int i = firstFinalMonth; i <= secondFinalMonth; i++) {
                        arry_months.add(i + "");
                    }
                } else if (year == firstFinalYear) {
                    for (int i = firstFinalMonth; i <= 12; i++) {
                        arry_months.add(i + "");
                    }
                } else if (year == secondFinalYear) {
                    for (int i = 1; i <= secondFinalMonth; i++) {
                        arry_months.add(i + "");
                    }
                } else {
                    for (int i = 1; i <= 12; i++) {
                        arry_months.add(i + "");
                    }
                }
                break;
            }
            case BEFORE: {
                if (year == firstFinalYear && year == getCurrentYear()) {
                    for (int i = firstFinalMonth; i <= getCurrentMonth(); i++) {
                        arry_months.add(i + "");
                    }
                } else if (year == firstFinalYear) {
                    for (int i = firstFinalMonth; i <= 12; i++) {
                        arry_months.add(i + "");
                    }
                } else if (year == getCurrentYear()) {
                    for (int i = 1; i <= getCurrentMonth(); i++) {
                        arry_months.add(i + "");
                    }
                } else {
                    for (int i = 1; i <= 12; i++) {
                        arry_months.add(i + "");
                    }
                }
                break;
            }
            case BEFORE_EXCEPT_TODAY: {
                if (year == firstFinalYear && year == getCurrentYear()) {
                    for (int i = firstFinalMonth; i <= getCurrentMonth(); i++) {
                        arry_months.add(i + "");
                    }
                } else if (year == firstFinalYear) {
                    for (int i = firstFinalMonth; i <= 12; i++) {
                        arry_months.add(i + "");
                    }
                } else if (year == getCurrentYear()) {
                    for (int i = 1; i <= getCurrentMonth(); i++) {
                        arry_months.add(i + "");
                    }
                } else {
                    for (int i = 1; i <= 12; i++) {
                        arry_months.add(i + "");
                    }
                }
                break;
            }
            case AFTER: {
                if (year == secondFinalYear && year == getCurrentYear()) {
                    for (int i = getCurrentMonth(); i <= secondFinalMonth; i++) {
                        arry_months.add(i + "");
                    }
                } else if (year == secondFinalYear) {
                    for (int i = 1; i <= secondFinalYear; i++) {
                        arry_months.add(i + "");
                    }
                } else if (year == getCurrentYear()) {
                    for (int i = getCurrentMonth(); i <= 12; i++) {
                        arry_months.add(i + "");
                    }
                } else {
                    for (int i = 1; i <= 12; i++) {
                        arry_months.add(i + "");
                    }
                }
                break;
            }
            case AFTER_EXCEPT_TODAY: {
                if (year == secondFinalYear && year == getCurrentYear()) {
                    for (int i = getCurrentMonth(); i <= secondFinalMonth; i++) {
                        arry_months.add(i + "");
                    }
                } else if (year == secondFinalYear) {
                    for (int i = 1; i <= secondFinalYear; i++) {
                        arry_months.add(i + "");
                    }
                } else if (year == getCurrentYear()) {
                    for (int i = getCurrentMonth(); i <= 12; i++) {
                        arry_months.add(i + "");
                    }
                } else {
                    for (int i = 1; i <= 12; i++) {
                        arry_months.add(i + "");
                    }
                }
                break;
            }
            case NOLIMIT: {
                for (int i = 1; i <= 12; i++) {
                    arry_months.add(i + "");
                }
                break;
            }
        }
    }

    //TODO 仅设置当前这一月的范围
    protected void setDayRange(int year, int month) {//指定年月的日期范围
        arry_days.clear();
        switch (userSelectedTimeType) {
            case NORMAL: {
                //TODO 默认不会对
                if (year == firstFinalYear && month == firstFinalMonth &&
                        year == secondFinalYear && month == secondFinalMonth) {//处于上限，下限内部
                    for (int i = firstFinalDay; i <= secondFinalDay; i++) {
                        arry_days.add(i + "");
                    }
                } else if (year == firstFinalYear && month == firstFinalMonth) {//处于上限内的那一月
                    for (int i = firstFinalDay; i <= getSpecificYearMonthMaxDay(year, month); i++) {
                        arry_days.add(i + "");
                    }
                } else if (year == secondFinalYear && month == secondFinalMonth) {//处于下限的那一月
                    for (int i = 1; i <= secondFinalDay; i++) {
                        arry_days.add(i + "");
                    }
                } else {
                    for (int i = 1; i <= getSpecificYearMonthMaxDay(year, month); i++) {
                        arry_days.add(i + "");
                    }
                }
                break;
            }
            case BEFORE: {
                //TODO 默认不会对
                if (year == firstFinalYear && month == firstFinalMonth &&
                        year == getCurrentYear() && month == getCurrentMonth()) {//处于上限，下限内部
                    for (int i = firstFinalDay; i <= firstStartDay; i++) {
                        arry_days.add(i + "");
                    }
                } else if (year == firstFinalYear && month == firstFinalMonth) {//处于上限内的那一月
                    for (int i = firstFinalDay; i <= getSpecificYearMonthMaxDay(year, month); i++) {
                        arry_days.add(i + "");
                    }
                } else if (year == getCurrentYear() && month == getCurrentMonth()) {//处于下限的那一月
                    for (int i = 1; i <= firstStartDay; i++) {
                        arry_days.add(i + "");
                    }
                } else {
                    for (int i = 1; i <= getSpecificYearMonthMaxDay(year, month); i++) {
                        arry_days.add(i + "");
                    }
                }
                break;
            }
            case BEFORE_EXCEPT_TODAY: {
                if (year == firstFinalYear && month == firstFinalMonth &&
                        year == getCurrentYear() && month == getCurrentMonth()) {//处于上限，下限内部
                    for (int i = firstFinalDay; i <= firstStartDay; i++) {
                        arry_days.add(i + "");
                    }
                } else if (year == firstFinalYear && month == firstFinalMonth) {//处于上限内的那一月
                    for (int i = firstFinalDay; i <= getSpecificYearMonthMaxDay(year, month); i++) {
                        arry_days.add(i + "");
                    }
                } else if (year == getCurrentYear() && month == getCurrentMonth()) {//处于下限的那一月
                    for (int i = 1; i <= firstStartDay; i++) {
                        arry_days.add(i + "");
                    }
                } else {
                    for (int i = 1; i <= getSpecificYearMonthMaxDay(year, month); i++) {
                        arry_days.add(i + "");
                    }
                }
                break;
            }
            case AFTER: {
                if (year == secondFinalYear && month == secondFinalMonth &&
                        year == getCurrentYear() && month == getCurrentMonth()) {//处于上限，下限内部
                    for (int i = secondStartDay; i <= secondFinalDay; i++) {
                        arry_days.add(i + "");
                    }
                } else if (year == secondFinalYear && month == secondFinalMonth) {//处于上限内的那一月
                    for (int i = 1; i <= secondFinalDay; i++) {
                        arry_days.add(i + "");
                    }
                } else if (year == getCurrentYear() && month == getCurrentMonth()) {//处于下限的那一月
                    for (int i = secondStartDay; i <= getSpecificYearMonthMaxDay(year, month); i++) {
                        arry_days.add(i + "");
                    }
                } else {
                    for (int i = 1; i <= getSpecificYearMonthMaxDay(year, month); i++) {
                        arry_days.add(i + "");
                    }
                }
                break;
            }
            case AFTER_EXCEPT_TODAY: {
                if (year == secondFinalYear && month == secondFinalMonth &&
                        year == getCurrentYear() && month == getCurrentMonth()) {//处于上限，下限内部
                    for (int i = secondStartDay; i <= secondFinalDay; i++) {
                        arry_days.add(i + "");
                    }
                } else if (year == secondFinalYear && month == secondFinalMonth) {//处于上限内的那一月
                    for (int i = 1; i <= secondFinalDay; i++) {
                        arry_days.add(i + "");
                    }
                } else if (year == getCurrentYear() && month == getCurrentMonth()) {//处于下限的那一月
                    for (int i = secondStartDay; i <= getSpecificYearMonthMaxDay(year, month); i++) {
                        arry_days.add(i + "");
                    }
                } else {
                    for (int i = 1; i <= getSpecificYearMonthMaxDay(year, month); i++) {
                        arry_days.add(i + "");
                    }
                }
                break;
            }
            case NOLIMIT: {
                for (int i = 1; i <= getSpecificYearMonthMaxDay(year, month); i++) {
                    arry_days.add(i + "");
                }
                break;
            }
        }
    }

    //*******************************************************************

    private class CalendarTextAdapter extends AbstractWheelTextAdapter {
        ArrayList<String> list;

        protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize,
                                      int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }
    }

    public void setBirthdayListener(OnBirthListener onBirthListener) {
        this.onBirthListener = onBirthListener;
    }

    @Override
    public void onClick(View v) {
        if (v == btnSure) {
            if (onBirthListener != null) {
                onBirthListener.onClick(selectedYearString, selectedMonthString, selectedDayString);
            }
        } else if (v == vChangeBirthChild) {
            return;
        } else {
            dismiss();
        }
        dismiss();
    }

    public interface OnBirthListener {
        public void onClick(String year, String month, String day);
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, CalendarTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(maxTextSize);
            } else {
                textvew.setTextSize(minTextSize);
            }
        }
    }

    protected int getCurrentYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    protected int getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    protected int getCurrentDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DATE);
    }

    //TODO 设置当前年月日-------并通过当前年份
    public void initData() {
        initData(getCurrentYear(), getCurrentMonth(), getCurrentDay());
    }

    /**
     * 设置年月日------含有设置年月日的范围
     *
     * @param year
     * @param month
     * @param day
     */
    public void initData(int year, int month, int day) {
        //TODO 设置默认选择的时间---当天时间
        switch (userSelectedTimeType) {
            case BEFORE_EXCEPT_TODAY: {
                if (day == 1) {
                    month = month - 1;
                    if (month < 1) {
                        year -= 1;
                        month = 12;
                    }
                    day = getSpecificYearMonthMaxDay(year, month);
                } else {
                    day = day - 1;
                }
                //TODO 选择的是偏移后的数据
                selectedYearString = year + "";
                selectedMonthString = month + "";
                selectedDayString = day + "";
                break;
            }
            case AFTER_EXCEPT_TODAY: {
                //TODO 往后偏移
                if (day == getSpecificYearMonthMaxDay(year, month)) {
                    month += 1;
                    if (month > 12) {
                        month = 1;
                        year += 1;
                    }
                    day = 1;
                } else {
                    day = day + 1;
                }
                selectedYearString = year + "";
                selectedMonthString = month + "";
                selectedDayString = day + "";
                break;
            }
            default: {
                selectedYearString = year + "";
                selectedMonthString = month + "";
                selectedDayString = day + "";
                break;
            }
        }
//        //TODO 这里写法有点问题---不论当前是不是今年，month都设置成了12
    }

    /**
     * 设置年份----设置设置第几个项
     *
     * @param year
     */
    public int setYearAdapterIndex(int year) {
        int yearIndex = 0;
        switch (userSelectedTimeType) {
            case NORMAL: {
                //TODO 默认从下限，到上限
                for (int i = firstFinalYear; i < secondFinalYear; i++) {
                    if (i == year) {
                        return yearIndex;
                    } else {
                        yearIndex++;
                    }
                }
                break;
            }
            case BEFORE: {
                //
                for (int i = firstFinalYear; i <= getCurrentYear(); i++) {
                    if (i == year) {
                        return yearIndex;
                    } else {
                        yearIndex++;
                    }
                }
                break;
            }
            case BEFORE_EXCEPT_TODAY: {
                for (int i = firstFinalYear; i <= getCurrentYear(); i++) {
                    if (i == year) {
                        return yearIndex;
                    } else {
                        yearIndex++;
                    }
                }
                break;
            }
            case AFTER: {
                for (int i = getCurrentYear(); i <= secondFinalYear; i++) {
                    if (i == year) {
                        return yearIndex;
                    } else {
                        yearIndex++;
                    }
                }
                break;
            }
            case AFTER_EXCEPT_TODAY: {
                for (int i = getCurrentYear(); i <= secondFinalYear; i++) {
                    if (i == year) {
                        return yearIndex;
                    } else {
                        yearIndex++;
                    }
                }
                break;
            }
            case NOLIMIT: {
                for (int i = getCurrentYear() - LIMIT_120; i <= getCurrentYear() + LIMIT_120; i++) {
                    if (i == year) {
                        return yearIndex;
                    } else {
                        yearIndex++;
                    }
                }
                break;
            }
        }
        return yearIndex;
    }

    /**
     * 设置月份------当前月时范围内的第几项
     *
     * @param month
     * @return
     */
    public int setMonthAdapterIndex(int month) {
        int monthIndex = 0;
        getMonthDayRangeDays(selectedYearIntValue, month);

        switch (userSelectedTimeType) {
            case NORMAL: {
                //TODO
                if (selectedYearIntValue == firstFinalYear && selectedYearIntValue == secondFinalYear) {
                    for (int i = firstFinalMonth; i <= secondFinalMonth; i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                } else if (selectedYearIntValue == firstFinalYear) {
                    for (int i = firstFinalMonth; i <= 12; i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                } else if (selectedYearIntValue == secondFinalYear) {
                    for (int i = 1; i <= secondFinalMonth; i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                } else {
                    for (int i = 1; i <= 12; i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                }
            }
            case BEFORE: {
                if (selectedYearIntValue == firstFinalYear && selectedYearIntValue == getCurrentYear()) {
                    for (int i = firstFinalMonth; i <= getCurrentMonth(); i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                } else if (selectedYearIntValue == firstFinalYear) {
                    for (int i = firstFinalMonth; i <= 12; i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                } else if (selectedYearIntValue == getCurrentYear()) {
                    for (int i = 1; i <= getCurrentMonth(); i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                } else {
                    for (int i = 1; i <= 12; i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                }
            }
            case BEFORE_EXCEPT_TODAY: {
                if (selectedYearIntValue == firstFinalYear && selectedYearIntValue == getCurrentYear()) {
                    for (int i = firstFinalMonth; i <= getCurrentMonth(); i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                } else if (selectedYearIntValue == firstFinalYear) {
                    for (int i = firstFinalMonth; i <= 12; i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                } else if (selectedYearIntValue == getCurrentYear()) {
                    for (int i = 1; i <= getCurrentMonth(); i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                } else {
                    for (int i = 1; i <= 12; i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                }
            }
            case AFTER: {
                if (selectedYearIntValue == secondFinalYear && selectedYearIntValue == getCurrentYear()) {
                    for (int i = getCurrentMonth(); i <= secondFinalMonth; i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                } else if (selectedYearIntValue == secondFinalYear) {
                    for (int i = 1; i <= secondFinalYear; i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                } else if (selectedYearIntValue == getCurrentYear()) {
                    for (int i = getCurrentMonth(); i <= 12; i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                } else {
                    for (int i = 1; i <= 12; i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                }
            }
            case AFTER_EXCEPT_TODAY: {
                if (selectedYearIntValue == secondFinalYear && selectedYearIntValue == getCurrentYear()) {
                    for (int i = getCurrentMonth(); i <= secondFinalMonth; i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                } else if (selectedYearIntValue == secondFinalYear) {
                    for (int i = 1; i <= secondFinalYear; i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                } else if (selectedYearIntValue == getCurrentYear()) {
                    for (int i = getCurrentMonth(); i <= 12; i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                } else {
                    for (int i = 1; i <= 12; i++) {
                        if (i == month) {
                            return monthIndex;
                        } else {
                            monthIndex++;
                        }
                    }
                    return 0;
                }
            }
            case NOLIMIT: {
                for (int i = 1; i <= 12; i++) {
                    if (i == month) {
                        return monthIndex;
                    } else {
                        monthIndex++;
                    }
                }
                return 0;
            }
        }
        return monthIndex;
    }

    /**
     * 计算每月多少天-----日期范围
     *
     * @param year
     * @param month
     */
    public void getMonthDayRangeDays(int year, int month) {
        boolean leayyear = false;
        if (year % 4 == 0 && year % 100 != 0) {
            leayyear = true;
        } else {
            leayyear = false;
        }
        for (int i = 1; i <= 12; i++) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    this.dayRange = 31;
                    return;
                case 2:
                    if (leayyear) {
                        this.dayRange = 29;
                    } else {
                        this.dayRange = 28;
                    }
                    return;
                case 4:
                case 6:
                case 9:
                case 11:
                    this.dayRange = 30;
                    return;
            }
        }
    }

    //TODO 指定年月的月份有多少天
    private int getSpecificYearMonthMaxDay(int year, int month) {
        boolean leayyear = false;
        if (year % 4 == 0 && year % 100 != 0) {
            leayyear = true;
        } else {
            leayyear = false;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                if (leayyear) {
                    return 29;
                } else {
                    return 28;
                }
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 30;
        }
    }

    //TODO 规范间隔时间------实际计算时，通过

    /**
     * 当前月的可选天数范围
     */
    protected void sortTimeLimit() {
        //TODO 需要重置，并规范年月日的限制范围--向前，或向后，不同的范围
        //TODO 按照当前的时间，分别计算出当前 日，月，年的偏移值

        //临时保存用户设定的限制值
        int beforeDay = userDayRange_before;
        int beforeMonth = userMonthRange_before;
        int beforeYear = userYearRange_before;

        int afterDay = userDayRange_after;
        int afterMonth = userMonthRange_after;
        int afterYear = userYearRange_after;
        /**
         * 初始按照包含当前的时间进行计算，在计算完成后，若选择的类型
         */
        //由于上下限的年月日都初始化了，
        //********************************   向前偏移  先算从当前日期开始的（含有今天的）
        //开始计算，得到向前选择时，时间的上限日期
        if (beforeMonth == 0 && beforeYear == 0) {//没有跨年，跨月，仅根据日期来计算
            if (getCurrentDay() >= beforeDay) {
                //TODO 范围在当月
                firstFinalDay = getCurrentDay() - beforeDay + 1;//TODO 范围即从 firstFinalDay ~  firstStartDay

            } else {
                //排除掉当月的时间---进入上一个月
                beforeDay = beforeDay - getCurrentDay();//循环初始

                while (true) {

                    //计算上一个月是哪年哪月
                    firstFinalMonth = firstFinalMonth - 1;//月份-1

                    // 计算年份的的上限
                    if (firstFinalMonth <= 0) {
                        firstFinalMonth = 12;
                        firstFinalYear = firstFinalYear - 1;// 年变为上一年
                    } else {//不需要额外操作

                    }

                    //TODO 循环条件判断
                    //若计算后的剩余日期数，少于往前推一个月那个月份的
                    if (beforeDay > getSpecificYearMonthMaxDay(firstFinalYear, firstFinalMonth)) {
                        // 减去往前推一个月的那月日期总数
                        beforeDay = beforeDay - getSpecificYearMonthMaxDay(firstFinalYear, firstFinalMonth);
                    } else {
                        //计算结束日期，是上限的年月的哪一天
                        firstFinalDay = getSpecificYearMonthMaxDay(firstFinalYear, firstFinalMonth) - beforeDay + 1;//TODO 得到最后一天
                        // 年月不再需要变化---终止计算的循环
                        break;
                    }
                }
                LogUtil.e("firstFinalYear   1 :" + firstFinalYear);
            }
        } else {//(getCurrentDay() >= beforeDay ? beforeDay : getCurrentDay())
            /**
             * 这种情况下会出现一个问题：
             * 当仅计算完日期后，若按照月份推算，是当月最后一天例如31号，往前推一个月，那个月只有30天，则此时会出问题
             *
             */
            if (getCurrentDay() >= beforeDay) {
                //TODO 范围在当月

                firstFinalDay = getCurrentDay() - beforeDay + 1;//TODO 范围即从 firstFinalDay ~  firstStartDay

                int tempCountMonth = firstFinalMonth;

                firstFinalMonth = (firstFinalMonth > beforeMonth % 12) ? (firstFinalMonth - beforeMonth % 12) : (12 + firstFinalMonth - beforeMonth % 12);

                firstFinalYear = getCurrentYear() - beforeYear - ((tempCountMonth > beforeMonth % 12) ? (beforeMonth / 12) : (beforeMonth / 12 + 1));

                int finalDayRange = getSpecificYearMonthMaxDay(getCurrentYear(), getCurrentMonth()) - firstFinalDay + 1;//计算 最后间隔了几天----由于日期间隔没超过一个月，
                // 计算最终日是从月末往前偏移多少(而由于需要将最终这一天包含进去，所以需要+1)

                if (getSpecificYearMonthMaxDay(firstFinalYear, firstFinalMonth) >= finalDayRange) {
                    firstFinalDay = getSpecificYearMonthMaxDay(firstFinalYear, firstFinalMonth) - finalDayRange + 1;
                } else {
                    finalDayRange = finalDayRange - getSpecificYearMonthMaxDay(firstFinalYear, firstFinalMonth);
                    firstFinalMonth--;
                    if (firstFinalMonth <= 0) {
                        firstFinalMonth = 12;
                        firstFinalYear--;
                    }
                    firstFinalDay = getSpecificYearMonthMaxDay(firstFinalYear, firstFinalMonth) - finalDayRange + 1;//根据最后的年月上限，得到真正的最终日期（避免出现最终时间异常的问题）
                }
                LogUtil.e("firstFinalYear   2 :" + firstFinalYear);
            } else {
                beforeDay = beforeDay - getCurrentDay();//

                while (true) {
                    //超出了当月的，跨度为两个月 --- 先计算 上一个月时哪年哪月
                    firstFinalMonth = firstFinalMonth - 1;//自己基础上偏移
                    // 计算年份的的上限
                    if (firstFinalMonth <= 0) {
                        firstFinalMonth = 12;
                        firstFinalYear = firstFinalYear - 1;// 变为上一年
                    } else {

                    }
                    //若计算后的剩余日期数，少于往前推一个月那个月份的
                    if (beforeDay > getSpecificYearMonthMaxDay(firstFinalYear, firstFinalMonth)) {
                        // 减去往前推一个月的那月日期总数
                        beforeDay = beforeDay - getSpecificYearMonthMaxDay(firstFinalYear, firstFinalMonth);
                    } else {
                        //TODO 这里的计算方式与时间少于当月的计算方式相同
                        //(firstFinalMonth + beforeMonth) % 12;
                        int tempFinalMonth = firstFinalMonth;//临时保存计算日期后的月份---为计算年份准备
                        firstFinalMonth = (firstFinalMonth > beforeMonth % 12) ? (firstFinalMonth - beforeMonth % 12) : (12 + firstFinalMonth - beforeMonth % 12);

                        firstFinalYear = firstFinalYear - beforeYear - ((tempFinalMonth > beforeMonth % 12) ? (beforeMonth / 12) : (beforeMonth / 12 + 1));

                        firstFinalDay = getSpecificYearMonthMaxDay(firstFinalYear, firstFinalMonth) - beforeDay + 1;
                        // 年月不再需要变化---终止计算的循环
                        break;
                    }
                }
                //结束日期计算---开始计算年份月份
                LogUtil.e("firstFinalYear   3 :" + firstFinalYear);
            }
        }
        //********************************   向后偏移---与向前偏移时相似，不过计算的规则需要变化（含有今天）

        //TODO 得到当前月份可选择的日期范围---选择最小值
        /**
         * 当选择的是当前，年，月时，
         * 日期范围 在   secondStartDay ~~~~~  月底 之间
         */
        if (afterMonth == 0 && afterYear == 0) {// getCurrentDay() + afterDay - 1    *******************   计算第一个月的间隔是否在那个月内（包含当天；若不含当天，则不需要计算-1）
            if (getSpecificYearMonthMaxDay(getCurrentYear(), getCurrentMonth()) >= (getCurrentDay() + afterDay - 1)) {//是否在当前月内
                //TODO 范围在当月
                secondFinalDay = getCurrentDay() + afterDay - 1;//TODO 范围即从 secondStartDay ~  secondFinalDay   (从secondStartDay开始算起)
            } else {
                afterDay = afterDay -
                        (getSpecificYearMonthMaxDay(secondFinalYear, secondFinalMonth) - getCurrentDay() + 1);//得到当月的间隔时间，由于需要包含当天，则需要+1
                // 减去当月剩余的天数，得到移动到下个月的剩余时间
                while (true) {
                    //超出了当月的，跨度为两个月 --- 先计算 下一个月是哪年哪月
                    secondFinalMonth = secondFinalMonth + 1;// 月份往后推一个月---在自己的基础上推
                    // 计算年份的的上限
                    if (secondFinalMonth > 12) {//当超过当年的月份时，变更为下一年
                        secondFinalMonth = 1;
                        secondFinalYear = secondFinalYear + 1;// 变为下一年
                    } else {

                    }

                    //若计算后的剩余日期数，是否少于往后推一个月那个月份的
                    if (afterDay > getSpecificYearMonthMaxDay(secondFinalYear, secondFinalMonth)) {//TODO 由于是在计算实际最后日期的上限，所以直接比较推后的那月的全部日期
                        // 减去往前推一个月的那月日期总数-------------剩余日期数仍然大于往后推的那个月总天数
                        afterDay = afterDay - getSpecificYearMonthMaxDay(secondFinalYear, secondFinalMonth);
                    } else {
                        //TODO 这里的计算方式与时间少于当月的计算方式相同
                        secondFinalDay = afterDay;//TODO 就是这一天了
                        // 年月不再需要变化---终止计算的循环
                        break;
                    }
                }
                LogUtil.e("secondFinalYear   1 :" + secondFinalYear);
            }
        } else {// 单独的月份与年份范围不为0
            if (getSpecificYearMonthMaxDay(secondFinalYear, secondFinalMonth) >= (getCurrentDay() + afterDay - 1)) {
                //TODO 范围在当月
                secondFinalDay = getCurrentDay() + afterDay - 1;//TODO 范围即从 secondStartDay ~  secondFinalDay  (需要将当天这一天包含进去)
                //TODO 计算最终的年份，月份下线
                int tempCountMonth = secondFinalMonth;

                secondFinalMonth = (secondFinalMonth + afterMonth) % 12;

                secondFinalYear = secondFinalYear + afterYear +
                        (tempCountMonth + afterMonth) / 12;//计算在日期偏移计算完成后的，月份需要偏移多少年

                //TODO 由于偏移年月计算完成后，存在最终日期可能超出这个最终的日期，所以需要再次判断下
                if (secondFinalDay > getSpecificYearMonthMaxDay(secondFinalYear, secondFinalMonth)) {
                    secondFinalDay = secondFinalDay - getSpecificYearMonthMaxDay(secondFinalYear, secondFinalMonth);
                    secondFinalMonth += 1;
                    if (secondFinalMonth > 12) {
                        secondFinalMonth = 1;
                        secondFinalYear++;
                    }
                }

                LogUtil.e("secondFinalYear   2 :" + secondFinalYear);
            } else {
                /**
                 从 getCurrentDay()  ~  到月底
                 */
                afterDay = afterDay -
                        (getSpecificYearMonthMaxDay(secondFinalYear, secondFinalMonth) - getCurrentDay() + 1);//减去--- 当月剩余的时间(需要包含当天，所以需要+1)
                while (true) {
                    //超出了当月的，跨度为两个月 --- 先计算 上一个月时哪年哪月
                    secondFinalMonth = secondFinalMonth + 1;//在当前最终月的基础上偏移，
                    // 计算年份的的上限
                    if (secondFinalMonth > 12) {
                        secondFinalMonth = 1;
                        secondFinalYear = secondFinalYear + 1;// 变为下一年
                    } else {

                    }

                    //若计算后的剩余日期数，少于往前推一个月那个月份的
                    if (afterDay > getSpecificYearMonthMaxDay(secondFinalYear, secondFinalMonth)) {
                        // 减去往前推一个月的那月日期总数
                        afterDay = afterDay - getSpecificYearMonthMaxDay(secondFinalYear, secondFinalMonth);
                    } else {
                        //TODO 这里的计算方式与时间少于当月的计算方式相同
                        secondFinalDay = afterDay;//TODO 就是这一天了 1~~~~这一天
                        // 年月不再需要变化---终止计算的循环
                        break;
                    }
                }

                int tempCountMonth = secondFinalMonth;

                secondFinalMonth = (secondFinalMonth + afterMonth) % 12;

                secondFinalYear = secondFinalYear + afterYear +
                        (tempCountMonth + afterMonth) / 12;//计算在日期偏移计算完成后的，月份需要偏移多少年

                //TODO 由于偏移年月计算完成后，存在最终日期可能超出这个最终的日期，所以需要再次判断下
                if (secondFinalDay > getSpecificYearMonthMaxDay(secondFinalYear, secondFinalMonth)) {
                    secondFinalDay = secondFinalDay - getSpecificYearMonthMaxDay(secondFinalYear, secondFinalMonth);
                    secondFinalMonth += 1;
                    if (secondFinalMonth > 12) {
                        secondFinalMonth = 1;
                        secondFinalYear++;
                    }
                }

                LogUtil.e("secondFinalYear   3 :" + secondFinalYear);
            }
        }
        LogUtil.e("类型：" + userSelectedTimeType.toString());
        LogUtil.e("日期开始日：" + firstStartDay
                + "\n日期结束日：" + firstFinalDay
                + "\n月份上限：" + firstFinalMonth
                + "\n年份上限：" + firstFinalYear
        );
        LogUtil.e("日期开始日：" + secondStartDay
                + "\n日期结束日：" + secondFinalDay
                + "\n月份下限：" + secondFinalMonth
                + "\n年份下限：" + secondFinalYear
        );

        LogUtil.e("***********************************************************************\n调整后");

        //TODO 按照包含今天的计算完成后，根据选择的类型，纠正对应的数据
        switch (userSelectedTimeType) {
            case NORMAL: {
                //TODO 默认不会对前后进行限制
                break;
            }
            case BEFORE: {
                break;
            }
            case BEFORE_EXCEPT_TODAY: {//TODO 矫正范围数据
                //TODO 起始日期往前推一天
                if (firstStartDay == 1) {//TODO 月初
                    int tempCountMonth = getCurrentMonth();
                    int tempCountYear = getCurrentYear();
                    tempCountMonth -= 1;
                    if (tempCountMonth < 1) {
                        tempCountYear -= 1;
                        tempCountMonth = 12;
                    }
                    firstStartDay = getSpecificYearMonthMaxDay(tempCountYear, tempCountMonth);
                } else {
                    firstStartDay -= 1;
                }
                //TODO 最终日期往前推1
                if (firstFinalDay == 1) {
                    firstFinalMonth -= 1;
                    if (firstFinalMonth < 1) {
                        firstFinalMonth = 12;
                        firstFinalYear -= 1;
                    }
                    firstFinalDay = getSpecificYearMonthMaxDay(firstFinalYear, firstFinalMonth);
                } else {
                    firstFinalDay -= 1;
                }

                break;
            }
            case AFTER: {
                break;
            }
            case AFTER_EXCEPT_TODAY: {
                //TODO 不含当天，起始日，往后推一天
                if (secondStartDay == getSpecificYearMonthMaxDay(getCurrentYear(), getCurrentMonth())) {//TODO 月末
                    int tempCountMonth = getCurrentMonth();
                    int tempCountYear = getCurrentYear();
                    tempCountMonth += 1;
                    if (tempCountMonth > 12) {
                        tempCountYear += 1;
                        tempCountMonth = 1;
                    }
                    secondStartDay = 1;
                } else {
                    secondStartDay += 1;
                }

                //TODO 最后一天往后推一天
                if (secondFinalDay == getSpecificYearMonthMaxDay(secondFinalYear, secondFinalMonth)) {
                    secondFinalMonth += 1;
                    if (secondFinalMonth > 12) {
                        secondFinalMonth = 1;
                        secondFinalYear += 1;
                    }
                    secondFinalDay = 1;
                } else {
                    secondFinalDay += 1;
                }
                break;
            }
            case NOLIMIT: {
                break;
            }
        }

        LogUtil.e("日期开始日：" + firstStartDay
                + "\n日期结束日：" + firstFinalDay
                + "\n月份上限：" + firstFinalMonth
                + "\n年份上限：" + firstFinalYear
        );
        LogUtil.e("日期开始日：" + secondStartDay
                + "\n日期结束日：" + secondFinalDay
                + "\n月份下限：" + secondFinalMonth
                + "\n年份下限：" + secondFinalYear
        );
    }
}