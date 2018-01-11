package com.quanjiakan.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pingantong.main.R;
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
public class HouseKeeperTimeSelectDialog extends Dialog implements android.view.View.OnClickListener {

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

    private boolean isSetData = false;

    //TODO 当前选中的时间
    private String selectYear;
    private String selectMonth;
    private String selectDay;

    private int selectYearValue;
    private int selectMonthValue;
    private int selectDayValue;

    private OnBirthListener onBirthListener;

    /**
     * TODO 限制类型
     * 当选择before时，限制
     */
    public enum DIALOG_TYPE {
        BEFORE,//TODO 往前选择日期，包含当天
        BEFORE_EXCEPT,//TODO 往前选择日期，不包含当天
        AFTER,//TODO 往后选择日期，包含当天
        AFTER_EXCEPT,//TODO 往后选择日期，不包含当天
        NORMAL,//TODO 前后皆可选择，含当天时间
        NOLIMIT //TODO 默认值（前后120年）
    }

    private static final int CURRENT_LIMIT = 0;//TODO 限制为当前范围(使用默认的限制长度)，即当前，年月日

    //TODO 无限制时的默认限制长度
    private static final int DEFAULT_LIMIT_LENGTH_YEAR = 120;//TODO 限制默认范围为一百二十---为年龄选择做兼容
    private static final int DEFAULT_LIMIT_LENGTH_MONTH = 12;//TODO 限制默认范围为一百二十---为年龄选择做兼容

    //TODO 年月日的限制时间---初始化范围时使用  往前算的时间范围
    private int firstLastMonthDayLimit = CURRENT_LIMIT;//TODO 最后一个月 限制的日期范围（从月初，或月末算起，当总的限制天数在当前月内时除外）
    private int firstMonthLimit = CURRENT_LIMIT;//TODO 限制的时间中，总共跨度的月数
    private int firstCurrentMonthDayLimit = CURRENT_LIMIT;//TODO 当前月的日期范围（从当前日开始算起）
    private int firstYearTopLimit = CURRENT_LIMIT;//TODO 限制的年份上限
    private int firstMonthTopLimit = CURRENT_LIMIT;//TODO 限制的月份的上限

    //TODO 往后算的时间范围----规范后的数据
    /**
     * 分别是
     * 当前月的日期范围（需要根据类型来看：1号到现在；现在到月底）
     * 最后一月的日期范围（也需要根据类型来看是那种计算方式）
     * 总共的间隔的月数
     * 年份的上限或下限
     * 月份的上限或下限（需要跟年配合使用）
     */
    private int secondLastMonthDayLimit = CURRENT_LIMIT;
    private int secondMonthLimit = CURRENT_LIMIT;
    private int secondCurrentMonthDayLimit = CURRENT_LIMIT;
    private int secondYearLowerLimit = CURRENT_LIMIT;//TODO 限制的年份上限
    private int secondMonthLowerLimit = CURRENT_LIMIT;//TODO 限制的月份的上限

    private final DIALOG_TYPE currentType;
    /**
     * 用户设置的时间间隔----初始化完成后，即为用户选择的时间
     */
    private int yearSelectLength;
    private int monthSelectLength;
    private int daySelectLength;

    //TODO 获取当前的年月日
    public void initCurrentTime() {
        firstYearTopLimit = CURRENT_LIMIT;
        firstMonthTopLimit = CURRENT_LIMIT;
    }

    public HouseKeeperTimeSelectDialog(Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;
        currentType = DIALOG_TYPE.NOLIMIT;
        initCurrentTime();
    }

    //TODO 当限制为0时，限制当前的选择的范围，如果后面的数据超出了，当前范围，则关联的上一级的范围
    public HouseKeeperTimeSelectDialog(Context context, DIALOG_TYPE type, int yearLength, int monthLength, int dayLength) {
        super(context, R.style.ShareDialog);
        this.context = context;
        currentType = type;

        if (yearLength > 0) {
            yearSelectLength = yearLength;
        } else {
            yearSelectLength = CURRENT_LIMIT;
        }

        if (monthLength > 0) {
            monthSelectLength = monthLength;
        } else {
            monthSelectLength = CURRENT_LIMIT;
        }

        if (dayLength > 0) {
            daySelectLength = dayLength;
        } else {
            daySelectLength = CURRENT_LIMIT;
        }

        //TODO 初始化时间相关参数
        initCurrentTime();
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
        if (!isSetData) {
            initData();
        }
        //TODO 初始化年份时间----设置限制范围
        setYearRange();
        setYearAdapter();
        //TODO 初始化月份时间-----设置限制范围
        setMonthRange(getCurrentYear());
        setMonthAdapter();
        //TODO 初始化年份时间-----设置限制范围
        setDayRange(getCurrentYear(),getCurrentMonth());
        setDayAdapter();

        //TODO 设置月和日的联动
        wvYear.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mYearAdapter);
                setYearAdapterIndex(Integer.parseInt(currentText));
                selectYear = currentText;
                selectYearValue = Integer.parseInt(currentText);
                setMonthRange(selectYearValue);//TODO 当前选择的年

                mMonthAdapter = new CalendarTextAdapter(context, arry_months, 0, maxTextSize, minTextSize);
                wvMonth.setVisibleItems(5);
                wvMonth.setViewAdapter(mMonthAdapter);
                wvMonth.setCurrentItem(0);
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

        //TODO 设置日的联动
        wvMonth.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mMonthAdapter);
                setMonthAdapterIndex(Integer.parseInt(currentText));
                selectMonth = currentText;
                selectMonthValue = Integer.parseInt(currentText);
                setDayRange(selectYearValue,selectMonthValue);
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
        //TODO 设置日的选择，不需要联动
        wvDay.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mDaydapter);
                selectDay = currentText;
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

    private void setYearAdapter() {
        mYearAdapter = new CalendarTextAdapter(context, arry_years, setYearAdapterIndex(currentYear), maxTextSize, minTextSize);
        wvYear.setVisibleItems(5);
        wvYear.setViewAdapter(mYearAdapter);
        wvYear.setCurrentItem(setYearAdapterIndex(currentYear));
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
    public void setYearRange() {
        arry_years.clear();
        switch (currentType) {
            case NORMAL: {
                //TODO 默认不会对
                for (int i = firstYearTopLimit; i < secondYearLowerLimit; i++) {
                    arry_years.add(i + "");
                }
                break;
            }
            case BEFORE: {
                for (int i = firstYearTopLimit; i <= getCurrentYear(); i++) {
                    arry_years.add(i + "");
                }
                break;
            }
            case BEFORE_EXCEPT: {
                for (int i = firstYearTopLimit; i <= getCurrentYear(); i++) {
                    arry_years.add(i + "");
                }
                break;
            }
            case AFTER: {
                for (int i = getCurrentYear(); i <= secondYearLowerLimit; i++) {
                    arry_years.add(i + "");
                }
                break;
            }
            case AFTER_EXCEPT: {
                for (int i = getCurrentYear(); i <= secondYearLowerLimit; i++) {
                    arry_years.add(i + "");
                }
                break;
            }
            case NOLIMIT: {
                for (int i = getCurrentYear() - DEFAULT_LIMIT_LENGTH_YEAR; i <= getCurrentYear() + DEFAULT_LIMIT_LENGTH_YEAR; i++) {
                    arry_years.add(i + "");
                }
                break;
            }
        }
    }

    //TODO 仅设置当前这一年的范围
    public void setMonthRange(int year) {
        arry_months.clear();
        switch (currentType) {
            case NORMAL: {
                //TODO 默认不会对
                if (year == firstYearTopLimit && year == secondYearLowerLimit) {
                    for (int i = firstMonthTopLimit; i <= secondMonthLowerLimit; i++) {
                        arry_months.add(i + "");
                    }
                } else if (year == firstYearTopLimit) {
                    for (int i = firstMonthTopLimit; i <= 12; i++) {
                        arry_months.add(i + "");
                    }
                } else if (year == secondYearLowerLimit) {
                    for (int i = 1; i <= secondMonthLowerLimit; i++) {
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
                if (year == firstYearTopLimit) {
                    for (int i = firstMonthTopLimit; i <= getCurrentMonth(); i++) {
                        arry_months.add(i + "");
                    }
                } else {
                    for (int i = 1; i <= getCurrentMonth(); i++) {
                        arry_months.add(i + "");
                    }
                }
                break;
            }
            case BEFORE_EXCEPT: {
                if (year == firstYearTopLimit) {
                    for (int i = firstMonthTopLimit; i <= getCurrentMonth(); i++) {
                        arry_months.add(i + "");
                    }
                } else {
                    for (int i = 1; i <= getCurrentMonth(); i++) {
                        arry_months.add(i + "");
                    }
                }
                break;
            }
            case AFTER: {
                if (year == secondYearLowerLimit) {
                    for (int i = getCurrentMonth(); i <= secondMonthLowerLimit; i++) {
                        arry_months.add(i + "");
                    }
                } else {
                    for (int i = getCurrentMonth(); i <= 12; i++) {
                        arry_months.add(i + "");
                    }
                }
                break;
            }
            case AFTER_EXCEPT: {
                if (year == secondYearLowerLimit) {
                    for (int i = getCurrentMonth(); i <= secondMonthLowerLimit; i++) {
                        arry_months.add(i + "");
                    }
                } else {
                    for (int i = getCurrentMonth(); i <= 12; i++) {
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
    public void setDayRange(int year,int month) {//指定年月的日期范围
        arry_days.clear();
        switch (currentType) {
            case NORMAL: {
                //TODO 默认不会对
                if (year == firstYearTopLimit && month == firstMonthTopLimit &&
                        year == secondYearLowerLimit && month == firstMonthTopLimit) {
                    for (int i = firstLastMonthDayLimit; i <= secondLastMonthDayLimit; i++) {
                        arry_days.add(i + "");
                    }
                } else if (year == firstYearTopLimit  && month == firstMonthTopLimit &&
                        year == getCurrentYear()  && month == getCurrentMonth()) {
                    for (int i = firstMonthTopLimit; i <= getCurrentDay(); i++) {
                        arry_days.add(i + "");
                    }
                } else if (year == firstYearTopLimit  && month == firstMonthTopLimit) {
                    for (int i = firstMonthTopLimit; i <= getSpecificYearMonthMaxDay(year,month); i++) {
                        arry_days.add(i + "");
                    }
                } else if(year == secondYearLowerLimit && month == firstMonthTopLimit &&
                        year == getCurrentYear()  && month == getCurrentMonth()){

                }



                else {
                    for (int i = 1; i <= 12; i++) {
                        arry_days.add(i + "");
                    }
                }
                break;
            }
            case BEFORE: {
                break;
            }
            case BEFORE_EXCEPT: {
                break;
            }
            case AFTER: {
                break;
            }
            case AFTER_EXCEPT: {
                break;
            }
            case NOLIMIT: {
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
                onBirthListener.onClick(selectYear, selectMonth, selectDay);
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

    public int getCurrentYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public int getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    public int getCurrentDay() {
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
        switch (currentType) {
            case BEFORE_EXCEPT: {
                if (day == 1) {
                    month -= 1;
                    if (month < 1) {
                        year -= 1;
                        month = 12;
                    }
                    day = getSpecificYearMonthMaxDay(year, month);
                } else {
                    day -= day;
                }
                selectYear = year + "";
                selectMonth = month + "";
                selectDay = day + "";
                break;
            }
            case AFTER_EXCEPT: {
                if (day == getSpecificYearMonthMaxDay(year, month)) {
                    month += 1;
                    if (month > 12) {
                        month = 1;
                        year += 1;
                    }
                    day = 1;
                } else {
                    day += day;
                }
                selectYear = year + "";
                selectMonth = month + "";
                selectDay = day + "";
                break;
            }
            default: {
                selectYear = year + "";
                selectMonth = month + "";
                selectDay = day + "";
                break;
            }
        }


        isSetData = true;

//        //TODO 这里写法有点问题---不论当前是不是今年，month都设置成了12
//        if (year == getCurrentYear()) {
//            this.monthRange = getCurrentMonth();
//        } else {
//
//        }
//        this.monthRange = 12;

    }

    /**
     * 设置年份----设置设置第几个项
     *
     * @param year
     */
    public int setYearAdapterIndex(int year) {
        int yearIndex = 0;
        if (year != getCurrentYear()) {

        } else {
            this.monthRange = getCurrentMonth();
        }
        this.monthRange = 12;
        for (int i = getCurrentYear() + 20; i > 1950; i--) {
            if (i == year) {
                return yearIndex;
            }
            yearIndex++;
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
        getMonthDayRangeDays(currentYear, month);
        for (int i = 1; i < this.monthRange; i++) {
            if (month == i) {
                return monthIndex;
            } else {
                monthIndex++;
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
    public int getSpecificYearMonthMaxDay(int year, int month) {
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
    private void sortTimeLimit() {
        //TODO 需要重置，并规范年月日的限制范围--向前，或向后，不同的范围
        //TODO 按照当前的时间，分别计算出当前 日，月，年的偏移值

        //临时保存限制的
        int tempDay = daySelectLength;
        int tempMonth = monthSelectLength;
        int tempYear = yearSelectLength;

        //********************************   向前偏移  先算从当前日期开始的（含有今天的）

        //*********** 若是不含当前，则将计算数值+1
        /*
        firstCurrentMonthDayLimit  计算时，起始日期，当月当日

        firstLastMonthDayLimit  日期的尽头，当不包含当天时


         */
        //不含当前时，则需要-1天
        firstCurrentMonthDayLimit = getCurrentDay();
        // 设置上限的初始值---需要通过计算得到实际值
        firstMonthTopLimit = getCurrentMonth();
        firstYearTopLimit = getCurrentYear();

        //TODO 得到当前月份可选择的日期范围---选择最小值
        /**
         * 当选择的是当前，年，月时，
         * 日期范围 在  1 ~~~~  firstCurrentMonthDayLimit 之间
         */
        if (tempMonth == 0 && tempYear == 0) {
            if (getCurrentDay() >= tempDay) {
                //TODO 范围在当月
                firstLastMonthDayLimit = getCurrentDay() - tempDay + 1;//TODO 范围即从 firstLastMonthDayLimit ~  firstCurrentMonthDayLimit
                firstMonthLimit = 0;
            } else {
                tempDay = tempDay - getCurrentDay();//
                while (true) {
                    //超出了当月的，跨度为两个月 --- 先计算 上一个月时哪年哪月
                    firstMonthLimit++;
                    firstMonthTopLimit = getCurrentMonth() - 1;
                    // 计算年份的的上限
                    if (firstMonthTopLimit <= 0) {
                        firstMonthTopLimit = 12;
                        firstYearTopLimit = firstYearTopLimit - 1;// 变为上一年
                    } else {

                    }
                    //若计算后的剩余日期数，少于往前推一个月那个月份的
                    if (tempDay > getSpecificYearMonthMaxDay(firstYearTopLimit, firstMonthTopLimit)) {
                        // 减去往前推一个月的那月日期总数
                        tempDay = tempDay - getSpecificYearMonthMaxDay(firstYearTopLimit, firstMonthTopLimit);
                    } else {
                        //TODO 这里的计算方式与时间少于当月的计算方式相同
                        firstLastMonthDayLimit = getSpecificYearMonthMaxDay(firstYearTopLimit, firstMonthTopLimit) - tempDay + 1;
                        // 年月不再需要变化---终止计算的循环
                        break;
                    }
                }
            }
        } else {//(getCurrentDay() >= tempDay ? tempDay : getCurrentDay())
            if (getCurrentDay() >= tempDay) {
                //TODO 范围在当月
                firstLastMonthDayLimit = getCurrentDay() - tempDay + 1;//TODO 范围即从 firstLastMonthDayLimit ~  firstCurrentMonthDayLimit
                firstMonthTopLimit = (getCurrentMonth() + tempMonth) % 12;
                firstYearTopLimit = getCurrentYear() - tempYear - (getCurrentMonth() + tempMonth) / 12;
                firstMonthLimit = tempYear * 12 + tempMonth;
            } else {
                tempDay = tempDay - getCurrentDay();//
                while (true) {
                    firstMonthLimit++;
                    //超出了当月的，跨度为两个月 --- 先计算 上一个月时哪年哪月
                    firstMonthTopLimit = getCurrentMonth() - 1;
                    // 计算年份的的上限
                    if (firstMonthTopLimit <= 0) {
                        firstMonthTopLimit = 12;
                        firstYearTopLimit = firstYearTopLimit - 1;// 变为上一年
                    } else {

                    }
                    //若计算后的剩余日期数，少于往前推一个月那个月份的
                    if (tempDay > getSpecificYearMonthMaxDay(firstYearTopLimit, firstMonthTopLimit)) {
                        // 减去往前推一个月的那月日期总数
                        tempDay = tempDay - getSpecificYearMonthMaxDay(firstYearTopLimit, firstMonthTopLimit);
                    } else {
                        //TODO 这里的计算方式与时间少于当月的计算方式相同
                        firstLastMonthDayLimit = getSpecificYearMonthMaxDay(firstYearTopLimit, firstMonthTopLimit) - tempDay + 1;
                        // 年月不再需要变化---终止计算的循环
                        break;
                    }
                }
                //结束日期计算---开始计算年份月份
                firstYearTopLimit = firstYearTopLimit - tempYear - (firstMonthTopLimit + tempMonth) / 12;
                firstMonthTopLimit = (firstMonthTopLimit + tempMonth) % 12;

                firstMonthLimit = firstMonthLimit + tempMonth + tempYear * 12;
                ;
            }
        }
        //********************************   向后偏移---与向前偏移时相似，不过计算的规则需要变化（含有今天）

        tempDay = daySelectLength;
        tempMonth = monthSelectLength;
        tempYear = yearSelectLength;
        //不含当前时，则需要-1天
        secondCurrentMonthDayLimit = getCurrentDay();
        // 设置上限的初始值---需要通过计算得到实际值
        secondMonthLowerLimit = getCurrentMonth();
        secondYearLowerLimit = getCurrentYear();

        //TODO 得到当前月份可选择的日期范围---选择最小值
        /**
         * 当选择的是当前，年，月时，
         * 日期范围 在   secondCurrentMonthDayLimit ~~~~~  月底 之间
         */
        if (tempMonth == 0 && tempYear == 0) {
            if (getSpecificYearMonthMaxDay(secondYearLowerLimit, secondMonthLowerLimit) >= (getCurrentDay() + tempDay)) {
                //TODO 范围在当月
                secondLastMonthDayLimit = getCurrentDay() + tempDay;//TODO 范围即从 secondCurrentMonthDayLimit ~  secondLastMonthDayLimit
                secondMonthLimit = 0;
            } else {
                tempDay = tempDay - (getSpecificYearMonthMaxDay(secondYearLowerLimit, secondMonthLowerLimit) - getCurrentDay());// 减去当月剩余的天数
                while (true) {
                    //超出了当月的，跨度为两个月 --- 先计算 下一个月是哪年哪月
                    secondMonthLimit++;
                    secondMonthLowerLimit = getCurrentMonth() + 1;// 月份往后推一个月
                    // 计算年份的的上限
                    if (secondMonthLowerLimit > 12) {//当超过当年的月份时，变更为下一年
                        secondMonthLowerLimit = 1;
                        secondYearLowerLimit = secondYearLowerLimit + 1;// 变为下一年
                    } else {

                    }

                    //若计算后的剩余日期数，少于往后推一个月那个月份的
                    if (tempDay > getSpecificYearMonthMaxDay(secondYearLowerLimit, secondMonthLowerLimit)) {
                        // 减去往前推一个月的那月日期总数
                        tempDay = tempDay - getSpecificYearMonthMaxDay(secondYearLowerLimit, secondMonthLowerLimit);
                    } else {
                        //TODO 这里的计算方式与时间少于当月的计算方式相同
                        secondLastMonthDayLimit = tempDay;//TODO 就是这一天了
                        // 年月不再需要变化---终止计算的循环
                        break;
                    }
                }
            }
        } else {// 单独的月份与年份范围不为0
            if (getSpecificYearMonthMaxDay(secondYearLowerLimit, secondMonthLowerLimit) >= (getCurrentDay() + tempDay)) {
                //TODO 范围在当月
                secondLastMonthDayLimit = getCurrentDay() + tempDay;//TODO 范围即从 secondCurrentMonthDayLimit ~  secondLastMonthDayLimit
                //TODO 计算最终的年份，月份下线
                secondMonthLimit = 0 + tempMonth + tempYear * 12;
                secondMonthLowerLimit = secondMonthLowerLimit + tempMonth;
                secondYearLowerLimit = secondYearLowerLimit + tempYear + (secondMonthLowerLimit + tempMonth) / 12;
            } else {
                /**
                 从 getCurrentDay()  ~  到月底
                 */
                tempDay = tempDay - (getSpecificYearMonthMaxDay(secondYearLowerLimit, secondMonthLowerLimit) - getCurrentDay());//减去--- 当月剩余的时间
                while (true) {
                    //超出了当月的，跨度为两个月 --- 先计算 上一个月时哪年哪月
                    secondMonthLimit++;

                    secondMonthLowerLimit = getCurrentMonth() + 1;
                    // 计算年份的的上限
                    if (secondMonthLowerLimit > 12) {
                        secondMonthLowerLimit = 1;
                        secondYearLowerLimit = secondYearLowerLimit + 1;// 变为下一年
                    } else {

                    }

                    //若计算后的剩余日期数，少于往前推一个月那个月份的
                    if (tempDay > getSpecificYearMonthMaxDay(secondYearLowerLimit, secondMonthLowerLimit)) {
                        // 减去往前推一个月的那月日期总数
                        tempDay = tempDay - getSpecificYearMonthMaxDay(secondYearLowerLimit, secondMonthLowerLimit);
                    } else {
                        //TODO 这里的计算方式与时间少于当月的计算方式相同
                        secondLastMonthDayLimit = tempDay;//TODO 就是这一天了 1~~~~这一天
                        // 年月不再需要变化---终止计算的循环
                        break;
                    }
                }
            }
        }

        //TODO 按照包含今天的计算完成后，根据选择的类型，纠正对应的数据
        switch (currentType) {
            case NORMAL: {
                //TODO 默认不会对前后进行限制
                break;
            }
            case BEFORE: {
                break;
            }
            case BEFORE_EXCEPT: {//TODO 矫正范围数据
                if (getCurrentDay() == 1) {//TODO 月初
                    int tempCountMonth = getCurrentMonth();
                    int tempCountYear = getCurrentYear();
                    tempCountMonth -= 1;
                    if (tempCountMonth < 1) {
                        tempCountYear -= 1;
                        tempCountMonth = 12;
                    }
                    firstCurrentMonthDayLimit = getSpecificYearMonthMaxDay(tempCountYear, tempCountMonth);
                } else {
                    firstCurrentMonthDayLimit -= 1;
                }

                if (firstLastMonthDayLimit == 1) {
                    firstMonthTopLimit -= 1;
                    if (firstMonthTopLimit < 1) {
                        firstMonthTopLimit = 12;
                        firstYearTopLimit -= 1;
                    }
                    firstLastMonthDayLimit = getSpecificYearMonthMaxDay(firstYearTopLimit, firstMonthTopLimit);
                } else {
                    firstLastMonthDayLimit -= 1;
                }

                break;
            }
            case AFTER: {
                break;
            }
            case AFTER_EXCEPT: {
                if (getCurrentDay() == getSpecificYearMonthMaxDay(getCurrentYear(), getCurrentMonth())) {//TODO 月末
                    int tempCountMonth = getCurrentMonth();
                    int tempCountYear = getCurrentYear();
                    tempCountMonth += 1;
                    if (tempCountMonth > 12) {
                        tempCountYear += 1;
                        tempCountMonth = 1;
                    }
                    secondCurrentMonthDayLimit = 1;
                } else {
                    secondCurrentMonthDayLimit += 1;
                }

                if (secondLastMonthDayLimit == getSpecificYearMonthMaxDay(secondYearLowerLimit, secondMonthLowerLimit)) {
                    secondMonthLowerLimit += 1;
                    if (secondMonthLowerLimit > 12) {
                        secondMonthLowerLimit = 1;
                        secondYearLowerLimit += 1;
                    }
                    secondLastMonthDayLimit = 1;
                } else {
                    secondLastMonthDayLimit += 1;
                }
                break;
            }
            case NOLIMIT: {
                break;
            }
        }
    }
}