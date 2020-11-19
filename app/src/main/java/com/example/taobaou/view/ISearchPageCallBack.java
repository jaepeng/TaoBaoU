package com.example.taobaou.view;

import com.example.taobaou.base.IBaseCallBack;
import com.example.taobaou.model.domain.SearchRcommend;
import com.example.taobaou.model.domain.SearchResult;

import java.util.List;

public interface ISearchPageCallBack extends IBaseCallBack {
    /**
     * 历史记录加载
     * @param histories
     */
    void onHistoriesLoad(List<String> histories);

    /**
     * 历史记录删除
     */
    void onHistoriesDeleted();


    /**
     * 搜索结果_成功
     * @param result
     */
    void onSearchLoadSuccess(SearchResult result);

    /**
     * 请求更多时网络错误
     */
    void onMoreLoadError();

    /**
     * 没有更多内容了
     */
    void onMoreLoadEmpty();

    /**
     * 获取推荐词
     * @param recommandList
     */
    void onRecommandWordsLoader(List<SearchRcommend.DataBean> recommandList);

    /**
     * 加载更多内容
     * @param moreresult
     */
    void onMoreLoaded(SearchResult moreresult);
}
