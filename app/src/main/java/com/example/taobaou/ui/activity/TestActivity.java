package com.example.taobaou.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.arcsoft.face.ActiveFileInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.enums.RuntimeABI;
import com.example.facelibs.common.Constants;
import com.example.taobaou.R;
import com.example.taobaou.base.BaseActivity;
import com.example.taobaou.model.domain.User;
import com.example.taobaou.utils.SharedPreferenceManager;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.taobaou.utils.ToastUtsils.showToast;

public class TestActivity extends BaseActivity {
    private static final String TAG="TestActivity_jae";
    @BindView(R.id.btn_test_sp)
    public Button btnSetData;
    @BindView(R.id.btn_test_sp_get)
    public Button btnGetData;
    @BindView(R.id.tv_test_show_message)
    public TextView tvShowMessage;

    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    boolean libraryExists = true;
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initPresenter() {

    }


    @OnClick(R.id.btn_test_sp)
    public void setData(){
       new Thread(new Runnable() {
           @Override
           public void run() {
//               OkHttpClient client = new OkHttpClient();
//               MediaType mediaType=new MediaType();
//               RequestBody requestBody=RequestBody.create()
//               Request request = new Request.Builder().url("http://192.168.3.52:8080/login")
//                       .post()
//                       .build();
//               try {
//                   Response response = client.newCall(request).execute();//发送请求
//                   String result = response.body().string();
//                   Log.d(TAG, "result: "+result);
//
//               } catch (IOException e) {
//                   e.printStackTrace();
//               }
           }
       }).start();
//        UserInfo userInfo=new UserInfo("1234");
//        SharedPreferenceManager.getInstance().setLastUser(userInfo);
//        boolean firstLogin = SharedPreferenceManager.getInstance().isFirstLogin();
    }
    @OnClick(R.id.btn_test_sp_get)
    public void getData(){

        User lastUser = SharedPreferenceManager.getInstance().getLastUser();
        tvShowMessage.setText(lastUser.toString());
    }

    @Override
    protected void initView() {


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
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
                Log.i(TAG, "subscribe: getRuntimeABI() " + runtimeABI);

                long start = System.currentTimeMillis();
                int activeCode = FaceEngine.activeOnline(TestActivity.this, Constants.APP_ID, Constants.SDK_KEY);
                Log.i(TAG, "subscribe cost: " + (System.currentTimeMillis() - start));
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
                            showToast(getString(R.string.active_success));
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                            showToast(getString(R.string.already_activated));
                        } else {
                            showToast(getString(R.string.active_failed, activeCode));
                        }

                        if (view != null) {
                            view.setClickable(true);
                        }
                        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
                        int res = FaceEngine.getActiveFileInfo(TestActivity.this, activeFileInfo);
                        if (res == ErrorInfo.MOK) {
                            Log.i(TAG, activeFileInfo.toString());
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

}
