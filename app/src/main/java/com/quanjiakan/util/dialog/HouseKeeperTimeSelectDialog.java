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

    private boolean issetdata = false;

    //TODO 当前选中的时间
    private String selectYear;
    private String selectMonth;
    private String selectDay;

    private OnBirthListener onBirthListener;

    /**
     * TODO 限制类型
     * 当选择before时，限制
     */
    public enum DIALOG_TYPE {
        BEFORE, NORMAL, AFTER, NOLIMIT
    }

    private static final int CURRENT_LIMIT = 0;//TODO 限制为当前范围(使用默认的限制长度)，即当前，年月日

    //TODO 无限制时的默认限制长度
    private static final int DEFAULT_LIMIT_LENGTH = 120;//TODO 限制默认范围为一百二十---为年龄选择做兼容
    private static final int DEFAULT_LIMIT_LENGTH_MONTH = 12;//TODO 限制默认范围为一百二十---为年龄选择做兼容

    //TODO 年月日的限制时间---初始化范围时使用  往前算的时间范围
    private int firstLastMonthDayLimit = CURRENT_LIMIT;//TODO 最后一个月 限制的日期范围（从月初，或月末算起，当总的限制天数在当前月内时除外）
    private int firstMonthLimit = CURRENT_LIMIT;//TODO 限制的时间中，总共跨度的月数
    private int firstCurrentMonthDayLimit = CURRENT_LIMIT;//TODO 当前月的日期范围（从当前日开始算起）
    private int firstYearTopLimit = CURRENT_LIMIT;//TODO 限制的年份上限
    private int firstMonthTopLimit = CURRENT_LIMIT;//TODO 限制的月份的上限

    //TODO 往后算的时间范围
    private int secondLastMonthDayLimit = CURRENT_LIMIT;
    private int secondMonthLimit = CURRENT_LIMIT;
    private int secondCurrentMonthDayLimit = CURRENT_LIMIT;
    private int secondYearLowerLimit = CURRENT_LIMIT;//TODO 限制的年份上限
    private int secondMonthLowerLimit = CURRENT_LIMIT;//TODO 限制的月份的上限

    private final DIALOG_TYPE currentType;
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
        if (!issetdata) {
            initData();
        }
        //TODO 初始化年份时间----设置限制范围
        setYearRange();
        setYearAdapter();
        //TODO 初始化月份时间-----设置限制范围
        setMonthRange(monthRange);
        setMonthAdapter();
        //TODO 初始化年份时间-----设置限制范围
        setDayRange(dayRange);
        setDayAdapter();

        //TODO 设置月和日的联动
        wvYear.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                selectYear = currentText;
                setTextviewSize(currentText, mYearAdapter);
                setYearAdapterIndex(Integer.parseInt(currentText));
                setMonthRange(monthRange);
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
                selectMonth = currentText;
                setTextviewSize(currentText, mMonthAdapter);
                setMonthAdapterIndex(Integer.parseInt(currentText));
                setDayRange(dayRange);
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
        switch (currentType) {
            case NORMAL: {
                break;
            }
            case BEFORE: {
                break;
            }
            case AFTER: {
                break;
            }
            case NOLIMIT: {
                break;
            }
        }
    }

    public void setMonthRange(int months) {
        switch (currentType) {
            case NORMAL: {
                //TODO 默认不会对
                break;
            }
            case BEFORE: {
                break;
            }
            case AFTER: {
                break;
            }
            case NOLIMIT: {
                break;
            }
        }
    }

    public void setDayRange(int days) {
        switch (currentType) {
            case NORMAL: {
                break;
            }
            case BEFORE: {
                break;
            }
            case AFTER: {
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
        selectYear = year + "";
        selectMonth = month + "";
        selectDay = day + "";

        issetdata = true;

        //TODO 这里写法有点问题---不论当前是不是今年，month都设置成了12
        if (year == getCurrentYear()) {
            this.monthRange = getCurrentMonth();
        } else {

        }
        this.monthRange = 12;

        getMonthDayRangeDays(year, month);
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

    //TODO 获取当前月份有多少天
    public int getCurrentMonthMaxDay() {
        boolean leayyear = false;
        if (getCurrentYear() % 4 == 0 && getCurrentYear() % 100 != 0) {
            leayyear = true;
        } else {
            leayyear = false;
        }
        switch (getCurrentMonth()) {
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

        //TODO ********************************   向前偏移
        //TODO 得到当前月份可选择的日期范围---选择最小值
        /**
         * 当选择的是当前，年，月时，
         * 日期范围 在  1 ~~~~  firstCurrentMonthDayLimit 之间
         */
        firstCurrentMonthDayLimit = (getCurrentDay() >= tempDay ? tempDay : getCurrentDay());

        //TODO 计算需要向前推几个月（当日期超出一个月的长度后，需要计算）

        if (tempDay > getCurrentDay()) {//TODO
            firstMonthLimit = 0 + tempMonth;//月份初始差值+设定的差值

            //TODO 上限的年月值，需要计算得到
            firstYearTopLimit= getCurrentYear();

            firstMonthTopLimit = getCurrentMonth();

            while (true) {
                if (tempDay > getSpecificYearMonthMaxDay(firstYearTopLimit, firstMonthTopLimit)) {//TODO 设置的日期限制大于当前月份的值
                    tempDay = tempDay - getSpecificYearMonthMaxDay(firstYearTopLimit, firstMonthTopLimit);//TODO 得到日期的差值
                    firstMonthLimit++;//TODO 增加一个月

                    firstMonthTopLimit--;

                    //TODO 同步需要计算的年份的数据
                    if (firstMonthTopLimit < 1) {//TODO
                        firstYearTopLimit--;
                        firstMonthTopLimit = 12;
                    } else {//TODO 不再进行额外操作

                    }

                }
            }

            //TODO 计算完间隔的月份后 计算间隔的年份（若间隔月份超过12月则会存在）

        }

        //TODO 通过日期后的计算，需要合并原有的设定的年、月的范围
        //TODO 当超出限制后，对应的年份也需要再次变更
        for (int i = 0;i<monthSelectLength;i++){
            firstMonthTopLimit--;
            if(firstMonthTopLimit < 1){
                firstYearTopLimit--;
                firstMonthTopLimit = 12;
            }
        }

        //TODO 计算年份的范围---限制年份最小到达公元0年
        firstYearTopLimit = firstYearTopLimit - yearSelectLength;
        if(firstYearTopLimit<0){
            firstYearTopLimit = 0;
        }


        //TODO ********************************   向后偏移


        switch (currentType) {
            case NORMAL: {
                //TODO 默认不会对前后进行限制
                break;
            }
            case BEFORE: {
                break;
            }
            case AFTER: {
                break;
            }
            case NOLIMIT: {
                break;
            }
        }
    }
}