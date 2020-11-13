package com.example.taobaou.view;

import com.example.taobaou.base.IBaseCallBack;
import com.example.taobaou.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback extends IBaseCallBack {
    /**
     * 数据加载回来
     * @param contents
     */
    void onContentLoaded(List<HomePagerContent.DataBean> contents);

    //加载更多网络错误
    void onLoaderMoreError();

    //没有更多内容
    void onLoaderMoreempty();
    //加载更多内容
    void onLoaderMoreLoaded(List<HomePagerContent.DataBean > contents);
    //加载轮播图
    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);

    int getCategoryId();
}
