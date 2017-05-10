package com.yulu.zhaoxinpeng.huanxindemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;
import com.yulu.zhaoxinpeng.huanxindemo.MyHelper;
import com.yulu.zhaoxinpeng.huanxindemo.R;
import com.yulu.zhaoxinpeng.huanxindemo.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.huanxindemo.ui.user.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 开屏界面
 */
public class SplashActivity extends AppCompatActivity {

    private Intent intent;
    private ActivityUtils mActivityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mActivityUtils = new ActivityUtils(this);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (MyHelper.getLogin()) {
                    //这两个方法是为了保证进入主页面后本地会话和群组都加载完毕
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();

                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    finish();
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    finish();
                }

                //注册环信连接状态监听
                EMClient.getInstance().addConnectionListener(new MyConnectionListener());
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    //实现环信连接状态监听
    private class MyConnectionListener implements EMConnectionListener {

        //已连接时
        @Override
        public void onConnected() {

        }

        //未连接时
        @Override
        public void onDisconnected(final int errorCode) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (errorCode == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                        mActivityUtils.showToast("显示帐号已经被移除");
                    } else if (errorCode == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        mActivityUtils.showToast("显示帐号在其他设备登录");
                        // TODO: 2017/5/10 0010 当前账号踢出 ，跳入欢迎页
                    } else {
                        if (NetUtils.hasNetwork(SplashActivity.this)) {
                            //连接不到聊天服务器
                            mActivityUtils.showToast("连接不到聊天服务器");
                        } else {
                            //当前网络不可用，请检查网络设置
                            mActivityUtils.showToast("当前网络不可用，请检查网络设置");
                        }

                    }
                }
            });
        }
    }
}
