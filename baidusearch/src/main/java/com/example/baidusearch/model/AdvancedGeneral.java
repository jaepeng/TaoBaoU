package com.example.baidusearch.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.baidusearch.utils.AuthService;
import com.example.baidusearch.utils.Base64Util;
import com.example.baidusearch.utils.HttpUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;

public class AdvancedGeneral {
    public static final String TAG="AdvancedGeneral_jae";
    private static String sAccessToken;
    private static SharedPreferences setting;

    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public static String advancedGeneral(String path,Context context) {
        setting = context.getSharedPreferences("BaiduAccessToken", Context.MODE_PRIVATE);
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general";
        try {
            // 本地文件路径
            String filePath = path;
            File file=new File(path);
            byte[] imgData = File2byte(file);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;
//            if (setting.getBoolean(SpConstans.SP_TODAY_FIRST_LAUNCH,false)) {
//                /*
//                1.判断日期是否改变：确认是不是一天，是同一天则不进行对token的请求
//                2.不是同一天则判断token是否改变
//                    2.1如果token改变则将存出的token值更新
//                    2.2如果token没有改变，则不去更新
//                3.将自己设置为不是第一次启动
//                4.如果是今天第一次启动，去判断token改变了没,然后将自己设置为不是第一次启动
//                 */
//                //todo:判断是否是同一天启动
//
//                String token = setting.getString(SpConstans.SP_ACCESSONTOKEN, null);
//                //查看存储的token和刚请求到的token是否相同
//                if (TextUtils.equals(token, AuthService.getAuth())) {
//
//                }else{
//                    //todo:不同的话，要去修改token值
//
//                }
//               //不是今天第一次启动了
//                SharedPreferences.Editor edit = setting.edit();
//                edit.putBoolean(SpConstans.SP_TODAY_FIRST_LAUNCH,false);
//            }


            sAccessToken = AuthService.getAuth();
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
//            if (TextUtils.isEmpty(setting.getString(SpConstans.SP_ACCESSONTOKEN,null))){
//                SharedPreferences.Editor editor = setting.edit();
//                editor.putString("AccessToken", sAccessToken);
//                editor.commit();
//            }

//            sAccessToken= setting.getString(SpConstans.SP_ACCESSONTOKEN,null);


            Log.d(TAG, "advancedGeneral:Token "+sAccessToken);

            String result = HttpUtil.post(url, sAccessToken, param);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            sAccessToken = AuthService.getAuth();
            SharedPreferences.Editor editor = setting.edit();
            editor.putString("AccessToken", sAccessToken);
            editor.commit();
        }
        return null;
    }
    public static byte[] File2byte(File tradeFile){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }

}