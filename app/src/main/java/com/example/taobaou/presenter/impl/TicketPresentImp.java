package com.example.taobaou.presenter.impl;

import com.example.taobaou.model.Api;
import com.example.taobaou.model.domain.TicketParams;
import com.example.taobaou.model.domain.TicketResult;
import com.example.taobaou.presenter.ITicketPresenter;
import com.example.taobaou.ui.activity.TicketActivity;
import com.example.taobaou.utils.LogUtils;
import com.example.taobaou.utils.RetrofitManager;
import com.example.taobaou.utils.UrlUtils;
import com.example.taobaou.view.ITicketPagerCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TicketPresentImp implements ITicketPresenter {
    private ITicketPagerCallback mViewCallback=null;
    private String mCover=null;
    private TicketResult mTicketResult;

    enum LoadState{
        LOADING,SUCCESS,ERROR, NONE
    }
    private LoadState mCurrentState =LoadState.NONE;

    @Override
    public void getTicket(String title, String url, String cover) {
        this.onTicketLoading();
        this.mCover=cover;

        String targetUrl = UrlUtils.getTicketUrl(url);

        //获取口令
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api=retrofit.create(Api.class);
        TicketParams ticketParams = new TicketParams(targetUrl,title);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code=response.code();
                LogUtils.d(this,"result code ->"+code);
                if (code== HttpURLConnection.HTTP_OK){
                    mTicketResult = response.body();
                    LogUtils.d(TicketActivity.class,"result   ->"+ mTicketResult.toString());
                    //通知UI更新

                    onTicketLoadSuccess();
                }else {
                    //请求失败
                    onLoadeTicketError();
                    mCurrentState =LoadState.ERROR;
                }

            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                //失败
                onLoadeTicketError();

            }
        });

    }

    private void onTicketLoadSuccess() {
        if (mViewCallback != null) {

            mViewCallback.onTicketLoader(mCover, mTicketResult);
        }else{
            mCurrentState =LoadState.SUCCESS;
        }
    }

    private void onLoadeTicketError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        }else{
            mCurrentState =LoadState.ERROR;
        }
    }

    @Override
    public void registerViewCallback(ITicketPagerCallback callback) {
        if (mCurrentState!=LoadState.NONE){
            //s说明状态已经改变了
            //更新UI
            if (mCurrentState==LoadState.SUCCESS){
                onTicketLoadSuccess();
            }else if (mCurrentState==LoadState.ERROR){
                onLoadeTicketError();
            }else if (mCurrentState==LoadState.LOADING){
                onTicketLoading();
            }
        }
        this.mViewCallback=callback;
    }

    private void onTicketLoading() {
        if (mViewCallback!=null){
            mViewCallback.onLoading();
        }else{
            mCurrentState =LoadState.LOADING;
        }
    }

    @Override
    public void unregisterViewCallback(ITicketPagerCallback callback) {
        this.mViewCallback=null;
    }
}
