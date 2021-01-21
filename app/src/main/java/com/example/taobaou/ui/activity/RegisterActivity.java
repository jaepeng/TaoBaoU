package com.example.taobaou.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facelibs.activity.RegisterAndRecognizeActivity;
import com.example.taobaou.R;
import com.example.taobaou.model.message.MessageCode;
import com.example.taobaou.model.message.MessageEvent;
import com.example.taobaou.utils.Constants;
import com.example.taobaou.utils.ToastUtsils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.util.LinkedHashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEdtAccount;
    private EditText mEdtPassword;
    private EditText mEdtRePassword;
    private Map<String, String> mUsermap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button btnRegister=findViewById(R.id.btn_register_register);
        Button btnFaceRegister=findViewById(R.id.btn_register_face_register);
        mEdtAccount = findViewById(R.id.edt_register_account);
        mEdtPassword = findViewById(R.id.edt_register_password);
        mEdtRePassword = findViewById(R.id.edt__register_repassword);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registeraccount = mEdtAccount.getText().toString();
                if (checkUserAccount(registeraccount)){
                    if (checkUserPassword(mEdtPassword.getText().toString(),mEdtRePassword.getText().toString())) {
                        mUsermap = new LinkedHashMap<>();
                        mUsermap.put(registeraccount,mEdtPassword.getText().toString());
                        setMap(RegisterActivity.this,mUsermap);
                        EventBus.getDefault().post(new MessageEvent(MessageCode.REGISTERUSERACCOUNT,mEdtAccount.getText().toString()));
                        finish();
                    }

                }


            }
        });
        btnFaceRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = mEdtAccount.getText().toString();
                if (checkUserAccount(account)){

                    RegisterAndRecognizeActivity.startActivity(RegisterActivity.this,account);
                }

            }
        });
    }

    private boolean checkUserPassword(String password, String repassword) {
        if (password.trim().contains(" ")){
            ToastUtsils.showToast("密码中不能包含空格");
            return false;
        }
        if (password.trim().length()<6){
            ToastUtsils.showToast("密码长度不允许小于6");
            return false;
        }
        if (!TextUtils.equals(password,repassword)) {
            ToastUtsils.showToast("两次密码不一致");
            return false;
        }
        return true;
    }

    private boolean checkUserAccount(String registeraccount) {
        if (TextUtils.isEmpty(registeraccount)){
            ToastUtsils.showToast("用户名不能为空");
            return false;
        }
        if (registeraccount.length()<3){
            ToastUtsils.showToast("账户长度不允许小于3");
            return false;
        }
        if (getMap(this).containsKey(registeraccount)){
            ToastUtsils.showToast("该账户已经被注册了!");
            return false;
        }

        return true;
    }

    public static void setMap(Context context, Map<String, String> map) {
        if (map != null) {
            JSONStringer jsonStringer = new JSONStringer();
            try {
                jsonStringer.array();
                for (String string : map.keySet()) {
                    jsonStringer.object();
                    jsonStringer.key("useraccount");
                    jsonStringer.value(string);
                    jsonStringer.key("userpassword");
                    jsonStringer.value(map.get(string));
                    jsonStringer.endObject();
                }
                jsonStringer.endArray();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences sp = context.getSharedPreferences(Constants.SP_KEY_USER_MAP, Context.MODE_PRIVATE);
            sp.edit().putString(Constants.SP_KEY_LOGIN_ACCOUNT_MAP, jsonStringer.toString()).apply();
        }
    }
    public static Map<String,String> getMap(Context context) {
        Map<String, String> examMap = new LinkedHashMap<>();
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
}