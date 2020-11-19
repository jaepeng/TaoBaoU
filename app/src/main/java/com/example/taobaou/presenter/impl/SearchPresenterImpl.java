package com.example.taobaou.presenter.impl;

import com.example.taobaou.model.Api;
import com.example.taobaou.model.domain.Histories;
import com.example.taobaou.model.domain.SearchRcommend;
import com.example.taobaou.model.domain.SearchResult;
import com.example.taobaou.presenter.ISearchPresenter;
import com.example.taobaou.utils.JsonCacheUtil;
import com.example.taobaou.utils.RetrofitManager;
import com.example.taobaou.view.ISearchPageCallBack;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenterImpl implements ISearchPresenter {


    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_HISTOREIS_SIZE = 10;
    private final Api mApi;
    private int currentPage=DEFAULT_PAGE;
    private ISearchPageCallBack mSearchViewCallBack=null;
    private String mCurrentKeyWord=null;
    private final JsonCacheUtil mJsonCacheUtil;

    public SearchPresenterImpl(){
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
        mJsonCacheUtil = JsonCacheUtil.getInstance();
        
    }
    @Override
    public void getHistories() {
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        if (mSearchViewCallBack!=null&&
                histories!=null&&
                histories.getHistories()!=null&&
                histories.getHistories().size()!=0){

            mSearchViewCallBack.onHistoriesLoad(histories.getHistories());
        }


    }

    /**
     * 清除搜索记录
     */
    @Override
    public void delHistories() {

        mJsonCacheUtil.delCache(KEY_HISTORIES);

    }


    public static final String KEY_HISTORIES="key_histories";

    private int mHistoriesMaxSize=DEFAULT_HISTOREIS_SIZE;
    /**
     * 添加历史记录
     * @param history
     */
    public void saveHistory(String history){
        //如果已经存在了，就清除，然后添加
        //对个数进行限制
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        List<String> historiesList=null;
        if (histories!=null&&histories.getHistories()!=null){
            historiesList = histories.getHistories();
            if (historiesList.contains(history)) {
                historiesList.remove(history);
            }
            //去重完成
        }else{
            //添加记录
            if (historiesList == null) {

                historiesList=new ArrayList<>();
            }
            if (histories == null) {
                histories=new Histories();
            }
            histories.setHistories(historiesList);
            //对个数进行限制
            if (historiesList.size()>mHistoriesMaxSize) {
                historiesList=historiesList.subList(0,mHistoriesMaxSize);
            }


            //添加数据
            historiesList.add(history);
            //保存记录
            mJsonCacheUtil.saveCache(KEY_HISTORIES,histories);

        }
    }


    @Override
    public void doSearch(String keywords) {

        if (mCurrentKeyWord==null||!mCurrentKeyWord.equals(keywords)){
            this.saveHistory(keywords);
            this.mCurrentKeyWord=keywords;
        }
        //更新UI状态
        if (mSearchViewCallBack != null) {
            mSearchViewCallBack.onLoading();
        }
        Call<SearchResult> task = mApi.doSearch(currentPage, keywords);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                if (code==HttpURLConnection.HTTP_OK){
                  handleSearchResultr(response.body());
                }else{
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onError();
            }
        });

    }

    private void handleSearchResultr(SearchResult result) {
        if (mSearchViewCallBack != null) {

            if (isResultEmpty(result)){
                //数据为空
                mSearchViewCallBack.onEmpty();

            }else{
                //数据不为空
                mSearchViewCallBack.onSearchLoadSuccess(result);
            }
        }
    }
    private boolean isResultEmpty(SearchResult result){
        try {
            return result==null&&result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size()==0;
        } catch (Exception e) {
            return false;
        }
    }

    private void onError() {
        if (mSearchViewCallBack != null) {
            mSearchViewCallBack.onError();
        }
    }

    @Override
    public void research() {
        if (mCurrentKeyWord==null){
            if (mSearchViewCallBack != null) {

                mSearchViewCallBack.onEmpty();
            }
        }else{
            this.doSearch(mCurrentKeyWord);
        }

    }

    @Override
    public void loaderMore() {
        currentPage++;
        //进行搜索
        if (mCurrentKeyWord==null){
            if (mSearchViewCallBack!=null){

                mSearchViewCallBack.onEmpty();
            }

        }else{
            //做搜索的事情
            doSearchMore();
        }

    }

    private void doSearchMore() {
        Call<SearchResult> task = mApi.doSearch(currentPage, mCurrentKeyWord);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                if (code==HttpURLConnection.HTTP_OK){
                    handleMoreSearchResultr(response.body());
                }else{
                    onLoadMoreErro();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onLoadMoreErro();
            }
        });


    }

    /**
     * 处理搜索更多
     * @param body
     */

    private void handleMoreSearchResultr(SearchResult body) {
        if (isResultEmpty(body)) {
            mSearchViewCallBack.onMoreLoadEmpty();
        }else{
            mSearchViewCallBack.onMoreLoaded(body);
        }
    }

    /**
     * 加载更多失败
     */
    private void onLoadMoreErro() {
        currentPage--;
        if (mSearchViewCallBack != null) {
            mSearchViewCallBack.onMoreLoadError();
        }
    }

    @Override
    public void getRecommendWords() {
        Call<SearchRcommend> task = mApi.getRecommendWords();
        task.enqueue(new Callback<SearchRcommend>() {
            @Override
            public void onResponse(Call<SearchRcommend> call, Response<SearchRcommend> response) {
                int code = response.code();
                if (code== HttpURLConnection.HTTP_OK){
                    //todo:处理结果
                    if (mSearchViewCallBack != null) {
                        mSearchViewCallBack.onRecommandWordsLoader(response.body().getData());
                    }

                }
            }

            @Override
            public void onFailure(Call<SearchRcommend> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    @Override
    public void registerViewCallback(ISearchPageCallBack callback) {
        this.mSearchViewCallBack=callback;
    }

    @Override
    public void unregisterViewCallback(ISearchPageCallBack callback) {
        this.mSearchViewCallBack=null;
    }
}
