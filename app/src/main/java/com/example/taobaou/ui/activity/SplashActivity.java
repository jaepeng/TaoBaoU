package com.example.taobaou.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.taobaou.R;
import com.example.taobaou.base.BaseActivity;
import com.example.taobaou.utils.RxTimer;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity {
    int timeWait =5;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    private RxTimer mRxTimer;
    private Handler mHandler=new Handler();
    private Runnable timeRunnable=new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this,1000);
            if (timeWait>=0) {
                tvSkip.setText("跳过(" + timeWait-- + ")");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvSkip.setText("跳过( "+(timeWait)+" )");
        mRxTimer = new RxTimer.Builder().setTime(timeWait).setTimeUnit(TimeUnit.SECONDS).build();
        mRxTimer.startTimer();
        mRxTimer.setOnTimeCome(new RxTimer.OnTimeCome() {
            @Override
            public void onTimeCome() {
                goMainActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.post(timeRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRxTimer!=null){
            mRxTimer.closeTimer();
            mRxTimer=null;

        }
        mHandler.removeCallbacks(timeRunnable);

    }

    @OnClick(R.id.tv_skip)
    public void goMain(){
        goMainActivity();
    }
    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    public void goMainActivity(){
        if (mRxTimer!=null){
            mRxTimer.closeTimer();
            mRxTimer=null;
        }
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}