package com.example.taobaou.presenter;

import com.example.taobaou.base.IBasePresenter;
import com.example.taobaou.view.ISearchPageCallBack;

public interface ISearchPresenter extends IBasePresenter<ISearchPageCallBack> {
    /**
     * 获取搜索历史
     */
    void getHistories();

    /**
     * 删除搜索历史
     */
    void delHistories();

    /**
     * 根据关键字搜索内容
     * @param keywords
     */
    void doSearch(String keywords);

    void research();

    /**
     * 获取更多内容
     */
    void loaderMore();


    /**
     * 如果没有搜索历史则获取推荐词
     */
    void getRecommendWords();
}
