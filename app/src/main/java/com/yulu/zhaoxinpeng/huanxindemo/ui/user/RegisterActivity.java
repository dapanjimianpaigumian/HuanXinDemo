package com.yulu.zhaoxinpeng.huanxindemo.ui.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.yulu.zhaoxinpeng.huanxindemo.R;
import com.yulu.zhaoxinpeng.huanxindemo.commons.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.username)
    EditText mETusername;
    @BindView(R.id.password)
    EditText mETpassword;
    @BindView(R.id.confirm_password)
    EditText mETconfirmPassword;
    @BindView(R.id.register_btn)
    Button mBtnregister;
    private Unbinder bind;
    private ActivityUtils mActivityUtils;
    private String confirmpassword;
    private String password;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bind = ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        initView();
    }

    private void initView() {
        mETusername.addTextChangedListener(mTextWatcher);
        mETpassword.addTextChangedListener(mTextWatcher);
        mETconfirmPassword.addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            username = mETusername.getText().toString();
            password = mETpassword.getText().toString();
            confirmpassword = mETconfirmPassword.getText().toString();

            if (TextUtils.isEmpty(username)) {
                mActivityUtils.showToast(R.string.User_name_cannot_be_empty);
                return;
            } else if (TextUtils.isEmpty(password)) {
                mActivityUtils.showToast(R.string.Password_cannot_be_empty);
                return;
            } else if (TextUtils.isEmpty(confirmpassword)) {
                mActivityUtils.showToast(R.string.Confirm_password_cannot_be_empty);
                return;
            } else if (!password.equals(confirmpassword)) {
                mActivityUtils.showToast(R.string.Two_input_password);
                return;
            }

            boolean canRegister=!TextUtils.isEmpty(username) &&
                    !TextUtils.isEmpty(password) && password.equals(confirmpassword);
            mBtnregister.setEnabled(canRegister);
        }
    };

    @OnClick(R.id.register_btn)
    public void onViewClicked() {
        //执行环信注册的相关内容
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("正在注册.....");
        pd.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(username, password);//同步方法
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            // TODO: 2017/5/3 0003 保存用户登录状态
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            //跳转到登录页面
                            finish();
                        }
                    });

                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            int errorCode = e.getErrorCode();
                            if (errorCode == EMError.NETWORK_ERROR) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
