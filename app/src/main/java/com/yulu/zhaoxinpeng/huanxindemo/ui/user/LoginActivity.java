package com.yulu.zhaoxinpeng.huanxindemo.ui.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.yulu.zhaoxinpeng.huanxindemo.MyHelper;
import com.yulu.zhaoxinpeng.huanxindemo.R;
import com.yulu.zhaoxinpeng.huanxindemo.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.huanxindemo.ui.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity {

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mActivityUtils.showToast("登录失败!");
        }
    };
    @BindView(R.id.username)
    EditText mEtUsername;
    @BindView(R.id.password)
    EditText mEtPassword;
    private Unbinder bind;
    private ActivityUtils mActivityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bind = ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @OnClick({R.id.login_btn_register, R.id.login_btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn_register:
                mActivityUtils.startActivity(RegisterActivity.class);
                break;
            case R.id.login_btn_login:
                String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    mActivityUtils.showToast(R.string.User_name_cannot_be_empty);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mActivityUtils.showToast(R.string.Password_cannot_be_empty);
                    return;
                }

                final ProgressDialog mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("正在登录中~");
                mProgressDialog.show();

                //执行环信登录的相关操作
                EMClient.getInstance().login(username, password, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        mProgressDialog.dismiss();
                        //保存登陆状态到本地配置中
                        MyHelper.setLogin(true);
                        mActivityUtils.startActivity(MainActivity.class);
                        finish();
                    }

                    @Override
                    public void onError(int code, String error) {
                        mProgressDialog.dismiss();
                        Log.e("Error code,error",code + error+"");
                        mHandler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });
                break;
        }
    }
}
