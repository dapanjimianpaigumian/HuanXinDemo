package com.yulu.zhaoxinpeng.huanxindemo.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.yulu.zhaoxinpeng.huanxindemo.MyHelper;
import com.yulu.zhaoxinpeng.huanxindemo.R;
import com.yulu.zhaoxinpeng.huanxindemo.commons.ActivityUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mActivityUtils.showToast("退出时遇到错误！");
        }
    };
    private Unbinder bind;
    private ActivityUtils mActivityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @OnClick(R.id.main_btn_loginout)
    public void onViewClicked() {
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在退出~");
        mProgressDialog.show();

        //执行环信的退出登录操作
        //true表示退出登录后不能够接收到消息，
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                mProgressDialog.dismiss();
                MyHelper.setLogin(false);
                mActivityUtils.startActivity(SplashActivity.class);
                finish();
            }

            @Override
            public void onError(int code, String error) {
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }
}
