package com.quanjiakan.activity.common.web;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.util.common.NetTypeCheckUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by john on 2017-11-24.
 */

public class CommonWebActivity extends BaseActivity {

    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    private String titleName;
    private String urlPath;

    private String last_url;

    private static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri[]> uploadMessage;
    private ValueCallback<Uri> mUploadMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_common_web);
        ButterKnife.bind(this);
        urlPath = getIntent().getStringExtra(IParamsName.PARAMS_COMMON_WEB_URL);
        titleName = getIntent().getStringExtra(IParamsName.PARAMS_COMMON_WEB_TITLE);
        initTitle();
        if (urlPath == null || urlPath.length() < 1 || ICommonData.HTTP_PREFIX.startsWith(urlPath.toLowerCase())) {
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_error_activity_parameters), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            return;
        }
        resetTitle();
    }

    @Override
    public void onStart() {
        super.onStart();
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
    //TODO ***********************************************

    public void initTitle() {
        ibtnBack.setVisibility(View.VISIBLE);

        tvTitle.setText(R.string.hint_web_tilte);
    }

    public void resetTitle() {
        if (titleName != null && titleName.length() > 0) {
            tvTitle.setText(titleName);
        }
    }

    public void initView(){
        mWebview = (WebView) findViewById(R.id.webview);

        mWebview.getSettings().setJavaScriptEnabled(true);

        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.getSettings().setLoadWithOverviewMode(true);

        mWebview.getSettings().setSupportZoom(true);
        mWebview.getSettings().setBuiltInZoomControls(true);
        mWebview.getSettings().setDisplayZoomControls(false);

        mWebview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);

        mWebview.getSettings().setAppCacheEnabled(true);
        mWebview.getSettings().setLightTouchEnabled(true);
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.getSettings().setDatabaseEnabled(true);

        mWebview.getSettings().setAllowFileAccess(true);
        mWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        mWebview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        mWebview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                    mWebview.clearCache(true);
                    progress.setVisibility(View.INVISIBLE);
                } else {
                    // 加载中
                    progress.setVisibility(View.VISIBLE);
                    progress.setProgress(newProgress);
                }
            }

            //************     由于4.4WebView被系统禁止调用，需要使用JS互相调用的形式进行传递
            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

            }

            //For Android 5.0
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                // make sure there is no existing message
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }
                uploadMessage = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    return false;
                }
                return true;
            }

        });

        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                last_url = url;
                if (last_url!=null&&last_url.contains("goods.php?id=301")) {  //这个网页存在问题特殊处理
                    refresh.setEnabled(false);
                }else {
                    refresh.setEnabled(true);
                }
//				view.loadUrl(last_url);
                return false;//解决跳转第三方页面重定向问题，返回false交给webview处理
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        if(NetTypeCheckUtil.isNetAvailable(this)){
            mWebview.loadUrl(urlPath);
        }else{
            mWebview.setVisibility(View.GONE);
        }

        refresh.setColorSchemeResources(R.color.holo_green_light);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);

                if(!NetTypeCheckUtil.isNetAvailable(CommonWebActivity.this)){
                    return;
                }
                if (last_url != null) {
                    mWebview.loadUrl(last_url);
                } else {
                    mWebview.loadUrl(urlPath);
                }

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == REQUEST_SELECT_FILE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (uploadMessage == null) return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == keyEvent.KEYCODE_BACK) {
            if (mWebview.canGoBack()) {
                mWebview.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, keyEvent);

    }

    @OnClick(R.id.ibtn_back)
    public void onViewClicked() {
        if(mWebview.canGoBack()){
            mWebview.goBack();
        }else{
            finish();
        }
    }
}
