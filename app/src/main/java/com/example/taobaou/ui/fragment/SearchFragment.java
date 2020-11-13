package com.example.taobaou.ui.fragment;

import android.view.View;

import com.example.taobaou.R;
import com.example.taobaou.base.BaseFragment;

public class SearchFragment extends BaseFragment {
    @Override
    protected int getRootViewResid() {
        return R.layout.fragment_serarch;
    }
    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
    }
}
