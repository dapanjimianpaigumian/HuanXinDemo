package com.yulu.zhaoxinpeng.huanxindemo.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.yulu.zhaoxinpeng.huanxindemo.R;
import com.yulu.zhaoxinpeng.huanxindemo.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.huanxindemo.ui.user.ChatActivity;
import com.yulu.zhaoxinpeng.huanxindemo.ui.user.SettingFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_vp)
    ViewPager mViewPager;
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
    private EaseContactListFragment mContactListFragment;
    private List<String> mContacts = new ArrayList<>();
    private Unbinder bind;
    private ActivityUtils mActivityUtils;
    @BindView(R.id.main_edit)EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        bind = ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
        mViewPager.setAdapter(mFragmentStatePagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // select a new tab and animate the selection.
                if (position != mBottomBar.getCurrentTabPosition()) {
                    mBottomBar.selectTabAtPosition(position, true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(0);
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_chat:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.tab_contact_list:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.tab_setting:
                        mViewPager.setCurrentItem(2);
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    private FragmentStatePagerAdapter mFragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                //会话
                case 0:
                    EaseConversationListFragment conversationListFragment = new EaseConversationListFragment();
                    conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
                        @Override
                        public void onListItemClicked(EMConversation conversation) {
                            //点击某个人，跳转到聊天页面
                            ChatActivity.open(MainActivity.this, conversation.conversationId());
                        }
                    });
                    return conversationListFragment;
                //通讯录
                case 1:
                    mContactListFragment = new EaseContactListFragment();
                    //需要设置联系人列表才能启动fragment
                    asyncGetContactsFromServer();
                    //设置item点击事件
                    mContactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
                        @Override
                        public void onListItemClicked(EaseUser user) {
                            //跳转到聊天页面
                            ChatActivity.open(MainActivity.this, user.getUsername());
                        }
                    });

                    return mContactListFragment;
                //设置
                case 2:
                    return new SettingFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    @OnClick(R.id.main_start_chat)
    public void onclick(){
        ChatActivity.open(MainActivity.this,editText.getText().toString());
    }


    //异步获取联系人
    private void asyncGetContactsFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //从环信服务器获取到所有联系人
                    mContacts = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //刷新联系人
                    refreshContacts();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //刷新联系人
    private void refreshContacts() {
        HashMap<String, EaseUser> hashmap = new HashMap<>();
        for (String hxId : mContacts) {
            EaseUser user = new EaseUser(hxId);
            hashmap.put(hxId, user);
        }
        mContactListFragment.setContactsMap(hashmap);
        mContactListFragment.refresh();
    }

}
