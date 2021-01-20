package com.example.taobaou.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.facelibs.activity.RegisterAndRecognizeActivity;
import com.example.taobaou.R;
import com.example.taobaou.base.BaseActivity;
import com.example.taobaou.model.domain.UserInfo;
import com.example.taobaou.utils.SharedPreferenceManager;

import butterknife.BindView;
import butterknife.OnClick;

public class TestActivity extends BaseActivity {
    @BindView(R.id.btn_test_sp)
    public Button btnSetData;
    @BindView(R.id.btn_test_sp_get)
    public Button btnGetData;
    @BindView(R.id.tv_test_show_message)
    public TextView tvShowMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initPresenter() {

    }


    @OnClick(R.id.btn_test_sp)
    public void setData(){
        Intent intent=new Intent(TestActivity.this, RegisterAndRecognizeActivity.class);
        startActivity(intent);
//        UserInfo userInfo=new UserInfo("1234");
//        SharedPreferenceManager.getInstance().setLastUser(userInfo);
//        boolean firstLogin = SharedPreferenceManager.getInstance().isFirstLogin();
    }
    @OnClick(R.id.btn_test_sp_get)
    public void getData(){

        UserInfo lastUser = SharedPreferenceManager.getInstance().getLastUser();
        tvShowMessage.setText(lastUser.toString());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }
}
