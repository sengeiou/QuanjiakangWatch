package com.quanjiakan.activity.common.image;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import uk.co.senab.photoview.PhotoView;


/**
 * 公共图片查看类
 */
public class ImageViewerActivity extends BaseActivity implements OnClickListener {
	private ImageButton ibtn_back;
	private TextView title;
	private PhotoView photoViewer;
	private String url;
	private ImageViewerActivity instances;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * ******************************************
		 * 方式一
		 */
		setContentView(R.layout.layout_image_viewer);
		initTitleBar();


		url = getIntent().getStringExtra(IParamsName.PARAMS_URL);
		if(url==null || "".equals(url.trim()) || url.length()<1){
			CommonDialogHint.getInstance().showHint(this, getResources().getString(R.string.error_image_path), new OnClickListener() {
				@Override
				public void onClick(View view) {
					finish();
				}
			});
			return;
		}

		intiView();
	}

	public void initTitleBar(){
		ibtn_back = (ImageButton) findViewById(R.id.ibtn_back);
		ibtn_back.setVisibility(View.VISIBLE);
		ibtn_back.setOnClickListener(this);
		title = (TextView) findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.title_picture_viewer));
	}

	public void intiView(){
		photoViewer = (PhotoView) findViewById(R.id.viewer);
		Picasso.with(this).load(url)./*fit().*/
				into(photoViewer);;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.ibtn_back:
				finish();
				break;
			default:
				break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(this.getClass().getSimpleName());
	}

	@Override
	public void onResume() {
		super.onResume();
		instances = this;
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(this.getClass().getSimpleName());
	}

	@Override
	public void onStop() {
		super.onStop();
		instances = null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
