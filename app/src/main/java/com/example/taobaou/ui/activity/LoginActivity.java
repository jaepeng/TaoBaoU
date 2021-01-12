package com.example.taobaou.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taobaou.R;
import com.example.taobaou.model.message.MessageCode;
import com.example.taobaou.model.message.MessageEvent;
import com.example.taobaou.utils.Constants;
import com.example.taobaou.utils.ToastUtsils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private String mAccount;
    private String mPassword;
    private Button mBtnLogin;
    private EditText mEdtAccount;
    private EditText mEdtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mBtnLogin = findViewById(R.id.btn_login_act_login);
        mEdtAccount = findViewById(R.id.edt_login_account);
        mEdtPassword = findViewById(R.id.edt__login_password);
        mBtnLogin.setOnClickListener(this);
    }

    private void loginByPassword(String account, String password) {
        Map<String, String> map = getMap(this);
        if (map.containsKey(account)){
            if (map.get(account).equals(password)){
              //跳转到MyInfoFragment界面
              //通知Fragment将相关控件显示,然后finish该界面也可以
                //todo:记录最后一次登录的账户:之后都显示它的内容

                EventBus.getDefault().post(new MessageEvent(MessageCode.LOGINUSERACCOUNT,account));
                finish();


            }else{
                ToastUtsils.showToast("密码不正确!");
            }
        }else {
            ToastUtsils.showToast("账号不正确!");
        }
    }
//提取
    public static Map<String,String> getMap(Context context) {
        Map<String, String> examMap = new HashMap<>();
        SharedPreferences sp = context.getSharedPreferences(Constants.SP_KEY_USER_MAP, Context.MODE_PRIVATE);
        String map = sp.getString(Constants.SP_KEY_LOGIN_ACCOUNT_MAP, "");
        if (map.length() > 0) {
            JSONTokener jsonTokener = new JSONTokener(map);
            try {
                JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    examMap.put(jsonObject.getString("useraccount"), jsonObject.getString("userpassword"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return examMap;
    }

    @Override
    public void onClick(View v) {
        mAccount = mEdtAccount.getText().toString();
        mPassword = mEdtPassword.getText().toString();
            loginByPassword(mAccount, mPassword);

    }
}