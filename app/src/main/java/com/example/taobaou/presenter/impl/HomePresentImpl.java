package com.example.taobaou.presenter.impl;
import com.example.taobaou.model.Api;
import com.example.taobaou.model.domain.Categories;
import com.example.taobaou.presenter.IHomePresenter;
import com.example.taobaou.utils.LogUtils;
import com.example.taobaou.utils.RetrofitManager;
import com.example.taobaou.view.IHomeCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresentImpl implements IHomePresenter {
    private IHomeCallback mCallback =null;


    @Override
    public void getCategories() {
        if (mCallback!=null) {
            mCallback.onLoading();
        }
        //加载分类内容
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api=retrofit.create(Api.class);
        Call<Categories> task=api.getCategory();
        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                //结果
                int code = response.code();
                LogUtils.d(HomePresentImpl.this,"code---》"+code);
                if (code== HttpURLConnection.HTTP_OK){
                    //请求成功
                    LogUtils.d(HomePresentImpl.this,"请求成功");
                    Categories categories = response.body();

                    if (mCallback!=null) {
                        if (categories==null||categories.getData().size()==0) {
                            mCallback.onEmpty();
                        }else{
                            mCallback.onCategoriesLoaded(categories);

                        }
                    }

                   //LogUtils.d(HomePresentImpl.this,"categories"+categories.toString());

                }else{
                    //请求失败
                    LogUtils.i(this,"请求失败");
                    if (mCallback!=null) {
                        mCallback.onError();
                    }
                }

            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                //加载失败的结果
                if (mCallback!=null) {
                    mCallback.onError();
                }
                LogUtils.e(this,"请求失败"+t.toString());
            }
        });
    }

    @Override
    public void registerViewCallback(IHomeCallback callback) {
        this.mCallback=callback;
    }

    @Override
    public void unregisterViewCallback(IHomeCallback callback) {
        mCallback=null;
    }
}
