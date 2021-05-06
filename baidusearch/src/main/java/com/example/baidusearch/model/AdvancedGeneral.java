package com.example.baidusearch.model;

import android.content.Context;
import android.content.SharedPreferences;

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

    public static String advancedGeneral(String path,Context context) {
        setting = context.getSharedPreferences("BaiduAccessToken", Context.MODE_PRIVATE);
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general";
        try {
            // 文件路径
            String filePath = path;
            File file=new File(path);
            byte[] imgData = File2byte(file);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;
            sAccessToken = AuthService.getAuth();
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