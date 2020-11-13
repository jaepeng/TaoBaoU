package com.example.taobaou.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder mbind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mbind = ButterKnife.bind(this);
        initView();
        initEvent();
        initPresenter();

    }

    protected abstract void initPresenter();

    /**
     * 需要复写
     */

    protected void initEvent() {
    }

    protected abstract void initView();

    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mbind!=null) {
            mbind.unbind();
        }
        this.release();
    }

    /**
     *
     */
    protected void release() {

    }
}
