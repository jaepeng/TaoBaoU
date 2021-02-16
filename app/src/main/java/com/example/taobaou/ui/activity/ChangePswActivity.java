package com.example.taobaou.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ToastUtils;
import com.example.taobaou.R;
import com.example.taobaou.model.Api;
import com.example.taobaou.model.message.MessageCode;
import com.example.taobaou.model.message.MessageEvent;
import com.example.taobaou.utils.OtherRetrofitManager;
import com.example.taobaou.utils.SharedPreferenceManager;
import com.example.taobaou.utils.SpConstans;
import com.example.taobaou.utils.ToastUtsils;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePswActivity extends AppCompatActivity {
    private static final String TAG = "ChangePswActivity";
    public EditText edtNewPsw;
    public EditText edtRepeatPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psw);
        Button btnConfimr = findViewById(R.id.btn_chang_psw_act_change);
        edtNewPsw = findViewById(R.id.edt_change_pwd_new_psw);
        edtRepeatPsw = findViewById(R.id.edt_change_psd_repate_password);
        btnConfimr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChangePwd();
            }
        });

    }

    public void checkChangePwd() {
        if (checkUserPassword(edtNewPsw.getText().toString(), edtRepeatPsw.getText().toString())) {


            Log.d(TAG, "checkChangePwd: ");

            //如果两个相等
            Api apiService = OtherRetrofitManager.getInstance().getApiService();
            String account = SharedPreferenceManager.getInstance().getString(SpConstans.LAST_USER_ACCOUNT);
            Log.d(TAG, "checkChangePwd: account" + account);
            Call<Boolean> task = apiService.changePwd(account, edtNewPsw.getText().toString());
            ToastUtils.showShort("请稍后!");
            task.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.code() == 200) {
                        if (response.body() == true) {
                            ToastUtils.showShort("修改成功!");
                            EventBus.getDefault().post(new MessageEvent(MessageCode.CHANGEPWSSUCCESS));
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {

                }
            });

            finish();

        }

    }

    private boolean checkUserPassword(String password, String repassword) {
        if (password.trim().contains(" ")) {
            ToastUtsils.showToast("密码中不能包含空格");
            return false;
        }
        if (password.trim().length() < 6) {
            ToastUtsils.showToast("密码长度不允许小于6");
            return false;
        }
        if (!TextUtils.equals(password, repassword)) {
            ToastUtsils.showToast("两次密码不一致");
            return false;
        }
        return true;
    }
}