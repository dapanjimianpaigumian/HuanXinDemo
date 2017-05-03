package com.yulu.zhaoxinpeng.huanxindemo.ui.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yulu.zhaoxinpeng.huanxindemo.R;
import com.yulu.zhaoxinpeng.huanxindemo.commons.ActivityUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity {

    private Unbinder bind;
    private ActivityUtils mActivityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bind = ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
    }

    @OnClick(R.id.login_btn_register)
    public void onViewClicked() {
        mActivityUtils.startActivity(RegisterActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
