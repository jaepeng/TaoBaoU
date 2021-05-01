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
import com.example.taobaou.utils.MD5Utils;
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
    public EditText edtOldPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psw);
        Button btnConfimr = findViewById(R.id.btn_chang_psw_act_change);
        edtNewPsw = findViewById(R.id.edt_change_pwd_new_psw);
        edtRepeatPsw = findViewById(R.id.edt_change_psd_repate_password);
        edtOldPsw = findViewById(R.id.edt_old_psw);
        btnConfimr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChangePwd();
            }
        });

    }

    public void checkChangePwd() {
        String oldpsw="";
        String newpsw = edtNewPsw.getText().toString();
        if (checkUserPassword(newpsw, edtRepeatPsw.getText().toString())) {


            Log.d(TAG, "checkChangePwd: ");

            //如果两个相等
            Api apiService = OtherRetrofitManager.getInstance().getApiService();
            String account = SharedPreferenceManager.getInstance().getString(SpConstans.LAST_USER_ACCOUNT);
            Log.d(TAG, "checkChangePwd: account" + account);
            String oldStr = edtOldPsw.getText().toString();
            if (oldStr.length()<6){
                ToastUtils.showShort("您输入旧密码位数不对!");
                return;
            }else{
                oldpsw= MD5Utils.encode2hex(oldStr);
            }
            Call<Boolean> task = apiService.changePwd(account, oldpsw, newpsw);
            ToastUtils.showShort("请稍后!");
            task.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.code() == 200) {
                        if (response.body() == true) {
                            ToastUtils.showShort("修改成功!");
                            EventBus.getDefault().post(new MessageEvent(MessageCode.CHANGEPWSSUCCESS));
                            finish();
                        }else{
                            ToastUtils.showShort("修改失败");
                        }
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.e(TAG, "onFailure: "+t.getMessage(),t);
                    ToastUtils.showShort("修改失败!");
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