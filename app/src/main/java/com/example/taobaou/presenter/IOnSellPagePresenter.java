package com.example.taobaou.presenter;

import com.example.taobaou.base.IBasePresenter;
import com.example.taobaou.view.IOnSellPageCallbak;

public interface IOnSellPagePresenter extends IBasePresenter<IOnSellPageCallbak> {
        //加载特惠内容
        void getOnsellContent();
        //重新加载
        //网络不佳时调用
        void reLoad();

    /**
     * 加载更多
     */
    void loadMore();
}
