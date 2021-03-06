package com.example.taobaou.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.baidusearch.utils.GsonUtils;
import com.example.taobaou.base.BaseApplication;
import com.example.taobaou.model.domain.UserInfo;

public class SharedPreferenceManager {
    private static SharedPreferenceManager instance = new SharedPreferenceManager();
    private String spName = SpConstans.COMMON_DATA;
    private static SharedPreferences sp;
    SharedPreferences.Editor edit;

    public SharedPreferenceManager() {
        sp = BaseApplication.getAppContext().getSharedPreferences(spName, Context.MODE_PRIVATE);

    }

    public static SharedPreferenceManager getInstance() {
        return instance;
    }

    public static SharedPreferences.Editor getSpEdit() {
        return sp.edit();
    }


    public void setDataApply(String key, String value) {
        getSpEdit().putString(key, value).apply();
    }

    public void setDataCommit(String key, String value) {
        getSpEdit().putString(key,value).commit();
    }

    public String getData(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    /**
     * 设置当前最后一个已登录用户是谁
     * @param userInfo
     */

    public void setLastUser(UserInfo userInfo) {
        String userInfoString = GsonUtils.toJson(userInfo);
        getSpEdit().putString(SpConstans.LAST_USER_ACCOUNT,userInfoString).apply();
    }
    /**
     * 获得当前最后一个已登录用户是谁
     * @param
     */
    public UserInfo getLastUser() {
        String lastUserString = sp.getString(SpConstans.LAST_USER_ACCOUNT,"");
        UserInfo userInfo = GsonUtils.fromJson(lastUserString, UserInfo.class);
        return userInfo;

    }
    /**
     * 判断是否是第一次登录
     */
    public boolean isFirstLogin(){
        boolean isFirstLogin = sp.getBoolean(SpConstans.FIRST_LOGIN, true);
        if (isFirstLogin){
            getSpEdit().putBoolean(SpConstans.FIRST_LOGIN,false).apply();
            return true;
        }
        return false;
    }
}
