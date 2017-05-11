package com.yulu.zhaoxinpeng.huanxindemo.ui.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.yulu.zhaoxinpeng.huanxindemo.R;

public class ChatActivity extends AppCompatActivity {

    //对外提供方法，方便外部类打开此Activity
    public static void open(Context context,String chatId){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_SINGLE);//默认单聊
        intent.putExtra(EaseConstant.EXTRA_USER_ID,chatId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        int chatType = getIntent().getIntExtra(EaseConstant.EXTRA_CHAT_TYPE, 0);
        String chatId = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
        //添加聊天的Fragment
        //new出EaseChatFragment或其子类的实例
        EaseChatFragment mChatFragment = new EaseChatFragment();
        //传入参数
        Bundle bundle = new Bundle();
        //聊天的类型。。。。单人（一对一），群聊，聊天组
        bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE,chatType);
        //具体的聊天对象
        bundle.putString(EaseConstant.EXTRA_USER_ID,chatId);
        mChatFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout_container,mChatFragment)
                .commit();
    }
}
