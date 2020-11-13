package com.example.taobaou.presenter.impl;

import com.example.taobaou.model.Api;
import com.example.taobaou.model.domain.HomePagerContent;
import com.example.taobaou.presenter.ICategoryPagerPresenter;
import com.example.taobaou.utils.RetrofitManager;
import com.example.taobaou.utils.UrlUtils;
import com.example.taobaou.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagePresenterImp implements ICategoryPagerPresenter {

    private Map<Integer,Integer> pagesInfo=new HashMap<>();
    private static final int DEFAULT_PAGE=1;
    private Integer mCurrentPage;


    @Override
    public void getConteantByCategoryId(int categoryID) {

        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId()==categoryID){
                callback.onLoading();
            }
        }
    //根据ID加载内容
        Integer targetPage = pagesInfo.get(categoryID);
        if (targetPage==null) {
            targetPage=DEFAULT_PAGE;
            pagesInfo.put(categoryID,targetPage);
        }

        Call<HomePagerContent> task = createTask(categoryID, targetPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code=response.code();
                if (code== HttpURLConnection.HTTP_OK){
                    HomePagerContent pagerContent = response.body();
                    //把数据给UI
                    handleHomePageContentResult(pagerContent,categoryID);


                }else{

                    handeleNetworkEroor(categoryID);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                handeleNetworkEroor(categoryID);
//                LogUtils.d(CategoryPagePresenterImp.this,"onFailure---->"+t.toString());
            }
        });
    }

    private Call<HomePagerContent> createTask(int categoryID, Integer targetPage) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api=retrofit.create(Api.class);
        String homePageUrl= UrlUtils.createHomePagerUrl(categoryID,targetPage);
        return api.getHomePageContent(homePageUrl);
    }

    private void handeleNetworkEroor(int categoryID) {

        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId()==categoryID) {

                callback.onError();
            }
        }
    }

    private void handleHomePageContentResult(HomePagerContent pagerContent, int categoryID) {
        //通知UI层更新数据
        List<HomePagerContent.DataBean> data = pagerContent.getData();
        for (ICategoryPagerCallback callback:callbacks){
            if (callback.getCategoryId()==categoryID) {

                if (pagerContent==null|| pagerContent.getData().size()==0) {
                    callback.onEmpty();
                }else{
                    List<HomePagerContent.DataBean> loopData= data.subList(data.size() - 5, data.size());
                    callback.onLooperListLoaded(loopData);
                    callback.onContentLoaded(data);
                }
            }
        }

    }




    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {
    callbacks.remove(callback);
    }

    private ArrayList<ICategoryPagerCallback> callbacks=new ArrayList<>();


    @Override
    public void loadMore(int categoryID) {
        //加载更多数据
        //1.拿到当前页码
        mCurrentPage = pagesInfo.get(categoryID);
        //2.页码增加
        mCurrentPage++;
        pagesInfo.put(categoryID,mCurrentPage);
        //        //3.加载数据
        Call<HomePagerContent> task = createTask(categoryID, mCurrentPage);
        //4.处理数据结果
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                //结果:
                int code = response.code();
                if (code== HttpURLConnection.HTTP_OK) {
                    HomePagerContent result = response.body();
                    handleLoaderResult(result,categoryID);
                }else{
                    handleLoadMoreError(categoryID);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
             //请求失败
            handleLoadMoreError(categoryID);
            }
        });


    }

    private void handleLoaderResult(HomePagerContent result, int categoryID) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId()==categoryID) {
                if (result==null||result.getData().size()==0) {
                    callback.onLoaderMoreempty();
                }else{
                    callback.onLoaderMoreLoaded(result.getData());
                }
            }
        }
    }

    private void handleLoadMoreError(int categoryID) {
        mCurrentPage--;
        pagesInfo.put(categoryID,mCurrentPage);
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId()==categoryID) {
                callback.onLoaderMoreError();
            }
        }
    }

    @Override
    public void reload(int categoryID) {

    }


}
