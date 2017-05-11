package com.yulu.zhaoxinpeng.huanxindemo.ui.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.yulu.zhaoxinpeng.huanxindemo.MyHelper;
import com.yulu.zhaoxinpeng.huanxindemo.R;
import com.yulu.zhaoxinpeng.huanxindemo.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.huanxindemo.ui.SplashActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/5/11.
 */

public class SettingFragment extends Fragment {

    Unbinder unbinder;
    private ActivityUtils mActivityUtils;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mActivityUtils.showToast("退出时遇到错误！");
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivityUtils = new ActivityUtils(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //退出的点击事件
    @OnClick(R.id.setting_btn_loginout)
    public void onViewClicked() {
        final ProgressDialog mProgressDialog = new ProgressDialog(getContext());
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
