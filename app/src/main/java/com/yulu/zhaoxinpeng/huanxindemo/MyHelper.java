package com.yulu.zhaoxinpeng.huanxindemo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/5/8.
 * 本地配置仓库
 */

public class MyHelper {
    private static final String NAME=MyHelper.class.getSimpleName();
    private static final String ISLOGIN="islogin";
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;

    private MyHelper(){};

    public static void init(Context context){
        mSharedPreferences=context.getSharedPreferences(NAME,context.MODE_PRIVATE);
        mEditor=mSharedPreferences.edit();
    }

    public static void setLogin(boolean islogin){
        mEditor.putBoolean(ISLOGIN,islogin);
        mEditor.apply();
    }

    public static boolean getLogin(){
        return mSharedPreferences.getBoolean(ISLOGIN,false);
    }
}
