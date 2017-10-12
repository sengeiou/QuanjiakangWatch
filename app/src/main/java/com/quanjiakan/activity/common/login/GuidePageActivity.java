package com.quanjiakan.activity.common.login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.SharePreferencesSetting;
import com.quanjiakan.activity.common.main.MainActivity;
import com.quanjiakan.watch.R;
import com.quanjiakan.view.InsideViewPager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public class GuidePageActivity extends BaseActivity {

	private InsideViewPager viewPager;
	private ArrayList<View> mViews = new ArrayList<>();
	private RadioGroup rgp;
	private RadioButton rbtn_1, rbtn_2, rbtn_3, rbtn_4, rbtn_5;
	private TextView btn_enter;
	private PackageManager packageManager = null;
	private PackageInfo packageInfo = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_guidepage);
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

	public void initView() {
		packageManager = getPackageManager();
		try {
			packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		btn_enter = (TextView) findViewById(R.id.btn_enter);
		rgp = (RadioGroup) findViewById(R.id.rgp);
		rbtn_1 = (RadioButton) findViewById(R.id.rbtn_1);
		rbtn_2 = (RadioButton) findViewById(R.id.rbtn_2);
		rbtn_3 = (RadioButton) findViewById(R.id.rbtn_3);
		rbtn_4 = (RadioButton) findViewById(R.id.rbtn_4);
		rgp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if (arg1 == R.id.rbtn_4) {
					btn_enter.setVisibility(View.VISIBLE);
				}else{
					btn_enter.setVisibility(View.GONE);
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
					rbtn_1.setChecked(true);
					btn_enter.setVisibility(View.GONE);
					rgp.setVisibility(View.VISIBLE);
				} else if (arg0 == 1) {
					rbtn_2.setChecked(true);
					btn_enter.setVisibility(View.GONE);
					rgp.setVisibility(View.VISIBLE);
				} else if (arg0 == 2) {
					rbtn_3.setChecked(true);
					btn_enter.setVisibility(View.GONE);
					rgp.setVisibility(View.VISIBLE);
				} else if (arg0 == 3) {
					rbtn_4.setChecked(true);
					btn_enter.setVisibility(View.VISIBLE);
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

		btn_enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent;
				if (SharePreferencesSetting.getInstance().getUserId() != 0) {
					intent = new Intent(GuidePageActivity.this, MainActivity.class);
				} else {
					intent = new Intent(GuidePageActivity.this, SigninActivity_mvp.class);
				}
				SharePreferencesSetting.getInstance().setKeyValue("isyindaoye", ""+packageInfo.versionCode);
				startActivity(intent);
				finish();
			}
		});

	}

	// 数据适配器
	PagerAdapter mPagerAdapter = new PagerAdapter() {

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

	@Override
	public Object getParamter(int type) {
		return null;
	}

	@Override
	public void showMyDialog(int type) {

	}

	@Override
	public void dismissMyDialog(int type) {

	}

	@Override
	public void onSuccess(int type, int httpResponseCode, Object result) {

	}

	@Override
	public void onError(int type, int httpResponseCode, Object errorMsg) {

	}

	@Override
	public View getViewComponentByID(int viewID) {
		return null;
	}
}
