package com.quanjiakan.activity.common.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.quanjiakan.activity.common.login.SigninActivity_mvp;
import com.quanjiakan.util.notification.NotificationUtil;
import com.quanjiakan.watch.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.logout)
    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        ButterKnife.bind(this);

        NotificationUtil.commonNotificationAlertOnce(this,"123","456",null);
    }

    @OnClick(R.id.logout)
    public void onClick() {
        //TODO

        onLogout();

        Intent intent = new Intent(this, SigninActivity_mvp.class);
        startActivity(intent);
        finish();
    }


    public void onLogout(){

    }
}
