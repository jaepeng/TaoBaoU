package com.example.taobaou.presenter.impl;

import com.example.taobaou.model.Api;
import com.example.taobaou.model.domain.OnSellContetn;
import com.example.taobaou.presenter.IOnSellPagePresenter;
import com.example.taobaou.utils.RetrofitManager;
import com.example.taobaou.utils.ToastUtsils;
import com.example.taobaou.utils.UrlUtils;
import com.example.taobaou.view.IOnSellPageCallbak;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OnSellPagePresenterImpl implements IOnSellPagePresenter {
    private static final int DEFAULT_PAGE = 1;
    private int currentPage=DEFAULT_PAGE;
    private IOnSellPageCallbak mOnSellPageCallBack=null;
    private final Api mApi;

    public OnSellPagePresenterImpl(){
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    /**
     * 当前状态
     */
    private boolean misLoading=false;

    @Override
    public void getOnsellContent() {
        if (misLoading){
            return;
        }
        misLoading=true;
        //通知UI状态为加载中
        if (mOnSellPageCallBack!=null){

            mOnSellPageCallBack.onLoading();
        }
        //获取特惠内容

        String url = UrlUtils.getOnSellPageUrl(currentPage);
        Call<OnSellContetn> task = mApi.getOnSellContent(url);
        task.enqueue(new Callback<OnSellContetn>() {
            @Override
            public void onResponse(Call<OnSellContetn> call, Response<OnSellContetn> response) {
                int code = response.code();
                OnSellContetn result = response.body();
                if (code== HttpURLConnection.HTTP_OK){
                    misLoading=false;
                    try {
                        if (isEmpty(result)){
                            onEmpty();
                        }
                        onSuccess(result);

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        onEmpty();
                    }

                }else{
                    onError();

                }
            }

            @Override
            public void onFailure(Call<OnSellContetn> call, Throwable t) {
                onError();
            }
        });

    }
    private void onEmpty(){
        if (mOnSellPageCallBack!=null){
            mOnSellPageCallBack.onEmpty();
        }
    }

    private void onSuccess(OnSellContetn result) {
        if (mOnSellPageCallBack!=null){

            mOnSellPageCallBack.onContentLoadSuccess(result);
        }
    }

    private void onError() {
        misLoading=false;
        if (mOnSellPageCallBack!=null) {
            mOnSellPageCallBack.onError();
        }
    }

    @Override
    public void reLoad() {
        this.getOnsellContent();

    }

    @Override
    public void loadMore() {
        if (misLoading){
            return;
        }
        misLoading=true;
        currentPage++;
        //去加载更多内容
        String targetUrl = UrlUtils.getOnSellPageUrl(currentPage);
        Call<OnSellContetn> task = mApi.getOnSellContent(targetUrl);
        task.enqueue(new Callback<OnSellContetn>() {
            @Override
            public void onResponse(Call<OnSellContetn> call, Response<OnSellContetn> response) {
                int code = response.code();
                OnSellContetn result = response.body();
                if (code== HttpURLConnection.HTTP_OK){
                    misLoading=false;

                    try {
                        if (isEmpty(result)){
                            onEmpty();
                        }
                        onMoreLoad(result);

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        onEmpty();
                    }

                }else{
                    onMoreLoadError();

                }

            }

            @Override
            public void onFailure(Call<OnSellContetn> call, Throwable t) {
                onMoreLoadError();
            }
        });


    }
    private boolean isEmpty(OnSellContetn contetn){
        int size = contetn.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        return size==0;


    }

    private void onMoreLoadError() {
        misLoading=false;
        currentPage--;
        mOnSellPageCallBack.onMoreLoadError();
    }

    /**
     * 加载更多，通知UI更新
     * @param result
     */
    private void onMoreLoad(OnSellContetn result) {
        ToastUtsils.showToast("正在加载中...");
        if (mOnSellPageCallBack!=null){
            if (!isEmpty(result)) {
                mOnSellPageCallBack.onMoreLoaded(result);
            }else{
                mOnSellPageCallBack.onMoreLoadEmpty();
                currentPage--;
            }
        }
        
    }

    @Override
    public void registerViewCallback(IOnSellPageCallbak callback) {
        this.mOnSellPageCallBack=callback;

    }

    @Override
    public void unregisterViewCallback(IOnSellPageCallbak callback) {
       this.mOnSellPageCallBack=null;
    }
}
