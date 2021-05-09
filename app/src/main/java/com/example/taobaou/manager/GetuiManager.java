package com.example.taobaou.manager;

import android.content.Context;

import com.igexin.sdk.PushManager;


/**
 * GetuiManager
 *
 * @author JesseHu
 * @date 2021/3/31
 */
public class GetuiManager {
    private static class GetuiManagerHolder {
        private static final GetuiManager INSTANCE = new GetuiManager();
    }

    public static GetuiManager getInstance() {
        return GetuiManagerHolder.INSTANCE;
    }

    /**
     * 初始化个推推送
     *
     * @param context
     */
    public void init(Context context) {
        PushManager.getInstance().initialize(context);
    }

    /**
     * 关闭推送
     *
     * @param context
     */
    public void turnOff(Context context) {
        PushManager.getInstance().turnOffPush(context);
    }

    /**
     * 开启推送
     *
     * @param context
     */
    public void turnOn(Context context) {
        PushManager.getInstance().turnOnPush(context);
    }
}
