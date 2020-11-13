package com.example.taobaou.ui.activity;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.taobaou.R;
import com.example.taobaou.base.BaseActivity;
import com.example.taobaou.utils.LogUtils;

import butterknife.BindView;

public class TestActivity extends BaseActivity {
    @BindView(R.id.test_navigation_bar)
    public RadioGroup navigationBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initPresenter() {

    }


    protected void initEvent() {
        navigationBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtils.d(TestActivity.class,"checked---->"+checkedId);
                switch (checkedId) {
                    case R.id.test_home:LogUtils.d(TestActivity.class,"This is home");
                        break;
                        case R.id.test_red_packet:LogUtils.d(TestActivity.class,"This is redpacket");
                        break;
                        case R.id.test_search:LogUtils.d(TestActivity.class,"This is search");
                        break;
                        case R.id.test_selected:LogUtils.d(TestActivity.class,"This is selected");
                        break;

                }
            }
        });
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }
}
