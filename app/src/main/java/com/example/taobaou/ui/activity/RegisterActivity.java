package com.example.taobaou.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.arcsoft.face.ActiveFileInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.enums.RuntimeABI;
import com.example.taobaou.R;
import com.example.taobaou.model.message.MessageCode;
import com.example.taobaou.model.message.MessageEvent;
import com.example.taobaou.ui.activity.face.FaceRegisetrActivity;
import com.example.taobaou.utils.Constants;
import com.example.taobaou.utils.SharedPreferenceManager;
import com.example.taobaou.utils.ToastUtsils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.taobaou.utils.ToastUtsils.showToast;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEdtAccount;
    private EditText mEdtPassword;
    private EditText mEdtRePassword;
    private Map<String, String> mUsermap;
    public static final String TAG="MainActivity";
    //人脸识别使用权限
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    boolean libraryExists = true;
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (SharedPreferenceManager.getInstance().isFirstLogin()){

            activeEngine(null);
        }
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

                    FaceRegisetrActivity.startActivity(RegisterActivity.this,account);
                }

            }
        });
    }
    protected boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }
    public void activeEngine(final View view) {
        if (!libraryExists) {
            showToast(getString(R.string.library_not_found));
            return;
        }
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
            return;
        }
        if (view != null) {
            view.setClickable(false);
        }
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                RuntimeABI runtimeABI = FaceEngine.getRuntimeABI();

                long start = System.currentTimeMillis();
                int activeCode = FaceEngine.activeOnline(RegisterActivity.this, com.example.facelibs.common.Constants.APP_ID, com.example.facelibs.common.Constants.SDK_KEY);
                emitter.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        if (activeCode == ErrorInfo.MOK) {
//                            showToast(getString(R.string.active_success));
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
//                            showToast(getString(R.string.already_activated));
                        } else {
//                            showToast(getString(R.string.active_failed, activeCode));
                        }

                        if (view != null) {
                            view.setClickable(true);
                        }
                        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
                        int res = FaceEngine.getActiveFileInfo(RegisterActivity.this, activeFileInfo);
                        if (res == ErrorInfo.MOK) {
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        if (view != null) {
                            view.setClickable(true);
                        }
                    }

                    @Override
                    public void onComplete() {

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