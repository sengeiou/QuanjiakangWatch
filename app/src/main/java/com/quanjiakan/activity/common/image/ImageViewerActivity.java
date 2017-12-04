package com.quanjiakan.activity.common.image;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.dialog.QuanjiakanDialog;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

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

	private Bitmap bitmap;

	/**
	 * 暂未进行缓存，需要进行改进
	 *
	 * 提供自己的ImageCache方式，
	 */
	class LoadImageTask extends AsyncTask<String,Integer,Void> {
		Dialog dialog;
		@Override
		public void onPreExecute() {
			super.onPreExecute();
			bitmap = null;
			dialog = QuanjiakanDialog.getInstance().getDialog(ImageViewerActivity.this,getResources().getString(R.string.hint_loading_image));
		}

		@Override
		public Void doInBackground(String... strings) {
			try {
				String url = strings[0];
				if(url!=null && url.toLowerCase().startsWith(ICommonData.PICTURE_COMMON_PREFIX)){
					/**
					 * 排除不符合规定的编码
					 */
					String encodedUrl = Uri.encode(url, ICommonData.ENCODE_ALLOW);
					HttpURLConnection conn = null;
					conn = (HttpURLConnection)(new URL(encodedUrl)).openConnection();
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(20000);
					/**
					 * 处理重定向
					 */
					for(int redirectCount = 0; conn.getResponseCode() / 100 == 3 && redirectCount < 5; ++redirectCount) {
						conn = this.createConnection(conn.getHeaderField(ICommonData.HTTP_HEADER_LOCATION));
					}
					int responseCode = conn.getResponseCode();
					if(responseCode== HttpURLConnection.HTTP_OK){
						if(conn.getContentEncoding()!=null && conn.getContentEncoding().contains(ICommonData.HTTP_HEADER_ENCODE_GZIP)){
							InputStream is = new GZIPInputStream(new BufferedInputStream(conn.getInputStream()));
							bitmap = BitmapFactory.decodeStream(is);
							is.close();
						}else{
							InputStream is = conn.getInputStream();
							bitmap = BitmapFactory.decodeStream(is);
							is.close();
						}
					}else{
						bitmap = null;
						if(conn.getInputStream()!=null){
							conn.getInputStream().close();
						}
					}
				}else{
					File file;
					if(url!=null && url.toLowerCase().startsWith(ICommonData.FILE_PATH_PREFIX1)){
						file = new File(url.toLowerCase().replace(ICommonData.FILE_PATH_PREFIX2,ICommonData.EMPTY));
					}else if(url!=null && url.toLowerCase().startsWith(ICommonData.FILE_PATH_PREFIX2)){
						file = new File(url.toLowerCase().replace(ICommonData.FILE_PATH_PREFIX3,ICommonData.EMPTY));
					}else{
						file = new File(url);
					}
					if(file.exists()){
						bitmap = BitmapFactory.decodeFile(url);
					}else{
						bitmap = null;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			dialog.dismiss();
			if(instances!=null){
				if(bitmap!=null){
					if(photoViewer!=null){
						photoViewer.setImageBitmap(bitmap);
					}
				}else{
					CommonDialogHint.getInstance().showHint(ImageViewerActivity.this,getString(R.string.error_net_analysis));
				}
			}
		}


		public HttpURLConnection createConnection(String url) throws IOException {
			String encodedUrl = Uri.encode(url, ICommonData.ENCODE_ALLOW);
			HttpURLConnection conn = (HttpURLConnection)(new URL(encodedUrl)).openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(20000);
			return conn;
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
		if(bitmap!=null){
			bitmap.recycle();
			bitmap = null;
		}
	}
}
