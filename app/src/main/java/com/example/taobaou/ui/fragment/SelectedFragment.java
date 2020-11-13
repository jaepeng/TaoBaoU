package com.example.taobaou.ui.fragment;

import android.view.View;

import com.example.taobaou.R;
import com.example.taobaou.base.BaseFragment;

public class SelectedFragment extends BaseFragment {
    @Override
    protected int getRootViewResid() {
        return R.layout.fragment_selected;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
    }

}
