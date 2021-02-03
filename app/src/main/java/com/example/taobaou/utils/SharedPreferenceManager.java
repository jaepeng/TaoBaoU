package com.example.taobaou.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.taobaou.base.BaseApplication;

public class SharedPreferenceManager {

    private static SharedPreferences mSp;
    private Context mContext;
    private String mFileName;

    private static class LocalDataUtilHolder {
        public static final SharedPreferenceManager INSTANCE = new SharedPreferenceManager();
    }

    public static SharedPreferenceManager getInstance() {
        return LocalDataUtilHolder.INSTANCE;
    }

    /**
     * 初始化
     *
     *
     */
    public void init() {
        this.mContext = BaseApplication.getAppContext();
        this.mFileName = SpConstans.COMMON_DATA;
    }

    /**
     * 获取SharedPreferences
     */
    private SharedPreferences getPreferences() {
        if (mSp == null) {
            /**
             * 获取SharedPreferences，如果存在则直接获取，如果不存在则创建
             * getSharedPreferences(String name,int mode)
             * name:文件名
             * mode:操作模式
             */
            mSp = mContext.getSharedPreferences(mFileName, Context.MODE_PRIVATE);
        }
        return mSp;
    }

    /**
     * 保存数据
     *
     * @param key   键
     * @param value 值
     */
    public void putValue(String key, Object value) {
        SharedPreferences sp = getPreferences();
        SharedPreferences.Editor edit = sp.edit();
        //判断传入value的类型，然后进行存储
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        }
        edit.apply();
    }

    /**
     * 获取String类型数据，如果数据不存在，返回""
     *
     * @param key 键
     * @return 返回值，默认为""
     */
    public String getString(String key) {
        SharedPreferences sp = getPreferences();
        return sp.getString(key, "");
    }

    /**
     * 获取String类型数据,如果数据不存在，返回自定义默认值
     *
     * @param key      键
     * @param defValue 默认值
     * @return 返回值，默认返回自定义默认值
     */
    public String getString(String key, String defValue) {
        SharedPreferences sp = getPreferences();
        return sp.getString(key, defValue);
    }

    /**
     * 获取int类型数据，如果数据不存在，返回0
     *
     * @param key 键
     * @return 返回值，默认为0
     */
    public int getInt(String key) {
        SharedPreferences sp = getPreferences();
        return sp.getInt(key, 0);
    }

    /**
     * 获取int类型数据,如果数据不存在，返回自定义默认值
     *
     * @param key      键
     * @param defValue 默认值
     * @return 返回值，默认返回自定义默认值
     */
    public int getInt(String key, int defValue) {
        SharedPreferences sp = getPreferences();
        return sp.getInt(key, defValue);
    }

    /**
     * 获取boolean类型数据，如果数据不存在，返回false
     *
     * @param key 键
     * @return 返回值，默认为false
     */
    public boolean getBoolean(String key) {
        SharedPreferences sp = getPreferences();
        return sp.getBoolean(key, false);
    }

    /**
     * 获取boolean类型数据,如果数据不存在，返回自定义默认值
     *
     * @param key      键
     * @param defValue 默认值
     * @return 返回值，默认返回自定义默认值
     */
    public boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sp = getPreferences();
        return sp.getBoolean(key, defValue);
    }

    /**
     * 获取long类型数据，如果数据不存在，返回false
     *
     * @param key 键
     * @return 返回值，默认为0
     */
    public long getLong(String key) {
        SharedPreferences sp = getPreferences();
        return sp.getLong(key, 0L);
    }

    /**
     * 获取long类型数据,如果数据不存在，返回自定义默认值
     *
     * @param key      键
     * @param defValue 默认值
     * @return 返回值，默认返回自定义默认值
     */
    public long getLong(String key, long defValue) {
        SharedPreferences sp = getPreferences();
        return sp.getLong(key, defValue);
    }

    /**
     * 获取float类型数据，如果数据不存在，返回false
     *
     * @param key 键
     * @return 返回值，默认为0
     */
    public float getFloat(String key) {
        SharedPreferences sp = getPreferences();
        return sp.getFloat(key, 0f);
    }

    /**
     * 获取float类型数据,如果数据不存在，返回自定义默认值
     *
     * @param key      键
     * @param defValue 默认值
     * @return 返回值，默认返回自定义默认值
     */
    public float getFloat(String key, float defValue) {
        SharedPreferences sp = getPreferences();
        return sp.getFloat(key, defValue);
    }

    /**
     * 删除指定数据
     *
     * @param key 键
     */
    public void delete(String key) {
        SharedPreferences sp = getPreferences();
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        edit.apply();
    }

    /**
     * 删除所有数据
     */
    public void deleteAll() {
        SharedPreferences sp = getPreferences();
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.apply();
    }
}
