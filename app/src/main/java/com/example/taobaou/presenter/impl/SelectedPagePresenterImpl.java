package com.example.taobaou.presenter.impl;

import com.example.taobaou.model.Api;
import com.example.taobaou.model.domain.SelectedContent;
import com.example.taobaou.model.domain.SelectedPageCategory;
import com.example.taobaou.utils.LogUtils;
import com.example.taobaou.utils.RetrofitManager;
import com.example.taobaou.utils.UrlUtils;
import com.example.taobaou.view.ISelctedPageCallBack;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedPagePresenterImpl implements ISelectedPresenter{
    private ISelctedPageCallBack mViewCallBack=null;
    private final Api mApi;
    private SelectedPageCategory.DataBean mCurrentCategoryItem=null;

    public SelectedPagePresenterImpl(){
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }
    @Override
    public void getCategories() {
        if (mViewCallBack!=null){
            mViewCallBack.onLoading();
        }

        //拿到Retrofit

        Call<SelectedPageCategory> task = mApi.getSelcetdPageCategories();
        task.enqueue(new Callback<SelectedPageCategory>() {
            @Override
            public void onResponse(Call<SelectedPageCategory> call, Response<SelectedPageCategory> response) {
                int code=response.code();
                LogUtils.d(SelectedPagePresenterImpl.class,"result code---->"+code);
                if (code== HttpURLConnection.HTTP_OK){
                    SelectedPageCategory result = response.body();
                    LogUtils.d(this,"result  ---->"+result.toString());
                    //todo: 通知精选页面更新
                    if (mViewCallBack!=null){

                        mViewCallBack.onCategoryLoad(result);
                    }

                }else{
                    onLoadError();
                }
            }

            @Override
            public void onFailure(Call<SelectedPageCategory> call, Throwable t) {
                onLoadError();

            }
        });

    }

    private void onLoadError() {
        if (mViewCallBack != null) {

            mViewCallBack.onError();
        }
    }

    @Override
    public void getContentByCategory(SelectedPageCategory.DataBean item) {

        this.mCurrentCategoryItem=item;
        String targetUrl = UrlUtils.getSelectedPgaeContentUrl(item.getFavorites_id());
        Call<SelectedContent> task = mApi.getSelectedContent(targetUrl);
        task.enqueue(new Callback<SelectedContent>() {
            @Override
            public void onResponse(Call<SelectedContent> call, Response<SelectedContent> response) {
                int code=response.code();
                LogUtils.d(SelectedPagePresenterImpl.class,"result code---->"+code);
                if (code== HttpURLConnection.HTTP_OK){
                    SelectedContent result = response.body();
                    LogUtils.d(this,"SelectedContent  result  ---->"+result.toString());
                    //todo: 通知精选页面更新
                    if (mViewCallBack!=null){

                        mViewCallBack.onContentLoad(result);
                    }

                }else{
                    onLoadError();
                }
            }

            @Override
            public void onFailure(Call<SelectedContent> call, Throwable t) {
                onLoadError();
            }
        });

    }

    @Override
    public void reloadContent() {
        if (mCurrentCategoryItem!=null){
            this.getContentByCategory(mCurrentCategoryItem);
        }

    }

    @Override
    public void registerViewCallback(ISelctedPageCallBack callback) {
        this.mViewCallBack=callback;
    }

    @Override
    public void unregisterViewCallback(ISelctedPageCallBack callback) {
        if (mViewCallBack!=null) {
            this.mViewCallBack = null;
        }
    }
}
