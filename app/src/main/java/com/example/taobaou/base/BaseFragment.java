package com.example.taobaou.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taobaou.R;
import com.example.taobaou.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private State currentState = State.NONE;
    private View successView;
    private View loadingView;
    private View errorView;
    private View emptyView;

    public enum State {
        NONE, LOADING, SUCCESS, ERROR, EMPTY
    }
    private Unbinder mbind;
    private FrameLayout mBaseContainer;

    @OnClick(R.id.network_error_tips)
    public  void retry(){
        //点击重新加载内容
        LogUtils.d(this,"on try....");
        onRetryClick();
    }

    protected void onRetryClick() {

        LogUtils.d(this,"onRetryClick");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = loadRootView(inflater,container);
        mBaseContainer = rootView.findViewById(R.id.base_container);
        loadStatesView(inflater,container);
        mbind = ButterKnife.bind(this, rootView);
        initView(rootView);
        initListener();
        initPresenter();

        loadData();
        return rootView;

    }

    /**
     * 如果子类需要去设置相关的监听事件，进行复写
     */
    protected void initListener() {
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout, container, false);
    }

    /**
     * 加载各种状态的view
     * @param inflater
     * @param container
     */
    private void loadStatesView(LayoutInflater inflater, ViewGroup container) {

        //成功的view
        successView = loadSuccessView(inflater, container);
        mBaseContainer.addView(successView);
        //loadingView

        loadingView = loadLoadingView(inflater, container);
        mBaseContainer.addView(loadingView);

        //错误页面
        errorView = laodErrorView(inflater, container);
        mBaseContainer.addView(errorView);

        //空页面
        emptyView = laodEmptyView(inflater, container);
        mBaseContainer.addView(emptyView);
        setUpState(State.NONE);
    }

    protected View laodErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error_layout,container,false);

    }
    protected View laodEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty_layout,container,false);

    }


    /**
     * 加载loading界面
     * @param inflater
     * @param container
     * @return
     */

    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading_layout,container,false);
    }

    protected  View loadSuccessView(LayoutInflater inflater, ViewGroup container){
        int resId=getRootViewResid();
        return   inflater.inflate(resId,container,false);
    }

    /**
     * 子类要通过该方法实现设置界面状态
     * @param state
     */
    public void  setUpState(State state){
        this.currentState=state;

        successView.setVisibility(currentState==State.SUCCESS?View.VISIBLE:View.GONE);
        loadingView.setVisibility(currentState==State.LOADING? View.VISIBLE:View.GONE);
        errorView.setVisibility(currentState==State.ERROR? View.VISIBLE:View.GONE);
        emptyView.setVisibility(currentState==State.EMPTY? View.VISIBLE:View.GONE);
    }
    protected  void initView(View rootView){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mbind!=null) {
            mbind.unbind();

        }
        releas();


    }

    protected void releas() {
        //释放资源
    }

    protected void initPresenter() {
        //创建Presenter
    }

    protected void loadData() {
        //加载数据
    }



    protected abstract int getRootViewResid();
}
