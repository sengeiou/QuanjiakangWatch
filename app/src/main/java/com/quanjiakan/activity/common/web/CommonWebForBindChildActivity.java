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
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.ICommonActivityRequestCode;
import com.quanjiakan.activity.base.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.upload.UploadUtil;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.common.NetTypeCheckUtil;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.image.BitmapUtil;
import com.quanjiakan.util.image.IImageCropInterface;
import com.quanjiakan.util.image.ImageCropHandler;
import com.quanjiakan.util.image.ImageUtils;
import com.quanjiakan.view.dialog.BottomSelectImageDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by john on 2017-11-24.
 */

public class CommonWebForBindChildActivity extends BaseActivity {

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
        initView();
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

        progress.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5, 0));
        progress.setMax(100);


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

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

        mWebview.addJavascriptInterface(new JSInterface(), "hgj");

        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                last_url = url;
                if (url.contains("activate_success.html")) {
                    //提示激活成功
                    CommonDialogHint.getInstance().showHint(CommonWebForBindChildActivity.this, getString(R.string.hint_web_active_success), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }else if (url.contains("hgj://take/photo")){
                    showImageSelectDialog();
                    return true;
                }
                view.loadUrl(url);
                return true;
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
        refresh.setEnabled(false);//激活流程中禁止刷新
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);

                if(!NetTypeCheckUtil.isNetAvailable(CommonWebForBindChildActivity.this)){
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

    private File file;
    private String imagePath = null;
    private String mCurrentPhotoPath = null;
    protected void showImageSelectDialog() {
        BottomSelectImageDialog dialog = new BottomSelectImageDialog(this);
        dialog.setmCameraResultListener(new BottomSelectImageDialog.onCameraResultListener() {
            @Override
            public void OnFilePath(String path) {
                mCurrentPhotoPath = path;
            }
        });
        dialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode){
            case ICommonActivityRequestCode.REQUEST_CODE_CAPTURE_CAMEIA:
                if (resultCode == RESULT_OK) {
                    if (mCurrentPhotoPath != null) {
                        ImageCropHandler.beginCrop(this, Uri.fromFile(new File(mCurrentPhotoPath)));
                        /**
                         * 是否进行裁剪：裁剪则进行上面的操作，否则，直接进行文件上传
                         */
                    }
                }
                break;
            case ICommonActivityRequestCode.REQUEST_PICK:
                if (resultCode == RESULT_OK) {
                    ImageCropHandler.beginCrop(this, intent.getData());
                    /**
                     * sourcePath = ImageUtils.uri2Path(data, HealthInquiryFurtherAskActivity.this);
                     */
                }
            case ICommonActivityRequestCode.REQUEST_CROP:
                doCrop(requestCode, resultCode, intent);
                break;
        }
    }

    //TODO 处理图片的裁剪
    public void doCrop(final int requestCode, final int resultCode, final Intent data) {
        ImageCropHandler.handleCrop(null, resultCode, data, new IImageCropInterface() {
            @Override
            public void getFilePath(String path) {
                imagePath = null;
                mCurrentPhotoPath = path;
                //TODO 修正上传图片为 400*400的分辨率，而不是默认的160*160
                Bitmap smallBitmap = BitmapUtil.getSmallBitmap(mCurrentPhotoPath, 400, 400);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                smallBitmap.compress(Bitmap.CompressFormat.PNG, 25, baos);
                String filename = System.currentTimeMillis() + ".png";
                String file_path = ImageUtils.saveBitmapToStorage(filename, smallBitmap);
                file = new File(file_path);

                uploadImageFile(null,file_path.toString());
            }
        });
    }

    public void uploadImageFile(String name,String path){
        String url = "http://picture.quanjiakan.com:9080/familycare-binary/upload?&platform=2&project=1" +
                "&token=" + BaseApplication.getInstances().getLoginInfo().getToken() +
                "&memberId=" + BaseApplication.getInstances().getLoginInfo().getUserId() +
                "&storage=12";//TODO 这个需要---如果没有可能会报参数错误的异常

        HashMap<String, String> paramsFile = new HashMap<>();
        paramsFile.put("file", path);
        paramsFile.put("filename", file.getName());
        paramsFile.put("image", file.getAbsolutePath());
        UploadUtil.uploadFile(this, url, null, paramsFile);
    }

    private class JSInterface {

        @JavascriptInterface
        public void acceptUrl(String imgUrl) {//此方法是将android端获取的url返给js

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


    @Override
    public Object getParamter(int type) {
        switch (type){
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD:
                HashMap<String,String> params = new HashMap<>();
                return params;
        }
        return null;
    }

    @Override
    public void showMyDialog(int type) {
        switch (type){
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD:
                getDialog(this,getString(R.string.hint_common_submit_file_image));//正在获取数据...
                break;
        }
    }

    @Override
    public void dismissMyDialog(int type) {
        switch (type){
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD:
                break;
        }
        dismissDialog();
    }

    @Override
    public void onSuccess(int type, int httpResponseCode, Object result) {
        switch (type){
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD:
                afterUpload(result);
                break;
        }
    }

    @Override
    public void onError(int type, int httpResponseCode, Object errorMsg) {
        switch (type){
            case IPresenterBusinessCode.COMMON_FILE_UPLOAD:
                break;
        }
        if(errorMsg!=null && errorMsg.toString().length()>0){
            CommonDialogHint.getInstance().showHint(this,errorMsg.toString());
        }else{
            CommonDialogHint.getInstance().showHint(this,getString(R.string.error_common_net_request_fail));
        }
    }

    //TODO 处理图片上传完成后的数据
    public void afterUpload(Object result) {
        if (result != null && result instanceof String) {
            //  {"code":"200","message":"http://picture.quanjiakan.com/quanjiakan/resources/chunyu/images/20171122115337_y8isoq1jzyi3gtvh28rt.png"}
            LogUtil.e("afterUpload:" + result.toString());
            if (result.toString() != null && !result.toString().equals("") && result.toString().toLowerCase().startsWith("{")) {
                //上传文件成功
                try {
                    JSONObject json = new JSONObject(result.toString());
                    if (json.has("code") && "200".equals(json.getString("code"))) {
                        /**
                         * 上传完成后获取的地址
                         * json.getString("message")
                         *
                         *
                         * 将地址传递到第二个页面
                         */
                        imagePath = json.getString("message");
                        mWebview.loadUrl("javascript:acceptUrl('" + imagePath + "')");
                    } else {
                        if(json.has("message") && json.getString("message")!=null){
                            CommonDialogHint.getInstance().showHint(this, json.getString("message"));
                        }else{
                            CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_common_submit_upload_fail));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    MobclickAgent.reportError(this, e);
                }

            } else {
                //文件上传失败
                CommonDialogHint.getInstance().showHint(this, getString(R.string.hint_common_submit_upload_fail));
            }

            if (file.exists()) {
                file.delete();
            }
        }
    }



}
