package com.example.taobaou.presenter;

import com.example.taobaou.base.IBasePresenter;
import com.example.taobaou.view.ICategoryPagerCallback;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {
    /**
     * 根据ID获取内容
     * @param categoryID
     */
    void getConteantByCategoryId(int categoryID);
    void loadMore(int categoryID);
    void reload(int categoryID);



}
