package com.quanjiakan.activity.common.guide;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.ICommonSharePreferencesKey;
import com.quanjiakan.activity.common.login.SigninActivity_mvp;
import com.quanjiakan.activity.common.main.MainActivity;
import com.quanjiakan.view.InsideViewPager;
import com.quanjiakan.watch.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuidePageActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    InsideViewPager viewPager;
    @BindView(R.id.btn_enter)
    TextView btnEnter;
    @BindView(R.id.rbtn_1)
    RadioButton rbtn1;
    @BindView(R.id.rbtn_2)
    RadioButton rbtn2;
    @BindView(R.id.rbtn_3)
    RadioButton rbtn3;
    @BindView(R.id.rbtn_4)
    RadioButton rbtn4;
    @BindView(R.id.rgp)
    RadioGroup rgp;

    private ArrayList<View> mViews = new ArrayList<>();
    private PackageManager packageManager = null;
    private PackageInfo packageInfo = null;

    // 数据适配器
    private PagerAdapter mPagerAdapter = new PagerAdapter() {

        @Override
        // 获取当前窗体界面数
        public int getCount() {
            return mViews.size();
        }

        @Override
        // 断是否由对象生成界面
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        // 是从ViewGroup中移出当前View
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mViews.get(arg1));
        }

        // 返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mViews.get(arg1));
            return mViews.get(arg1);
        }
    };

    /**
     * **********************************************************************************************************
     * 生命周期方法
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_guidepage);
        ButterKnife.bind(this);
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
     * **********************************************************************************************************
     */

    public void initPackageInfo() {
        packageManager = getPackageManager();
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initView() {
        initPackageInfo();

        rgp = (RadioGroup) findViewById(R.id.rgp);
        rgp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (arg1 == R.id.rbtn_4) {
                    btnEnter.setVisibility(View.VISIBLE);
                } else {
                    btnEnter.setVisibility(View.GONE);
                }
            }
        });


        LayoutParams lp = new LayoutParams();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;

        ImageView image_1 = new ImageView(GuidePageActivity.this);
        image_1.setImageResource(R.drawable.guide_page2);
        image_1.setScaleType(ImageView.ScaleType.FIT_XY);
        image_1.setLayoutParams(lp);

        ImageView image_2 = new ImageView(GuidePageActivity.this);
        image_2.setImageResource(R.drawable.guide_page3);
        image_2.setScaleType(ImageView.ScaleType.FIT_XY);
        image_2.setLayoutParams(lp);

        ImageView image_3 = new ImageView(GuidePageActivity.this);
        image_3.setImageResource(R.drawable.guide_page4);
        image_3.setScaleType(ImageView.ScaleType.FIT_XY);
        image_3.setLayoutParams(lp);

        ImageView image_4 = new ImageView(GuidePageActivity.this);
        image_4.setImageResource(R.drawable.guide_page5);
        image_4.setScaleType(ImageView.ScaleType.FIT_XY);
        image_4.setLayoutParams(lp);

        mViews.add(image_1);
        mViews.add(image_2);
        mViews.add(image_3);
        mViews.add(image_4);

        viewPager = (InsideViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if (arg0 == 0) {
                    rbtn1.setChecked(true);
                    btnEnter.setVisibility(View.GONE);
                    rgp.setVisibility(View.VISIBLE);
                } else if (arg0 == 1) {
                    rbtn2.setChecked(true);
                    btnEnter.setVisibility(View.GONE);
                    rgp.setVisibility(View.VISIBLE);
                } else if (arg0 == 2) {
                    rbtn3.setChecked(true);
                    btnEnter.setVisibility(View.GONE);
                    rgp.setVisibility(View.VISIBLE);
                } else if (arg0 == 3) {
                    rbtn4.setChecked(true);
                    btnEnter.setVisibility(View.VISIBLE);
                    rgp.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @OnClick(R.id.btn_enter)
    public void onViewClicked() {
        //TODO 保存当前版本
        BaseApplication.getInstances().setKeyValue(ICommonSharePreferencesKey.KEY_ACCESSED_WELCOME, "" + packageInfo.versionCode);

        Intent intent;
        //TODO 判断是否存在之前的登录信息，若存在，则使用原来的信息进行登录
        if (BaseApplication.getInstances().isLogin()) {
            intent = new Intent(GuidePageActivity.this, MainActivity.class);
        } else {
            intent = new Intent(GuidePageActivity.this, SigninActivity_mvp.class);
        }
        startActivity(intent);
        finish();
    }

    /**
     * **********************************************************************************************************
     */
}
