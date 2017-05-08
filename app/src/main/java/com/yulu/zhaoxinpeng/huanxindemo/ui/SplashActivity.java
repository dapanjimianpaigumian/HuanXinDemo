package com.yulu.zhaoxinpeng.huanxindemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hyphenate.chat.EMClient;
import com.yulu.zhaoxinpeng.huanxindemo.MyHelper;
import com.yulu.zhaoxinpeng.huanxindemo.R;
import com.yulu.zhaoxinpeng.huanxindemo.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.huanxindemo.ui.user.LoginActivity;
import com.yulu.zhaoxinpeng.huanxindemo.ui.user.RegisterActivity;

import java.util.Timer;
import java.util.TimerTask;
/**
 * 开屏界面
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
                if (MyHelper.getLogin()) {
                    //这两个方法是为了保证进入主页面后本地会话和群组都加载完毕
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();

                    mActivityUtils.startActivity(MainActivity.class);
                    finish();
                } else {
                    mActivityUtils.startActivity(LoginActivity.class);
                    finish();
                }
            }
        }, 1000);
    }
}
