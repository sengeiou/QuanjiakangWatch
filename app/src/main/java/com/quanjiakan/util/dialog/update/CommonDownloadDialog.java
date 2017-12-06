package com.quanjiakan.util.dialog.update;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.entity.VersionInfoEntity;
import com.quanjiakan.util.common.UnitExchangeUtil;
import com.quanjiakan.util.download.IDownloadCallback;
import com.quanjiakan.util.download.MultiThreadAsyncTask;

/**
 * Created by Administrator on 2017/10/10.
 */

public class CommonDownloadDialog {
    /**
     *
     */
    public enum POSITION{
        CENTER,BOTTOM,TOP
    }
    private static CommonDownloadDialog instances;

    /**
     * 检查当前版本
     */

    private CommonDownloadDialog(){

    }
    public static CommonDownloadDialog getInstance(){
        if(instances==null){
            instances = new CommonDownloadDialog();
        }
        return instances;
    }

    private Dialog updateDialog;
    private ProgressBar progressBar;
    private TextView rate;

    private ImageView closeBtn;
    private TextView versionUpdataNewVersionHint;
    private TextView clickBtn;
    private TextView updateContent;
    private boolean currentStatus = false;
    private LinearLayout progressParent;
    private RelativeLayout updateContentParent;
    private PackageManager packageManager = null;
    private PackageInfo packageInfo = null;

    private MultiThreadAsyncTask updateTask;


    /**
     ******************************************************************************************************************************************************************
     *
     * 如果存在多个Dialog显示调用，当第一个没通过交互消失时，后续调用处理将被忽略
     *
     */

    /**
     * 默认处于中间位置
     * @param context
     * @param versionInfoEntity
     */
    public void showUpdateDialog(final Context context,final VersionInfoEntity versionInfoEntity,final IDownloadCallback callback){

        packageManager = context.getPackageManager();
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (versionInfoEntity.getVersionCode() <= packageInfo.versionCode) {
            BaseApplication.getInstances().updateCheckTime();
            return;
        }

        updateDialog = new Dialog(context, R.style.ShareDialog);
        updateDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (updateDialog != null && updateDialog.isShowing()) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update_new_version, null);
        //TODO 关闭按钮
        closeBtn = (ImageView) view.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                if (updateTask != null) {
                    updateTask.stopSubThread();
                    updateTask = null;
                }

                updateDialog.dismiss();
                //TODO 非强制更新则记录检查更新时间
                if(!versionInfoEntity.isForceUpdate()){
                    BaseApplication.getInstances().updateCheckTime();
                }
            }
        });
        //TODO 显示需要更新的这个版本名称
        versionUpdataNewVersionHint = (TextView) view.findViewById(R.id.versionUpdataNewVersionHint);
        versionUpdataNewVersionHint.setText(context.getString(R.string.common_update_version_content_postfix)+versionInfoEntity.getVersion());

        updateContent = (TextView) view.findViewById(R.id.updateContent);
        updateContent.setText(R.string.common_update_content);
        if(versionInfoEntity.getContent()!=null){
            if(versionInfoEntity.getContent().contains("\\\\n")){
                String[] splits = versionInfoEntity.getContent().split("\\\\n");
                String content = "";
                for (String temp:
                        splits) {
                    content+=temp+"\n";
                }
                updateContent.setText(content);
            }else if(versionInfoEntity.getContent().contains("\\n")){
                String[] splits = versionInfoEntity.getContent().split("\\n");
                String content = "";
                for (String temp:
                        splits) {
                    content+=temp+"\n";
                }
                updateContent.setText(content);
            }else if(versionInfoEntity.getContent().contains(";")){
                String[] splits = versionInfoEntity.getContent().split(";");
                String content = "";
                for (String temp:
                        splits) {
                    content+=temp+"\n";
                }
                updateContent.setText(content);
            }

        }
        //*******************************************************************************************
        progressParent = (LinearLayout) view.findViewById(R.id.progressParent);
        updateContentParent = (RelativeLayout) view.findViewById(R.id.updateContentParent);

        progressParent.setVisibility(View.GONE);
        updateContentParent.setVisibility(View.VISIBLE);
        //*******************************************************************************************
        //TODO 更新进度条
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 15, 0));
        progressBar.setMax(100);

        rate = (TextView) view.findViewById(R.id.rate);
        rate.setVisibility(View.VISIBLE);

        //*******************************************************************************************

        clickBtn = (TextView) view.findViewById(R.id.clickBtn);
        clickBtn.setBackgroundResource(R.drawable.selecter_update_btn_update);
        clickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!currentStatus){//TODO 尚未点击更新----
                    currentStatus = true;
                    clickBtn.setBackgroundResource(R.drawable.selecter_update_btn_cancel);


                    progressParent.setVisibility(View.VISIBLE);
                    updateContentParent.setVisibility(View.GONE);
                    closeBtn.setVisibility(View.VISIBLE);
                    //TODO 如果是强制更新，则屏蔽掉点击
                    //TODO 其实会存在一个问题，就是开始安装时，Dialog未dismiss，所以，如果取消安装，回到界面会导致
                    if(versionInfoEntity.isForceUpdate()){
                        clickBtn.setEnabled(false);
                    }

                    //TODO 开始进行更新
                    if (updateTask != null) {
                        updateTask.stopSubThread();
                        updateTask = null;
                    }
                    updateTask = new MultiThreadAsyncTask(context,
                            //TODO 尝试使用增加版本号名，来区分版本名称（虽然理论上不同版本间大小可能不相同，但无法绝对保证）
                            versionInfoEntity.getUrl(),/*versionInfoEntity.getVersion(),*/
                            callback, updateDialog);
                    updateTask.execute("");
                }else{
                    if (updateTask != null) {
                        updateTask.stopSubThread();
                        updateTask = null;
                    }
                    updateDialog.dismiss();
                    BaseApplication.getInstances().updateCheckTime();
                }
            }
        });
        //
        WindowManager.LayoutParams lp = updateDialog.getWindow().getAttributes();
        lp.width = UnitExchangeUtil.dip2px(context, 260);
        lp.height = UnitExchangeUtil.dip2px(context, 295);
        lp.gravity = Gravity.CENTER;

        updateDialog.setContentView(view, lp);
        updateDialog.setCanceledOnTouchOutside(false);
        updateDialog.show();
        //TODO 每次显示时重置当前状态，避免出现无法更新的情况
        currentStatus = false;
    }

    public void updateProgress(int progress, String rateText){
        if (progressBar != null && rate != null) {
            progressBar.setProgress(progress);
            rate.setText(rateText);
        }
    }
}
