package com.yulu.zhaoxinpeng.huanxindemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yulu.zhaoxinpeng.huanxindemo.R;
import com.yulu.zhaoxinpeng.huanxindemo.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.huanxindemo.ui.user.LoginActivity;
import com.yulu.zhaoxinpeng.huanxindemo.ui.user.RegisterActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 欢迎页
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ActivityUtils mActivityUtils = new ActivityUtils(this);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO: 2017/5/3 保存用户登录状态
                boolean isLogin = true;
                if (isLogin) {
                    mActivityUtils.startActivity(LoginActivity.class);
                    finish();
                } else {
                    mActivityUtils.startActivity(RegisterActivity.class);
                    finish();
                }
            }
        }, 1000);
    }
}
