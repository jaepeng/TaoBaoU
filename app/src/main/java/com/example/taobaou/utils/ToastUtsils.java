package com.example.taobaou.utils;

import android.widget.Toast;

import com.example.taobaou.base.BaseApplication;

public class ToastUtsils {

    private static Toast sToast;

    public static void showToast(String tips){
        if (sToast==null) {
            sToast = Toast.makeText(BaseApplication.getAppContext(), tips, Toast.LENGTH_SHORT);
        }else{
            sToast.setText(tips);
        }

        sToast.show();
    }
}
