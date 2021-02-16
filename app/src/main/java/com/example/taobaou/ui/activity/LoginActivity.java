package com.example.taobaou.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taobaou.R;
import com.example.taobaou.model.Api;
import com.example.taobaou.model.domain.User;
import com.example.taobaou.model.message.MessageCode;
import com.example.taobaou.model.message.MessageEvent;
import com.example.taobaou.ui.activity.face.FaceRegisetrActivity;
import com.example.taobaou.utils.Constants;
import com.example.taobaou.utils.OtherRetrofitManager;
import com.example.taobaou.utils.SharedPreferenceManager;
import com.example.taobaou.utils.SpConstans;
import com.example.taobaou.utils.ToastUtsils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private String mAccount;
    private String mPassword;
    private Button mBtnLogin;
    private EditText mEdtAccount;
    private EditText mEdtPassword;
    private Api mApi;
    private Button mBtnFaceLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mBtnLogin = findViewById(R.id.btn_login_act_login);
        mEdtAccount = findViewById(R.id.edt_login_account);
        mEdtPassword = findViewById(R.id.edt__login_password);
        mBtnFaceLogin = findViewById(R.id.btn_login_act_face_login);
        mBtnLogin.setOnClickListener(this);
        mBtnFaceLogin.setOnClickListener(this);
        mApi= OtherRetrofitManager.getInstance().getApiService();
    }

    private void loginByPassword(String account, String password) {

        Call<User> task = mApi.login(account, password);
        task.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code()==200){
                    ToastUtsils.showToast("登录成功");
                    if (response.body()!=null){
                        SharedPreferenceManager.getInstance().putValue(SpConstans.LAST_USER_ACCOUNT,response.body().getAccount());
                        EventBus.getDefault().post(new MessageEvent(MessageCode.LOGINUSERACCOUNT,account));
                        finish();
                    }else{
                        ToastUtsils.showToast("登录失败");
                    }

                }else{
                    ToastUtsils.showToast("登录失败");
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ToastUtsils.showToast("登录失败");
            }
        });

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
        if (v.getId()==R.id.btn_login_act_login){
            mAccount = mEdtAccount.getText().toString();
            mPassword = mEdtPassword.getText().toString();
            loginByPassword(mAccount, mPassword);
        }else if (v.getId()==R.id.btn_login_act_face_login){
            FaceRegisetrActivity.startActivity(this,true);
            finish();
        }


    }
}