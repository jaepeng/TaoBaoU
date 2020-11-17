package com.example.taobaou.view;

import com.example.taobaou.base.IBaseCallBack;
import com.example.taobaou.model.domain.OnSellContetn;

public interface IOnSellPageCallbak extends IBaseCallBack {
    /**
     * 加载特惠内容
     * @param result
     */
    void onContentLoadSuccess(OnSellContetn result);

    /**
     * 加载更多的结果
     * @param moreResult
     */
    void onMoreLoaded(OnSellContetn moreResult);

    /**
     * 加载更多失败
     * 不是加载Error,因为只是请求更多时发生错误
     */
    void onMoreLoadError();

    void onMoreLoadEmpty();
}
