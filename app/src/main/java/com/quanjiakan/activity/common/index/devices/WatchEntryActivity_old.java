package com.quanjiakan.activity.common.index.devices;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.common.index.devices.old.WatchOldEntryFragment;
import com.quanjiakan.activity.common.main.fragment.MainMapFragment;
import com.quanjiakan.activity.common.main.fragment.SettingFragment;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.db.entity.BindWatchInfoEntity;
import com.quanjiakan.db.manager.DaoManager;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/21.
 */

public class WatchEntryActivity_old extends BaseActivity {

    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ibtn_menu)
    ImageButton ibtnMenu;
    @BindView(R.id.menu_text)
    TextView menuText;
    @BindView(R.id.viewContainer)
    FrameLayout viewContainer;

    //**********************************************************


    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;

    private int currentFragment = -1;

    private WatchOldEntryFragment watchOldEntryFragment;

    private long idInDatabase;
    private String IMEI;

    private BindWatchInfoEntity entity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fragment_watch_old);
        ButterKnife.bind(this);

        idInDatabase = getIntent().getLongExtra(IParamsName.PARAMS_COMMON_ID_IN_DB,-1);
        IMEI = getIntent().getStringExtra(IParamsName.PARAMS_DEVICE_ID);
        if(idInDatabase==-1 || IMEI==null){
            CommonDialogHint.getInstance().showHint(this, getString(R.string.error_common_error_activity_parameters), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            return;
        }

        getDataInDataBase();

        initTitleBar();

        setWatchOldFragment(ICommonData.WATCH_OLD_FRAGMENT_WATCH);
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

    public void getDataInDataBase(){
        //TODO 获取对应的实体
        entity = DaoManager.getInstances(this).getDaoSession().getBindWatchInfoEntityDao().load(idInDatabase);
    }


    public void initTitleBar() {
        ibtnBack.setVisibility(View.VISIBLE);
        ibtnBack.setImageResource(R.drawable.back);

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.device_entry_old_title);

        ibtnMenu.setVisibility(View.GONE);
        menuText.setVisibility(View.GONE);
    }

    public void setWatchOldFragment(int type){
        switch (type){
            case ICommonData.WATCH_OLD_FRAGMENT_WATCH:{
                if (currentFragment == ICommonData.WATCH_OLD_FRAGMENT_WATCH) {
                    return;
                }
                startFragmentTransaction();
                hideOther(currentFragment);
                //TODO 
                startFragmentTransaction();
                if (watchOldEntryFragment == null) {
                    watchOldEntryFragment = new WatchOldEntryFragment();
                    transaction.add(R.id.viewContainer, watchOldEntryFragment, ICommonData.WATCH_OLD_FRAGMENT_WATCH_TAG);
                } else {
                    watchOldEntryFragment = (WatchOldEntryFragment) fragmentManager.findFragmentByTag(ICommonData.WATCH_OLD_FRAGMENT_WATCH_TAG);
                    transaction.show(watchOldEntryFragment);
                }
                //TODO
                currentFragment = ICommonData.WATCH_OLD_FRAGMENT_WATCH;

                watchOldEntryFragment.setEntity(entity);

                transaction.commit();
                break;
            }
            case ICommonData.WATCH_OLD_FRAGMENT_MATTRESS:{
                if (currentFragment == ICommonData.WATCH_OLD_FRAGMENT_MATTRESS) {
                    return;
                }
                break;
            }
            case ICommonData.WATCH_OLD_FRAGMENT_STICK:{
                if (currentFragment == ICommonData.WATCH_OLD_FRAGMENT_STICK) {
                    return;
                }
                break;
            }
        }
    }

    public void startFragmentTransaction() {
        //TODO 准备开始Fragment事务
        if (fragmentManager == null) {
            fragmentManager = getFragmentManager();
        }
        transaction = fragmentManager.beginTransaction();
    }

    public void hideOther(int item) {
        switch (item) {
            case ICommonData.WATCH_OLD_FRAGMENT_WATCH: {
                if (watchOldEntryFragment != null) {
                    watchOldEntryFragment = (WatchOldEntryFragment) fragmentManager.findFragmentByTag(ICommonData.WATCH_OLD_FRAGMENT_WATCH_TAG);
                    transaction.hide(watchOldEntryFragment);
                    transaction.commit();
                } else {
                    transaction.commit();
                }
                break;
            }
            case ICommonData.WATCH_OLD_FRAGMENT_MATTRESS: {
//                if (fragmentMessage != null) {
//                    fragmentMessage = (MessageListFragment) fragmentManager.findFragmentByTag(ICommonData.WATCH_OLD_FRAGMENT_MATTRESS_TAG);
//                    transaction.hide(fragmentMessage);
//                    transaction.commit();
//                } else {
//                    transaction.commit();
//                }
                break;
            }
            case ICommonData.WATCH_OLD_FRAGMENT_STICK: {
//                if (fragmentSetting != null) {
//                    fragmentSetting = (SettingFragment) fragmentManager.findFragmentByTag(ICommonData.WATCH_OLD_FRAGMENT_STICK_TAG);
//                    transaction.hide(fragmentSetting);
//                    transaction.commit();
//                } else {
//                    transaction.commit();
//                }
                break;
            }
        }
    }

    @OnClick({R.id.ibtn_back, R.id.tv_title, R.id.ibtn_menu, R.id.menu_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.tv_title:
                break;
            case R.id.ibtn_menu:
                break;
            case R.id.menu_text:
                break;
        }
    }
}
